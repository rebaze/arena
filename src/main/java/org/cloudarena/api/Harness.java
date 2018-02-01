package org.cloudarena.api;

import java.util.function.Consumer;

public interface Harness {

    Harness with(TestSubject deployment);

    Harness configure();

    <T> TestExecutionReceipt<T> execute(Consumer<Context> consumer);
}
