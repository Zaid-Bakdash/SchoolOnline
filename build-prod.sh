#!/bin/bash

################################################################################
# Production Build Script
# Purpose: Clean build with prod profile, fat JAR, deployment ready
# Usage: ./build-prod.sh [options]
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
echo "  Production Build"
echo -e "${GREEN}========================================${NC}"

# Change to project directory
cd "$PROJECT_DIR"

# Start timer
START_TIME=$(date +%s)

# Run build with prod profile
echo -e "${YELLOW}Building with prod profile...${NC}"
echo "Profile: prod (production mode)"
echo "Tests: Skipped (CI/CD responsibility)"
echo "Optimization: Enabled"
echo ""

./mvnw clean package \
    -Pprod \
    -DskipTests \
    -Dorg.slf4j.simpleLogger.defaultLogLevel=warn \
    -T 1C \
    "$@"

# End timer
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

# Get JAR file info
JAR_FILE="target/online-school-0.0.1-SNAPSHOT.jar"
JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)

# Create deployment directory if it doesn't exist
DEPLOY_DIR="$PROJECT_DIR/deploy"
mkdir -p "$DEPLOY_DIR"

# Copy JAR to deployment directory
cp "$JAR_FILE" "$DEPLOY_DIR/online-school.jar"

# Print results
echo ""
echo -e "${GREEN}========================================${NC}"
echo "  Build Completed Successfully"
echo -e "${GREEN}========================================${NC}"
echo "Build Duration: ${DURATION}s"
echo "Profile: Production"
echo "Artifact: $JAR_FILE"
echo "Size: $JAR_SIZE"
echo "Deployed To: $DEPLOY_DIR/online-school.jar"
echo ""
echo "Next steps:"
echo "  - Copy $DEPLOY_DIR/online-school.jar to production server"
echo "  - Run: java -jar online-school.jar"
echo "  - Check logs: tail -f /var/log/online-school/application.log"
echo ""
