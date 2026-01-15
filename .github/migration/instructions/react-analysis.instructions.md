# React 16 to React 18 Migration - General Analysis Instructions

**Document Version:** 1.0  
**Purpose:** General framework for analyzing React 16 applications for React 18 migration  
**Target:** Systematic repository analysis to inform application-specific migration guide  
**Based on:** react-analysis-template.md (customized for React 16→18 migrations)

---

## Overview

This document provides a systematic framework for analyzing React 16 applications to prepare for
React 18 migration. Use this as a checklist when examining the codebase to create a comprehensive
application-specific migration guide.

**Key Principle:** The application-specific migration guide created from this analysis is the heart
of the migration. Invest time in thorough analysis to create a complete, detailed guide.

---

## 1. Initial Application Assessment

### 1.1 Current React Setup Analysis

**Package.json Review:**

- Current React version (target: 16.x → 18.3.1)
- Current React DOM version (must match React version)
- Package manager (npm, yarn, pnpm)
- Build tool (Create React App / react-scripts, Webpack, Vite, custom)
- Node.js version requirement

**Build Tool Identification:**

- react-scripts version (if CRA) - need 5.x for React 18
- Webpack version (if custom) - need 5.x for React 18
- Vite version (if used) - need 4.x+ for React 18
- Custom build configuration complexity

**Document in Analysis:**

```
React Version: 16.x.x
Build Tool: react-scripts 3.x / Webpack 4 / Vite / Custom
Node Version: XX
Package Manager: npm/yarn/pnpm
TypeScript: Yes/No
```

### 1.2 Project Structure Analysis

**Examine Directory Structure:**

- Component organization (`components/`, `pages/`, `containers/`)
- State management location (`store/`, `redux/`, `context/`)
- Routing structure
- Testing structure (`__tests__/`, `*.test.js`, `*.spec.js`)
- Shared utilities and helpers

**Identify Patterns:**

- Naming conventions used
- Component organization strategy
- Co-location of styles and tests
- Feature-based vs. type-based organization

### 1.3 Component Architecture Analysis

**Component Census:**

```
Total Components: X
  - Functional Components: X
  - Class Components: X (need conversion if simple, or remain if error boundaries)
  - HOC Wrappers: X (identify recompose, custom HOCs)
  - Components with lifecycle methods: X
  - Components with UNSAFE_ methods: X (CRITICAL - must migrate)
  - Error Boundaries: X (remain as class components)
```

**Lifecycle Method Usage:**

- `componentDidMount` - how many? (→ useEffect with [])
- `componentDidUpdate` - how many? (→ useEffect with dependencies)
- `componentWillUnmount` - how many? (→ useEffect cleanup)
- `UNSAFE_componentWillMount` - how many? (→ useEffect, CRITICAL)
- `UNSAFE_componentWillReceiveProps` - how many? (→ useEffect with comparison)
- `UNSAFE_componentWillUpdate` - how many? (→ useEffect or remove)
- `shouldComponentUpdate` - how many? (→ React.memo or useMemo)
- `getDerivedStateFromProps` - how many? (→ useState with conditional logic)
- `getSnapshotBeforeUpdate` - how many? (→ useLayoutEffect, rare)

**Deprecated Pattern Scan:**

- String refs: `ref="myRef"` (→ useRef)
- `findDOMNode()` usage (→ useRef with ref forwarding)
- Legacy context API: `getChildContext`, `contextTypes` (→ Context API with useContext)
- `React.createClass` (very old, unlikely in React 16, but check)

**HOC and Composition Patterns:**

- recompose library usage (check for `compose`, `lifecycle`, `withState`, etc.)
- Custom HOCs (candidates for custom hooks)
- Render props patterns (candidates for custom hooks)
- Function as child patterns

---

## 2. Dependencies and Package Analysis

### 2.1 Core Dependencies Audit

**React Ecosystem (check package.json):**

```
React: 16.x → 18.3.1
ReactDOM: 16.x → 18.3.1
react-scripts: 3.x → 5.x (if CRA)
react-redux: X.x → 8.1.3 (if using Redux)
react-router-dom: 5.x → 6.22.0+ (major migration)
```

**State Management:**

