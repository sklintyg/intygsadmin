# intygsadmin - React 16 to React 18 Migration Requirements

**Document Version:** 1.1 (Enhanced with intyg-frontend insights)  
**Date:** January 14, 2026  
**Application:** intygsadmin  
**Migration Target:** React 18.3.1 with modern React patterns  
**Current Version:** React 16.10.2  
**Reference Application:** intyg-frontend (webcert, minaintyg, rehabstod)

---

## Executive Summary

This document defines the requirements and design choices for migrating intygsadmin from React
16.10.2 to React 18.3.1. The migration will follow modern React best practices aligned with the
intyg-frontend monorepo patterns, eliminate deprecated patterns (lifecycle methods, recompose), and
leverage React 18 features like Concurrent Rendering and Automatic Batching. The application will
maintain its Redux-based state management while adopting functional components with hooks
throughout, with optional modernization to align with intyg-frontend's Redux Toolkit patterns.

**Key Migration Focus:**

- Upgrade React 16.10.2 → 18.3.1
- Migrate to React Router v6 (aligns with intyg-frontend)
- Eliminate `UNSAFE_componentWillMount` and other deprecated lifecycle methods
- Migrate from recompose HOCs to React hooks
- Replace Enzyme with React Testing Library (aligns with intyg-frontend)
- Modernize third-party dependencies (styled-components, react-router, etc.)
- Maintain Redux state management with optional Redux Toolkit migration path
- Align testing patterns with intyg-frontend (RTL + MSW)

---

## 1. Core Technology Stack

### 1.1 Framework and Runtime

**Target Versions:**

- **React Version:** 18.3.1 (from 16.10.2)
- **React DOM Version:** 18.3.1 (must match React version)
- **Node Version:** Node 18+ LTS (verify compatibility)
- **Package Manager:** npm (existing)
- **Build Tool:** Create React App / react-scripts (upgrade to 5.x for React 18 support)

**Migration Requirements:**

- Replace `ReactDOM.render()` with `createRoot()` in `src/index.js`
- Update all React imports to use new module structure
- Ensure all dependencies are React 18 compatible

### 1.2 State Management

**Current Stack:**

- Redux 4.0.1 with redux-thunk 2.3.0
- connected-react-router 6.4.0
- react-redux 6.0.1
- recompose 0.30.0 (for lifecycle HOCs)

**Target Stack (Option A - Conservative):**

- **Redux:** Keep Redux 4.x, update to 4.2.1
- **React-Redux:** Update to 8.1.3 (React 18 compatible)
- **Router Integration:** Remove connected-react-router, migrate to React Router 6 native (aligns
  with intyg-frontend)
- **Thunk:** Keep redux-thunk 2.4.2
- **Recompose:** REMOVE - migrate all usage to React hooks

**Target Stack (Option B - Modern, Aligns with intyg-frontend):**

- **Redux Toolkit:** Migrate to @reduxjs/toolkit 2.x (like minaintyg/rehabstod)
- **React-Redux:** Update to 8.1.3
- **Router Integration:** React Router 6 native (no connected-react-router)
- **Thunk:** Included in Redux Toolkit (RTK)
- **Recompose:** REMOVE

**Recommendation:** Start with Option A (conservative), with clear migration path to Option B if
beneficial.

**Migration Strategy:**

- Replace all `recompose` `lifecycle()` HOCs with `useEffect` hook
- Replace all `recompose` `compose()` with standard React patterns
- Remove connected-react-router dependency
- Use React Router 6 `useNavigate()` hook for programmatic navigation
- Keep Redux structure intact (actions, reducers, store) initially
- **Optional Enhancement:** Gradually migrate to Redux Toolkit slices (following intyg-frontend
  patterns)

**Redux Hooks Pattern (Required - aligns with intyg-frontend):**

Migrate from `connect()` HOC to Redux hooks:

**Before (connect HOC):**

```javascript
import {connect} from 'react-redux'

const Component = ({data, fetchData}) => {
  // component code
}

const mapStateToProps = (state) => ({
  data: state.data
})

const mapDispatchToProps = (dispatch) => ({
  fetchData: () => dispatch(fetchData())
})

export default connect(mapStateToProps, mapDispatchToProps)(Component)
```

**After (hooks):**

```javascript
import {useDispatch, useSelector} from 'react-redux'
import {fetchData} from './actions'

const Component = () => {
  const dispatch = useDispatch()
  const data = useSelector(state => state.data)

  const handleFetch = () => dispatch(fetchData())

  // component code
}

export default Component
```

**Benefits:**

- More readable code (no HOC wrapping)
- Better TypeScript support
- Easier to test
- Aligns with modern React patterns
- Matches intyg-frontend architecture

**Migration Notes:**

- Test coverage analysis identified recompose usage in `App.js` and `components/users/Users.js`
- All `lifecycle()` HOCs must be converted to functional components with hooks
- All `connect()` HOCs must be converted to `useDispatch`/`useSelector`
- UNSAFE_componentWillMount in App.js must be migrated to useEffect
- connected-react-router is not compatible with React Router 6 or React 18

### 1.3 Testing Strategy

**Current Stack:**

- Jest (via react-scripts 3.2.0)
- Enzyme 3.10.0 with enzyme-adapter-react-16
- Redux Mock Store 1.5.3
- Sinon 7.5.0 (for spies/stubs)
- Fetch Mock 7.5.1

**Target Stack:**

