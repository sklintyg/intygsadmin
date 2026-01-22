# Full Modern React Migration - Complete Scope

**Date:** January 14, 2026  
**Applies to:** intygsadmin-migration-guide.md  
**Decision:** FULL MODERN MIGRATION APPROVED ✅

---

## Complete Migration Scope

Based on comprehensive repository scanning, here are ALL patterns that need migration:

### 1. Redux Patterns (30+ files)

- **Pattern:** `connect()` HOC → `useDispatch()`/`useSelector()`
- **Files:** 30+ files
- **Impact:** MAJOR

### 2. Router Patterns (2 files)

- **Pattern:** `withRouter` HOC → `useLocation()`/`useNavigate()`/`useParams()`
- **Files Found:**
    - `components/bannerList/BannerListContainer.js`
    - `components/bannerList/PaginatedListContainer.js`
- **Impact:** MINOR

### 3. Ref Patterns (3 files) ⚠️ NEEDS REVIEW

- **Pattern:** `createRef()` → `useRef()` (in functional components)
- **Files Found:**
    - `components/integratedUnits/IntegratedUnitsSearch.js`
    - `components/privatePractitioner/PrivatePractitionerSearch.js`
    - `components/intygInfo/IntygInfoSearch.js`
- **Status:** Already functional components using createRef() - should migrate to useRef()
- **Impact:** MINOR (good practice improvement)

### 4. PropTypes (11 files) ℹ️ OPTIONAL

- **Pattern:** Runtime PropTypes validation
- **Files:** 11 files use PropTypes
- **Decision:** KEEP (PropTypes are still valid in React 18, useful for runtime validation)
- **Impact:** NONE (no migration needed)

### 5. Recompose Patterns (20 files) ✅ ALREADY PLANNED

- **Pattern:** `lifecycle()`, `compose()` → hooks
- **Files:** 20 files
- **Impact:** MAJOR (already in original plan)

### 6. React Router 5→6 (All route files) ✅ ALREADY PLANNED

- **Pattern:** `<Switch>` → `<Routes>`, `<Redirect>` → `<Navigate>`
- **Impact:** MAJOR (already in original plan)

---

## Detailed Findings

### Finding 1: withRouter Usage (React Router 5 Pattern)

**Files:**

1. `components/bannerList/BannerListContainer.js`
2. `components/bannerList/PaginatedListContainer.js`

**Current Pattern:**

```javascript
import {withRouter} from 'react-router-dom'
import {compose} from 'recompose'
import {connect} from 'react-redux'

export default compose(
    withRouter,
    connect(mapStateToProps, actions),
    lifecycle(lifeCycleValues)
)(Component)
```

**Target Pattern (React Router 6):**

```javascript
import {useLocation, useNavigate, useParams} from 'react-router-dom'
import {useDispatch, useSelector} from 'react-redux'

const Component = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const params = useParams()
  const dispatch = useDispatch()
  // ... component code
}

export default Component
```

**Note:** `withRouter` is removed in React Router v6 - must use hooks

---

### Finding 2: createRef() in Functional Components

**Files:**

1. `components/integratedUnits/IntegratedUnitsSearch.js`
2. `components/privatePractitioner/PrivatePractitionerSearch.js`
3. `components/intygInfo/IntygInfoSearch.js`

**Current Pattern:**

```javascript
import React, {createRef, useEffect, useState} from 'react'

const Component = () => {
  const inputRef = createRef(); // ⚠️ Recreated on every render!
  // ...
}
```

**Target Pattern:**

```javascript
import React, {useRef, useEffect, useState} from 'react'

const Component = () => {
  const inputRef = useRef(null); // ✅ Persists across renders
  // ...
}
```

**Why:** `createRef()` creates a new ref on every render in functional components. `useRef()`
persists the ref across renders.

---

### Finding 3: this.props Usage (Only in lifecycle HOCs)

**Found in 6 files** within `lifecycle()` HOC functions:

- App.js
- UsersList.js
- SessionPollerContainer.js
- IntygInfoHistoryList.js
- DataExportList.js
- BannerListContainer.js

**Status:** Will be automatically fixed when migrating `lifecycle()` to `useEffect`