- Redux version (4.x → keep or migrate to @reduxjs/toolkit 2.x)
- react-redux version (must update to 8.x for React 18)
- Redux middleware (thunk, saga, etc.)
- connected-react-router (INCOMPATIBLE with React Router 6 - remove)
- Context API usage (modern or legacy)
- Other state libraries (MobX, Zustand, Jotai, Recoil)

**Routing:**

- react-router-dom version (5.x → 6.22.0+, breaking changes)
- connected-react-router (remove, not compatible with RR6)
- Custom routing solutions

**Form Libraries:**

- Formik, React Hook Form, Redux Form, etc.
- React 18 compatibility check required

### 2.2 Styling Dependencies

**CSS-in-JS:**

- styled-components (4.x → 6.x for React 18)
- emotion (@emotion/react needs update)
- JSS or other CSS-in-JS solutions

**CSS Preprocessors:**

- node-sass (DEPRECATED - replace with sass/dart-sass)
- sass, less, stylus
- PostCSS configuration

**UI Frameworks:**

- Bootstrap + reactstrap
- Material-UI (@mui/material)
- Ant Design
- Chakra UI
- Tailwind CSS

**Check Compatibility:**

- Each UI library must support React 18
- Check for peerDependency warnings
- Review migration guides for breaking changes

### 2.3 Testing Dependencies

**Current Testing Stack:**

```
Test Runner: Jest X.x / Vitest X.x
Component Testing: Enzyme X.x / React Testing Library X.x
Enzyme Adapter: enzyme-adapter-react-16 (REMOVE for React 18)
Mocking: sinon, fetch-mock, msw
E2E: Cypress, Playwright, Selenium
```

**Required Updates:**

- **CRITICAL:** Enzyme has NO official React 18 adapter
    - Migrate to React Testing Library (recommended)
    - Or use unofficial adapter (not recommended)
- Jest: update to latest
- React Testing Library: update to 13.x+ for React 18
- Remove sinon if RTL is adopted (has built-in utilities)
- Add MSW (Mock Service Worker) for better API mocking

### 2.4 Third-Party Component Libraries

**Scan for:**

- Date pickers (react-datepicker, rc-calendar, etc.)
- Modals (react-modal, react-responsive-modal, etc.)
- Dropdowns and selects (react-select, downshift, etc.)
- Tooltips (react-tooltip, tippy.js, etc.)
- Drag and drop (react-dnd, react-beautiful-dnd, etc.)
- Rich text editors (Draft.js, Slate, TinyMCE, Quill)
- Charts (recharts, victory, chart.js, etc.)
- Tables (react-table, ag-grid, etc.)
- Notifications (react-toastify, notistack, etc.)
- File uploads (react-dropzone, etc.)

**For Each Library:**

```
Library: [name]
Current Version: X.x.x
React 18 Compatible: Yes/No/Unknown
Required Update: X.x.x → Y.y.y
Breaking Changes: Yes/No (list if yes)
Alternative if incompatible: [name or build custom]
```

### 2.5 Build and Development Tools

**Build Configuration:**

- Webpack configuration complexity
- Babel presets and plugins
- PostCSS, Autoprefixer configuration
- Environment variables setup
- Proxy configuration for development

**Code Quality Tools:**

- ESLint version and plugins
    - eslint-plugin-react (update)
    - eslint-plugin-react-hooks (critical for hooks rules)
    - eslint-plugin-jsx-a11y (accessibility)
- Prettier version
- Pre-commit hooks (husky, lint-staged)

---

## 3. Component Migration Analysis

### 3.1 Class Component Inventory

**Categorize by Complexity:**

**Simple (Easy to Convert):**

- Few or no lifecycle methods
- Simple state
- No refs
- Candidates for early migration

**Medium (Moderate Effort):**

- Multiple lifecycle methods
- Complex state
- Some refs
- Migrate mid-phase

**Complex (Careful Planning):**

- Many lifecycle methods
- Complex state logic
- Multiple refs
- Context usage
- Migrate later with thorough testing

**Keep as Class:**

- Error boundaries (must remain class components in React 18)

**Example Inventory:**

```
Simple: [List component names or paths]
Medium: [List component names or paths]
Complex: [List component names or paths]
Error Boundaries: [List component names or paths]
```

### 3.2 Lifecycle Method Mapping Strategy

**For Each Component with Lifecycle Methods:**

**componentDidMount → useEffect with []:**

