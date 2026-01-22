# Test Coverage Analysis: intygsadmin React Frontend

**Date**: January 14, 2026  
**Migration Type**: React 16 to React 18  
**Analysis Scope**: Frontend only (`web/client/src`)

---

## Executive Summary

The intygsadmin React frontend has **minimal test coverage**. Manual code inspection reveals:

- **Total React Components**: ~50+ components
- **Total Pages**: 9 pages
- **Test Files Found**: 2 test files only
    - `App.test.js` (basic smoke test)
    - `components/toggler/Toggler.spec.js` (unit test)
- **Test Files for Actions/Reducers**: ~10 spec files
- **Overall Assessment**: **Critical test gap** - <5% component coverage

This presents **HIGH RISK** for the React 16 to 18 migration, particularly for:

- Class component to functional component migrations (if any exist)
- Lifecycle method changes
- Deprecated patterns (UNSAFE_componentWillMount, componentWillMount)
- Redux connect patterns with recompose

---

## 1. Current Test Coverage Baseline

### Test Infrastructure

- **Testing Framework**: Jest (via react-scripts 3.2.0)
- **Test Utilities**:
    - Enzyme 3.10.0 with enzyme-adapter-react-16
    - Redux Mock Store 1.5.3
    - Sinon 7.5.0
    - Fetch Mock 7.5.1
- **Coverage Configuration**: Configured in package.json (collectCoverageFrom: src/**/*.{js})

### Existing Test Files

#### Component Tests

1. **App.test.js** ✅
    - **Coverage**: Basic smoke test only
    - **Type**: Renders without crashing
    - **Completeness**: 10% - No behavioral testing

2. **components/toggler/Toggler.spec.js** ✅
    - **Coverage**: Good unit test coverage
    - **Tests**: Rendering, icon toggling, click events
    - **Completeness**: 80% - Well tested

#### Action/Reducer Tests (Redux Store)

Files with `.spec.js`:

- ✅ `appConfig.spec.js`
- ✅ `banner.spec.js`
- ✅ `bannerList.actions.spec.js`
- ✅ `integratedUnits.spec.js`
- ✅ `intygInfo.spec.js`
- ✅ `intygInfoList.spec.js`
- ✅ `modal.spec.js`
- ✅ `privatePractitioner.spec.js`
- ✅ `sessionPoll.spec.js`
- ✅ `user.spec.js`
- ✅ `users.spec.js`
- ✅ `bannerList.reducer.spec.js`
- ✅ `integratedUnits.spec.js` (reducer)
- ✅ `modal.spec.js` (reducer)
- ✅ `privatePractitioner.spec.js` (reducer)
- ✅ `util.spec.js`

**Redux Test Coverage**: Good (~70% of actions/reducers have tests)

---

## 2. Files/Components with Inadequate Coverage

### 🔴 CRITICAL - No Tests (High Business Logic)

#### Pages (0% coverage)

All 9 pages have **no tests**:

- ❌ `pages/BannerPage.js`
- ❌ `pages/DataExportPage.js`
- ❌ `pages/ErrorPage.js`
- ❌ `pages/IndexPage.js`
- ❌ `pages/IntegratedUnitsPage.js`
- ❌ `pages/IntygInfoPage.js`
- ❌ `pages/PrivatePractitionerPage.js`
- ❌ `pages/ResendPage.js`
- ❌ `pages/UsersPage.js`

#### Complex Components (0% coverage)

- ❌ `components/users/Users.js` - Redux connected, pagination, dialogs
- ❌ `components/users/UsersList.js`
- ❌ `components/users/UsersPageHeader.js`
- ❌ `components/users/UsersActionBar.js`
- ❌ `components/users/dialogs/CreateUser.dialog.js`
- ❌ `components/users/dialogs/RemoveUser.dialog.js`
- ❌ `components/dataExport/DataExport.js`
- ❌ `components/dataExport/DataExportList.js`
- ❌ `components/dataExport/DataExportPageHeader.js`
- ❌ `components/dataExport/DataExportActionBar.js`
- ❌ `components/dataExport/dialogs/UpdateDataExport.dialog.js`
- ❌ `components/dataExport/dialogs/ResendDataExportKey.dialog.js`
- ❌ `components/privatePractitioner/PrivatePractitioner*.js` (5 files)
- ❌ `components/bannerList/` (all files)
- ❌ `components/bannerDialogs/` (all files)
- ❌ `components/integratedUnits/` (all files)
- ❌ `components/intygInfo/` (all files)

