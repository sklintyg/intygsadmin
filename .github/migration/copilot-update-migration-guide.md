# 🔄 Update Migration Tool Guide

This guide is for the AI agent to help developers quickly update the migration guide and progress
document when issues, concerns, or new requirements arise during any code migration.

---

## 🎯 Purpose

When developers encounter problems during migration, they need to update the migration guide and
progress document to reflect new understanding, changed approaches, or additional requirements. This
tool streamlines that process for any type of migration (backend, frontend, or cross-platform).

---

## 📋 Workflow

This tool follows an iterative workflow to update the migration guide and optionally migrate the
updated area.

### Step 1: Gather Context

Ask the developer for the following information:

**Required Information:**

1. **Focus Area**: What part of the migration needs updating? 
   - Backend examples: "REST controllers", "JPA repositories", "logging configuration", "transaction management"
   - Frontend examples: "component architecture", "state management", "routing", "API integration"
2. **Problem/Concern**: What issue was encountered? What needs to change?
3. **Proposed Solution** (optional): Does the developer have a preferred approach?

**Format:**

```
🤖 Agent: Please provide the following:
- Focus Area: [What needs updating?]
- Problem/Concern: [What issue did you encounter?]
- Proposed Solution: [Optional - your preferred approach]
```

Wait for developer response before proceeding.

---

### Step 2: Read Current State

Read the following documents to understand current state:

1. **Migration Guide**: `.github/migration/[app]-migration-guide.md`
2. **Progress Document**: `.github/migration/[app]-migration-progress.md`
3. **Requirements**: `.github/migration/instructions/[app]-requirements.md`
4. **Test Coverage** (if exists): `.github/migration/[app]-test-coverage-analysis.md`

Find the relevant sections related to the focus area.

---

### Step 3: Analyze and Propose Updates

Based on the focus area and problem:

1. **Identify affected sections** in the migration guide
2. **Review current approach** vs what's needed
3. **Propose specific updates** to the guide
4. **Identify progress document impacts**

**Present to developer:**

```
🤖 Agent Analysis:

Current Approach in Guide:
[Summarize current approach in the affected section]

Proposed Updates:
1. [Specific change to guide section X]
2. [Specific change to guide section Y]
3. [Update to progress document if needed]

Rationale:
[Why these changes address the problem]

Do you approve these updates?
```

Wait for developer confirmation.

---

### Step 4: Update Documents

After developer approval:

1. **Update Migration Guide**
    - Modify the relevant sections with new approach
    - Add clarifications or additional steps
    - Ensure consistency with requirements document
    - Mark updated sections with date if significant change

2. **Update Progress Document**
    - Add `OBSERVE` note if developer review needed
    - Update status of affected steps
    - Add notes about the change for context

3. **Verify Consistency**
    - Ensure guide and progress are aligned
    - Check that updates don't conflict with requirements
    - Verify related sections are still consistent

---

### Step 5: Summarize Changes

Provide a clear summary:

```
✅ Updates Complete:

Migration Guide Changes:
- [Section X]: [Summary of change]
- [Section Y]: [Summary of change]

Progress Document Changes:
- [Summary of progress updates]
```

---

### Step 6: Migration Decision

After updating the guide and progress document, ask the developer:

```
🤖 Agent: Now that the migration guide has been updated for [focus area], would you like to:

A) Migrate this area now using the updated approach
B) Leave it for later and continue with other parts

If you choose A, I will proceed with migrating [focus area] using the updated guide in small iterations.
If you choose B, I will mark this in the progress document and you can return to it later.

What would you prefer?
```

**If Developer Chooses A (Migrate Now):**

1. Switch to using the main migration workflow (Prompt 5)
2. Work iteratively on the updated focus area:
    - Break the focus area into small increments
    - Implement one increment at a time
    - Build and test after each increment
    - Update progress document after each increment
    - Ask developer for confirmation before next increment
3. Mark area as complete in progress document when done

**If Developer Chooses B (Leave for Later):**

1. Add note in progress document: "Guide updated for [focus area], awaiting implementation"
2. Mark with `OBSERVE` or appropriate status
3. Confirm with developer that update is complete
4. End update-migration session

---

## 🔄 Iterative Migration After Update

If developer chooses to migrate the updated area immediately, follow this iterative approach:

### Iteration Loop:

1. **Plan Next Increment**
   ```
   🤖 Agent Plan for Iteration [N]:
   - What will be changed: [specific changes]
   - Files affected: [list files]
   - Expected outcome: [what should work after this]
   
   Ready to proceed with this increment?
   ```
   Wait for developer approval.