```javascript
// Old
componentDidMount()
{
  fetchData();
}

// New
useEffect(() => {
  fetchData();
}, []); // empty deps = run once
```

**componentDidUpdate → useEffect with dependencies:**

```javascript
// Old
componentDidUpdate(prevProps)
{
  if (prevProps.id !== this.props.id) {
    fetchData(this.props.id);
  }
}

// New
useEffect(() => {
  fetchData(id);
}, [id]); // run when id changes
```

**componentWillUnmount → useEffect cleanup:**

```javascript
// Old
componentWillUnmount()
{
  cleanup();
}

// New
useEffect(() => {
  setup();
  return () => cleanup(); // cleanup function
}, []);
```

**UNSAFE_componentWillMount → useEffect or remove:**

```javascript
// Old (DEPRECATED)
UNSAFE_componentWillMount()
{
  initializeData();
}

// New - Option 1: useEffect
useEffect(() => {
  initializeData();
}, []);

// New - Option 2: useState with function (if initialization only)
const [data] = useState(() => initializeData());
```

### 3.3 State Conversion Patterns

**Simple State:**

```javascript
// Old
this.state = {count: 0};
this.setState({count: 1});

// New
const [count, setCount] = useState(0);
setCount(1);
```

**Complex State:**

```javascript
// Old
this.state = {
  users: [],
  loading: false,
  error: null
};

// New - Option 1: Multiple useState
const [users, setUsers] = useState([]);
const [loading, setLoading] = useState(false);
const [error, setError] = useState(null);

// New - Option 2: useReducer (if related state)
const [state, dispatch] = useReducer(reducer, {
  users: [],
  loading: false,
  error: null
});
```

### 3.4 HOC and Recompose Migration

**recompose Library:**
If recompose is used, ALL usage must be migrated to hooks:

**Common recompose utilities:**

- `compose()` → standard function composition or remove
- `lifecycle()` → useEffect
- `withState()` → useState
- `withHandlers()` → useCallback
- `withProps()` → useMemo or inline
- `mapProps()` → useMemo
- `withContext()` → useContext

**Custom HOCs:**

- Analyze each HOC purpose
- Extract logic into custom hooks where possible
- Keep HOCs only if necessary (cross-cutting concerns)

---

## 4. React 18 Specific Requirements

### 4.1 New Root API Migration (MANDATORY)

**Entry Point Changes:**

**Location:** Usually `src/index.js` or `src/main.jsx`

**Before:**

```javascript
import ReactDOM from 'react-dom';

ReactDOM.render(<App/>, document.getElementById('root'));
```

**After:**

```javascript
import {createRoot} from 'react-dom/client';

const root = createRoot(document.getElementById('root'));
root.render(<App/>);
```

**If SSR/Hydration:**

```javascript
// Before
ReactDOM.hydrate(<App/>, document.getElementById('root'));

// After
import {hydrateRoot} from 'react-dom/client';

hydrateRoot(document.getElementById('root'), <App/>);
```

### 4.2 Automatic Batching (No Changes Needed)

**Analyze Code for:**

- Code relying on synchronous state updates (rare, but possible)
- Usage of `unstable_batchedUpdates` (can remove in React 18)
- Multiple setState calls (will batch automatically now)

**Benefit:** Performance improvement with no code changes in most cases.

### 4.3 Concurrent Features (Optional, Post-Migration)

**Evaluate After Core Migration:**

**startTransition (for non-urgent updates):**

```javascript
import {startTransition} from 'react';

startTransition(() => {
  setSearchQuery(value); // non-urgent update
});
```

**Use cases:**

- Filtering large lists
- Search inputs
- Navigation that triggers heavy renders

**useDeferredValue (defer expensive re-renders):**

```javascript
const deferredValue = useDeferredValue(value);
```

**Use cases:**

- Large list rendering
- Expensive computations
- Keeping UI responsive

**useId (for accessibility):**

```javascript
const id = useId();
<label htmlFor={id}>Name</label>
<input id={id}/>
```

### 4.4 Strict Mode Validation

**Ensure StrictMode is Enabled:**

```javascript
// In index.js
root.render(
    <React.StrictMode>
      <App/>
    </React.StrictMode>
);
```

**React 18 StrictMode Changes:**

