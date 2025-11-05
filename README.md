# DocumentVault

[![CI](https://github.com/bmsujon/DocumentVault/actions/workflows/ci.yml/badge.svg)](https://github.com/bmsujon/DocumentVault/actions/workflows/ci.yml)
[![Release](https://github.com/bmsujon/DocumentVault/actions/workflows/release.yml/badge.svg)](https://github.com/bmsujon/DocumentVault/actions/workflows/release.yml)
[![Java 25 LTS](https://img.shields.io/badge/Java-25%20LTS-orange.svg)](https://openjdk.java.net/projects/jdk/25/)
[![Privacy First](https://img.shields.io/badge/Privacy-First-green.svg)]()
[![Offline Only](https://img.shields.io/badge/Offline-Only-blue.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Your Personal Document Security System**

A privacy-focused, offline-first JavaFX desktop application for managing your personal documents with bank-level security. Designed for individuals and professionals who need secure, private document management without cloud dependencies. Built with **Java 25 LTS** and featuring enterprise-grade security for sensitive documents including medical records, certificates, financial documents, and study materials.

## 🔒 Privacy & Security First

- **100% Offline**: Your documents never leave your device - no cloud, no servers, no data transmission
- **Bank-Level Encryption**: AES-256 encryption for sensitive documents (medical, financial, legal)
- **Zero Tracking**: No analytics, no telemetry, no data collection - your privacy is absolute
- **Local Storage Only**: All data stored locally in encrypted format on your machine
- **Professional Grade**: HIPAA-ready security for healthcare professionals and privacy-conscious users

## 📚 Document Management Features

- **Comprehensive Document Types**: Study materials, medical records, certificates, financial documents, legal papers
- **Advanced Security Levels**: Public, Sensitive, Restricted, and Confidential classifications
- **Smart Organization**: Categories, tags, and intelligent search across all document types
- **Expiration Tracking**: Automatic alerts for certificates, licenses, and time-sensitive documents
- **Audit Trail**: Complete access logging for sensitive documents
- **Multi-Factor Security**: Optional enhanced authentication for confidential documents
- **Auto-save**: Changes are automatically saved with encryption

## Requirements

- **Java 25 LTS** or higher (Long-Term Support release with premier support until September 2030)
- Maven 3.6+
- Supported platforms: Windows 10+, macOS 10.14+, Linux (Ubuntu 18.04+)

## 🚀 Java 25 LTS Features

This application leverages modern Java 25 LTS features for enhanced performance, stability, and developer experience:

- **Sealed Classes**: Type-safe inheritance hierarchy for `LibraryItem`
- **Records**: Immutable data classes like `SearchCriteria` with zero boilerplate
- **Pattern Matching**: Enhanced `instanceof` and switch expressions
- **Performance Optimizations**: Parallel streams and modern JVM improvements
- **Memory Efficiency**: Records and optimized string operations

### Performance Benchmarks

- **Search Operations**: 0.004-0.26 ms per operation
- **Pattern Matching**: 0.391 ms/op for type checking
- **Memory Usage**: Efficient garbage collection with minimal overhead
- **Test Coverage**: 379 passing tests (100% compatibility)

## Building and Running

### Using Maven

```bash
# Build the project
mvn clean install

# Run the application
mvn javafx:run
```

### Using IDE

Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, etc.) and run the `DocumentVaultApp` main class (or use the `Launcher` class for JavaFX compatibility).

## Testing

The project includes a comprehensive test suite with **379 passing tests** covering all core layers:

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=NoteTest

# Run tests with coverage report
mvn clean test
```

### Test Coverage

- **Total Tests**: 379 (100% passing)
- **Model Layer**: 195 tests (90%+ coverage)
- **Service Layer**: 81 tests (85%+ coverage)
- **ViewModel**: 31 tests (80%+ coverage)
- **Utilities**: 72 tests (85%+ coverage)
- **Overall Coverage**: 85%+ on tested layers

For detailed testing information, see:

- `TESTING_PLAN.md` - Comprehensive testing strategy and implementation status
- `TEST_SETUP_README.md` - Test infrastructure setup and usage guide

## Project Structure

```
src/main/java/com/documentvault/
├── DocumentVaultApp.java         # Main application entry point
├── Launcher.java                 # JavaFX launcher wrapper
├── model/                        # Domain models
│   ├── LibraryItem.java          # Abstract sealed base class
│   ├── Note.java                 # Note item type
│   ├── PdfDocument.java          # PDF item type
│   ├── MediaLink.java            # Audio/Video link type
│   ├── TextSnippet.java          # Text snippet type
│   ├── Category.java             # Category model
│   └── SearchCriteria.java       # Search record (Java 25)
├── service/                      # Business logic layer
│   ├── LibraryService.java       # Main service interface
│   ├── LibraryServiceImpl.java   # Service implementation
│   └── StorageService.java       # File I/O operations
├── controller/                   # UI controllers
│   ├── MainController.java       # Main window controller
│   └── ItemFormController.java   # Add/Edit form controller
├── viewmodel/                    # ViewModels for data binding
│   └── LibraryViewModel.java     # Main view model
└── util/                         # Utility classes
    ├── BrandConstants.java       # Brand identity constants
    ├── DateUtil.java             # Date formatting utilities
    ├── FileUtil.java             # File handling utilities
    └── AlertUtil.java            # Dialog utilities

src/main/resources/
├── fxml/                          # FXML view files
│   ├── MainView.fxml
│   ├── ItemForm.fxml
│   └── ItemDetails.fxml
├── css/                           # Stylesheets
│   └── application.css
└── icons/                         # Application icons
```

## Data Storage

All library data is stored in JSON format in the following location:

- **macOS/Linux**: `~/.documentvault/`
- **Windows**: `%USERPROFILE%\.documentvault\`

Files:

- `library-items.json` - All library items
- `categories.json` - Category definitions

## Usage

1. **Adding Items**: Click the "+" button or use File → New Item
2. **Editing Items**: Double-click an item or select and click Edit
3. **Deleting Items**: Select item(s) and press Delete or click the trash icon
4. **Searching**: Use the search bar at the top to filter items
5. **Categories**: Manage categories from View → Categories menu

## License

MIT License - Feel free to use and modify as needed.
