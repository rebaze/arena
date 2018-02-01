package com.user.test;

import com.acme.osgi.MyComponent;
import org.microsdk.api.*;
import org.microsdk.api.DependencyService;
import org.microsdk.core.*;
import org.microsdk.core.docker.DockerImageDependency;

import java.io.IOException;

import static org.microsdk.core.docker.DockerImageDependency.docker;
import static org.microsdk.core.docker.DockerImageDependency.mariaDB;

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

    }
}
