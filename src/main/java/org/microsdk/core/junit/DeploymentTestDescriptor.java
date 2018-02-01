package org.microsdk.core.junit;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;

public class DeploymentTestDescriptor extends AbstractTestDescriptor {
    private final Class<?> javaClass;
    private final Method javaMethod;

    public DeploymentTestDescriptor(UniqueId uniqueId, Class<?> javaClass, Method m) {
        super(uniqueId.append("deployment",javaClass.getName()),"Candidate: " + javaClass.getName(),MethodSource.from(javaClass.getName(),m.getName()));
        this.javaClass = javaClass;
        this.javaMethod = m;
    }


    public Class<?> getJavaClass() {
        return javaClass;
    }

    public Method getJavaMethod() {
        return javaMethod;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }
}
