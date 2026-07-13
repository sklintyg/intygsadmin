# Lifecycle Migration Progress — intygsadmin (K1J-2265)

## Phase checklist

- [x] Phase 0 — Baseline and documentation
- [x] Phase 1 — Gradle 9 wrapper
- [x] Phase 2 — intyg-bom + CI images
- [x] Phase 3 — Spring Boot 4 compile + modular starters
- [x] Phase 4 — Jackson 3 migration
- [x] Phase 5 — Autoconfig audit (Redis, JMS, Jackson)
- [x] Phase 6 — Integration tests + dependency audit
- [ ] Phase 7 — WebClient.Builder + webclient starter — **N/A** (no WebClient beans)
- [ ] Phase 8 — Final sign-off (remove migrator)
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
- Added `runtimeOnly spring-boot-properties-migrator` for property audit (remove in Phase 8).

**Accepted deviations (unchanged):**

- `ObjectMapperConfig` / `JsonMapperBuilderCustomizer` — custom `LocalDate`/`LocalDateTime`
  serializers required by API contract.
- `SessionConfig` / `IneraCookieSerializer` — Inera IdP cookie behaviour.
- `JobConfig` / `RedisLockProvider` — ShedLock distributed locking.
- `ApplicationConfig` / `RestClient.create()` — deferred to separate RestClient autoconfig ticket.

- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.
- `appRunDebug` smoke test with properties migrator — **Started IntygsadminApplication**; no property
  renames logged; process killed afterwards.

### Phase 6 — Integration tests + dependency audit (done)

**N/A (skill defaults):** Testcontainers module renames, `@AutoConfigureTestRestTemplate`, `RestTestClient`.

**Test resource fixes:**

- `web/src/test/resources/application.yml`: `spring.session.redis.*` → `spring.session.data.redis.*`
  (Boot 4); aligned `privatepractitionerservice.base.url` and `terminationservice.api` with runtime
  property names.

**Gradle / IT infrastructure:**

- Fixed `restAssuredTest` task — was **NO-SOURCE** (missing `testClassesDirs` / `classpath` wiring);
  now runs 9 IT classes (24 tests).
- Added `appRunIt` task — starts app with stub profiles (`it-stub`, `wc-stub`, `pp-stub`, `ts-stub`)
  for local/CI Rest Assured runs without external integration services.

**IT fix (Rest Assured 6 / Jackson 3 API dates):**

- `DataExportControllerIT` — compare sort/paging via `jsonPath` string lists instead of typed
  `DataExportResponse` deserialization (Rest Assured client uses Jackson 2; API emits custom
  `LocalDateTime` format from `ObjectMapperConfig`).

**Dependency audit:**

| Dependency | Verdict |
| ---------- | ------- |
| `spring-boot-starter-test` | Keep — used across modules |
| `spring-security-test` | Keep — `WithMockIntygsadminUser` in unit tests |
| `rest-assured-bom:6.0.0` | Keep — ITs verified green |
| `spring-boot-starter-session-data-redis` | Done (Phase 3) — `SESSION` cookie in ITs |
| Jackson 2 transitive (infra / Rest Assured client) | Accepted — no app `databind` imports; ITs avoid typed client deserialization for custom dates |
| Testcontainers | N/A |

**Verification:**

- `clean build spotlessCheck test -x buildReactApp -x copyReactbuild -x testReactApp` — **green**.
- `appRunIt` + `restAssuredTest` — **24/24 passed**; app process killed afterwards.

**Local IT workflow:**

```
./gradlew :intygsadmin-web:appRunIt          # terminal 1
./gradlew :intygsadmin-web:restAssuredTest   # terminal 2
```

### Phase 7 — WebClient auto-config — **N/A**

No `WebClient` beans in codebase.

### Phase 8 — Final sign-off (pending)

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

- `spring-boot-properties-migrator` added in Phase 5 — remove in Phase 8; startup reported no
  property renames needed.
- `SessionConfig` / `IneraCookieSerializer` — keep manual bean (Inera IdP requirement).
- `ObjectMapperConfig` / `JsonMapperBuilderCustomizer` — keep for custom date serialization.
- `JobConfig` / `RedisLockProvider` — keep for ShedLock.
- `ApplicationConfig` / `RestClient.create()` — deferred to separate RestClient autoconfig ticket.
- `restAssuredTest` verified in Phase 6 (24/24 with `appRunIt` + stub profiles).