- **Jest:** Update via react-scripts 5.x (or consider Vitest for performance - see below)
- **React Testing Library:** @testing-library/react 13.x (React 18 compatible)
- **Jest DOM:** @testing-library/jest-dom 6.x for custom matchers
- **User Event:** @testing-library/user-event 14.x for interactions
- **Redux Mock Store:** Keep 1.5.4 or update if needed
- **Mock Service Worker (MSW):** Add 2.x for API mocking (aligns with intyg-frontend)
- **REMOVE:** Enzyme and enzyme-adapter-react-16 (no React 18 support)
- **REMOVE:** Sinon (RTL has built-in testing utilities)
- **REMOVE:** Fetch Mock (replace with MSW for better patterns)

**Alternative: Vitest (from intyg-frontend):**

- intyg-frontend uses Vitest v3.2.4 (10-20x faster than Jest)
- Better Vite integration if migrating build tools
- Consider for future optimization, but not required for initial migration
- **Recommendation:** Stick with Jest via react-scripts 5.x initially for simpler migration

**Testing Requirements:**

- Migrate existing Enzyme test (`Toggler.spec.js`) to React Testing Library
- Add tests AFTER React 18 migration is complete (test migrated code, not old code)
- Prioritize tests per test coverage analysis document:
    - CRITICAL: App.js with hooks (migrated from lifecycle)
    - CRITICAL: Auth components (SecuredRoute, UnsecuredRoute)
    - CRITICAL: SessionPoller
    - HIGH: Modal system
    - HIGH: Form components (date/time pickers)
    - MEDIUM: Page smoke tests
- Target coverage: 60%+ for components (intyg-frontend uses 75-80%, aim for gradual improvement)
- Use React Testing Library best practices (from intyg-frontend):
    - Query by role, label, text (user-centric, accessibility-first)
    - Avoid implementation details
    - Test behavior, not implementation
    - Use `user-event` instead of `fireEvent` for realistic interactions

**Mock Service Worker (MSW) Setup (from intyg-frontend):**

```javascript
// src/mocks/server.js
import {setupServer} from 'msw/node'
import {handlers} from './handlers'

export const server = setupServer(...handlers)

// src/setupTests.js
import {server} from './mocks/server'

beforeAll(() => server.listen({onUnhandledRequest: 'error'}))
afterEach(() => server.resetHandlers())
afterAll(() => server.close())
```

**Benefits of MSW:**

- Network-level mocking (more realistic than mocking fetch/axios)
- Same mock handlers for development and tests
- Can validate responses against schemas
- Better debugging (shows actual network requests)

**Test Structure:**

- Unit tests for utility functions and custom hooks
- Component tests for UI behavior
- Integration tests for Redux-connected components
- Use Mock Service Worker (MSW) for API mocking if needed

### 1.4 Styling Solution

**Current Stack:**

- styled-components 4.4.0
- Bootstrap 4.3.1 / reactstrap 8.0.0
- node-sass 4.11.0 (for SCSS)
- Custom styled components in `components/styles/`

**Target Stack:**

- **styled-components:** Update to 5.3.x or 6.x (React 18 compatible)
- **Bootstrap:** Keep 4.x or consider upgrade to 5.x (optional)
- **reactstrap:** Update to latest compatible with Bootstrap version
- **node-sass:** Migrate to sass (dart-sass) - node-sass is deprecated
- **Custom styles:** Maintain existing component structure

**Migration Notes:**

- Ensure all styled-components are React 18 compatible
- Test that styled components work with new React root API
- Update any SSR-related styled-components config if applicable

### 1.5 Build Configuration

**Current:**

- react-scripts 3.2.0 (Create React App 3)
- Webpack 4 (via react-scripts)

**Target:**

- **react-scripts:** 5.x (Create React App 5 with React 18 support)
- **Webpack:** 5 (via react-scripts)

**Build Requirements:**

- Update all CRA dependencies
- Ensure polyfills are correctly configured (core-js 3.2.1)
- Maintain IE 11 support if required (check browserslist)
- Code splitting with React.lazy() and Suspense (use where beneficial)

**Environment Variables:**

- Keep existing REACT_APP_ prefixed environment variables
- Update setupProxy.js if needed for new webpack-dev-server

### 1.6 Code Quality and Standards

**Existing:**

- ESLint (via react-scripts, extends "react-app")
- Prettier 1.18.2 with custom config

**Target:**

- **ESLint:** Update via react-scripts 5.x with React 18 rules
- **React Hooks Rules:** Enforce via eslint-plugin-react-hooks
- **Prettier:** Update to 2.x or 3.x
- **TypeScript:** NOT required (keep JavaScript)

**Code Standards:**

- All components must be functional (no class components)
- All hooks must follow hooks rules (no conditional hooks)
- PropTypes recommended for type safety (or migrate to TypeScript in future)

### 1.7 Naming Conventions

Follow existing intygsadmin patterns:

**Components:**

- PascalCase for components (e.g., `UsersList`, `DataExport`, `SessionPoller`)
- Match file names to component names

**Hooks:**

- camelCase with `use` prefix (e.g., `useOnClickOutside`)
- Custom hooks in `components/hooks/` directory

**Files:**

- `.js` extension for all JavaScript files (maintain consistency)
- Page files: `[Name]Page.js` (e.g., `UsersPage.js`, `BannerPage.js`)
- Component files: `[Name].js` (e.g., `Users.js`, `UsersList.js`)
- Dialog files: `[Name].dialog.js` (e.g., `CreateUser.dialog.js`)
- Test files: `[Name].test.js` or `[Name].spec.js`

