package org.cloudarena.junit.core;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

public class ArenaContainerTestDescriptor extends AbstractTestDescriptor
{

    public ArenaContainerTestDescriptor( UniqueId parent )
    {
        super( parent.append( "Arena","Arena" ), "Arena Test Engine");
    }

    @Override
    public Type getType()
    {
        return Type.CONTAINER;
    }
}
