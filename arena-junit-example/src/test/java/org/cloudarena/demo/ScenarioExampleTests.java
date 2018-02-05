package org.cloudarena.demo;

import org.cloudarena.api.TestSubject;
import org.cloudarena.docker.api.DockerContainerDependencyInstaller;
import org.cloudarena.docker.core.MariaDbServiceInstaller;
import org.cloudarena.junit.api.Arena;
import org.cloudarena.junit.api.Candidate;
import org.cloudarena.junit.api.Dependency;
import org.cloudarena.junit.api.Plan;
import org.cloudarena.junit.core.AdhocDeployment;
import org.cloudarena.junit.core.ClasspathDeployment;

import static org.assertj.core.api.Assertions.fail;

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
    void mariadb(DockerContainerDependencyInstaller docker)
    {
        docker.image( "mariadb:latest" );
    }

    @Dependency
    void mariadbService(MariaDbServiceInstaller mariaDB) { }

    // is this a blackbox or a whitebox test.

    @Plan
    void scene()
    {
        fail( "i want to fail" );
    }
}