2. **Implement Increment**
    - Make the code changes
    - Follow the updated guide approach
    - Keep changes small and focused

3. **Verify Build**
    - Run build command
    - Report any build errors
    - If build fails, fix errors before proceeding

4. **Update Progress**
    - Mark increment as complete
    - Update progress document
    - Note any OBSERVE items

5. **Confirm and Continue**
   ```
   ✅ Iteration [N] Complete:
   - Changes made: [summary]
   - Build status: [success/failure]
   - Files modified: [list]
   
   Ready for next iteration, or is this area complete?
   ```
   Wait for developer decision.

6. **Repeat or Conclude**
    - If more work needed: Go to step 1 for next iteration
    - If area complete: Mark as done and end

---

## 🔑 Key Principles

### Focus on Clarity

- Updates should make the guide clearer and more actionable
- Add specific examples if helpful
- Remove ambiguity

### Maintain Consistency

- Ensure updates align with requirements document
- Keep guide and progress in sync
- Don't contradict other sections

### Be Specific

- Provide concrete steps, not vague guidance
- Include code patterns or configuration examples
- Reference specific target framework features and best practices

### Minimal Documentation

- Update only what's needed
- Don't create new documentation files
- Focus on making existing guide better

---

## 📝 Common Update Scenarios

### Scenario 1: Approach Isn't Working

**Backend Example**: "REST controller migration approach from guide doesn't work with our custom error handling"

**Actions**:
1. Review REST controller section in guide
2. Understand custom error handling requirements
3. Update guide with corrected approach (e.g., use `@RestControllerAdvice` for Spring Boot)
4. Update progress to mark affected steps for re-work

**Frontend Example**: "Component lifecycle migration approach doesn't handle our complex state updates"

**Actions**:
1. Review component migration section in guide
2. Understand complex state update patterns
3. Update guide with corrected approach (e.g., use `useEffect` with proper dependencies)
4. Update progress to mark affected components for re-work

### Scenario 2: Missing Steps

**Backend Example**: "Guide doesn't mention how to migrate our custom interceptors"

**Actions**:
1. Add new section or subsection to guide
2. Provide detailed migration steps for interceptors
3. Reference framework alternatives (e.g., Spring AOP or built-in filters)
4. Add new steps to progress document

**Frontend Example**: "Guide doesn't mention how to migrate our custom hooks"

**Actions**:
1. Add new section or subsection to guide
2. Provide detailed migration steps for custom hooks
3. Reference React 18 patterns and best practices
4. Add new steps to progress document

### Scenario 3: Requirements Changed

**Backend Example**: "We decided to use Flyway instead of Liquibase"

**Actions**:
1. Update requirements document first (if not already done)
2. Update migration guide database migration section
3. Update progress to reflect new approach
4. Mark any completed Liquibase work for revision

**Frontend Example**: "We decided to use React Query instead of custom data fetching"

**Actions**:
1. Update requirements document first (if not already done)
2. Update migration guide data fetching section
3. Update progress to reflect new approach
4. Mark any completed custom data fetching work for revision

### Scenario 4: Better Approach Discovered

**Backend Example**: "Found Spring Boot Actuator can replace our custom health checks"

**Actions**:
1. Update guide to recommend Actuator instead of custom implementation
2. Add simplification notes
3. Update progress to remove custom health check tasks

**Frontend Example**: "Found target framework's built-in lazy loading can replace our custom solution"

**Actions**:
1. Update guide to recommend built-in feature instead of custom implementation
2. Add simplification notes
3. Update progress to remove custom lazy loading tasks

---

## 🚫 What NOT to Do

- ❌ Don't create new documentation files
- ❌ Don't update requirements without developer explicit approval
- ❌ Don't make assumptions about business logic
- ❌ Don't remove sections without understanding impact
- ❌ Don't update code directly (this tool only updates guide/progress)

---

## 💡 Tips for Effective Updates

1. **Be Surgical**: Update only the affected sections
2. **Add Context**: Explain why the change was needed
3. **Provide Examples**: Include code snippets or configuration examples
4. **Cross-Reference**: Link related sections if helpful
5. **Validate**: Ensure updates don't break other parts of the guide

---

## 🔄 Iteration

If developer requests further refinement:

- Read their feedback
- Adjust the updates
- Present revised changes
- Wait for approval
- Apply updates

---

## ✅ Success Criteria

Update is successful when:

- Migration guide reflects correct approach for focus area
- Progress document is synchronized with guide updates
- Developer confirms updates resolve their concern
- No inconsistencies introduced
- Guide remains clear and actionable

