# Intyg Frontend - React Design Choices Analysis

## Executive Summary

This document analyzes the design choices made in the Intyg frontend repository, a monorepo
containing multiple React applications for Swedish healthcare certificate management. The repository
demonstrates a mature, production-ready architecture with consistent patterns across three main
applications: **webcert**, **minaintyg**, and **rehabstod**.

---

## 1. Component Architecture

### 1.1 Functional Components with Hooks

**Choice:** Exclusive use of functional components with React Hooks (no class components)

**Evidence:**

- All components use functional syntax with hooks like `useState`, `useEffect`, `useMemo`
- Custom hooks pattern (e.g., `useNavigateEffect`, `usePrevious`, `useLogout`)
- No class-based components found in the codebase

**Benefits:**

- Modern React best practices (post-16.8)
- Simpler mental model and less boilerplate
- Better code reuse through custom hooks
- Improved performance optimization opportunities

**Trade-offs:**

- Requires team familiarity with hooks lifecycle model
- More complex state management compared to Redux class components
- Hooks rules must be strictly followed (no conditional hooks)

### 1.2 No React.memo Optimization

**Choice:** Minimal use of performance memoization techniques

**Evidence:**

- No `React.memo` usage found in search results
- Limited `useMemo` usage (only 8 instances across all apps)
- No `useCallback` usage found

**Implications:**

- **Positive:** Simpler code, easier to maintain, premature optimization avoided
- **Negative:** Potential re-render issues in large lists or complex component trees
- **Assessment:** Pragmatic choice - optimize when performance issues arise, not preemptively

### 1.3 Strict Mode Enabled

**Choice:** React.StrictMode wrapper in all applications

**Evidence:**

```tsx
// apps/webcert/src/index.tsx
<StrictMode>
  <Provider store={store}>
    <FloatingDelayGroup delay={200}>
      <App/>
    </FloatingDelayGroup>
  </Provider>
</StrictMode>
```

**Benefits:**

- Identifies potential problems during development
- Detects unsafe lifecycle methods
- Warns about deprecated APIs
- Double-invokes effects to detect side-effect issues

---

## 2. State Management

### 2.1 Redux Toolkit as Primary State Manager

**Choice:** Redux Toolkit with RTK Query for both global state and server state

**Evidence:**

- `@reduxjs/toolkit` used in all applications
- Webcert: v1.9.7, Minaintyg/Rehabstod: v2.8.2
- Consistent store configuration pattern across apps

**Implementation Details:**

**Webcert (Complex/Legacy Pattern):**

```typescript
// Traditional Redux with custom middleware
const store = configureApplicationStore([
  api.middleware,
  apiMiddleware,
  certificateMiddleware,
  userMiddleware,
  // ... 13+ custom middlewares
])
```

**Minaintyg (Modern Pattern):**

```typescript
// Simpler RTK Query focused approach
export const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat([
        api.middleware,
        sessionMiddleware,
        errorMiddleware
      ]),
})
```

**Benefits:**

- Official Redux best practices baked in
- Reduced boilerplate compared to plain Redux
- Built-in immutability with Immer
- Excellent TypeScript support
- DevTools integration

**Trade-offs:**

- Steeper learning curve than Context API
- More complex setup than simpler alternatives (Zustand, Jotai)
- Overkill for small applications (appropriate here given complexity)

### 2.2 RTK Query for Data Fetching

**Choice:** RTK Query for server state management

**Evidence:**

```typescript
// apps/minaintyg/src/store/api.ts
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
  endpoints: (builder) => ({ /* ... */}),
})
```

**Features Used:**

- Automatic cache management
- Optimistic updates via `invalidatesTags`
- Polling with `useGetSessionPingQuery`
- TypeScript code generation from endpoints

**Benefits:**

- Eliminates manual loading/error states
- Automatic request deduplication
- Cache invalidation strategy built-in
- Normalized data handling

**Trade-offs:**

- Tight coupling to Redux
- Learning curve for cache invalidation patterns
- More opinionated than axios + custom hooks

### 2.3 Hybrid Approach in Webcert

**Choice:** Custom axios-based middleware alongside RTK Query

**Evidence:**

```typescript
// apps/webcert/src/store/api.ts - Custom axios baseQuery
const axiosBaseQuery = ({baseUrl}: { baseUrl: string }) =>
    async (arg) => {
      try {
        const result = await axios({ /* ... */})
        return {data: result.data}
      } catch (axiosError) { /* ... */
      }
    }
```

**Additional Pattern:**

```typescript
// Custom middleware for legacy API calls
export const apiMiddleware: Middleware = (middlewareAPI) => (next) => (action) => {
  if (apiCallBegan.match(action)) {
    // Custom axios request handling
  }
}
```

**Implications:**

- **Reason:** Webcert is the oldest, most complex application with legacy patterns
- **Challenge:** Two different data-fetching paradigms in same codebase
- **Benefit:** Gradual migration path to RTK Query without breaking existing code
- **Recommendation:** Eventual consolidation to single pattern for consistency

### 2.4 Typed Hooks Pattern

**Choice:** Strongly typed Redux hooks with custom wrappers

**Evidence:**

```typescript
// apps/webcert/src/store/store.ts
export type AppDispatch = typeof store.dispatch
export const useAppDispatch = (): AppDispatch => useDispatch<AppDispatch>()
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector
```

**Enforcement via ESLint:**

