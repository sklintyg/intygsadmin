import js from '@eslint/js'
import globals from 'globals'
import reactPlugin from 'eslint-plugin-react'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import jsxA11y from 'eslint-plugin-jsx-a11y'
import importPlugin from 'eslint-plugin-import'

export default [
  {
    ignores: ['coverage/**/*.*', 'build/**/*.*', 'public/**/*.min.js'],
  },
  js.configs.recommended,
  {
    files: ['**/*.spec.js', '**/*.test.js', '**/testUtils/**/*.js'],
    languageOptions: {
      globals: globals.jest,
    },
  },
  {
    files: ['public/**/*.js'],
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.jquery,
      },
    },
  },
  reactPlugin.configs.flat.recommended,
  reactPlugin.configs.flat['jsx-runtime'],
  {
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      'jsx-a11y': jsxA11y,
      import: importPlugin,
    },
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    settings: {
      react: {
        version: 'detect',
      },
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      ...jsxA11y.configs.recommended.rules,
      'react/prop-types': 'off',
      'react/react-in-jsx-scope': 'off',
      'react-hooks/rules-of-hooks': 'error',
      'react-hooks/exhaustive-deps': 'warn',
      'no-unused-vars': ['warn', { argsIgnorePattern: '^_', varsIgnorePattern: '^React$' }],
      'no-undef': 'error',
      'array-callback-return': 'warn',
      'react/jsx-no-undef': 'error',
      'no-duplicate-imports': 'error',
      'import/no-anonymous-default-export': 'warn',
      'react/no-unknown-property': ['error', { ignore: ['trigger'] }],
      'jsx-a11y/label-has-associated-control': [
        'error',
        {
          labelComponents: [],
          labelAttributes: [],
          controlComponents: [],
          assert: 'either',
          depth: 25,
        },
      ],
      'react-refresh/only-export-components': ['warn', { checkJS: true }],
    },
  },
]

