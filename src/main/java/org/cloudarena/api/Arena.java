package org.cloudarena.api;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Testable
public @interface Arena {
}
