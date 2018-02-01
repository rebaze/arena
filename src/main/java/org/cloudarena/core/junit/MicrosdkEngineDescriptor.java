package org.cloudarena.core.junit;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public class MicrosdkEngineDescriptor extends EngineDescriptor implements Node<MicrosdkEngineExecutionContext> {

    public MicrosdkEngineDescriptor(UniqueId uniqueId) {
        super(uniqueId,"Arena Arena Engine");

    }

    @Override
    public MicrosdkEngineExecutionContext prepare(MicrosdkEngineExecutionContext context) {
        return context;
    }

    @Override
    public void cleanUp(MicrosdkEngineExecutionContext context) throws Exception {

    }
}
