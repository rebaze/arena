package org.cloudarena.core.junit;

import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

import java.util.Optional;

public class Engine extends HierarchicalTestEngine<MicrosdkEngineExecutionContext> {

    public static final String ENGINE_ID = "org.cloudarena";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    /**
     * Returns {@code org.junit.jupiter} as the group ID.
     */
    @Override
    public Optional<String> getGroupId() {
        return Optional.of("org.cloudarena");
    }

    /**
     * Returns {@code junit-jupiter-engine} as the artifact ID.
     */
    @Override
    public Optional<String> getArtifactId() {
        return Optional.of("junit-jupiter-engine");
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        MicrosdkEngineDescriptor engineDescriptor = new MicrosdkEngineDescriptor(uniqueId);
        new ScenarioDiscovery().resolveSelectors(discoveryRequest, engineDescriptor);
        return engineDescriptor;
    }

    @Override
    protected MicrosdkEngineExecutionContext createExecutionContext(ExecutionRequest request) {
        return new MicrosdkEngineExecutionContext(request.getEngineExecutionListener(),
                request.getConfigurationParameters());
    }
}
