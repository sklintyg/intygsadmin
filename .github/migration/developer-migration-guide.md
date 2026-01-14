# 🚀 Code Migration Process Using AI

This document outlines a structured approach to migrating applications between frameworks, libraries,
or versions using AI assistance, with a focus on maintaining code quality and enabling iterative development.

**Supported Migration Types:**
- Backend migrations (e.g., WildFly → Spring Boot, Express → NestJS)
- Frontend migrations (e.g., React 16 → React 18, Angular → React)
- Any framework-to-framework or version-to-version migration

---

## ⚠️ Problem Statement

When using AI for migration tasks, several challenges arise:

- Abstract task definitions lead to inconsistent AI-generated results
- AI-generated code often deviates from established standards
- Building context for AI during code generation is slow
- Difficult to clear AI conversation history while maintaining context

---

## 💡 Solution Framework

### Core Methodology

#### 🎯 Context Enhancement for AI

- ✅ Provide specific requirements and design choices to guide AI generation
- ✅ Follow established patterns to ensure consistent AI output
- ✅ Enable greater control over the AI-driven migration process

#### ⚡ Improved AI Interaction Experience

- Store migration status as context for AI continuity
- Faster AI generation and response times through structured inputs
- Ability to clear AI conversations when needed while preserving essential context
- Support for parallel work streams with independent AI sessions

#### ✔️ Quality Control

- Requires human review of AI-generated context and proposed solutions
- May need AI implementation reset if instructions change significantly
- Validates that AI adheres to coding standards and requirements

---

## 📋 Migration Process

![img.png](img.png)

### 📝 Document Creation Phase

**Migration Type Identification:**

When you start `/migration`, the agent will first ask:
- **Source framework/technology** (e.g., WildFly, React 16, Angular 12)
- **Target framework/technology** (e.g., Spring Boot, React 18, Angular 17)
- Brief description of the migration

**Test Coverage Analysis (Optional but Recommended):**

The agent will ask if you want to analyze test coverage:
- If **Yes** → Agent analyzes coverage and creates `[APP]-test-coverage-analysis.md`
  - Identifies gaps and high-risk areas
  - Establishes baseline for regression prevention
  - Helps prioritize test additions
- If **No** → Proceeds to requirements

**Requirements Document Flow:**

The agent will then:

1. **Check for requirements document**
    - If `[APP]-requirements.md` exists in `instructions/` → Use it
    - If NOT exists → Agent asks: *"We need to create a requirements document that I will then
      enhance. Should we use a template?"*
        - Based on migration type, suggests appropriate template
        - Developer answers **Yes** → Agent helps create from template
        - Developer answers **No** → Agent helps create from scratch

2. **(Optional) Analyze existing target framework patterns** → Creates `[APP]-[target-framework]-design-choices.md`
    - Example: `myapp-spring-design-choices.md`, `myapp-react-design-choices.md`

3. **AI enhances requirements** (Prompt 1.2) → Creates `[APP]-requirements.md` (FINAL)
    - Uses initial requirements + design choices analysis + test coverage findings
    - This becomes the authoritative requirements document

**Then the agent continues with:**

4. Generate analysis instructions for repository examination
5. Create application-specific migration guide (incorporates test coverage findings)
6. Verify guide quality and completeness
7. Establish progress tracking document

> **⚠️ Important:** Templates provide comprehensive starting points with all recommended sections.
> The AI will enhance whatever you provide with application-specific insights.

**Key Documents:**

- **[migration-type]-requirements-template.md** (Template) → **[APP]-requirements.md** (Final)
- **[APP]-test-coverage-analysis.md** (Optional but recommended baseline)

### 🛠️ Implementation Phase

1. Use AI to perform iterative migration following the guide
2. Update progress document after each AI-assisted iteration
3. Mark areas needing developer attention with "OBSERVE"
4. Manually review and update guides as necessary based on AI implementation findings

---

## 🔁 Iteration Strategy

Migration should proceed in small, manageable increments with frequent updates to the progress
document. This approach allows for:

- Thorough examination of each component
- Better management of technical complexity
- Clearer communication about design choices
- Earlier identification of issues requiring developer input

---

## 👨‍💻 Developer Responsibilities

While AI handles the bulk of the migration implementation, developers play a crucial role in
ensuring quality and maintaining project integrity:

### 📚 Context Management

- Provide relevant context documents to the AI at the beginning of each session
- Ensure the AI has access to migration guides, requirements, and progress tracking documents
- Update context documents when design decisions or requirements change

