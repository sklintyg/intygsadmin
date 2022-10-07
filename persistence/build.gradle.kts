plugins {
  id("io.spring.dependency-management")
}

dependencies {

  implementation("org.liquibase:liquibase-core")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.data:spring-data-jpa")

  implementation("com.querydsl:querydsl-core")
  implementation("com.querydsl:querydsl-jpa")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("jakarta.persistence:jakarta.persistence-api")

  implementation("se.inera.intyg.infra:driftbanner-dto:${project.extra["intygInfraVersion"]}")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("com.querydsl:querydsl-apt::jpa")
  annotationProcessor("jakarta.persistence:jakarta.persistence-api")
  annotationProcessor("org.projectlombok:lombok")

  runtimeOnly("mysql:mysql-connector-java")
  runtimeOnly("com.h2database:h2")
}