**No separate action needed** - already covered in Phase 2

---

### Finding 4: PropTypes Usage

**Files Using PropTypes (11 files):**

1. Toggler.js
2. SpinnerButton.js
3. iaButton4.js
4. RadioWrapper.js
5. Logout.js
6. DisplayDateTime.js
7. DisplayDate.js
8. FetchError.js
9. BannerListContainer.js
10. ErrorMessageFormatter.js
11. Alert.js

**Decision:** KEEP PropTypes

- PropTypes are still valid in React 18
- Provide runtime validation (useful in JavaScript projects)
- Not deprecated
- No migration needed

---

## Updated Migration Phases

### Phase 1: Foundation & Setup (3 increments) ✅ PLANNED

- 1.1: Update Core Dependencies
- 1.2: Migrate Entry Point to createRoot()
- 1.3: Enable StrictMode (optional)

### Phase 2: Remove Deprecations (10 increments) ✅ PLANNED

- 2.1: Migrate App.js (UNSAFE_componentWillMount)
- 2.2: Update configureStore.js
- 2.3: Update rootReducer
- 2.4-2.8: Migrate lifecycle() files (5 files)
- 2.9: Remove compose() (15+ files)
- 2.10: Remove recompose dependency

### Phase 2B: Redux Hooks Migration (2 increments) ⬅️ NEW

- **2B.1: Migrate connect() to Redux hooks (30+ files)**
    - Remove all `connect()` HOC usage
    - Replace with `useDispatch()`/`useSelector()`
    - Pattern established and applied to all files
- **2B.2: Migrate withRouter to Router hooks (2 files)**
    - Remove `withRouter` HOC
    - Replace with `useLocation()`/`useNavigate()`/`useParams()`

### Phase 2C: Ref Pattern Updates (1 increment) ⬅️ NEW (MINOR)

- **2C.1: Update createRef() to useRef() (3 files)**
    - IntegratedUnitsSearch.js
    - PrivatePractitionerSearch.js
    - IntygInfoSearch.js

### Phase 3: React Router 6 Migration (4 increments) ✅ PLANNED

- 3.1: Update SecuredRoute component
- 3.2: Update UnsecuredRoute component
- 3.3: Update route definitions
- 3.4: Update navigation code

### Phase 4: Testing Infrastructure (2 increments) ✅ PLANNED

- 4.1: Update setupTests.js
- 4.2: Setup MSW (optional)

### Phase 5: Add Tests (4 increments) ✅ PLANNED

- 5.1: Migrate Toggler.spec.js to RTL
- 5.2: Update App.test.js
- 5.3: Add comprehensive App.js tests
- 5.4: Add critical component tests

### Phase 6: Cleanup & Validation (2 increments) ✅ PLANNED

- 6.1: Replace node-sass with sass
- 6.2: Final cleanup

---

## Updated Timeline

**Original Estimate:** 4-6 days  
**With Redux Hooks:** 6-9 days  
**With All Modern Patterns:** 6.5-9.5 days

**Additional Time:**

- Redux hooks (30+ files): +2-3 days
- withRouter (2 files): +0.25 days
- createRef→useRef (3 files): +0.25 days

**Total:** 6.5-9.5 days

**Breakdown:**

- Phase 1: 1 day
- Phase 2: 1-2 days (recompose)
- Phase 2B: 2-3 days (Redux hooks + withRouter)
- Phase 2C: 0.5 days (useRef)
- Phase 3: 1-1.5 days (Router 6)
- Phase 4: 0.5 days (Test setup)
- Phase 5: 1-2 days (Tests)
- Phase 6: 0.5 days (Cleanup)

---

## Quality Gates - Complete List

### Phase 2B Completion Verification

**Redux Hooks:**

```bash
# Must return 0 (except Provider, test files, auth components)
grep -r "connect(" web/client/src/components/ | grep -v "// "
grep -r "connect(" web/client/src/pages/

# All components should use hooks
grep -r "useDispatch\|useSelector" web/client/src/ | wc -l
# Expected: 30+ occurrences
```

**Router Hooks:**

