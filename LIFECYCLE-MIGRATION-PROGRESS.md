# Lifecycle Migration Progress — intygsadmin (K1J-2265)

## Phase checklist

- [x] Phase 0 — Baseline and documentation
- [x] Phase 1 — Gradle 9 wrapper
- [x] Phase 2 — intyg-bom + CI images
- [x] Phase 3 — Spring Boot 4 compile + modular starters
- [x] Phase 4 — Jackson 3 migration
- [x] Phase 5 — Autoconfig audit (Redis, JMS, Jackson)
- [x] Phase 6 — Integration tests + dependency audit (partial — see log)
- [x] Phase 7 — WebClient.Builder + webclient starter — **N/A** (no WebClient beans)
- [x] Phase 8 — Final sign-off (remove migrator)
- [ ] Phase 9 — Friendliness audit (documentation)

## Phase log

### Phase 0 — Baseline (done)

- Baseline versions: `intygBomVersion=1.0.0.14`, Gradle wrapper `8.14.4`,
  Jenkins `builder.image.tag=21.0.6`, `runtime.image.tag=21.0.2`.
- Modules: `intygsadmin-logging`, `intygsadmin-persistence`, `intygsadmin-web`.
- No wsdl2java, Camel, intyg-common, Testcontainers, ActiveMQ/JMS, or manual
  `RedisCacheManager`/`WebClient` beans found — these Jira/skill items are **N/A** for this repo.
- Jackson databind (non-annotation) touchpoints: `ObjectMapperConfig`,
  `CustomAuthorizationResolver`, `BaseRestIntegrationTest`, `TestDataBootstrapper`, `FakeUser`.
- Raw Spring deps to replace with starters: `spring-boot-starter-web` (web),
  `spring-webmvc` + `aspectjweaver` (logging), `spring-session-data-redis` (web).
- Baseline build (`clean build spotlessCheck test`, excluding React tasks) is **green**
  (`BUILD SUCCESSFUL`, 34 actionable tasks). Gradle reports deprecated features incompatible with
  Gradle 9 (details via `--warning-mode all`) — expected, to be resolved in Phase 1.
- See `LIFECYCLE-MIGRATION-PLAN.md` for full inventory.

### Phase 1 — Gradle 9 wrapper (done)

- Upgraded Gradle wrapper to **9.6.1** (`gradle-wrapper.properties`, wrapper JAR, `gradlew` scripts).
- Applied CycloneDX Gradle 9 fix in root `build.gradle`: replaced `tasks.matching { …
  cyclonedxDirectBom … }` with `pluginManager.withPlugin('org.cyclonedx.bom') { … }`.
- Baseline Gradle 9 deprecation warnings from pre-migration build resolved.

### Phase 2 — intyg-bom + CI images (done)

- Updated `gradle.properties` to `intygBomVersion=1.0.0.17` (landed at `.17`, not `.16`).
- Updated `Jenkins.properties` builder/runtime image tags to `25.0.3` / `25.0.1`.
- Restored legacy SBOM output names on `intygsadmin-web` (`bom.json` / `bom.xml`) so CI keeps
  matching the aggregation module output; moved CycloneDX plugin from root to `web` module.
- Spring Boot 4 compile fixes: `EntityScan` import, `RequestErrorController` / `ErrorController`
  API, security logout matcher, `spring.session.data.redis.*` property rename.
- **Partial Jackson 3** (compile-only): `ObjectMapperConfig` migrated to `JsonMapperBuilderCustomizer`
  + `tools.jackson.*` serializers — remaining Jackson 2 imports deferred to Phase 4.
