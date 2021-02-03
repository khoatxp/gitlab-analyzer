module.exports = {
    preset: 'ts-jest',
    testEnvironment: 'node',
    verbose: true,
    collectCoverageFrom: [
        "**/*.{ts, tsx}",
        "!**/node_modules/**",
        "!**/public/**",
        "!**/styles/**",
        "!**/tests/**",
    ],
    collectCoverage: true,
    coverageThreshold: {
        global: {
            branches: 100,
            functions: 100,
            lines: 100,
            statements: 100,
        }
    }
};