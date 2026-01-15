# Missing Patterns from Inspiration Document

## Date: January 14, 2026

This document lists important patterns found in the intyg-frontend inspiration document that should
be added to the intygsadmin migration guide.

---

## 1. ✅ IMPLEMENTED: Parameterized Store Configuration

**Pattern from other app:**

```typescript
export const configureApplicationStore = (middleware: Middleware[]) =>
    configureStore({
      reducer,
      middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(middleware),
    })
```

**Status:** ✅ IMPLEMENTED in `web/client/src/store/configureStore.js`

**Benefits:**

- Testability: Can create stores with test-specific middleware
- Flexibility: Can add custom middleware at runtime
- Clean separation: Store factory vs store instance

---

## 2. 🔴 MISSING: Typed Redux Hooks Pattern

**Pattern from intyg-frontend:**

```typescript
// store/hooks.ts
import {useDispatch, useSelector} from 'react-redux';
import type {RootState, AppDispatch} from './store';

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
```

**Enforcement via ESLint:**

```javascript
'@typescript-eslint/no-restricted-imports'
:
[
  'error',
  {
    name: 'react-redux',
    importNames: ['useDispatch', 'useSelector'],
    message: 'Use typed hook `useAppDispatch/useAppSelector` instead.',
  },
]
```

**Recommendation:**

- **For JavaScript:** Can create similar pattern without types:
  ```javascript
  // store/hooks.js
  import { useDispatch, useSelector } from 'react-redux';
  
  export const useAppDispatch = () => useDispatch();
  export const useAppSelector = useSelector;
  ```
- **For Future TypeScript migration:** This pattern is ready to add types
- **Priority:** MEDIUM - Good practice but not required for React 18 migration

---

## 3. 🔴 MISSING: Mock Service Worker (MSW) Setup

**Pattern from intyg-frontend:**

**Browser Setup (Development):**

```typescript
// main.tsx
if (import.meta.env.MODE === 'development' && import.meta.env.VITE_USE_MOCKS === 'true') {
  const {worker} = await import('./mocks/browser')
  worker.start()
}
```

**Test Setup:**

```typescript
// setupTests.ts
import {server} from './mocks/server'

beforeAll(() => server.listen({onUnhandledRequest: 'error'}))
afterEach(() => server.resetHandlers())
afterAll(() => server.close())
```

**Handler Example:**

```typescript
// mocks/handlers.ts
import {rest} from 'msw'

export const handlers = [
  rest.get('/api/user', (req, res, ctx) => {
    return res(ctx.json({name: 'Test User', isAuthenticated: true}))
  }),
]
```

**Recommendation:**

- **Current Guide Status:** Increment 4.2 mentions MSW as "Optional"
- **Update Needed:** Should be marked as RECOMMENDED for modern React apps
- **Priority:** HIGH - Critical for testing without backend dependencies

---

## 4. 🔴 MISSING: Custom Middleware Patterns

**Pattern from webcert (for reference):**

```typescript
// Middleware for session management
export const sessionMiddleware: Middleware = (store) => (next) => (action) => {
  if (shouldCheckSession(action)) {
    // Check session validity
    store.dispatch(sessionActions.ping())
  }
  return next(action)
}

// Middleware for error handling
export const errorMiddleware: Middleware = (store) => (next) => (action) => {
  if (isRejectedAction(action)) {
    // Handle errors globally
    store.dispatch(errorActions.show(action.error))
  }
  return next(action)
}
```

**Usage:**

```typescript
const store = configureApplicationStore([
  sessionMiddleware,
  errorMiddleware,
])
```

**Recommendation:**

- **Current Status:** Guide shows how to add middleware but doesn't show examples
- **Add to Guide:** Section on "Common Custom Middleware Patterns"
- **Priority:** MEDIUM - Useful reference but not blocking

---

## 5. 🟡 PARTIALLY COVERED: Navigation State Management

**Pattern from webcert:**

```typescript
// Redux slice for programmatic navigation
const navigateSlice = createSlice({
  name: 'navigate',
  initialState: {pathname: undefined, replace: false},
  reducers: {
    push: (state, {payload}: PayloadAction<string>) => {
      state.pathname = payload
    },
    replace: (state, {payload}: PayloadAction<string>) => {
      state.pathname = payload
      state.replace = true
    },
  },
})

// Custom hook to sync Redux state with router
export function useNavigateEffect() {
  const navigate = useNavigate()
  const pathname = useAppSelector((state) => state.ui.uiNavigation.pathname)

  useEffect(() => {
    if (pathname) {
      navigate(pathname, {replace: shouldReplace})
      dispatch(reset())
    }
  }, [pathname])
}
```