### 🔍 Verification and Review

- Review all AI-generated code changes before accepting them
- Verify that AI responses align with established coding standards and requirements
- Test functionality to ensure AI-implemented changes work as expected
- Check that AI adheres to the migration guide and requirements

### 💾 Version Control

- Commit changes in small, logical increments after verification
- Keep the branch up to date with frequent commits to track progress
- Use descriptive commit messages that reflect the specific migration step completed
- Push changes regularly to maintain backup and enable collaboration

### 📖 Guide Maintenance

- Address all "OBSERVE" markers left by the AI
- Update migration and progress documents based on implementation findings
- Document any deviations from the original plan and rationale

**Quick Update Tool:**
When you encounter issues or need to update the migration approach, use the `/update-migration`
tool:

- Type `/update-migration` in Copilot chat
- Describe the focus area and problem encountered
- AI will help you update the guide and progress document quickly
- Ensures consistency between guide, progress, and requirements

### 📝 Documentation Philosophy

**Keep Documentation Minimal:**

- Do NOT create unnecessary documentation files (README, guides, architecture docs, etc.)
- Focus on working code, not documentation
- The migration guide and progress document are sufficient for tracking
- Code should be self-explanatory with clear naming and structure
- Only update existing documentation if absolutely necessary
- AI should never create documentation files during migration if not asked

---

## 📁 Folder Structure and Organization

### Configuration Files Location

**Backend Applications (e.g., Spring Boot, NestJS):**

- `src/main/resources/application.properties` or `application.yml` - Default configuration
- `src/main/resources/application-dev.*` - Local development configuration
- `src/main/resources/application-test.*` - Test configuration
- External or environment-specific configs in devops folder
- Database migration scripts: `src/main/resources/db/changelog/` (Liquibase) or `src/main/resources/db/migration/` (Flyway)

**Frontend Applications (e.g., React, Angular):**

- `.env` files for environment variables (`.env`, `.env.development`, `.env.production`)
- Configuration files: `src/config/` or `src/constants/`
- Build configuration: `vite.config.js`, `webpack.config.js`, or framework-specific config files
- Test configuration: `jest.config.js`, `vitest.config.js`, `cypress.config.js`

**Test Resources:**

- Backend: `src/test/resources/` - Test-specific overrides, test data, SQL scripts, mock files
- Frontend: `src/tests/` or `src/__tests__/` - Test utilities, fixtures, mock data

**DevOps Configuration:**

- `devops/` folder structure for environment-specific external configurations
- Docker files at project root or in `devops/docker/`
- CI/CD configuration: `.github/workflows/`, `.gitlab-ci.yml`, etc.

### Migration Documentation Location

All migration-related documents should be organized under:

```
.github/migration/
├── templates/
│   ├── [migration-type]-requirements-template.md
│   └── [source-framework]-analysis-template.md
├── instructions/
│   ├── [app]-requirements.md
│   ├── [app]-[target-framework]-design-choices.md (optional)
│   └── [source-framework]-analysis.instructions.md
├── prompts/
│   └── migration.prompt.md
├── examples/ (optional - framework-specific examples)
├── [app]-test-coverage-analysis.md (optional but recommended)
├── [app]-migration-guide.md
├── [app]-migration-progress.md
├── copilot-migration-guide.md
└── developer-migration-guide.md
```

**Examples by Migration Type:**

**Backend (e.g., WildFly → Spring Boot):**
```
.github/migration/
├── templates/
│   ├── wildfly-to-spring-requirements-template.md
│   └── wildfly-analysis-template.md
├── myapp-test-coverage-analysis.md
├── myapp-migration-guide.md
└── myapp-migration-progress.md
```

**Frontend (e.g., React 16 → React 18):**
```
.github/migration/
├── templates/
│   ├── react-requirements-template.md
│   └── react-analysis-template.md
├── myapp-test-coverage-analysis.md
├── myapp-migration-guide.md
└── myapp-migration-progress.md
```

---

## ✨ Success Criteria

The migration is considered successful when:

- ✅ Application runs on target framework with equivalent functionality
- ✅ Code adheres to modern target framework standards and best practices
- ✅ Technical debt is minimized
- ✅ All developer "OBSERVE" items have been addressed
- ✅ All source framework dependencies removed
- ✅ Configuration properly organized
- ✅ Tests migrated to target framework testing patterns
- ✅ Application builds successfully
- ✅ Test coverage maintained or improved (if baseline established)
