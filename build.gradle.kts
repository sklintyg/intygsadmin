import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import se.inera.intyg.IntygPluginCheckstyleExtension
import se.inera.intyg.JavaVersion
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsadmin.build.Config.Dependencies
import se.inera.intyg.intygsadmin.build.Config.Jvm
import se.inera.intyg.intygsadmin.build.Config.TestDependencies

plugins {
  kotlin("jvm")
  maven
  `maven-publish`

  id("se.inera.intyg.plugin.common") apply false
  id("io.spring.dependency-management")
}

allprojects {
  group = "se.inera.intyg.intygsadmin"
  version = System.getProperty("buildVersion", "0-SNAPSHOT")

  apply(plugin = "maven-publish")
  extra.apply {
    set("intygInfraVersion", System.getProperty("infraVersion", "3.19.0-SNAPSHOT"))
    set("errorproneExclude", "true") //FIXME: Errorprone does not support Kotlin and KAPT. Until it does this will exclude the errorprone task for this project
  }

  repositories {
    mavenLocal()
    maven ("https://nexus.drift.inera.se/repository/it-public/")
    mavenCentral {
      content {
        // this repository contains everything BUT artifacts with group starting with "se.inera"
        excludeGroupByRegex("se\\.inera.*")
      }
    }
  }

  publishing {
    repositories {
      maven {
        url = uri("https://nexus.drift.inera.se/repository/maven-releases/")
        credentials {
          username = System.getProperty("ineraNexusUsername")
          password = System.getProperty("ineraNexusPassword")
        }
      }
    }
  }
}

apply(plugin = "se.inera.intyg.plugin.common")

subprojects {
  apply(plugin = "org.gradle.maven")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "se.inera.intyg.plugin.common")

  apply<DependencyManagementPlugin>()

  configure<IntygPluginCheckstyleExtension> {
    javaVersion = JavaVersion.JAVA11
    showViolations = true
    ignoreFailures = false
  }

  dependencyManagement {
    val springBootVersion: String by extra
    imports {
      mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
  }

  dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("net.jcip:jcip-annotations:${Dependencies.jcipAnnotationsVersion}")
    compileOnly("com.github.spotbugs:spotbugs-annotations:${Dependencies.spotbugsAnnotationsVersion}")

    implementation("org.apache.commons:commons-lang3")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
      exclude(module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.platform:junit-platform-runner") {
      exclude(module = "junit")
    }
    testImplementation("io.github.benas:random-beans:${TestDependencies.randomBeansVersion}")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.springframework.security:spring-security-test")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  }

  tasks {

    withType<Test> {
      useJUnitPlatform()
    }

    withType<JavaCompile> {
      sourceCompatibility = Jvm.sourceCompatibility
      targetCompatibility = Jvm.targetCompatibility
      options.encoding = Jvm.encoding
    }
  }
}

gradle.taskGraph.whenReady {
  var runningRestassuredTest = false
  project("intygsadmin-web") { runningRestassuredTest = gradle.taskGraph.hasTask(tasks["restAssuredTest"]) }

  if (runningRestassuredTest) {
    project("intygsadmin-web") { tasks["test"].enabled = false }
    project("intygsadmin-persistence") { tasks["test"].enabled = false }
  }
}

tasks {
  register<TagReleaseTask>("tagRelease")
}

dependencies {
  subprojects.forEach { archives(it) }
}