```javascript
// packages/eslint-config-react/index.js
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

**Benefits:**

- Type-safe state access across application
- Prevents accidental use of untyped hooks
- Better IDE autocomplete and refactoring support
- Catches type errors at compile time

---

## 3. Routing Approach

### 3.1 React Router v6 with Data Router

**Choice:** React Router DOM v6.30.1+ with `createBrowserRouter`

**Evidence:**

```tsx
// apps/minaintyg/src/routes.tsx
export const routes = createRoutesFromChildren([
      <Route
          path="/"
          handle={{crumb: () => 'Intyg'}}
          errorElement={<ErrorBoundary/>}
          element={<ProtectedRoute><Layout>...</Layout></ProtectedRoute>}
      >
        <Route index element={<CertificateListPage/>}/>
        <Route path=":id" element={<CertificatePage/>}/>
      </Route>,
    ])

    // main.tsx
    < RouterProvider
router = {createBrowserRouter(routes)}
/>
```

**Advanced Features Used:**

- **Error Boundaries per route:** `errorElement={<ErrorBoundary />}`
- **Route handles for breadcrumbs:** `handle={{ crumb: () => 'Label' }}`
- **Protected route wrappers:** Custom authentication HOC
- **Nested layouts:** Outlet-based composition

**Benefits:**

- Modern data-loading patterns (though not utilized here)
- Better error handling boundaries
- Type-safe route parameters
- Improved programmatic navigation

**Trade-offs:**

- Breaking changes from v5 (migration effort)
- More verbose than v5 for simple cases
- Some features unused (loaders, actions)

### 3.2 Custom Navigation State Management

**Choice:** Redux slice for programmatic navigation instead of imperative `navigate()`

**Evidence:**

```typescript
// apps/webcert/src/store/navigateSlice.ts
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

**Rationale:**

- Decouples navigation logic from components
- Testable navigation logic without router mocks
- Navigation can be triggered from middleware/side effects

**Trade-offs:**

- **Complexity:** Adds indirection layer
- **Non-standard:** Most React Router apps use imperative `navigate()`
- **Maintenance:** Custom abstraction requires documentation
- **Assessment:** Useful for complex navigation flows in Webcert, possibly over-engineered for
  simpler apps

---

## 4. Data Fetching Strategy

### 4.1 Dual Strategy: RTK Query + Custom Axios

**Choice:** RTK Query for newer code, custom axios middleware for legacy

**RTK Query Pattern (Minaintyg - Modern):**

```typescript
const {data, isLoading, error} = useGetCertificatesQuery(filterState)
```

**Custom Axios Pattern (Webcert - Legacy):**

```typescript
dispatch(apiCallBegan({
  url: '/certificate/123',
  method: 'GET',
  onSuccess: getCertificateSuccess.type,
  onError: getCertificateError.type,
}))
```

**Cache Invalidation Strategy:**

```typescript
tagTypes: ['User', 'Certificate'],
    providesTags
:
(result) =>
    result ? [
      ...result.content.map(({id}) => ({type: 'Certificate', id})),
      {type: 'Certificate', id: 'LIST'}
    ] : [],
    invalidatesTags
:
(result, error, {id}) =>
    error ? [] : [{type: 'Certificate', id}],
```

**Benefits:**

- Automatic caching and deduplication
- Optimistic updates support
- Background refetching
- CSRF token injection via `prepareHeaders`

**Challenges:**

- Two different error handling patterns
- Different loading state management approaches
- Inconsistent patterns between apps

### 4.2 Mock Service Worker (MSW) for Development

**Choice:** MSW for API mocking in development and tests

**Evidence:**

```typescript
// apps/minaintyg/src/main.tsx
if (import.meta.env.MODE === 'development' && import.meta.env.VITE_USE_MOCKS === 'true') {
  const {worker} = await import('./mocks/browser')
  worker.start()
}

// setupTests.ts
beforeAll(() => {
  server.listen({onUnhandledRequest: 'error'})
})
```

**Test Usage:**

```tsx
// Override mock per test
server.use(
    rest.post('/api/certificate', (_, res, ctx) =>
        res(ctx.status(200), ctx.json({content: []}))
    )
)
```

**Benefits:**

- **Realistic:** Mocks at network level, not function level
- **Reusable:** Same mocks for development and testing
- **Type-safe:** Can validate against Zod schemas
- **Isolation:** No backend dependency for frontend development

**Trade-offs:**

- Additional dependency and setup complexity
- Requires maintaining mock data
- Potential drift between mocks and actual API

---

## 5. Styling Approach

### 5.1 Hybrid CSS Strategy

**Choice:** Tailwind CSS + Styled Components + Legacy CSS

**Component Libraries:**

- `@inera/ids-design` v7.0.0 - Swedish design system
- `@inera/ids-react` v7.0.0 - React components for design system

**Styling Technologies:**

1. **Tailwind CSS** (v3.3.3)
   ```typescript
   // tailwind.config.ts
   export default {
     content: ['./src/**/*.{js,ts,jsx,tsx}'],
     corePlugins: { preflight: false }, // Don't reset styles
     important: true,
   }
   ```

2. **Styled Components** (v5.3.11 - Webcert only)
   ```tsx
   const GroupWrapper = styled.div<Props>`
     ${(props) => props.layout === ConfigLayout.COLUMNS && css`
       column-count: 2;
       gap: 0.9375rem;
     `}
   `
   ```

