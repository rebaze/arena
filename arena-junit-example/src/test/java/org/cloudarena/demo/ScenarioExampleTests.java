package org.cloudarena.demo;

import org.cloudarena.api.*;
import org.cloudarena.docker.core.DockerImageDependency;
import org.cloudarena.junit.api.Arena;
import org.cloudarena.junit.api.Candidate;
import org.cloudarena.junit.api.Dependency;
import org.cloudarena.junit.api.Plan;
import org.cloudarena.junit.core.AdhocDeployment;
import org.cloudarena.junit.core.ClasspathDeployment;

import static org.assertj.core.api.Assertions.fail;
import static org.cloudarena.docker.core.DockerImageDependency.docker;
import static org.cloudarena.docker.core.DockerImageDependency.mariaDB;

@Arena
public class ScenarioExampleTests
{

    @Candidate
    TestSubject adhocDeployment()
    {
        return AdhocDeployment.builder()
            .add( MyComponent.class )
            .build();
    }

    @Candidate
    TestSubject fromGradleClassPath()
    {
        return ClasspathDeployment.builder().gav( "org.rebaze.integrity:org.rebaze.integrity.tree" )
            .build();
    }

    @Dependency
    DockerImageDependency mariadb()
    {
        return docker( "mariadb:latest" );
    }

    @Dependency
    DependencyService mariadbService()
    {
        return mariaDB();
    }

    // is this a blackbox or a whitebox test.

    @Plan
    void scene()
    {
        fail( "i want to fail" );
    }
}
