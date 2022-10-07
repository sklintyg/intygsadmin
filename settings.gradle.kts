pluginManagement {
  repositories {
    maven("https://nexus.drift.inera.se/repository/it-public/")
    gradlePluginPortal()
  }
  resolutionStrategy {
    val kotlinVersion: String by extra
    val springBootVersion: String by extra
    val nebulaNodePluginVersion: String by extra
    val intygGradlePluginVersion: String by extra
    val dependencyManagementPluginVersion: String by extra

    eachPlugin {
      if (requested.id.id.startsWith("org.jetbrains.kotlin.")) {
        useVersion(kotlinVersion)
      }
      if (requested.id.id.startsWith("se.inera.intyg.plugin.common")) {
        useVersion(intygGradlePluginVersion)
      }
      if (requested.id.id.startsWith("io.spring.dependency-management")) {
        useVersion(dependencyManagementPluginVersion)
      }
      if (requested.id.id.startsWith("org.springframework.boot")) {
        useVersion(springBootVersion)
      }
      if (requested.id.id.startsWith("nebula.node")) {
        useVersion(nebulaNodePluginVersion)
      }
    }
  }
}

rootProject.name = "intygsadmin"

include(":persistence")
include(":web")
include(":test")
include(":liquibase-runner")

fun getProjectDirName(project: String): String {
  return when (project) {
    "${rootProject.name}-persistence" -> "$rootDir/persistence"
    "${rootProject.name}-web" -> "$rootDir/web"
    "${rootProject.name}-test" -> "$rootDir/test"
    "${rootProject.name}-liquibase-runner" -> "$rootDir/tools/liquibase-runner"
    else -> throw IllegalArgumentException("Project module $project does not exist.")
  }
}

for (project in rootProject.children) {
  project.name = "${rootProject.name}-${project.name}"
  val projectName = project.name

  project.projectDir = file(getProjectDirName(projectName))
  project.buildFileName = "build.gradle.kts"

  if (!project.projectDir.isDirectory) {
    throw IllegalArgumentException("Project directory ${project.projectDir} for project ${project.name} does not exist.")
  }

  if (!project.buildFile.isFile) {
    throw IllegalArgumentException("Build file ${project.buildFile} for project ${project.name} does not exist.")
  }
}