**Conventions:**

- Continue using existing patterns (dialog suffixes, container suffixes, etc.)
- No breaking changes to naming conventions unless necessary

---

## 2. Architecture and Design Principles

### 2.1 Component Architecture

**Current Pattern:**

- Mix of functional and HOC-wrapped components
- recompose for lifecycle management
- Redux connect() for state management
- Styled components for styling

**Target Pattern:**

- **100% functional components** with hooks
- **NO class components**
- **NO recompose HOCs** (migrate to hooks)
- Redux connect() acceptable (or optionally migrate to hooks)
- Maintain separation of concerns:
    - **Pages:** Route-level components (in `pages/`)
    - **Components:** Reusable UI components (in `components/`)
    - **Dialogs:** Modal/dialog components (`*.dialog.js`)
    - **Containers:** Redux-connected components if needed

**Migration Requirements:**

- Convert all lifecycle HOCs to useEffect
- Convert all recompose utilities to equivalent hooks
- Keep component structure (don't restructure file organization)

### 2.2 React 18 New Features

**Mandatory Changes:**

- **New Root API:** Replace `ReactDOM.render()` with `createRoot()` in `index.js`
- **Automatic Batching:** Benefit from automatic batching (no code changes needed)

**Optional Enhancements:**

- **Suspense:** Consider using for code splitting (React.lazy already used?)
- **startTransition:** Use for non-urgent UI updates if needed
- **useId:** Use for generating unique IDs for accessibility
- **useDeferredValue:** Consider for expensive computations

**Not Required Initially:**

- Concurrent rendering features (can be adopted incrementally post-migration)
- Streaming SSR (not applicable for this app)

### 2.3 State Management Strategy

**Redux Structure (Keep):**

- Actions in `store/actions/`
- Reducers in `store/reducers/`
- Store configuration in `store/configureStore.js`
- Redux DevTools integration

**Patterns:**

- Continue using Redux for global state (user, app config, banner lists, etc.)
- Use local state (useState) for component-only state
- Use useEffect for side effects
- Keep redux-thunk for async actions

**Migration Notes:**

- Do NOT rewrite Redux store structure
- Update react-redux connect patterns to React 18 compatible versions
- connected-react-router may need update or replacement

### 2.4 Routing

**Current:**

- react-router-dom 5.1.2
- connected-react-router 6.4.0 (Redux integration)
- HashRouter for routing

**Target (Aligned with intyg-frontend):**

- **react-router-dom:** Update to 6.22.0+ (intyg-frontend uses 6.30.1)
- **Router API:** `createBrowserRouter` or `createHashRouter` with data router API
- **REMOVE:** connected-react-router (not React 18 compatible, not needed with React Router 6)
- **Maintain:** HashRouter strategy (use `createHashRouter` instead of `createBrowserRouter`)

**Migration Strategy:**

- Replace `<HashRouter>` with `<RouterProvider router={createHashRouter(routes)} />`
- Update route definitions to use new `createRoutesFromChildren` or JSX route structure
- Replace `useHistory()` with `useNavigate()`
- Update `<Redirect>` to `<Navigate>`
- Update `<Switch>` to `<Routes>` (if not already using Routes)
- Remove connected-react-router and any Redux navigation middleware

**React Router 6 Features to Adopt (from intyg-frontend):**

```javascript
// Modern route structure
export const routes = createRoutesFromChildren([
      <Route
          path="/"
          errorElement={<ErrorBoundary/>}
          element={<ProtectedRoute><Layout/></ProtectedRoute>}
      >
        <Route index element={<IndexPage/>}/>
        <Route path="banner" element={<BannerPage/>}/>
        <Route path="administratorer" element={<UsersPage/>}/>
        {/* ... other routes */}
      </Route>
    ])

    // In index.js
    < RouterProvider
router = {createHashRouter(routes)}
/>
```

**Error Boundaries:**

- Use `errorElement` prop for route-level error handling
- Create reusable `<ErrorBoundary />` component

**Protected Routes:**

- Wrap routes with `<ProtectedRoute>` component (similar to current SecuredRoute)
- Use React Router 6 layout routes for auth checking

**Routes to Preserve:**

- `/` - HomePage (IndexPage)
- `/banner` - BannerPage
- `/integratedUnits` - IntegratedUnitsPage
- `/privatePractitioner` - PrivatePractitionerPage
- `/intygInfo` - IntygInfoPage
- `/administratorer` - UsersPage
- `/dataExport` - DataExportPage
- `/resend` - ResendPage
- `/exit/:errorCode/:logId?` - ErrorPage
- `/loggedout/:code` - HomePage with logout code

**Auth Pattern:**

- Update SecuredRoute and UnsecuredRoute to work with React Router 6
- Use `<Outlet />` for nested route rendering
- Ensure auth logic works with new router patterns

**Breaking Changes from v5 to v6:**

- `useHistory()` → `useNavigate()`
- `useRouteMatch()` → `useMatch()`
- `<Redirect>` → `<Navigate>`
- `<Switch>` → `<Routes>` (if using)
- Relative routes work differently (now relative to route path, not current URL)
- Route element must be React element, not component

---

## 3. Deprecated Patterns Migration

### 3.1 Lifecycle Methods

**Current Usage (from test coverage analysis):**

- `UNSAFE_componentWillMount` in `App.js`
- `componentDidMount` in `App.js`
- `componentWillUnmount` in `App.js`

**Migration Requirements:**

- `UNSAFE_componentWillMount` → `useEffect` with empty dependency array (runs once)
- `componentDidMount` → `useEffect` with empty dependency array
- `componentWillUnmount` → `useEffect` cleanup function

**Example Migration:**

```javascript
// OLD (lifecycle HOC with recompose)
const lifeCycleValues = {
  UNSAFE_componentWillMount() {
    this.props.fetchAppConfig()
    this.props.getUser()
  },
  componentDidMount() {
    this.unlisten = history.listen(() => {
      this.props.closeAllModals()
    })
  },
  componentWillUnmount() {
    this.unlisten()
  },
}

export default compose(
    connect(null, mapDispatchToProps),
    lifecycle(lifeCycleValues)
)(App)

// NEW (hooks)
const App = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatch(fetchAppConfig())
    dispatch(getUser())
  }, [dispatch])

  useEffect(() => {
    const unlisten = history.listen(() => {
      dispatch(closeAllModals())
    })
    return () => unlisten()
  }, [dispatch])

  return (/* JSX */)
}
```

### 3.2 Recompose Migration

**Recompose Utilities to Replace:**

- `compose()` → Standard function composition or remove
- `lifecycle()` → useEffect hook
- Any other recompose HOCs → Equivalent hooks

**Migration Strategy:**

1. Identify all recompose usage (scan codebase)
2. Convert lifecycle() to useEffect
3. Convert compose() chains to standard React patterns
4. Remove recompose dependency

**Files with Known Usage:**

- `App.js` - lifecycle HOC
- `components/users/Users.js` - compose HOC
- Likely others (requires full scan)

### 3.3 Other Deprecated Patterns

**React 16 Patterns to Remove:**

- `findDOMNode` - Replace with refs if used
- String refs - Replace with callback refs or useRef
- Legacy context API - Replace with new Context API if used
- `componentWillReceiveProps` - Replace with useEffect + comparison if found
- `componentWillUpdate` - Replace with useEffect if found

**Scan Required:**

- Full codebase scan for deprecated patterns
- Document all occurrences
- Plan migration for each

---

## 4. Dependency Updates

### 4.1 Core Dependencies

**React Ecosystem:**

```json
{
  "react": "^18.3.1",
  "react-dom": "^18.3.1",
  "react-scripts": "^5.0.1",
  "react-redux": "^8.1.3",
  "react-router-dom": "^6.22.0"
}
```

**Redux (Option A - Keep Redux 4.x):**

```json
{
  "redux": "^4.2.1",
  "redux-thunk": "^2.4.2"
}
```

**Redux (Option B - Migrate to Redux Toolkit, aligns with intyg-frontend):**

```json
{
  "@reduxjs/toolkit": "^2.8.2"
}
```

Note: RTK includes Redux, Thunk, Immer, and Reselect built-in.

**Styling:**

```json
{
  "styled-components": "^6.1.8",
  "bootstrap": "^4.6.2",
  "reactstrap": "^9.2.2",
  "sass": "^1.70.0"
}
```

Note: Replace node-sass (deprecated) with dart-sass.

**UI Components:**

```json
{
  "react-datepicker": "^4.25.0",
  "react-responsive-modal": "^6.4.2",
  "react-js-pagination": "^3.0.3"
}
```

**Utilities:**

```json
{
  "lodash": "^4.17.21",
  "flat": "^6.0.1",
  "core-js": "^3.36.0"
}
```

### 4.2 Dev Dependencies

**Testing:**

```json
{
  "@testing-library/react": "^13.4.0",
  "@testing-library/jest-dom": "^6.2.0",
  "@testing-library/user-event": "^14.5.2",
  "msw": "^2.7.0",
  "redux-mock-store": "^1.5.4"
}
```

**Remove:**

```json
{
  "enzyme": "REMOVE",
  "enzyme-adapter-react-16": "REMOVE",
  "sinon": "REMOVE",
  "fetch-mock": "REMOVE"
}
```

**Build Tools:**

```json
{
  "prettier": "^3.2.4",
  "http-proxy-middleware": "^2.0.6"
}
```

### 4.3 Dependency Update Strategy

**Phase 1: Core React + Router Update**

1. Update React, ReactDOM, react-scripts
2. Update react-redux to 8.x
3. Update react-router-dom to 6.x
4. Remove connected-react-router
5. Test that app builds

**Phase 2: Remove Recompose**

1. Migrate all recompose usage to hooks
2. Remove recompose dependency
3. Test that app builds and runs

**Phase 3: Router Migration**

1. Migrate from React Router 5 to 6 patterns
2. Update all route definitions
3. Update navigation code (useHistory → useNavigate)
4. Update auth route patterns
5. Test all routing works

**Phase 4: Update Supporting Libraries**

1. Update styled-components to 6.x
2. Update UI component libraries (datepicker, modal, etc.)
3. Replace node-sass with sass
4. Test all UI components

**Phase 5: Update Testing**

1. Install React Testing Library and MSW
2. Remove Enzyme and Sinon
3. Update test setup
4. Migrate/add tests

**Phase 6: Optional Redux Toolkit Migration**

1. If beneficial, gradually migrate to Redux Toolkit
2. Convert action creators to createSlice
3. Use RTK Query for data fetching if needed
4. Maintain backwards compatibility during migration

---

## 5. Testing Requirements

### 5.1 Testing Strategy Summary

**Approach:** Test AFTER migration is complete

- More efficient: test final React 18 code, not intermediate states
- Avoid double work: don't test React 16 code that will change
- Use test coverage analysis as guide during migration

**Coverage Goals:**

- **Post-Migration Minimum:** 60% component coverage
- **Priority Areas:** As defined in test coverage analysis document
- **Maintain:** ~70% Redux actions/reducers coverage (already achieved)

### 5.2 Test Migration Plan

**Existing Tests:**

- `App.test.js` - Basic smoke test (UPDATE after App.js migration)
- `Toggler.spec.js` - Enzyme test (MIGRATE to RTL)
- Redux spec files - Keep (update if needed for React 18)

**New Tests to Add (Priority Order):**

**CRITICAL (Add First):**

1. **App.js comprehensive test**
    - Tests new hook-based lifecycle
    - Tests routing integration
    - Tests Redux integration
    - Tests history listener setup/cleanup

2. **Auth Components**
    - SecuredRoute component
    - UnsecuredRoute component
    - Test authentication flows
    - Test route protection

3. **SessionPoller**
    - Test polling behavior
    - Test session expiry handling
    - Test Redux integration

4. **Sample Redux Component**
    - Test `Users.js` or similar
    - Validates Redux connect + hooks pattern
    - Establishes pattern for other components

**HIGH (Add During Consolidation):**

5. Modal system tests
6. Form component tests (date/time pickers)
7. Page smoke tests (all 9 pages render)

**MEDIUM (Fill gaps):**

8. List/table component tests
9. Navigation component tests
10. Utility component tests

### 5.3 Test Requirements

**React Testing Library Best Practices:**

- Query by role, label, text (accessible queries)
- Test user behavior, not implementation
- Avoid testing internal state
- Use userEvent for interactions
- Mock API calls with fetch-mock or MSW

**Test Structure:**

```javascript
// Example test structure
import {render, screen} from '@testing-library/react'
import {Provider} from 'react-redux'
import configureStore from './store/configureStore'

test('renders component correctly', () => {
  const store = configureStore()
  render(
      <Provider store={store}>
        <MyComponent/>
      </Provider>
  )
  expect(screen.getByRole('button', {name: /submit/i})).toBeInTheDocument()
})
```

**Coverage Reporting:**

- Run `npm test -- --coverage` to generate reports
- Set up coverage thresholds in package.json
- Track coverage trends over time

---

## 6. Build Configuration

### 6.1 Package.json Scripts

**Maintain Existing Scripts:**

```json
{
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  }
}
```

**Add Coverage Script:**

```json
{
  "scripts": {
    "test:coverage": "react-scripts test --coverage --watchAll=false"
  }
}
```

### 6.2 Environment Configuration

**Maintain:**

- setupProxy.js for dev server proxying
- polyfills.js for IE 11 support (if needed)
- serviceWorker.js (if used)

**Update:**

- setupTests.js to use React Testing Library

### 6.3 Build Optimization

**Existing:**

- Code splitting (if used with React.lazy)
- Tree shaking
- Minification

**Maintain:**

- All existing build optimizations
- Ensure React 18 builds are not larger than React 16 builds

---

## 7. Migration Phases and Increments

### 7.1 Phase 1: Preparation (Current)

**Goal:** Establish baseline and plan

- ✅ Test coverage analysis complete
- ✅ Requirements document created
- 🔜 Migration guide creation
- 🔜 Progress tracker creation

### 7.2 Phase 2: Core Migration

**Phase 2.1: Dependencies and Setup**

- Upgrade React, ReactDOM, react-scripts
- Update react-redux
- Update setupTests.js (prepare for RTL)
- Verify app builds

**Phase 2.2: Fix Deprecated Patterns**

- Scan for all deprecated lifecycle methods
- Scan for all recompose usage
- Migrate App.js (highest priority)
- Remove recompose dependency

**Phase 2.3: Update Root API**

- Replace ReactDOM.render() with createRoot()
- Test app renders correctly

**Phase 2.4: Update Supporting Libraries**

- Update styled-components
- Update UI libraries (datepicker, modal)
- Replace node-sass with sass
- Test UI components work

**Phase 2.5: Router Update (if needed)**

- Evaluate and update routing
- Test all routes work

**Phase 2.6: Remove Enzyme, Add RTL**

- Install React Testing Library
- Remove Enzyme dependencies
- Update setupTests.js

### 7.3 Phase 3: Testing and Validation

**Phase 3.1: Add Critical Tests**

- App.js tests
- Auth component tests
- SessionPoller tests
- Sample Redux component test

**Phase 3.2: Add Additional Tests**

- Modal tests
- Form component tests
- Page tests
- Utility component tests

**Phase 3.3: Achieve Coverage Goals**

- Fill remaining gaps
- Reach 60%+ component coverage
- Update test coverage analysis

### 7.4 Phase 4: Cleanup and Optimization

**Cleanup:**

- Remove all deprecated patterns
- Remove unused dependencies
- Clean up any migration artifacts
- Update documentation

**Verification:**

- All tests pass
- App builds successfully
- No console warnings
- Manual QA of critical flows

---

## 8. Post-Migration Cleanup and Optimization

### 8.1 Dependency Cleanup

- ✅ Remove `recompose` dependency
- ✅ Remove `enzyme` and `enzyme-adapter-react-16`
- ✅ Remove `sinon` (use RTL utilities)
- ✅ Remove `node-sass` (replaced with `sass`)
- Review and remove any other unused dependencies
- Verify no React 16 specific dependencies remain

### 8.2 Code Optimization

**Remove:**

- All recompose imports and usage
- All Enzyme test utilities
- UNSAFE_ lifecycle methods
- Any deprecated patterns

**Optimize:**

- Consider converting more connect() to hooks (optional)
- Add React.lazy() for code splitting (if beneficial)
- Use React 18 features where appropriate (startTransition, useId)

### 8.3 Test Cleanup

- Ensure all tests pass
- Remove flaky tests or fix them
- Consolidate test utilities
- Document test patterns for team

### 8.4 Configuration Cleanup

- Remove old react-scripts configuration if any
- Clean up any migration-related comments
- Update README if needed

### 8.5 Performance Verification

- Verify bundle size hasn't increased significantly
- Test app performance (initial load, interactions)
- Verify React 18 automatic batching improves performance
- Check for any performance regressions

---

## 9. Key Insights from intyg-frontend Reference

This section summarizes patterns and practices from intyg-frontend (webcert, minaintyg, rehabstod)
that should inform the intygsadmin migration.

### 9.1 Component Patterns

**From intyg-frontend:**

- **100% functional components** with hooks (no class components)
- **Strict Mode** enabled in all apps for development safety
- **Minimal performance optimization** (no premature React.memo/useCallback)
- **Custom hooks** for reusable logic (e.g., `useNavigate`, `usePrevious`, `useLogout`)

**Apply to intygsadmin:**

- Convert all lifecycle-based components to functional with hooks
- Enable StrictMode during development
- Don't add unnecessary memoization initially
- Create custom hooks for common patterns (session polling, auth checks, etc.)

### 9.2 State Management Patterns

**From intyg-frontend:**

- **Redux Toolkit** in minaintyg/rehabstod (modern, v2.8.2)
- **Traditional Redux** still used in webcert (legacy, being modernized)
- **Typed hooks** pattern for type safety:
  ```javascript
  export const useAppDispatch = () => useDispatch()
  export const useAppSelector = useSelector
  ```
- **No connected-react-router** - using React Router 6 native navigation

**Apply to intygsadmin:**

- Keep Redux 4.x initially, but plan migration path to RTK
- Implement typed hooks pattern (even in JavaScript, good practice)
- Remove connected-react-router completely
- Use React Router 6 `useNavigate()` hook for programmatic navigation

### 9.3 Routing Patterns

**From intyg-frontend:**

- **React Router v6.30.1** with `createBrowserRouter` API
- **Route-level error boundaries** via `errorElement` prop
- **Nested layouts** with `<Outlet />` for shared UI
- **Protected routes** implemented as wrapper components
- **No Redux integration** for routing (removed connected-react-router)

**Apply to intygsadmin:**

- Migrate to React Router 6 with `createHashRouter` (maintain hash routing)
- Use `errorElement` for better error handling
- Refactor SecuredRoute/UnsecuredRoute to work with v6 patterns
- Remove all connected-react-router dependencies and middleware

### 9.4 Testing Patterns

**From intyg-frontend:**

- **React Testing Library** exclusively (no Enzyme)
- **Mock Service Worker (MSW)** for API mocking in tests and dev
- **Vitest** as test runner (10-20x faster than Jest)
- **High coverage thresholds:** 75-80% (branches, lines, statements)
- **Accessibility-first queries:** `getByRole`, `getByLabelText`
- **User-centric testing:** `user-event` for realistic interactions

**Apply to intygsadmin:**

- Replace Enzyme with React Testing Library
- Add MSW for API mocking (replace fetch-mock)
- Keep Jest initially (Vitest is optional future optimization)
- Set coverage target at 60% initially, grow to 75-80%
- Use accessibility queries and user-event patterns

### 9.5 Build and Tooling

**From intyg-frontend:**

- **Vite** as build tool (10-100x faster than Webpack)
- **pnpm** as package manager (faster, more efficient)
- **Strict TypeScript** configuration (type safety)
- **Monorepo architecture** with shared packages

**Apply to intygsadmin:**

- Keep Create React App/react-scripts 5.x for initial migration (simpler)
- Consider Vite migration in future for performance
- Keep npm (don't change package manager during migration)
- No TypeScript requirement (keep JavaScript)
- No monorepo needed (single app)

### 9.6 Code Quality Patterns

**From intyg-frontend:**

- **ESLint** with strict rules for hooks and TypeScript
- **Prettier** for consistent formatting
- **Strict Mode** enabled (catches potential issues)
- **No premature optimization** (React.memo only when needed)

**Apply to intygsadmin:**

- Enable ESLint React hooks rules via react-scripts
- Keep Prettier configuration
- Enable Strict Mode during development
- Avoid premature optimization during migration

### 9.7 What NOT to Adopt (Too Complex for This Migration)

**Skip these intyg-frontend patterns:**

- ❌ **TypeScript** - Keep JavaScript (big effort, separate migration)
- ❌ **Vite** - Keep react-scripts (don't change build tool + framework simultaneously)
- ❌ **pnpm** - Keep npm (don't change package manager during migration)
- ❌ **Vitest** - Keep Jest (simpler, included in react-scripts)
- ❌ **Monorepo** - Not applicable (single app)
- ❌ **Tailwind CSS** - Keep styled-components + Bootstrap (don't change styling approach)

### 9.8 Priority Alignment Summary

| Pattern               | intyg-frontend        | intygsadmin Target    | Priority |
|-----------------------|-----------------------|-----------------------|----------|
| React 18              | ✅ 18.3.1              | ✅ 18.3.1              | CRITICAL |
| Functional Components | ✅ 100%                | ✅ 100%                | CRITICAL |
| React Router 6        | ✅ v6.30.1             | ✅ v6.22.0+            | HIGH     |
| Redux Toolkit         | ✅ minaintyg/rehabstod | ⚠️ Optional (Phase 2) | MEDIUM   |
| React Testing Library | ✅ v16.3.0             | ✅ v13.4.0+            | HIGH     |
| MSW                   | ✅ v2.x                | ✅ v2.7.0              | MEDIUM   |
| Vitest                | ✅ v3.2.4              | ❌ Keep Jest           | LOW      |
| Vite                  | ✅ v5.4.21             | ❌ Keep CRA            | LOW      |
| TypeScript            | ✅ Strict              | ❌ Keep JavaScript     | N/A      |
| Monorepo              | ✅ pnpm workspace      | ❌ Single app          | N/A      |

---

## 10. Risk Management

The migration is complete and successful when:

**Build and Runtime:**

- [ ] Application builds successfully with no errors
- [ ] Application starts successfully with React 18
- [ ] No console warnings or errors (React or otherwise)
- [ ] All routes work correctly
- [ ] All pages render correctly

**Code Quality:**

- [ ] All class components converted to functional (if any existed)
- [ ] All recompose usage removed
- [ ] All UNSAFE_ lifecycle methods removed
- [ ] createRoot() is used in index.js
- [ ] No React 16 dependencies remain
- [ ] No deprecated patterns remain

**Functionality:**

- [ ] All features work correctly (manual testing)
- [ ] All user interactions function as expected
- [ ] Redux state management works correctly
- [ ] Routing and navigation work correctly
- [ ] Forms and inputs work correctly
- [ ] Modals and dialogs work correctly
- [ ] Session management works correctly
- [ ] All API calls work correctly

**Testing:**

- [ ] All existing tests pass (Redux tests)
- [ ] New critical tests added and passing (App, auth, session)
- [ ] Test coverage >= 60% for components
- [ ] Enzyme fully replaced with React Testing Library
- [ ] Test suite runs successfully

**Dependencies:**

- [ ] React upgraded to 18.3.1
- [ ] ReactDOM upgraded to 18.3.1
- [ ] react-scripts upgraded to 5.x
- [ ] react-redux updated to 8.x
- [ ] styled-components updated to 6.x
- [ ] All UI libraries React 18 compatible
- [ ] node-sass replaced with sass
- [ ] recompose removed
- [ ] Enzyme removed

**Documentation:**

- [ ] Migration progress document complete
- [ ] All OBSERVE items resolved or documented
- [ ] Test coverage analysis updated with final coverage
- [ ] README updated if needed

**Performance:**

- [ ] Bundle size not significantly increased
- [ ] App performance maintained or improved
- [ ] No performance regressions identified

---

## 10. Application-Specific Requirements

### 10.1 Existing Application Structure

**Maintain Structure:**

- Pages in `pages/` directory
- Components in `components/` directory (by feature)
- Redux in `store/` directory (actions, reducers)
- Styles in component-level files or `components/styles/`

**Preserve Features:**

- User management (administratörer)
- Banner management
- Integrated units management
- Private practitioner management
- Intyg info display
- Data export functionality
- Resend functionality
- Session polling and management
- Error handling and error pages
- Login options
- Modal system

### 10.2 Backend Integration

**Maintain:**

- All API endpoints and contracts
- API base URL configuration
- setupProxy.js for dev server
- Error handling patterns

**Do NOT:**

- Change API contracts
- Change backend integration patterns
- Modify backend code

### 10.3 Authentication and Authorization

**Maintain:**

- Existing auth patterns (SecuredRoute/UnsecuredRoute)
- User session management
- Session polling behavior
- Logout flows
- Login options

**Verify:**

- Auth still works after migration
- Session management works correctly
- Protected routes remain protected

### 10.4 Styling and UI

**Maintain:**

- Existing UI/UX design
- Bootstrap styling
- Custom styled components
- Responsive behavior
- Accessibility features

**Preserve:**

- Color scheme (iaColors)
- Typography (iaTypography)
- Layout patterns (iaLayout)
- SVG icons (iaSvgIcons)
- Custom inputs (HsaInput, etc.)

### 10.5 Browser Support

**Maintain:**

- Current browserslist configuration
- IE 11 support (if required - verify with team)
- Modern browser support
- Polyfills as needed

### 10.6 Development Environment

**Maintain:**

- setupProxy.js configuration
- Environment variables pattern (REACT_APP_)
- Development server configuration
- Build process

### 10.7 Known Issues and Concerns

**From Test Coverage Analysis:**

- OBSERVE: Verify how many files use deprecated lifecycle methods (full scan needed)
- OBSERVE: Verify extent of recompose usage (full scan needed)
- OBSERVE: Verify third-party library React 18 compatibility (test after upgrade)
- OBSERVE: connected-react-router may need replacement (evaluate during migration)

### 10.8 Success Metrics

**Functional:**

- Zero regression bugs reported
- All manual test cases pass
- All automated tests pass

**Performance:**

- Page load time maintained or improved
- Bundle size within 10% of current size
- No performance regressions in React DevTools profiler

**Code Quality:**

- Zero deprecated pattern warnings
- ESLint passes with no errors
- Test coverage >= 60%

**Developer Experience:**

- Build time maintained or improved
- Hot reload works correctly
- Dev server works correctly

---

## 10. Risk Management

### 10.1 Identified Risks

**HIGH RISK:**

- **React Router 5 → 6 migration** has breaking changes (different APIs, relative routing changes)
- connected-react-router removal requires rewriting navigation logic
- Third-party UI libraries may have breaking changes (datepicker, modal)
- Recompose migration may be more extensive than initially thought

**MEDIUM RISK:**

- styled-components update may have breaking changes (v4 → v6)
- Test migration more time-consuming than expected
- MSW learning curve and mock setup effort

**LOW RISK:**

- Core React 18 upgrade (well-documented, gradual adoption possible)
- Redux updates (minimal breaking changes expected)

### 10.2 Mitigation Strategies

**For React Router 6:**

- **Create migration checklist** for all v5 → v6 API changes
- **Migrate incrementally:** Start with simple routes, then complex ones
- **Reference intyg-frontend** patterns for proven approaches
- **Test routing thoroughly** after migration
- **Keep old patterns** temporarily if needed (can coexist during migration)

**For connected-react-router removal:**

- Replace with `useNavigate()` hook from React Router 6
- Remove Redux navigation middleware
- Update all `history.push()` calls to `navigate()`
- Test programmatic navigation thoroughly

**For UI libraries:**

- Update one library at a time
- Test each library after update
- Have rollback plan if library incompatible
- Check React 18 compatibility before updating

**For recompose:**

- Scan codebase early for all usage
- Create migration pattern/template
- Migrate incrementally with testing
- Reference intyg-frontend for hooks patterns

**For testing:**

- Start with simple components for RTL migration
- Establish patterns early (refer to intyg-frontend)
- Reuse test utilities across tests
- Add MSW incrementally (start with dev mocks, then test mocks)

### 10.3 Rollback Plan

If migration encounters critical issues:

1. Revert to previous commit (ensure clean commits)
2. Document issue encountered
3. Research solution
4. Retry migration with fix

**Git Strategy:**

- Commit frequently during migration
- Use descriptive commit messages
- Keep commits atomic (one logical change per commit)
- Test after each commit
- Tag stable points for easy rollback

---

## 11. Migration Phases and Increments

**Estimated Effort:** 4-6 days for experienced React developer (increased from 3-5 due to React
Router 6 migration)

**Breakdown:**

- Phase 1 (Preparation): ✅ Complete
- Phase 2.1 (Core Dependencies + Router): 0.5 day
- Phase 2.2 (Deprecations - recompose, lifecycle): 1-2 days (depends on extent)
- Phase 2.3 (Root API): 0.5 day
- Phase 2.4 (React Router 6 Migration): 1-1.5 days (NEW - significant effort)
- Phase 2.5 (Supporting libs): 0.5 day
- Phase 2.6 (Testing setup): 0.5 day
- Phase 3 (Add tests): 1-2 days
- Phase 4 (Cleanup): 0.5 day

**New Complexity Factors:**

- **React Router 5 → 6 migration** adds significant effort (breaking changes, API updates)
- Extent of recompose usage (requires scan)
- Number of deprecated lifecycle methods (requires scan)
- connected-react-router removal and navigation code updates
- Third-party library compatibility issues
- Testing thoroughness
- MSW setup and mock creation

**Risk Buffer:** Add 1 day buffer for unexpected React Router 6 issues

---

## 12. References and Resources

### 12.1 Official Documentation

- [React 18 Upgrade Guide](https://react.dev/blog/2022/03/08/react-18-upgrade-guide)
- [React 18 Release Notes](https://react.dev/blog/2022/03/29/react-v18)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)
- [React Hooks](https://react.dev/reference/react)
- [React Router v6 Guide](https://reactrouter.com/en/main/upgrading/v5)
- [Mock Service Worker (MSW)](https://mswjs.io/docs/)

### 12.2 Migration Guides

- [Upgrading to React 18](https://react.dev/blog/2022/03/08/react-18-upgrade-guide)
- [React Router v5 to v6](https://reactrouter.com/en/main/upgrading/v5)
- [Replace recompose with hooks](https://github.com/acdlite/recompose#a-note-from-the-author-acdlite-oct-25-2018)
- [Migrate from Enzyme to RTL](https://testing-library.com/docs/react-testing-library/migrate-from-enzyme/)
- [MSW Getting Started](https://mswjs.io/docs/getting-started)

### 12.3 Internal Documents

- **Test Coverage Analysis:** `.github/migration/intygsadmin-test-coverage-analysis.md`
- **Design Choices Reference:**
  `.github/migration/instructions/intyg-frontend-react-design-choices.md`
- **Migration Progress:** `.github/migration/intygsadmin-migration-progress.md` (to be created)
- **Migration Guide:** `.github/migration/intygsadmin-migration-guide.md` (to be created)

---

**Document Status:** ✅ Enhanced with intyg-frontend insights (v1.1)  
**Last Updated:** January 14, 2026  
**Next Action:** Generate General Analysis Instructions (Step 5), then Application-Specific
Migration Guide (Step 6)  
**Priority:** High - Foundation for entire migration

