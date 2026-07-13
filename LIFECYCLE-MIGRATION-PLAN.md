# Lifecycle Migration Plan — intygsadmin (K1J-2265)

## Goal

Migrate intygsadmin to Java 25 / Spring Boot 4.1.0 / Gradle 9.6.x / Jackson 3, following the
Spring Boot 4.0 Migration Guide and the generalized `spring-boot-4-lifecycle-migration` skill
(reference implementation: minaintyg, K1J-2265).

## Target versions

| Property | From | To (resolved) |
| -------- | ---- | ------------- |
| `intygBomVersion` | 1.0.0.14 | **1.0.0.17** |
| Gradle wrapper | 8.14.4 | **9.6.1** |
| Spring Boot | 3.5.x (via BOM) | 4.1.0 |
| Jackson | 2.x | 3.x (`tools.jackson.*`) |
| Java toolchain | 21 (via BOM `javaVersion`) | 25 |
| Jenkins `builder.image.tag` | 21.0.6 | 25.0.3 |
| Jenkins `runtime.image.tag` | 21.0.2 | 25.0.1 |

## Module layout

- `intygsadmin-logging` — shared logging aspect module (`spring-boot-starter-aspectj` + `spring-web`)
- `intygsadmin-persistence` — JPA/Liquibase/QueryDSL module; Jackson used by `TestDataBootstrapper`
- `intygsadmin-web` — main Spring Boot application (`bootJar`), React client bundled in,
  SBOM aggregation module (`Jenkins.properties: sbom.aggregation.module=intygsadmin-web`)

No separate `integration-test` module; ITs live in `web/src/test` (`*IT*` pattern, run via
`restAssuredTest` Gradle task against a running instance, not Testcontainers-based).

## Project inventory

| Item | Status |
| ---- | ------ |
| wsdl2java plugin | Not used — N/A |
| Apache Camel | Not used — N/A |
| intyg-common / `dependencies.common.version` | Not used — N/A |
| Testcontainers | Not used — N/A (ITs use `restAssuredTest` against a running app) |
| ActiveMQ / JMS | Not used — N/A |
| Manual `RedisCacheManager` bean | Not found — N/A |
| WebClient beans | Not found — **Phase 7 N/A** |
| Manual `@Configuration` classes | `SessionConfig` (CookieSerializer — keep, Inera-specific),
  `SecurityConfig`, `OpenApiConfig`, `ObjectMapperConfig` (`JsonMapperBuilderCustomizer`),
  `JobConfig`, `ApplicationConfig` |
| Session | Migrated to `spring-boot-starter-session-data-redis` (Phase 3) |
| Raw Spring deps | Migrated in Phase 3 (`webmvc`, `starter-aspectj`, `spring-web`) |
| Jackson databind usages | See Phase 4 checklist below |
| SBOM / Jenkins | `cyclonedxBom` legacy output (`bom.json` / `bom.xml`) on `web` module — done (Phase 2) |

## Open questions resolved

- Jira ticket: **K1J-2265**
- No intyg-common dependency → `dependencies.common.version` N/A
- No external schema artifacts / analytics `schemaVersion` in this app → N/A unless discovered later
- Gradle wrapper upgrade: performed and verified (9.6.1)
- `spring-boot-properties-migrator`: added in Phase 5; no renames reported at startup; remove in Phase 8

## Phases

See `LIFECYCLE-MIGRATION-PROGRESS.md` for live status tracking. Phases follow the standard skill
workflow (0–9); Phase 7 (WebClient) is **N/A**.

### Phase 4 — Jackson 3 file checklist

- [x] `ObjectMapperConfig` — done in Phase 2 (required for Boot 4 compile)
- [x] `persistence/build.gradle` — `tools.jackson.core:jackson-databind`
- [x] `CustomAuthorizationResolver`, `FakeApiController`, `TestDataBootstrapper`, `BaseRestIntegrationTest`
- [x] `FakeUser` + 9 builder DTOs — `tools.jackson.databind.annotation.*`
- [x] No `com.fasterxml.jackson.databind|core|datatype` imports in application code

## Verification commands

```
./gradlew clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp
./gradlew :intygsadmin-web:appRunDebug   # smoke-test startup (kill process after)
./gradlew :intygsadmin-web:restAssuredTest   # requires running app; run manually/CI (Phase 6)
```

## Risks

- React client build (`buildReactApp`/`copyReactbuild`) wired into `bootJar`/`compileTestJava` —
  unrelated to Boot/Jackson but must keep working through Gradle 9 bump.
- `spotless` + `googleJavaFormat` version pinned via BOM — verify compatibility with Gradle 9.
- **Dual Jackson classpath** until Phase 4 completes: `persistence` direct `jackson-databind`
  (Jackson 2 coordinate) coexists with Boot's Jackson 3 via `spring-boot-starter-jackson`.
- `restAssuredTest` is **not** part of default `build` — must be verified separately before sign-off
  (Phase 6).
- Jackson 3 `FAIL_ON_NULL_FOR_PRIMITIVES=true` default — low risk for this app's DTOs but smoke-test
  after migration.

## Phase 6 scope (intygsadmin-specific)

Not Testcontainers-based. Phase 6 covers:

- Jackson 3 fixes in IT helpers (if any remain after Phase 4)
- Rest Assured 6.x compatibility with running app
- Session cookie handling (`SESSION=` — already used in `BaseRestIntegrationTest`)
- Manual/CI run of `:intygsadmin-web:restAssuredTest`

Mark as N/A: Testcontainers module renames, `@AutoConfigureTestRestTemplate`, `RestTestClient`.
