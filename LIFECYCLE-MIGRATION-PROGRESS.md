# Lifecycle Migration Progress — intygsadmin (K1J-2265)

## Phase checklist

- [x] Phase 0 — Baseline and documentation
- [x] Phase 1 — Gradle 9 wrapper
- [ ] Phase 2 — intyg-bom + CI images
- [ ] Phase 3 — Spring Boot 4 compile + modular starters
- [ ] Phase 4 — Jackson 3 migration
- [ ] Phase 5 — Autoconfig audit (Redis, JMS, Jackson)
- [ ] Phase 6 — Integration tests + dependency audit
- [ ] Phase 7 — WebClient.Builder + webclient starter
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

### Phase 1 — Gradle 9 wrapper (pending)

### Phase 2 — intyg-bom + CI images (pending)

### Phase 3 — Spring Boot 4 compile + starters (pending)

### Phase 4 — Jackson 3 migration (pending)

### Phase 5 — Autoconfig audit (pending)

### Phase 6 — Integration tests + dependency audit (pending)

### Phase 7 — WebClient auto-config (pending — expected N/A, confirm no WebClient usage introduced)

### Phase 8 — Final sign-off (pending)

### Phase 9 — Friendliness audit (pending)

## Lessons learned

_(to be filled in as phases complete)_

## Accepted deviations / deferred follow-ups

_(to be filled in as phases complete)_
