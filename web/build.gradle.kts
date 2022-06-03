import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import se.inera.intyg.intygsadmin.build.Config.Dependencies
import se.inera.intyg.intygsadmin.build.Config.TestDependencies

val buildClient = project.hasProperty("client")
val devPort = 8070 + System.getProperty("instance", "0").toInt()
val devPortInternal = 8170 + System.getProperty("instance", "0").toInt()
val debugPort = 8870 + System.getProperty("instance", "0").toInt()
val applicationDir = "${rootProject.projectDir}/devops/dev"

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
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.springframework.security.oauth:spring-security-oauth2:${Dependencies.springSecurityOauth2Version}")
  implementation("org.springframework.session:spring-session-data-redis")
  implementation("org.mapstruct:mapstruct:${Dependencies.mapstructVersion}")
  implementation("com.querydsl:querydsl-core")
  implementation("org.apache.poi:poi:${Dependencies.apachePoiVersion}")
  implementation("org.apache.poi:poi-ooxml:${Dependencies.apachePoiVersion}")

  implementation("net.javacrumbs.shedlock:shedlock-spring:${Dependencies.shedlockSpringVersion}")
  implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:${Dependencies.shedlockSpringVersion}")

  implementation("it.ozimov:embedded-redis:${Dependencies.embeddedRedisVersion}")
  implementation("redis.clients:jedis")

  implementation("se.inera.intyg.infra:intyginfo:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:testcertificate:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:integreradeenheter:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:driftbanner-dto:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:security-common:${project.extra["intygInfraVersion"]}")

  //api documentation
  implementation("io.springfox:springfox-swagger2:${Dependencies.swaggerVersion}")
  implementation("io.springfox:springfox-swagger-ui:${Dependencies.swaggerUIVersion}")
  implementation("io.springfox:springfox-data-rest:${Dependencies.swaggerDataVersion}")

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
  npmVersion = Dependencies.npmVersion
  download = true
  distBaseUrl = "https://nodejs.org/dist/"
  nodeModulesDir = file("${project.projectDir}/client")
  workDir = file("${rootProject.projectDir}/.gradle/nodejs")
  npmWorkDir = file("${rootProject.projectDir}/.gradle/npm")
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

  val startReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "start"))
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
    systemProperty("integration.tests.baseUrl", System.getProperty("baseUrl", "http://localhost:8070"))
    systemProperty("integration.tests.actuatorUrl", System.getProperty("actuatorUrl", "http://localhost:8170"))
    include("**/*IT*")
  }

  test {
    exclude("**/*IT*")
  }

  bootJar {
    manifest {
      attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
      attributes("Start-Class" to "se.inera.intyg.intygsadmin.web.IntygsadminApplication")
    }
  }

  clean {
    delete("client/build")
  }

  bootRun {
    systemProperty("dev.http.port", devPort)
    systemProperty("dev.http.port.internal", devPortInternal)
    systemProperty("spring.profiles.active", "dev, fake, caching-enabled, it-stub, wc-stub, pp-stub, ts-stub")
    systemProperty("spring.config.additional-location", "file:${applicationDir}/config/")
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

  // Convenience methods so that we can use the same naming conventions
  val appRunDebug by registering {
    println("Running in Debug mode")
    doFirst {
      bootRun.configure {
        jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$debugPort")
      }
    }
    finalizedBy("bootRun")
  }

  val appRun by registering {
    println("Running in normal mode")
    finalizedBy("bootRun")
  }
}
