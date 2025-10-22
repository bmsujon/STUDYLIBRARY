#!/bin/bash

# Study Library Manager - Build and Run Script
# This script builds the project using Maven and runs the application

echo "================================================"
echo "  Study Library Manager - Build & Run Script"
echo "================================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "   Please install Java 17 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Error: Java 17 or higher is required"
    echo "   Current version: $(java -version 2>&1 | head -n 1)"
    exit 1
fi

echo "✅ Java version check passed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Error: Maven is not installed or not in PATH"
    echo "   Please install Maven 3.6 or higher"
    exit 1
fi

echo "✅ Maven check passed"
echo ""

# Clean and build the project
echo "📦 Building project..."
mvn clean install

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""
echo "🚀 Starting Study Library Manager..."
echo ""

# Run the application
mvn javafx:run

echo ""
echo "================================================"
echo "  Thank you for using Study Library Manager!"
echo "================================================"