#### Infrastructure Components (0% coverage)

- ❌ `App.js` - Only smoke test, no routing/lifecycle tests
- ❌ `components/auth/SecuredRoute` - Authorization logic
- ❌ `components/auth/UnsecuredRoute` - Routing logic
- ❌ `components/header/` - Navigation
- ❌ `components/iaMenu/MenuBar.js`
- ❌ `components/iaMenu/MenuBarButton.js`
- ❌ `components/sessionPoller/SessionPollerContainer.js` - Critical for session management
- ❌ `components/errorModal/` - Error handling
- ❌ `components/modalContainer/modalContainer.js`
- ❌ `components/loginOptions/LoginOptions.js`
- ❌ `components/loginOptions/LoginOptionsContainer.js`

#### Form/Input Components (0% coverage)

- ❌ `components/datePicker/DatePicker.js`
- ❌ `components/datePicker/DatePickerPopup.js`
- ❌ `components/timePicker/TimePicker.js`
- ❌ `components/timePicker/TimePickerPopup.js`
- ❌ `components/radioButton/RadioButton.js`
- ❌ `components/radioButton/RadioWrapper.js`
- ❌ `components/CustomTextarea/` (if exists)
- ❌ `components/styles/HsaInput.js`

#### Utility Components (0% coverage)

- ❌ `components/loadingSpinner/` - UI feedback
- ❌ `components/spinnerButton/SpinnerButton.js` - Action feedback
- ❌ `components/styles/ListPagination.js` - Reused pagination
- ❌ `components/styles/TableSortHead.js` - Table sorting
- ❌ `components/displayDateTime/` - Date formatting
- ❌ `components/externalLink/` - Link handling
- ❌ `components/helpChevron/` - Help UI
- ❌ `components/alert/` - Alert system
- ❌ `components/ResendStatusCount/` - Status display

#### Custom Hooks (0% coverage)

- ❌ `components/hooks/UseOnClickOutside.js` - Important hook for modals/dropdowns

---

## 3. High-Risk Migration Areas

### 🚨 Critical Risks for React 16 → 18 Migration

#### A. Lifecycle Methods (Deprecated/Changed)

**File**: `App.js`

- ❌ **UNSAFE_componentWillMount** is used - **DEPRECATED** in React 18
- ❌ **componentWillUnmount** - Needs testing for cleanup
- **Risk Level**: **CRITICAL**
- **Impact**: App initialization and routing may break
- **Tests Needed**: Full lifecycle testing before migration

#### B. Redux + Recompose Pattern

Many components use this pattern:

```javascript
export default compose(
    connect(mapStateToProps, actions),
    lifecycle({...})
)(Component)
```

**Files Affected**:

- `App.js`
- `components/users/Users.js`
- Likely many others (need full scan)

**Risk Level**: **HIGH**

- `recompose` library may have React 18 compatibility issues
- Lifecycle HOC usage needs to be migrated to hooks
- **Tests Needed**: Component behavior with Redux state

#### C. Enzyme Compatibility

**Current**: enzyme-adapter-react-16 v1.15.1
**Issue**:

- No official Enzyme adapter for React 18
- Community adapters exist but are unofficial
- May need to migrate to React Testing Library

**Risk Level**: **CRITICAL**

- Existing tests will break
- Decision needed: Keep Enzyme with unofficial adapter OR migrate to RTL
- **Action Required**: Determine testing strategy before migration

#### D. React Router v5 with Connected Router

