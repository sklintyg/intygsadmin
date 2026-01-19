# Vite Usage Documentation

This document provides a comprehensive analysis of how **Vite** is used in the Intyg Frontend
repository as the primary build tool and development server for React applications in this monorepo
structure.

## Overview

**Vite** (version ^5.4.21) serves as the modern build tool and development server for all frontend
applications in this pnpm workspace monorepo. Vite replaces traditional bundlers like Webpack by
leveraging native ES modules during development and Rollup for optimized production builds.

### Key Benefits of Vite in This Repository

1. **Lightning-fast cold starts** - Native ES modules eliminate bundling during development
2. **Instant Hot Module Replacement (HMR)** - Updates reflect immediately without full page reloads
3. **Optimized production builds** - Rollup-based builds with automatic code splitting
4. **TypeScript support out-of-the-box** - No additional configuration needed
5. **CSS preprocessing** - Built-in support for PostCSS and Tailwind CSS
6. **Monorepo-friendly** - Works seamlessly with pnpm workspaces

## Repository Structure

The repository contains multiple applications, each with its own Vite configuration:

```
frontend/
├── apps/
│   ├── webcert/           # Main certificate management app (Port 3000)
│   ├── rehabstod/         # Rehabilitation support app (Port 5173)
│   ├── minaintyg/         # Patient portal app (Port 5174)
│   └── maintenance/       # Static maintenance pages (Port 5175, MPA)
├── packages/
│   ├── components/        # Shared React components
│   ├── theme-1177/        # Theme package
│   ├── utils/             # Utility functions
│   └── ...
└── vitest.workspace.json  # Vitest testing workspace configuration
```

## Vite Configuration Patterns

### 1. Single Page Application (SPA) Pattern

Most applications (`webcert`, `rehabstod`, `minaintyg`) use the standard SPA pattern with React.

#### Example: `apps/webcert/vite.config.ts`

```typescript
import legacy from '@vitejs/plugin-legacy'
import react from '@vitejs/plugin-react'
import type {ProxyOptions, UserConfig} from 'vite'
import {defineConfig, loadEnv} from 'vite'

export default ({mode}: UserConfig) => {
  // Load environment variables based on mode (development, production, test)
  process.env = {...process.env, ...loadEnv(mode ?? 'development', process.cwd())}

  const hmr = !(process.env.VITE_HMR === 'false')
  const host = process.env.VITE_HOST ?? 'localhost'

  // Configure API proxy for backend communication
  const proxy = [
    '/fake',
    '/api',
    '/moduleapi',
    '/testability',
    '/visa',
    '/v2',
    '/webcert',
    '/saml2',
    '/error.jsp',
    '/logout',
    '/login',
  ].reduce<Record<string, string | ProxyOptions>>(
      (result, route) => ({
        ...result,
        [route]: {
          secure: false,
          target: process.env.VITE_API_TARGET ?? 'https://webcert-devtest.intyg.nordicmedtest.se',
          cookieDomainRewrite: {'*': ''},
          protocolRewrite: 'https',
          changeOrigin: true,
          autoRewrite: true,
        },
      }),
      {}
  )

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
            host: process.env.VITE_WS_HOST ?? 'wc.localtest.me',
            protocol: process.env.VITE_WS_PROTOCOL ?? 'ws',
          }
          : false,
    },
  })
}
```

**Key Configuration Elements:**

- **Environment-based configuration**: Uses `loadEnv()` to load `.env` files
- **API Proxy**: Routes API calls to backend servers during development
- **React plugin**: Enables Fast Refresh and JSX transformation
- **Legacy plugin**: Generates polyfills for older browsers (optional)
- **Flexible HMR**: Can be disabled for testing/CI environments
- **Custom host/port**: Each app runs on different ports to enable parallel development

### 2. Multi-Page Application (MPA) Pattern

The `maintenance` app uses Vite's multi-page application support for generating multiple static HTML
pages.

#### Example: `apps/maintenance/vite.config.ts`

