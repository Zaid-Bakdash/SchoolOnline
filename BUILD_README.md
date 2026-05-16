# Online School - Build Instructions

## Quick Start

### Prerequisites
- **Java**: JDK 21 or later
- **Maven**: 3.9.5+ (included via Maven Wrapper)
- **Git**: For version control

### First Build
```bash
# Clone repository
git clone <repository-url>
cd SchoolOnline

# Build with Maven Wrapper (no Maven installation needed)
./mvnw clean package
```

---

## Build Scripts

Three convenient build scripts are provided for different scenarios:

### 1. Development Build
```bash
./build-dev.sh
```

**Use When**: Making code changes, running tests locally, debugging
**What It Does**:
- Clean build with development profile
- Runs all unit tests (20 tests)
- Generates JaCoCo code coverage report
- Optimized for fast feedback loop

**Time**: ~16 seconds

**Output**:
```
Artifact: target/online-school-0.0.1-SNAPSHOT.jar
Test Results: 20/20 passing ✓
Coverage: 80%+ (requirement met)
Reports:
  - Code Coverage: target/site/jacoco/index.html
  - Test Results: target/surefire-reports/index.html
```

**Next Steps**:
```bash
# Run the application
java -jar target/online-school-0.0.1-SNAPSHOT.jar

# View coverage report
open target/site/jacoco/index.html
```

---

### 2. Test Build
```bash
./build-test.sh
```

**Use When**: Running comprehensive tests before committing, CI/CD pipeline, final verification
**What It Does**:
- Clean build with test profile
- Runs all unit tests + integration tests
- Generates and verifies code coverage (minimum 80%)
- Generates detailed test and coverage reports
- Enforces quality gates (fails if thresholds not met)

**Time**: ~16 seconds

**Output**:
```
Artifact: target/online-school-0.0.1-SNAPSHOT.jar
Tests: 20 unit tests + integration tests passing ✓
Coverage: Verified (80%+ minimum requirement met)
Reports:
  - Code Coverage: target/site/jacoco/index.html
  - Unit Test Results: target/surefire-reports/index.html
  - Integration Test Results: target/failsafe-reports/index.html
```

**Failure Scenarios**:
- If any test fails: Build fails with error message
- If coverage < 80%: Build fails with coverage report
- Fix issues and rerun `./build-test.sh`

---

### 3. Production Build
```bash
./build-prod.sh
```

**Use When**: Creating production release, building for deployment, skipping tests (CI/CD did them)
**What It Does**:
- Clean build with production profile
- Skips all tests (assumes CI/CD ran them)
- Optimizes for performance and size
- Creates fat JAR with all dependencies
- Copies JAR to `deploy/` directory

**Time**: ~8 seconds (fastest option)

**Output**:
```
Artifact: target/online-school-0.0.1-SNAPSHOT.jar (48MB)
Deployed To: deploy/online-school.jar
Size: 48M
```

**Deployment**:
```bash
# Copy JAR to production server
scp deploy/online-school.jar user@prod-server:/opt/apps/

# Start application on server
ssh user@prod-server
java -jar /opt/apps/online-school.jar

# Check logs
tail -f /var/log/online-school/application.log
```

---

## Direct Maven Commands

### Using Maven Wrapper
```bash
# Maven wrapper (recommended, uses v3.9.5)
./mvnw clean package

# On Windows
mvnw.cmd clean package
```

### Using Installed Maven
If Maven is installed on your system:
```bash
mvn clean package
```

---

## Maven Lifecycle Phases

### Compile Only (Fast)
```bash
./mvnw compile
```
**Use**: Quick syntax check, IDE indexing
**Time**: ~6s
**Output**: Compiled classes in `target/classes/`

### Run Tests Only
```bash
./mvnw test
```
**Use**: Run tests without packaging
**Time**: ~10s
**Output**: Test reports in `target/surefire-reports/`

