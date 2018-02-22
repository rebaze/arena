package org.cloudarena.junit.core;

import org.cloudarena.api.DependencyService;
import org.cloudarena.docker.api.DockerContainerDependencyInstaller;
import org.cloudarena.docker.api.TypedDockerInstaller;
import org.cloudarena.docker.core.DockerImageDependency;
import org.cloudarena.junit.discovery.ScenarioDiscovery;
import org.junit.platform.engine.*;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;

public class SimpleEngine implements TestEngine
{

    @Override
    public String getId()
    {
        return "junit-arena";
    }

    @Override
    public TestDescriptor discover( EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId )
    {
        ArenaEngineDescriptor engineDescriptor = new ArenaEngineDescriptor( uniqueId );
        new ScenarioDiscovery().resolveSelectors( discoveryRequest, engineDescriptor );
        return engineDescriptor;
    }

    @Override
    public void execute( ExecutionRequest request )
    {
        executeStyles( request, request.getRootTestDescriptor() );
    }

    private void executeStyles( ExecutionRequest request, TestDescriptor test )
    {
        request.getEngineExecutionListener().executionStarted( test );
        if ( test instanceof ScenarioTestDescriptor )
        {
            runScenario( request,  (ScenarioTestDescriptor ) test );
        }
        else if ( test instanceof DeploymentTestDescriptor )
        {
            runDeployment( request, ( DeploymentTestDescriptor ) test );
        }
        else if ( test instanceof ScenarioDependencyDescriptor )
        {
            runDependency( request, ( ScenarioDependencyDescriptor ) test );
        } else {
            for ( TestDescriptor t : test.getChildren() ) {
                executeStyles( request,t );
            }
        }
        request.getEngineExecutionListener().executionFinished( test,TestExecutionResult.successful() );

    }
    //
    private void runDependency( ExecutionRequest request, ScenarioDependencyDescriptor dependency )
    {
        try
        {
            request.getEngineExecutionListener().executionStarted( dependency );
            Object arena = dependency.getJavaClass().newInstance();
            dependency.getJavaMethod().setAccessible( true );
            Class<?>[] paramTypes = dependency.getJavaMethod().getParameterTypes();
            if (paramTypes.length != 1) {
                throw new RuntimeException("Dependency must have exactly one argument.");
            }
//                    if (paramTypes[0].isAssignableFrom( DockerContainerDependencyInstaller.class )) {

            Class<?> type = paramTypes[0];

            DockerContainerDependencyInstaller installer = new DockerImageDependency();
            DependencyService service = ( DependencyService )installer;
            if (DockerContainerDependencyInstaller.class.isAssignableFrom( type )) {
                dependency.getJavaMethod().invoke( arena, installer );
                // should be ready to use now:
            }else if (TypedDockerInstaller.class.isAssignableFrom( type )) {
                TypedDockerInstaller typedInstaller = (TypedDockerInstaller) type.newInstance();
                typedInstaller.install(installer);

            }else {
                throw new RuntimeException("Dependency injection other than single docker not supported yet.");

            }
            handleDependency( request, dependency, service );
            request.getEngineExecutionListener()
                .executionFinished( dependency, TestExecutionResult.successful() );

        }
        catch ( Exception e )
        {
            request.getEngineExecutionListener()
                .executionFinished( dependency, TestExecutionResult.failed( e ) );
        }
    }

    private void runDeployment( ExecutionRequest request, DeploymentTestDescriptor test )
    {
        request.getEngineExecutionListener().executionStarted( test );
        request.getEngineExecutionListener()
            .executionFinished( test, TestExecutionResult.successful() );
    }

    private void runScenario( ExecutionRequest request, ScenarioTestDescriptor test )
    {
        try
        {
            TreeSession session = new DefaultTreeSessionFactory().create();
            // Add Candidate and Dependency to tree
            Tree result = session.createTreeBuilder()
                //.branch(deployment.tree()) // add the deployment itself first
                .seal();
            executeArena( request, test );
        }
        catch ( Exception e )
        {
            request.getEngineExecutionListener()
                .executionFinished( test, TestExecutionResult.failed( e ) );
        }
    }

    private void executeArena( ExecutionRequest request, ScenarioTestDescriptor stest )
        throws InstantiationException, IllegalAccessException
    {
        request.getEngineExecutionListener().executionStarted( stest );

        Object arena = stest.getJavaClass().newInstance();
        stest.getJavaMethod().setAccessible( true );
        try
        {
            // only run the arena without deps for now:

            stest.getJavaMethod().invoke( arena );
            request.getEngineExecutionListener()
                .executionFinished( stest, TestExecutionResult.successful() );
        }
        catch ( Exception e )
        {
            request.getEngineExecutionListener()
                .executionFinished( stest, TestExecutionResult.failed( e ) );
        }
    }

    private void handleDependency( ExecutionRequest request, ScenarioDependencyDescriptor test,
        DependencyService service )
    {
        try
        {
            service.init();
            service.start();
            service.stop();
        }
        finally
        {
            service.destroy();
        }
    }
}