```typescript
import legacy from '@vitejs/plugin-legacy'
import path, {resolve} from 'path'
import {defineConfig} from 'vite'

const root = resolve(__dirname, 'src')
const outDir = resolve(__dirname, 'dist')

export default defineConfig({
  appType: 'mpa',  // Multi-page application mode
  plugins:
      process.env.LEGACY_SUPPORT !== 'false'
          ? [
            legacy({
              targets: ['defaults', 'not IE 11'],
            }),
          ]
          : [],
  root,
  build: {
    outDir,
    emptyOutDir: true,
    rollupOptions: {
      input: {
        root: resolve(root, 'index.html'),
        general: resolve(root, 'general/index.html'),
        intyg: resolve(root, 'intyg/index.html'),
      },
      onwarn(warning, warn) {
        if (warning.code === 'MODULE_LEVEL_DIRECTIVE') {
          return
        }
        warn(warning)
      },
    },
  },
  resolve: {
    alias: {
      '@inera/ids-design': path.resolve(__dirname, './node_modules/@inera/ids-design'),
    },
  },
  server: {
    host: '0.0.0.0',
    port: 5175,
    allowedHosts: true,
  },
})
```

**Key MPA Features:**

- **appType: 'mpa'**: Tells Vite this is a multi-page application
- **Multiple entry points**: Each HTML file has its own entry point
- **Separate bundles**: Each page generates its own optimized bundle
- **Static output**: Perfect for maintenance pages that need minimal interactivity

## Application Entry Points

Vite uses HTML files as entry points, with ES module script tags pointing to TypeScript/TSX files.

### Standard SPA Entry Pattern

#### `apps/webcert/index.html`

```html
<!doctype html>
<html lang="sv">
<head>
  <meta charset="utf-8"/>
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,500,700" rel="stylesheet"/>
  <link href="/favicon.ico" rel="icon"/>
  <meta content="minimum-scale=1, initial-scale=1, width=device-width" name="viewport"/>
  <title>Webcert</title>
</head>
<body>
<noscript>Du måste aktivera JavaScript för att köra den här appen.</noscript>
<div id="root"></div>
<div id="modalRoot"></div>
<!-- Vite entry point - native ES module -->
<script type="module" src="/src/index.tsx"></script>
</body>
</html>
```

#### `apps/webcert/src/index.tsx`

```typescript
import {FloatingDelayGroup} from '@frontend/components'
import {StrictMode} from 'react'
import ReactDOM from 'react-dom/client'
import {Provider} from 'react-redux'
import App from './App'
import './index.css'  // Vite processes this CSS import
import store from './store/store'

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <StrictMode>
        <Provider store = {store} >
    <FloatingDelayGroup delay = {200} >
        <App / >
        </FloatingDelayGroup>
        < /Provider>
        < /StrictMode>
)
```

**How Vite Processes This:**

1. **Development**: Serves `index.tsx` as native ES module, transforming JSX on-the-fly
2. **Build**: Bundles all dependencies, tree-shakes unused code, generates optimized chunks
3. **CSS**: Processes `index.css` with PostCSS/Tailwind, injects during dev, extracts for production

### MPA Entry Pattern

#### `apps/maintenance/src/general/index.html`

```html
<!doctype html>
<html lang="sv">
<head>
  <meta charset="UTF-8"/>
  <title>Planerat underhåll</title>
</head>
<body class="ids">
<div id="root"></div>
<!-- Direct reference to component file -->
<script type="module" src="./general.tsx"></script>
</body>
</html>
```

#### `apps/maintenance/src/general/general.tsx`

```typescript
import {AppLink} from '@frontend/components'
import {PageHero, PageHeroActions} from '@frontend/theme-1177'
import {IDSFooterIneraAdmin, IDSHeaderIneraAdmin} from '@inera/ids-react'
import React from 'react'
import ReactDOM from 'react-dom/client'
import '../styling/inera-admin.css'

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <div className = "flex min-h-screen flex-col" >
    <IDSHeaderIneraAdmin className = "z-40 print:hidden"
brandText = "Intygstjänster" / >
<main className = "relative flex-1" >
<div className = "ids-content mx-auto max-w-screen-md py-24 text-center" >
<PageHero heading = "Planerat underhåll"
type = "error" >
    {/* Maintenance page content */}
    < /PageHero>
    < /div>
    < /main>
    < IDSFooterIneraAdmin
className = "print:hidden" / >
    </div>
    < /React.StrictMode>
)
```

**MPA Benefits:**

- Each page is independently deployable
- No shared state between pages
- Smaller bundle sizes per page
- Perfect for simple, static content pages

