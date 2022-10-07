import se.inera.intyg.intygsadmin.build.Config.Dependencies

plugins {
  application
}

apply<io.spring.gradle.dependencymanagement.DependencyManagementPlugin>()

tasks {
  startScripts {
    mainClassName = "liquibase.integration.commandline.LiquibaseCommandLine --driver=com.mysql.cj.jdbc.Driver " +
        "--changeLogFile=changelog/db-changelog-master.xml"
    defaultJvmOpts = listOf("-Dfile.encoding=utf8", "-Dliquibase.headless=true")
  }
}

sonarqube {
  setSkipProject(true)
}

val liquibaseVersion = dependencyManagement.managedVersions["org.liquibase:liquibase-core"]

dependencies {
  implementation("info.picocli:picocli:${Dependencies.picocliVersion}")
  runtimeOnly("mysql:mysql-connector-java")
  runtimeOnly("org.liquibase:liquibase-core")
  runtimeOnly("org.liquibase.ext:liquibase-hibernate5:$liquibaseVersion")
  runtimeOnly(project(":${rootProject.name}-persistence"))
}

val liquibaseRunnerFileTar = file("$buildDir/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.tar")
val liquibaseRunnerTar = artifacts.add("archives", liquibaseRunnerFileTar) {
  type = "tar"
  builtBy("distTar")
}

val liquibaseRunnerFileZip = file("$buildDir/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.zip")
val liquibaseRunnerZip = artifacts.add("archives", liquibaseRunnerFileZip) {
  type = "zip"
  builtBy("distZip")
}

publishing {
  publications {
    create<MavenPublication>("liquibase") {
      artifact(liquibaseRunnerZip)
      artifact(liquibaseRunnerTar)
    }
  }
}
