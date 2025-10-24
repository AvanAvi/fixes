#!/bin/bash

echo "🧹 ================================================"
echo "🧹   FINAL E2E ENVIRONMENT CLEANUP & VERIFICATION"
echo "🧹 ================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "📦 Step 1: Cleaning Docker containers..."
docker stop $(docker ps -q) 2>/dev/null && echo "  ✅ Stopped running containers" || echo "  ℹ️  No containers running"
docker rm $(docker ps -aq) 2>/dev/null && echo "  ✅ Removed all containers" || echo "  ℹ️  No containers to remove"
echo ""

echo "🗑️  Step 2: Cleaning Maven build artifacts..."
./mvnw clean -q
if [ $? -eq 0 ]; then
    echo "  ✅ Maven clean successful"
else
    echo "  ❌ Maven clean failed"
fi
echo ""

echo "📄 Step 3: Removing backup and log files..."
rm -f pom.xml.bak* pom.xml.backup* 2>/dev/null && echo "  ✅ Removed POM backups" || echo "  ℹ️  No POM backups found"
rm -f e2e-logs.txt e2e-profile.txt full-error.log 2>/dev/null && echo "  ✅ Removed log files" || echo "  ℹ️  No log files found"
echo ""

echo "🔌 Step 4: Verifying ports are free..."
PORT_8080=$(lsof -i :8080 2>/dev/null | wc -l)
PORT_9090=$(lsof -i :9090 2>/dev/null | wc -l)
PORT_3307=$(lsof -i :3307 2>/dev/null | wc -l)

if [ $PORT_8080 -eq 0 ]; then
    echo -e "  ${GREEN}✅ Port 8080: FREE${NC}"
else
    echo -e "  ${RED}❌ Port 8080: OCCUPIED${NC}"
    lsof -i :8080
fi

if [ $PORT_9090 -eq 0 ]; then
    echo -e "  ${GREEN}✅ Port 9090: FREE${NC}"
else
    echo -e "  ${RED}❌ Port 9090: OCCUPIED${NC}"
    lsof -i :9090
fi

if [ $PORT_3307 -eq 0 ]; then
    echo -e "  ${GREEN}✅ Port 3307: FREE${NC}"
else
    echo -e "  ${RED}❌ Port 3307: OCCUPIED${NC}"
    lsof -i :3307
fi
echo ""

echo "📋 Step 5: Verifying project configuration..."
if grep -q "e2e.server.port.*9090" pom.xml; then
    echo -e "  ${GREEN}✅ E2E port configured correctly (9090)${NC}"
else
    echo -e "  ${RED}❌ E2E port not configured${NC}"
fi

if grep -q "server.port.*{e2e.server.port}" pom.xml; then
    echo -e "  ${GREEN}✅ Spring Boot plugin configured correctly${NC}"
else
    echo -e "  ${RED}❌ Spring Boot plugin not configured${NC}"
fi
echo ""

echo "🐳 Step 6: Docker status..."
DOCKER_CONTAINERS=$(docker ps -a | wc -l)
if [ $DOCKER_CONTAINERS -eq 1 ]; then
    echo -e "  ${GREEN}✅ No Docker containers (clean)${NC}"
else
    echo -e "  ${YELLOW}⚠️  Found $((DOCKER_CONTAINERS-1)) Docker container(s)${NC}"
    docker ps -a
fi
echo ""

echo "📊 Step 7: Disk space check..."
if [ -d "target" ]; then
    echo -e "  ${RED}❌ target/ directory still exists${NC}"
else
    echo -e "  ${GREEN}✅ target/ directory cleaned${NC}"
fi
echo ""

echo "🎯 ================================================"
echo "🎯   CLEANUP COMPLETE!"
echo "🎯 ================================================"
echo ""
echo "Ready to run: ./mvnw clean verify -Pe2e-tests"
echo ""