## Vite Plugins

### 1. @vitejs/plugin-react

**Purpose**: Official Vite plugin for React support

**Features:**

- **Fast Refresh**: Instant updates during development without losing component state
- **JSX Transformation**: Compiles JSX/TSX to JavaScript
- **Automatic React import**: No need to `import React` in every file (React 17+)
- **Development-only warnings**: Helps catch common mistakes

**Usage in all SPA applications:**

```typescript
import react from '@vitejs/plugin-react'

plugins: [react()]
```

### 2. @vitejs/plugin-legacy

**Purpose**: Generate legacy browser support with polyfills

**Features:**

- Generates separate bundles for modern and legacy browsers
- Automatic polyfill injection for older browsers
- Modern browsers get optimized, smaller bundles
- Legacy browsers (not IE 11) get polyfilled versions

**Configuration:**

```typescript
import legacy from '@vitejs/plugin-legacy'

plugins: [
  react(),
  process.env.LEGACY_SUPPORT !== 'false'
      ? legacy({
        targets: ['defaults', 'not IE 11'],
      })
      : []
]
```

**Targets Explanation:**

- `'defaults'`: Browsers with >0.5% market share, not dead
- `'not IE 11'`: Explicitly exclude IE 11 (unsupported)

**Can be disabled** by setting `LEGACY_SUPPORT=false` environment variable for faster builds in
development.

## Environment Configuration

Vite uses a sophisticated environment variable system with `.env` files.

### Environment File Hierarchy

Vite loads environment files in this order (later files override earlier ones):

1. `.env` - Loaded in all cases
2. `.env.local` - Loaded in all cases, ignored by git
3. `.env.[mode]` - Only loaded in specified mode (e.g., `.env.development`)
4. `.env.[mode].local` - Only loaded in specified mode, ignored by git

### Environment Variables in This Repository

#### Webcert Application

**`.env.development`** (Default development settings):

```
VITE_API_TARGET=https://webcert-devtest.intyg.nordicmedtest.se
VITE_HOST=wc.localtest.me
```

**`.env.development.local`** (Local override, not in git):

```
VITE_HTTPS=false
VITE_API_TARGET=http://localhost:8020
VITE_HOST=0.0.0.0
```

**`.env.test`** (CI/Test environment):

```
VITE_API_TARGET=https://webcert-devtest.intyg.nordicmedtest.se
VITE_HOST='127.0.0.1'
VITE_HMR=false
VITE_WATCH=false
```

#### Rehabstod Application

**`.env.development`**:

```
VITE_API_TARGET=https://rehabstod-devtest.intyg.nordicmedtest.se
VITE_HOST=rs.localtest.me
```

**`.env.development.local`**:

```
VITE_HTTPS=false
VITE_API_TARGET=http://localhost:8030
VITE_HOST=0.0.0.0
```

#### Minaintyg Application

**`.env`** (All modes):

```
VITE_LOGIN_URL='/saml2/authenticate/eleg'
```

**`.env.development`**:

```
VITE_API_TARGET=https://minaintyg-devtest.intyg.nordicmedtest.se
VITE_HOST=mi2.mi.localtest.me
VITE_LOGIN_URL='/welcome'
```

### Using Environment Variables in Config

```typescript
export default ({mode}: UserConfig) => {
  // Load environment variables
  process.env = {...process.env, ...loadEnv(mode ?? 'development', process.cwd())}

  // Use variables with fallback defaults
  const hmr = !(process.env.VITE_HMR === 'false')
  const host = process.env.VITE_HOST ?? 'localhost'

  return defineConfig({
    server: {
      host,
      proxy: {
        '/api': {
          target: process.env.VITE_API_TARGET ?? 'https://default-backend.com',
        }
      }
    }
  })
}
```

### Accessing Environment Variables in Application Code

In application code, environment variables are available via `import.meta.env`:

```typescript
// Access variables prefixed with VITE_
const apiUrl = import.meta.env.VITE_API_TARGET
const loginUrl = import.meta.env.VITE_LOGIN_URL

// Built-in Vite variables
const isDev = import.meta.env.DEV        // true in development
const isProd = import.meta.env.PROD      // true in production
const mode = import.meta.env.MODE        // 'development', 'production', 'test'
```

