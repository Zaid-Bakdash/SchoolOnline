#!/bin/bash

################################################################################
# Test Build Script
# Purpose: Clean build with test profile, all tests, coverage verification
# Usage: ./build-test.sh [options]
################################################################################

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$SCRIPT_DIR"

echo -e "${GREEN}========================================${NC}"
echo "  Test Build"
echo -e "${GREEN}========================================${NC}"

# Change to project directory
cd "$PROJECT_DIR"

# Start timer
START_TIME=$(date +%s)

# Run build with test profile
echo -e "${YELLOW}Building with test profile...${NC}"
echo "Profile: test (comprehensive testing)"
echo "Tests: All unit + integration tests"
echo "Coverage: Enforced (minimum 80%)"
echo ""

./mvnw clean package \
    -Ptest \
    -Dorg.slf4j.simpleLogger.defaultLogLevel=info \
    -T 1C \
    "$@"

# Check coverage report
if [ -f "target/site/jacoco/index.html" ]; then
    echo ""
    echo -e "${GREEN}Code Coverage Report Generated${NC}"
fi

# End timer
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

# Print results
echo ""
echo -e "${GREEN}========================================${NC}"
echo "  Build Completed Successfully"
echo -e "${GREEN}========================================${NC}"
echo "Build Duration: ${DURATION}s"
echo "Profile: Test"
echo "Tests: All tests passed ✓"
echo "Coverage: Requirements met ✓"
echo "Artifact: target/online-school-0.0.1-SNAPSHOT.jar"
echo ""
echo "Generated Reports:"
echo "  - Code Coverage: target/site/jacoco/index.html"
echo "  - Test Results: target/surefire-reports/index.html"
echo "  - Failsafe Results: target/failsafe-reports/index.html"
echo ""