```bash
# Must return 0
grep -r "withRouter" web/client/src/

# Should use hooks instead
grep -r "useLocation\|useNavigate" web/client/src/
# Expected: 2+ occurrences
```

### Phase 2C Completion Verification

**Ref Patterns:**

```bash
# Functional components should use useRef, not createRef
grep -r "createRef()" web/client/src/components/
# Expected: 0 results

grep -r "useRef(" web/client/src/components/
# Expected: 4+ results (3 migrations + UseOnClickOutside)
```

---

## Benefits of Full Migration

### 1. Redux Hooks

- ✅ More readable code (no HOC nesting)
- ✅ Better performance (selective re-renders)
- ✅ Easier testing (no HOC mocking)
- ✅ Aligns with intyg-frontend

### 2. Router Hooks

- ✅ No HOC wrapper needed
- ✅ Direct access to router state
- ✅ Required for React Router 6

### 3. useRef Pattern

- ✅ Correct hook usage in functional components
- ✅ Prevents ref recreation on every render
- ✅ Better performance

### 4. Overall

- ✅ **100% modern React patterns**
- ✅ **Fully aligned with intyg-frontend**
- ✅ **More maintainable codebase**
- ✅ **Better developer experience**
- ✅ **Future-proof architecture**

---

## Migration Examples

### Example 1: Component with Everything

**Before (BannerListContainer.js):**

```javascript
import {compose, lifecycle} from 'recompose'
import {connect} from 'react-redux'
import {withRouter} from 'react-router-dom'

const Component = (props) => { /* ... */
}

const lifeCycleValues = {
  componentDidMount() {
    this.props.fetchData()
  }
}

const mapStateToProps = (state) => ({ /* ... */})

export default compose(
    withRouter,
    connect(mapStateToProps, actions),
    lifecycle(lifeCycleValues)
)(Component)
```

**After:**

```javascript
import {useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useLocation, useNavigate} from 'react-router-dom'

const Component = () => {
  const dispatch = useDispatch()
  const data = useSelector(state => state.data)
  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    dispatch(actions.fetchData())
  }, [dispatch])

  // ... component code
}

export default Component
```

### Example 2: Ref Pattern

**Before:**

```javascript
import React, {createRef, useState} from 'react'

const Component = () => {
  const inputRef = createRef() // ❌ Recreated on every render

  return <input ref={inputRef}/>
}
```

**After:**

```javascript
import React, {useRef, useState} from 'react'

const Component = () => {
  const inputRef = useRef(null) // ✅ Persists across renders

  return <input ref={inputRef}/>
}
```

---

## Final Scope Summary

**Total Files to Modify:** ~65+ files

| Pattern            | Files      | Effort     | Priority |
|--------------------|------------|------------|----------|
| recompose removal  | 20 files   | 1-2 days   | CRITICAL |
| connect() → hooks  | 30+ files  | 2-3 days   | HIGH     |
| withRouter → hooks | 2 files    | 0.25 days  | HIGH     |
| createRef → useRef | 3 files    | 0.25 days  | MEDIUM   |
| React Router 5→6   | All routes | 1-1.5 days | HIGH     |
| Testing migration  | ~6 tests   | 1-2 days   | HIGH     |
| Other updates      | Various    | 1.5 days   | MEDIUM   |

**Total Estimate:** 6.5-9.5 days

---

## ✅ Developer Approved

**Decision:** Proceed with FULL modern migration including:

- ✅ Redux hooks (useDispatch/useSelector)
- ✅ Router hooks (useLocation/useNavigate)
- ✅ Ref hooks (useRef)
- ✅ All other planned migrations

**Ready to continue with Increment 1.1!**

---

**Status:** APPROVED - Ready for Implementation  
**Next:** Continue with Increment 1.1 (Update Dependencies)  
**Updated:** January 14, 2026

**Files Found:**