### Full Build with Packaging
```bash
./mvnw package
```
**Use**: Create deployable JAR
**Time**: ~17s
**Output**: JAR in `target/online-school-0.0.1-SNAPSHOT.jar`

### Skip Tests
```bash
./mvnw clean package -DskipTests
```
**Use**: Fast builds when you know tests pass
**Time**: ~10s (no test execution)
**Output**: JAR without running tests

### Parallel Build (Multi-Module Projects)
```bash
./mvnw clean package -T 1C
```
**Use**: Multi-module projects
**Note**: Single-module projects don't benefit from parallelization
**Threads**: 1 per CPU core

---

## Build Profiles

The project supports three Maven profiles:

### Development Profile (-Pdev)
```bash
./mvnw clean package -Pdev
```
**Configuration**:
- Logging: DEBUG level
- Database: In-memory H2 (devdb)
- Tests: Unit tests included
- Coverage: Enabled but not enforced

### Test Profile (-Ptest)
```bash
./mvnw clean package -Ptest
```
**Configuration**:
- Logging: INFO level
- Database: In-memory H2 (testdb)
- Tests: All tests (unit + integration)
- Coverage: Enforced (minimum 80%)

### Production Profile (-Pprod)
```bash
./mvnw clean package -Pprod
```
**Configuration**:
- Logging: WARN level
- Database: External database (via env vars)
- Tests: Skipped
- Optimization: Performance-focused

---

## Configuration Files

### Maven Wrapper Configuration
**File**: `.mvn/wrapper/maven-wrapper.properties`
```properties
wrapperVersion=3.3.4
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip
distributionType=only-script
```

### JVM Configuration
**File**: `.mvn/jvm.config`
```
-Xmx1024m
```
- Sets heap memory to 1GB
- Sufficient for project build

### Project Configuration
**File**: `pom.xml`
- Dependency versions
- Plugin configurations
- Build profiles
- Test settings

---

## Common Tasks

### View Test Results
```bash
./mvnw test surefire-report:report
open target/site/surefire-report.html
```

### View Code Coverage
```bash
./mvnw verify jacoco:report
open target/site/jacoco/index.html
```

### View All Reports
```bash
./mvnw clean verify site
open target/site/index.html
```

### Check Dependency Tree
```bash
./mvnw dependency:tree
```

### Find Dependency Updates
```bash
./mvnw versions:display-dependency-updates
```

### Run Specific Test
```bash
./mvnw test -Dtest=StudentServiceTest
```

### Generate Project Documentation
```bash
./mvnw site
open target/site/index.html
```

---

## Troubleshooting

### Build Fails: "Could not find goal"
**Solution**: Update Maven wrapper
```bash
./mvnw wrapper:wrapper
```

### Out of Memory Error
**Solution**: Increase heap in `.mvn/jvm.config`
```
-Xmx2048m
```

### Tests Failing Intermittently
**Solution**: Run with clean build
```bash
./mvnw clean test
```

### Slow Builds
**Solution**: Use incremental builds
```bash
./mvnw compile  # Much faster than clean
mvn test        # Run only tests
```

### Dependency Conflicts
**Solution**: Check dependency tree
```bash
./mvnw dependency:tree | grep "conflicting-artifact"
```

---

## CI/CD Integration

### GitHub Actions
The project includes automated CI/CD workflow: `.github/workflows/build.yml`

