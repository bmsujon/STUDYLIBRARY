# Development Guide - Study Library Manager

## Project Overview

The Study Library Manager is a JavaFX desktop application following the **MVVM (Model-View-ViewModel)** architecture pattern with clear separation of concerns.

## Architecture

### Layer Structure

```
┌─────────────────────────────────────────┐
│           View Layer (FXML)             │
│  - MainView.fxml                        │
│  - ItemForm.fxml                        │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│      Controller Layer                   │
│  - MainController                       │
│  - ItemFormController                   │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│      ViewModel Layer                    │
│  - LibraryViewModel                     │
│    (Observable properties)              │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│      Service Layer                      │
│  - LibraryService (interface)           │
│  - LibraryServiceImpl                   │
│  - StorageService                       │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│      Model Layer                        │
│  - LibraryItem (abstract)               │
│  - Note, PdfDocument, MediaLink,        │
│    TextSnippet (concrete)               │
│  - Category                             │
└─────────────────────────────────────────┘
```

## Package Structure

### `com.documentvault.model`

Domain models representing the core business entities.

- **LibraryItem** (abstract sealed): Base class for all library items
  - Common properties: id, title, description, category, tags, dates
  - Abstract methods for type-specific behavior
- **Note**: Rich text or markdown notes
- **PdfDocument**: PDF file references with metadata
- **MediaLink**: Audio/Video URLs with media info
- **TextSnippet**: Code snippets and quick text
- **Category**: Organizational categories

### `com.documentvault.service`

Business logic and data operations.

- **LibraryService**: Interface defining all operations
- **LibraryServiceImpl**: Implementation with in-memory storage
- **StorageService**: JSON file I/O operations using Gson

### `com.documentvault.viewmodel`

View state management and data binding.

- **LibraryViewModel**:
  - Manages observable collections
  - Handles property bindings
  - Coordinates between view and service layers

### `com.documentvault.controller`

UI controllers for view components.

- **MainController**: Main window controller
- **ItemFormController**: Add/Edit form controller

### `com.documentvault.util`

Utility classes for common operations.

- **DateUtil**: Date formatting and relative time
- **FileUtil**: File and URL operations
- **AlertUtil**: Dialog utilities
- **SampleDataInitializer**: Demo data creation

## Key Design Patterns

### 1. Singleton Pattern

- **StorageService**: Single instance for file operations
- **LibraryServiceImpl**: Single instance for data management

### 2. Strategy Pattern

- Different item types (Note, PDF, etc.) implement specific behaviors

### 3. Observer Pattern

- JavaFX property binding for reactive UI updates
- ObservableList for automatic table updates

### 4. Factory Pattern (Implicit)

- Gson type adapters for polymorphic deserialization

## Data Flow

### Adding a New Item

```
User Input (View)
    ↓
Controller validates input
    ↓
Controller calls ViewModel.addItem()
    ↓
ViewModel calls Service.addItem()
    ↓
Service updates in-memory collection
    ↓
Service calls StorageService.saveItems()
    ↓
StorageService writes to JSON file
    ↓
ViewModel refreshes ObservableList
    ↓
UI table updates automatically
```

### Search/Filter Operation

```
User types in search field
    ↓
TextField is bound to ViewModel.searchQueryProperty
    ↓
Property change listener triggers
    ↓
ViewModel.filterItems() is called
    ↓
Service queries with filters applied
    ↓
ViewModel updates ObservableList
    ↓
UI reflects filtered results
```

## Storage Implementation

### JSON Structure

**library-items.json**:

```json
[
  {
    "type": "NOTE",
    "data": {
      "id": "uuid-here",
      "title": "Sample Note",
      "content": "Note content...",
      "isMarkdown": true,
      ...
    }
  },
  ...
]
```

### Polymorphic Serialization

Custom Gson adapter (`LibraryItemAdapter`) handles:

- Serialization: Wraps items with type information
- Deserialization: Routes to correct concrete class based on type

## Adding New Features

### Adding a New Item Type

1. **Create Model Class**

   ```java
   public class NewItemType extends LibraryItem {
       public NewItemType() {
           super(ItemType.NEW_TYPE);
       }
       // Add type-specific fields
   }
   ```

2. **Update ItemType Enum**

   ```java
   public enum ItemType {
       // ... existing types
       NEW_TYPE("Display Name");
   }
   ```

3. **Update StorageService Adapter**

   ```java
   case "NEW_TYPE" -> context.deserialize(data, NewItemType.class);
   ```

4. **Create FXML Form Section**

   - Add fields to ItemForm.fxml
   - Update ItemFormController

5. **Add Toolbar Button**
   - Update MainView.fxml
   - Add handler in MainController

### Adding a New Filter

1. **Add UI Control**

   ```xml
   <ComboBox fx:id="newFilterComboBox" />
   ```

2. **Add Property to ViewModel**

   ```java
   private final ObjectProperty<FilterType> newFilter;
   ```

3. **Update filterItems() Method**

   ```java
   if (newFilter.get() != null) {
       // Apply filter logic
   }
   ```

4. **Bind in Controller**
   ```java
   newFilterComboBox.valueProperty()
       .bindBidirectional(viewModel.newFilterProperty());
   ```

## Testing Strategy

### Implementation Status: ✅ COMPLETE

The project now includes a comprehensive test suite with **379 passing tests** providing 85%+ coverage across all core layers.

### Unit Tests ✅ COMPLETE

**Model Layer** (195 tests):

- ✅ NoteTest (14 tests) - Content, markdown, preview, searching
- ✅ CategoryTest (24 tests) - Category management, colors, equality
- ✅ PdfDocumentTest (34 tests) - File operations, metadata, formatting
- ✅ MediaLinkTest (41 tests) - URL validation, duration, media types
- ✅ TextSnippetTest (37 tests) - Content, language, line counting
- ✅ LibraryItemTest (45 tests) - Base class, tags, dates, inheritance

