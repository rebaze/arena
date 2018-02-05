package org.cloudarena.junit.core;

import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;

public class ScenarioDependencyDescriptor extends AbstractTestDescriptor
{

    private final Class<?> javaClass;
    private final Method javaMethod;

    public ScenarioDependencyDescriptor( UniqueId uniqueId, Class<?> javaClass, Method m )
    {
        super( uniqueId.append( "dependency", javaClass.getName() ),
            "Verifying external dependency " + javaClass.getSimpleName() + ":" + m.getName(),
            MethodSource.from( javaClass.getName(), m.getName() ) );
        this.javaClass = javaClass;
        this.javaMethod = m;

    }

    public Class<?> getJavaClass()
    {
        return javaClass;
    }

    public Method getJavaMethod()
    {
        return javaMethod;
    }

    @Override
    public TestDescriptor.Type getType()
    {
        return TestDescriptor.Type.TEST;
    }
}

