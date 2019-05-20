package se.inera.intyg.intygsadmin.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val encoding = "UTF-8"
  }

  object Dependencies {

    //Project dependencies
    const val intygPluginVersion = "3.0.5"

    //External dependencies
    const val nodePluginVersion = "1.3.1"
    const val nodeVersion = "10.15.1"

    const val kotlinVersion = "1.3.31"
    const val springBootVersion = "2.1.5.RELEASE"
    const val springDependencyManagementVersion = "1.0.7.RELEASE"

    const val mapstructVersion = "1.3.0.Final"
    const val querydslVersion = "4.2.1"
    const val hibernateJpsVersion = "1.0.2.Final"
    const val javaxAnnotationVersion = "1.3.2"
    const val commonsIOVersion = "2.6"
    const val swaggerVersion = "2.9.2"
  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.27.0"
    const val junit5Version = "5.4.1"
    const val randomBeansVersion = "3.9.0"
    const val restAssuredVersion = "3.3.0"
  }
}
