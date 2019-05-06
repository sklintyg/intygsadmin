package se.inera.intyg.intygsadmin.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val encoding = "UTF-8"
  }

  object Dependencies {

    //Project dependencies
    const val intygPluginVersion = "2.0.4"

    //External dependencies
    const val nodePluginVersion = "1.3.1"
    const val nodeVersion = "10.15.1"

    const val kotlinVersion = "1.3.31"
    const val springBootVersion = "2.1.4.RELEASE"
    const val springDependencyManagementVersion = "1.0.7.RELEASE"

    const val querydslVersion = "4.2.1"
    const val hibernateJpsVersion = "1.0.2.Final"
    const val javaxAnnotationVersion = "1.3.2"
    const val commonsIOVersion = "2.6"
  }

  object TestDependencies {
    const val junit5Version = "5.4.1"
    const val restAssuredVersion = "3.3.0"
  }
}