3. **Legacy CSS** (Webcert)
   ```tsx
   import 'inera-core-css/dist/inera-master.css'
   ```

**Configuration:**

```typescript
// postcss.config.cjs (all apps)
module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
```

**Custom Design Tokens:**

```typescript
// packages/colors/src/index.ts - Shared color palette
export const colors = {
  // Workspace-wide color constants
}

// Consumed in Tailwind config
import {colors} from '@frontend/colors'

export default {
  theme: {colors},
}
```

**Styling Architecture Assessment:**

**Benefits:**

- **Flexibility:** Utility-first (Tailwind) + component-scoped (styled-components)
- **Design System Integration:** IDS components provide consistency
- **Shared Tokens:** Centralized color management
- **Autoprefixer:** Automatic vendor prefix handling

**Challenges:**

- **Complexity:** Three styling paradigms to understand
- **Bundle Size:** Styled-components adds runtime (~13KB)
- **Migration Path:** Webcert has most technical debt with legacy CSS + styled-components
- **Inconsistency:** Different patterns between apps

**Recommendations:**

- **Ideal State:** Standardize on Tailwind + CSS Modules or Tailwind + CSS-in-JS (not both)
- **Migration:** Gradually replace styled-components with Tailwind utilities
- **Custom Components:** Use IDS components where possible for consistency

### 5.2 Responsive Design Approach

**Choice:** Mobile-first with custom breakpoints

**Evidence:**

```typescript
// tailwind.config.ts
theme: {
  screens: {
    print: {
      raw: 'print'
    }
  ,
    sm: '480px',
        md
  :
    '800px',
        lg
  :
    '940px',
        xl
  :
    '1280px',
  }
,
}
```

**Usage:**

```tsx
<div className="max-w-screen-lg">
  {/* Responsive layout */}
</div>
```

**Benefits:**

- Custom breakpoints aligned with healthcare domain needs
- Print media support for certificate printing
- Consistent responsive behavior across apps

---

## 6. Testing Strategy

### 6.1 Vitest as Test Runner

**Choice:** Vitest (v3.2.4) replacing Jest

**Configuration:**

```typescript
// vitest.config.ts
export default defineProject({
  test: {
    globals: true,
    environment: 'jsdom',
    include: ['./src/**/*.{test,spec}.?(c|m)[jt]s?(x)'],
    setupFiles: ['src/setupTests.ts'],
    coverage: {
      reporter: ['text', 'json', 'lcov'],
      thresholds: {
        branches: 80,
        lines: 80,
        functions: 75,
        statements: 80,
      },
    },
  },
})
```

**Workspace Configuration:**

```json
// vitest.workspace.json
[
  "apps/*",
  "packages/*"
]
```

**Benefits:**

- **Performance:** 10-20x faster than Jest for this codebase
- **Vite Integration:** Uses same config/transforms as dev server
- **ESM Native:** First-class ES modules support
- **Monorepo Support:** Built-in workspace awareness
- **HMR for Tests:** Instant test reruns during development

**Trade-offs:**

- Newer tool (less ecosystem maturity than Jest)
- Different API than Jest (migration effort)
- Less widespread team knowledge

### 6.2 Testing Library Ecosystem

**Choice:** React Testing Library + jest-dom + user-event

**Dependencies:**

```json
{
  "@testing-library/react": "^16.3.0",
  "@testing-library/jest-dom": "^6.9.0",
  "@testing-library/user-event": "^14.6.1",
  "@testing-library/dom": "^10.4.0"
}
```

**Test Patterns:**

```tsx
// Component test example
it('Should expand when "Visa mer" is pressed', async () => {
  render(<ExpandableText text="a b c" maxLength={3}/>)
  await userEvent.click(screen.getByRole('button', {name: 'Visa mer'}))
  expect(screen.getByText('a b c')).toBeInTheDocument()
})
```

**Best Practices Applied:**

- **Accessibility-first queries:** `getByRole`, `getByLabelText`
- **User-centric testing:** `user-event` instead of `fireEvent`
- **Async utilities:** `waitFor`, `findBy*` for async operations
- **No implementation details:** Testing behavior, not implementation

**Benefits:**

- Encourages accessibility (tests fail if ARIA is wrong)
- Mimics real user interactions
- Catches more meaningful bugs
- Aligns with modern React testing philosophy

### 6.3 MSW for API Mocking

**Choice:** Mock Service Worker for network-level mocking

**Setup:**

```typescript
// setupTests.ts
import {server} from './mocks/server'

beforeAll(() => server.listen({onUnhandledRequest: 'error'}))
afterEach(() => server.resetHandlers())
afterAll(() => server.close())
```

**Per-test Overrides:**

```tsx
it('Should render alert when list is empty', async () => {
  server.use(
      rest.post('/api/certificate', (_, res, ctx) =>
          res(ctx.status(200), ctx.json({content: []}))
      )
  )
  // Test implementation
})
```

**Benefits:**

- **Realistic:** Tests actual fetch/axios calls
- **Reusable:** Same handlers for dev and tests
- **Type-safe:** Integrated with Zod schemas
- **Debugging:** Network tab shows actual requests

### 6.4 Coverage Thresholds

**Choice:** Enforced 75-80% coverage minimums

**Thresholds:**

- Branches: 80%
- Lines: 80%
- Functions: 75%
- Statements: 80%

**CI Integration:**

```json
{
  "test:ci": "vitest run --reporter=junit --coverage"
}
```