- Double-invokes effects in development (to find missing cleanup)
- Helps identify components with side effects in render
- Ensures proper effect cleanup

**Check:**

- All effects have proper cleanup if needed
- No side effects in render phase
- Components work correctly with double-invocation

---

## 5. Routing Migration Analysis

### 5.1 React Router Version Check

**Current Version:**

- React Router 5.x or earlier → Must migrate to 6.x
- connected-react-router → Must remove (not compatible with RR6)

**React Router 6 Major Changes:**

```
<Switch> → <Routes>
<Route component={} /> → <Route element={<Component />} />
useHistory() → useNavigate()
useRouteMatch() → useMatch()
<Redirect> → <Navigate>
withRouter → hooks (useNavigate, useParams, useLocation)
exact prop → default behavior (remove)
```

### 5.2 Route Definition Migration

**Before (React Router 5):**

```javascript
<Switch>
  <Route exact path="/" component={Home}/>
  <Route path="/users" component={Users}/>
  <Redirect to="/"/>
</Switch>
```

**After (React Router 6):**

```javascript
<Routes>
  <Route path="/" element={<Home/>}/>
  <Route path="/users" element={<Users/>}/>
  <Route path="*" element={<Navigate to="/" replace/>}/>
</Routes>
```

### 5.3 Navigation Code Migration

**Programmatic Navigation:**

```javascript
// Before
import {useHistory} from 'react-router-dom';

const history = useHistory();
history.push('/users');
history.replace('/home');

// After
import {useNavigate} from 'react-router-dom';

const navigate = useNavigate();
navigate('/users');
navigate('/home', {replace: true});
```

### 5.4 connected-react-router Removal

**If using connected-react-router:**

1. Remove package dependency
2. Remove router reducer from Redux store
3. Remove router middleware
4. Replace Redux-based navigation with `useNavigate` hook
5. Update all `history.push()` calls in Redux actions

---

## 6. Testing Migration Analysis

### 6.1 Current Test Infrastructure

**Inventory:**

```
Total Test Files: X
Component Tests: X
  - Enzyme: X (must migrate)
  - RTL: X (update)
Integration Tests: X
E2E Tests: X
Test Coverage: X%
```

**Test Patterns Used:**

- Shallow rendering (Enzyme) vs. full rendering
- Component state testing (implementation detail)
- Snapshot testing
- Async testing patterns

### 6.2 Enzyme to React Testing Library Migration

**Why Migrate:**

- No official Enzyme adapter for React 18
- RTL encourages better testing practices (test behavior, not implementation)
- RTL is React team recommended approach
- Aligns with accessibility best practices

**Migration Strategy:**

**Enzyme Patterns → RTL Equivalents:**

```javascript
// Enzyme
import {shallow, mount} from 'enzyme';

const wrapper = shallow(<Component/>);
wrapper.find('.button').simulate('click');
expect(wrapper.state('count')).toBe(1);

// RTL
import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';

render(<Component/>);
await userEvent.click(screen.getByRole('button'));
expect(screen.getByText('Count: 1')).toBeInTheDocument();
```

**Query Priority (RTL Best Practices):**

1. **getByRole** - Most accessible, preferred
2. **getByLabelText** - Forms
3. **getByPlaceholderText** - Forms (if no label)
4. **getByText** - Non-interactive content
5. **getByDisplayValue** - Form inputs with values
6. **getByAltText** - Images
7. **getByTitle** - Last resort
8. **getByTestId** - Only if no other option

**Avoid:**

- Testing component internal state
- Testing implementation details
- Using enzyme shallow rendering equivalents

### 6.3 Mock Service Worker (MSW) Addition

**Benefits over fetch-mock/sinon:**

- Network-level mocking (more realistic)
- Same handlers for dev and tests
- Doesn't mock fetch/axios directly
- Can validate responses against schemas

**Setup:**

```javascript
// src/mocks/handlers.js
import {rest} from 'msw';

export const handlers = [
  rest.get('/api/users', (req, res, ctx) => {
    return res(ctx.json({users: []}));
  }),
];

// src/mocks/server.js
import {setupServer} from 'msw/node';
import {handlers} from './handlers';

export const server = setupServer(...handlers);

// src/setupTests.js
import {server} from './mocks/server';

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
```

### 6.4 Test Configuration Updates

**Update setupTests.js:**