1. App.js
2. components/users/UsersList.js
3. components/users/UsersActionBar.js
4. components/users/Users.js
5. components/users/dialogs/RemoveUser.dialog.js
6. components/users/dialogs/CreateUser.dialog.js
7. components/TestLinks/TestLinks.js
8. components/sessionPoller/SessionPollerContainer.js
9. components/ResendStatusCount/ResendStatusCount.js
10. components/privatePractitioner/PrivatePractitionerSearchResult.dialog.js
11. components/privatePractitioner/PrivatePractitionerSearch.js
12. components/privatePractitioner/PrivatePractitionerExport.js
13. components/modalContainer/modalContainer.js
14. components/loginOptions/LoginOptionsContainer.js
15. components/intygInfo/IntygInfoHistoryList.js
16. components/intygInfo/IntygInfoHistory.js
17. components/intygInfo/IntygInfoSearch.js
18. components/intygInfo/IntygInfoDialog.js
19. components/integratedUnits/IntegratedUnitsSearch.js
20. components/integratedUnits/IntegratedUnitsExport.js
21. components/dataExport/DataExport.js
22. components/dataExport/DataExportActionBar.js
23. components/dataExport/dialogs/UpdateDataExport.dialog.js
24. components/dataExport/dialogs/ResendDataExportKey.dialog.js
25. components/dataExport/dialogs/EraseDataExport.dialog.js
26. components/dataExport/dialogs/CreateDataExport.dialog.js
27. components/bannerList/BannerListContainer.js
28. components/bannerDialogs/CreateBanner.dialog.js
29. components/bannerDialogs/RemoveBanner.dialog.js
30. components/header/HeaderContainer.js
31. components/iaMenu/MenuBar.js
32. components/auth/SecuredRoute.js
33. components/auth/UnsecuredRoute.js
34. pages/ResendPage.js
    35+ (possibly more to be found in full scan)

---

## New Phase 2B: Migrate connect() to Redux Hooks

**Insert After:** Phase 2 (Remove Deprecations)  
**Before:** Phase 3 (React Router 6)

### Increment 2B.1: Create Example Migration Pattern

**Objective:** Establish Redux hooks pattern with one example file

**Example File:** `components/dataExport/DataExport.js`

**Before (connect HOC):**

```javascript
import React from 'react';
import {compose} from 'recompose';
import {connect} from 'react-redux';
import * as actions from '../../store/actions/dataExport';
import {getDataExportList, getIsFetching} from '../../store/reducers/dataExport';

const DataExport = ({dataExportList, fetchDataExportList, isFetching}) => {
  // component code
}

const mapStateToProps = (state) => {
  return {
    dataExportList: getDataExportList(state),
    isFetching: getIsFetching(state),
  };
}

export default compose(
    connect(mapStateToProps, actions)
)(DataExport);
```

**After (Redux hooks):**

```javascript
import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import * as actions from '../../store/actions/dataExport';
import {getDataExportList, getIsFetching} from '../../store/reducers/dataExport';

const DataExport = () => {
  const dispatch = useDispatch();
  const dataExportList = useSelector(getDataExportList);
  const isFetching = useSelector(getIsFetching);

  const fetchDataExportList = (params) => dispatch(actions.fetchDataExportList(params));

  // component code (unchanged)
}

export default DataExport;
```

**Changes:**

1. Remove `compose` and `connect` imports
2. Add `useDispatch` and `useSelector` imports
3. Remove `mapStateToProps` and `mapDispatchToProps`
4. Use `useSelector` for state
5. Use `useDispatch` for actions
6. Remove HOC wrapping at export

**Verify:** Build succeeds, component works

---

### Increment 2B.2: Migrate All connect() Files (30+ files)

**⚠️ IMPORTANT:** Agent MUST scan repository to find ALL files using connect()

**Scan Command:**

```bash
grep -r "from 'react-redux'" web/client/src/ | grep "connect"
```

**Pattern to Migrate:**

For each file found:

1. **Import Changes:**
   ```javascript
   // Remove
   import { connect } from 'react-redux'
   import { compose } from 'recompose'  // if present
   
   // Add
   import { useDispatch, useSelector } from 'react-redux'
   ```

2. **Component Changes:**
   ```javascript
   // Add hooks at top of component
   const dispatch = useDispatch()
   const stateValue = useSelector(state => state.value)
   
   // Or use selector functions
   const stateValue = useSelector(getSelectorFunction)
   ```

