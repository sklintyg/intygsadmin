# intygsadmin - React 16 to React 19 Migration Guide

**Document Version:** 1.1  
**Date:** January 15, 2026  
**Application:** intygsadmin  
**Migration Type:** React 16.10.2 → React 18.3.1 → React 19.2.x  
**Repository:** C:\Users\mhoernfeldt\intyg\intygsadmin

---

## Document Purpose

This is the **COMPLETE, DETAILED MIGRATION GUIDE** for migrating intygsadmin from React 16.10.2 to
React 19.2.x (via React 18.3.1). This document is the **heart of the migration** - it provides
step-by-step instructions, specific file changes, and detailed increments to ensure a successful
migration.

**This guide was generated through systematic analysis of the intygsadmin codebase on January 14,
2026, and updated with React 19 upgrade on January 15, 2026.**

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Current State Analysis](#2-current-state-analysis)
3. [Migration Strategy](#3-migration-strategy)
4. [Detailed Migration Increments](#4-detailed-migration-increments)
5. [Testing Strategy](#5-testing-strategy)
6. [Rollback Plan](#6-rollback-plan)
7. [Post-Migration Validation](#7-post-migration-validation)
8. [React 19 Upgrade](#8-react-19-upgrade)
9. [Vite Migration](#9-vite-migration)
10. [Appendices](#10-appendices)

---

## 1. Executive Summary

### 1.1 Application Overview

**intygsadmin** is a React 16.10.2 application for managing healthcare certificates in Sweden. It
uses:

- Redux for state management
- connected-react-router for routing integration
- recompose for HOC composition (MUST REMOVE)
- Enzyme for testing (MUST MIGRATE to RTL)
- styled-components v4 for styling
- node-sass (DEPRECATED)

**Total Migration Scope:**

- **97 component files** in `web/client/src/components/`
- **9 page files** in `web/client/src/pages/`
- **20 files using recompose** (must migrate to hooks)
- **6 files using lifecycle HOC** (must migrate to useEffect)
- **30+ files using connect() HOC** (must migrate to Redux hooks)
- **1 file with UNSAFE_componentWillMount** (App.js - CRITICAL)
- **0 class components** (all already functional ✅)
- **2 test files** (1 Enzyme test to migrate)

### 1.2 Critical Findings

🔴 **CRITICAL ISSUES:**

1. **UNSAFE_componentWillMount in App.js** - Deprecated, must migrate to useEffect
2. **20 files use recompose** - Library incompatible with React 18 patterns, must remove
3. **6 files use lifecycle() HOC** - Must convert to useEffect hooks
4. **30+ files use connect() HOC** - Must migrate to Redux hooks (useDispatch/useSelector) to align
   with intyg-frontend
5. **connected-react-router** - Not compatible with React Router 6, must remove
6. **Enzyme testing** - No React 18 adapter, must migrate to RTL
7. **node-sass** - Deprecated, must replace with sass (dart-sass)

🟡 **HIGH PRIORITY:**

- **react-router-dom 5.1.2** → Must upgrade to 6.22.0+ (breaking changes)
- **styled-components 4.4.0** → Update to 6.x for React 18
- **react-datepicker 2.9.6** → Update to 4.x for React 18
- **react-responsive-modal 4.0.1** → Update to 6.x for React 18

✅ **GOOD NEWS:**

- **NO class components found** (all components already functional)
- **NO findDOMNode() usage** found
- **NO string refs** found
- **NO legacy context API** found
- Redux store structure is clean and maintainable

### 1.3 Migration Timeline

**Estimated Effort:** 4-6 days for experienced React developer (React 18) + 0.5-1 day (React 19)

**Phase Breakdown:**

- **Phase 1:** Setup & Dependencies (1 day)
- **Phase 2:** Recompose & Deprecations (1-2 days)
- **Phase 3:** React Router 6 Migration (1-1.5 days)
- **Phase 4:** Testing Infrastructure (0.5 day)
- **Phase 5:** Test Addition (1-2 days)
- **Phase 6:** Cleanup & Validation (0.5 day)
- **Phase 7:** React 19 Upgrade (0.5-1 day)

---

## 2. Current State Analysis

### 2.1 Package Analysis

**Current Dependencies (from package.json):**

```json
{
  "react": "^16.10.2",
  →
  18.3.1
  "react-dom": "^16.10.2",
  →
  18.3.1
  "react-scripts": "3.2.0",
  →
  5.0.1
  "react-redux": "^6.0.1",
  →
  8.1.3
  "react-router-dom": "^5.1.2",
  →
  6.22.0
  "connected-react-router": "^6.4.0",
  →
  REMOVE
  "recompose": "^0.30.0",
  →
  REMOVE
  "redux": "^4.0.1",
  →
  Keep
  or
  →
  @reduxjs/toolkit
  2.x
  "redux-thunk": "^2.3.0",
  →
  Keep
  2.4.2
  "styled-components": "^4.4.0",
  →
  6.1.8
  "node-sass": "^4.11.0",
  →
  REMOVE,
  use
  sass
  1.70.0
  "react-datepicker": "^2.9.6",
  →
  4.25.0
  "react-responsive-modal": "^4.0.1",
  →
  6.4.2
  "react-js-pagination": "3.0.2",
  →
  Verify
  compatibility
  "bootstrap": "^4.3.1",
  →
  Keep
  or
  →
  5.x
  "reactstrap": "^8.0.0"
  →
  9.2.2
}
```

**Current Dev Dependencies:**

```json
{
  "enzyme": "^3.10.0",
  →
  REMOVE
  "enzyme-adapter-react-16": "^1.15.1",
  →
  REMOVE
  "sinon": "^7.5.0",
  →
  REMOVE
  "fetch-mock": "^7.5.1",
  →
  REMOVE
  "prettier": "^1.18.2"
  →
  3.2.4
}
```

**Dependencies to ADD:**

```json
{
  "@testing-library/react": "^13.4.0",
  "@testing-library/jest-dom": "^6.2.0",
  "@testing-library/user-event": "^14.5.2",
  "msw": "^2.7.0",
  "sass": "^1.70.0"
}
```

### 2.2 Component Architecture Analysis

**Total Components:** 97 files in `components/` + 9 in `pages/` = 106 files

**Component Type Breakdown:**

- **Functional Components:** 106 (100%) ✅
- **Class Components:** 0 ✅
- **Error Boundaries:** 0 (none found, may need to add)

**recompose Usage (20 files):**

**Files using `compose()` only (15 files):**

1. `components/privatePractitioner/PrivatePractitionerSearch.js`
2. `components/privatePractitioner/PrivatePractitionerSearchResult.dialog.js`
3. `components/users/UsersActionBar.js`
4. `components/users/Users.js`
5. `components/privatePractitioner/PrivatePractitionerExport.js`
6. `components/TestLinks/TestLinks.js`
7. `components/users/dialogs/RemoveUser.dialog.js`
8. `components/users/dialogs/CreateUser.dialog.js`
9. `components/ResendStatusCount/ResendStatusCount.js`
10. `components/header/HeaderContainer.js`
11. `components/iaMenu/MenuBar.js`
12. `components/intygInfo/IntygInfoHistory.js`
13. `components/intygInfo/IntygInfoDialog.js`
14. `components/intygInfo/IntygInfoSearch.js`
15. `components/loginOptions/LoginOptionsContainer.js`
16. `components/integratedUnits/IntegratedUnitsExport.js`
17. `components/integratedUnits/IntegratedUnitsSearch.js`

**Files using `lifecycle()` HOC (6 files - CRITICAL):**

1. **`src/App.js`** - ⚠️ Uses `UNSAFE_componentWillMount` (CRITICAL!)
2. `components/users/UsersList.js`
3. `components/sessionPoller/SessionPollerContainer.js`
4. `components/intygInfo/IntygInfoHistoryList.js`
5. `components/bannerList/BannerListContainer.js`
6. `components/dataExport/DataExportList.js`

### 2.3 Entry Point Analysis

**File:** `web/client/src/index.js`

**Current Code:**

```javascript
import ReactDOM from 'react-dom';
// ...
ReactDOM.render(
    <Provider store={store}>
      <App/>
    </Provider>,
    document.getElementById('root')
);
```

**Required Change:** Must migrate to `createRoot()`

### 2.4 App.js Detailed Analysis (CRITICAL FILE)

**File:** `src/App.js` (85 lines)

**Current Structure:**

- Functional component enhanced with recompose
- Uses `compose()` + `lifecycle()` + `connect()`
- **UNSAFE_componentWillMount** for app initialization
- `componentDidMount` for history listener setup
- `componentWillUnmount` for cleanup

**Lifecycle Code:**

```javascript
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
```

**Migration Requirements:**

1. Remove `recompose` imports
2. Convert `lifecycle()` HOC to `useEffect` hooks
3. Convert `connect()` to hooks (optional, can keep)
4. Handle history listener setup/cleanup with useEffect

### 2.5 Routing Analysis

**Current Setup:**

- `react-router-dom` 5.1.2
- `connected-react-router` 6.4.0
- Uses `<HashRouter>` wrapped in `<ConnectedRouter>`
- Uses `<Switch>` for route definitions
- Custom `SecuredRoute` and `UnsecuredRoute` components

**Routes Defined:**

- `/` - HomePage (UnsecuredRoute)
- `/loggedout/:code` - HomePage (UnsecuredRoute)
- `/banner` - BannerPage (SecuredRoute)
- `/integratedUnits` - IntegratedUnitsPage (SecuredRoute)
- `/privatePractitioner` - PrivatePractitionerPage (SecuredRoute)
- `/intygInfo` - IntygInfoPage (SecuredRoute)
- `/administratorer` - UsersPage (SecuredRoute)
- `/dataExport` - DataExportPage (SecuredRoute)
- `/resend` - ResendPage (SecuredRoute)
- `/exit/:errorCode/:logId?` - ErrorPage (UnsecuredRoute, error page)

### 2.6 Testing Analysis

**Current Test Files:**

1. `src/App.test.js` - Basic smoke test (ReactDOM.render)
2. `components/toggler/Toggler.spec.js` - Enzyme test (shallow + simulate)
3. `components/alert/Alert.spec.js` - Found, likely Enzyme
4. Redux action/reducer tests (`.spec.js` files in `store/`)

**Test Configuration:**

- `setupTests.js` - Configures Enzyme adapter
- Jest via react-scripts
- Coverage collection configured

**Migration Required:**

- Replace Enzyme with React Testing Library
- Migrate 2-3 Enzyme tests
- Add MSW for API mocking
- Add critical tests post-migration

### 2.7 Redux Store Analysis

**Store Structure:**

- `store/configureStore.js` - Store configuration
- `store/actions/` - Action creators (good structure ✅)
- `store/reducers/` - Reducers (good structure ✅)
- Uses `connected-react-router` for router integration (MUST REMOVE)
- Uses `redux-thunk` for async actions (KEEP)

**No Redux migration required** (can keep Redux 4.x or optionally migrate to RTK later)

---

## 3. Migration Strategy

### 3.1 Migration Principles

1. **Incremental:** Each increment leaves app buildable and functional
2. **Test After Each Increment:** Verify `npm run build` succeeds
3. **Commit Frequently:** Atomic commits for easy rollback
4. **Priority Order:** Critical issues first, optimizations last
5. **OBSERVE Flags:** Mark uncertainties for developer review

### 3.2 Phase Overview

**Phase 1: Foundation & Setup**

- Update core React dependencies
- Update build tool (react-scripts)
- Migrate entry point to createRoot()
- Verify app builds

**Phase 2: Remove Deprecations (recompose & lifecycle)**

- Migrate App.js (CRITICAL - has UNSAFE_componentWillMount)
- Migrate 5 other lifecycle() files
- Remove all compose() usage (15 files)
- Remove recompose dependency

**Phase 3: React Router 6 Migration**

- Update react-router-dom
- Remove connected-react-router
- Migrate route definitions
- Update SecuredRoute/UnsecuredRoute
- Update all navigation code

**Phase 4: Testing Infrastructure**

- Install React Testing Library + MSW
- Remove Enzyme
- Update setupTests.js
- Setup MSW handlers

**Phase 5: Add Tests**

- Migrate Toggler.spec.js to RTL
- Add App.js tests (critical)
- Add SessionPoller tests
- Add auth component tests
- Add modal/form tests

**Phase 6: Update Dependencies & Cleanup**

- Update styled-components
- Update UI libraries
- Replace node-sass with sass
- Remove unused dependencies
- Final verification

### 3.3 Risk Assessment

**HIGH RISK Components:**

1. **App.js** - Core initialization, routing, history
2. **SessionPoller** - Critical for session management
3. **SecuredRoute/UnsecuredRoute** - Authentication logic

**MEDIUM RISK Components:**

- UsersList, BannerListContainer, DataExportList (lifecycle usage)
- All dialog components (modal functionality)
- All form components (date pickers, inputs)

**LOW RISK Components:**

- Presentational components (no logic)
- Styled components
- Utility components

---

## 4. Detailed Migration Increments

### Phase 1: Foundation & Setup

#### Increment 1.1: Update Core Dependencies

**Objective:** Update React, ReactDOM, and react-scripts to React 18 compatible versions.

**Files to Modify:**

- `web/client/package.json`

**Steps:**

1. **Update package.json dependencies:**

```json
{
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-redux": "^8.1.3",
    "react-router-dom": "^6.22.0",
    "react-scripts": "5.0.1",
    "redux": "^4.2.1",
    "redux-thunk": "^2.4.2",
    "styled-components": "^6.1.8",
    "sass": "^1.70.0",
    "bootstrap": "^4.6.2",
    "reactstrap": "^9.2.2",
    "react-datepicker": "^4.25.0",
    "react-responsive-modal": "^6.4.2",
    "react-js-pagination": "3.0.3",
    "core-js": "^3.36.0",
    "flat": "^6.0.1",
    "lodash": "^4.17.21",
    "typeface-roboto": "0.0.75"
  }
}
```

2. **Remove deprecated dependencies:**

```json
{
  "dependencies": {
    // REMOVE these lines:
    // "connected-react-router": "^6.4.0",
    // "recompose": "^0.30.0",
    // "node-sass": "^4.11.0"
  }
}
```

3. **Update devDependencies:**

```json
{
  "devDependencies": {
    "@testing-library/react": "^13.4.0",
    "@testing-library/jest-dom": "^6.2.0",
    "@testing-library/user-event": "^14.5.2",
    "msw": "^2.7.0",
    "http-proxy-middleware": "^2.0.6",
    "prettier": "^3.2.4",
    "redux-mock-store": "^1.5.4",
    "uuid": "^3.3.3"
  }
}
```

4. **Remove from devDependencies:**

```json
{
  "devDependencies": {
    // REMOVE these lines:
    // "enzyme": "^3.10.0",
    // "enzyme-adapter-react-16": "^1.15.1",
    // "sinon": "^7.5.0",
    // "fetch-mock": "^7.5.1"
  }
}
```

5. **Install dependencies:**

```bash
cd web/client
npm install
```

6. **Verify:**

- Check for peer dependency warnings
- Ensure no critical errors
- App may not start yet (expected - more changes needed)

**Expected Result:** Dependencies updated, npm install succeeds

**OBSERVE:** If peer dependency errors occur, note them for resolution

---

#### Increment 1.2: Migrate Entry Point to createRoot()

**Objective:** Update index.js to use React 18's new root API.

**Files to Modify:**

- `web/client/src/index.js`

**Current Code:**

```javascript
import ReactDOM from 'react-dom';
// ...
ReactDOM.render(
    <Provider store={store}>
      <App/>
    </Provider>,
    document.getElementById('root')
);
```

**New Code:**

```javascript
import {createRoot} from 'react-dom/client';
// ...
const root = createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
      <App/>
    </Provider>
);
```

**Complete File After Changes:**

```javascript
import './polyfills'
import 'typeface-roboto';
import React from 'react';
import {createRoot} from 'react-dom/client';
import {Provider} from 'react-redux'
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import configureStore from './store/configureStore';

import './components/styles/bootstrap-overrides.scss';

const store = configureStore();
const root = createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
      <App/>
    </Provider>
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
```

**Verify:**

```bash
npm run build
```

**Expected Result:** Build succeeds (app won't start yet due to recompose/router issues)

**OBSERVE:** If build fails, check error messages for guidance

---

#### Increment 1.3: Enable StrictMode (Optional but Recommended)

**Objective:** Enable React StrictMode to catch potential issues during development.

**Files to Modify:**

- `web/client/src/index.js`

**Updated Code:**

```javascript
root.render(
    <React.StrictMode>
      <Provider store={store}>
        <App/>
      </Provider>
    </React.StrictMode>
);
```

**Note:** StrictMode will double-invoke effects in development to help find bugs. This is expected
behavior.

**Verify:** App should still build (may have console warnings - expected)

---

### Phase 2: Remove Deprecations (recompose & lifecycle)

This is the CRITICAL phase. We'll migrate all recompose usage to hooks.

#### Increment 2.1: Migrate App.js (HIGHEST PRIORITY)

**Objective:** Remove UNSAFE_componentWillMount and migrate App.js to hooks.

**File:** `web/client/src/App.js`

**Current Structure:**

```javascript
import {compose, lifecycle} from 'recompose'
import {connect} from 'react-redux'
// ...

const App = () => {
  // JSX
}

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
```

**New Structure with Hooks:**

```javascript
import React, {Fragment, useEffect} from 'react'
import {HashRouter, Routes, Route, Navigate} from 'react-router-dom'
import {useDispatch} from 'react-redux'
import HomePage from './pages/IndexPage'
import BannerPage from './pages/BannerPage'
import IntegratedUnitsPage from './pages/IntegratedUnitsPage'
import PrivatePractitionerPage from './pages/PrivatePractitionerPage'
import Header from './components/header'
import {getUser} from './store/actions/user'
import SecuredRoute from './components/auth/SecuredRoute'
import UnsecuredRoute from './components/auth/UnsecuredRoute'
import {history} from './store/configureStore'
import {closeAllModals} from './store/actions/modal'
import ErrorPage from './pages/ErrorPage'
import ErrorModal from './components/errorModal'
import TestLinks from './components/TestLinks/TestLinks'
import SessionPoller from './components/sessionPoller'
import {fetchAppConfig} from './store/actions/appConfig'
import IntygInfoPage from "./pages/IntygInfoPage";
import UsersPage from "./pages/UsersPage";
import DataExportPage from "./pages/DataExportPage";
import ResendPage from "./pages/ResendPage";

const App = () => {
  const dispatch = useDispatch();

  // Replace UNSAFE_componentWillMount
  useEffect(() => {
    dispatch(fetchAppConfig());
    dispatch(getUser());
  }, [dispatch]);

  // Replace componentDidMount + componentWillUnmount
  useEffect(() => {
    const unlisten = history.listen(() => {
      dispatch(closeAllModals());
    });
    return () => unlisten();
  }, [dispatch]);

  return (
      <HashRouter>
        <Fragment>
          <SessionPoller/>
          {process.env.NODE_ENV !== 'production' && <TestLinks/>}
          <Header/>
          <ErrorModal/>
          {/* Routes will be updated in React Router 6 migration */}
          <Routes>
            <Route path="/" element={<UnsecuredRoute component={HomePage} exact/>}/>
            <Route path="/loggedout/:code" element={<UnsecuredRoute component={HomePage}/>}/>
            <Route path="/banner" element={<SecuredRoute component={BannerPage}/>}/>
            <Route path="/integratedUnits"
                   element={<SecuredRoute component={IntegratedUnitsPage}/>}/>
            <Route path="/privatePractitioner"
                   element={<SecuredRoute component={PrivatePractitionerPage}/>}/>
            <Route path="/intygInfo" element={<SecuredRoute component={IntygInfoPage}/>}/>
            <Route path="/administratorer" element={<SecuredRoute component={UsersPage}/>}/>
            <Route path="/dataExport" element={<SecuredRoute component={DataExportPage}/>}/>
            <Route path="/resend" element={<SecuredRoute component={ResendPage}/>}/>
            <Route path="/exit/:errorCode/:logId?"
                   element={<UnsecuredRoute component={ErrorPage} isErrorPage/>}/>
          </Routes>
        </Fragment>
      </HashRouter>
  );
};

export default App;
```

**OBSERVE:**

- This removes `ConnectedRouter` - we'll handle router state differently
- Routes now use React Router 6 syntax (Switch → Routes)
- This may temporarily break the app until configureStore.js is updated

**Verify:**

```bash
npm run build
```

**Expected Result:** Should build (may have warnings about history usage)

---

#### Increment 2.2: Update configureStore.js

**Objective:** Remove connected-react-router integration from store.

**File:** `web/client/src/store/configureStore.js`

**Current** (likely has):

```javascript
import {connectRouter, routerMiddleware} from 'connected-react-router'
import {createBrowserHistory} from 'history'

export const history = createBrowserHistory()

const store = createStore(
    connectRouter(history)(rootReducer),
    applyMiddleware(routerMiddleware(history), thunk)
)
```

**New:**

```javascript
import {createStore, applyMiddleware} from 'redux'
import thunk from 'redux-thunk'
import {createBrowserHistory} from 'history'
import rootReducer from './reducers'

export const history = createBrowserHistory()

const configureStore = () => {
  const store = createStore(
      rootReducer,
      applyMiddleware(thunk)
  )
  return store
}

export default configureStore
```

**OBSERVE:** Check actual configureStore.js structure and adjust accordingly

**Verify:** App should build

---

#### Increment 2.3: Update rootReducer

**Objective:** Remove router reducer from root reducer.

**File:** `web/client/src/store/reducers/index.js`

**Remove:**

```javascript
import {connectRouter} from 'connected-react-router'

const rootReducer = (history) => combineReducers({
  router: connectRouter(history), // REMOVE THIS
  // ... other reducers
})
```

**New:**

```javascript
import {combineReducers} from 'redux'
// ... other reducer imports

const rootReducer = combineReducers({
  // ... other reducers (no router reducer)
})

export default rootReducer
```

**Verify:** App should build and potentially start now

---

#### Increment 2.4: Migrate SessionPollerContainer.js

**File:** `web/client/src/components/sessionPoller/SessionPollerContainer.js`

**Current** (uses lifecycle HOC with componentDidUpdate):

```javascript
import {connect} from 'react-redux'
import {compose, lifecycle} from 'recompose'
import {requestPollUpdate, startPoll, stopPoll} from '../../store/actions/sessionPoll'

const SessionPollerContainer = () => {
  return null // Non-visual component
}

const lifeCycleValues = {
  componentDidMount() {
    if (this.props.isAuthenticated) {
      this.props.startPolling()
    }
  },

  componentDidUpdate() {
    if (this.props.isAuthenticated) {
      this.props.requestPollUpdateNow()
      this.props.startPolling()
    }
  },

  componentWillUnmount() {
    this.props.stopPolling()
  },
}

const mapDispatchToProps = (dispatch) => ({
  requestPollUpdateNow: () => dispatch(requestPollUpdate()),
  startPolling: () => dispatch(startPoll()),
  stopPolling: () => dispatch(stopPoll()),
})

const mapStateToProps = (state) => ({
  isAuthenticated: state.user.isAuthenticated,
})

export default compose(
    connect(mapStateToProps, mapDispatchToProps),
    lifecycle(lifeCycleValues)
)(SessionPollerContainer)
```

**New** (with hooks):

```javascript
import {useEffect} from 'react'
import {connect} from 'react-redux'
import {requestPollUpdate, startPoll, stopPoll} from '../../store/actions/sessionPoll'

const SessionPollerContainer = ({
  isAuthenticated,
  startPolling,
  stopPolling,
  requestPollUpdateNow
}) => {
  // componentDidMount + componentWillUnmount
  useEffect(() => {
    if (isAuthenticated) {
      startPolling()
    }
    return () => stopPolling()
  }, [isAuthenticated, startPolling, stopPolling])

  // componentDidUpdate (runs when isAuthenticated changes)
  useEffect(() => {
    if (isAuthenticated) {
      requestPollUpdateNow()
      startPolling()
    }
  }, [isAuthenticated, requestPollUpdateNow, startPolling])

  return null // Non-visual component
}

const mapDispatchToProps = (dispatch) => ({
  requestPollUpdateNow: () => dispatch(requestPollUpdate()),
  startPolling: () => dispatch(startPoll()),
  stopPolling: () => dispatch(stopPoll()),
})

const mapStateToProps = (state) => ({
  isAuthenticated: state.user.isAuthenticated,
})

export default connect(mapStateToProps, mapDispatchToProps)(SessionPollerContainer)
```

**Key Changes:**

1. Removed `compose` and `lifecycle` imports
2. Converted lifecycle methods to two useEffect hooks
3. First useEffect handles mount/unmount with cleanup
4. Second useEffect handles updates when `isAuthenticated` changes
5. Removed `lifecycle(lifeCycleValues)` from export

**OBSERVE:** Verify polling behavior still works correctly after migration

**Verify:** Build and test session polling still works

---

#### Increment 2.5: Migrate UsersList.js

**File:** `web/client/src/components/users/UsersList.js`

**Pattern:** Same as SessionPoller - replace lifecycle() with useEffect

**Steps:**

1. Remove `import {compose, lifecycle} from 'recompose'`
2. Add `import { useEffect } from 'react'`
3. Convert lifecycle values to useEffect
4. Remove `lifecycle(lifeCycleValues)` from compose chain
5. If compose only wraps connect, replace with just connect

**Verify:** Build succeeds

---

#### Increment 2.6: Migrate BannerListContainer.js

**File:** `web/client/src/components/bannerList/BannerListContainer.js`

**Same pattern as UsersList**

**Verify:** Build succeeds

---

#### Increment 2.7: Migrate IntygInfoHistoryList.js

**File:** `web/client/src/components/intygInfo/IntygInfoHistoryList.js`

**Same pattern**

**Verify:** Build succeeds

---

#### Increment 2.8: Migrate DataExportList.js

**File:** `web/client/src/components/dataExport/DataExportList.js`

**Same pattern**

**Verify:** Build succeeds

---

#### Increment 2.9: Remove compose() from Remaining Files (15 files)

**Files (compose only, no lifecycle):**

For these files, if `compose()` only wraps `connect()`, simply remove compose:

**Before:**

```javascript
import {compose} from 'recompose'
import {connect} from 'react-redux'

export default compose(
    connect(mapStateToProps, actions)
)(Component)
```

**After:**

```javascript
import {connect} from 'react-redux'

export default connect(mapStateToProps, actions)(Component)
```

**Files to update:**

1. `components/privatePractitioner/PrivatePractitionerSearch.js`
2. `components/privatePractitioner/PrivatePractitionerSearchResult.dialog.js`
3. `components/users/UsersActionBar.js`
4. `components/users/Users.js`
5. `components/privatePractitioner/PrivatePractitionerExport.js`
6. `components/TestLinks/TestLinks.js`
7. `components/users/dialogs/RemoveUser.dialog.js`
8. `components/users/dialogs/CreateUser.dialog.js`
9. `components/ResendStatusCount/ResendStatusCount.js`
10. `components/header/HeaderContainer.js`
11. `components/iaMenu/MenuBar.js`
12. `components/intygInfo/IntygInfoHistory.js`
13. `components/intygInfo/IntygInfoDialog.js`
14. `components/intygInfo/IntygInfoSearch.js`
15. `components/loginOptions/LoginOptionsContainer.js`
16. `components/integratedUnits/IntegratedUnitsExport.js`
17. `components/integratedUnits/IntegratedUnitsSearch.js`

**Verify After Each File:** Build succeeds

---

#### Increment 2.10: Remove recompose from package.json

**Objective:** Remove recompose dependency after all usage is removed.

**File:** `web/client/package.json`

**Action:**

- Verify no files import from 'recompose':
  ```bash
  grep -r "from 'recompose'" web/client/src/
  ```
- If no results, remove dependency

**Verify:**

```bash
npm uninstall recompose
npm run build
```

**Expected Result:** Build succeeds, app should start and run

---

### Phase 2B: Optional Enhancements (Recommended Patterns from intyg-frontend)

**Status:** OPTIONAL - These increments add modern patterns inspired by intyg-frontend but are not
required for React 18 migration.

**Reference:** See `.github/migration/missing-patterns-from-inspiration.md` for detailed analysis.

---

#### Increment 2B.1: Add Typed Redux Hooks (Optional)

**Objective:** Create wrapper hooks for type-safe Redux usage (prepares for future TypeScript
migration).

**File:** `web/client/src/store/hooks.js` (NEW)

**Create:**

```javascript
import {useDispatch, useSelector} from 'react-redux';

// Typed dispatch hook
export const useAppDispatch = () => useDispatch();

// Typed selector hook
export const useAppSelector = useSelector;
```

**Usage Pattern:**

```javascript
// Before (in components)
import {useDispatch, useSelector} from 'react-redux';

// After
import {useAppDispatch, useAppSelector} from '../../store/hooks';
```

**Benefits:**

- Consistent hook usage across application
- Ready for TypeScript migration (just add types)
- Prevents accidental use of untyped hooks
- Better IDE autocomplete

**Migration Strategy:**

- Create the hooks file
- Gradually migrate components during other updates
- Use ESLint rule to enforce (optional, TypeScript only)

**Priority:** MEDIUM - Good practice but not urgent

---

#### Increment 2B.2: Setup Mock Service Worker (MSW)

**Objective:** Add MSW for API mocking in development and testing.

**Install:**

```bash
npm install msw@^2.7.0 --save-dev
```

**Create:** `web/client/src/mocks/handlers.js`

```javascript
import {rest} from 'msw';

export const handlers = [
  // User API
  rest.get('/api/user', (req, res, ctx) => {
    return res(
        ctx.status(200),
        ctx.json({
          hsaId: 'TEST123',
          name: 'Test User',
          role: 'ADMIN',
          isAuthenticated: true,
        })
    );
  }),

  // App config API
  rest.get('/api/config', (req, res, ctx) => {
    return res(
        ctx.status(200),
        ctx.json({
          version: '1.0.0-TEST',
          loginUrl: '/fake-login',
        })
    );
  }),

  // Add more handlers as needed
];
```

**Create:** `web/client/src/mocks/browser.js`

```javascript
import {setupWorker} from 'msw/browser';
import {handlers} from './handlers';

export const worker = setupWorker(...handlers);
```

**Create:** `web/client/src/mocks/server.js`

```javascript
import {setupServer} from 'msw/node';
import {handlers} from './handlers';

export const server = setupServer(...handlers);
```

**Update:** `web/client/src/index.js`

```javascript
// Add at top of file, before rendering
if (process.env.NODE_ENV === 'development' && process.env.REACT_APP_USE_MOCKS === 'true') {
  const {worker} = await import('./mocks/browser');
  worker.start({
    onUnhandledRequest: 'bypass', // Or 'warn' to see unhandled requests
  });
}

// ...existing render code...
```

**Update:** `web/client/src/setupTests.js`

```javascript
import '@testing-library/jest-dom';
import {server} from './mocks/server';

// Establish API mocking before all tests
beforeAll(() => server.listen({onUnhandledRequest: 'error'}));

// Reset handlers after each test (important for test isolation)
afterEach(() => server.resetHandlers());

// Clean up after tests are finished
afterAll(() => server.close());
```

**Add to `.env.development`** (create if doesn't exist):

```bash
REACT_APP_USE_MOCKS=true
```

**Benefits:**

- **No Backend Required:** Develop frontend without backend running
- **Consistent Mocks:** Same mocks for development and testing
- **Network-Level Mocking:** More realistic than function mocking
- **Easy Per-Test Overrides:** Can override specific endpoints per test

**Usage in Tests:**

```javascript
import {render, screen} from '@testing-library/react';
import {server} from './mocks/server';
import {rest} from 'msw';

test('handles error from API', async () => {
  // Override handler for this test
  server.use(
      rest.get('/api/user', (req, res, ctx) => {
        return res(ctx.status(500), ctx.json({error: 'Server error'}));
      })
  );

  render(<YourComponent/>);

  // Test error handling...
});
```

**Priority:** HIGH - Critical for modern testing practices

---

#### Increment 2B.3: Add Error Boundaries to Routes

**Objective:** Implement React error boundaries for better error handling.

**Create:** `web/client/src/components/ErrorBoundary/ErrorBoundary.js`

```javascript
import React from 'react';
import {useRouteError} from 'react-router-dom';
import styled from 'styled-components';

const ErrorContainer = styled.div`
  padding: 40px;
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
`;

const ErrorTitle = styled.h1`
  color: #d32f2f;
  font-size: 24px;
  margin-bottom: 16px;
`;

const ErrorMessage = styled.p`
  color: #666;
  font-size: 16px;
  margin-bottom: 24px;
`;

const ErrorDetails = styled.pre`
  background: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  text-align: left;
  overflow-x: auto;
  font-size: 14px;
`;

export function ErrorBoundary() {
  const error = useRouteError();

  return (
      <ErrorContainer>
        <ErrorTitle>Något gick fel</ErrorTitle>
        <ErrorMessage>
          Ett oväntat fel har inträffat. Försök ladda om sidan.
        </ErrorMessage>
        {process.env.NODE_ENV === 'development' && error && (
            <ErrorDetails>
              {error.message || 'Unknown error'}
              {error.stack && `\n\n${error.stack}`}
            </ErrorDetails>
        )}
      </ErrorContainer>
  );
}

export default ErrorBoundary;
```

**Update:** `web/client/src/App.js` (React Router 6 routes)

```javascript
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary';

// In your Routes
<Routes>
  <Route
      path="/"
      element={<UnsecuredRoute component={HomePage}/>}
      errorElement={<ErrorBoundary/>}
  />
  <Route
      path="/banner"
      element={<SecuredRoute component={BannerPage}/>}
      errorElement={<ErrorBoundary/>}
  />
  {/* Add errorElement to all routes */}
</Routes>
```

**Alternative:** Global error boundary (catch all)

```javascript
import {createBrowserRouter, RouterProvider} from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: '/',
    errorElement: <ErrorBoundary/>,
    children: [
      // All your routes here
    ],
  },
]);

function App() {
  return <RouterProvider router={router}/>;
}
```

**Benefits:**

- **Graceful Degradation:** App doesn't crash completely on errors
- **Better UX:** Users see helpful error message instead of blank screen
- **Development Debugging:** Stack traces in development mode
- **Per-Route Errors:** Can have different error handling per route section

**Priority:** MEDIUM - Good practice for production apps

---

#### Increment 2B.4: Add Custom Middleware Examples (SKIP - Reference Only)

**⚠️ SKIP THIS INCREMENT** - Custom middleware is NOT needed for React 18 migration.

**Objective:** This increment provides reference patterns only. Do NOT create these files during
migration.

**Rationale:**

- Custom middleware should be added only when specific business requirements emerge
- Template middleware files add no value and reduce code quality
- The `configureApplicationStore` pattern already supports adding middleware when needed

**If you need custom middleware in the future:**

**Pattern 1: Session Middleware (Example)**

```javascript
// Example ONLY - do not create unless you have specific session management needs
export const sessionMiddleware = (store) => (next) => (action) => {
  const result = next(action);

  // Your session logic here
  const state = store.getState();
  const timeSinceLastPing = Date.now() - (state.session?.lastPing || 0);

  if (timeSinceLastPing > 60000) {
    store.dispatch({type: 'session/ping'});
  }

  return result;
};
```

**Pattern 2: Error Middleware (Example)**

```javascript
// Example ONLY - do not create unless you have specific error handling needs
export const errorMiddleware = (store) => (next) => (action) => {
  const result = next(action);

  // Your error handling logic here
  if (action.type?.endsWith('/rejected')) {
    const error = action.error || action.payload;
    store.dispatch({
      type: 'notifications/showError',
      payload: {
        message: error.message || 'Ett fel uppstod',
        code: error.code,
      },
    });
  }

  return result;
};
```

**Usage in configureStore.js (when you create real middleware):**

```javascript
import {sessionMiddleware} from './middleware/sessionMiddleware';
import {errorMiddleware} from './middleware/errorMiddleware';

export const store = configureApplicationStore([
  sessionMiddleware,
  errorMiddleware,
]);
```

**Benefits:**

- **Cross-Cutting Concerns:** Handle session/errors globally
- **Centralized Logic:** Don't repeat in every action
- **Testable:** Middleware can be tested in isolation

**Priority:** LOW - Add when needed, not required for migration

---

### Phase 2B Summary

**What We Added:**

- ✅ Typed Redux hooks pattern (optional, JavaScript-safe)
- ✅ MSW setup for API mocking (highly recommended)
- ✅ Error boundaries for better error handling
- ✅ Custom middleware examples for reference

**What To Skip:**

- ❌ Redux navigation state management (over-engineered)
- ⏳ RTK Query (future enhancement, Phase 7)
- ⏳ Advanced environment config (mostly already handled)

**Priority Assessment:**

- **HIGH:** MSW setup (Increment 2B.2)
- **MEDIUM:** Error boundaries (Increment 2B.3), Typed hooks (2B.1)
- **LOW:** Custom middleware (2B.4) - reference only

**Continue to Phase 3?** YES - These are optional enhancements. Core React 18 migration is complete
after Phase 2.

---

### Phase 3: React Router 6 Migration

#### Increment 3.1: Update SecuredRoute Component

**File:** `web/client/src/components/auth/SecuredRoute.js`

**Current** (React Router 5):

```javascript
import {Route, Redirect} from 'react-router-dom'

const SecuredRoute = ({component: Component, ...rest}) => (
    <Route
        {...rest}
        render={(props) =>
            isAuthenticated ? <Component {...props} /> : <Redirect to="/"/>
        }
    />
)
```

**New** (React Router 6):

```javascript
import {Navigate} from 'react-router-dom'

const SecuredRoute = ({component: Component, ...props}) => {
  const isAuthenticated = useSelector(selectIsAuthenticated) // Adjust selector

  if (!isAuthenticated) {
    return <Navigate to="/" replace/>
  }

  return <Component {...props} />
}
```

**OBSERVE:** Adjust based on actual authentication logic in file

**Verify:** Build succeeds

---

#### Increment 3.2: Update UnsecuredRoute Component

**File:** `web/client/src/components/auth/UnsecuredRoute.js`

**Similar pattern to SecuredRoute**

**Verify:** Build succeeds

---

#### Increment 3.3: Update All Route Definitions

Already partially done in App.js migration, but verify all routes use correct syntax:

**React Router 6 Syntax:**

```javascript
<Routes>
  <Route path="/" element={<ComponentWrapper/>}/>
  <Route path="/users/:id" element={<ComponentWrapper/>}/>
</Routes>
```

**Verify:** All routes render correctly

---

#### Increment 3.4: Update Navigation Code (if any)

**Search for:**

- `useHistory()` → `useNavigate()`
- `history.push()` → `navigate()`
- `history.replace()` → `navigate(path, { replace: true })`

**Command:**

```bash
grep -r "useHistory\|history.push\|history.replace" web/client/src/
```

**Update each occurrence**

**Verify:** Navigation works correctly

---

### Phase 4: Testing Infrastructure

#### Increment 4.1: Update setupTests.js

**File:** `web/client/src/setupTests.js`

**Current:**

```javascript
import {configure} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

configure({adapter: new Adapter()});
```

**New:**

```javascript
import '@testing-library/jest-dom';
```

**Verify:**

```bash
npm test
```

**Expected:** Existing tests will fail (expected - they use Enzyme)

---

#### Increment 4.2: Setup MSW (Optional for now)

**Create:** `web/client/src/mocks/handlers.js`

```javascript
import {rest} from 'msw'

export const handlers = [
  // Add API mock handlers as needed
  rest.get('/api/user', (req, res, ctx) => {
    return res(ctx.json({ /* mock data */}))
  }),
]
```

**Create:** `web/client/src/mocks/server.js`

```javascript
import {setupServer} from 'msw/node'
import {handlers} from './handlers'

export const server = setupServer(...handlers)
```

**Update:** `setupTests.js`

```javascript
import '@testing-library/jest-dom';
import {server} from './mocks/server'

beforeAll(() => server.listen({onUnhandledRequest: 'warn'}))
afterEach(() => server.resetHandlers())
afterAll(() => server.close())
```

**Verify:** Tests can run (may still fail on Enzyme tests)

---

### Phase 5: Add Tests

#### Increment 5.1: Migrate Toggler.spec.js to RTL

**File:** `web/client/src/components/toggler/Toggler.spec.js`

**Current** (Enzyme):

```javascript
import {shallow} from 'enzyme';
import sinon from 'sinon';

it('renders a CollapseIcon when expanded = false', () => {
  const wrapper = shallow(<Toggler expanded={false}/>).shallow();
  expect(wrapper.find(CollapseIcon)).toHaveLength(1);
});
```

**New** (RTL):

```javascript
import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Toggler from './Toggler';
import {CollapseIcon, ExpandIcon} from "../styles/iaSvgIcons";

describe('<Toggler />', () => {
  it('renders a CollapseIcon when expanded = false', () => {
    render(<Toggler expanded={false}/>);
    // Query by test id or role, adjust based on actual implementation
    expect(screen.getByRole('button')).toBeInTheDocument();
  });

  it('renders an ExpandIcon when expanded = true', () => {
    render(<Toggler expanded={true}/>);
    expect(screen.getByRole('button')).toBeInTheDocument();
  });

  it('calls handleToggle when clicked', async () => {
    const handleToggle = jest.fn();
    render(<Toggler handleToggle={handleToggle}/>);
    await userEvent.click(screen.getByRole('button'));
    expect(handleToggle).toHaveBeenCalledTimes(1);
  });
});
```

**Verify:**

```bash
npm test Toggler
```

**Expected:** Test passes

---

#### Increment 5.2: Update App.test.js

**File:** `web/client/src/App.test.js`

**Current:**

```javascript
import ReactDOM from 'react-dom';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<Provider store={store}><App/></Provider>, div);
  ReactDOM.unmountComponentAtNode(div);
});
```

**New:**

```javascript
import {render} from '@testing-library/react';
import {Provider} from 'react-redux';
import App from './App';
import configureStore from './store/configureStore';

const store = configureStore();

it('renders without crashing', () => {
  render(
      <Provider store={store}>
        <App/>
      </Provider>
  );
});
```

**Verify:** Test passes

---

#### Increment 5.3: Add App.js Comprehensive Tests

**Create:** `web/client/src/App.test.js` (expand existing)

**Add tests for:**

1. App initializes (fetches config and user)
2. History listener sets up correctly
3. Cleanup happens on unmount
4. Routes render correctly

**Example:**

```javascript
describe('App', () => {
  let store;

  beforeEach(() => {
    store = configureStore();
  });

  it('renders without crashing', () => {
    render(
        <Provider store={store}>
          <App/>
        </Provider>
    );
  });

  it('fetches app config and user on mount', () => {
    const fetchAppConfig = jest.spyOn(store, 'dispatch');
    render(
        <Provider store={store}>
          <App/>
        </Provider>
    );
    // Verify dispatch was called with correct actions
    expect(fetchAppConfig).toHaveBeenCalled();
  });

  // Add more tests for routes, navigation, etc.
});
```

**Verify:** Tests pass

---

#### Increment 5.4: Add Critical Component Tests

**Priority components to test:**

1. SessionPoller - session management
2. SecuredRoute - authentication
3. UnsecuredRoute - routing
4. Modal dialogs
5. Form components

**Create test files as needed:**

- `SessionPoller.test.js`
- `SecuredRoute.test.js`
- `UnsecuredRoute.test.js`

**Verify:** Coverage increases, tests pass

---

### Phase 6: Update Dependencies & Cleanup

#### Increment 6.1: Replace node-sass with sass

**Already done in package.json update (Increment 1.1)**, now verify SCSS compilation works.

**SCSS Files in Project:**

- `web/client/src/components/styles/bootstrap-overrides.scss` (main file)
- `web/client/src/components/styles/iaCommon.scss`
- `web/client/src/components/styles/iaColors.scss`
- `web/client/src/components/styles/datepicker-override.scss`

**Key File:** `bootstrap-overrides.scss` uses Bootstrap imports:

```scss
@import '~bootstrap/scss/_functions';
@import '~bootstrap/scss/_variables';
// etc.
```

**Verify:**

1. All `.scss` files still compile correctly
2. Bootstrap imports work with sass (dart-sass)
3. No compilation errors

**Test:**

```bash
npm run build
```

**If Issues:**

- The `~` prefix for node_modules should work with sass (dart-sass)
- If errors occur, check sass version compatibility with Bootstrap 4.6.2
- May need to update sass loader configuration (react-scripts 5.x should handle this)

**Expected:** All SCSS compiles successfully, styles work in app

---

#### Increment 6.2: Final Cleanup

**Remove:**

- Any remaining recompose references
- Any commented-out old code
- Unused imports

**Update:**

- README.md with React 18 information
- Any documentation

**Verify:**

```bash
npm run build
npm test
```

**Expected:** All pass, no warnings

---

## 5. Testing Strategy

### 5.1 Test Coverage Goals

**Baseline** (from test coverage analysis): <5% component coverage

**Target:**

- **Post-Migration Minimum:** 60% component coverage
- **Critical Components:** 80%+ coverage
- **Redux:** Maintain ~70% (already achieved)

### 5.2 Test Priorities

**CRITICAL (Add First):**

1. App.js - initialization, routing, lifecycle
2. SessionPoller - session management
3. SecuredRoute/UnsecuredRoute - authentication

**HIGH (Add During):**

4. Modal components
5. Form components (date pickers, inputs)
6. List components with pagination

**MEDIUM (Fill Gaps):**

7. Page smoke tests
8. Navigation components
9. Utility components

### 5.3 Testing Patterns

**Use React Testing Library Best Practices:**

- Query by role (most accessible)
- Query by label text (forms)
- Query by text (content)
- Avoid test IDs unless necessary
- Test user behavior, not implementation

**Example Pattern:**

```javascript
import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';

test('user can submit form', async () => {
  render(<MyForm/>);

  await userEvent.type(screen.getByLabelText(/name/i), 'John');
  await userEvent.click(screen.getByRole('button', {name: /submit/i}));

  await waitFor(() => {
    expect(screen.getByText(/success/i)).toBeInTheDocument();
  });
});
```

---

## 6. Rollback Plan

### 6.1 Git Strategy

**Branch Structure:**

- Main branch: `main` (stable)
- Migration branch: `feature/react-18-migration`
- Tag stable points: `v1.0.0-react16`, `v1.0.0-react18-phase1`, etc.

**Commit Strategy:**

- Commit after each increment
- Use descriptive messages: `feat(migration): migrate App.js to hooks`
- Tag after each phase completion

### 6.2 Rollback Triggers

**Immediate Rollback If:**

- Critical functionality broken (auth, session, data access)
- Performance degradation >20%
- Build fails and cannot be fixed quickly
- Security vulnerability introduced

### 6.3 Rollback Procedure

```bash
# Immediate rollback to last stable tag
git checkout v1.0.0-react16

# Or revert specific commits
git revert <commit-hash>

# Deploy previous version
npm install
npm run build
# Deploy build/
```

---

## 7. Post-Migration Validation

### 7.1 Functional Validation Checklist

**Critical Flows:**

- [ ] Application starts without errors
- [ ] User login/authentication works
- [ ] Session management works
- [ ] All routes accessible
- [ ] Banner management works
- [ ] User management (administratörer) works
- [ ] Data export works
- [ ] Integrated units search works
- [ ] Private practitioner search works
- [ ] Intyg info display works
- [ ] Resend functionality works
- [ ] Forms submit correctly
- [ ] Modals open and close correctly
- [ ] Date pickers work
- [ ] Pagination works
- [ ] Sorting works
- [ ] Filtering works
- [ ] Error handling works
- [ ] Logout works

### 7.2 Browser Testing

**Test on:**

- [ ] Chrome (latest)
- [ ] Firefox (latest)
- [ ] Safari (latest)
- [ ] Edge (latest)
- [ ] IE 11 (if required per browserslist)

### 7.3 Performance Validation

**Measure:**

- Bundle size (compare before/after)
- Initial load time
- Time to interactive
- React DevTools Profiler (no unnecessary re-renders)

**Expected:**

- Bundle size similar or smaller (due to removing recompose)
- Performance same or better (automatic batching)

### 7.4 Test Coverage Validation

**Run:**

```bash
npm test -- --coverage
```

**Verify:**

- All tests pass
- Coverage >= 60% components
- Critical components >= 80%

### 7.5 Code Quality Validation

**Check:**

- [ ] No console errors
- [ ] No React warnings
- [ ] ESLint passes
- [ ] No UNSAFE_ methods
- [ ] No recompose imports
- [ ] No Enzyme imports
- [ ] No deprecated patterns

**Run:**

```bash
npm run build
# Check console for warnings
```

**Repository-Wide Verification Commands:**

```bash
# Verify NO recompose usage
grep -r "from 'recompose'" web/client/src/
# Expected: 0 results

# Verify NO UNSAFE_ methods
grep -r "UNSAFE_component" web/client/src/
# Expected: 0 results

# Verify NO Enzyme usage
grep -r "from 'enzyme'" web/client/src/
# Expected: 0 results

# Verify NO connected-react-router
grep -r "connected-react-router" web/client/src/
# Expected: 0 results

# Verify React Router 5 patterns removed
grep -r "react-router-dom.*Redirect" web/client/src/
# Expected: 0 results (should use Navigate)

grep -r "react-router-dom.*Switch" web/client/src/
# Expected: 0 results (should use Routes)
```

**All scans must return 0 results for migration to be considered complete.**

---

## 8. React 19 Upgrade

**Status:** Ready to begin after React 18 migration is complete  
**Prerequisites:** All Phase 1-6 increments completed and validated  
**Target:** React 19.2.x  
**Risk Level:** Medium (React 19 has breaking changes)

### 8.1 Overview

React 19 is a major release that includes:

- **Breaking Changes:** Removed deprecated APIs (e.g., `ReactDOM.render`, legacy context)
- **New Features:** React Compiler, improved hydration, new hooks
- **Performance Improvements:** Automatic batching enhancements, faster reconciliation
- **API Changes:** Changes to refs, context, and Suspense behavior

**Migration Strategy:**

- Incremental upgrade with validation after each step
- Test thoroughly after each dependency update
- Monitor console for warnings about deprecated patterns
- Update ecosystem packages to React 19 compatible versions

### 8.2 Phase 7 Increments

#### Increment 7.1: Update React Core Dependencies

**Goal:** Update React and ReactDOM to 19.2.x

**Files to Modify:**

- `web/client/package.json`

**Changes:**

```json
{
  "dependencies": {
    "react": "^19.2.0",
    "react-dom": "^19.2.0"
  }
}
```

**Steps:**

1. Update package.json with React 19.2.x versions
2. Run `npm install` or `pnpm install`
3. Check for peer dependency warnings
4. Verify installation completes successfully

**Validation:**

```bash
npm list react react-dom
# Should show react@19.2.x and react-dom@19.2.x

npm run build
# Should compile without errors
```

**OBSERVE:**

- Check console for deprecation warnings
- Note any peer dependency conflicts

---

#### Increment 7.2: Update React Ecosystem Dependencies

**Goal:** Update React-related packages to React 19 compatible versions

**Files to Modify:**

- `web/client/package.json`

**Packages to Update:**

**Testing Libraries:**

```json
{
  "devDependencies": {
    "@testing-library/react": "^16.0.0",
    "@testing-library/jest-dom": "^6.6.0",
    "@testing-library/user-event": "^14.5.2"
  }
}
```

**Redux:**

```json
{
  "dependencies": {
    "react-redux": "^9.1.2",
    "@reduxjs/toolkit": "^2.5.0"
  }
}
```

**Router:**

```json
{
  "dependencies": {
    "react-router-dom": "^7.1.0"
  }
}
```

**Other React Dependencies:**

```json
{
  "dependencies": {
    "react-datepicker": "^7.5.0",
    "react-responsive-modal": "^6.4.2",
    "styled-components": "^6.1.15"
  }
}
```

**Steps:**

1. Update each package version in package.json
2. Run `npm install` or `pnpm install`
3. Check for breaking changes in each package's changelog
4. Run tests after each update if concerned about compatibility

**Validation:**

```bash
npm list
# Verify no peer dependency conflicts

npm run build
# Should compile successfully

npm test
# All tests should pass
```

**OBSERVE:**

- React Router 7 may have breaking changes from v6
- Check styled-components compatibility with React 19
- Verify react-datepicker works correctly with React 19

---

#### Increment 7.3: Address React 19 Breaking Changes

**Goal:** Fix any code that uses deprecated or changed APIs in React 19

**Common Breaking Changes to Check:**

**1. String Refs (Already compliant ✅)**

- React 19 removes string refs completely
- intygsadmin already uses React.createRef/useRef ✅

**2. Legacy Context (Already compliant ✅)**

- React 19 removes legacy context API
- intygsadmin uses modern Context API ✅

**3. ReactDOM APIs (Already migrated ✅)**

- `ReactDOM.render` removed (already using `createRoot` ✅)
- `ReactDOM.hydrate` removed (not used ✅)

**4. PropTypes**

- React 19 may remove built-in PropTypes
- Check if app uses PropTypes, move to `prop-types` package if needed

**5. defaultProps in Function Components**

- React 19 deprecates defaultProps for function components
- Use default parameters instead

**Potential Changes Needed:**

**Check for defaultProps usage:**

```bash
grep -r "defaultProps" web/client/src/components/
grep -r "defaultProps" web/client/src/pages/
```

**If found, migrate to default parameters:**

```javascript
// Before (React 18)
const MyComponent = ({title, count}) => {
  // ...
}
MyComponent.defaultProps = {
  title: 'Default',
  count: 0
}

// After (React 19)
const MyComponent = ({title = 'Default', count = 0}) => {
  // ...
}
```

**Validation:**

```bash
npm run build
# Check console for deprecation warnings

npm start
# Test application functionality

npm test
# All tests should pass
```

**OBSERVE:**

- Note any new console warnings
- Check if third-party components use deprecated APIs
- Verify all routes and navigation work correctly

---

#### Increment 7.4: Update for React 19 Behavioral Changes

**Goal:** Ensure code works with React 19's behavioral changes

**Key Behavioral Changes:**

**1. Automatic Batching**

- React 19 expands automatic batching (already in React 18)
- Should be transparent, but verify state updates work correctly

**2. Stricter StrictMode**

- React 19 StrictMode is more aggressive
- May reveal potential issues in development

**3. Refs Timing Changes**

- Refs may be updated at slightly different times
- Verify any ref-dependent code works correctly

**4. Suspense Changes**

- Improved Suspense behavior
- If using Suspense, test thoroughly

**5. useEffect Cleanup Timing**

- Cleanup may run at different times
- Verify no race conditions in effects

**Testing Focus:**

1. Test all user flows thoroughly
2. Check sessionPoller behavior (uses effects)
3. Verify modals and dialogs work correctly
4. Test routing and navigation
5. Check data fetching and async operations
6. Verify forms and user input handling

**Validation:**

```bash
npm start
# Manual testing of all features

npm test
# All tests should pass

npm run build
# Production build should succeed
```

**OBSERVE:**

- Any unexpected behavior in development
- StrictMode warnings in console
- Performance differences (should be faster)

---

#### Increment 7.5: Final React 19 Validation

**Goal:** Comprehensive validation of React 19 upgrade

**Validation Checklist:**

**Build & Tests:**

- [ ] `npm run build` succeeds without warnings
- [ ] `npm test` all tests pass
- [ ] `npm start` application starts correctly
- [ ] No console errors in development
- [ ] No console errors in production build

**Functional Testing:**

- [ ] All routes accessible
- [ ] Authentication flow works
- [ ] Session polling works correctly
- [ ] All CRUD operations work (users, banners, etc.)
- [ ] Data export functionality works
- [ ] Private practitioner search works
- [ ] Integrated units search works
- [ ] All modals and dialogs open/close correctly
- [ ] Form validation works
- [ ] Date pickers work correctly
- [ ] Pagination works
- [ ] Table sorting/filtering works

**Performance:**

- [ ] Application loads quickly
- [ ] No performance regressions
- [ ] Bundle size similar or smaller
- [ ] React DevTools shows React 19

**Code Quality:**

- [ ] No deprecation warnings in console
- [ ] ESLint passes
- [ ] Prettier formatting consistent
- [ ] No TypeScript errors (if applicable)

**Compatibility:**

- [ ] Works in target browsers
- [ ] Redux DevTools works
- [ ] MSW (if enabled) works correctly
- [ ] All third-party libraries compatible

**Documentation:**

- [ ] Update README if needed
- [ ] Update CHANGELOG
- [ ] Document any known issues
- [ ] Update migration progress document

**Rollback Preparation:**

```bash
# Before committing, ensure you can rollback
git status
# Verify all changes are tracked

git diff web/client/package.json
# Review dependency changes

# Consider creating a rollback branch
git branch react-18-stable
```

**OBSERVE:**

- Any subtle behavior changes
- Performance metrics comparison
- User experience differences
- Browser compatibility issues

---

### 8.3 React 19 Rollback Plan

If critical issues are discovered after React 19 upgrade:

**Quick Rollback (package.json only):**

```json
{
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-redux": "^9.1.2",
    "react-router-dom": "^6.26.1",
    "@testing-library/react": "^13.4.0"
  }
}
```

**Steps:**

1. Revert package.json changes
2. Run `npm install` or `pnpm install`
3. Rebuild and test
4. Document the issue for future resolution

**Full Rollback (if code changes made):**

```bash
git checkout react-18-stable
npm install
npm run build
```

---

### 8.4 React 19 Resources

**Official Documentation:**

- React 19 Release Notes: https://react.dev/blog/2024/12/05/react-19
- React 19 Upgrade Guide: https://react.dev/blog/2024/04/25/react-19-upgrade-guide
- React Compiler: https://react.dev/learn/react-compiler

**Breaking Changes:**

- Removed APIs: string refs, legacy context, defaultProps
- Changed APIs: ref handling, Context behavior
- New warnings: development-only checks

**New Features:**

- React Compiler (optional, for optimization)
- Actions (form handling improvements)
- useOptimistic hook
- use() hook for resources
- Document metadata support

**Migration Tools:**

- React 19 codemod (if available)
- ESLint plugin for React 19 deprecations

---

## 9. Vite Migration

**Status:** Optional - Can be performed after React 19 upgrade is complete and stable  
**Prerequisites:** All Phase 1-8 increments completed and validated  
**Target:** Vite 5.x (from Create React App / react-scripts)  
**Risk Level:** Medium-High (Major build tool change)

### 9.1 Overview

Vite is a modern build tool that provides significantly faster development experience compared to
Create React App (react-scripts). This migration involves replacing the Webpack-based build system
with Vite's native ES module approach.

**Key Benefits:**

1. **Lightning-fast cold starts** - Instant dev server startup (vs. minutes with CRA)
2. **Instant Hot Module Replacement (HMR)** - Changes reflect immediately without page reloads
3. **Optimized production builds** - Rollup-based builds with automatic code splitting
4. **TypeScript support out-of-the-box** - No additional configuration needed
5. **Modern standards** - Built on native ES modules
6. **Better monorepo support** - Works seamlessly with pnpm workspaces

**Migration Considerations:**

- **Breaking change**: Completely replaces build system
- **Environment variables**: Must prefix with `VITE_` for client exposure
- **Import changes**: Some import patterns need adjustment
- **Testing**: May need to adjust test configuration
- **Build scripts**: npm scripts need updating

### 9.2 Phase 9 Increments

#### Increment 9.1: Install Vite Dependencies

**Goal:** Install Vite and related plugins

**Files to Modify:**

- `web/client/package.json`

**Dependencies to Add:**

```json
{
  "devDependencies": {
    "vite": "^5.4.21",
    "@vitejs/plugin-react": "^4.3.4",
    "@vitejs/plugin-legacy": "^5.4.3"
  }
}
```

**Dependencies to Remove:**

```json
{
  "devDependencies": {
    "react-scripts": "removed"
  }
}
```

**Steps:**

1. Update package.json
2. Run `npm install` or `pnpm install`
3. Verify installation completes successfully

**Validation:**

```bash
npm list vite @vitejs/plugin-react
npx vite --version
```

---

#### Increment 9.2: Create Vite Configuration

**Goal:** Create vite.config.js with appropriate settings

**Files to Create:**

- `web/client/vite.config.js`

**Configuration:**

```javascript
import legacy from '@vitejs/plugin-legacy'
import react from '@vitejs/plugin-react'
import path from 'path'
import {defineConfig, loadEnv} from 'vite'

export default ({mode}) => {
  process.env = {...process.env, ...loadEnv(mode ?? 'development', process.cwd())}

  final
  hmr = !(process.env.VITE_HMR === 'false')
  final
  host = process.env.VITE_HOST ?? 'localhost'

  final
  proxy = [
    '/api',
    '/fake',
    '/logout',
    '/login',
    '/error.jsp',
  ].reduce((result, route) => ({
    ...result,
    [route]: {
      secure: false,
      target: process.env.VITE_API_TARGET ?? 'http://localhost:8080',
      cookieDomainRewrite: {'*': ''},
      changeOrigin: true,
      autoRewrite: true,
    },
  }), {})

  return defineConfig({
    plugins: [react()].concat(
        process.env.LEGACY_SUPPORT !== 'false'
            ? legacy({
              targets: ['defaults', 'not IE 11'],
            })
            : []
    ),
    server: {
      host,
      port: 3000,
      proxy,
      strictPort: true,
      allowedHosts: true,
      hmr: hmr
          ? {
            host: process.env.VITE_WS_HOST ?? host,
            protocol: process.env.VITE_WS_PROTOCOL ?? 'ws',
          }
          : false,
    },
    build: {
      outDir: 'build',
      sourcemap: true,
      rollupOptions: {
        output: {
          manualChunks: {
            vendor: ['react', 'react-dom', 'react-router-dom'],
            redux: ['redux', 'react-redux'],
          },
        },
      },
    },
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
  })
}
```

**OBSERVE:**

- Adjust proxy routes based on actual backend API endpoints
- Port 3000 matches current development setup
- Legacy plugin included for IE 11 if needed (check browserslist)

---

#### Increment 9.3: Move and Update index.html

**Goal:** Move index.html to project root and update for Vite

**Files to Modify:**

- `web/client/public/index.html` → `web/client/index.html`

**Changes Required:**

1. Move `public/index.html` to project root (`web/client/index.html`)
2. Add ES module script tag for entry point
3. Remove %PUBLIC_URL% placeholders

**Updated index.html:**

```html
<!doctype html>
<html lang="sv">
<head>
  <meta charset="utf-8"/>
  <link rel="icon" href="/favicon.ico"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <meta name="theme-color" content="#000000"/>
  <meta name="description" content="Intygsadmin - Administration av Intygstjänster"/>
  <title>Intygsadmin</title>
</head>
<body>
<noscript>Du måste aktivera JavaScript för att köra den här appen.</noscript>
<div id="root"></div>
<script type="module" src="/src/index.js"></script>
</body>
</html>
```

**Key Changes:**

- Removed `%PUBLIC_URL%` (Vite handles asset paths automatically)
- Added `<script type="module" src="/src/index.js"></script>` (Vite entry point)
- Moved to project root

**OBSERVE:**

- Verify all asset paths still work (favicon, manifest, etc.)
- Check if manifest.json is needed and reference it correctly

---

#### Increment 9.4: Create Environment Variable Files

**Goal:** Create .env files for Vite environment variables

**Files to Create:**

- `web/client/.env.development`
- `web/client/.env.production`
- `web/client/.env.development.local.example`

**`.env.development`:**

```env
VITE_API_TARGET=http://localhost:8080
VITE_HOST=localhost
```

**`.env.production`:**

```env
VITE_API_TARGET=
```

**`.env.development.local.example`:**

```env
# Copy this file to .env.development.local and adjust for your local setup
VITE_HTTPS=false
VITE_API_TARGET=http://localhost:8080
VITE_HOST=0.0.0.0
VITE_HMR=true
```

**Update `.gitignore`:**

```gitignore
.env.local
.env.development.local
.env.production.local
```

**OBSERVE:**

- All client-exposed environment variables MUST be prefixed with `VITE_`
- Update any code that references `process.env.REACT_APP_*` to `import.meta.env.VITE_*`

---

#### Increment 9.5: Update Environment Variable References

**Goal:** Replace CRA environment variables with Vite equivalents

**Search Pattern:**

```bash
grep -r "process.env.REACT_APP_" web/client/src/
grep -r "process.env.PUBLIC_URL" web/client/src/
grep -r "process.env.NODE_ENV" web/client/src/
```

**Replacements:**

| Old (CRA)                 | New (Vite)                                                               |
|---------------------------|--------------------------------------------------------------------------|
| `process.env.REACT_APP_*` | `import.meta.env.VITE_*`                                                 |
| `process.env.PUBLIC_URL`  | `import.meta.env.BASE_URL` or just `/`                                   |
| `process.env.NODE_ENV`    | `import.meta.env.MODE` or `import.meta.env.DEV` / `import.meta.env.PROD` |

**Example Changes:**

```javascript
final
isDev = import.meta.env.DEV
final
isProd = import.meta.env.PROD
final
mode = import.meta.env.MODE

final
apiTarget = import.meta.env.VITE_API_TARGET
```

**OBSERVE:**

- Check all files that use environment variables
- NODE_ENV usage may need different approach (DEV/PROD booleans)
- Update any build scripts that set environment variables

---

#### Increment 9.6: Update Package Scripts

**Goal:** Replace react-scripts commands with Vite commands

**Files to Modify:**

- `web/client/package.json`

**Script Changes:**

```json
{
  "scripts": {
    "start": "vite",
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "vitest",
    "test:ui": "vitest --ui",
    "test:coverage": "vitest run --coverage"
  }
}
```

**Remove:**

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

**OBSERVE:**

- Consider keeping separate `start` and `dev` scripts for compatibility
- Build output location is now configurable (defaults to `dist`, we set to `build`)

---

#### Increment 9.7: Update Test Configuration for Vitest

**Goal:** Migrate from Jest (CRA) to Vitest

**Files to Create:**

- `web/client/vitest.config.js`

**Configuration:**

```javascript
import react from '@vitejs/plugin-react'
import path from 'path'
import {defineConfig} from 'vitest/config'

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/setupTests.js'],
    css: false,
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html', 'lcov'],
      exclude: [
        'node_modules/',
        'src/setupTests.js',
        '**/*.test.{js,jsx}',
        '**/*.spec.{js,jsx}',
      ],
      thresholds: {
        branches: 60,
        lines: 60,
        functions: 60,
        statements: 60,
      },
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})
```

**Dependencies to Add:**

```json
{
  "devDependencies": {
    "vitest": "^1.6.0",
    "@vitest/ui": "^1.6.0",
    "@vitest/coverage-v8": "^1.6.0",
    "jsdom": "^24.0.0"
  }
}
```

**Update setupTests.js** (if needed):

```javascript
import '@testing-library/jest-dom'

global.expect = expect
```

**OBSERVE:**

- Vitest is mostly Jest-compatible, but check for any custom Jest configuration
- Some Jest-specific patterns may need adjustment
- Coverage thresholds match post-migration targets

---

#### Increment 9.8: Handle CSS/SASS Imports

**Goal:** Ensure CSS preprocessing works with Vite

**Dependencies (Already Added Earlier):**

```json
{
  "devDependencies": {
    "sass": "^1.70.0"
  }
}
```

**Vite Auto-Detection:**

- Vite automatically processes `.scss` files with sass installed
- No additional configuration needed
- CSS modules work with `.module.scss` naming

**Verify Imports:**

```bash
grep -r "@import.*~" web/client/src/
```

**If found, update Webpack-style imports:**

```scss
@import '~bootstrap/scss/bootstrap';
```

**To:**

```scss
@import 'bootstrap/scss/bootstrap';
```

**OBSERVE:**

- Vite doesn't need the `~` prefix for node_modules imports
- Check for any custom webpack loaders that need Vite equivalents

---

#### Increment 9.9: Handle Asset Imports

**Goal:** Update asset import patterns for Vite

**Vite Asset Handling:**

```javascript
import logoUrl from './logo.png'

<
img
src = {logoUrl}
alt = "Logo" / >
```

**For SVG as React Components (if needed):**

Install plugin:

```json
{
  "devDependencies": {
    "vite-plugin-svgr": "^4.2.0"
  }
}
```

Update `vite.config.js`:

```javascript
import svgr from 'vite-plugin-svgr'

plugins: [
  react(),
  svgr(),
]
```

Usage:

```javascript
import {ReactComponent as Logo} from './logo.svg'

<
Logo / >
```

**OBSERVE:**

- Check how SVGs are currently imported
- Verify all asset types work correctly (images, fonts, etc.)

---

#### Increment 9.10: Update Backend Integration

**Goal:** Ensure proxy configuration works with backend

**Review proxy configuration in `vite.config.js`:**

```javascript
final
proxy = [
  '/api',
  '/fake',
  '/logout',
  '/login',
  '/error.jsp',
].reduce((result, route) => ({
  ...result,
  [route]: {
    secure: false,
    target: process.env.VITE_API_TARGET ?? 'http://localhost:8080',
    cookieDomainRewrite: {'*': ''},
    changeOrigin: true,
    autoRewrite: true,
  },
}), {})
```

**Test Requirements:**

1. Verify all API routes are proxied correctly
2. Test authentication flow (cookies should work)
3. Verify session management
4. Test file uploads/downloads if applicable

**OBSERVE:**

- Add any missing backend routes to proxy configuration
- Test with actual backend server running
- Verify cookie handling works correctly

---

#### Increment 9.11: First Build and Validation

**Goal:** Build the application with Vite for the first time

**Commands:**

```bash
cd web/client
npm run build
```

**Expected Output:**

```
vite v5.4.21 building for production...
✓ 234 modules transformed.
build/index.html                   0.52 kB │ gzip:  0.31 kB
build/assets/index-abc123.css     45.20 kB │ gzip: 12.34 kB
build/assets/index-def456.js     128.45 kB │ gzip: 45.67 kB
build/assets/vendor-ghi789.js    156.78 kB │ gzip: 52.34 kB
✓ built in 8.52s
```

**Validation Checklist:**

- [ ] Build completes without errors
- [ ] Build output in `build/` directory
- [ ] index.html contains script tags with hashed filenames
- [ ] Asset files have content hashes
- [ ] Source maps generated (if configured)
- [ ] Bundle sizes reasonable (compare to CRA build)

**OBSERVE:**

- Note any build warnings
- Check bundle sizes vs. previous CRA build
- Verify chunk splitting is working

---

#### Increment 9.12: Development Server Testing

**Goal:** Test development server with HMR

**Commands:**

```bash
cd web/client
npm run dev
```

**Expected Output:**

```
VITE v5.4.21  ready in 543 ms

➜  Local:   http://localhost:3000/
➜  Network: use --host to expose
➜  press h + enter to show help
```

**Testing Checklist:**

- [ ] Server starts in < 5 seconds
- [ ] Application loads in browser
- [ ] No console errors
- [ ] Edit a component → HMR updates instantly
- [ ] Edit a CSS file → Styles update without reload
- [ ] Redux state preserved during HMR
- [ ] API proxy works (test login, data fetching)
- [ ] Session management works

**HMR Test:**

1. Open application in browser
2. Open DevTools console
3. Edit a component file and save
4. Verify: Component updates without page reload
5. Verify: Redux state NOT reset
6. Edit CSS file and save
7. Verify: Styles update instantly

**OBSERVE:**

- Note HMR speed compared to CRA
- Check for any HMR errors in console
- Verify all features work the same as before

---

#### Increment 9.13: Update CI/CD Pipeline

**Goal:** Update build and deployment scripts for Vite

**Files to Modify:**

- `Jenkins.properties` (if applicable)
- `.github/workflows/*` (if using GitHub Actions)
- Any CI/CD configuration files

**Build Script Updates:**

```bash
cd web/client
npm ci
npm run build
```

**Environment Variables in CI:**

```bash
VITE_API_TARGET=https://api.production.com
VITE_HOST=0.0.0.0
```

**Docker Updates (if applicable):**

Update Dockerfile to use Vite:

```dockerfile
FROM node:18 AS build

WORKDIR /app
COPY web/client/package*.json ./
RUN npm ci

COPY web/client/ ./
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
```

**OBSERVE:**

- Update any deployment documentation
- Test build in CI environment
- Verify environment variables are properly injected

---

#### Increment 9.14: Update Documentation

**Goal:** Document Vite setup and development workflow

**Files to Update:**

- `web/client/README.md`
- Developer setup guides
- Contribution guidelines

**README.md Updates:**

```markdown
## Development

This project uses Vite as the build tool.

### Prerequisites

- Node.js 18+
- npm or pnpm

### Getting Started

```bash
cd web/client
npm install
npm run dev
```

The application will be available at http://localhost:3000

### Environment Variables

Create `.env.development.local` for local overrides:

```env
VITE_API_TARGET=http://localhost:8080
VITE_HOST=localhost
```

All client-exposed variables must be prefixed with `VITE_`.

### Available Scripts

- `npm run dev` - Start development server with HMR
- `npm run build` - Build for production
- `npm run preview` - Preview production build locally
- `npm run test` - Run tests with Vitest
- `npm run test:ui` - Run tests with UI
- `npm run test:coverage` - Generate coverage report

### Build Tool

This project uses [Vite](https://vitejs.dev/) for:

- Lightning-fast dev server startup
- Instant Hot Module Replacement (HMR)
- Optimized production builds
- Native ES modules in development

See `vite.config.js` for configuration details.

```

**OBSERVE:**

- Update any screenshots showing build output
- Document any Vite-specific patterns used
- Note differences from previous CRA setup

---

### 9.3 Vite Migration Validation

**Comprehensive Testing Checklist:**

#### Development Experience

- [ ] Dev server starts in < 5 seconds (vs. CRA: 30-60+ seconds)
- [ ] HMR updates in < 100ms
- [ ] No console errors or warnings
- [ ] All environment variables work
- [ ] API proxy works correctly
- [ ] Session management works
- [ ] File watching works

#### Build Quality

- [ ] Production build completes successfully
- [ ] Bundle sizes reasonable or smaller than CRA
- [ ] Code splitting working correctly
- [ ] Source maps generated
- [ ] Assets hashed correctly
- [ ] No build warnings

#### Functionality

- [ ] All application features work
- [ ] Authentication/login works
- [ ] All routes accessible
- [ ] Forms submit correctly
- [ ] Modals work
- [ ] Date pickers work
- [ ] All CRUD operations work
- [ ] Error handling works
- [ ] Logout works

#### Testing

- [ ] All tests pass with Vitest
- [ ] Test coverage maintained or improved
- [ ] Tests run faster than with Jest
- [ ] Coverage reports generate correctly

#### Browser Testing

- [ ] Chrome (latest)
- [ ] Firefox (latest)
- [ ] Safari (latest)
- [ ] Edge (latest)
- [ ] IE 11 (if required) - with legacy plugin

#### Performance

- [ ] Initial load time same or better
- [ ] Bundle size same or smaller
- [ ] Time to interactive same or better
- [ ] No performance regressions

**Verification Commands:**

```bash
grep -r "react-scripts" web/client/
grep -r "process.env.REACT_APP_" web/client/src/
grep -r "@import.*~" web/client/src/
grep -r "process.env.PUBLIC_URL" web/client/src/
```

All should return 0 results.

---

### 9.4 Vite Migration Rollback Plan

If critical issues are discovered after Vite migration:

**Quick Rollback:**

```bash
git stash
git checkout main
npm install
npm start
```

**Partial Rollback (keep React 19, revert to CRA):**

1. Restore `package.json` with `react-scripts`
2. Restore original build scripts
3. Move `index.html` back to `public/`
4. Restore environment variable references
5. Run `npm install`
6. Test thoroughly

**Files to Revert:**

- `web/client/package.json`
- `web/client/index.html` (location and content)
- `web/client/vite.config.js` (delete)
- `web/client/vitest.config.js` (delete)
- `web/client/.env.*` files
- Any files with `import.meta.env` changes

---

### 9.5 Vite Migration Resources

**Official Documentation:**

- Vite Guide: https://vitejs.dev/guide/
- Vite Configuration: https://vitejs.dev/config/
- Vite Plugin React: https://github.com/vitejs/vite-plugin-react
- Migrating from CRA: https://vitejs.dev/guide/migration.html

**Key Concepts:**

- **Native ES Modules**: Vite serves source files as ES modules in development
- **Dependency Pre-bundling**: Vite pre-bundles dependencies with esbuild
- **Hot Module Replacement**: Updates modules without page reload
- **Build Optimization**: Rollup-based production builds with tree-shaking

**Common Patterns:**

- Environment variables: `import.meta.env.VITE_*`
- Asset imports: `import assetUrl from './asset.png'`
- JSON imports: `import data from './data.json'`
- Dynamic imports: `const module = await import('./module.js')`

**Troubleshooting:**

- **Slow HMR**: Check for circular dependencies
- **Module not found**: Verify import paths (no webpack aliases without config)
- **CSS not loading**: Check sass is installed for .scss files
- **Proxy not working**: Verify proxy configuration and API target

---

### 9.6 Post-Vite Migration Benefits

**Measured Improvements (Expected):**

| Metric                | CRA      | Vite            | Improvement     |
|-----------------------|----------|-----------------|-----------------|
| Dev server cold start | 30-60s   | 2-5s            | **90% faster**  |
| Dev server warm start | 15-30s   | 1-2s            | **95% faster**  |
| HMR update time       | 1-5s     | 50-100ms        | **95% faster**  |
| Build time            | 60-120s  | 30-60s          | **50% faster**  |
| Bundle size           | Baseline | Same or smaller | 0-10% reduction |

**Developer Experience:**

- Near-instant feedback during development
- No waiting for rebuilds
- State preservation during HMR
- Modern tooling and patterns
- Better error messages
- Simpler configuration

**Production Benefits:**

- Optimized bundle splitting
- Better tree-shaking
- Smaller bundle sizes
- Faster build times in CI/CD
- Modern output (ES modules + legacy fallback)

---

## 10. Appendices

### Appendix A: Complete File Inventory

**Files Requiring Modification:**

**Phase 1: Entry Point & Setup (3 files)**

1. `web/client/package.json` - Dependency updates
2. `web/client/src/index.js` - createRoot() migration
3. (Optional) Enable StrictMode in index.js

**Phase 2: Remove Recompose (22 files)**

**Critical - Lifecycle HOC Migration (6 files):**

1. `src/App.js` - UNSAFE_componentWillMount, componentDidMount, componentWillUnmount
2. `components/sessionPoller/SessionPollerContainer.js` - componentDidMount, componentDidUpdate,
   componentWillUnmount
3. `components/users/UsersList.js` - componentDidMount
4. `components/intygInfo/IntygInfoHistoryList.js` - componentDidMount
5. `components/bannerList/BannerListContainer.js` - componentDidMount
6. `components/dataExport/DataExportList.js` - componentDidMount

**compose() Only (17 files):**

7. `components/privatePractitioner/PrivatePractitionerSearch.js`
8. `components/privatePractitioner/PrivatePractitionerSearchResult.dialog.js`
9. `components/users/UsersActionBar.js`
10. `components/users/Users.js`
11. `components/privatePractitioner/PrivatePractitionerExport.js`
12. `components/TestLinks/TestLinks.js`
13. `components/users/dialogs/RemoveUser.dialog.js`
14. `components/users/dialogs/CreateUser.dialog.js`
15. `components/ResendStatusCount/ResendStatusCount.js`
16. `components/header/HeaderContainer.js`
17. `components/iaMenu/MenuBar.js`
18. `components/intygInfo/IntygInfoHistory.js`
19. `components/intygInfo/IntygInfoDialog.js`
20. `components/intygInfo/IntygInfoSearch.js`
21. `components/loginOptions/LoginOptionsContainer.js`
22. `components/integratedUnits/IntegratedUnitsExport.js`
23. `components/integratedUnits/IntegratedUnitsSearch.js`

**Redux Store (2 files):**

24. `src/store/configureStore.js` - Remove routerMiddleware
25. `src/store/reducers/index.js` - Remove router reducer

**Phase 3: React Router 6 (3 files)**

26. `components/auth/SecuredRoute.js` - Update to React Router 6
27. `components/auth/UnsecuredRoute.js` - Update to React Router 6
28. (All navigation code that uses useHistory/history.push - if any)

**Phase 4: Testing (3 files)**

29. `src/setupTests.js` - Replace Enzyme with RTL
30. Create `src/mocks/handlers.js` - MSW handlers
31. Create `src/mocks/server.js` - MSW server

**Phase 5: Tests to Migrate/Add (3-5 files)**

32. `components/toggler/Toggler.spec.js` - Migrate to RTL
33. `src/App.test.js` - Expand with comprehensive tests
34. Create `components/auth/SecuredRoute.test.js`
35. Create `components/sessionPoller/SessionPoller.test.js`
36. (Additional test files as needed)

**Total Files to Modify:** ~36 files  
**Total Files to Create:** 4-6 new test files

**Files NOT Requiring Changes:**

- All 9 page files (already functional, no lifecycle)
- ~75 presentational components (no recompose, no lifecycle)
- 16+ Redux test files (testing Redux, not React components)
- All styled component files
- All utility files
- Custom hook (already properly implemented)

### Appendix B: OBSERVE Items

Throughout migration, these items require developer attention or verification:

1. **OBSERVE:** SessionPollerContainer has `componentDidUpdate` - verify polling behavior works
   correctly after migration to useEffect
2. **OBSERVE:** SecuredRoute has unused props (`hasCurrentUnit`, `allowMissingUnit`) - verify these
   are not needed or implement if required
3. **OBSERVE:** Verify AppConstants.DEFAULT_PAGE is correct redirect target for authenticated users
4. **OBSERVE:** After React Router 6 migration, test all route transitions thoroughly (especially
   auth flows)
5. **OBSERVE:** IE 11 support requirement - verify with team (browserslist includes "ie 11")
6. **OBSERVE:** Third-party library behavior after updates (especially react-datepicker,
   react-responsive-modal)
7. **OBSERVE:** Any custom navigation logic beyond standard routing
8. **OBSERVE:** Bootstrap 4.6.2 import syntax with dart-sass - verify no issues
9. **OBSERVE:** Redux DevTools extension compatibility with React 18 (should be fine, but test)
10. **OBSERVE:** Performance metrics after migration - bundle size, load time, TTI

**Resolution Process:**

- Test each OBSERVE item during relevant increment
- Document findings
- Flag for developer review if unclear
- Update migration guide notes if needed

### Appendix C: Reference Documents

- **Requirements:** `.github/migration/instructions/intygsadmin-requirements.md`
- **Test Coverage:** `.github/migration/intygsadmin-test-coverage-analysis.md`
- **Design Choices:** `.github/migration/instructions/intyg-frontend-react-design-choices.md`
- **Analysis Instructions:** `.github/migration/instructions/react-analysis.instructions.md`

### Appendix D: Estimated Effort by Increment

| Phase   | Increment                         | Estimated Time | Risk Level |
|---------|-----------------------------------|----------------|------------|
| 1.1     | Update Dependencies               | 0.5h           | Low        |
| 1.2     | Migrate Entry Point               | 0.5h           | Low        |
| 1.3     | Enable StrictMode                 | 0.25h          | Low        |
| 2.1     | Migrate App.js                    | 2-3h           | **HIGH**   |
| 2.2     | Update configureStore             | 1h             | Medium     |
| 2.3     | Update rootReducer                | 0.5h           | Medium     |
| 2.4-2.8 | Migrate lifecycle files           | 3-4h           | Medium     |
| 2.9     | Remove compose()                  | 2h             | Low        |
| 2.10    | Remove recompose                  | 0.25h          | Low        |
| 3.1-3.2 | Update auth routes                | 1-2h           | **HIGH**   |
| 3.3     | Update route definitions          | 1h             | Medium     |
| 3.4     | Update navigation code            | 1h             | Medium     |
| 4.1     | Update setupTests                 | 0.5h           | Low        |
| 4.2     | Setup MSW                         | 1h             | Low        |
| 5.1-5.4 | Add tests                         | 4-8h           | Medium     |
| 6.1-6.2 | Cleanup                           | 1-2h           | Low        |
| 7.1     | Update React Core to 19.2.x       | 0.5h           | Medium     |
| 7.2     | Update Ecosystem Dependencies     | 1h             | Medium     |
| 7.3     | Address React 19 Breaking Changes | 1-2h           | **HIGH**   |
| 7.4     | Update for Behavioral Changes     | 1-2h           | Medium     |
| 7.5     | Final React 19 Validation         | 1h             | Medium     |

**Total React 18:** 19-30 hours (2.5-4 days) for experienced developer  
**Total React 19:** 4.5-6.5 hours (0.5-1 day) for experienced developer  
**Grand Total:** 23.5-36.5 hours (3-5 days)

---

**Document Status:** ✅ Complete and Ready for Implementation (Updated for React 19)  
**Next Action:** Begin Phase 1, Increment 1.1 (or Phase 7 if React 18 complete)  
**Created:** January 14, 2026  
**Updated:** January 15, 2026 (React 19 upgrade added)  
**Author:** AI Migration Agent (Copilot)

