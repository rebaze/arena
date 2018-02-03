package org.cloudarena.core.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import org.cloudarena.api.DependencyService;

public class DockerImageDependency implements DependencyService
{

    private final String image;
    private ContainerInfo info;
    private DockerClient docker;

    public DockerImageDependency( String image )
    {
        this.image = image;
    }

    public static DependencyService mariaDB()
    {
        return new DockerImageDependency( "mariadb:latest" );
    }

    public static DockerImageDependency docker( String image )
    {
        return new DockerImageDependency( image );
    }

    private void closeDocker( DockerClient docker )
    {
        try
        {
            if ( docker != null )
            {
                docker.close();
            }
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
        }
    }

    @Override
    public void init()
    {

        try
        {
            docker = DefaultDockerClient.fromEnv().build();
            if ( docker.listImages( DockerClient.ListImagesParam.byName( image ) ).isEmpty() )
            {
                docker.pull( image );
            }

            /**
             final String[] ports = {"80", "22"};
             final Map<String, List<PortBinding>> portBindings = new HashMap<>();
             for (String port : ports) {
             List<PortBinding> hostPorts = new ArrayList<>();
             hostPorts.add(PortBinding.of("0.0.0.0", port));
             portBindings.put(port, hostPorts);
             }
             List<PortBinding> randomPort = new ArrayList<>();
             randomPort.add(PortBinding.randomPort("0.0.0.0"));
             portBindings.put("443", randomPort);

             final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
             **/

            final ContainerConfig containerConfig = ContainerConfig.builder()
                .image( image ) //.exposedPorts(ports)
                .build();

            final ContainerCreation creation = docker.createContainer( containerConfig );
            String id = creation.id();

            this.info = docker.inspectContainer( id );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void start()
    {
        try
        {
            docker.startContainer( info.id() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void stop()
    {
        try
        {
            docker.stopContainer( info.id(), 5 );
            docker.waitContainer( info.id() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void destroy()
    {
        try
        {
            docker.removeContainer( info.id() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            docker.close();
        }
    }
}
