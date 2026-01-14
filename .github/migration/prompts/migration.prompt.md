---
description: 'Reusable prompts for code migration tasks'
mode: agent
---

# Code Migration Prompts

This document contains reusable prompts for specific tasks in the migration process. Use these
prompts with an AI agent to guide code migrations between frameworks, libraries, or versions.

**Note:** Replace `[SOURCE]` and `[TARGET]` with your specific migration (e.g., WildFly → Spring Boot, React 16 → React 18).

---

## Prompt 1.1: Create Requirements Inspiration Document

> [ℹ️] **For Developers:** This prompt analyzes your existing target framework code patterns to document
> current design choices, creating a baseline for migration requirements.

**Objective:** Analyze existing design choices in a reference application using the target framework.

**Instructions:**

Analyze the design choices made in this repository regarding its use of [TARGET FRAMEWORK]. Summarize your findings in a document named `[YOUR-APP]-[target-framework]-design-choices.md`.

Your analysis should cover areas relevant to the target framework, such as (adapt based on migration type):

**For Backend Frameworks (e.g., Spring Boot):**
- **Dependency injection strategy** (constructor injection, field injection, configuration classes, component scanning)
- **Configuration approach** (YAML/properties, profiles, environment separation)
- **Security design** (authentication, authorization, filters, security config classes, password handling, token handling, CSRF, method-level security)
- **Data access choices** (ORM, repositories, transactions, query patterns)
- **Error handling** (exception mappers, controllers, global handlers)
- **REST API design** (controllers, DTOs, input validation, response mapping)
- **Testing strategy** (Integration tests, unit tests, test slices, mocking frameworks)
- **Logging strategy** (logging framework, structured logging, log levels)
- **Build configuration** (build tool, packaging, dependencies)

**For Frontend Frameworks (e.g., React):**
- **Component architecture** (functional vs class components, hooks usage)
- **State management** (local state, context, external libraries like Redux)
- **Routing approach** (routing library, route structure, navigation patterns)
- **Data fetching strategy** (fetch libraries, caching, error handling)
- **Styling approach** (CSS modules, styled components, CSS-in-JS, utility frameworks)
- **Testing strategy** (unit tests, component tests, E2E tests, testing libraries)
- **Build configuration** (bundler, optimization, environment variables)
- **Performance patterns** (code splitting, lazy loading, memoization)

Be as detailed as possible and connect each observed design choice to its implications (benefits,
drawbacks, trade-offs).

---

## Prompt 1.2: Enhance Requirements Document

> [ℹ️] **For Developers:** This prompt enhances your requirements document with application-specific
> insights from the design choices analysis and test coverage analysis (if performed).

**Objective:** Enhance the requirements document with comprehensive migration insights.

**Instructions:**

You have a `[YOUR-APP]-requirements.md` document that was created in Step 2 (either from the
template or from scratch).

Enhance this document using insights from:
- `[YOUR-APP]-[target-framework]-design-choices.md` (if available from Step 3)
- `[YOUR-APP]-test-coverage-analysis.md` (if available from Step 1)

Keep it concise and focused.

**Your Task:**

- Review the current requirements document
- Add application-specific insights from the design choices analysis
- Incorporate test coverage findings if available (high-risk areas, testing priorities)
- Clarify any vague sections with concrete details
- Ensure all critical areas are covered
- Keep it concise - enhance, don't duplicate

**Output:** Enhanced `[YOUR-APP]-requirements.md` ready for use in creating the migration guide.

---

## Prompt 2: Generate General Analysis Document

> [ℹ️] **For Developers:** This prompt generates detailed instructions for how the AI should analyze
> your repository in order to perform the migration, ensuring a structured approach that enables safe,
> incremental migration. This document is general and can be used independent of the specific application
> being migrated.

**Objective:** Create comprehensive analysis instructions for the migration process.

**Instructions:**

Create a step-by-step in-depth description as a `.md` file of how to analyze a code repository that
will be migrated from [SOURCE] to [TARGET]. The description should explain in detail how the
repository should be examined, what components need to be identified, and what information must be
collected.

It should be written so that it can be placed inside a code repository and used as instructions for
an AI agent to follow during the analysis phase. Go in depth when describing the analysis so that
the AI agent can perform a complete analysis. Instruct the agent to mark unanswered questions in the
analysis with `OBSERVE` so that the developer can manually investigate.

**Format:**

The analysis document should be structured to enable incremental migration where the application
remains functional after each iteration. Structure the resulting document with clear, logical
increments that:

- Allow the application to work after each migration step
- Minimize the risk of breaking the entire application
- Enable testing and verification at each stage
- Prioritize foundational changes before dependent features
- Group related changes together to maintain consistency

The goal is to avoid situations where everything crashes. Instead, each increment should be
self-contained and leave the application in a working state.

**Include Analysis Sections for (adapt based on migration type):**

**For Backend Migrations (e.g., WildFly → Spring Boot, Express → NestJS):**
1. **Project Structure Analysis** - Multi-module setup, build configuration, packaging
2. **Dependency Analysis** - Current dependencies, what needs to be replaced, target framework packages needed
3. **Data Layer Analysis** - Database access, ORM, entities, datasource configuration, transaction management
4. **Service Layer Analysis** - Business logic, dependency injection, service boundaries
5. **API Layer Analysis** - REST/GraphQL endpoints, request/response handling, error handling
6. **Configuration Analysis** - Configuration files, environment-specific configs, secrets management
7. **Testing Analysis** - Existing tests, frameworks used, what needs migration
8. **Logging Analysis** - Current logging framework, migration path
9. **Security Analysis** - Authentication, authorization, security configurations
10. **Integration Analysis** - External systems, messaging, caching, etc.
11. **Deployment Analysis** - Packaging, container configuration, DevOps setup
12. **Cleanup and Optimization Analysis** - What will need cleanup after migration

**For Frontend Migrations (e.g., React 16 → React 18, Angular → React):**
1. **Project Structure Analysis** - Component structure, directory organization, build setup
2. **Dependency Analysis** - Current packages, what needs updates/replacement
3. **Component Architecture Analysis** - Component types, patterns used, state management
4. **State Management Analysis** - Current solution, migration path to target pattern
5. **Routing Analysis** - Routing library, route definitions, navigation patterns
6. **Data Fetching Analysis** - API integration, caching, loading states
7. **Styling Analysis** - Styling solution, theme management, responsive design
8. **Testing Analysis** - Test frameworks, component tests, E2E tests
9. **Build Configuration Analysis** - Build tool, optimization, environment variables
10. **Performance Analysis** - Code splitting, lazy loading, optimization opportunities
11. **Accessibility Analysis** - Current state, areas for improvement
12. **Cleanup and Optimization Analysis** - Deprecated patterns to remove, modern alternatives

**Emphasize:**

- Prefer built-in features of the target framework over custom implementations
- Identify opportunities to simplify code with target framework features
- Focus on creating clean, maintainable applications in the target framework
- Suggest removal of unnecessary custom code that the target framework handles
- Include cleanup steps as part of the migration guide (not as separate afterthought)

> **💡 Developer Tip:** Enhance document creation with several models  
> **Follow-up Prompt:** Independent of this repository, is this instruction missing something?

---

## Prompt 3: Generate Application-Specific Guide

> [ℹ️] **For Developers:** This is the **most critical step** in the migration process. This prompt
> creates a comprehensive migration guide specific to your application. The quality and completeness
> of this guide determines how smooth the implementation phase will be.

**Objective:** Create a complete, detailed, tailored migration guide for the specific application.

> **⚠️ CRITICAL:** This step **MUST** be done iteratively. This is the heart of the entire
> migration. Do NOT rush through this step.

**Task:**

Using the `[source-framework]-analysis.instructions.md` and `[YOUR-APP]-requirements.md` as instructions, go
through this repository to perform a comprehensive analysis of how to change the application from
[SOURCE] to [TARGET].

**Include Test Coverage:** If `[YOUR-APP]-test-coverage-analysis.md` exists (from Step 1), incorporate
those findings into the migration plan, prioritizing high-risk areas identified in the coverage analysis.

**Priority:** The requirements document holds higher importance. If you find conflicting
instructions between the requirements and the analysis instructions, always choose the option
specified in the requirements document.

**Completeness:** The guide must be comprehensive and cover:

- Every file that needs to be changed
- Every dependency that needs to be added or removed
- Every configuration that needs to be migrated
- Specific code patterns and examples for the migration
- Clear, actionable steps that can be followed in Phase 2
- Test coverage considerations for high-risk areas

**Strategy - MANDATORY ITERATIVE APPROACH:**

⚠️ **This MUST be done iteratively** - divide the analysis into multiple focused iterations based on
the structure provided in `[source-framework]-analysis.instructions.md`:

- Follow the analysis sections defined in the general analysis guide
- Work through one major area at a time (e.g., dependencies, data layer, service layer, API layer,
  configuration, etc.)