```javascript
// Remove Enzyme setup
// import { configure } from 'enzyme';
// import Adapter from 'enzyme-adapter-react-16';
// configure({ adapter: new Adapter() });

// Add RTL setup
import '@testing-library/jest-dom';
import {server} from './mocks/server';

beforeAll(() => server.listen({onUnhandledRequest: 'error'}));
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
```

**Update Jest Configuration:**

- Ensure jsdom environment
- Update coverage thresholds if needed
- Configure transformIgnorePatterns for node_modules if needed

---

## 7. Build and Configuration Updates

### 7.1 Build Tool Assessment

**Create React App (react-scripts):**

```
Current: react-scripts 3.x
Target: react-scripts 5.x
Changes: React 18 support, Webpack 5, updated dependencies
```

**Custom Webpack:**

```
Current: Webpack 4.x
Target: Webpack 5.x
Changes: Updated loaders, plugins, configuration syntax
```

**Vite (if migrating):**

```
Benefits: 10-100x faster dev server, simpler config
Considerations: Different env variables (VITE_ prefix), different plugins
```

### 7.2 Package Manager Considerations

**npm vs yarn vs pnpm:**

- Stick with current package manager during migration
- Don't change package manager AND framework simultaneously
- Consider pnpm for future (faster, more efficient, monorepo support)

### 7.3 Node Version

**Verify Node.js Version:**

- React 18 requires Node 14+ (12.x is EOL)
- Recommend Node 18 LTS or Node 20
- Update .nvmrc if present
- Update CI/CD pipelines

### 7.4 Environment Variables

**If migrating build tools:**

- CRA: REACT_APP_ prefix
- Vite: VITE_ prefix
- Webpack: process.env.X

**Audit all environment variables and update if needed.**

---

## 8. Performance and Bundle Analysis

### 8.1 Current Bundle Analysis

**Measure Baseline:**

```
Total Bundle Size: X KB
Main Bundle: X KB
Vendor Bundle: X KB
Chunks: X files
Lazy-loaded Routes: X
```

**Tools:**

- webpack-bundle-analyzer (for Webpack)
- rollup-plugin-visualizer (for Vite)
- source-map-explorer

### 8.2 Code Splitting Opportunities

**Identify:**

- Routes that can be lazy-loaded
- Large components suitable for React.lazy()
- Vendor dependencies that can be split
- Unused code that can be tree-shaken

**Implement:**

```javascript
const HeavyComponent = React.lazy(() => import('./HeavyComponent'));

<Suspense fallback={<Loading/>}>
  <HeavyComponent/>
</Suspense>
```

### 8.3 React 18 Performance Features

**Automatic Batching:**

- Free performance win (no changes)
- Multiple state updates batch automatically

**Transitions (Optional):**

- Mark non-urgent updates
- Keep UI responsive during heavy renders
- Consider for search, filtering, large lists

**useDeferredValue (Optional):**

- Defer expensive computations
- Useful for search inputs with large result lists

---

## 9. Migration Planning and Strategy

### 9.1 Component Migration Priority

**Priority 1 (First):**

- Entry point (index.js) - createRoot() migration
- Error boundaries (remain as class, verify React 18 compatibility)
- Simple functional components (already compatible, just test)

**Priority 2 (Early):**

- Simple class components (few lifecycle methods)
- Components with UNSAFE_ methods (critical to fix)
- Shared/common components used everywhere

**Priority 3 (Mid):**

- Medium complexity class components
- HOCs and recompose usage
- Custom hooks extraction

**Priority 4 (Later):**

- Complex class components (thorough testing needed)
- Low-usage components
- Components with test coverage gaps

**Priority 5 (Last):**

- Experimental or unused components
- Legacy components marked for removal

### 9.2 Incremental Migration Approach

**Phase 1: Foundation (Week 1)**

- Update React, ReactDOM to 18.3.1
- Update react-scripts to 5.x (or build tool)
- Update react-redux to 8.x
- Migrate entry point to createRoot()
- Verify app builds and runs
- Fix any immediate breaking issues

**Phase 2: Deprecations (Week 1-2)**

- Scan and fix all UNSAFE_ lifecycle methods
- Remove recompose library usage
- Update any deprecated patterns
- Verify app still works

**Phase 3: Routing (Week 2)**

