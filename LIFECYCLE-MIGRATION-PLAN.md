# Lifecycle Migration Plan — intygsadmin (K1J-2265)

## Goal

Migrate intygsadmin to Java 25 / Spring Boot 4.1.0 / Gradle 9.6.0 / Jackson 3, following the
Spring Boot 4.0 Migration Guide and the generalized `spring-boot-4-lifecycle-migration` skill
(reference implementation: minaintyg, K1J-2265).

## Target versions

| Property | From | To |
| -------- | ---- | -- |
| `intygBomVersion` | 1.0.0.14 | 1.0.0.16 (or later stable) |
| Gradle wrapper | 8.14.4 | 9.6.0 |
| Spring Boot | 3.5.x (via BOM) | 4.1.0 |
| Jackson | 2.x | 3.x |
| Java toolchain | 21 (via BOM `javaVersion`) | 25 |
| Jenkins `builder.image.tag` | 21.0.6 | 25.0.3 |
| Jenkins `runtime.image.tag` | 21.0.2 | 25.0.1 |

## Module layout

- `intygsadmin-logging` — shared logging aspect module (raw `spring-webmvc` + `aspectjweaver`)
- `intygsadmin-persistence` — JPA/Liquibase/QueryDSL module, direct `jackson-databind` dependency
- `intygsadmin-web` — main Spring Boot application (`bootJar`), React client bundled in,
  SBOM aggregation module (`Jenkins.properties: sbom.aggregation.module=intygsadmin-web`)

No separate `integration-test` module; ITs live in `web/src/test` (`*IT*` pattern, run via
`restAssuredTest` Gradle task against a running instance, not Testcontainers-based).

## Project inventory (Phase 0 findings)

| Item | Status |
| ---- | ------ |
| wsdl2java plugin | Not used — N/A |
| Apache Camel | Not used — N/A |
| intyg-common / `dependencies.common.version` | Not used — N/A |
| Testcontainers | Not used — N/A (ITs use `restAssuredTest` against a running app) |
| ActiveMQ / JMS | Not used — N/A |
| Manual `RedisCacheManager` bean | Not found — N/A |
| WebClient beans | Not found — N/A, Phase 7 likely N/A |
| Manual `@Configuration` classes | `SessionConfig` (CookieSerializer — keep, Inera-specific),
  `SecurityConfig`, `OpenApiConfig`, `ObjectMapperConfig` (Jackson2ObjectMapperBuilderCustomizer),
  `JobConfig`, `ApplicationConfig` |
| Session | `org.springframework.session:spring-session-data-redis` (raw) → migrate to
  `spring-boot-starter-session-data-redis` |
| Raw Spring deps | `web/build.gradle`: `spring-boot-starter-web` → `spring-boot-starter-webmvc`;
  `logging/build.gradle`: `spring-webmvc` + `aspectjweaver` → `spring-boot-starter-aspectj` (verify
  webmvc still needed for annotations used in aspects — likely reduce to `spring-web`) |
| Jackson databind usages (excl. annotations) | `ObjectMapperConfig.java` (web, customizer + JSR-310
  serializers), `CustomAuthorizationResolver.java` (web), `BaseRestIntegrationTest.java` (web test),
  `TestDataBootstrapper.java` (persistence), `FakeUser.java` (web, `JsonDeserialize`/`JsonPOJOBuilder`
  from `databind.annotation` — moves with databind package, not `jackson.annotation`) |
| SBOM / Jenkins | `sbom.aggregation.module=intygsadmin-web`, `sbom.aggregation.path=web` — verify
  `cyclonedxBom` output path on `web` module after Boot 4 bump |

## Open questions resolved

- Jira ticket: **K1J-2265**
- No intyg-common dependency → `dependencies.common.version` N/A
- No external schema artifacts / analytics `schemaVersion` in this app → N/A unless discovered later
- Gradle wrapper upgrade: agent will perform and verify (not team-manual for this repo unless told
  otherwise)

## Phases

See `LIFECYCLE-MIGRATION-PROGRESS.md` for live status tracking. Phases follow the standard skill
workflow (0–9); Phase 7 (WebClient) expected to close as N/A pending confirmation in Phase 5/6.

## Verification commands

```
./gradlew clean build spotlessCheck test
./gradlew :intygsadmin-web:restAssuredTest   # requires running app instance; run manually/CI
```

## Risks

- React client build (`buildReactApp`/`copyReactbuild`) wired into `bootJar`/`compileTestJava` —
  unrelated to Boot/Jackson but must keep working through Gradle 9 bump.
- `spotless` + `googleJavaFormat` version pinned via BOM — verify compatibility with Gradle 9.
- CycloneDX `cyclonedxDirectBom` override already present in root `build.gradle` — verify still
  correct under CycloneDX 3.x from bumped BOM; SBOM aggregation module is `web`, check
  `cyclonedxBom` output path there too.
