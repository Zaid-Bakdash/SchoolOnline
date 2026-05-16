#!/bin/bash

################################################################################
# Development Build Script
# Purpose: Clean build with dev profile, unit tests, and reports
# Usage: ./build-dev.sh [options]
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
echo "  Development Build"
echo -e "${GREEN}========================================${NC}"

# Change to project directory
cd "$PROJECT_DIR"

# Start timer
START_TIME=$(date +%s)

# Run build with dev profile
echo -e "${YELLOW}Building with dev profile...${NC}"
echo "Profile: dev (development mode)"
echo "Tests: Unit tests included"
echo "Coverage: JaCoCo coverage analysis enabled"
echo ""

./mvnw clean package \
    -Pdev \
    -DskipIntegrationTests=true \
    -Dorg.slf4j.simpleLogger.defaultLogLevel=info \
    -T 1C \
    "$@"

# End timer
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

# Print results
echo ""
echo -e "${GREEN}========================================${NC}"
echo "  Build Completed Successfully"
echo -e "${GREEN}========================================${NC}"
echo "Build Duration: ${DURATION}s"
echo "Profile: Development"
echo "Artifact: target/online-school-0.0.1-SNAPSHOT.jar"
echo ""
echo "Next steps:"
echo "  - Run application: java -jar target/online-school-0.0.1-SNAPSHOT.jar"
echo "  - View coverage: open target/site/jacoco/index.html"
echo "  - View test reports: open target/surefire-reports/index.html"
echo ""