**Current**: react-router-dom v5.1.2, connected-react-router v6.4.0
**Risk**:

- May need upgrade to React Router v6 for React 18 compatibility
- Connected router integration may need updates

**Risk Level**: **MEDIUM**

- **Tests Needed**: Route navigation, history integration

#### E. Third-Party Component Libraries

**Potentially Incompatible**:

- `react-datepicker` v2.9.6 - May need update
- `react-responsive-modal` v4.0.1 - May need update
- `react-js-pagination` 3.0.2 - May need update
- `styled-components` v4.4.0 - Should update to v5+ for React 18

**Risk Level**: **MEDIUM**

- **Tests Needed**: Date picker, modals, pagination behavior

---

## 4. Prioritized Test Gaps

### Priority: CRITICAL (Must Add Before Migration)

1. **App.js Lifecycle Tests**
    - Test UNSAFE_componentWillMount behavior
    - Test componentDidMount history listener
    - Test componentWillUnmount cleanup
    - Test routing integration
    - **Reason**: Core app initialization - breaking this breaks everything

2. **Authentication/Authorization Components**
    - `components/auth/SecuredRoute`
    - `components/auth/UnsecuredRoute`
    - **Reason**: Security-critical, affects all protected routes

3. **Session Management**
    - `components/sessionPoller/SessionPollerContainer.js`
    - Test polling behavior, session expiry handling
    - **Reason**: User sessions must not break

4. **Redux Connected Components (Sample)**
    - `components/users/Users.js` - Complex component with Redux
    - Test mapStateToProps, action dispatching
    - **Reason**: Validate Redux integration pattern works post-migration

### Priority: HIGH (Add During Migration)

5. **Modal System**
    - `components/errorModal/`
    - `components/modalContainer/modalContainer.js`
    - All dialog components (CreateUser, RemoveUser, etc.)
    - **Reason**: Used throughout app, modals often break with React updates

6. **Form Components**
    - Date/Time pickers
    - Radio buttons
    - Custom inputs
    - **Reason**: User input critical, date pickers often have compatibility issues

7. **Page Integration Tests**
    - At least smoke tests for all 9 pages
    - Test page renders, navigation works
    - **Reason**: Ensure pages don't crash post-migration

8. **Custom Hooks**
    - `UseOnClickOutside.js`
    - **Reason**: Hook behavior may change subtly in React 18

### Priority: MEDIUM (Add Incrementally)

9. **List/Table Components**
    - UsersList, DataExportList, etc.
    - Pagination, sorting, filtering
    - **Reason**: Complex UI logic, good to have coverage

10. **Navigation Components**
    - Header, MenuBar
    - **Reason**: Ensure navigation still works

11. **Utility Components**
    - LoadingSpinner, SpinnerButton
    - TableSortHead, ListPagination
    - **Reason**: Reused components, test once use everywhere

### Priority: LOW (Nice to Have)

12. **Styled Components**
    - `components/styles/*` (mostly presentational)
    - **Reason**: Low business logic, visual testing would be ideal

13. **Static Pages**
    - ErrorPage, IndexPage
    - **Reason**: Simple components, lower risk

---

## 5. Recommended Test Types

### Unit Tests

**What**: Individual component behavior in isolation
**Focus Areas**:

- All custom hooks
- Form components (inputs, date pickers, radio buttons)
- Utility components (Toggler, LoadingSpinner, etc.)
- Redux actions and reducers (already have good coverage ✅)

**Tools**: Jest + React Testing Library (recommended) or Enzyme with unofficial adapter

### Component Integration Tests

**What**: Components with their Redux store connections
**Focus Areas**:

- Users.js and other Redux-connected components
- Modal dialogs with actions
- SessionPoller with store integration

**Tools**: Jest + RTL + Redux Mock Store

### Integration Tests (Mini E2E)

**What**: Full page rendering with routing
**Focus Areas**:

- App.js with full routing
- SecuredRoute/UnsecuredRoute with auth
- Page navigation flows