**Assessment:**

- **Appropriate:** High enough to catch issues, not excessive
- **Pragmatic:** Functions at 75% allows for some untestable code
- **Enforcement:** Blocks CI/CD pipeline on violations

### 6.5 Playwright for E2E Testing

**Choice:** Playwright (v1.53.0) for end-to-end tests (Webcert only)

**Evidence:**

```json
{
  "test:playwright": "playwright test"
}
```

**Implications:**

- **Mature Application:** Only Webcert has E2E tests (most critical/complex)
- **Strategy:** Unit/integration tests for most cases, E2E for critical paths
- **Tooling:** Modern alternative to Cypress with better performance

---

## 7. Build Configuration

### 7.1 Vite as Build Tool

**Choice:** Vite (v5.4.21) replacing Webpack

**Configuration:**

```typescript
// vite.config.ts
export default defineConfig({
  plugins: [react()].concat(
      process.env.LEGACY_SUPPORT !== 'false'
          ? legacy({targets: ['defaults', 'not IE 11']})
          : []
  ),
  server: {
    host,
    port: 3000,
    proxy: { /* ... */},
    hmr: { /* ... */},
  },
})
```

**Features:**

- **Dev Server:** Instant HMR using esbuild
- **Production:** Rollup-based optimized builds
- **Legacy Support:** Optional via `@vitejs/plugin-legacy`
- **Proxy:** Development API proxying to backend
- **Environment Variables:** `VITE_*` prefix pattern

**Benefits:**

- **Speed:** 10-100x faster than Webpack for dev server startup
- **Simple Config:** Sensible defaults, minimal configuration needed
- **Modern:** Native ESM, optimized for HTTP/2
- **Plugin Ecosystem:** Vite + Rollup plugins available

**Trade-offs:**

- Newer tool (less Stack Overflow solutions than Webpack)
- Some Webpack-specific plugins may not have Vite equivalents
- Legacy browser support requires additional plugin

### 7.2 TypeScript Configuration

**Choice:** Strict TypeScript with workspace-wide configuration

**Root Config:**

```jsonc
// tsconfig.json
{
  "compilerOptions": {
    "target": "esnext",
    "module": "esnext",
    "strict": true,
    "strictNullChecks": true,
    "noUncheckedIndexedAccess": true,
    "paths": {
      "@frontend/colors": ["./packages/colors/src/index.ts"],
      "@frontend/components": ["./packages/components/src/index.ts"],
      // ...
    }
  }
}
```

**Key Settings:**

- **`strict: true`:** All type-checking strictness flags enabled
- **`noUncheckedIndexedAccess`:** Array/object access returns `T | undefined`
- **`paths`:** Workspace package aliases for clean imports
- **`noEmit: true`:** Type-checking only (Vite handles compilation)

**Benefits:**

- **Type Safety:** Catches nullability and undefined access errors
- **Refactoring:** Safe large-scale refactors with compiler guarantees
- **Documentation:** Types serve as inline documentation
- **IDE Support:** Excellent autocomplete and error detection

**Trade-offs:**

- More verbose code with explicit type annotations
- Steeper learning curve for TypeScript beginners
- Slower initial development (pays off long-term)

### 7.3 pnpm Workspace Architecture

**Choice:** pnpm (v9.11.0) with workspace protocol

**Configuration:**

```yaml
# pnpm-workspace.yaml
packages:
  - 'apps/*'
  - 'packages/*'
  - 'e2e'
```

**Dependency Management:**

```json
{
  "dependencies": {
    "@frontend/components": "workspace:*",
    "@frontend/utils": "workspace:*"
  }
}
```

**Benefits:**

- **Disk Space:** Single copy of each package version (symlinked)
- **Speed:** Faster installs than npm/yarn (parallel, cached)
- **Strict:** No phantom dependencies (only declared deps available)
- **Monorepo Native:** Workspace protocol for inter-package deps

**Overrides Strategy:**

```json
{
  "pnpm": {
    "overrides": {
      "tough-cookie@<4.1.3": ">=4.1.3",
      "@babel/traverse@<7.23.2": ">=7.23.2"
      // Security patches
    }
  }
}
```

### 7.4 Legacy Browser Support

**Choice:** Optional IE11+ support via plugin

**Implementation:**

```typescript
// vite.config.ts
plugins: [react()].concat(
    process.env.LEGACY_SUPPORT !== 'false'
        ? legacy({targets: ['defaults', 'not IE 11']})
        : []
)
```

**Browserslist Config:**

```json
{
  "browserslist": [
    ">0.3%",
    "last 2 versions",
    "not dead",
    "Firefox ESR",
    "not op_mini all",
    "not kaios > 0"
  ]
}
```

**Implications:**

- **Healthcare Domain:** May require support for older institutional browsers
- **Optional:** Can be disabled for faster dev builds
- **Trade-off:** Larger bundle size with polyfills when enabled

---

## 8. Performance Patterns

### 8.1 Minimal Memoization Strategy

**Choice:** No aggressive optimization, profile-driven approach

**Evidence:**

- No `React.memo` usage
- Limited `useMemo` (8 instances total)
- No `useCallback` usage

**Pattern Example:**

```typescript
// Only used where genuinely needed
const suggestions = useMemo(
    () => diagnosticCodes.filter(/* expensive filter */),
    [dependencies]
)
```

**Philosophy:**

