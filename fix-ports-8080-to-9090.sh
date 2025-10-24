#!/bin/bash

echo "🔧 Automatically fixing all 8080 → 9090..."
echo "=========================================="

# Backup first
echo "Creating backups..."
cp src/main/resources/application.properties src/main/resources/application.properties.bak
cp Dockerfile Dockerfile.bak
cp docker-compose.yml docker-compose.yml.bak
cp pom.xml pom.xml.bak

# Fix each file
echo "Fixing application.properties..."
sed -i 's/server.port=8080/server.port=9090/g' src/main/resources/application.properties

echo "Fixing Dockerfile..."
sed -i 's/EXPOSE 8080/EXPOSE 9090/g' Dockerfile

echo "Fixing docker-compose.yml..."
sed -i 's/"8080:8080"/"9090:9090"/g' docker-compose.yml

echo "Fixing pom.xml..."
sed -i 's/--server.port=8080/--server.port=9090/g' pom.xml
sed -i 's/<server.port>8080<\/server.port>/<server.port>9090<\/server.port>/g' pom.xml

echo "Fixing E2E test files..."
find src/e2e/java -name "*E2E.java" -exec sed -i 's/"server.port", "8080"/"server.port", "9090"/g' {} +

echo ""
echo "✅ Done! Run ./verify-ports.sh to confirm."
echo ""
echo "⚠️  Backups created with .bak extension"
echo "   To restore: mv file.bak file"
