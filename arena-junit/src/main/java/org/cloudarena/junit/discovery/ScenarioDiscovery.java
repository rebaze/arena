package org.cloudarena.junit.discovery;

import org.cloudarena.junit.api.Arena;
import org.cloudarena.junit.api.Candidate;
import org.cloudarena.junit.api.Dependency;
import org.cloudarena.junit.api.Plan;
import org.cloudarena.junit.core.*;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ClassLoaderUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.discovery.*;

import java.lang.reflect.Method;
import java.util.List;

public class ScenarioDiscovery
{
    public void resolveSelectors( EngineDiscoveryRequest discoveryRequest,
        ArenaEngineDescriptor engineDescriptor )
    {
        ArenaContainerTestDescriptor desc = new ArenaContainerTestDescriptor(engineDescriptor.getUniqueId());
        engineDescriptor.addChild( desc );

        scanByClasspathRootSelector( discoveryRequest, desc );
        scanByClassSelector( discoveryRequest, desc );
        scanByMethodSelector( discoveryRequest, desc );

        System.out.println("Arena Tests found: " + desc.getChildren().size());

    }

    private void scanByClasspathRootSelector( EngineDiscoveryRequest discoveryRequest,
        TestDescriptor desc )
    {
        List<ClasspathRootSelector> roots = discoveryRequest.getSelectorsByType( ClasspathRootSelector.class );
        ClasspathScanner classpathScanner = new ClasspathScanner(
            ClassLoaderUtils::getDefaultClassLoader, ReflectionUtils::loadClass);

        for ( ClasspathRootSelector root : roots ) {
            List<Class<?>> scanned = classpathScanner.scanForClassesInClasspathRoot( root.getClasspathRoot(), ClassFilter
                .of(clazz -> true) );
            for ( Class<?> clazz : scanned){
                // use that:
                if ( clazz.isAnnotationPresent( Arena.class ) )
                {
                    for ( Method m : clazz.getDeclaredMethods() )
                    {
                        if ( m.isAnnotationPresent( Plan.class ) )
                        {
                            desc.addChild(
                                new ScenarioTestDescriptor( desc.getUniqueId(),
                                    clazz, m ) );
                        }
                    }
                }
            }
            // scan classes from here..
        }
    }

    private void scanByClassSelector( EngineDiscoveryRequest discoveryRequest, TestDescriptor desc )
    {
        List<ClassSelector> classes = discoveryRequest.getSelectorsByType( ClassSelector.class );
        for ( ClassSelector clazz : classes )
        {
            if ( clazz.getJavaClass().isAnnotationPresent( Arena.class ) )
            {
                // discover the plan:
                for ( Method m : clazz.getJavaClass().getDeclaredMethods() )
                {
                    if ( m.isAnnotationPresent( Plan.class ) )
                    {
                        desc.addChild(
                            new ScenarioTestDescriptor( desc.getUniqueId(),
                                clazz.getJavaClass(), m ) );
                    }
                }
            }
        }
    }

    private void scanByMethodSelector( EngineDiscoveryRequest discoveryRequest,
        TestDescriptor desc )
    {
        List<MethodSelector> methods = discoveryRequest.getSelectorsByType( MethodSelector.class );
        for ( MethodSelector m : methods )
        {
            System.out.println("Testing for Arena : " + m.getClassName() + "." + m.getMethodName());

            if ( m.getJavaMethod().isAnnotationPresent( Candidate.class ) )
            {
                desc.addChild(
                    new DeploymentTestDescriptor( desc.getUniqueId(), m.getJavaClass(),
                        m.getJavaMethod() ) );
            }

            if ( m.getJavaMethod().isAnnotationPresent( Plan.class ) )
            {
                desc.addChild(
                    new ScenarioTestDescriptor( desc.getUniqueId(), m.getJavaClass(),
                        m.getJavaMethod() ) );
            }

            if ( m.getJavaMethod().isAnnotationPresent( Dependency.class ) )
            {
                desc.addChild(
                    new ScenarioDependencyDescriptor( desc.getUniqueId(),
                        m.getJavaClass(), m.getJavaMethod() ) );
            }
        }
    }
}