- **Premature Optimization Avoided:** Only optimize when profiling shows issues
- **Simplicity Prioritized:** Readable code over micro-optimizations
- **Trade-off Accepted:** Some unnecessary re-renders vs. complexity

**Assessment:**

- **Appropriate for:** Medium-scale apps without extreme performance needs
- **Risk:** May require optimization work if lists grow large
- **Mitigation:** Vitest performance testing, React DevTools profiling

### 8.2 Code Splitting Approach

**Choice:** Route-based splitting (implicit via Vite)

**Evidence:**

- No manual `lazy()` imports found
- Vite automatically code-splits by route
- Dynamic imports for mocks: `await import('./mocks/browser')`

**Implications:**

- **Automatic:** Vite splits per-route without configuration
- **Missed Opportunity:** Could add manual splitting for large components
- **Bundle Analysis:** No evidence of bundle size monitoring (could add `rollup-plugin-visualizer`)

### 8.3 Asset Optimization

**Choice:** Vite's default optimizations

**Features:**

- **CSS:** PostCSS with autoprefixer
- **Images:** Automatic optimization (Vite built-in)
- **Tree Shaking:** Rollup-based dead code elimination
- **Minification:** esbuild for JS/CSS minification

---

## 9. Error Handling

### 9.1 Error Boundaries

**Choice:** React Router error boundaries per route

**Implementation:**

```tsx
// Minaintyg pattern
export function ErrorBoundary() {
  const error = useRouteError()
  const [logError] = useLogErrorMutation()

  useEffect(() => {
    if (error instanceof Error && hasSession) {
      logError({
        id: randomUUID(),
        code: ErrorCode.enum.CLIENT_ERROR,
        message: error.message,
        stackTrace: error.stack ?? 'NO_STACK_TRACE',
      })
    }
  }, [error])

  return <ErrorPageHero id={id} type="unknown"/>
}

// Route configuration
<Route
    path="/"
    errorElement={<ErrorBoundary/>}
    element={<Layout/>}
/>
```

**Webcert Pattern (React Error Boundary library):**

```tsx
import {ErrorBoundary} from 'react-error-boundary'

function ErrorBoundaryWrapper() {
  const dispatch = useAppDispatch()

  const onError = (error: Error) => {
    dispatch(throwError(
        createErrorRequest(ErrorType.ROUTE, ErrorCode.UNEXPECTED_ERROR, error.message)
    ))
  }

  return <ErrorBoundary fallbackRender={ErrorMessage} onError={onError}/>
}
```

**Benefits:**

- **Graceful Degradation:** App doesn't crash entirely
- **User Experience:** Friendly error page instead of blank screen
- **Logging:** Automatic error reporting to backend
- **Recovery:** User can navigate away from error

**Strategy:**

- **Route-level:** Isolates errors to specific features
- **Automatic Logging:** All errors sent to backend with stack trace
- **Session-aware:** Only logs if user is authenticated

### 9.2 Redux Error State Management

**Choice:** Dedicated error slice with middleware

**Webcert Pattern:**

```typescript
// Error actions
dispatch(throwError(createErrorRequest(
    ErrorType.ROUTE,
    ErrorCode.UNEXPECTED_ERROR,
    error.message,
    error.stack
)))

// Error middleware handles logging
export const errorMiddleware: Middleware = (middlewareAPI) => (next) => (action) => {
  if (throwError.match(action)) {
    // Log to backend
    // Show toast notification
  }
}
```

**Benefits:**

- Centralized error handling logic
- Consistent error display (toasts, modals)
- Testable error scenarios

**Trade-offs:**

- Adds complexity vs. throwing errors directly
- Redux overkill for simple error messages

---

## 10. Input Validation

### 10.1 Zod for Runtime Validation

**Choice:** Zod schemas for API responses and form validation

**Schema Definition:**

```typescript
// apps/minaintyg/src/schema/certificate.schema.ts
export const certificateMetadataSchema = z.object({
  issuer: certificateIssuerSchema,
  unit: certificateUnitSchema,
  events: z.array(certificateEventSchema),
  id: z.string(),
  issued: z.string().datetime(),
  recipient: certificateRecipientSchema.nullable(),
})

export type CertificateMetadata = z.infer<typeof certificateMetadataSchema>
```

**Webcert Form Validation:**

```typescript
// apps/webcert/src/store/pp/ppStep02ReducerSlice.ts
const zodError = step02FormDataSchema.safeParse(state.data).error
let errors = zodError ? z.flattenError(zodError).fieldErrors : undefined
```

**Integration with Testing:**

```typescript
// Generate fake data from schemas
import {fakerFromSchema} from '@frontend/fake'

const mockCertificate = fakerFromSchema(certificateMetadataSchema)({
  id: 'custom-id',
  // Override specific fields
})
```

**Benefits:**

- **Type Safety:** TypeScript types derived from schemas
- **Runtime Validation:** Catches API contract violations
- **Self-documenting:** Schema is source of truth
- **Testing Integration:** Generate realistic mock data
- **Parsing:** `.parse()` throws on invalid data, `.safeParse()` returns result

**Trade-offs:**

- Bundle size increase (~10KB gzipped)
- Schema duplication if backend has similar validation
- Learning curve for Zod API

**Assessment:** Excellent choice for this use case - healthcare data requires strict validation

---

## 11. Logging and Observability

### 11.1 Backend Error Logging

**Choice:** Centralized error logging via API endpoint

