package org.cloudarena.junit.core;

import org.cloudarena.api.DependencyService;
import org.junit.platform.engine.*;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;

public class SimpleEngine implements TestEngine
{

    @Override
    public String getId()
    {
        return "org.cloudarena.junit";
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
        TestDescriptor desc = request.getRootTestDescriptor();

        for ( TestDescriptor test : desc.getChildren() )
        {
            // lets just create an instance and go with it

            if ( test instanceof ScenarioTestDescriptor )
            {
                ScenarioTestDescriptor stest = ( ScenarioTestDescriptor ) test;
                try
                {
                    TreeSession session = new DefaultTreeSessionFactory().create();
                    // Add Candidate and Dependency to tree
                    Tree result = session.createTreeBuilder()
                        //.branch(deployment.tree()) // add the deployment itself first
                        .seal();
                    request.getEngineExecutionListener().executionStarted( test );
                    executeArena( request, stest );
                    request.getEngineExecutionListener()
                        .executionFinished( test, TestExecutionResult.successful() );
                }
                catch ( Exception e )
                {
                    request.getEngineExecutionListener()
                        .executionFinished( test, TestExecutionResult.failed( e ) );
                }

            }
            else if ( test instanceof DeploymentTestDescriptor )
            {
                DeploymentTestDescriptor stest = ( DeploymentTestDescriptor ) test;
                // we assume that to be successful
                request.getEngineExecutionListener().executionStarted( test );
                request.getEngineExecutionListener()
                    .executionFinished( test, TestExecutionResult.successful() );

            }
            else if ( test instanceof ScenarioDependencyDescriptor )
            {
                ScenarioDependencyDescriptor dependency = ( ScenarioDependencyDescriptor ) test;
                try
                {
                    request.getEngineExecutionListener().executionStarted( test );
                    Object arena = dependency.getJavaClass().newInstance();
                    dependency.getJavaMethod().setAccessible( true );
                    DependencyService service = ( DependencyService ) dependency.getJavaMethod()
                        .invoke( arena );
                    handleDependency( request, dependency, service );
                }
                catch ( Exception e )
                {
                    request.getEngineExecutionListener()
                        .executionFinished( test, TestExecutionResult.failed( e ) );
                }

            }
        }
    }

    private void executeArena( ExecutionRequest request, ScenarioTestDescriptor stest )
        throws InstantiationException, IllegalAccessException
    {
        Object arena = stest.getJavaClass().newInstance();
        stest.getJavaMethod().setAccessible( true );
        try
        {
            // only run the arena without deps for now:

            stest.getJavaMethod().invoke( arena );
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
        request.getEngineExecutionListener()
            .executionFinished( test, TestExecutionResult.successful() );
    }
}
