package org.cloudarena.junit.discovery;

import org.cloudarena.junit.api.Arena;
import org.cloudarena.junit.api.Candidate;
import org.cloudarena.junit.api.Dependency;
import org.cloudarena.junit.api.Plan;
import org.cloudarena.junit.core.ArenaEngineDescriptor;
import org.cloudarena.junit.core.DeploymentTestDescriptor;
import org.cloudarena.junit.core.ScenarioDependencyDescriptor;
import org.cloudarena.junit.core.ScenarioTestDescriptor;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ClassLoaderUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.*;

import java.lang.reflect.Method;
import java.util.List;

public class ScenarioDiscovery
{
    public void resolveSelectors( EngineDiscoveryRequest discoveryRequest,
        ArenaEngineDescriptor engineDescriptor )
    {
        // just assume that annotating the class with Arena is fine for now:
       // discoveryRequest.
        System.out.println("Discover now: " + discoveryRequest);
        System.out.println("Filter Class   : " + discoveryRequest.getFiltersByType( ClassNameFilter.class ));
        System.out.println("Filter Package : " + discoveryRequest.getFiltersByType( PackageNameFilter.class ));

        List<ClasspathRootSelector> roots = discoveryRequest.getSelectorsByType( ClasspathRootSelector.class );
        ClasspathScanner classpathScanner = new ClasspathScanner(
            ClassLoaderUtils::getDefaultClassLoader, ReflectionUtils::loadClass);

        for ( ClasspathRootSelector root : roots ) {
            List<Class<?>> scanned = classpathScanner.scanForClassesInClasspathRoot( root.getClasspathRoot(), ClassFilter.of(clazz -> true) );
            for ( Class<?> clazz : scanned){
                // use that:
                if ( clazz.isAnnotationPresent( Arena.class ) )
                {
                    for ( Method m : clazz.getDeclaredMethods() )
                    {
                        if ( m.isAnnotationPresent( Plan.class ) )
                        {
                            engineDescriptor.addChild(
                                new ScenarioTestDescriptor( engineDescriptor.getUniqueId(),
                                    clazz, m ) );
                        }
                    }
                }
            }
            // scan classes from here..
        }

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
                        engineDescriptor.addChild(
                            new ScenarioTestDescriptor( engineDescriptor.getUniqueId(),
                                clazz.getJavaClass(), m ) );
                    }
                }
            }
        }

        List<MethodSelector> methods = discoveryRequest.getSelectorsByType( MethodSelector.class );
        for ( MethodSelector m : methods )
        {
            System.out.println("Testing for Arena : " + m.getClassName() + "." + m.getMethodName());

            if ( m.getJavaMethod().isAnnotationPresent( Candidate.class ) )
            {
                engineDescriptor.addChild(
                    new DeploymentTestDescriptor( engineDescriptor.getUniqueId(), m.getJavaClass(),
                        m.getJavaMethod() ) );
            }

            if ( m.getJavaMethod().isAnnotationPresent( Plan.class ) )
            {
                engineDescriptor.addChild(
                    new ScenarioTestDescriptor( engineDescriptor.getUniqueId(), m.getJavaClass(),
                        m.getJavaMethod() ) );
            }

            if ( m.getJavaMethod().isAnnotationPresent( Dependency.class ) )
            {
                engineDescriptor.addChild(
                    new ScenarioDependencyDescriptor( engineDescriptor.getUniqueId(),
                        m.getJavaClass(), m.getJavaMethod() ) );
            }
        }
        System.out.println("Arena Tests found: " + engineDescriptor.getChildren().size());

    }
}
