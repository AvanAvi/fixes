#!/bin/bash

echo "🔍 Checking Port Configuration (should all be 9090)..."
echo "=================================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0

# Function to check file for port
check_file() {
    local file=$1
    local expected_port=$2
    local pattern=$3
    local description=$4
    
    echo -n "Checking $description... "
    
    if [ ! -f "$file" ]; then
        echo -e "${RED}FILE NOT FOUND${NC}"
        ((ERRORS++))
        return
    fi
    
    if grep -q "$pattern" "$file"; then
        local found_port=$(grep "$pattern" "$file" | head -1)
        if echo "$found_port" | grep -q "$expected_port"; then
            echo -e "${GREEN}✓ OK${NC}"
        else
            echo -e "${RED}✗ WRONG PORT${NC}"
            echo "  Found: $found_port"
            ((ERRORS++))
        fi
    else
        echo -e "${YELLOW}⚠ PATTERN NOT FOUND${NC}"
        echo "  Pattern: $pattern"
        ((ERRORS++))
    fi
}

# Function to check for any remaining 8080 references
check_no_8080() {
    local file=$1
    local description=$2
    
    echo -n "Checking $description for leftover 8080... "
    
    if [ ! -f "$file" ]; then
        echo -e "${RED}FILE NOT FOUND${NC}"
        ((ERRORS++))
        return
    fi
    
    if grep -q "8080" "$file"; then
        echo -e "${RED}✗ FOUND 8080${NC}"
        echo "  Lines with 8080:"
        grep -n "8080" "$file" | sed 's/^/    /'
        ((ERRORS++))
    else
        echo -e "${GREEN}✓ OK${NC}"
    fi
}

echo "📋 Checking Configuration Files:"
echo "--------------------------------"

# 1. application.properties
check_file "src/main/resources/application.properties" "9090" "server.port" "application.properties"

# 2. Dockerfile
check_file "Dockerfile" "9090" "EXPOSE" "Dockerfile"

# 3. docker-compose.yml
check_file "docker-compose.yml" "9090:9090" "9090:9090" "docker-compose.yml ports mapping"

# 4. pom.xml - Check for property definition
echo -n "Checking pom.xml (e2e.server.port property)... "
if grep -q "<e2e\.server\.port>9090</e2e\.server\.port>" "pom.xml"; then
    echo -e "${GREEN}✓ OK${NC}"
elif grep -q "\-\-server\.port=9090" "pom.xml"; then
    echo -e "${GREEN}✓ OK (hardcoded)${NC}"
else
    echo -e "${RED}✗ WRONG${NC}"
    echo "  Should have: <e2e.server.port>9090</e2e.server.port> OR --server.port=9090"
    ((ERRORS++))
fi

# 5. pom.xml - Check for property usage in Spring Boot plugin
echo -n "Checking pom.xml (Spring Boot uses property)... "
if grep -q "\-\-server\.port=\${e2e\.server\.port}" "pom.xml"; then
    echo -e "${GREEN}✓ OK (using property)${NC}"
elif grep -q "\-\-server\.port=9090" "pom.xml"; then
    echo -e "${GREEN}✓ OK (hardcoded)${NC}"
else
    echo -e "${RED}✗ WRONG${NC}"
    echo "  Should have: --server.port=\${e2e.server.port} OR --server.port=9090"
    ((ERRORS++))
fi

# 6. pom.xml - Check for property usage in Failsafe
echo -n "Checking pom.xml (Failsafe uses property)... "
if grep -q "<server\.port>\${e2e\.server\.port}</server\.port>" "pom.xml"; then
    echo -e "${GREEN}✓ OK (using property)${NC}"
elif grep -q "<server\.port>9090</server\.port>" "pom.xml"; then
    echo -e "${GREEN}✓ OK (hardcoded)${NC}"
else
    echo -e "${RED}✗ WRONG${NC}"
    echo "  Should have: <server.port>\${e2e.server.port}</server.port> OR <server.port>9090</server.port>"
    ((ERRORS++))
fi

echo ""
echo "📋 Checking E2E Test Files:"
echo "----------------------------"

# E2E Test Files
E2E_FILES=(
    "src/e2e/java/com/attsw/bookstore/e2e/BookRestControllerE2E.java"
    "src/e2e/java/com/attsw/bookstore/e2e/BookWebE2E.java"
    "src/e2e/java/com/attsw/bookstore/e2e/CategoryRestControllerE2E.java"
    "src/e2e/java/com/attsw/bookstore/e2e/CategoryWebE2E.java"
)

for e2e_file in "${E2E_FILES[@]}"; do
    filename=$(basename "$e2e_file")
    check_file "$e2e_file" "9090" "server.port.*9090" "$filename"
done

echo ""
echo "🔎 Checking for Leftover 8080 References:"
echo "------------------------------------------"

# Check all files for any remaining 8080
check_no_8080 "src/main/resources/application.properties" "application.properties"
check_no_8080 "Dockerfile" "Dockerfile"
check_no_8080 "docker-compose.yml" "docker-compose.yml"
check_no_8080 "pom.xml" "pom.xml"

for e2e_file in "${E2E_FILES[@]}"; do
    filename=$(basename "$e2e_file")
    check_no_8080 "$e2e_file" "$filename"
done

echo ""
echo "=================================================="

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✅ All port configurations are correct (9090)!${NC}"
    exit 0
else
    echo -e "${RED}❌ Found $ERRORS error(s). Please fix the port configurations.${NC}"
    exit 1
fi
