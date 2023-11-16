import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import se.inera.intyg.intygsadmin.build.Config.Dependencies

plugins {
  application
}

apply<DependencyManagementPlugin>()

tasks {
  startScripts {
    mainClass.set("liquibase.integration.commandline.LiquibaseCommandLine --driver=com.mysql.cj.jdbc.Driver " +
        "--changeLogFile=changelog/db-changelog-master.xml")
    defaultJvmOpts = listOf("-Dfile.encoding=utf8", "-Dliquibase.headless=true")
  }
}

sonar { isSkipProject = true }

val liquibaseVersion = dependencyManagement.managedVersions["org.liquibase:liquibase-core"]

dependencies {
  implementation("info.picocli:picocli:${Dependencies.picocliVersion}")
  runtimeOnly("com.mysql:mysql-connector-j")
  runtimeOnly("org.liquibase:liquibase-core")
  runtimeOnly("org.liquibase.ext:liquibase-hibernate5:$liquibaseVersion")
  runtimeOnly(project(":${rootProject.name}-persistence"))
}

val buildDirectory: File = project.layout.buildDirectory.get().asFile
val liquibaseRunnerFileTar = file("${buildDirectory}/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.tar")
val liquibaseRunnerFileZip = file("${buildDirectory}/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.zip")

val liquibaseRunnerTar = artifacts.add("archives", liquibaseRunnerFileTar) {
  type = "tar"
  builtBy("distTar")
}

val liquibaseRunnerZip = artifacts.add("archives", liquibaseRunnerFileZip) {
  type = "zip"
  builtBy("distZip")
}
