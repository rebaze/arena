package org.microsdk.api;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Deployments are always static code (aka data).
 * Deployments are included into the result of a Plan.
 *
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Testable
public @interface Candidate {
}
