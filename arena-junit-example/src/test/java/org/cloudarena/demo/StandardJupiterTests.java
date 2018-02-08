package org.cloudarena.demo;

import org.cloudarena.junit.api.Arena;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.platform.engine.TestEngine;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.platform.commons.util.ClassLoaderUtils.getDefaultClassLoader;

/**
 * A modern jupter based test.
 */
class StandardJupiterTests
{
    @Test void testSuccess()
    {
        // YES!
    }

    @Test
    void testMe()
    {
        List<TestEngine> engines = ( List<TestEngine> ) CollectionUtils.toStream(
            ServiceLoader.load( TestEngine.class,
                getDefaultClassLoader() ) ).collect( Collectors.toList() );

        assertEquals( 2, engines.size() );
        for ( TestEngine engine : engines )
        {
            //System.out.println( "Engine Y : " + engine.getId() );
        }

        //fail( "failme" );
    }
}
