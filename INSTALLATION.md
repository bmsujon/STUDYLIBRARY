# Installation & Setup Guide - Study Library Manager

## Table of Contents

1. [System Requirements](#system-requirements)
2. [Installing Prerequisites](#installing-prerequisites)
3. [Building the Application](#building-the-application)
4. [Running the Application](#running-the-application)
5. [First-Time Setup](#first-time-setup)
6. [Troubleshooting](#troubleshooting)
7. [Uninstallation](#uninstallation)

## System Requirements

### Minimum Requirements

- **OS**: macOS 10.14+, Windows 10+, or Linux (Ubuntu 18.04+, Fedora 30+)
- **Java**: OpenJDK or Oracle JDK 17 or higher
- **Maven**: Apache Maven 3.6 or higher
- **RAM**: 512 MB available
- **Disk Space**: 100 MB for application + storage for your library data

### Recommended Requirements

- **Java**: OpenJDK 17 or 21
- **RAM**: 1 GB available
- **Disk Space**: 500 MB

## Installing Prerequisites

### macOS

#### Install Homebrew (if not already installed)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

#### Install Java 17

```bash
# Using Homebrew
brew install openjdk@17

# Add to PATH (add to ~/.zshrc or ~/.bash_profile)
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc

# Verify installation
java -version
```

#### Install Maven

```bash
brew install maven

# Verify installation
mvn -version
```

### Windows

#### Install Java 17

1. **Download Java**:

   - Visit [Adoptium.net](https://adoptium.net/)
   - Download JDK 17 (LTS) for Windows
   - Choose the `.msi` installer

2. **Install**:

   - Run the downloaded installer
   - Follow the installation wizard
   - Check "Set JAVA_HOME variable"
   - Check "Add to PATH"

3. **Verify**:
   ```cmd
   java -version
   ```

#### Install Maven

1. **Download Maven**:

   - Visit [Maven Downloads](https://maven.apache.org/download.cgi)
   - Download the Binary zip archive

2. **Extract**:

   - Extract to `C:\Program Files\Apache\maven`

3. **Set Environment Variables**:

   - Open System Properties → Environment Variables
   - Add `MAVEN_HOME`: `C:\Program Files\Apache\maven`
   - Add to `Path`: `%MAVEN_HOME%\bin`

4. **Verify**:
   ```cmd
   mvn -version
   ```

### Linux (Ubuntu/Debian)

#### Install Java 17

```bash
# Update package list
sudo apt update

# Install OpenJDK 17
sudo apt install openjdk-17-jdk

# Verify installation
java -version
```

#### Install Maven

```bash
# Install Maven
sudo apt install maven

# Verify installation
mvn -version
```

### Linux (Fedora/RHEL)

#### Install Java 17

```bash
# Install OpenJDK 17
sudo dnf install java-17-openjdk-devel

# Verify installation
java -version
```

#### Install Maven

```bash
# Install Maven
sudo dnf install maven

# Verify installation
mvn -version
```

## Building the Application

### Option 1: Using the Build Scripts (Recommended)

#### macOS/Linux

```bash
cd /path/to/StudyLibrary
chmod +x run.sh
./run.sh
```

#### Windows

```cmd
cd C:\path\to\StudyLibrary
run.bat
```

The scripts will:

1. Check Java and Maven installation
2. Clean previous builds
3. Compile the application
4. Package everything
5. Run the application

### Option 2: Manual Build

```bash
# Navigate to project directory
cd /path/to/StudyLibrary

# Clean previous builds
mvn clean

# Compile and package
mvn install

# The build artifacts will be in target/ directory
```

### Build Output

After successful build, you should see:

```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX.XXX s
[INFO] Finished at: YYYY-MM-DDTHH:MM:SS
```

## Running the Application

### Option 1: Using Maven Plugin (Development)

```bash
cd /path/to/StudyLibrary
mvn javafx:run
```

### Option 2: Using JAR File (if packaged)

```bash
java -jar target/study-library-1.0.0.jar
```

### Option 3: Using IDE

#### IntelliJ IDEA

1. Open the project (`File → Open` → select `StudyLibrary` folder)
2. Wait for Maven import to complete
3. Right-click on `StudyLibraryApp.java`
4. Select `Run 'StudyLibraryApp.main()'`

#### Eclipse

1. Import as Maven project (`File → Import → Maven → Existing Maven Projects`)
2. Select the `StudyLibrary` folder
3. Right-click on `StudyLibraryApp.java`
4. Select `Run As → Java Application`

#### VS Code

1. Open the project folder
2. Install "Extension Pack for Java" if not installed
3. Open `StudyLibraryApp.java`
4. Click "Run" above the main method

## First-Time Setup

### Initial Launch

When you first run the application:

1. **Data Directory Creation**:

   - The app creates `~/.studylibrary/` folder
   - This contains JSON files for your data

2. **Empty Library**:

   - You'll see an empty table
   - No categories or items yet

3. **Optional: Load Sample Data**:
   ```bash
   # Run from project root
   cd src/main/java
   java com.studylibrary.util.SampleDataInitializer
   ```
   Then restart the application.

### Creating Your First Items

1. **Add a Category**:

   - When adding your first item
   - Enter a category name in the category field
   - It will be created automatically

2. **Add Your First Note**:

   - Click `+ Note` or press `Ctrl+N`
   - Fill in title: "My First Note"
   - Add description and tags
   - Click Save

3. **Test Search**:
   - Type in the search bar
   - Items filter in real-time

### Recommended Initial Setup

1. **Create Common Categories**:
   - Add items with categories: "Work", "Study", "Personal"
2. **Define Tag System**:

   - Decide on tag naming convention
   - Example: lowercase, hyphen-separated

3. **Import Existing Data** (if applicable):

   - Place existing PDFs in accessible locations
   - Add them to the library with correct paths

4. **Backup Setup**:
   - Note the location: `~/.studylibrary/`
   - Set up automatic backup (cloud sync, etc.)

## Troubleshooting

### Application Won't Start

#### "Java not found" Error

```bash
# Check Java installation
java -version

# If not installed, install Java 17 (see above)

# Check JAVA_HOME
echo $JAVA_HOME  # macOS/Linux
echo %JAVA_HOME%  # Windows
```

#### "Maven not found" Error

```bash
# Check Maven installation
mvn -version

# If not installed, install Maven (see above)
```

#### JavaFX Module Errors

```
Error: JavaFX runtime components are missing
```

**Solution**: JavaFX is included in dependencies. Run:

```bash
mvn clean install
mvn javafx:run
```

### Build Failures

#### Compilation Errors

```bash
# Clean and rebuild
mvn clean
mvn install -U  # -U forces update of dependencies
```

#### Dependency Download Failures

```bash
# Clear Maven cache
rm -rf ~/.m2/repository/org/openjfx

# Retry build
mvn clean install
```

### Runtime Errors

#### "Cannot load FXML" Error

**Cause**: Resource files not found  
**Solution**: Ensure you're running from project root

```bash
cd /path/to/StudyLibrary
mvn javafx:run
```

#### "Storage directory access denied"

**Cause**: Permission issues  
**Solution**: Check permissions

```bash
# macOS/Linux
ls -la ~/.studylibrary
chmod 755 ~/.studylibrary
```

#### "JSON parse error"

**Cause**: Corrupted JSON file  
**Solution**:

1. Backup current files
2. Delete corrupted JSON
3. Restart application (creates new empty files)

### Performance Issues

#### Slow Startup

- Check available RAM
- Close other applications
- Verify Java version (17+)

#### Slow Search

- Reduce number of items
- Check CPU usage
- Restart application

### Data Issues

#### Lost Data

1. Check `~/.studylibrary/` folder exists
2. Verify JSON files are not empty
3. Check file permissions
4. Restore from backup

#### Cannot Save

1. Check disk space: `df -h`
2. Verify write permissions
3. Check file is not locked

## Uninstallation

### Remove Application

#### Delete Project Folder

```bash
# Navigate to parent directory
cd /path/to/parent

# Remove project
rm -rf StudyLibrary
```

### Remove User Data

**⚠️ Warning**: This deletes all your library data!

#### macOS/Linux

```bash
rm -rf ~/.studylibrary
```

#### Windows

```cmd
rmdir /s %USERPROFILE%\.studylibrary
```

### Keep Data for Future Use

If you want to uninstall but keep your data:

1. Backup `~/.studylibrary/` folder
2. Remove the application folder
3. Keep the backup for later restoration

### Clean Uninstall

```bash
# Remove application
rm -rf /path/to/StudyLibrary

# Remove data
rm -rf ~/.studylibrary

# Clear Maven cache (optional)
rm -rf ~/.m2/repository/com/studylibrary

# Remove global IDE configurations (optional)
# IntelliJ: Delete .idea folder
# Eclipse: Delete .settings, .project, .classpath
```

## Advanced Setup

### Creating Desktop Shortcut

#### macOS

Create `StudyLibrary.command`:

```bash
#!/bin/bash
cd /path/to/StudyLibrary
mvn javafx:run
```

Make executable: `chmod +x StudyLibrary.command`

#### Windows

Create `StudyLibrary.bat`:

```cmd
@echo off
cd C:\path\to\StudyLibrary
mvn javafx:run
pause
```

#### Linux

Create `StudyLibrary.desktop`:

```ini
[Desktop Entry]
Version=1.0
Type=Application
Name=Study Library Manager
Exec=/path/to/StudyLibrary/run.sh
Icon=/path/to/icon.png
Terminal=false
Categories=Education;
```

### Building Standalone Executable

#### Using jpackage (Java 17+)

```bash
# Build the application
mvn clean package

# Create installer (macOS)
jpackage --input target \
         --name StudyLibrary \
         --main-jar study-library-1.0.0.jar \
         --main-class com.studylibrary.StudyLibraryApp \
         --type dmg

# Windows: use --type msi
# Linux: use --type deb or --type rpm
```

### Customizing Installation

#### Change Data Location

Edit `StorageService.java`:

```java
private static final String APP_DIR_NAME = "your-custom-folder";
```

#### Set Custom Theme

Edit `application.css`:

```css
.root {
  -fx-primary-color: #YOUR_COLOR;
}
```

## Getting Help

### Documentation

- **README.md**: Project overview
- **USER_GUIDE.md**: Complete user manual
- **DEVELOPMENT.md**: Developer documentation
- **QUICK_REFERENCE.md**: Quick tips

### Support

- Check documentation files
- Review error messages carefully
- Verify prerequisites are installed
- Try clean build: `mvn clean install`

## Post-Installation Checklist

After successful installation:

- [ ] Java 17+ verified
- [ ] Maven 3.6+ verified
- [ ] Application builds successfully
- [ ] Application runs without errors
- [ ] Can create new items
- [ ] Search functionality works
- [ ] Data persists after restart
- [ ] Backup strategy in place

## Next Steps

1. **Read the User Guide**: `USER_GUIDE.md`
2. **Try Sample Data**: Run `SampleDataInitializer`
3. **Create Your First Items**: Add notes, PDFs, etc.
4. **Explore Features**: Search, filter, organize
5. **Set Up Backup**: Configure automatic backup

---

**Need Help?** Check the troubleshooting section above or review the documentation files.

**Version**: 1.0.0  
**Last Updated**: October 2025