- Update react-router-dom to 6.x
- Remove connected-react-router
- Migrate route definitions
- Update navigation code
- Test all routes work

**Phase 4: Testing (Week 2-3)**

- Install React Testing Library and MSW
- Remove Enzyme
- Migrate critical tests
- Establish testing patterns

**Phase 5: Components (Week 3-4)**

- Migrate class components to functional (incremental)
- Convert HOCs to hooks
- Add tests as you go
- Verify functionality

**Phase 6: Optimization (Week 4-5)**

- Add code splitting
- Optimize bundle size
- Implement React 18 concurrent features (optional)
- Performance testing

**Phase 7: Cleanup (Week 5)**

- Remove deprecated dependencies
- Clean up dead code
- Update documentation
- Final QA and testing

### 9.3 Risk Mitigation

**High Risk Areas:**

- Components with UNSAFE_ methods
- Complex class components with many lifecycle methods
- Third-party libraries without React 18 support
- Areas with low test coverage

**Mitigation Strategies:**

- Add tests BEFORE migrating high-risk components
- Create feature flags for gradual rollout
- Maintain backward compatibility where possible
- Have rollback plan ready
- Test thoroughly at each increment

### 9.4 Testing Strategy

**Test Levels:**

- Unit tests: Hooks, utilities, pure functions
- Component tests: UI behavior with RTL
- Integration tests: Feature flows
- E2E tests: Critical user journeys

**Test Coverage Goals:**

- Baseline coverage before migration: X%
- Target coverage after migration: X% (at least maintain, ideally improve)
- Critical paths: 80%+ coverage
- All migrated components: 60%+ coverage

---

## 10. Documentation and Communication

### 10.1 Migration Progress Tracking

**Document:**

- Components migrated: X / Y
- Tests updated: X / Y
- Dependencies updated: X / Y
- Issues encountered: [List]
- Blockers: [List]
- OBSERVE flags: [List requiring developer input]

### 10.2 Code Documentation Updates

**Update:**

- README.md with React 18 information
- CONTRIBUTING.md with new patterns
- Component documentation
- API documentation
- Setup instructions if changed

### 10.3 Team Communication

**Communicate:**

- Migration plan and timeline
- New patterns and conventions (hooks, RTL)
- Breaking changes
- How to write new components
- How to write new tests
- Training sessions if needed

---

## 11. Post-Migration Validation

### 11.1 Functional Validation Checklist

**Critical Flows:**

- [ ] Application starts without errors
- [ ] User authentication works
- [ ] All routes accessible
- [ ] Forms submit correctly
- [ ] Data fetching works
- [ ] State management works
- [ ] Navigation works
- [ ] Session management works
- [ ] Error handling works
- [ ] Modals and dialogs work
- [ ] File uploads work (if applicable)
- [ ] Downloads work (if applicable)
- [ ] Search and filtering work
- [ ] Pagination works
- [ ] Sorting works

**Cross-Browser Testing:**

- [ ] Chrome
- [ ] Firefox
- [ ] Safari
- [ ] Edge
- [ ] Mobile browsers (iOS Safari, Chrome Mobile)

**Device Testing:**

- [ ] Desktop (various resolutions)
- [ ] Tablet
- [ ] Mobile

### 11.2 Performance Validation

**Compare Metrics:**

```
                Before      After       Change
Bundle Size:    X KB        Y KB        +/-Z KB
Load Time:      X ms        Y ms        +/-Z ms
TTI:           X ms        Y ms        +/-Z ms
LCP:           X ms        Y ms        +/-Z ms
FID:           X ms        Y ms        +/-Z ms
CLS:           X           Y           +/-Z
```

**Tools:**

- Lighthouse
- React DevTools Profiler
- Chrome DevTools Performance
- Web Vitals library

### 11.3 Test Coverage Validation

**Coverage Report:**

```
                    Before      After       Change
Statements:         X%          Y%          +/-Z%
Branches:           X%          Y%          +/-Z%
Functions:          X%          Y%          +/-Z%
Lines:              X%          Y%          +/-Z%

Critical Components: X%        Y%          +/-Z%
```

**Verify:**

- [ ] All tests pass
- [ ] No skipped or disabled tests
- [ ] Coverage maintained or improved
- [ ] No flaky tests
- [ ] Test suite runs in reasonable time