**Recommendation:**

- **Current Status:** Guide covers `useNavigate()` replacement but not Redux integration
- **Assessment:** This pattern is **over-engineered** for most apps
- **Priority:** LOW - Mention as "advanced pattern" but don't recommend for intygsadmin

---

## 6. 🔴 MISSING: RTK Query Integration Pattern

**Pattern from minaintyg:**

```typescript
// api.ts
export const api = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: '/api/',
    prepareHeaders: (headers) => {
      headers.set('X-XSRF-TOKEN', getCookie('XSRF-TOKEN') ?? '')
      return headers
    },
  }),
  tagTypes: ['User', 'Certificate'],
  endpoints: (builder) => ({
    getUser: builder.query<User, void>({
      query: () => '/user',
      providesTags: ['User'],
    }),
  }),
})

// Store setup
const store = configureApplicationStore([api.middleware])
```

**Recommendation:**

- **Current Status:** Not mentioned in guide
- **Assessment:** RTK Query is a **major upgrade** from manual fetch/axios
- **Priority:** LOW for initial migration, HIGH for future modernization
- **Add to Guide:** New section "Phase 7: Optional - RTK Query Migration"

---

## 7. 🔴 MISSING: Error Boundary Implementation

**Pattern from intyg-frontend:**

```tsx
// ErrorBoundary.tsx
export function ErrorBoundary() {
  const error = useRouteError()

  return (
      <div>
        <h1>Something went wrong</h1>
        <p>{error.message}</p>
      </div>
  )
}

// Usage in routes
<Route
    path="/"
    errorElement={<ErrorBoundary/>}
    element={<Layout/>}
/>
```

**Recommendation:**

- **Current Status:** Not mentioned in migration guide
- **Priority:** MEDIUM - Good practice for production apps
- **Add to Guide:** New increment in Phase 3 "Add Error Boundaries"

---

## 8. 🟡 PARTIALLY COVERED: Strict Mode

**Pattern from intyg-frontend:**

```tsx
<StrictMode>
  <Provider store={store}>
    <App/>
  </Provider>
</StrictMode>
```

**Recommendation:**

- **Current Status:** ✅ Increment 1.3 covers this
- **Status:** GOOD - Already in guide

---

## 9. 🔴 MISSING: Development vs Production Configuration

**Pattern from intyg-frontend:**

```typescript
// Environment-based feature flags
if (import.meta.env.MODE === 'development') {
  // Development-only features
}

// DevTools conditional
devTools: process.env.NODE_ENV !== 'production'

// Different base URLs
baseUrl: import.meta.env.VITE_API_URL || '/api/'
```

**Recommendation:**

- **Current Status:** DevTools conditional is in guide ✅
- **Add:** Section on environment configuration best practices
- **Priority:** LOW - Most is already handled

---

## Priority Summary for Guide Updates

### 🔴 HIGH PRIORITY (Should Add to Guide):

1. **MSW Setup** - Move from "optional" to "recommended"
2. **Typed Redux Hooks** - Add as optional enhancement section
3. **Error Boundaries** - Add as new increment

### 🟡 MEDIUM PRIORITY (Nice to Have):

4. **Custom Middleware Examples** - Add reference section
5. **RTK Query** - Add as "Future Enhancement" section

### 🟢 LOW PRIORITY (Optional):

6. **Navigation State Management** - Mention as advanced pattern (don't recommend)
7. **Environment Config** - Already mostly covered

---

## Recommended Guide Updates

### Update 1: Enhance Increment 2.2

✅ **DONE** - Updated configureStore to use parameterized pattern

### Update 2: Add New Section After Phase 2

**Title:** "Phase 2B: Optional Enhancements"

- Increment 2.11: Add Typed Redux Hooks (Optional)
- Increment 2.12: Add Error Boundaries to Routes
- Increment 2.13: Setup MSW for Development

### Update 3: Add Future Enhancements Section

**Title:** "Phase 7: Future Modernization (Optional)"

- RTK Query migration
- Custom middleware patterns
- Advanced navigation patterns

---

## Implementation Status for intygsadmin

- ✅ Parameterized configureStore: IMPLEMENTED
- ✅ Modern middleware composition: IMPLEMENTED
- ✅ DevTools configuration: IMPLEMENTED
- ⏳ Typed hooks: NOT YET IMPLEMENTED (JavaScript app)
- ⏳ MSW setup: NOT YET IMPLEMENTED
- ⏳ Error boundaries: NOT YET IMPLEMENTED
- ⏳ RTK Query: NOT YET IMPLEMENTED (future enhancement)


