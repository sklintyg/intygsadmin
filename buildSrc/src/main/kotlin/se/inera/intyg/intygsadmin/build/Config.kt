package se.inera.intyg.intygsadmin.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val encoding = "UTF-8"
  }

  object Dependencies {

    //Project dependencies
    const val intygPluginVersion = "3.1.4"

    //External dependencies
    const val nodePluginVersion = "1.3.1"
    const val nodeVersion = "10.16.2"
    const val npmVersion = "6.10.3"

    const val kotlinVersion = "1.3.31"
    const val springBootVersion = "2.2.5.RELEASE"
    const val springDependencyManagementVersion = "1.0.9.RELEASE"
    const val springSecurityOauth2Version = "2.3.6.RELEASE"

    const val shedlockSpringVersion = "4.5.0"
    const val embeddedRedisVersion = "0.7.2"

    const val mapstructVersion = "1.3.0.Final"
    const val hibernateJpsVersion = "1.0.2.Final"
    const val commonsIOVersion = "2.6"
    const val swaggerVersion = "2.9.2"
    const val commonsLang3Version = "3.9"
    const val apachePoiVersion = "4.1.0"

    // Spotbugs annotations
    const val spotbugsAnnotationsVersion = "3.1.12"
    const val jcipAnnotationsVersion = "1.0"
  }

  object TestDependencies {
    const val h2Version = "2.1.210"
    const val mockitoCoreVersion = "2.27.0"
    const val junit5Version = "5.4.2"
    const val randomBeansVersion = "3.9.0"
    const val restAssuredVersion = "4.0.0"
  }
}
