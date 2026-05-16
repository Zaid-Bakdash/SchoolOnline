# Maven Profiles Documentation

## Overview
This document explains the three Maven profiles configured for the Online School application to support different environments: development, testing, and production.

## Profile Configurations

### 1. Development Profile (`dev`)

**Profile ID:** `dev`  
**Activation:** Active by default (no command needed)

**Purpose:** Used for local development with verbose output and debugging capabilities

**Build Command:**
```bash
mvn clean package
```

**Configuration:**
- **Spring Profile:** `dev`
- **Logging Level:** `DEBUG`
- **Database:** H2 in-memory database (`jdbc:h2:mem:devdb`)
- **H2 Console:** Enabled for database inspection at `/h2-console`
- **JPA/Hibernate:**
  - `ddl-auto=create-drop`: Recreates schema on startup
  - `show-sql=true`: Shows SQL statements
  - `format_sql=true`: Formats SQL for readability
- **Test Execution:** Skips integration tests, runs only unit tests
- **Property File:** `application-dev.properties`

**Use Cases:**
- Local development and debugging
- Testing individual features
- Experimenting with database changes

---

### 2. Test Profile (`test`)

**Profile ID:** `test`  
**Activation:** Explicitly activated with `-Ptest`

**Purpose:** Used for running comprehensive tests including unit and integration tests with code coverage metrics

**Build Command:**
```bash
mvn clean package -Ptest
```

**Configuration:**
- **Spring Profile:** `test`
- **Logging Level:** `INFO`
- **Database:** H2 in-memory database (`jdbc:h2:mem:testdb`)
- **H2 Console:** Disabled for cleaner test output
- **JPA/Hibernate:**
  - `ddl-auto=create-drop`: Recreates schema for each test run
  - `show-sql=false`: Suppresses SQL output
- **Test Execution:**
  - Includes both `*Test.java` and `*IntegrationTest.java` test classes
  - Runs all tests
- **Code Coverage:** JaCoCo plugin enabled to generate code coverage reports
- **Property File:** `application-test.properties`

**Use Cases:**
- Running full test suite before deployment
- Generating code coverage reports (`target/site/jacoco/index.html`)
- Continuous Integration (CI) pipelines
- Ensuring code quality standards

---

### 3. Production Profile (`prod`)

**Profile ID:** `prod`  
**Activation:** Explicitly activated with `-Pprod`

**Purpose:** Used for building optimized, production-ready deployments with minimal logging and no test execution

**Build Command:**
```bash
mvn clean package -Pprod
```

**Configuration:**
- **Spring Profile:** `prod`
- **Logging Level:** `WARN`
- **Database:** Uses environment variable (`${env.DATABASE_URL}`)
- **H2 Console:** Disabled for security
- **JPA/Hibernate:**
  - `ddl-auto=validate`: Only validates schema, no modifications
  - `show-sql=false`: Suppresses SQL output
- **Test Execution:** Tests are skipped (assumes CI/CD already validated them)
- **JAR Creation:**
  - Creates executable fat JAR with all dependencies bundled
  - Enables executable mode for direct execution
- **Property File:** `application-prod.properties`

**Use Cases:**
- Building final deployable artifacts
- Production deployments
- Release builds
- Scenarios where tests have already been validated in CI/CD

---

## Property Files Summary

| Profile | Property File | Database | H2 Console | Log Level | SQL Display |
|---------|--------------|----------|-----------|-----------|------------|
| dev | application-dev.properties | H2 memory | ✓ Enabled | DEBUG | ✓ Enabled |
| test | application-test.properties | H2 memory | ✗ Disabled | INFO | ✗ Disabled |
| prod | application-prod.properties | Environment | ✗ Disabled | WARN | ✗ Disabled |

---

## Usage Examples

### Development Workflow
```bash
# Build with default dev profile
mvn clean package

# Run the application
java -jar target/online-school-0.0.1-SNAPSHOT.jar
```

### Pre-deployment Testing
```bash
# Run comprehensive tests with coverage
mvn clean package -Ptest

# View code coverage report
open target/site/jacoco/index.html
```

### Production Deployment
```bash
# Build production-ready JAR
mvn clean package -Pprod

# Run the production JAR
./target/online-school-0.0.1-SNAPSHOT.jar
```

---

## Key Features by Profile

### Development (`dev`)
- Verbose logging for easy debugging
- In-memory database that resets on each startup
- H2 console for database inspection
- Skips integration tests for faster development cycle
- Shows SQL queries and formats them for readability

### Testing (`test`)
- Code coverage analysis with JaCoCo
- Runs both unit and integration tests
- Reduced logging to focus on test output
- Fresh in-memory database for each test run
- Generates test reports for CI/CD

### Production (`prod`)
- Minimal logging (WARN level only)
- No test execution (assumes CI/CD validation)
- Uses external database via environment variable
- Creates executable, deployable JAR
- Optimized for performance and security

---

## Environment Variables

For production deployments, ensure the following environment variable is set:

```bash
export DATABASE_URL=jdbc:mysql://host:port/database
```

This variable is referenced in `application-prod.properties` and used by the prod profile.

---

## Troubleshooting

### Issue: Tests fail with prod profile
**Solution:** The prod profile skips tests intentionally. Use the test profile for running tests.

### Issue: Wrong properties being used
**Solution:** Verify the active profile in logs. Look for: `The following X profile(s) are active:`

### Issue: Database connection errors
**Solution:** For prod profile, ensure `DATABASE_URL` environment variable is properly set.

### Issue: Coverage report not generated
**Solution:** Use the test profile: `mvn clean package -Ptest`. Report will be at `target/site/jacoco/index.html`

---

## Best Practices

1. **Use dev profile** for local development to get maximum debugging information
2. **Use test profile** before committing code to run full test suite with coverage
3. **Use prod profile** only for final production builds
4. **Set DATABASE_URL** environment variable in production deployment scripts
5. **Check active profile** in startup logs to confirm correct configuration