**Implementation:**

```typescript
export const api = createApi({
  endpoints: (builder) => ({
    logError: builder.mutation<void, ErrorLogRequest>({
      query: (error) => ({
        url: 'log/error',
        method: 'POST',
        body: error,
      }),
    }),
  }),
})
```

**Error Structure:**

```typescript
interface ErrorLogRequest {
  id: string
  code: ErrorCode
  message: string
  stackTrace?: string
}
```

**Benefits:**

- Server-side log aggregation
- Production error monitoring
- Stack traces for debugging

**Limitations:**

- No client-side logging library (e.g., Sentry, LogRocket)
- No performance monitoring
- No user session replay

**Recommendation:** Consider adding Sentry/DataDog for richer observability

### 11.2 Session Management

**Choice:** Polling-based session monitoring

**Implementation:**

```typescript
// Middleware checks session status periodically
export const sessionMiddleware: Middleware = (middlewareAPI) => (next) => (action) => {
  if (sessionTimeout) {
    dispatch(showSessionWarning())
  }
}
```

**Trade-offs:**

- Simple to implement but creates background requests
- Could use WebSockets for more efficient session management

---

## 12. Security Considerations

### 12.1 CSRF Protection

**Choice:** XSRF token in request headers

**Implementation:**

```typescript
// RTK Query
prepareHeaders: (headers) => {
  headers.set('X-XSRF-TOKEN', getCookie('XSRF-TOKEN') ?? '')
  return headers
}

// Custom axios
headers: {
  'X-XSRF-TOKEN'
:
  getCookie('XSRF-TOKEN') ?? ''
}
```

**Benefits:**

- Prevents cross-site request forgery
- Standard Spring Security pattern

### 12.2 Dependency Security

**Choice:** pnpm overrides for security patches

**Evidence:**

```json
{
  "pnpm": {
    "overrides": {
      "tough-cookie@<4.1.3": ">=4.1.3",
      "@babel/traverse@<7.23.2": ">=7.23.2",
      "semver@>=7.0.0 <7.5.2": ">=7.5.2",
      "follow-redirects@<1.15.4": ">=1.15.4"
    }
  }
}
```

**Benefits:**

- Forces secure versions across all dependencies
- Mitigates known CVEs
- Centralized security patch management

**Recommendation:** Add automated dependency scanning (Dependabot, Snyk)

### 12.3 Content Security

**Choice:** DOMPurify for HTML sanitization

**Evidence:**

```json
{
  "dependencies": {
    "dompurify": "^3.2.4",
    "html-react-parser": "^4.2.2"
  }
}
```

**Usage Pattern:**

```tsx
import parse from 'html-react-parser'
import DOMPurify from 'dompurify'

const SafeHtml = ({html}) => (
    <div>{parse(DOMPurify.sanitize(html))}</div>
)
```

**Benefits:**

- Prevents XSS attacks from user-generated content
- Sanitizes HTML from backend
- Essential for healthcare applications with strict security requirements

---

## 13. Code Quality and Standards

### 13.1 ESLint Configuration

**Choice:** Airbnb style guide + custom rules

**Workspace-wide:**

```javascript
// packages/eslint-config-custom/index.js
extends:
[
  'eslint:recommended',
  'airbnb-base',
  'plugin:@typescript-eslint/recommended',
  'plugin:compat/recommended',
  'prettier',
]
```

**React-specific:**

```javascript
// packages/eslint-config-react/index.js
extends:
[
  'airbnb',
  'airbnb-typescript',
  'plugin:react/recommended',
  'plugin:react-hooks/recommended',
  'plugin:jsx-a11y/recommended',
]
```

**Custom Rules:**

- **No default exports:** `'import/no-default-export': 'error'`
- **Typed hooks only:** Prevents use of untyped `useDispatch`/`useSelector`
- **React rules:** `'react/no-multi-comp': 'error'`

**Benefits:**

- Consistent code style across team
- Catches common bugs (missing dependencies in hooks)
- Enforces accessibility (jsx-a11y)
- TypeScript best practices

### 13.2 Prettier Integration

**Choice:** Prettier for code formatting

**Evidence:**

```json
{
  "lint-staged": {
    "*": "prettier --write --ignore-unknown"
  }
}
```

**Benefits:**