3. **Action Dispatching:**
   ```javascript
   // Instead of props
   const handleAction = () => dispatch(actionCreator())
   ```

4. **Remove HOC:**
   ```javascript
   // Remove
   const mapStateToProps = (state) => ({ ... })
   const mapDispatchToProps = (dispatch) => ({ ... })
   export default connect(mapStateToProps, mapDispatchToProps)(Component)
   
   // Replace with
   export default Component
   ```

**Files to Migrate (Representative List - Scan for Complete List):**

- All 30+ files found in analysis
- Some files may have both recompose AND connect (migrate both patterns)

**Verify After All:**

```bash
# Should return only test files and Provider usage
grep -r "from 'react-redux'" web/client/src/ | grep -v "useDispatch\|useSelector"
```

---

## Impact on Timeline

### Original Timeline

- **Phase 1:** Setup & Dependencies (1 day)
- **Phase 2:** Recompose & Deprecations (1-2 days)
- **Phase 3:** React Router 6 (1-1.5 days)
- **Phase 4:** Testing (0.5 day)
- **Phase 5:** Add Tests (1-2 days)
- **Phase 6:** Cleanup (0.5 day)
- **Total:** 4-6 days

### Updated Timeline

- **Phase 1:** Setup & Dependencies (1 day)
- **Phase 2:** Recompose & Deprecations (1-2 days)
- **Phase 2B:** Redux Hooks Migration (2-3 days) ⬅️ NEW
- **Phase 3:** React Router 6 (1-1.5 days)
- **Phase 4:** Testing (0.5 day)
- **Phase 5:** Add Tests (1-2 days)
- **Phase 6:** Cleanup (0.5 day)
- **Total:** 6-9 days

**Added Effort:** 2-3 days (30+ files to migrate)

---

## Benefits of Redux Hooks Migration

1. **Aligns with intyg-frontend** - Matches modern patterns
2. **More readable code** - No HOC nesting
3. **Easier to test** - Pure functions, no HOC mocking
4. **Better performance** - Selective re-renders with useSelector
5. **Simpler refactoring** - Less boilerplate
6. **Modern React patterns** - Hooks everywhere

---

## Migration Order Recommendation

**Revised Phase Order:**

1. **Phase 1:** Update Dependencies ✅
2. **Phase 2:** Remove recompose (lifecycle + compose)
3. **Phase 2B:** Migrate connect() to hooks (NEW)
4. **Phase 3:** React Router 6
5. **Phase 4:** Testing Infrastructure
6. **Phase 5:** Add Tests
7. **Phase 6:** Cleanup

**Rationale:**

- Remove recompose first (incompatible library)
- Then migrate connect to hooks (both are Redux patterns)
- Then update Router (routing layer)
- Then tests (test the final migrated code)

---

## Quality Gate Updates

**For Phase 2B Completion:**

**Repository Scan Required:**

```bash
# Must return 0 results (except Provider and test files)
grep -r "connect(" web/client/src/components/
grep -r "connect(" web/client/src/pages/

# All should use hooks
grep -r "useDispatch\|useSelector" web/client/src/components/ | wc -l
# Should be 30+ files
```

**Success Criteria:**

- ✅ All component files use useDispatch/useSelector
- ✅ No connect() HOC usage in components (only Provider in index.js)
- ✅ App builds successfully
- ✅ All features work correctly
- ✅ Redux DevTools still works

---

## Developer Decision Required

**Current Status:** Paused at Increment 1.1 (Update Dependencies)

**Question:** Should we proceed with the updated migration plan that includes Redux hooks migration?

**Option A: YES - Full Modern Migration (Recommended)**

- Migrate to Redux hooks (30+ files)
- Total time: 6-9 days
- Result: Fully aligned with intyg-frontend
- Modern React patterns throughout

**Option B: NO - Partial Migration (Faster)**

- Keep connect() HOC
- Total time: 4-6 days
- Result: React 18 compatible but not modernized
- Can migrate hooks later

**Please confirm which option you prefer before continuing.**

---

**Created:** January 14, 2026  
**Status:** Awaiting Developer Decision  
**Impact:** +2-3 days, +30 files to migrate

