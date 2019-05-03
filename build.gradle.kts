
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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

  repositories {
    mavenLocal()
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
      mavenContent {
        releasesOnly()
      }
    }
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/snapshots/")
      mavenContent {
        snapshotsOnly()
      }
    }

    mavenCentral()
  }
}

apply(plugin = "se.inera.intyg.plugin.common")

subprojects {
  apply(plugin = "org.gradle.maven")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "se.inera.intyg.plugin.common")
  apply(plugin = "kotlin")

  apply<DependencyManagementPlugin>()

  dependencyManagement {
    imports {
      mavenBom("org.springframework.boot:spring-boot-dependencies:${Dependencies.springBootVersion}")
      mavenBom("org.junit:junit-bom:${TestDependencies.junit5Version}")
    }
  }

  dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = Jvm.kotlinJvmTarget
    }
  }
}

tasks {
  register<TagReleaseTask>("tagRelease")
}

publishing {
  repositories {
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
      credentials {
        username = System.getProperty("nexusUsername")
        password = System.getProperty("nexusPassword")
      }
    }
  }
}

dependencies {
  subprojects.forEach { archives(it) }
}
