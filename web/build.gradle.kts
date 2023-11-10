import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import se.inera.intyg.intygsadmin.build.Config.Dependencies

val buildClient = project.hasProperty("client")
val devPort = 8070 + System.getProperty("instance", "0").toInt()
val devPortInternal = 8170 + System.getProperty("instance", "0").toInt()
val debugPort = 8870 + System.getProperty("instance", "0").toInt()
val applicationDir = "${rootProject.projectDir}/devops/dev"

plugins {
  id("org.springframework.boot")
  id("com.netflix.nebula.node")
}

dependencies {
  implementation(project(":${rootProject.name}-persistence"))

  implementation("se.inera.intyg.infra:driftbanner-dto:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:integreradeenheter:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:intyginfo:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:security-common:${project.extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:testcertificate:${project.extra["intygInfraVersion"]}")

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

  implementation("com.querydsl:querydsl-core")
  implementation("it.ozimov:embedded-redis:${Dependencies.embeddedRedisVersion}")
  implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:${Dependencies.shedlockSpringVersion}")
  implementation("net.javacrumbs.shedlock:shedlock-spring:${Dependencies.shedlockSpringVersion}")
  implementation("org.apache.poi:poi-ooxml:${Dependencies.apachePoiVersion}")
  implementation("org.mapstruct:mapstruct:${Dependencies.mapstructVersion}")
  implementation("org.springdoc:springdoc-openapi-data-rest:${Dependencies.springDocVersion}")
  implementation("org.springdoc:springdoc-openapi-ui:${Dependencies.springDocVersion}")
  implementation("org.springframework.security.oauth:spring-security-oauth2:${Dependencies.springSecurityOauth2Version}")
  implementation("org.springframework.session:spring-session-data-redis")
  implementation("redis.clients:jedis")

  compileOnly("org.projectlombok:lombok")

  annotationProcessor("org.projectlombok:lombok")
  annotationProcessor("org.mapstruct:mapstruct-processor:${Dependencies.mapstructVersion}")
  testAnnotationProcessor("org.mapstruct:mapstruct-processor:${Dependencies.mapstructVersion}")

  testImplementation("commons-io:commons-io:${Dependencies.commonsIOVersion}")
  testImplementation("io.rest-assured:json-path")
  testImplementation("io.rest-assured:json-schema-validator")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:xml-path")
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

springBoot { buildInfo() }

tasks {

  named<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
  }

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
    into("${project.layout.buildDirectory.get().asFile}/resources/main/static")
  }

  val runtimeOnlyExtended = configurations.create("runtimeOnlyExtended")
          .extendsFrom(configurations.runtimeOnly.get())

  val pathingJar by creating(Jar::class) {
    dependsOn(runtimeOnlyExtended)
    archiveAppendix.set("pathing")

    doFirst {
      manifest {
        val mainClass = "se.inera.intyg.intygsadmin.web.IntygsadminApplication"
        val classpath = configurations.runtimeClasspath.get().files
            .map { it.toURI().toURL().toString().replaceFirst("file:/", "/") }
            .joinToString(separator = " ")
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

  clean {
    delete("client/build")
  }

  bootJar {
    manifest {
      attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
      attributes("Start-Class" to "se.inera.intyg.intygsadmin.web.IntygsadminApplication")
    }
  }

  bootRun {
    systemProperty("dev.http.port", devPort)
    systemProperty("dev.http.port.internal", devPortInternal)
    systemProperty("spring.profiles.active", "dev, fake, caching-enabled, it-stub, wc-stub, pp-stub, ts-stub")
    systemProperty("spring.config.additional-location", "file:${applicationDir}/config/")
  }

  test {
    exclude("**/*IT*")
  }

  bootRunMainClassName { dependsOn(copyReactbuild) }
  bootRun { dependsOn(copyReactbuild) }
  test { dependsOn(testReactApp) }

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
