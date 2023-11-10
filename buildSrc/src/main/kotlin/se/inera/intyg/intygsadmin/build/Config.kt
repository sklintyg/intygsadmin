package se.inera.intyg.intygsadmin.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val encoding = "UTF-8"
  }

  object Dependencies {
    const val apachePoiVersion = "5.2.4"
    const val commonsIOVersion = "2.14.0"
    const val embeddedRedisVersion = "0.7.2"
    const val kotlinVersion = "1.9.10"
    const val mapstructVersion = "1.5.5.Final"
    const val nodeVersion = "12.22.12"
    const val npmVersion = "6.14.16"
    const val picocliVersion = "4.7.5"
    const val shedlockSpringVersion = "4.46.0"
    const val springDocVersion = "1.7.0"
    const val springSecurityOauth2Version = "2.5.2.RELEASE"
    const val spotbugsAnnotationsVersion = "4.8.1"
  }

  object TestDependencies {
    const val randomBeansVersion = "3.9.0"
  }
}