- For each iteration:
    - Analyze that specific area in depth
    - Provide detailed findings with specific code examples or patterns
    - Identify all changes needed for that area
    - Mark any uncertainties with `OBSERVE`
    - Present findings and wait for developer confirmation
    - Allow developer to ask questions or request deeper analysis before moving to next area

**After Each Iteration:**

- Provide detailed findings for that area
- Include specific code examples or patterns
- Mark any uncertainties with `OBSERVE`
- Wait for developer confirmation before proceeding to next iteration
- Allow developer to ask questions or request deeper analysis

**Quality Over Speed:**

- It is more important that each section is covered in depth than that it is done fast
- A complete, thorough guide makes implementation smooth and predictable
- Incomplete guide = problems in implementation phase

**Remember:**

Focus should be on building a technically up-to-date and correct application with minimal technical
debt. Therefore, if you identify something that could be improved or refactored in order to utilize
the target framework in an optimal way, include that in the analysis. However, the main focus of the
analysis is the migration itself, so finding other refactoring suggestions should not be prioritized.

**Result:**

This whole analysis should result in a document called `[YOUR-APP]-migration-guide.md`. This
document should be comprehensive, detailed, and actionable - it is the roadmap for the entire
implementation phase.

**Structure of the Resulting Guide:**

The guide must be structured to enable iterative, incremental implementation in Phase 2:

- Divide the migration into clear, logical increments (not just analysis areas)
- Each increment should be a complete, self-contained unit of work
- Order increments so the application remains functional after each one
- Prioritize foundational changes (dependencies, configuration) before features
- Group related changes together to maintain consistency
- **Include final cleanup and optimization steps** based on requirements Section 8 and general
  analysis
- Each increment should specify:
    - What will be changed
    - Files affected
    - Dependencies to add/remove
    - Configuration changes needed
    - How to verify the increment worked (build, test, start)

**Final Steps in the Guide:**

The guide must include post-migration cleanup and optimization steps at the end. Combine guidance
from:

- Requirements document (Post-Migration Cleanup and Optimization)
- General analysis instructions about cleanup
- Application-specific cleanup needs identified during analysis

The goal is to create a guide that can be followed step-by-step in Phase 2, where each step leaves
the application in a working, buildable state, and ends with a clean, optimized application in the
target framework.

---

## Prompt 3.5: Verify Guide Quality and Completeness

> [ℹ️] **For Developers:** This prompt validates that the migration guide is comprehensive and
> complete before proceeding to implementation. It identifies gaps and recommends iterations to
> improve the guide.

**Objective:** Analyze the migration guide for completeness and quality, recommend improvements.

**Task:**

Review the `[YOUR-APP]-migration-guide.md` that was just created and verify it is
comprehensive and ready for implementation.

**Quality Checklist:**

Analyze the guide against the following criteria:

1. **Coverage Completeness**
    - [ ] All modules/layers analyzed (dependencies, data/state layer, service/business layer, API/component layer,
      configuration, tests, logging)
    - [ ] Every file that needs changes is identified
    - [ ] All dependencies to add/remove are specified
    - [ ] All configuration changes are documented
    - [ ] Cleanup and optimization steps included at the end

2. **Increment Structure**
    - [ ] Migration divided into clear, logical increments
    - [ ] Increments are ordered properly (foundational → features)
    - [ ] Each increment is self-contained (leaves app functional)
    - [ ] Each increment specifies what changes, files affected, dependencies, configuration
    - [ ] Each increment includes verification steps (build, test, start)

3. **Detail Level**
    - [ ] Specific code patterns and examples provided
    - [ ] Clear migration instructions (not vague)
    - [ ] Requirements document properly referenced
    - [ ] Target framework best practices incorporated

4. **OBSERVE Flags**
    - [ ] All uncertainties marked with `OBSERVE`
    - [ ] Design decisions flagged for developer input
    - [ ] External dependencies or configurations noted

5. **Requirements Alignment**
    - [ ] Follows requirements document (highest priority)
    - [ ] Testing strategy from requirements applied
    - [ ] Logging strategy from requirements applied
    - [ ] Naming conventions from requirements applied

**Analysis Format:**

Present your analysis in this format:

```
📊 Guide Quality Analysis

✅ Strengths:
- [List what the guide does well]

⚠️ Gaps Identified:
1. [Area]: [What's missing or needs more detail]
2. [Area]: [What's missing or needs more detail]
...

🔄 Recommended Iterations:
1. [Specific area to iterate on]: [Why and what to add]
2. [Specific area to iterate on]: [Why and what to add]
...

📈 Overall Assessment:
- Completeness: [X/10]
- Detail Level: [X/10]
- Ready for Implementation: [Yes/No]

💡 Recommendation:
[Recommend: "Proceed to Prompt 4" OR "Iterate on areas X, Y, Z before proceeding"]
```

