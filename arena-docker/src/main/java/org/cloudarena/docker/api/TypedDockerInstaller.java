package org.cloudarena.docker.api;

public interface TypedDockerInstaller {
    void install(DockerContainerDependencyInstaller docker);
}