**Security Note**: Only variables prefixed with `VITE_` are exposed to client code. This prevents
accidentally exposing secrets.

## Development Server Features

### Port Assignment Strategy

Each application runs on a different port to enable parallel development:

| Application | Port | Host Default        | Purpose                  |
|-------------|------|---------------------|--------------------------|
| webcert     | 3000 | wc.localtest.me     | Main certificate app     |
| rehabstod   | 5173 | rs.localtest.me     | Rehabilitation support   |
| minaintyg   | 5174 | mi2.mi.localtest.me | Patient portal           |
| maintenance | 5175 | 0.0.0.0             | Static maintenance pages |

### API Proxy Configuration

Vite's dev server can proxy API requests to backend servers, avoiding CORS issues during
development.

#### Webcert Proxy Configuration

```typescript
const proxy = [
  '/fake',        // Fake data endpoints
  '/api',         // REST API
  '/moduleapi',   // Module-specific API
  '/testability', // Test endpoints
  '/visa',        // View endpoints
  '/v2',          // API v2
  '/webcert',     // Legacy routes
  '/saml2',       // SAML authentication
  '/error.jsp',   // Error pages
  '/logout',      // Logout endpoint
  '/login',       // Login endpoint
].reduce<Record<string, string | ProxyOptions>>(
    (result, route) => ({
      ...result,
      [route]: {
        secure: false,                    // Allow self-signed certificates
        target: process.env.VITE_API_TARGET,
        cookieDomainRewrite: {'*': ''}, // Rewrite cookies for localhost
        protocolRewrite: 'https',         // Rewrite protocol to HTTPS
        changeOrigin: true,               // Change origin header
        autoRewrite: true,                // Automatically rewrite location header
      },
    }),
    {}
)
```

**How It Works:**

1. Frontend makes request to `http://localhost:3000/api/users`
2. Vite intercepts the request (matches `/api` route)
3. Vite forwards to `https://webcert-devtest.intyg.nordicmedtest.se/api/users`
4. Backend responds, Vite forwards response back to frontend
5. Cookies are rewritten for localhost domain

**Benefits:**

- No CORS configuration needed
- Development experience matches production
- Can switch between local backend and remote backend easily
- Preserves authentication cookies

### Hot Module Replacement (HMR)

HMR is Vite's signature feature - instant updates without full page reload.

#### HMR Configuration Options

```typescript
server: {
  hmr: hmr
      ? {
        host: process.env.VITE_WS_HOST ?? 'wc.localtest.me',
        protocol: process.env.VITE_WS_PROTOCOL ?? 'ws',
      }
      : false,
}
```

**HMR in Action:**

1. **Edit a component** - Change text in a React component
2. **Save file** - Vite detects the change
3. **Fast Refresh** - React Fast Refresh updates only that component
4. **State preserved** - Component state is preserved when possible
5. **Instant feedback** - Change visible in milliseconds

**HMR for Different File Types:**

- **`.tsx/.jsx` files**: React Fast Refresh (preserves state)
- **`.css` files**: Hot update without page reload
- **`.ts` files** (utilities): Reloads importing modules
- **Config files**: Requires server restart

**Disabling HMR:**

Set `VITE_HMR=false` in `.env.test` for CI/E2E testing where HMR isn't needed.

### Strict Port Mode

```typescript
server: {
  strictPort: true,
}
```

**Effect**: If the port is already in use, Vite will fail to start rather than trying the next
available port.

**Benefit**: Prevents confusion when multiple instances try to start on the same port.

### Allowed Hosts

```typescript
server: {
  allowedHosts: true,
}
```

**Effect**: Allows the dev server to be accessed from any hostname.

**Use Case**: Enables access via custom domains like `wc.localtest.me` instead of just `localhost`.

## Module Resolution and Aliases

Vite resolves modules using standard Node.js resolution with custom aliases.

### Path Aliases

```typescript
resolve: {
  alias: {
    '@inera/ids-design'
  :
    path.resolve(__dirname, './node_modules/@inera/ids-design'),
  }
,
}
```

**Purpose**: Explicitly resolve the IDS design system to the local node_modules.

### Workspace Dependencies

The monorepo uses pnpm workspace protocol for internal packages:

