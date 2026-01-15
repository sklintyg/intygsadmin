module.exports = {
  testEnvironment: 'jsdom',
  collectCoverageFrom: ['src/**/*.{js}'],
  transformIgnorePatterns: ['node_modules/(?!(msw|@mswjs|@bundled-es-modules|until-async)/)'],
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
}
