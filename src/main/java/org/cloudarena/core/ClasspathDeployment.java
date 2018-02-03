package org.cloudarena.core;

import com.google.common.io.ByteStreams;
import org.cloudarena.api.TestSubject;
import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClasspathDeployment implements TestSubject
{

    public final static Selector DATA = Selector.selector( "data" );

    private final Tree tree;

    public ClasspathDeployment( Tree tree )
    {
        this.tree = tree;
    }

    public static ClasspathDeployment.Builder builder()
    {
        return new ClasspathDeployment.Builder();
    }

    @Override
    public Tree tree()
    {
        return tree;
    }

    public static class Builder
    {

        private List<Class<?>> item = new ArrayList<>();

        public ClasspathDeployment.Builder gav( String gav )
        {
            //this.item.add(clazz);
            return this;
        }

        public TestSubject build()
        {
            try
            {
                // Wrap all of it up on the fly..
                // Lets assume we only need the pure tree for now
                TreeSession session = new DefaultTreeSessionFactory().create();
                TreeBuilder builder = session.createTreeBuilder();
                for ( Class<?> clazz : item )
                {
                    URL loadedClazz = clazz
                        .getResource( "/" + clazz.getName().replace( ".", "/" ) + ".class" );
                    try ( InputStream inp = loadedClazz.openStream() )
                    {
                        builder.branch( DATA ).branch( clazz.getName() )
                            .add( ByteStreams.toByteArray( inp ) );
                    }
                }
                return new ClasspathDeployment( builder.seal() );
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        }
    }

}