```json
{
  "dependencies": {
    "@frontend/components": "workspace:*",
    "@frontend/utils": "workspace:*",
    "@frontend/theme-1177": "workspace:*"
  }
}
```

**How Vite Handles This:**

1. pnpm links workspace packages via symlinks
2. Vite follows symlinks and resolves to source code
3. Changes in workspace packages trigger HMR
4. No need to rebuild packages during development

**Import Example:**

```typescript
// In apps/webcert/src/components/SomeComponent.tsx
import {Button, Dialog} from '@frontend/components'
import {formatDate} from '@frontend/utils'
import {PageHero} from '@frontend/theme-1177'
```

Vite resolves these to:

- `@frontend/components` → `packages/components/src/index.ts`
- `@frontend/utils` → `packages/utils/src/index.ts`
- `@frontend/theme-1177` → `packages/theme-1177/src/index.ts`

## CSS Processing

Vite has built-in support for CSS preprocessing with PostCSS and Tailwind CSS.

### PostCSS Configuration

Each application has `postcss.config.cjs`:

```javascript
module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
```

### CSS Import Handling

**In Development:**

```typescript
import './index.css'  // Injected via <style> tag, instant HMR
```

**In Production:**

```typescript
import './index.css'  // Extracted to separate CSS file, minified
```

### Tailwind CSS Integration

Applications use Tailwind CSS configured via `tailwind.config.ts`:

```typescript
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

**Vite + Tailwind Flow:**

1. **CSS file imports** Tailwind directives:
   ```css
   @tailwind base;
   @tailwind components;
   @tailwind utilities;
   ```

2. **Vite processes** the CSS through PostCSS

3. **Tailwind scans** all files in `content` for class usage

4. **Tailwind generates** only the CSS classes actually used

5. **Result**: Minimal CSS bundle with only used utilities

### CSS Modules

Vite supports CSS Modules automatically:

```typescript
// Component.module.css
.
button
{
  background: blue;
}

// Component.tsx
import styles from './Component.module.css'

<button className = {styles.button} > Click < /button>
```

**Benefits:**

- Scoped styles (no naming conflicts)
- Type-safe with TypeScript
- Tree-shakeable (unused styles are removed)

## Build Process

### Development Build Command

```bash
pnpm dev
# or
pnpm start
```

**What Happens:**

1. **Read `vite.config.ts`** for each application
2. **Start dev server** on configured port
3. **Load environment** variables from `.env.development`
4. **Enable HMR** WebSocket connection
5. **Serve files** as native ES modules
6. **Transform on-demand** - Only transforms files when requested
7. **Watch for changes** and trigger HMR

### Production Build Command

```bash
pnpm build
```

Runs `vite build` in each application:

```json
{
  "scripts": {
    "build": "vite build"
  }
}
```

**Build Process:**

1. **Pre-bundling** - Bundle dependencies with esbuild
2. **Tree-shaking** - Remove unused code
3. **Code splitting** - Split into optimal chunks
4. **Minification** - Minify JavaScript and CSS
5. **Asset optimization** - Optimize images, fonts
6. **Legacy bundles** (if enabled) - Generate polyfilled versions
7. **Source maps** - Generate for debugging
8. **Output to `dist/`** - Creates production-ready files

### Build Output Structure

**SPA (webcert) output:**

```
dist/
├── assets/
│   ├── index-[hash].js       # Main bundle
│   ├── vendor-[hash].js      # Third-party dependencies
│   ├── index-[hash].css      # Extracted CSS
│   └── [dynamic]-[hash].js   # Lazy-loaded chunks
├── favicon.ico
└── index.html                # Entry point with injected script tags
```

**MPA (maintenance) output:**

```
dist/
├── assets/
│   ├── general-[hash].js
│   ├── general-[hash].css
│   ├── intyg-[hash].js
│   ├── intyg-[hash].css
│   └── shared-[hash].js      # Shared code between pages
├── index.html
├── general/
│   └── index.html
└── intyg/
    └── index.html
