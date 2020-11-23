import com.moowork.gradle.node.task.NodeTask
import se.inera.intyg.intygsadmin.build.Config.Dependencies

plugins {
  id("com.moowork.node")
}

node {
  version = Dependencies.nodeVersion
  download = true
  distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
  nodeModulesDir = file("${project.projectDir}")
  workDir = file("${rootProject.projectDir}/.gradle/nodejs")
  npmWorkDir = file("${rootProject.projectDir}/.gradle/npm")
}

tasks {
  clean {
    delete("build/test-results")
  }
  register<NodeTask>("cypressTest") {
    dependsOn("npmInstall")

    val baseUrl = System.getProperty("baseUrl", "http://localhost:8070")

    setScript(file("scripts/run.js"))

    setArgs(listOf("baseUrl=$baseUrl"))
  }
}