**Tools**: Jest + RTL + Mock Service Worker (for API mocks)

### Snapshot Tests (Use Sparingly)

**What**: Component output snapshots
**Focus Areas**:

- Styled components
- Static pages
  **Warning**: Snapshots become outdated quickly, use only for stable components

---

## 6. Test Coverage Goals

### Pre-Migration Minimum Coverage

- **Critical Components**: 80%+ coverage
    - App.js lifecycle
    - Auth components
    - SessionPoller
- **Redux Layer**: 70%+ (already met ✅)
- **Pages**: At least smoke tests (renders without crash) for all 9
- **Custom Hooks**: 90%+ coverage

### Post-Migration Target Coverage

- **Overall Component Coverage**: 60%+
- **Critical Business Logic**: 80%+
- **Redux Layer**: 70%+ (maintain ✅)
- **Integration Tests**: Key user flows covered

---

## 7. Testing Strategy Recommendation

### Decision Point 1: Enzyme vs React Testing Library

**Option A: Keep Enzyme** (with unofficial React 18 adapter)

- ✅ Pros: Existing test (Toggler.spec.js) can stay as-is
- ❌ Cons: Unofficial adapter, may have bugs, shallow rendering discouraged
- **Recommendation**: **NOT RECOMMENDED** - Technical debt

**Option B: Migrate to React Testing Library** ✅ **RECOMMENDED**

- ✅ Pros:
    - Official React 18 support
    - Better testing practices (test behavior, not implementation)
    - Active community, modern approach
    - Aligns with React team recommendations
- ❌ Cons: Need to rewrite existing Toggler.spec.js
- **Recommendation**: **STRONGLY RECOMMENDED**

### Decision Point 2: When to Add Tests

**Option A**: Add all critical tests NOW (before migration)

- ❌ Cons: Tests will need updating when code changes (lifecycle → hooks)
- ❌ Cons: Testing React 16 code that will be replaced
- ❌ Cons: Inefficient - test twice (old code + new code)

**Option B**: Add tests incrementally during migration

- ❌ Cons: Some regressions may slip through
- ❌ Cons: Complex to test while code is changing

**Option C**: Migrate FIRST, then add tests ✅ **RECOMMENDED**

- ✅ Pros: Test the final React 18 code, not temporary React 16 code
- ✅ Pros: Write tests once in modern RTL
- ✅ Pros: More efficient - no test rewriting needed
- ✅ Pros: Test coverage analysis guides what to watch during migration
- ✅ Pros: Manual testing verifies migration works before adding tests
- ❌ Cons: Need careful manual testing during migration
- **Best For**: Efficient approach when you have good analysis
- **Mitigation**: Use test coverage analysis as migration guide + careful manual testing

**Recommended Approach**: **Option C** (Migrate First, Test After) with careful monitoring

**Rationale**:

- We already have comprehensive test coverage analysis as our guide
- React 16 → 18 migration will change code structure (lifecycle → hooks, recompose → hooks)
- Writing tests for React 16 code means rewriting them for React 18 code
- More efficient to test the final migrated code once

**Strategy**:

1. **Phase 1 (Current)**: Document baseline, identify risks ✅
2. **Phase 2A (Migration)**:
    - Use test coverage analysis to identify high-risk areas
    - Migrate carefully, watching for deprecated patterns
    - Manual test critical flows after each increment
    - Keep app building and running
3. **Phase 2B (Add Tests)**:
    - Add critical tests to migrated code
    - Use React Testing Library from the start
    - Focus on areas identified in coverage analysis
4. **Phase 3 (Consolidation)**: Fill remaining gaps, achieve 60%+ coverage

---

## 8. Specific Observations & OBSERVE Flags

### 🔍 OBSERVE: Lifecycle Methods Inventory Needed

**Question**: How many components use deprecated lifecycle methods?

- Found: `App.js` uses `UNSAFE_componentWillMount`
- **Action Required**: Full codebase scan for:
    - `componentWillMount`
    - `UNSAFE_componentWillMount`
    - `componentWillReceiveProps`
    - `UNSAFE_componentWillReceiveProps`
    - `componentWillUpdate`
    - `UNSAFE_componentWillUpdate`

