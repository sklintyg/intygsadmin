package se.inera.intyg.intygsadmin.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val encoding = "UTF-8"
  }

  object Dependencies {
    const val apachePoiVersion = "5.2.2"
    const val commonsIOVersion = "2.11.0"
    const val embeddedRedisVersion = "0.7.2"
    const val jcipAnnotationsVersion = "1.0"
    const val kotlinVersion = "1.7.10"
    const val mapstructVersion = "1.5.2.Final"
    const val nodeVersion = "12.22.12"
    const val npmVersion = "6.14.16"
    const val picocliVersion = "4.6.3"
    const val shedlockSpringVersion = "4.41.0"
    const val springDocVersion = "1.6.11"
    const val springSecurityOauth2Version = "2.5.2.RELEASE"
    const val spotbugsAnnotationsVersion = "4.7.2"
  }

  object TestDependencies {
    const val randomBeansVersion = "3.9.0"
  }
}