```

### Build Optimization Features

#### 1. Code Splitting

Vite automatically splits code at dynamic imports:

```typescript
// Lazy load route components
const CertificateView = lazy(() => import('./pages/CertificateView'))
const PatientView = lazy(() => import('./pages/PatientView'))
```

**Result**: Separate chunks loaded on-demand, reducing initial bundle size.

#### 2. Vendor Splitting

Third-party dependencies are automatically split into a `vendor` chunk:

```typescript
// Vite automatically detects node_modules imports
import React from 'react'
import ReactDOM from 'react-dom'
import {Provider} from 'react-redux'
```

**Result**: Vendor chunk cached separately, only app code changes on updates.

#### 3. CSS Code Splitting

CSS is extracted per chunk:

```typescript
import './ComponentA.css'  // Extracted with ComponentA chunk
```

**Result**: Only CSS for loaded components is downloaded.

#### 4. Asset Handling

Static assets are optimized and hashed:

```typescript
import logo from './logo.svg'           // Returns hashed URL
import styles from './styles.module.css' // CSS modules

<img src = {logo}
alt = "Logo" / >
```

**Result**: Efficient caching, automatic optimization.

### Build Performance

**First Build:**

- esbuild pre-bundles dependencies (very fast)
- Rollup bundles application code
- Minification and optimization

**Subsequent Builds:**

- Vite caches pre-bundled dependencies
- Only changed files are rebuilt
- Incremental builds are much faster

**Typical Build Times** (estimated):

- Small app (maintenance): ~5-10 seconds
- Medium app (minaintyg, rehabstod): ~20-30 seconds
- Large app (webcert): ~30-60 seconds

## Vitest Integration

Vite's test framework, **Vitest**, shares the same configuration pipeline as Vite itself.

### Vitest Workspace Configuration

Root `vitest.workspace.json`:

```json
[
  "apps/*",
  "packages/*"
]
```

**Effect**: Vitest discovers and runs tests in all apps and packages.

### Vitest Configuration Example

`apps/webcert/vitest.config.ts`:

```typescript
import path from 'path'
import type {ViteUserConfig} from 'vitest/config'
import {defineProject} from 'vitest/config'

