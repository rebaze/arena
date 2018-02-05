package org.cloudarena.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A modern jupter based test.
 */
class StandardJupiterTests
{
    @Test void testSuccess()
    {
        // YES!
    }

    @Test void testMe()
    {
        fail( "failme" );
    }
}
