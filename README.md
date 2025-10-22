# Study Library Manager

A modern JavaFX desktop application for managing your personal study library including notes, PDFs, audio/video links, and text snippets.

## Features

- **Multiple Item Types**: Manage Notes, PDFs, Audio/Video Links, and Text Snippets
- **Search & Filter**: Quickly find items by title, category, or tags
- **Category System**: Organize items with custom categories and tags
- **Metadata Tracking**: Automatic tracking of creation and modification dates
- **Persistent Storage**: All data saved as JSON files in your home directory
- **Modern UI**: Clean, responsive interface with CSS styling
- **Auto-save**: Changes are automatically saved

## Requirements

- Java 17 or higher
- Maven 3.6+

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
