This document describes the Maven profiles configured in `pom.xml` and how to build with them.

Profiles:

- dev: Activated by default. Properties:
  - `spring.profiles.active=dev`
  - `logging.level=DEBUG`
  - `database.url=jdbc:h2:mem:devdb`
  - Skips integration tests (`skipITs=true`).

- test: Properties:
  - `spring.profiles.active=test`
  - `logging.level=INFO`
  - `database.url=jdbc:h2:mem:testdb`
  - Enables JaCoCo coverage and runs integration tests.

- prod: Properties:
  - `spring.profiles.active=prod`
  - `logging.level=WARN`
  - `database.url=${env.DATABASE_URL}`
  - Skips tests and builds a fat JAR (via the Shade plugin).

Build commands:

- Dev (default):

```bash
./mvnw clean package
```

- Test (with coverage):

```bash
./mvnw clean package -Ptest
```

- Prod (fat JAR):

```bash
./mvnw clean package -Pprod -DskipTests=true
```

Notes:
- Integration tests are named `*IntegrationTest.java` and are excluded from the Surefire plugin and run by Failsafe when enabled.
- JaCoCo report is generated in `target/site/jacoco` when running the `test` profile.
