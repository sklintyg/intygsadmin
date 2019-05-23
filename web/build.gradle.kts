
import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import se.inera.intyg.intygsadmin.build.Config.Dependencies
import se.inera.intyg.intygsadmin.build.Config.TestDependencies

// FIXME: Openshift build pipeline passes useMinifiedJavaScript to build (not client)
val buildClient = project.hasProperty("client") || project.hasProperty("useMinifiedJavaScript")

plugins {
  id("org.springframework.boot")
  id("com.moowork.node")
}

dependencies {

  // Project dependencies
  implementation(project(":${rootProject.name}-persistence"))

  // External dependencies
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.mapstruct:mapstruct:${Dependencies.mapstructVersion}")
  implementation("com.querydsl:querydsl-core:${Dependencies.querydslVersion}")

  //api documentation
  implementation("io.springfox:springfox-swagger2:${Dependencies.swaggerVersion}")
  implementation("io.springfox:springfox-swagger-ui:${Dependencies.swaggerVersion}")
  implementation("io.springfox:springfox-data-rest:${Dependencies.swaggerVersion}")

  compileOnly("org.projectlombok:lombok")

  annotationProcessor("org.mapstruct:mapstruct-processor:${Dependencies.mapstructVersion}")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation("commons-io:commons-io:${Dependencies.commonsIOVersion}")
  testImplementation("io.rest-assured:rest-assured:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:json-schema-validator:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:json-path:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:xml-path:${TestDependencies.restAssuredVersion}")
}

node {
  version = Dependencies.nodeVersion
  download = true
  distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
  nodeModulesDir = file("${project.projectDir}/client")
}

springBoot {
  buildInfo()
}

tasks {

  val buildReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "build"))
  }

  val testReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "test", "--", "--coverage"))
  }

  val copyReactbuild by creating(Copy::class) {
    dependsOn(buildReactApp)

    from("client/build/")
    into("${project.buildDir}/resources/main/static")
  }

  val pathingJar by creating(Jar::class) {
    dependsOn(configurations.runtime)
    archiveAppendix.set("pathing")

    doFirst {
      manifest {
        val classpath = configurations.runtimeClasspath.get().files
           .map { it.toURI().toURL().toString().replaceFirst("file:/", "/") }
           .joinToString(separator = " ")

        val mainClass = "se.inera.intyg.intygsadmin.web.IntygsadminApplication"

        attributes["Class-Path"] = classpath
        attributes["Main-Class"] = mainClass
      }
    }
  }

  val restAssuredTest by creating(Test::class) {
    outputs.upToDateWhen { false }
    systemProperty("integration.tests.baseUrl", System.getProperty("baseUrl", "http://localhost:8680"))
    systemProperty("integration.tests.actuatorUrl", System.getProperty("actuatorUrl", "http://localhost:8681"))
    include("**/*IT*")
  }

  test {
    exclude("**/*IT*")
  }

  bootJar {
    manifest {
      attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
      attributes("Start-Class" to  "se.inera.intyg.intygsadmin.web.IntygsadminApplication")
    }
  }

  clean {
    delete("client/build")
  }

  if (OperatingSystem.current().isWindows) {
    bootRun {
      dependsOn(pathingJar)

      doFirst {
        classpath = files(
           "${project.projectDir}/build/classes/java/main",
           "${project.projectDir}/src/main/resources",
           "${project.projectDir}/build/resources/main",
           pathingJar.archiveFile)
      }
    }
  }

  if (buildClient) {

    bootJar {
      dependsOn(buildReactApp)
      from("client/build/") {
        into("static")
      }
    }

    bootRun {
      dependsOn(copyReactbuild)
    }

    test {
      dependsOn(testReactApp)
    }
  }
  
  bootRun {
      jvmArgs = listOf("-Dconfig.folder=${project.rootProject.projectDir}/devops/dev")
  }
}
