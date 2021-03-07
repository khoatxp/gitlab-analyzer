module.exports = {
    preset: 'ts-jest',
    testEnvironment: 'jsdom',
    verbose: true,
    setupFilesAfterEnv: ["<rootDir>/tests/setupTests.ts"],
    collectCoverageFrom: [
        "**/*.{tsx,ts}",
        "!**/node_modules/**",
        "!**/pages/_app.tsx/**",
        "!**/pages/_document.tsx/**",
        "!**/public/**",
        "!**/styles/**",
        "!**/tests/**",
    ],
    collectCoverage: true,
    coverageThreshold: {
        global: {
            branches: 70,
            functions: 60,
            lines: 88,
            statements: 88,
        }
    },
    "snapshotSerializers": ["enzyme-to-json/serializer"],
  // https://github.com/zeit/next.js/issues/8663#issue-490553899
    "globals": {
    // we must specify a custom tsconfig for tests because we need the typescript transform
    // to transform jsx into js rather than leaving it jsx such as the next build requires. you
    // can see this setting in tsconfig.jest.json -> "jsx": "react"
    "ts-jest": {
      "tsconfig": "<rootDir>/tsconfig.jest.json"
    }
  }
};