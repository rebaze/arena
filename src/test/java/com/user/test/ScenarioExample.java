package com.user.test;

import org.cloudarena.api.*;
import org.cloudarena.api.DependencyService;
import org.cloudarena.core.*;
import org.cloudarena.core.docker.DockerImageDependency;

import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;
import static org.cloudarena.core.docker.DockerImageDependency.docker;
import static org.cloudarena.core.docker.DockerImageDependency.mariaDB;

@Arena
public class ScenarioExample {

    @Candidate
    TestSubject adhocDeployment() throws IOException {
        return AdhocDeployment.builder()
                .add(MyComponent.class)
                .build();
    }

    @Candidate
    TestSubject fromGradleClassPath() {
        return ClasspathDeployment.builder().gav("org.rebaze.integrity:org.rebaze.integrity.tree").build();
    }

    @Dependency
    DockerImageDependency mariadb() {
        return docker("mariadb:latest");
    }

    @Dependency
    DependencyService mariadbService() {
        return mariaDB();
    }

    @Plan
    void scene( ) {
        fail("i want to fail");
    }
}
