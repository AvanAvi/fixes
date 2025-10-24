#!/bin/bash

echo "🔍 === PRE-TEST VERIFICATION ==="
echo ""

echo "1️⃣ Port 8080 status:"
if lsof -i :8080 > /dev/null 2>&1; then
    echo "❌ OCCUPIED"
    lsof -i :8080
else
    echo "✅ FREE"
fi
echo ""

echo "2️⃣ Port 9090 status:"
if lsof -i :9090 > /dev/null 2>&1; then
    echo "❌ OCCUPIED"
    lsof -i :9090
else
    echo "✅ FREE"
fi
echo ""

echo "3️⃣ Port 3307 (MySQL) status:"
if lsof -i :3307 > /dev/null 2>&1; then
    echo "✅ MySQL RUNNING (expected for E2E)"
    lsof -i :3307 | grep -E "COMMAND|mysql|docker"
else
    echo "⚠️ MySQL NOT running (will be started by Maven)"
fi
echo ""

echo "4️⃣ POM.xml configuration:"
if grep -q "e2e.server.port" pom.xml; then
    echo "✅ Found e2e.server.port property:"
    grep -A 1 "e2e.server.port" pom.xml | head -2
else
    echo "❌ Missing e2e.server.port property - NEEDS FIX!"
fi
echo ""

echo "5️⃣ Spring Boot plugin argument:"
if grep -q "server.port.*{e2e.server.port}" pom.xml; then
    echo "✅ Spring Boot will use port 9090"
else
    echo "❌ Spring Boot plugin not configured correctly - NEEDS FIX!"
fi
echo ""

echo "🎯 === VERIFICATION COMPLETE ==="
