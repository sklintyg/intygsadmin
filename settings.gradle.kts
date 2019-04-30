import se.inera.intyg.intygsadmin.build.Config.Dependencies

pluginManagement {
	repositories {
		maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
		gradlePluginPortal()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id.startsWith("org.jetbrains.kotlin.")) {
				useVersion(Dependencies.kotlinVersion)
			}
			if (requested.id.id.startsWith("se.inera.intyg.plugin.common")) {
				useVersion(Dependencies.intygPluginVersion)
			}
			if (requested.id.id.startsWith("io.spring.dependency-management")) {
				useVersion(Dependencies.springDependencyManagementVersion)
			}
			if (requested.id.id.startsWith("org.springframework.boot")) {
				useVersion(Dependencies.springBootVersion)
			}
			if (requested.id.id.startsWith("com.moowork.node")) {
				useVersion(Dependencies.nodePluginVersion)
			}
		}
	}
}

rootProject.name = "intygsadmin"

include(":common")
include(":mail-sender")
include(":integration")
include(":persistence")
include(":web")
include(":test")

fun getProjectDirName(project: String): String {
	return when(project) {
		"${rootProject.name}-common" ->"$rootDir/common"
		"${rootProject.name}-mail-sender" ->"$rootDir/mail-sender"
		"${rootProject.name}-integration" ->"$rootDir/integration"
		"${rootProject.name}-persistence" ->"$rootDir/persistence"
		"${rootProject.name}-web" ->"$rootDir/web"
		"${rootProject.name}-test" ->"$rootDir/test"
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