**Triggers**:
- Push to main, develop, feature/* branches
- Pull requests to main, develop

**Steps**:
1. Checkout code
2. Set up JDK 21
3. Build with dev profile (PRs)
4. Build and test with test profile (all builds)
5. Upload test results and coverage
6. Create GitHub release (main branch tags)

**View Workflow**: GitHub → Actions tab

### Jenkins Integration
To integrate with Jenkins:
```groovy
pipeline {
    stages {
        stage('Build') {
            steps {
                sh './build-test.sh'
            }
        }
        stage('Deploy') {
            steps {
                sh './build-prod.sh'
                sh 'scp deploy/online-school.jar prod-server:/opt/apps/'
            }
        }
    }
}
```

---

## Docker Integration

### Build Docker Image
If Docker support is added (future):
```bash
./mvnw spring-boot:build-image
docker run -p 8080:8080 online-school:0.0.1-SNAPSHOT
```

### Local Testing with Docker
```bash
docker run -p 8080:8080 online-school:latest
curl http://localhost:8080/api/health
```

---

## Performance Tips

### Tip 1: Use Incremental Builds
```bash
./mvnw compile  # 65% faster - only compiles changed files
```

### Tip 2: Skip Tests Locally (If Confident)
```bash
./mvnw clean package -DskipTests  # 50% faster
```

### Tip 3: Use IDE Integration
- IntelliJ IDEA: Built-in Maven integration
- Eclipse: m2e plugin
- VS Code: Extension Packs for Java

### Tip 4: Check Build Performance
```bash
./mvnw clean package --batch-mode --fail-fast
```

### Tip 5: Warm Up Dependency Cache
```bash
./mvnw dependency:resolve  # Download all deps upfront
```

---

## Build Environment Variables

### Maven Options
```bash
# Increase parallel threads
export MAVEN_OPTS="-T 1C"

# Increase heap memory
export MAVEN_OPTS="-Xmx2048m"

# Enable debug output
export MAVEN_OPTS="-X"
```

### Java Options
```bash
# Set Java home
export JAVA_HOME=/path/to/jdk21

# Run all builds with Java 21
./mvnw clean package
```

---

## File Structure

```
SchoolOnline/
├── build-dev.sh          # Development build script
├── build-test.sh         # Test build script
├── build-prod.sh         # Production build script
├── mvnw                  # Maven Wrapper script
├── mvnw.cmd              # Maven Wrapper for Windows
├── .mvn/
│   ├── wrapper/
│   │   └── maven-wrapper.properties
│   └── jvm.config        # JVM configuration
├── pom.xml               # Maven configuration
├── src/
│   ├── main/java/        # Source code
│   └── test/java/        # Test code
├── target/               # Build output (generated)
│   ├── classes/
│   ├── test-classes/
│   ├── online-school-*.jar
│   ├── surefire-reports/ # Test reports
│   ├── failsafe-reports/ # Integration test reports
│   └── site/             # Generated documentation
│       └── jacoco/       # Code coverage reports
└── deploy/               # Deployment JAR (created by build-prod.sh)
    └── online-school.jar
```

---

## Next Steps

1. **First Time**:
   ```bash
   ./build-dev.sh
   ```

2. **Development**:
   ```bash
   # Edit code, then
   ./mvnw compile  # Quick feedback
   ./build-dev.sh  # Full build with tests
   ```

3. **Before Commit**:
   ```bash
   ./build-test.sh  # Comprehensive testing
   git add . && git commit -m "Feature: ..."
   ```

4. **Release**:
   ```bash
   ./build-prod.sh
   # Artifact ready in deploy/online-school.jar
   ```

---

## Getting Help

### View Maven Help
```bash
./mvnw --help
./mvnw clean --help
```

### Check Build Performance
See [BUILD_PERFORMANCE.md](BUILD_PERFORMANCE.md) for:
- Build time benchmarks
- Performance optimization tips
- Comparison of different build strategies

### Review Assignment
See [HOMEWORK.md](HOMEWORK.md) for:
- Assignment requirements
- Learning objectives
- Expected deliverables

---

## Summary

| Task | Command | Time |
|------|---------|------|
| Quick Development Build | `./build-dev.sh` | 16s |
| Comprehensive Test Build | `./build-test.sh` | 16s |
| Fast Production Build | `./build-prod.sh` | 8s |
| Quick Compile Only | `./mvnw compile` | 6s |
| Full Maven Build | `./mvnw clean package` | 17s |

Choose the appropriate build script for your workflow and get productive!