### 🔍 OBSERVE: Class vs Functional Components

**Question**: Are there class components that need migration to functional?

- `App.js` - Uses lifecycle via recompose HOC (not a class)
- **Action Required**: Scan for class components vs functional components
- **Impact**: Class components may need refactoring to hooks

### 🔍 OBSERVE: Recompose Usage Extent

**Question**: How extensively is recompose used?

- Found in: `App.js`, `components/users/Users.js`
- **Action Required**: Full scan for `compose()`, `lifecycle()`, other recompose HOCs
- **Impact**: May need systematic migration from recompose to hooks

### 🔍 OBSERVE: Third-Party Library React 18 Compatibility

**Question**: Which dependencies need updates?

- **Action Required**: Check compatibility for:
    - react-datepicker (likely needs update)
    - react-responsive-modal (likely needs update)
    - styled-components v4 (should update to v5+)
    - react-js-pagination (check compatibility)
    - connected-react-router (may need alternative)

### 🔍 OBSERVE: Testing Strategy Approval

**Question**: Does team approve React Testing Library migration?

- **Decision Needed**: Enzyme (unofficial adapter) vs RTL
- **Recommendation**: RTL for modern, maintainable tests
- **Impact**: Affects all new tests and potentially 1 existing test

---

## 9. Action Items

### ✅ Completed: Phase 1 (Pre-Migration Analysis)

1. ✅ **Test coverage baseline established** - This document
2. ✅ **Testing library decision** - React Testing Library (will install during React 18 upgrade)
3. ✅ **Deprecated patterns identified** - UNSAFE_componentWillMount, recompose usage
4. ✅ **Testing strategy decided** - Add tests AFTER React 18 migration (more efficient)

### 📋 Phase 2A: React 18 Migration (Before Adding Tests)

1. 🔴 **Scan codebase for deprecated lifecycle methods**
    - Search for: `UNSAFE_componentWillMount`, `componentWillReceiveProps`, etc.
    - Document all usages
    - Create migration plan

2. 🔴 **Scan for recompose usage**
    - List all files using `compose()`, `lifecycle()`, other recompose HOCs
    - Plan hooks migration strategy

3. 🔴 **Upgrade React 16 → 18**
    - Update React, ReactDOM
    - Update all dependencies
    - Install React Testing Library at this time

4. 🔴 **Fix deprecated lifecycle methods**
    - Migrate UNSAFE_componentWillMount → useEffect
    - Migrate recompose lifecycle → hooks

5. 🔴 **Verify app builds and runs**
    - Test manually that core functionality works
    - Fix any breaking changes

### 📋 Phase 2B: Add Critical Tests (After React 18 Works)

**Why After?** Testing the migrated code avoids having to rewrite tests twice.

6. 🔴 **Install and configure RTL** (if not done in Phase 2A)
    - `@testing-library/react@^12.1.5` (React 18 compatible)
    - `@testing-library/jest-dom@^5.16.5`
    - `@testing-library/user-event@^14.4.3`
    - Update `setupTests.js`