- Liquibase: `org.liquibase:liquibase-core` → `spring-boot-starter-liquibase` in persistence.
- Rest Assured test dependencies pinned via `rest-assured-bom:6.0.0` (BOM no longer supplies coords).
- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` is green.

### Phase 3 — Spring Boot 4 compile + starters (done)

- Replaced `spring-boot-starter-web` with `spring-boot-starter-webmvc` in `web/build.gradle`.
- Replaced raw `spring-session-data-redis` with `spring-boot-starter-session-data-redis` in
  `web/build.gradle`.
- Replaced `aspectjweaver` + `spring-webmvc` with `spring-boot-starter-aspectj` + `spring-web` in
  `logging/build.gradle` to align shared-module dependencies with Boot 4 starter layout.
- `build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` is green.

### Phase 4 — Jackson 3 migration (done)

- Updated `persistence/build.gradle`: `com.fasterxml.jackson.core:jackson-databind` →
  `tools.jackson.core:jackson-databind`.
- Migrated all remaining Jackson 2 imports to Jackson 3 (`tools.jackson.*`); kept
  `com.fasterxml.jackson.annotation.*` where used (`IntygInfoDTO`, `DataExportResponse`).
- Replaced `ObjectMapper` / `JsonProcessingException` with `JsonMapper` / `JacksonException` in
  `CustomAuthorizationResolver`, `FakeApiController` (injected), `TestDataBootstrapper`,
  `BaseRestIntegrationTest`.
- Migrated 10 builder DTOs (`@JsonDeserialize` / `@JsonPOJOBuilder`) to
  `tools.jackson.databind.annotation.*`.
- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.
- `appRunDebug` smoke test — **Started IntygsadminApplication** on ports 8070/8170; test data
  bootstrap via Jackson 3 `JsonMapper` succeeded; process killed afterwards.
- Note: Jackson 2.21.4 still appears transitively via `se.inera.intyg.infra` artifacts — not
  introduced by this project; no application code imports `com.fasterxml.jackson.databind|core`.

### Phase 5 — Autoconfig audit (done)

**Audit areas (intygsadmin):**

| Area | Finding | Action |
| ---- | ------- | ------ |
| Redis cache | No `@Bean RedisCacheManager` | N/A |
| JMS | Not used | N/A |
| Jackson | Manual `JsonMapper` instances | Inject Boot `JsonMapper` bean |
| Session | `SessionConfig` / `IneraCookieSerializer` | Keep — Inera IdP requirement |
| ShedLock | `JobConfig` / `RedisLockProvider` | Keep — ShedLock requires explicit provider |
| RestClient | `ApplicationConfig` / `RestClient.create()` | Deferred — separate ticket |
| Properties | `spring-boot-properties-migrator` at startup | No renames reported |

**Changes:**

- Registered `CustomAuthorizationResolver` as `@Bean`; injects Boot `JsonMapper` (replaces manual
  `JsonMapper.builder().build()`).
- `TestDataBootstrapper` injects Boot `JsonMapper` (replaces manual builder).
- Evaluated `spring-boot-properties-migrator` at startup — no property renames reported; not kept in
  final build.

**Accepted deviations (unchanged):**

- `ObjectMapperConfig` / `JsonMapperBuilderCustomizer` — custom `LocalDate`/`LocalDateTime`
  serializers required by API contract.
- `SessionConfig` / `IneraCookieSerializer` — Inera IdP cookie behaviour.
- `JobConfig` / `RedisLockProvider` — ShedLock distributed locking.
- `ApplicationConfig` / `RestClient.create()` — deferred to separate RestClient autoconfig ticket.

- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.
- `appRunDebug` smoke test with properties migrator — **Started IntygsadminApplication**; no property
  renames logged; process killed afterwards.

### Phase 6 — Integration tests + dependency audit (partial)

**Done (committed):**

- `web/src/test/resources/application.yml`: `spring.session.redis.*` → `spring.session.data.redis.*`
  (Boot 4); aligned `privatepractitionerservice.base.url` and `terminationservice.api` with runtime
  property names.
- Dependency audit documented (see below).

**Deferred** (ITs not run in CI; revisit after `se.inera.intyg.infra` dependency removal):

- `restAssuredTest` Gradle task wiring (currently reports NO-SOURCE without `testClassesDirs` /
  `classpath`).
- `appRunIt` task with integration stub profiles.
- `DataExportControllerIT` Rest Assured / Jackson 3 client deserialization fixes.
- Local `restAssuredTest` verification.

**N/A (skill defaults):** Testcontainers module renames, `@AutoConfigureTestRestTemplate`, `RestTestClient`.

**Dependency audit:**

| Dependency | Verdict |
| ---------- | ------- |
| `spring-boot-starter-test` | Keep — used across modules |
| `spring-security-test` | Keep — `WithMockIntygsadminUser` in unit tests |
| `rest-assured-bom:6.0.0` | Keep — present for future IT work |
| `spring-boot-starter-session-data-redis` | Done (Phase 3) |
| Jackson 2 transitive (infra / Rest Assured client) | Accepted until infra removed |
| Testcontainers | N/A |

- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.

### Phase 8 — Final sign-off (done)

**Resolved versions (verified):**

| Target | Resolved |
| ------ | -------- |
| Java | 25 (`javaVersion` via intyg-bom) |
| Spring Boot | 4.1.0 (`spring-boot-starter-webmvc`) |
| Gradle | 9.6.1 |
| intyg-bom | 1.0.0.17 |
| Jackson (app) | 3.1.4 (`tools.jackson.core:jackson-databind`) |
| Jenkins builder image | 25.0.3 |
| Jenkins runtime image | 25.0.1 |

**Sign-off checks:**

- `spring-boot-properties-migrator` — not in build (evaluated in Phase 5; no renames needed).
- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.
- `appRunDebug` — **Started IntygsadminApplication**; process killed afterwards.
- No application code imports `com.fasterxml.jackson.databind|core|datatype` (Jackson 3 adoption complete).
- Jackson 2.21.4 still transitive via `se.inera.intyg.infra` — accepted until infra removal.

**Outstanding (post-migration):**

- Phase 9 friendliness audit (`SPRING-BOOT-4-AUDIT.md`).
- `restAssuredTest` infrastructure and verification (deferred Phase 6).
- `ApplicationConfig` / `RestClient.create()` autoconfig (separate ticket).
- Remove `se.inera.intyg.infra` dependency (separate initiative).

### Phase 9 — Friendliness audit (pending)

## Lessons learned

- BOM landed at `1.0.0.17` and Gradle at `9.6.1` (newer than skill defaults `1.0.0.16` / `9.6.0`).
- Until Phase 4, web compile classpath carried **both** Jackson 2.21.4 and 3.1.4 — persistence
  direct `com.fasterxml.jackson.core:jackson-databind` was the main Jackson 2 source.
- `ObjectMapperConfig` had to move to Jackson 3 APIs in Phase 2 for Boot 4 compile; document as
  partial Phase 4 work to avoid phase-boundary confusion.
- Properties migrator at startup reported no further renames after manual `spring.session.data.redis.*`
  migration in Phase 2.

## Accepted deviations / deferred follow-ups

- `spring-boot-properties-migrator` was evaluated in Phase 5; not present in final build (no property
  renames needed).
- `SessionConfig` / `IneraCookieSerializer` — keep manual bean (Inera IdP requirement).
- `ObjectMapperConfig` / `JsonMapperBuilderCustomizer` — keep for custom date serialization.
- `JobConfig` / `RedisLockProvider` — keep for ShedLock.
- `ApplicationConfig` / `RestClient.create()` — deferred to separate RestClient autoconfig ticket.
- `restAssuredTest` / `appRunIt` / IT deserialization fixes — deferred until infra removal and CI
  runs ITs (Phase 6 partial).
