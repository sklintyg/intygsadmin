import com.github.spotbugs.snom.SpotBugsPlugin
import com.github.spotbugs.snom.SpotBugsTask
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import se.inera.intyg.findGitRepository
import se.inera.intyg.copyFile
import se.inera.intyg.IntygPluginCheckstyleExtension
import se.inera.intyg.JavaVersion
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsadmin.build.Config.Dependencies
import se.inera.intyg.intygsadmin.build.Config.Jvm
import se.inera.intyg.intygsadmin.build.Config.TestDependencies
import java.nio.file.Files
import java.nio.file.Paths

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
    set("intygInfraVersion", System.getProperty("infraVersion", "3.21.0-SNAPSHOT"))
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
    tasks {
      val spotbugsIncludesAndExcludes by creating {
        dependsOn(":unzipBuildTools")
        val spotbugsToolDir = "${rootProject.layout.buildDirectory.get().asFile}/build-tools/spotbugs"
        withType<SpotBugsTask> {
          includeFilter.set(file("${spotbugsToolDir}/spotbugsIncludeFilter.xml"))
          excludeFilter.set(file("${spotbugsToolDir}/spotbugsExcludeFilter.xml"))
        }
      }

      withType<Checkstyle> { named("checkstyleMain") { dependsOn(":unzipBuildTools") } }
      withType<JacocoReport> { named("jacocoTestReport") { mustRunAfter(":unzipBuildTools") } }
      withType<SpotBugsTask> {
        named("spotbugsMain") { dependsOn(spotbugsIncludesAndExcludes) }
        named("spotbugsTest") { enabled = false }
      }
    }

    dependencies { SpotBugsPlugin.SLF4J_CONFIG_NAME("org.slf4j:slf4j-simple") }
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

subprojects.map { it.name }.forEach {
  dependencies { testReportAggregation(project(":${it}")) }
}

tasks {
  register("tagRelease", TagReleaseTask::class)

  val unzipBuildTools by registering(Copy::class) {
    into("${rootProject.layout.buildDirectory.get().asFile}/build-tools/")
    from(rootProject.buildscript.configurations.getByName("classpath").find { it.name.contains("se.inera.intyg.plugin.common") }
            ?.let { zipTree(it).matching { include("/spotbugs/**", "/git_hooks/**") } })
  }

  val applyGitHooks by registering(Copy::class) {
    dependsOn(unzipBuildTools)
    val repository = findGitRepository(project.rootProject.projectDir)
    val gitHooksDir = "${rootProject.layout.buildDirectory.get().asFile}/build-tools/git_hooks"
    val commitMsg = file("${gitHooksDir}/commit-msg")
    val preCommit = file("${gitHooksDir}/pre-commit")
    val toDir = Paths.get(repository.directory.path, "hooks")

    if (!Files.exists(toDir)) {
      Files.createDirectory(toDir)
    }
    copyFile(commitMsg, toDir)
    copyFile(preCommit, toDir)
  }

  compileJava { dependsOn(applyGitHooks) }
}
