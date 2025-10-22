@echo off
REM Study Library Manager - Build and Run Script for Windows
REM This script builds the project using Maven and runs the application

echo ================================================
echo   Study Library Manager - Build ^& Run Script
echo ================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo [OK] Java is installed
echo.

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo [OK] Maven is installed
echo.

REM Clean and build the project
echo Building project...
call mvn clean install

if %errorlevel% neq 0 (
    echo.
    echo Build failed. Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo [OK] Build successful!
echo.
echo Starting Study Library Manager...
echo.

REM Run the application
call mvn javafx:run

echo.
echo ================================================
echo   Thank you for using Study Library Manager!
echo ================================================
pause