export default defineProject({
  resolve: {
    alias: {
      '@inera/ids-design': path.resolve(__dirname, './node_modules/@inera/ids-design'),
    },
  },
  test: {
    css: false,                    // Don't process CSS in tests
    deps: {
      inline: ['@inera/ids-react'], // Inline specific dependencies
    },
    globals: true,                  // Global test APIs (describe, it, expect)
    environment: 'jsdom',           // Browser-like environment
    include: ['./src/**/*.{test,spec}.?(c|m)[jt]s?(x)'],
    setupFiles: ['src/setupTests.ts'],
    silent: process.env.CI === 'true',
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
} as ViteUserConfig)
```

### Why Vitest Works Well with Vite

1. **Same transformation pipeline**: Tests see the same code as the browser
2. **Fast test runs**: Uses Vite's transformation cache
3. **HMR for tests**: Tests rerun instantly when files change
4. **ESM native**: No need for complex module mocks
5. **Parallel execution**: Tests run concurrently by default

### Running Tests

```bash
pnpm test              # Watch mode, all workspaces
pnpm test:ci           # Single run with coverage
pnpm test:ui           # Visual test UI
```

**Test Workflow:**

1. **Vitest starts** - Loads workspace configuration
2. **Discovers tests** - Scans for `.test.ts` and `.spec.tsx` files
3. **Transforms code** - Uses Vite transformations
4. **Runs tests** - Executes in jsdom environment
5. **Watches** - Re-runs affected tests on file changes
6. **Reports** - Shows results and coverage

## pnpm Workspace Integration

Vite works seamlessly with pnpm's workspace feature for monorepo management.

### Workspace Configuration

`pnpm-workspace.yaml`:

```yaml
packages:
  - 'apps/*'
  - 'packages/*'
  - 'e2e'
```

### Parallel Development

Start all applications in parallel:

```bash
pnpm start
# Runs: pnpm -r --parallel run -r dev
```

**Result:**

- webcert dev server on port 3000
- rehabstod dev server on port 5173
- minaintyg dev server on port 5174
- maintenance dev server on port 5175

All with HMR enabled, watching workspace dependencies.

### Workspace Dependency Updates

**Scenario**: You edit a component in `packages/components/src/Button.tsx`

**What Happens:**

1. **File changes** detected by file watcher
2. **Vite invalidates** the module cache
3. **HMR triggers** in all dependent apps
4. **Components update** instantly in webcert, rehabstod, minaintyg
5. **Tests rerun** if test watch mode is active

**No rebuild required** - source code changes propagate immediately.

## Advanced Vite Features Used

### 1. Dynamic Imports

Used for route-based code splitting:

```typescript
// Lazy load routes
const routes = [
  {
    path: '/certificate',
    component: lazy(() => import('./pages/Certificate')),
  },
  {
    path: '/patient',
    component: lazy(() => import('./pages/Patient')),
  },
]
```

### 2. Import Glob

Vite supports glob imports for batch file imports:

```typescript
// Import all test fixtures
const fixtures = import.meta.glob('./fixtures/*.json')

// Import all icons
const icons = import.meta.glob('./icons/*.svg', {as: 'raw'})
```

### 3. JSON Imports

Import JSON files directly:

```typescript
import packageJson from '../package.json'

console.log(packageJson.version)
```

### 4. Asset URLs

Vite returns asset URLs for static files:

```typescript
import logoUrl from './logo.png'

// In production: '/assets/logo-[hash].png'
// In development: '/src/assets/logo.png'
```

### 5. Worker Support

Import web workers:

```typescript
import Worker from './worker?worker'

const worker = new Worker()
```

## Performance Optimization Patterns

### 1. Dependency Pre-bundling

Vite pre-bundles dependencies with esbuild:

**Why?**

- Many small ES modules → Single bundled file
- CommonJS modules → Converted to ESM
- Reduces HTTP requests in development

**When Does It Happen?**

- First time running dev server
- After adding new dependencies
- After changing dependency versions

**Visual Indicator:**

```
Pre-bundling dependencies:
  react
  react-dom
  @reduxjs/toolkit
  ...
(this will be run only when your dependencies have changed)
```

### 2. Browser Caching

Vite uses strong caching strategies:

**Development:**

- 304 Not Modified for unchanged files
- ETag headers for cache validation

**Production:**

- Content-based hashing in filenames
- Long cache expiration (1 year)
- Changed files get new hash → cache bust

### 3. Tree Shaking

Vite (via Rollup) removes unused exports:

```typescript
// utils.ts exports many functions
export const formatDate = () => { /* ... */
}
export const formatTime = () => { /* ... */
}
export const formatDateTime = () => { /* ... */
}

// app.ts only imports one
import {formatDate} from './utils'

// Final bundle only includes formatDate
```

### 4. Minification

Production builds are minified:

**JavaScript**: esbuild or terser minification

- Removes whitespace
- Shortens variable names
- Removes dead code
- Reduces file size by ~70%

**CSS**: CSS minification

- Removes whitespace
- Shortens color values
- Combines rules
- Reduces file size by ~50%

## Common Development Workflows

### Starting Development

```bash
# Start all apps
pnpm dev

# Start specific app
cd apps/webcert
pnpm dev

# Start with local backend
# (Edit .env.development.local)
pnpm dev
```

### Building for Production

```bash
# Build all apps
pnpm build

# Build specific app
cd apps/webcert
pnpm build

# Preview production build locally
cd apps/webcert
pnpm preview  # Serves dist/ folder
```

### Testing with Vite

```bash
# Run tests in watch mode
pnpm test

# Run tests once (CI mode)
pnpm test:ci

# Run tests with UI
pnpm test:ui

# Run tests for changed files only
pnpm test -- related --run
```

### Debugging Vite

```bash
# Verbose output
DEBUG=vite:* pnpm dev

# Show module resolution
DEBUG=vite:resolve pnpm dev

# Show HMR updates
DEBUG=vite:hmr pnpm dev
```

## Troubleshooting Common Issues

### Issue: Port Already in Use

**Symptom**: `Error: Port 3000 is already in use`

**Solution:**

- Kill process using the port
- Change port in `.env.development`: `VITE_PORT=3001`
- Remove `strictPort: true` from config (not recommended)

### Issue: HMR Not Working

**Symptom**: Changes don't reflect without full page reload

**Solution:**

- Check WebSocket connection in browser DevTools
- Verify `VITE_HMR` is not set to `false`
- Check firewall isn't blocking WebSocket port
- Try setting explicit `VITE_WS_HOST` in `.env`

### Issue: Module Not Found

**Symptom**: `Cannot find module '@frontend/components'`

**Solution:**

- Run `pnpm install` to install workspace dependencies
- Check `pnpm-workspace.yaml` includes the package
- Verify alias configuration in `vite.config.ts`
- Clear Vite cache: `rm -rf node_modules/.vite`

### Issue: Slow Build Times

**Symptom**: Production builds take too long

**Solution:**

- Disable legacy plugin in development: `LEGACY_SUPPORT=false`
- Check for circular dependencies
- Reduce bundle size with lazy loading
- Use Vite's `build.rollupOptions.output.manualChunks` for better splitting

### Issue: Memory Issues

**Symptom**: Build fails with out of memory error

**Solution:**

- Increase Node.js memory: `NODE_OPTIONS=--max-old-space-size=4096 pnpm build`
- Build apps sequentially instead of in parallel
- Check for memory leaks in application code

## Vite vs. Traditional Bundlers

### Vite vs. Webpack

| Feature           | Vite                    | Webpack                     |
|-------------------|-------------------------|-----------------------------|
| Dev Server Start  | Instant                 | Slow (bundles everything)   |
| HMR Speed         | Instant                 | Moderate (rebuilds modules) |
| Configuration     | Minimal                 | Complex                     |
| Build Speed       | Fast (esbuild + Rollup) | Moderate (Terser)           |
| Production Output | Optimized               | Optimized                   |
| Learning Curve    | Easy                    | Steep                       |

### Why This Repository Chose Vite

1. **Developer Experience**: Near-instant server start and HMR
2. **Modern Standards**: Built on native ES modules
3. **Monorepo Friendly**: Works seamlessly with pnpm workspaces
4. **Vitest Integration**: Same config for builds and tests
5. **Future-proof**: Aligned with web platform standards
6. **Performance**: Fast builds, optimized output
7. **TypeScript Support**: First-class TypeScript support
8. **Growing Ecosystem**: Active community, regular updates

## Best Practices for This Repository

### 1. Environment Variables

✅ **Do:**

- Use `VITE_` prefix for client-exposed variables
- Create `.env.local` for personal overrides (never commit)
- Document all environment variables

❌ **Don't:**

- Commit `.env.local` files
- Put secrets in `VITE_` prefixed variables
- Hardcode URLs in source code

### 2. Imports

✅ **Do:**

- Use workspace imports: `@frontend/components`
- Import CSS at component level
- Use dynamic imports for code splitting

❌ **Don't:**

- Use relative paths across workspace boundaries
- Import everything from barrel files if not needed
- Circular dependencies between packages

### 3. Configuration

✅ **Do:**

- Keep configurations consistent across apps
- Use environment variables for different setups
- Document custom configuration

❌ **Don't:**

- Duplicate configuration code
- Override defaults unnecessarily
- Commit environment-specific settings

### 4. Performance

✅ **Do:**

- Use lazy loading for routes
- Split large components
- Leverage browser caching

❌ **Don't:**

- Import entire libraries when you need one function
- Disable minification in production
- Ignore bundle size warnings

## Future Considerations

### Potential Improvements

1. **Build Cache**: Implement shared build cache for CI
2. **Bundle Analysis**: Regular bundle size monitoring
3. **Vite 6**: Upgrade when stable (improved performance)
4. **Custom Plugins**: Create plugins for common patterns
5. **Module Federation**: Consider for shared components across apps
6. **SSR/SSG**: Server-side rendering for better SEO (if needed)

### Monitoring

Track these metrics:

- Dev server start time
- HMR update time
- Build time
- Bundle sizes
- Browser cache hit rate

## Conclusion

Vite provides the Intyg Frontend repository with a modern, fast, and efficient development
experience. Its integration with pnpm workspaces, React, TypeScript, and Vitest creates a cohesive
development environment that scales from individual packages to complex applications.

The configuration patterns established in this repository balance flexibility (environment-based
settings) with consistency (shared plugins and standards), making it easy for developers to work
across multiple applications while maintaining performance and developer experience.

**Key Takeaways:**

- **Instant dev server**: No waiting for bundling
- **Fast HMR**: Changes reflect immediately
- **Monorepo support**: Seamless workspace integration
- **Optimized builds**: Production-ready output
- **Modern standards**: Built on ES modules
- **Developer friendly**: Minimal configuration, maximum productivity