**Utilities** (72 tests):

- ✅ DateUtilTest (29 tests) - Date formatting, relative time
- ✅ FileUtilTest (43 tests) - File operations, URL validation

### Integration Tests ✅ COMPLETE

**Service Layer** (81 tests):

- ✅ LibraryServiceImplTest (58 tests) - CRUD operations, search, filtering, categories
  - Uses integration testing approach with real LibraryService
  - Tests business logic with actual service implementation
- ✅ StorageServiceTest (23 tests) - JSON persistence, file I/O, error handling
  - Uses @TempDir for file isolation
  - Tests polymorphic serialization with Gson

**ViewModel** (31 tests):

- ✅ LibraryViewModelTest (31 tests) - JavaFX property binding, observables, filters
  - Tests search/category/type filtering
  - Tests observable collection updates

### Testing Tools & Frameworks

- **JUnit 5** (5.10.0) - Modern testing framework with @DisplayName, @TempDir
- **AssertJ** (3.24.2) - Fluent assertions for readable tests
- **Maven Surefire** (3.1.2) - Test execution plugin
- **@TempDir** - File I/O isolation for integration tests
- **Reflection API** - Singleton instance reset for test independence

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LibraryServiceImplTest

# Run specific test method
mvn test -Dtest=NoteTest#shouldGenerateUniqueId

# Clean build and test
mvn clean test
```

### Test Statistics

- **Total Tests**: 379
- **Pass Rate**: 100% (379/379)
- **Execution Time**: < 10 seconds
- **Coverage**: 85%+ on tested layers
- **Test Classes**: 11
- **Build Status**: ✅ SUCCESS

### UI Tests ⏸️ OPTIONAL

- TestFX framework available but not yet implemented
- UI testing considered optional for future enhancement
- Controller tests can be added using TestFX for JavaFX UI workflows

## Performance Considerations

### Current Implementation

- In-memory storage: Fast for small to medium libraries (< 10,000 items)
- Full table scan for searches: O(n) complexity
- File I/O on every operation: Ensures data persistence

### Optimization Opportunities

1. **Lazy Loading**: Load items on demand
2. **Indexing**: Add search index for faster queries
3. **Batch Operations**: Save in batches instead of individual operations
4. **Virtual Scrolling**: For very large tables
5. **Background Threads**: For file I/O operations

## Code Style Guidelines

### Naming Conventions

- **Classes**: PascalCase (e.g., `LibraryItem`)
- **Methods**: camelCase (e.g., `addItem`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `APP_TITLE`)
- **Private fields**: camelCase with descriptive names

### Documentation

- All public methods must have Javadoc
- Complex logic should have inline comments
- Each class should have a class-level Javadoc

### Error Handling

- Use try-catch for I/O operations
- Show user-friendly error messages via AlertUtil
- Log errors to console for debugging

## Build Configuration

### Maven Dependencies

```xml
<!-- Core dependencies -->
- JavaFX Controls (UI components)
- JavaFX FXML (declarative UI)
- JavaFX Web (future: rich text editor)
- Gson (JSON serialization)
```

### Maven Plugins

```xml
- maven-compiler-plugin (Java 17)
- javafx-maven-plugin (running JavaFX apps)
```

## Future Enhancements

### Short-term (v1.1)

- [ ] Category management dialog
- [ ] Export to different formats (CSV, Markdown)
- [ ] Import functionality
- [ ] Settings/Preferences dialog
- [ ] Dark mode theme

### Medium-term (v1.2)

- [ ] Rich text editor for notes (using JavaFX WebView)
- [ ] PDF preview in app
- [ ] Advanced search with operators
- [ ] Item linking/relationships
- [ ] Statistics dashboard

### Long-term (v2.0)

- [ ] Cloud sync support
- [ ] Mobile companion app
- [ ] Collaborative features
- [ ] Plugin system
- [ ] Database backend option

## Contributing

### Before Submitting Changes

1. **Format Code**: Follow existing style
2. **Update Documentation**: Keep README and guides current
3. **Test Thoroughly**: All features should work
4. **Update Version**: Follow semantic versioning
5. **Write Clear Commits**: Descriptive commit messages

### Code Review Checklist

- [ ] Code follows style guidelines
- [ ] All public methods documented
- [ ] Error handling implemented
- [ ] No hardcoded values
- [ ] Resource files properly located
- [ ] No console output in production code (except errors)

## Troubleshooting Development Issues

### JavaFX Not Found

```bash
# Verify JavaFX is in dependencies
mvn dependency:tree | grep javafx

# Clean and rebuild
mvn clean install
```

### FXML Loading Errors

- Check controller path in fx:controller attribute
- Verify fx:id matches field names in controller
- Ensure @FXML annotations present

### Gson Serialization Errors

- Check type adapters are registered
- Verify JSON structure matches model
- Test with simple data first

## Resources

### Documentation

- [JavaFX Documentation](https://openjfx.io/)
- [Gson User Guide](https://github.com/google/gson/blob/master/UserGuide.md)
- [Maven Documentation](https://maven.apache.org/guides/)

### Tools

- **IDE**: IntelliJ IDEA (recommended), Eclipse, or VS Code
- **Scene Builder**: For visual FXML editing
- **JSON Viewer**: For debugging storage files

---

**Version**: 1.0.0  
**Created**: October 2025  
**Last Updated**: October 22, 2025 (Testing Implementation Complete)  
**Test Status**: ✅ 379 Tests Passing (100%)  
**Maintainer**: Development Team
