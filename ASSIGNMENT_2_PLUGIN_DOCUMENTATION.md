# Assignment 2: Plugin Configuration and Automation - Implementation Guide

## Overview

This document describes all Maven plugins configured for the Online School project to support compilation, testing, packaging, code quality analysis, and documentation generation.

---

## Plugin Configurations

### 1. Compiler Plugin (maven-compiler-plugin)

**Version:** 3.11.0

**Purpose:** Compile Java source code with specific configuration options

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>21</source>
        <target>21</target>
        <encoding>UTF-8</encoding>
        <showWarnings>true</showWarnings>
        <compilerArgs>
            <arg>-Xlint:unchecked</arg>
            <arg>-Xlint:deprecation</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

**Key Features:**
- **Java Version:** 21 (source and target)
- **File Encoding:** UTF-8 for cross-platform compatibility
- **Warning Levels:** Shows all warnings for unchecked and deprecated code
- **Annotation Processing:** Prepared for Lombok via provided dependency

**Usage:**
```bash
mvn clean compile
```

---

### 2. Surefire Plugin (maven-surefire-plugin)

**Version:** 3.0.0

**Purpose:** Execute unit tests during the test phase

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>2</threadCount>
        <argLine>-Xmx1024m</argLine>
        <excludes>
            <exclude>**/*IntegrationTest.java</exclude>
        </excludes>
        <reportFormat>plain</reportFormat>
        <reportFormat>xml</reportFormat>
    </configuration>
</plugin>
```

**Key Features:**
- **Parallel Execution:** Runs 2 test methods in parallel for faster test suite
- **Memory:** Allocates 1024MB for test execution
- **Exclusions:** Excludes integration tests from unit test phase
- **Report Formats:** Generates both plain text and XML reports

**Reporting Output:**
- `target/surefire-reports/` - Test execution reports
- `target/surefire-reports/TEST-*.xml` - XML format for CI/CD integration

**Usage:**
```bash
mvn test
mvn clean package  # Includes test phase
```

---

### 3. Failsafe Plugin (maven-failsafe-plugin)

**Version:** 3.0.0

**Purpose:** Execute integration tests in separate phase

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <includes>
            <include>**/*IntegrationTest.java</include>
        </includes>
        <argLine>-Xmx1024m</argLine>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Key Features:**
- **Integration Tests:** Includes only files matching `*IntegrationTest.java`
- **Separate Phase:** Runs after packaging in `integration-test` phase
- **Verification:** Fails build if integration tests fail
- **Memory:** 1024MB allocated for integration test execution

**Lifecycle Phase:** `integration-test`

**Usage:**
```bash
mvn verify  # Runs all tests including integration tests
```

---

### 4. JaCoCo Plugin (jacoco-maven-plugin)

**Version:** 0.8.10

**Purpose:** Analyze code coverage and generate coverage reports

**Configuration:**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <excludes>
                            <exclude>com.bootcamp.onlineschool.config.*</exclude>
                        </excludes>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                            <limit>
                                <counter>BRANCH</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.70</minimum>
                            </limit>
                            <limit>
                                <counter>CLASS</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.90</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**Key Features:**
- **Code Coverage Analysis:**
  - Line coverage: 80% minimum
  - Branch coverage: 70% minimum
  - Class coverage: 90% minimum
- **Excluded Packages:** Configuration classes are excluded from thresholds
- **Report Generation:** Creates HTML coverage report
- **Build Verification:** Fails build if thresholds not met

**Coverage Report Location:**
- `target/site/jacoco-aggregate/index.html` - HTML report
- `target/jacoco.exec` - Coverage data file

**Usage:**
```bash
mvn test                    # Generates coverage data
mvn jacoco:report          # Creates HTML report
mvn clean package -Ptest   # Full build with coverage checks (test profile)
```

**Viewing Reports:**
```bash
open target/site/jacoco-aggregate/index.html  # macOS
xdg-open target/site/jacoco-aggregate/index.html  # Linux
start target/site/jacoco-aggregate/index.html  # Windows
```

---

### 5. JAR Plugin (maven-jar-plugin)

**Version:** 3.3.0

