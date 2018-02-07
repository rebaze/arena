package org.cloudarena.docker.core;

import org.cloudarena.docker.api.DockerContainerDependencyInstaller;
import org.cloudarena.docker.api.TypedDockerInstaller;

public final class MariaDbServiceInstaller implements TypedDockerInstaller
{
    @Override
    public void install(DockerContainerDependencyInstaller docker) {
        docker.image("mariadb:latest");
    }
}
