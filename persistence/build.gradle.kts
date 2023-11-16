plugins {
  id("io.spring.dependency-management")
}

dependencies {
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("com.querydsl:querydsl-core")
  implementation("com.querydsl:querydsl-jpa")
  implementation("jakarta.persistence:jakarta.persistence-api")
  implementation("org.liquibase:liquibase-core")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.data:spring-data-jpa")
  implementation("se.inera.intyg.infra:driftbanner-dto:${project.extra["intygInfraVersion"]}")

  compileOnly("org.projectlombok:lombok")

  runtimeOnly("com.mysql:mysql-connector-j")
  runtimeOnly("com.h2database:h2")

  annotationProcessor("com.querydsl:querydsl-apt::jpa")
  annotationProcessor("jakarta.persistence:jakarta.persistence-api")
  annotationProcessor("org.projectlombok:lombok")
}
