# 🚀 Copilot Migration Guide: Automated Code Migration Process

This guide is the sole context for the AI agent (Copilot) to autonomously perform code migrations,
including iterations, document updates, and developer guidance.

---

> **Specification**  
> **app to migrate:** intygsadmin
> **migration type:** React frontend, to React 18 from 16  
> **app to use as inspiration:** intyg-frontend

---

## 📋 Migration Process Overview

The migration follows a structured, iterative approach with clearly defined phases, increments, and
iterations.

**🎯 Key Success Factor:** The application-specific migration guide (created in Phase 1, Step 5) is
the **heart of the entire migration**. A thorough, complete, and well-analyzed guide makes the
implementation phase predictable and successful. Invest time in creating a comprehensive guide
through multiple iterations.

### Terminology

- **Phase**: A major stage of the migration process (e.g., Initialization, Implementation,
  Completion)
- **Step**: A distinct task within a phase (e.g., "Generate Requirements Document", "Migrate REST
  Controllers")
- **Increment**: A complete, deployable unit of work that leaves the application functional (e.g.,
  migrating one module or layer)
- **Iteration**: A single work cycle within an increment or step (e.g., analyzing one controller,
  updating one entity)

### Key Principles

- **Incremental Migration**: Each increment leaves the application functional and buildable.
- **Build After Each Increment**: Always verify the application builds successfully after completing
  an increment.
- **Developer Priority**: Requirements document takes precedence over all other guidance.
- **AI Autonomy**: Agent handles analysis, code changes, and documentation.
- **Human Oversight**: Flag uncertainties with `OBSERVE`.
- **Prefer Target Framework Solutions**: Use built-in features of the target framework over custom implementations.

### Migration Phases at a Glance

Optional steps cannot be skipped unless developer chooses to skip them when asked. 

1. **Phase 1: Initialization (Foundation Building)** 🏗️
    - **Step 1 (Optional but Recommended)**: Analyze test coverage to identify gaps and prevent regressions
    - Create requirements and analysis documents  
    - **⭐ MOST CRITICAL**: Build comprehensive application-specific migration guide (iteratively)
    - Guide must include cleanup and optimization as final steps
    - Agent should look through migration/examples folder for example code and reference these when applicable
    - Create progress tracking document
    - All documents need to be complete and approved by developer before next phase can begin
    - **Success Criteria**: A complete, detailed guide that covers every aspect of migration
      including cleanup, with test coverage baseline established

2. **Phase 2: Implementation (Execute the Plan)** 🔨
    - Follow the guide created in Phase 1 (all steps including cleanup)
    - Work in small increments
    - Build and verify after each increment
    - Update progress document
    - **Success Criteria**: All migration steps from guide completed (including cleanup)

3. **Phase 3: Completion (Finalize and Verify)** ✅
    - Resolve all OBSERVE items
    - Verify build, tests, and application startup
    - **Developer satisfaction check** (always required)
    - **Success Criteria**: Developer is satisfied migration is complete

---

## 📚 Documents and Artifacts

The agent creates and maintains these documents:

### 0. Test Coverage Analysis Document (`[YOUR-APP]-test-coverage-analysis.md`)

- **Purpose**: Baseline test coverage analysis to prevent regressions
- **Location**: `.github/migration/` (peer to migration guide and progress)
- **When Used**: Phase 1, Step 1 (Optional but Highly Recommended) - BEFORE any other documents
- **Creation**: Agent-generated through analysis of existing test coverage
- **Content**:
    - Current coverage metrics (baseline)
    - Files/components/classes with inadequate coverage
    - Prioritized test gaps (Critical/High/Medium/Low)
    - High-risk migration areas
    - Recommended test types to add
    - OBSERVE flags for unclear business logic
- **Priority**: Informs requirements and migration guide creation
- **Usage**: Referenced in requirements enhancement and migration guide generation
- **Benefits**: Prevents regressions, establishes quality baseline, enables safe refactoring
- **Recommended for**: All production applications and complex migrations

### 1. Base Requirements Template (`templates/[migration-type]-requirements-template.md`)

- **Purpose**: Starting point template for creating requirements document
- **Location**: `.github/migration/templates/`
- **When Used**: Phase 1, Step 1 - if requirements don't exist
- **Usage Flow**:
    1. Agent asks developer what type of migration is being performed
    2. Agent checks if `[YOUR-APP]-requirements.md` exists
    3. If NOT exists → Agent asks developer: "Should we use the template?"
    4. If Yes → Agent uses appropriate template for the migration type
    5. Then in Step 3: Agent enhances it with insights from design choices analysis
- **Note**: Template filename depends on migration type (e.g., `wildfly-to-spring-requirements-template.md`, `angular-to-react-requirements-template.md`)
- **Recommended for**: First-time migrations or when starting from scratch

### 1.1. Analysis Instructions Template (`templates/[source-framework]-analysis-template.md`)

- **Purpose**: Starting point template for creating general analysis instructions
- **Location**: `.github/migration/templates/`
- **When Used**: Phase 1, Step 5 - when generating general analysis instructions
- **Usage Flow**:
    1. Agent uses template as base structure for analysis instructions
    2. Agent customizes based on common patterns for the specific migration type
    3. Results in `[source-framework]-analysis.instructions.md`
- **Improvement Mechanism**: Updated at end of migration (Phase 3, Step 5) with learnings
- **Note**: Template filename depends on source framework (e.g., `wildfly-analysis-template.md`, `react-analysis-template.md`)
- **Note**: Does NOT include test coverage analysis (that's Step 1, separate document)
- **Recommended for**: Providing consistent analysis framework across migrations

### 2. Requirements Document (`[YOUR-APP]-requirements.md`)

- **Purpose**: Comprehensive requirements for the migration
- **Creation**:
    - Step 2: Created from template or from scratch based on migration type
    - Step 4: Enhanced with insights from inspiration document and test coverage analysis (if available)
- **Priority**: Highest; resolve conflicts in favor of this document
- **Location**: `.github/migration/instructions/`
- **Note**: This is the authoritative requirements used throughout migration

### 3. Inspiration Document (`[YOUR-APP]-[target-framework]-design-choices.md`)

- **Purpose**: Analyzes existing patterns in target framework from reference application
- **Creation**: Prompt 1.1 (optional)
- **Location**: `.github/migration/instructions/`
- **Use**: Provides insights to enhance requirements in Step 4
- **Note**: Filename depends on target framework (e.g., `myapp-spring-design-choices.md`, `myapp-react-design-choices.md`)

### 4. General Analysis Instructions (`[source-framework]-analysis.instructions.md`)

- **Purpose**: Step-by-step repository analysis guide (excludes test coverage - that's Step 1)
- **Creation**: Phase 1, Step 5 (Prompt 2) - based on `templates/[source-framework]-analysis-template.md`
- **Note**: Requirements override when creating application guide
- **Location**: `.github/migration/instructions/`
- **Improvement Mechanism**: Template can be updated at migration completion with learnings

### 5. Application-Specific Guide (`[YOUR-APP]-migration-guide.md`)

- **Purpose**: Tailored migration guide
- **Creation**: Prompt 3 using requirements, analysis, and test coverage (if available)
- **Priority**: Requirements used here, not general analysis
- **Location**: `.github/migration/`

### 6. Progress Document (`[YOUR-APP]-migration-progress.md`)

- **Purpose**: Tracks progress and iterations.
- **Creation**: Prompt 4.
- **Updates**: Agent updates after each iteration, marking `OBSERVE`.
- **Location**: `.github/migration/`

---

## 🔄 Agent Workflow

The agent follows this sequence autonomously, with details on inputs, outputs, and iteration for
each step.
Each phase is divided into steps. For each step the agent should:

1. If the first step of the phase, describe the phase. If the step is iterative, describe the
   iteration process for the first iteration.
2. Describe the step. If the step is iterative, describe the current iteration.
3. Start with describing what you are planning on doing to complete the step, or what it needs from
   the developer.
   Use format:
   🤖 Agent plan:
   👩‍💻 Developer input needed:
4. Wait for developer confirmation to proceed with whole step OR iteration. THIS CANNOT BE SKIPPED.
5. **Update the progress document** with what was completed in this iteration.
6. Summarize what has been done and ask for developer confirmation to move onto first point (
   describe)
   for next step OR next iteration.

**Important**: In Phase 2 (Implementation), each increment follows a structured **4-step multi-agent
workflow** (Plan → Implement → Validate → Quality Check). The agent MUST update the progress
document after EVERY increment passes the quality check. This ensures high-quality, validated
changes at each step.

---

**Purpose**: Build the foundation for a successful migration. The quality of documents created here,
especially the application-specific migration guide, determines how smooth Phase 2 will be.

**Critical Success Factor**: Do NOT rush through this phase. A complete, well-analyzed migration
guide is worth the investment - it will save time and prevent issues in implementation.

For this phase we are setting up the necessary documents to enable the migration. The agent should
first determine the migration type, then check if documents exist in `.github/migration/instructions` folder,
otherwise ask developer for it or generate it based on the instructions.

0. **Determine Migration Type**
    - **Action**: Ask developer what type of migration is being performed
    - **Format**:
      ```
      🔍 Migration Type Identification
      
      To provide the best guidance, I need to understand what type of migration you're performing.
      
      Please specify:
      1. Source framework/technology: [e.g., WildFly, Angular 12, Vue 2, etc.]
      2. Target framework/technology: [e.g., Spring Boot, React, Vue 3, etc.]
      3. Brief description: [e.g., "Migrating REST API from WildFly to Spring Boot"]
      
      This will help me:
      - Select the appropriate requirements template
      - Use correct analysis patterns
      - Generate accurate migration steps
      - Name documents appropriately
      ```
    - **Wait for developer response**
    - **Output**: Migration type determined and recorded for use throughout process
    - **Note**: This information will be used to:
        - Select correct template from `templates/` folder
        - Name documents appropriately (e.g., `[YOUR-APP]-migration-guide.md`)
        - Customize analysis instructions
        - Reference appropriate examples from `examples/` folder

1. **Analyze Test Coverage and Identify Gaps (Optional but Highly Recommended)**
    - **⭐ HIGHLY RECOMMENDED**: Prevents regressions during migration by ensuring adequate test coverage
    - **Action**: Ask developer if they want to analyze test coverage before creating any migration documents
    - **Purpose**: 
        - Identify critical paths with missing tests
        - Prevent regressions during migration
        - Establish baseline for post-migration verification
        - Prioritize adding tests before structural changes
        - Create foundation for safe migration
    - **When to Perform**: BEFORE creating requirements or analysis documents (early baseline)
    - **Analysis Steps**:
        1. **Run Coverage Report**: Execute test coverage tool (Jest, Istanbul, pytest, JaCoCo, etc.)
        2. **Analyze Coverage Metrics**:
            - Overall coverage percentage (lines, branches, functions, statements)
            - Identify files/components/classes with low or no coverage
            - Find critical business logic without tests
            - Locate complex code that will be migrated
        3. **Identify High-Risk Areas**:
            - Core business logic with <70% coverage
            - Complex components/classes being migrated (e.g., class components, EJBs, legacy patterns)
            - Critical user flows without E2E/integration tests
            - Integration points (APIs, databases, external services)
        4. **Create Test Coverage Analysis Document**: `[YOUR-APP]-test-coverage-analysis.md`
            - **Current coverage metrics** (baseline numbers)
            - **List of files/components with inadequate coverage** (by priority)
            - **Prioritized list of tests to add** (Critical, High, Medium, Low)
            - **Recommended test types** (unit, component/integration, E2E)
            - **High-risk migration areas** needing tests
    - **Output**: `[YOUR-APP]-test-coverage-analysis.md` in `.github/migration/` with:
        - Coverage baseline (to compare post-migration)
        - Test gaps identified with priority levels
        - Prioritized recommendations for adding tests
        - OBSERVE flags for areas where business logic is unclear
        - Risk assessment for migration
    - **Developer Decision Point**: After analysis, developer decides:
        - **Option A**: Add critical missing tests now (recommended for high-risk areas)
        - **Option B**: Document gaps and add tests incrementally during migration
        - **Option C**: Skip and proceed (not recommended for production apps)
    - **Location**: `.github/migration/` (peer to migration guide and progress)
    - **Benefits**:
        - Catch regressions early
        - Build confidence in migration
        - Establish quality baseline
        - Safer refactoring during migration
        - Provides data for requirements document
    - **Iterative**: No (single analysis), but adding tests can be iterative
    - **If No**: Skip to Step 2
    - **Note**: This analysis informs the requirements document and migration guide creation

2. **Request Initial Requirements**
    - **Action**: Check if `[YOUR-APP]-requirements.md` exists in
      `.github/migration/instructions/`
    - **If EXISTS**: Proceed to Step 3
    - **If NOT EXISTS**: Ask developer:
      ```
      📋 Requirements Document Needed
      
      We need to create a requirements document that I will enhance with application-specific insights in Step 4.
      
      Based on your migration type ([SOURCE] to [TARGET]), I can use a template if available:
      `templates/[migration-type]-requirements-template.md`
      
      Available options:
      1. Use existing template (if available for this migration type)
      2. Use a general requirements template and customize it
      3. Create requirements from scratch
      
      Which approach would you prefer?
      ```
    - **Wait for developer response**
    - **Based on response**: Create `[YOUR-APP]-requirements.md` using selected approach
    - **Note**: If test coverage analysis was performed (Step 1), reference those findings here
    - **Output**: `[YOUR-APP]-requirements.md` (will be enhanced in Step 4)

3. **Request Inspiration Document (Optional)**
    - **Action**: Ask developer if they have an application in the target framework to analyze for design
      patterns
    - **If Yes**: Developer will execute Prompt 1.1 to analyze and create
      `[YOUR-APP]-[target-framework]-design-choices.md` and copy it in this repository
    - **Wait for developer confirmation that document is available**
    - **If No**: Skip to Step 4
    - **Output**: `[YOUR-APP]-[target-framework]-design-choices.md` (optional)

4. **Execute Prompt 1.2: Enhance Requirements Document**
    - **Purpose**: Enhance the initial requirements with application-specific insights
    - **Input**:
        - `[YOUR-APP]-requirements.md` (from Step 2)
        - `[YOUR-APP]-[target-framework]-design-choices.md` (if available from Step 3)
        - `[YOUR-APP]-test-coverage-analysis.md` (if available from Step 1)
    - **Output**: Enhanced `[YOUR-APP]-requirements.md`
    - **Action**: Agent reviews and enhances requirements with specific insights
    - **Prompt**: See Prompt 1.2 in `prompts/[migration-type].prompt.md`
    - **Iterative**: No.

5. **Execute Prompt 2: Generate General Analysis Instructions**
    - **Needed**: None.
    - **Input**: `templates/[source-framework]-analysis-template.md` (as base structure).
    - **Output**: `[source-framework]-analysis.instructions.md` (general analysis guide).
    - **Iterative**: No.
    - **Action**: Agent uses template as foundation and customizes for the specific migration patterns
    - **Prompt**: See Prompt 2 in `prompts/[migration-type].prompt.md`.
    - **Follow-up**: After executing Prompt 2, ask the developer if they want to perform the
      follow-up prompt: "Independent of this repository, is this instruction missing something?"
      Print the full follow-up prompt details for the developer to decide.

6. **Execute Prompt 3: Generate Application-Specific Guide**
    - **⭐ CRITICAL**: This is the heart of the migration. The quality of this guide determines
      migration success.
    - **Needed**: Requirements and analysis instructions.
    - **Input**: 
        - `[YOUR-APP]-requirements.md` (from Step 4)
        - `[source-framework]-analysis.instructions.md` (from Step 5)
        - `[YOUR-APP]-test-coverage-analysis.md` (if available from Step 1)
        - Repository analysis
    - **Output**: `[YOUR-APP]-migration-guide.md` (tailored migration guide).
    - **Iterative**: ⚠️ **YES - MANDATORY**. This MUST be done iteratively:
        - Divide repository analysis into multiple iterations
        - Each iteration focuses on one area (dependencies, data layer, services, controllers, etc.)
        - Go deep in each iteration - this guide must be complete and detailed
        - Wait for developer confirmation after each iteration
        - The guide must cover ALL aspects needed for migration
        - **Guide must include cleanup and optimization as final steps** (combining requirements
          Section 8 and general analysis)
        - **Incorporate test coverage findings** if Step 1 was performed
        - A complete, thorough guide makes Phase 2 implementation smooth and predictable
    - **Prompt**: See Prompt 3 in `prompts/[migration-type].prompt.md`.

7. **Execute Prompt 3.5: Verify Guide Quality and Completeness**
    - **⭐ QUALITY GATE**: This step validates the guide is ready for implementation.
    - **Input**: 
        - `[YOUR-APP]-migration-guide.md` (from Step 6)
        - `[YOUR-APP]-requirements.md` (from Step 4)
    - **Output**: Quality analysis with recommendations
    - **Action**:
        - Agent analyzes guide against quality checklist
        - Identifies gaps and missing details
        - Recommends iterations to improve guide OR proceed to next step
        - Developer decides: proceed or iterate to improve specific areas
    - **Iterative**: Yes if gaps identified - return to Step 6 to improve specific areas
    - **Prompt**: See Prompt 3.5 in `prompts/[migration-type].prompt.md`.

7. **Execute Prompt 4: Generate Progress Document**
    - **Needed**: Verified, complete application-specific guide.
    - **Input**: `[YOUR-APP]-migration-guide.md` (from Step 6).
    - **Output**: `[YOUR-APP]-migration-progress.md` (progress tracker).
    - **Iterative**: No.
    - **Prompt**: See Prompt 4 in `prompts/[migration-type].prompt.md`.

### Phase 2: Implementation

For this phase we are performing the actual migration using the created documents.

**Agent Responsibility**: The agent MUST update the progress document after EVERY increment. This is
not optional.

**Multi-Agent Workflow**: Each increment follows a structured 4-step approach that ensures quality:

```
PLAN → IMPLEMENT -> RENAME/MOVE FILES (IF APPLICABLE) → VALIDATE → QUALITY CHECK
  ↓         ↓           ↓            ↓
  📋        🔨          ✅           🔍
                                     ↓
                               [Pass] → Next Increment
                               [Fail] → Iterate (back to IMPLEMENT)
```

**Process Overview**:

- Work in small increments
- Each increment goes through all 4 steps (Plan → Implement → Validate → Quality Check)
- When describing plan, present any unanswered OBSERVES to agent
- IMPORTANT: Make sure you only include parts in the actual increment, for example don't include logging changes when updating a service
- IMPORTANT: If increment includes renaming or moving files, you need to divided into several steps. 1) Update code inside file in original location with original name 2) Move/Rename file in separate step ONLY AFTER developer has approved this explicitly after confirming implementation.
- After quality check passes, update the progress document
- Flag any uncertainties with `OBSERVE` in the progress document
- **After each increment, capture improvement feedback** (see Feedback Flow below)
- The progress document serves as the main tracker for migration status
- The guide includes all the steps necessary to migrate the application
- Agent must describe why quality gate pass or fails.

**Critical**: The Quality Check step acts as a gate - if quality standards are not met, the
increment is iterated until it passes. High quality includes using Spring Boot best practices, simple code, not custom solutions when not ncessary.

---

#### Feedback Flow (After Each Increment)

After completing each increment (after Quality Check passes), the agent captures improvement feedback:

**Step 5: 💡 FEEDBACK CAPTURE** (Reflection Agent Role)

- **Purpose**: Continuously improve the migration process and capture learnings
- **Actions**:
    - Reflect on the increment just completed
    - Identify what could have been better in:
        - **Planning**: Was the plan accurate? Did scope creep occur?
        - **Implementation**: Were there unexpected complexities? Better approaches available?
        - **Guide Quality**: Was the guide clear enough? Missing details?
        - **Requirements**: Could requirements be more specific to avoid ambiguity?
        - **Process**: Could the increment have been structured differently?
    - Document findings in a dedicated section of the progress document
- **Output**: Improvement notes added to progress document
- **Format in Progress Document**:
  ```
  ## Improvement Feedback

  ### Increment [X]: [Name]
  **What Went Well:**
  - [Positive observations]

  **What Could Be Improved:**
  - [Specific improvement points]
  - [Better approaches identified]

  **Recommendations for Template/Guide:**
  - [Suggestions for improving base requirements template]
  - [Suggestions for improving general analysis instructions]
  - [Process improvements]
  ```
- **Note**: This is a learning mechanism - not everything needs improvement, but honest reflection helps future migrations

---

#### Increment Workflow (4-Step Approach)

For EACH increment during migration, follow this workflow:

**Step 1: 📋 PLAN** (Analysis Agent Role)

- **Purpose**: Understand what needs to be done in this increment
- **Actions**:
    - Review the migration guide for the current increment
    - Review the progress document to understand context
    - Identify specific files, classes, and changes needed
    - List concrete tasks to complete this increment
    - Identify potential risks or complexities
    - Define success criteria for this increment
- **Output**: Clear, detailed plan presented to developer
- **Format**:
  ```
  📋 PLAN for [Increment Name]
  
  Scope:
  - [What will be migrated/changed]
  
  Files to modify:
  - [List of files]
  
  Changes required:
  - [Specific changes needed]
  
  Success criteria:
  - [How we know this increment is complete]
  
  Potential risks:
  - [Any concerns or complexities]
  ```
- **Developer Confirmation**: Wait for developer to approve plan before proceeding

**Step 2: 🔨 IMPLEMENT** (Implementation Agent Role)

- **Purpose**: Execute the planned changes
- **Actions**:
    - Follow the plan created in Step 1
    - Make code changes according to migration guide and requirements
    - Ensure changes align with target framework best practices
    - Follow coding conventions from requirements document
    - Keep changes focused on the current increment scope
    - Do not deviate from the plan without noting it
- **Output**: Code changes completed
- **Note**: If unexpected issues arise, document them in the progress document and/or guide, but
  complete the increment as best as possible

**Step 3: ✅ VALIDATE** (Validation Agent Role)

- **Purpose**: Verify the implementation works functionally
- **Actions**:
    - Build the application: `./gradlew build`
    - Check for compilation errors
    - Run tests: `./gradlew test` (if appropriate for this increment)
    - Verify application can start (if appropriate)
    - Check that no functionality was broken
- **Output**: Validation report
- **Format**:
  ```
  ✅ VALIDATION for [Increment Name]
  
  Build: [PASS/FAIL] + details
  Compilation: [PASS/FAIL] + any errors
  Tests: [PASS/FAIL] + results
  Application Start: [PASS/FAIL] + details
  
  Issues found:
  - [List any problems]
  ```
- **Gate**: If validation fails critically (build errors), must fix before proceeding to Quality
  Check

**Step 4: 🔍 QUALITY CHECK** (Quality Agent Role)

- **Purpose**: Critically assess if changes meet quality standards
- **Actions**:
    - **Code Quality Review**:
        - Are target framework best practices followed?
        - Is code clean, readable, and maintainable?
        - Are naming conventions from requirements followed?
        - Are there any code smells or anti-patterns?
        - Is error handling appropriate?
    - **Architecture Review**:
        - Does the implementation align with target framework architecture?
        - Are dependencies properly managed?
        - Is separation of concerns maintained?
        - Are the right framework annotations/patterns used?
    - **Requirements Compliance**:
        - Does it meet the requirements document specifications?
        - Are all items from the plan actually completed?
        - Are there any shortcuts or technical debt introduced?
    - **Completeness Check**:
        - Are there leftover source framework artifacts?
        - Are imports cleaned up?
        - Are deprecated patterns removed?
        - Is documentation updated if needed?
- **Output**: Quality assessment with pass/fail decision
- **Format**:
  ```
  🔍 QUALITY CHECK for [Increment Name]
  
  Code Quality: [PASS/FAIL]
  - [Specific observations]
  
  Architecture: [PASS/FAIL]
  - [Specific observations]
  
  Requirements Compliance: [PASS/FAIL]
  - [Specific observations]
  
  Completeness: [PASS/FAIL]
  - [Specific observations]
  
  Overall Decision: [✅ APPROVED | ⚠️ NEEDS IMPROVEMENT]
  
  If NEEDS IMPROVEMENT:
  Issues to address:
  1. [Specific issue]
  2. [Specific issue]
  
  Recommendations:
  - [How to fix each issue]
  ```
- **Gate Decision**:
    - **✅ APPROVED**: Proceed to update progress document and move to next increment
    - **⚠️ NEEDS IMPROVEMENT**: Return to Step 2 (IMPLEMENT) with specific issues to fix, then
      repeat Steps 3 & 4
- **Developer Involvement**: Present quality check results. Developer can override and proceed, or
  agree to iterate.

---

#### After Quality Check Passes

**Capture Feedback** (REQUIRED):

- Reflect on the increment (see Feedback Flow above)
- Document what went well and what could be improved
- Add findings to "Improvement Feedback" section in progress document
- Include recommendations for template/guide improvements

**Update Progress Document** (REQUIRED):

- Mark the increment as complete in the progress document
- Document what was accomplished
- Note any OBSERVE items if needed
- Update overall progress percentage

**Developer Confirmation**:

- Present summary of what was completed
- Show quality check results
- Ask for confirmation to proceed to next increment

---

#### Complete Increment Example

```
Increment 5: Remove all recompose usage from components

📋 PLAN
Scope: Remove all recompose imports and usage from repository
Files mentioned in guide: UsersList.js, App.js, SessionPoller.js

Repository Scan Results:
- Command: grep -r "from 'recompose'" src/
- Found 20 files total (guide mentioned 3 as examples)
- Complete file list: [all 20 files listed]

Plan: Migrate all 20 files, not just the 3 examples in guide

[Developer approves plan with complete list]

🔨 IMPLEMENT
[Code changes made to all 20 files]

✅ VALIDATE
Build: PASS
Tests: PASS
Application Start: PASS

🔍 QUALITY CHECK
Code Quality: PASS - Target framework patterns used correctly
Architecture: PASS - Follows React 18 hooks patterns
Requirements: PASS - All lifecycle converted to useEffect

Completeness (Repository-Wide Scan): FAIL
- Scanned: grep -r "from 'recompose'" src/
- Results: 2 files still found
- Files mentioned in guide: UsersList.js, App.js, SessionPoller.js (migrated ✓)
- Additional files found in scan: HeaderContainer.js, LoginOptions.js (NOT migrated ✗)
- Verification: 18/20 files migrated, 2 missed

Overall: ⚠️ NEEDS IMPROVEMENT
Issues: HeaderContainer.js and LoginOptions.js still import recompose

[Return to IMPLEMENT to fix missed files]

🔨 IMPLEMENT (Iteration 2)
[Migrate HeaderContainer.js and LoginOptions.js]

✅ VALIDATE
Build: PASS
Tests: PASS

🔍 QUALITY CHECK
Code Quality: PASS
Architecture: PASS
Requirements: PASS

Completeness (Repository-Wide Scan): PASS
- Scanned: grep -r "from 'recompose'" src/
- Results: 0 files found
- All 20 files successfully migrated
- Verification: Complete - no recompose usage remains in repository

Overall: ✅ APPROVED

💡 FEEDBACK CAPTURE
What Went Well:
- Repository-wide scan caught files not mentioned in guide
- Complete migration verified with grep search

What Could Be Improved:
- Initial scan in PLAN phase found all 20 files, but implementation only did 18
- Should have double-checked scan results before moving to quality check

Recommendations for Template/Guide:
- Emphasize that guide provides examples, not complete lists
- Add explicit step in IMPLEMENT to re-verify file list from PLAN
- Quality gate repository scan is critical - caught the gap

[Update progress document with feedback, proceed to Increment 6]
```

---

1. **Execute Prompt 5: Perform Migration Changes (with 4-Step Workflow)**
    - **Needed**: Guide and progress documents.
    - **Input**: `[YOUR-APP]-migration-guide.md` +
      `[YOUR-APP]-migration-progress.md` + repository code. The guide will include more
      complete details than progress document so use it as a main reference.
    - **Process**: For EACH increment, follow the 4-step workflow (Plan → Implement → Validate →
      Quality Check)
    - **Output**:
        - Code changes (after quality check passes)
        - **REQUIRED**: Updated `[YOUR-APP]-migration-progress.md` (after quality check
          passes)
        - Mark `OBSERVE` items in progress document if needed
    - **Iterative**: Yes (small increments, each following 4-step workflow, agent updates progress
      after quality check passes).
    - **Quality Gate**: Quality Check must pass before moving to next increment.
    - **Prompt**: See Prompt 5 in `prompts/[migration-type].prompt.md`.

2. **Handle OBSERVE Items**
    - **Needed**: When `OBSERVE` flagged in progress.
    - **Input**: Progress document.
    - **Output**: Pause iteration, inform developer of needed input.
    - **Iterative**: As needed.

### Phase 3: Completion

After all migration steps are completed, the agent performs final verification and confirms
completion with the developer.

1. **Check Progress Document**
    - **Needed**: All migration steps from guide appear complete (including cleanup steps).
    - **Input**: Progress document review.
    - **Output**: List of any remaining incomplete steps or OBSERVE items.
    - **Action**: If incomplete steps remain, continue with Phase 2. If all complete, proceed to
      step 2.

2. **Resolve Remaining OBSERVE Items**
    - **Needed**: Any unresolved `OBSERVE` flags from implementation or cleanup.
    - **Input**: Developer input for design decisions.
    - **Output**: All `OBSERVE` items addressed.
    - **Iterative**: As needed.
    - **Action**: If OBSERVE items remain, pause and ask developer for input. Do not proceed until
      resolved.

3. **Final Verification**
    - **Needed**: All cleanup complete, no OBSERVE items remain.
    - **Verification Steps**:
        1. Build application: `./gradlew build` (must succeed)
        2. Run tests: `./gradlew test` (all tests must pass)
        3. Start application: Verify application starts successfully
    - **Output**: Confirmation that application builds, tests pass, and starts.
    - **Action**: If any verification fails, fix issues before proceeding.

4. **Developer Satisfaction Check**
    - **Always Required**: Never skip this step.
    - **Ask Developer**:
      ```
      ✅ Migration Process Summary:

      Completed:
      - [List all major migration areas completed]
      - All source framework dependencies removed
      - Cleanup and optimization complete
      - Application builds successfully
      - All tests pass
      - Application starts successfully

      Remaining (if any):
      - [List any known issues or TODOs]

      Are you satisfied with the migration? Is there anything else you'd like me to address?
      ```
    - **Wait for Developer Response**.
    - **If Satisfied**: Proceed to step 5.
    - **If Not Satisfied**: Ask what needs to be addressed and continue working on those areas.

5. **Summarize Improvement Feedback and Update Templates**
    - **Needed**: Developer has approved migration completion.
    - **Actions**:
        - Review all improvement feedback captured in progress document
        - Summarize key learnings and recommendations
        - Present consolidated feedback to developer
    - **Ask Developer**:
      ```
      📊 Migration Learnings Summary:

      Throughout this migration, I captured improvement feedback after each increment.
      Here's a summary of the key learnings:

      **Process Improvements:**
      - [Summarize process-related improvements]

      **Guide/Requirements Improvements:**
      - [Summarize recommendations for base requirements template]
      - [Summarize recommendations for analysis instructions template]

      **Implementation Insights:**
      - [Summarize technical learnings and better approaches identified]

      Would you like me to update the templates with these improvements?
      - Base Requirements Template (templates/[migration-type]-requirements-template.md)
      - Analysis Instructions Template (templates/[source-framework]-analysis-template.md)
      
      This will help future migrations benefit from these learnings.
      ```
    - **Wait for Developer Response**.
    - **If Yes**: Update the relevant templates with improvements and mark migration as complete.
    - **If No**: Mark migration as complete without template updates.

### Developer Guidance

- Clearly state required inputs.
- Summarize progress after iterations.
- Use `OBSERVE` for human decisions.

**When Issues Arise:**
If the developer encounters problems or needs to update the migration approach, they can use the
`/update-migration` tool. This tool helps quickly update the guide and progress document to reflect
new understanding or changed requirements. The agent should then continue migration using the
updated guide.

---

## 💬 Conversation Section

Copilot should always verify if the developer is satisfied with the output of each step before
proceeding to the next step. This ensures quality control and allows for adjustments or
clarifications.

---

## 👨‍💻 Agent Responsibilities

- Autonomous execution of analysis, changes, documentation.
- **Update progress document after EVERY increment** - this is mandatory, not optional.
- Incremental, non-breaking progress.
- Adhere to requirements, minimize debt, suggest improvements.
- Clear communication and updates.
- Mark uncertainties with `OBSERVE` in the progress document.

---

## ✨ Success Criteria

- App functional on target framework.
- Modern standards, minimal debt.
- All `OBSERVE` resolved, documents complete.

---

## 📝 Notes

- Replace `[YOUR-APP]` with app name (e.g., `hjmtj-core`).
- Use this guide as primary context.
- Mark uncertainties with `OBSERVE`.

---

## 📊 Progress

Track the completion of each step in the migration process. Mark with [x] when completed or if
already completed but not marked.

### Phase 1: Initialization

- [x] 
    0. Determine Migration Type
- [x] 
    1. Analyze Test Coverage and Identify Gaps (Optional but Highly Recommended)
- [x] 
    2. Request Initial Requirements
- [x] 
    3. Request Inspiration Document (Optional)
- [x] 
    4. Execute Prompt 1.2: Enhance Requirements Document
- [ ] 
    5. Execute Prompt 2: Generate General Analysis Instructions (+ Follow-up enhancements)
- [ ] 
    6. Execute Prompt 3: Generate Application-Specific Guide
- [ ] 
    7. Execute Prompt 3.5: Verify Guide Quality and Completeness (Quality Gate)
- [ ] 
    8. Execute Prompt 4: Generate Progress Document

### Phase 2: Implementation

- [ ] 
    1. Execute Prompt 5: Perform Migration Changes (iterative, with build verification after each
       increment)
- [ ] 
    2. Handle OBSERVE Items (as needed)

### Phase 3: Completion

- [ ] 
    1. Check Progress Document
- [ ] 
    2. Resolve Remaining OBSERVE Items
- [ ] 
    3. Final Verification (Build, Test, Start Application)
- [ ] 
    4. Developer Satisfaction Check (ALWAYS REQUIRED)
- [ ] 
    5. Summarize Improvement Feedback and Update Templates (if developer approves)