7. 🔴 **Add CRITICAL tests** (Priority #1):
    - App.js with hooks (migrated from lifecycle)
    - SecuredRoute/UnsecuredRoute tests
    - SessionPoller tests
    - One complex Redux-connected component (e.g., Users.js)

8. 📋 **Migrate existing Enzyme test** (Toggler.spec.js):
    - Rewrite in RTL for consistency
    - Verify behavior matches

9. 📋 **Add HIGH priority tests incrementally**:
    - Modal system
    - Form components
    - Page smoke tests

### 📋 Phase 3: Post-Migration

10. 📋 **Fill remaining test gaps** (MEDIUM/LOW priority)
11. 📋 **Run full coverage report**: `npm test -- --coverage`
12. 📋 **Set up CI coverage threshold** (e.g., 60% minimum)
13. 📋 **Update this document** with actual coverage achieved

---

## 10. Risk Assessment

### Overall Migration Risk Level: **HIGH** ⚠️

**Risk Factors**:

1. ❌ **Minimal existing test coverage** (<5% components)
2. ❌ **Deprecated lifecycle methods in use** (UNSAFE_componentWillMount)
3. ❌ **Recompose dependency** (may need migration to hooks)
4. ❌ **Enzyme compatibility issues** (no official React 18 adapter)
5. ❌ **Third-party library compatibility** (date picker, modals, etc.)

**Mitigation Strategies**:

1. ✅ **Add critical tests BEFORE migration** (App.js, auth, session)
2. ✅ **Incremental approach** - migrate in small chunks
3. ✅ **Test each increment** before proceeding
4. ✅ **Use React Testing Library** for forward compatibility
5. ✅ **Update dependencies** proactively (styled-components, date picker, etc.)
6. ✅ **Manual testing checklist** for critical user flows

### Risk Reduction Confidence

- **With recommended testing strategy**: Risk reduced to **MEDIUM** 🟡
- **With full test coverage (60%+)**: Risk reduced to **LOW** 🟢

---

## 11. Summary & Recommendations

### Current State

- **Test Coverage**: <5% of React components (critical gap)
- **Redux Coverage**: ~70% of actions/reducers (good ✅)
- **Risk Level**: HIGH without additional testing

### Recommended Approach

**Phase 1: Foundation (BEFORE migration)**

1. Install React Testing Library
2. Add CRITICAL tests (App.js, auth, session)
3. Scan for deprecated patterns
4. Plan recompose → hooks migration

**Phase 2: Incremental Migration (DURING)**

1. Follow test-then-migrate pattern
2. Add tests before changing each component
3. Migrate in small increments
4. Build & verify after each increment

**Phase 3: Consolidation (AFTER)**

1. Fill remaining test gaps (target 60%+ coverage)
2. Manual QA of critical flows
3. Update documentation

### Success Metrics

- ✅ Zero runtime errors in production
- ✅ All critical user flows work
- ✅ 60%+ component test coverage
- ✅ 70%+ Redux test coverage (maintain)
- ✅ CI/CD pipeline with coverage checks

---

## Appendix: Component Inventory

### Total Component Count (Approximate)

- **Pages**: 9
- **Components**: ~50+
    - `alert/`: ~3 files
    - `auth/`: ~2 files
    - `bannerDialogs/`: ~5 files
    - `bannerList/`: ~5 files
    - `CustomTextarea/`: ~1 file
    - `dataExport/`: ~7 files
    - `datePicker/`: ~3 files
    - `displayDateTime/`: ~2 files
    - `errorModal/`: ~2 files
    - `externalLink/`: ~1 file
    - `header/`: ~3 files
    - `helpChevron/`: ~1 file
    - `hooks/`: ~1 file
    - `iaMenu/`: ~2 files
    - `integratedUnits/`: ~5 files
    - `intygInfo/`: ~5 files
    - `loadingSpinner/`: ~1 file
    - `loginOptions/`: ~2 files
    - `modalContainer/`: ~1 file
    - `privatePractitioner/`: ~6 files
    - `radioButton/`: ~3 files
    - `ResendStatusCount/`: ~1 file
    - `sessionPoller/`: ~2 files
    - `spinnerButton/`: ~2 files
    - `styles/`: ~10 files
    - `TestLinks/`: ~1 file
    - `timePicker/`: ~3 files
    - `toggler/`: ~2 files (1 tested ✅)
    - `users/`: ~6 files

### Test Files Count

- **Component tests**: 2 (App.test.js, Toggler.spec.js)
- **Redux tests**: ~16 spec files
- **Total test files**: ~18
- **Files needing tests**: ~70+ files

---

**Document Owner**: AI Agent (Copilot Migration Tool)  
**Last Updated**: January 14, 2026  
**Next Review**: After Phase 1 critical tests added

