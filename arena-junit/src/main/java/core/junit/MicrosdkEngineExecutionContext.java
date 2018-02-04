package org.cloudarena.core.junit;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;

public class MicrosdkEngineExecutionContext implements EngineExecutionContext
{
    private final ConfigurationParameters configurationParameters;
    private final EngineExecutionListener engineExecutionListener;

    public MicrosdkEngineExecutionContext( EngineExecutionListener engineExecutionListener,
        ConfigurationParameters configurationParameters )
    {
        this.engineExecutionListener = engineExecutionListener;
        this.configurationParameters = configurationParameters;
    }
}
