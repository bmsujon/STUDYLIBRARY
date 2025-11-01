# Study Library Manager

[![CI](https://github.com/bmsujon/STUDYLIBRARY/actions/workflows/ci.yml/badge.svg)](https://github.com/bmsujon/STUDYLIBRARY/actions/workflows/ci.yml)
[![Release](https://github.com/bmsujon/STUDYLIBRARY/actions/workflows/release.yml/badge.svg)](https://github.com/bmsujon/STUDYLIBRARY/actions/workflows/release.yml)
[![Java 25](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/projects/jdk/25/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern JavaFX desktop application for managing your personal study library including notes, PDFs, audio/video links, and text snippets. Built with **Java 25** and featuring modern language constructs like sealed classes, records, and enhanced pattern matching.

## Features

- **Multiple Item Types**: Manage Notes, PDFs, Audio/Video Links, and Text Snippets
- **Search & Filter**: Quickly find items by title, category, or tags
- **Category System**: Organize items with custom categories and tags
- **Metadata Tracking**: Automatic tracking of creation and modification dates
- **Persistent Storage**: All data saved as JSON files in your home directory
- **Modern UI**: Clean, responsive interface with CSS styling
- **Auto-save**: Changes are automatically saved

## Requirements

- **Java 25** or higher (LTS recommended)
- Maven 3.6+
- Supported platforms: Windows 10+, macOS 10.14+, Linux (Ubuntu 18.04+)

## 🚀 Java 25 Features

This application leverages cutting-edge Java 25 features for enhanced performance and developer experience:

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

Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, etc.) and run the `StudyLibraryApp` main class.

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
src/main/java/com/studylibrary/
├── StudyLibraryApp.java          # Main application entry point
├── model/                         # Domain models
│   ├── LibraryItem.java          # Abstract base class
│   ├── Note.java                 # Note item type
│   ├── PdfDocument.java          # PDF item type
│   ├── MediaLink.java            # Audio/Video link type
│   ├── TextSnippet.java          # Text snippet type
│   └── Category.java             # Category model
├── service/                       # Business logic layer
│   ├── LibraryService.java       # Main service interface
│   ├── LibraryServiceImpl.java   # Service implementation
│   └── StorageService.java       # File I/O operations
├── controller/                    # UI controllers
│   ├── MainController.java       # Main window controller
│   ├── ItemFormController.java   # Add/Edit form controller
│   └── ItemDetailsController.java # Details view controller
├── viewmodel/                     # ViewModels for data binding
│   └── LibraryViewModel.java     # Main view model
└── util/                          # Utility classes
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

- **macOS/Linux**: `~/.studylibrary/`
- **Windows**: `%USERPROFILE%\.studylibrary\`

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
