package org.cloudarena.junit.api;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DependencyService can be data but can also be a service that is expected to be running.
 * Only meta-data is included into the Tree of the Plan output.
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Testable
public @interface Dependency
{
}
