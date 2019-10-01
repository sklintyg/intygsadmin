import se.inera.intyg.intygsadmin.build.Config.Dependencies

plugins {
  id("io.spring.dependency-management")
}

dependencies {

  implementation("org.liquibase:liquibase-core")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  implementation("com.querydsl:querydsl-core")
  implementation("com.querydsl:querydsl-jpa")
  implementation("com.fasterxml.jackson.core:jackson-databind")

  implementation("se.inera.intyg.infra:driftbanner-dto:${project.extra["intygInfraVersion"]}")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("com.querydsl:querydsl-apt::jpa")
  annotationProcessor("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:${Dependencies.hibernateJpsVersion}")
  annotationProcessor("javax.annotation:javax.annotation-api")
  annotationProcessor("org.projectlombok:lombok")

  runtime("mysql:mysql-connector-java")
  runtime("com.h2database:h2")
}
