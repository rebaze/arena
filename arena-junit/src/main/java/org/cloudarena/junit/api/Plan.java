package org.cloudarena.junit.api;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Testable
public @interface Plan
{
}