- Zero config formatting
- No style debates (Prettier's opinion)
- Auto-format on commit (lint-staged)

### 13.3 Husky + Lint-Staged

**Choice:** Pre-commit hooks for quality gates

**Configuration:**

```json
{
  "lint-staged": {
    "*": "prettier --write --ignore-unknown",
    "*.{js,jsx,ts,tsx}": [
      "eslint --fix",
      "pnpm test -- related --run"
    ]
  }
}
```

**Enforcement:**

- Format all files with Prettier
- Lint and auto-fix TypeScript/JavaScript
- Run tests related to changed files

**Benefits:**

- Prevents committing broken code
- Consistent formatting across team
- Fast feedback loop (only changed tests)

**Trade-offs:**

- Slower commits (offset by only testing related files)
- Requires team buy-in (can be bypassed with `--no-verify`)

### 13.4 Deprecation Detection

**Choice:** ESLint plugin for deprecated APIs

**Evidence:**

```json
{
  "devDependencies": {
    "eslint-plugin-deprecation": "^2.0.0"
  }
}
```

**Benefits:**

- Proactive detection of deprecated React APIs
- Prevents use of deprecated library methods
- Eases future upgrades

---

## 14. Monorepo Architecture

### 14.1 Workspace Structure

**Choice:** Apps + packages separation

**Structure:**

```
apps/
  webcert/         - Main certificate management app
  minaintyg/       - Patient-facing app
  rehabstod/       - Rehabilitation support app
  maintenance/     - Maintenance mode app
packages/
  components/      - Shared React components
  colors/          - Design tokens
  utils/           - Utility functions
  fake/            - Testing utilities (MSW, Zod mocks)
  theme-1177/      - 1177 specific theme
  filtrex/         - Custom filter library
  eslint-config-*/ - Shared ESLint configs
```

**Benefits:**

- **Code Reuse:** Shared components across 3+ apps
- **Consistency:** Centralized design tokens and configs
- **Independent Deployment:** Each app can be deployed separately
- **Type Safety:** TypeScript resolves workspace packages

### 14.2 Shared Component Library

**Choice:** `@frontend/components` package with Rollup build

**Build Configuration:**

```json
{
  "name": "@frontend/components",
  "main": "./dist/index.js",
  "module": "./dist/index.js",
  "exports": {
    ".": {
      "require": "./dist/index.cjs",
      "import": "./dist/index.js"
    }
  }
}
```

**Build Tool:**

```javascript
// rollup.config.js
export default {
  input: 'src/index.ts',
  output: [
    {file: 'dist/index.js', format: 'esm'},
    {file: 'dist/index.cjs', format: 'cjs'}
  ],
  plugins: [esbuild(), styles()]
}
```

**Benefits:**

- **Dual Format:** ESM + CJS for compatibility
- **Fast Builds:** Rollup + esbuild plugin
- **Tree Shaking:** Consumers only bundle what they import

### 14.3 Centralized Fake Data Generation

**Choice:** `@frontend/fake` package with Zod integration

**Implementation:**

```typescript
export function fakerFromSchema<T extends ZodTypeAny>(
    schema: T,
    options?: FakeFromSchemaOptions
) {
  return (data?: DeepPartial<z.infer<T>>): z.infer<T> =>
      data ? customDeepmerge(generateMock(schema, options), data)
          : generateMock(schema, options)
}
```

**Usage:**

```typescript
// Generate realistic test data
const mockCertificate = fakerFromSchema(certificateMetadataSchema)({
  id: 'test-123',
  // Other fields auto-generated
})
```

**Benefits:**

- **Type-Safe Mocks:** Automatically match schema
- **Realistic Data:** Uses Swedish locale (names, addresses)
- **Reusable:** Same fake data for tests and development

**Custom String Maps:**

```typescript
export const stringMap = {
  hsaId: fakeHSA,              // Swedish healthcare IDs
  personId: fakePersonId,      // Swedish person numbers
  namn: faker.name.fullName,   // Swedish names
  stad: faker.address.city,    // Swedish cities
}
```

---

## 15. Deployment and DevOps

### 15.1 Docker Containerization

**Choice:** Nginx-based Docker containers

**Dockerfile:**

```dockerfile
# Build artifacts copied to Nginx
WORKDIR /usr/share/nginx/html
COPY /$application_path/dist/ .

# Custom Nginx config with templates
COPY /$application_path/nginx/templates /etc/nginx/templates/
COPY /$application_path/nginx/conf/nginx.conf /etc/nginx/nginx.conf

# Non-root user
RUN chown -R nginx:nginx /var/cache/nginx /var/run/nginx.pid
USER nginx

EXPOSE 8080
```

**Benefits:**

- **Static Hosting:** React apps served via Nginx
- **Security:** Non-root user (nginx:nginx)
- **Configuration:** Template-based Nginx config
- **Standardized:** Same pattern across all apps

**Implications:**

- **SPA Routing:** Nginx must handle React Router (fallback to index.html)
- **Environment Variables:** Baked into build, not runtime (consider runtime config endpoint)

### 15.2 Multi-App Docker Compose

**Choice:** docker-compose.yml for local development

**Benefits:**

- Run all apps + backend together
- Simulates production environment locally
- Consistent across team

### 15.3 CI/CD Integration

**Evidence:**

```json
{
  "test:ci": "vitest run --reporter=junit --coverage"
}
```

**Jenkins Properties Files:**

- Each app has `Jenkins.properties` for CI configuration
- Likely integrated with Jenkins pipeline

**Quality Gates:**

- Coverage thresholds enforced (75-80%)
- Lint must pass
- Tests must pass
- TypeScript compilation must succeed

---

## 16. Accessibility

### 16.1 Design System Integration

**Choice:** IDS (Inera Design System) components

**Evidence:**

```json
{
  "@inera/ids-design": "^7.0.0",
  "@inera/ids-react": "^7.0.0"
}
```

**Benefits:**

- **WCAG Compliance:** Built-in accessibility
- **Consistency:** Standardized Swedish public sector UI
- **Maintained:** Updates handled by IDS team

### 16.2 ESLint Accessibility Checking

**Choice:** jsx-a11y plugin

**Evidence:**

```javascript
extends:
[
  'plugin:jsx-a11y/recommended'
]
```

**Benefits:**

- Catches accessibility issues at development time
- Enforces ARIA attributes
- Validates semantic HTML

### 16.3 Testing Library Accessibility Focus

**Philosophy:** Queries by accessibility attributes

**Pattern:**

```tsx
// Good: Query by role (accessible)
screen.getByRole('button', {name: 'Submit'})

// Avoid: Query by test ID (not accessible)
screen.getByTestId('submit-button')
```

**Benefits:**

- Tests fail if ARIA is missing
- Encourages accessible component design

---

## 17. Internationalization (i18n)

### 17.1 Minimal i18n Strategy

**Choice:** Swedish-only, hardcoded strings

**Evidence:**

- No react-intl or i18next library
- Swedish strings in components
- `setDefaultOptions({ locale: sv })` for date-fns

**Implications:**

- **Appropriate:** Healthcare domain is Swedish-only
- **Limitation:** Cannot easily expand to other languages
- **Simplicity:** No translation management overhead

**Date Localization:**

```tsx
import {sv} from 'date-fns/locale'

setDefaultOptions({locale: sv})

// Minaintyg also uses React Aria i18n
< I18nProvider
locale = "sv-SE" >
```

**Assessment:** Pragmatic choice for single-language application

---

## 18. Key Strengths

1. **Modern React Patterns:** Hooks, functional components, latest React Router
2. **Type Safety:** Strict TypeScript, Zod runtime validation, typed Redux
3. **Testing Culture:** High coverage thresholds, RTL best practices, MSW for realistic mocks
4. **Monorepo Benefits:** Code sharing, consistent tooling, centralized configs
5. **Developer Experience:** Vite for fast builds, Vitest for fast tests, ESLint/Prettier for
   consistency
6. **Security Focus:** CSRF protection, DOMPurify, dependency overrides
7. **Production Ready:** Docker containers, CI/CD integration, error boundaries

---

## 19. Areas for Improvement

### 19.1 Styling Consistency

**Issue:** Three different styling approaches (Tailwind, styled-components, legacy CSS)

**Recommendation:**

- Standardize on Tailwind + CSS Modules
- Migrate away from styled-components (runtime overhead)
- Remove legacy CSS dependencies

### 19.2 Data Fetching Unification

**Issue:** Webcert uses both RTK Query and custom axios middleware

**Recommendation:**

- Migrate legacy API calls to RTK Query endpoints
- Remove custom apiMiddleware once fully migrated
- Document migration strategy

### 19.3 Performance Monitoring

**Issue:** No client-side performance monitoring

**Recommendation:**

- Add Sentry or LogRocket for error tracking
- Implement Web Vitals monitoring
- Add bundle size tracking in CI

### 19.4 Code Splitting Optimization

**Issue:** No manual code splitting beyond route-based

**Recommendation:**

- Add `lazy()` for large components (e.g., charts, modals)
- Analyze bundle size with `rollup-plugin-visualizer`
- Consider dynamic imports for rarely-used features

### 19.5 Environment Variable Strategy

**Issue:** Environment variables baked into build

**Recommendation:**

- Consider runtime config endpoint for environment-specific values
- Allows same Docker image across environments
- More flexible deployment

### 19.6 Navigation Abstraction

**Issue:** Custom Redux navigation slice adds complexity

**Recommendation:**

- Evaluate if complexity is justified
- Consider removing for simpler apps (minaintyg, rehabstod)
- Use standard `useNavigate()` unless strong reason

---

## 20. Technology Stack Summary

### Core Technologies

- **React:** 18.3.1
- **TypeScript:** 5.9.2
- **Redux Toolkit:** 1.9.7 (Webcert) / 2.8.2 (Minaintyg/Rehabstod)
- **React Router:** 6.30.1

### Build & Dev Tools

- **Build Tool:** Vite 5.4.21
- **Package Manager:** pnpm 9.11.0
- **Test Runner:** Vitest 3.2.4
- **E2E Testing:** Playwright 1.53.0

### Styling

- **Tailwind CSS:** 3.3.3
- **Styled Components:** 5.3.11 (Webcert)
- **Design System:** @inera/ids-design 7.0.0

### Data Fetching

- **RTK Query:** Built into Redux Toolkit
- **Axios:** 1.12.0 (Webcert legacy)

### Validation & Testing

- **Zod:** 3.22.3 / 4.1.12
- **React Testing Library:** 16.3.0
- **MSW:** 1.2.0 (Minaintyg)
- **DOMPurify:** 3.2.4

### Code Quality

- **ESLint:** 8.56.0
- **Prettier:** 3.1.1
- **Husky:** 7.0.4

---

## Conclusion

The Intyg frontend repository demonstrates a **mature, production-grade React architecture** with
strong emphasis on:

1. **Type safety** (TypeScript + Zod)
2. **Testing culture** (high coverage, RTL, MSW)
3. **Developer experience** (Vite, Vitest, monorepo)
4. **Code quality** (ESLint, Prettier, pre-commit hooks)
5. **Modern patterns** (Hooks, RTK Query, React Router v6)

The main **architectural debt** lies in Webcert's dual data-fetching strategies (RTK Query + custom
axios middleware) and mixed styling approaches (Tailwind + styled-components + legacy CSS). These
reflect the application's evolution over time and represent clear migration paths for future work.

The newer applications (Minaintyg, Rehabstod) demonstrate cleaner patterns with RTK Query, simpler
middleware chains, and more consistent Tailwind usage, suggesting the team is successfully learning
from past decisions.

**Overall Assessment:** This is a well-architected React monorepo that balances pragmatism with best
practices, making it suitable for a complex healthcare domain with strict security and reliability
requirements.