**Purpose:** Configure JAR packaging with manifest entries

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.3.0</version>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>lib/</classpathPrefix>
                <mainClass>com.bootcamp.onlineschool.OnlineSchoolApplication</mainClass>
            </manifest>
            <manifestEntries>
                <Implementation-Title>${project.name}</Implementation-Title>
                <Implementation-Version>${project.version}</Implementation-Version>
                <Built-By>${user.name}</Built-By>
                <Build-Timestamp>${maven.build.timestamp}</Build-Timestamp>
                <Build-Jdk>${java.version}</Build-Jdk>
                <Implementation-Vendor-Id>${project.groupId}</Implementation-Vendor-Id>
            </manifestEntries>
        </archive>
        <excludes>
            <exclude>**/*Test.class</exclude>
            <exclude>**/*IntegrationTest.class</exclude>
            <exclude>test-resources/**</exclude>
        </excludes>
    </configuration>
</plugin>
```

**Key Features:**
- **Manifest Entries:**
  - Implementation-Title: Online School
  - Implementation-Version: 0.0.1-SNAPSHOT
  - Built-By: Build username
  - Build-Timestamp: Build timestamp
  - Build-Jdk: Java version (21)
  - Implementation-Vendor-Id: com.bootcamp
- **Classpath:** Automatically added to manifest
- **Test Exclusions:** Test classes not included in JAR
- **Main Class:** Set for execution

**Generated JAR:**
- `target/online-school-0.0.1-SNAPSHOT.jar` - Regular JAR
- `target/online-school-0.0.1-SNAPSHOT.jar.original` - Original before repackage

---

### 6. Spring Boot Maven Plugin (spring-boot-maven-plugin)

**Version:** 3.2.0 (from parent)

**Purpose:** Create executable fat JAR with all dependencies

**Configuration:**
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>com.bootcamp.onlineschool.OnlineSchoolApplication</mainClass>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Key Features:**
- **Fat JAR Creation:** Packages all dependencies inside the JAR
- **Executable JAR:** Can be run directly: `java -jar application.jar`
- **Repackage Goal:** Replaces default JAR with executable version
- **Main Class:** Automatically set to Spring Boot application

**Generated Artifacts:**
- `target/online-school-0.0.1-SNAPSHOT.jar` - Executable fat JAR (~21MB)
- `target/online-school-0.0.1-SNAPSHOT.jar.original` - Original thin JAR

**Usage:**
```bash
mvn clean package                # Creates fat JAR
java -jar target/online-school-0.0.1-SNAPSHOT.jar  # Run application
```

---

### 7. Site Plugin (maven-site-plugin)

**Version:** 3.12.1

**Purpose:** Generate project documentation and reports

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-site-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        <outputEncoding>UTF-8</outputEncoding>
    </configuration>
</plugin>
```

**Key Features:**
- **Documentation Generation:** Creates HTML project documentation
- **UTF-8 Encoding:** Ensures proper character encoding
- **Multi-format Support:** Generates various report formats

**Usage:**
```bash
mvn site                    # Generate site
mvn site:deploy            # Deploy site to server
```

---

### 8. Javadoc Plugin (maven-javadoc-plugin)

**Version:** 3.5.0

**Purpose:** Generate API documentation from source code

**Configuration:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.5.0</version>
    <configuration>
        <source>21</source>
        <encoding>UTF-8</encoding>
        <docencoding>UTF-8</docencoding>
    </configuration>
</plugin>
```

**Key Features:**
- **API Documentation:** Generates HTML from JavaDoc comments
- **Java 21 Support:** Configured for Java 21 syntax
- **Encoding:** UTF-8 for source and documentation

**Generated Documentation:**
- `target/site/apidocs/` - API documentation
- `target/site/testapidocs/` - Test API documentation

**Usage:**
```bash
mvn javadoc:javadoc       # Generate Javadocs
mvn site                  # Includes Javadoc generation
```

---

## Build Profiles Integration

### Development Profile (dev) - Default

- **Surefire:** Parallel execution (2 threads), excludes integration tests
- **JaCoCo:** Coverage data collected but not enforced
- **Failsafe:** Not active

### Test Profile (-Ptest)

- **Surefire:** Parallel execution, runs all tests
- **JaCoCo:** Coverage data collected and thresholds enforced
- **Failsafe:** Runs integration tests
- **Reports:** Full coverage reports generated

### Production Profile (-Pprod)

- **Surefire:** Tests skipped
- **JaCoCo:** Skipped
- **Failsafe:** Skipped
- **Spring Boot:** Creates repackaged executable JAR

---

## Build Lifecycle

### Standard Build Phases

```
1. validate      - Validate project structure
2. compile       - Compile Java sources (Compiler Plugin)
3. test          - Run unit tests (Surefire Plugin)
4. package       - Create JAR (JAR Plugin + Spring Boot Plugin)
5. integration-test  - Run integration tests (Failsafe Plugin)
6. verify        - Verify test results
7. install       - Install to local repository
8. deploy        - Deploy to remote repository
```

### Code Coverage Phases

```
1. test          - Surefire runs tests with JaCoCo agent
2. jacoco:report - JaCoCo generates HTML report
3. jacoco:check  - JaCoCo verifies coverage thresholds
```

### Site Generation Phase

```
1. site          - Generates all reports:
                   - Surefire test reports
                   - JaCoCo coverage reports
                   - Javadoc API documentation
                   - Project information
                   - Dependency information
```

---

## Common Commands

```bash
# Compile
mvn clean compile

# Run unit tests
mvn test

# Run all tests (including integration)
mvn verify

# Package application
mvn clean package

# Package with dev profile (default)
mvn clean package

# Package with test profile (with coverage)
mvn clean package -Ptest

# Package with prod profile (skip tests)
mvn clean package -Pprod

# Generate site documentation
mvn site

# View coverage report
mvn test && open target/site/jacoco-aggregate/index.html

# Run application
java -jar target/online-school-0.0.1-SNAPSHOT.jar

# Parallel build (1 thread per CPU core)
mvn clean package -T 1C

# Debug build
mvn clean package -X
```

---

## Generated Artifacts

### After `mvn clean package`

```
target/
├── online-school-0.0.1-SNAPSHOT.jar       (Fat JAR ~21MB)
├── online-school-0.0.1-SNAPSHOT.jar.original  (Original thin JAR)
├── classes/                               (Compiled classes)
├── test-classes/                          (Test classes)
├── surefire-reports/                      (Test reports)
├── jacoco.exec                            (Coverage data)
└── site/                                  (Documentation - after mvn site)
    ├── index.html
    ├── apidocs/                          (Javadocs)
    ├── jacoco-aggregate/                 (Coverage reports)
    └── surefire-report.html              (Test report)
```

---

## Troubleshooting

### Issue: Tests fail with parallel execution

**Solution:** Run with single thread
```bash
mvn test -Dsurefire.reuseForks=false
```

### Issue: Coverage thresholds failing

**Solution:** View detailed coverage report
```bash
mvn test
open target/site/jacoco-aggregate/index.html
```

### Issue: Integration tests not running

**Solution:** Use verify phase instead of test
```bash
mvn verify
```

### Issue: JAR not executable

**Solution:** Ensure Spring Boot plugin is configured correctly
```bash
java -cp target/online-school-0.0.1-SNAPSHOT.jar com.bootcamp.onlineschool.OnlineSchoolApplication
```

### Issue: Site generation fails

**Solution:** Clean Maven cache and rebuild
```bash
rm -rf ~/.m2/repository
mvn clean site
```

---

## Performance Considerations

### Parallel Testing
- Configured for 2 threads by default
- Adjust in `<threadCount>` in Surefire configuration
- Use `-T 1C` flag for parallel module building

### Memory Configuration
- Tests run with `-Xmx1024m` (1GB)
- Increase if running large test suites
- Modify `<argLine>` in Surefire/Failsafe plugins

### Coverage Analysis
- JaCoCo can slow down tests by 5-10%
- Disable in development with: `mvn test -DargLine=`
- Enable only in test profile

---

## Plugin Versions Summary

| Plugin | Version | Purpose |
|--------|---------|---------|
| maven-compiler-plugin | 3.11.0 | Java compilation |
| maven-surefire-plugin | 3.0.0 | Unit testing |
| maven-failsafe-plugin | 3.0.0 | Integration testing |
| jacoco-maven-plugin | 0.8.10 | Code coverage |
| maven-jar-plugin | 3.3.0 | JAR packaging |
| spring-boot-maven-plugin | 3.2.0 | Executable JAR |
| maven-site-plugin | 3.12.1 | Documentation |
| maven-javadoc-plugin | 3.5.0 | API docs |

---

## Next Steps

For more advanced plugin configuration, see:
- [Maven Compiler Plugin Documentation](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Maven Surefire Plugin Documentation](https://maven.apache.org/surefire/)
- [JaCoCo Plugin Documentation](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Spring Boot Maven Plugin Documentation](https://docs.spring.io/spring-boot/docs/current/maven-plugin/)
