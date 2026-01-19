import legacy from '@vitejs/plugin-legacy'
import react from '@vitejs/plugin-react'
import path from 'path'
import { defineConfig, loadEnv } from 'vite'

export default ({ mode }) => {
  const env = loadEnv(mode ?? 'development', process.cwd())

  const hmr = !(env.VITE_HMR === 'false')
  const host = env.VITE_HOST ?? 'localhost'
  const apiTarget = env.VITE_API_TARGET ?? 'http://localhost:8070'

  const proxy = ['/api', '/fake', '/fake-api', '/public-api', '/logout', '/login', '/error.jsp'].reduce(
    (result, route) => ({
      ...result,
      [route]: {
        secure: false,
        target: apiTarget,
        cookieDomainRewrite: { '*': '' },
        changeOrigin: true,
        autoRewrite: true,
        configure: (proxy, options) => {
          proxy.on('error', (err, req, res) => {
            console.log('Proxy error on', req.url, ':', err.message)
          })
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('Proxying request:', req.method, req.url, '->', apiTarget + req.url)
          })
        },
      },
    }),
    {}
  )

  return defineConfig({
    plugins: [
      react({
        include: /\.(jsx|js|tsx|ts)$/,
        jsxRuntime: 'automatic',
      }),
    ].concat(
      env.LEGACY_SUPPORT !== 'false'
        ? legacy({
            targets: ['defaults', 'not IE 11'],
          })
        : []
    ),
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
          silenceDeprecations: ['import', 'global-builtin', 'color-functions', 'legacy-js-api', 'if-function'],
          quietDeps: true,
        },
      },
    },
    server: {
      host,
      port: 3000,
      proxy,
      strictPort: true,
      hmr: hmr
        ? {
            host: env.VITE_WS_HOST ?? host,
            protocol: env.VITE_WS_PROTOCOL ?? 'ws',
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
            redux: ['redux', 'react-redux', '@reduxjs/toolkit'],
          },
        },
      },
    },
    esbuild: {
      loader: 'jsx',
      include: /src\/.*\.[jt]sx?$/,
      exclude: [],
    },
    optimizeDeps: {
      esbuildOptions: {
        loader: {
          '.js': 'jsx',
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