**Decision Point:**

After presenting the analysis, ask the developer:

```
Based on this analysis, would you like to:
A) Proceed to creating the progress document (Prompt 4)
B) Iterate on specific areas to improve the guide

If B, which areas should I focus on?
```

Wait for developer response before proceeding.

---

## Prompt 4: Generate Progress Document

> [ℹ️] **For Developers:** This prompt creates a progress tracking document that allows the AI to
> work incrementally through the migration guide while marking areas that need your attention.

**Objective:** Create a progress tracking document for iterative migration.

**Instructions:**

Generate a progress document `[YOUR-APP]-migration-progress.md` that can be used to iterate
over the `[YOUR-APP]-migration-guide.md`.

The progress document should:

- Give context to enable the agent to work in iterations
- Be updated when the agent iterates over the migration guide
- Mark areas needing developer attention with `OBSERVE` (e.g., design choices or similar)

**Required Structure:**

The progress document must include the following sections:

1. **Migration Status Overview**
    - Overall completion percentage (e.g., "35% complete")
    - Current phase (Initialization, Implementation, Completion)
    - Last updated timestamp

2. **Increment Tracking**
    - List of all increments from the migration guide
    - Status for each increment:
        - ✅ Done (completed and verified)
        - 🔄 In Progress (currently working on)
        - ⏸️ Blocked (waiting on developer input or resolution)
        - ⏹️ Not Started
    - Notes for each increment (what was changed, files affected, etc.)

3. **OBSERVE Items**
    - List of all flagged items needing developer attention
    - Each item should include:
        - Focus area (e.g., "REST Controllers - Error Handling")
        - Description of the issue/question
        - Status (Pending, Resolved)

4. **Build/Test Status**
    - Last build result (Success/Failure)
    - Last test run result (X/Y tests passing)
    - Application startup status (Yes/No)

5. **Metrics**
    - Files changed
    - Dependencies added/removed
    - Tests migrated
    - Any other relevant metrics

**Example:**

Generate a progress document `[YOUR-APP]-migration-progress.md` that can be used to
iterate over the `[YOUR-APP]-migration-guide.md`. The progress document should give
context to enable the agent to work in iterations and include all required sections listed above.

---

## Prompt 5: Perform Migration Changes

> [ℹ️] **For Developers:** This is the main execution prompt where the AI performs the actual
> migration work in small increments, updating progress and flagging issues for your review.

**Objective:** Execute the migration in small, tracked increments.

> **⚠️ Note:** This step requires iteration

### Conditions

You will have two documents:

1. `[YOUR-APP]-migration-guide.md` - which explains how this application should be
   migrated from [SOURCE] to [TARGET]
2. `[YOUR-APP]-migration-progress.md` - which includes the progress you have made
   going over the guide and performing the migration as well as `OBSERVE` markers where you need the
   developer's attention

### Action

Use the guide to perform the migration for this repository from [SOURCE] to [TARGET].

**Requirements:**

- Perform changes in small increments
- Update progress document after every increment
- Read necessary files to get context for each iteration
- Verify application builds after each increment (e.g., `./gradlew build`, `npm run build`, `yarn build`)
- Do not proceed if build fails

### Follow Guide Document

All technical decisions, best practices, and guidelines are in the guide but if you are unsure about
something you can also look at the requirements and update the guide. However, notify the developer
about this.

### Remember

- **Communication:** Communicate any doubts to the developer
- **Escalation:** Add `OBSERVE` in progress and end iteration if more information needed
- **Focus:** Technically up-to-date application with minimal technical debt
- **Improvement:** Suggest target framework optimizations if identified
- **No Documentation Files:** Only update code and progress document
- **Clean Dependencies:** Remove unused dependencies during migration

---

## Prompt 6: Update Guide and Progress Manually

> [ℹ️] **For Developers:** Use this prompt when concerns arise during migration to update the guide
> and progress documents, ensuring the AI has accurate instructions going forward. Tool
> /migrate-update can also be used.

**Objective:** Address concerns and update documentation based on implementation findings.

### Problem

During the implementation of the migration from [SOURCE] to [TARGET], concerns have been raised.

### Solution

The guide `[YOUR-APP]-migration-guide.md` needs to be updated to address these concerns:
`[LIST CONCERNS]`.

After updating the guide, verify if the progress document needs to be updated as well.