### 11.4 Code Quality Validation

**Checks:**

- [ ] No console.error in production
- [ ] No React warnings in console
- [ ] ESLint passes with no errors
- [ ] No deprecated patterns remain
- [ ] All UNSAFE_ methods removed
- [ ] All recompose usage removed
- [ ] All Enzyme tests migrated
- [ ] No TODO or FIXME comments without issues

### 11.5 Accessibility Validation

**WCAG 2.1 Level AA:**

- [ ] Keyboard navigation works
- [ ] Screen reader compatible
- [ ] Sufficient color contrast
- [ ] Proper ARIA labels
- [ ] Focus management correct
- [ ] Form labels present
- [ ] Error messages clear

**Tools:**

- axe DevTools
- WAVE browser extension
- Lighthouse accessibility audit
- Manual keyboard testing
- Screen reader testing (NVDA, JAWS, VoiceOver)

---

## 12. Rollback and Contingency Planning

### 12.1 Git Strategy

**Best Practices:**

- Work in feature branch
- Commit frequently with descriptive messages
- Tag stable migration milestones
- Keep main branch deployable
- Create migration branch from main

**Commit Message Pattern:**

```
feat(migration): migrate User component to hooks
fix(migration): resolve routing issue in Nav
test(migration): add RTL tests for Dashboard
docs(migration): update README for React 18
```

### 12.2 Deployment Strategy

**Options:**

**Option A: Feature Flag**

- Deploy both versions
- Toggle between old and new
- Gradual rollout to users
- Easy rollback

**Option B: Blue-Green Deployment**

- Deploy new version to staging
- Test thoroughly
- Switch traffic
- Keep old version ready

**Option C: Incremental Pages**

- Migrate page by page in production
- Users see mix of old and new
- Lower risk

### 12.3 Rollback Plan

**If Critical Issues Arise:**

1. **Immediate:** Revert deployment to last stable version
2. **Identify:** Root cause of issue
3. **Fix:** In development environment
4. **Test:** Thoroughly before redeploying
5. **Document:** Issue and resolution

**Rollback Triggers:**

- Critical functionality broken
- Performance degradation >20%
- Security vulnerabilities introduced
- Data loss or corruption
- Widespread user complaints

---

## 13. Final Checklist

### 13.1 Pre-Migration Checklist

- [ ] Test coverage baseline established
- [ ] All dependencies inventoried
- [ ] Migration plan approved
- [ ] Timeline agreed upon
- [ ] Team trained on new patterns
- [ ] Backup/rollback plan ready
- [ ] Monitoring and alerts configured

### 13.2 Migration Checklist

- [ ] React and ReactDOM updated to 18.3.1
- [ ] Build tool updated (react-scripts 5.x or equivalent)
- [ ] Entry point migrated to createRoot()
- [ ] All UNSAFE_ methods removed
- [ ] recompose removed
- [ ] React Router updated to 6.x
- [ ] connected-react-router removed
- [ ] Enzyme removed
- [ ] React Testing Library added
- [ ] MSW added for mocking
- [ ] All class components evaluated (migrate or keep)
- [ ] All tests passing
- [ ] No console warnings
- [ ] Performance acceptable

### 13.3 Post-Migration Checklist

- [ ] All features working
- [ ] All tests passing
- [ ] Test coverage maintained/improved
- [ ] Performance metrics acceptable
- [ ] No deprecated patterns remain
- [ ] Documentation updated
- [ ] Team trained
- [ ] Monitoring shows no issues
- [ ] User feedback positive
- [ ] Rollback plan tested

---

## Notes for Application-Specific Guide Creation

When using this analysis framework to create the application-specific migration guide:

1. **Be Thorough:** Scan entire codebase systematically
2. **Be Specific:** List actual file names, component names, patterns found
3. **Be Detailed:** Provide code examples from actual codebase
4. **Be Incremental:** Plan small, testable increments
5. **Be Clear:** Mark uncertainties with `OBSERVE` flags
6. **Be Complete:** The application-specific guide is the migration roadmap - invest time in making
   it comprehensive

**Quality of application-specific guide directly determines migration success.**

---

**Document Status:** Ready for application-specific analysis  
**Next Action:** Use this framework to analyze intygsadmin repository and create detailed migration
guide  
**Priority:** HIGH - Framework for entire migration planning

