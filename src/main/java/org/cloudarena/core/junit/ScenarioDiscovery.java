package org.cloudarena.core.junit;

import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.cloudarena.api.Dependency;
import org.cloudarena.api.Candidate;
import org.cloudarena.api.Plan;
import org.cloudarena.api.Arena;

import java.lang.reflect.Method;
import java.util.List;

public class ScenarioDiscovery {
    public void resolveSelectors(EngineDiscoveryRequest discoveryRequest, MicrosdkEngineDescriptor engineDescriptor) {
        // just assume that annotating the class with Arena is fine for now:
        List<ClassSelector> classes = discoveryRequest.getSelectorsByType(ClassSelector.class);
        for (ClassSelector clazz : classes) {
            if (clazz.getJavaClass().isAnnotationPresent(Arena.class)) {
                // discover the plan:
                for (Method m : clazz.getJavaClass().getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Plan.class)) {
                        engineDescriptor.addChild(new ScenarioTestDescriptor(engineDescriptor.getUniqueId(),clazz.getJavaClass(),m));
                    }
                }
            }
        }

        List<MethodSelector> methods = discoveryRequest.getSelectorsByType(MethodSelector.class);
        for (MethodSelector m : methods) {
            if (m.getJavaMethod().isAnnotationPresent(Candidate.class)) {
                engineDescriptor.addChild(new DeploymentTestDescriptor(engineDescriptor.getUniqueId(),m.getJavaClass(),m.getJavaMethod()));
            }

            if (m.getJavaMethod().isAnnotationPresent(Plan.class)) {
                engineDescriptor.addChild(new ScenarioTestDescriptor(engineDescriptor.getUniqueId(),m.getJavaClass(),m.getJavaMethod()));
            }

            if (m.getJavaMethod().isAnnotationPresent(Dependency.class)) {
                engineDescriptor.addChild(new ScenarioDependencyDescriptor(engineDescriptor.getUniqueId(),m.getJavaClass(),m.getJavaMethod()));
            }
        }

    }
}
