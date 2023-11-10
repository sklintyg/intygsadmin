import com.github.spotbugs.snom.SpotBugsPlugin
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import se.inera.intyg.IntygPluginCheckstyleExtension
import se.inera.intyg.JavaVersion
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsadmin.build.Config.Dependencies
import se.inera.intyg.intygsadmin.build.Config.Jvm
import se.inera.intyg.intygsadmin.build.Config.TestDependencies

plugins {
  id("se.inera.intyg.plugin.common") apply false
  id("io.spring.dependency-management")
  id("test-report-aggregation")
  kotlin("jvm")
}

allprojects {
  apply(plugin = "se.inera.intyg.plugin.common")
  apply<DependencyManagementPlugin>()

  group = "se.inera.intyg.intygsadmin"
  version = System.getProperty("buildVersion", "0-SNAPSHOT")

  dependencyManagement {
    val springBootVersion: String by extra
    imports {
      mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
  }

  extra.apply {
    set("intygInfraVersion", System.getProperty("infraVersion", "3.20.0-SNAPSHOT"))
    set("errorproneExclude", "true") //FIXME: Errorprone does not support Kotlin and KAPT. Until it does this will exclude the errorprone task for this project
  }

  repositories {
    mavenLocal()
    maven ("https://nexus.drift.inera.se/repository/it-public/")
    mavenCentral {
      content {
        excludeGroupByRegex("se\\.inera.*")
      }
    }
  }

  if (project.hasProperty("codeQuality")) {
    dependencies {
      SpotBugsPlugin.SLF4J_CONFIG_NAME("org.slf4j:slf4j-simple")
    }
  }

  tasks.clean {
    setDelete(fileTree(project.layout.buildDirectory.get().asFile) {
      exclude("/tmp/.cache/expanded/expanded.lock")
    })
  }
}

subprojects {
  configure<IntygPluginCheckstyleExtension> {
    javaVersion = JavaVersion.JAVA11
    showViolations = true
    ignoreFailures = false
  }

  dependencies {
    implementation("org.apache.commons:commons-lang3")

    compileOnly("com.github.spotbugs:spotbugs-annotations:${Dependencies.spotbugsAnnotationsVersion}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("io.github.benas:random-beans:${TestDependencies.randomBeansVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.platform:junit-platform-runner") { exclude(module = "junit") }
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") { exclude(module = "junit") }
    testImplementation("org.springframework.security:spring-security-test")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  }

  tasks {
    withType<Test> { useJUnitPlatform() }
    withType<JavaCompile> {
      sourceCompatibility = Jvm.sourceCompatibility
      targetCompatibility = Jvm.targetCompatibility
      options.encoding = Jvm.encoding
    }
  }
}

tasks.register<TagReleaseTask>("tagRelease")

subprojects.map {it.name }.forEach {
  dependencies { testReportAggregation(project(":${it}")) }
}
