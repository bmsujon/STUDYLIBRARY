# DocumentVault v1.3.0 - Focused Sprint Plan

**Duration:** 12 weeks  
**Start:** November 2025  
**Release:** February 2026  
**Scope:** Original v1.2.0 features ONLY (security deferred to v1.4.0)

---

## 🎯 **Sprint Goals**

**Sprint 1 (Weeks 1-3):** UI/UX Foundation - Dark mode, settings, table enhancements  
**Sprint 2 (Weeks 4-5):** Search & Categories - Advanced search, visual categories  
**Sprint 3 (Weeks 6-8):** Data Management - Export, import, backup/restore  
**Sprint 4 (Weeks 9-12):** Integration & Release - Testing, documentation, ship it

---

## 🏃 **Sprint 1: UI/UX Foundation (Weeks 1-3)**

### **Week 1: Dark Mode Theme System**

#### **Monday: Theme CSS Development**

**Tasks:**

- [ ] Create `src/main/resources/css/dark-theme.css`
- [ ] Define professional dark color palette
- [ ] Refine `light-theme.css` for consistency
- [ ] Test all UI components with both themes

**Color Palette (Dark):**

```css
Background: #1e1e1e
Surface: #252526
Elevated: #2d2d30
Border: #3e3e42
Text Primary: #cccccc
Text Secondary: #858585
Accent: #007acc
```

**Deliverable:** Complete CSS files

---

#### **Tuesday-Wednesday: Theme Manager**

**Tasks:**

- [ ] Create `com.documentvault.theme` package
- [ ] Implement `ThemeManager.java` singleton
- [ ] Add theme preference storage in settings
- [ ] Implement theme switching logic with fade transition

```java
public class ThemeManager {
    private static final ThemeManager INSTANCE = new ThemeManager();

    public enum Theme { LIGHT, DARK }

    public void applyTheme(Theme theme) {
        // Load CSS, apply with 200ms fade
    }

    public Theme getCurrentTheme() {
        return SettingsManager.getInstance().getTheme();
    }
}
```

**Deliverable:** Working theme manager

---

#### **Thursday: Theme UI Integration**

**Tasks:**

- [ ] Add "View > Theme" menu item
- [ ] Add keyboard shortcut Cmd/Ctrl+Shift+T
- [ ] Add theme toggle to toolbar (optional)
- [ ] Test theme switching in all dialogs

**Deliverable:** Complete theme switcher UI

---

#### **Friday: Testing & Polish**

**Tasks:**

- [ ] Test all UI components in both themes
- [ ] Fix any contrast/readability issues
- [ ] Test theme persistence across app restarts
- [ ] Code review and documentation

**Acceptance Criteria:**
✅ Dark and light themes complete  
✅ Theme switch < 200ms  
✅ Theme preference persisted  
✅ All UI elements styled consistently

---

### **Week 2: Settings & Preferences System**

#### **Monday-Tuesday: Settings Framework**

**Tasks:**

- [ ] Create `com.documentvault.settings` package
- [ ] Implement `Setting<T>` generic class
- [ ] Implement `SettingsManager` singleton
- [ ] Design settings JSON schema
- [ ] Implement `SettingsStorage` (load/save JSON)

```java
public class SettingsManager {
    private static final SettingsManager INSTANCE = new SettingsManager();

    // Settings
    public Setting<Theme> theme = new Setting<>("theme", Theme.LIGHT);
    public Setting<Integer> autoSaveIntervalSeconds = new Setting<>("autoSaveInterval", 60);
    public Setting<String> dateFormat = new Setting<>("dateFormat", "yyyy-MM-dd");

    public void load() { /* Load from ~/.documentvault/settings.json */ }
    public void save() { /* Save to JSON */ }
}
```

**Deliverable:** Settings framework

---

#### **Wednesday-Thursday: Settings Dialog UI**

**Tasks:**

- [ ] Create `SettingsDialog.fxml` with tabs
- [ ] Create `SettingsController.java`
- [ ] Implement 4 tabs: General, Display, Data, Advanced
- [ ] Add controls for all settings

**Tab Structure:**

- **General:** Theme, startup behavior
- **Display:** Font size, table density, date/time format
- **Data:** Auto-save, backup location, export defaults
- **Advanced:** Debug mode, memory display, reset button

**Deliverable:** Complete settings dialog

---

#### **Friday: Integration & Testing**

**Tasks:**

- [ ] Wire settings to all features
- [ ] Add "Preferences" menu item (Cmd/Ctrl+,)
- [ ] Test settings persistence
- [ ] Test validation (prevent invalid values)
- [ ] Test "Reset to Defaults" button

**Acceptance Criteria:**
✅ All settings functional  
✅ Settings persist across restarts  
✅ Validation works  
✅ Dialog responsive and polished

---

### **Week 3: Table View Enhancements**

#### **Monday-Tuesday: Column Management**

**Tasks:**

- [ ] Create `TableViewEnhancer` utility class
- [ ] Implement resizable columns (drag borders)
- [ ] Implement reorderable columns (drag headers)
- [ ] Add column visibility controls (right-click menu)

```java
public class TableViewEnhancer {
    public static void enableAdvancedFeatures(TableView<?> table, String tableId) {
        enableColumnReordering(table);
        enableColumnResizing(table);
        enableColumnVisibility(table);
        loadColumnState(table, tableId);
    }
}
```

**Deliverable:** Column management features

---

#### **Wednesday: Density Modes**

**Tasks:**

- [ ] Create CSS for 3 density modes
- [ ] Implement density mode switching
- [ ] Add View menu items for density
- [ ] Add density setting to preferences

**Density Modes:**

- **Compact:** -fx-padding: 2px 8px;
- **Comfortable:** -fx-padding: 4px 8px; (default)
- **Spacious:** -fx-padding: 8px 12px;

**Deliverable:** Working density modes

---

#### **Thursday: Visual Polish**

**Tasks:**

- [ ] Add alternating row colors
- [ ] Improve row hover effects
- [ ] Enhance selection highlighting
- [ ] Update context menus with more actions

**Deliverable:** Polished table view

---

#### **Friday: State Persistence & Testing**

**Tasks:**

- [ ] Implement column state persistence
- [ ] Test column resize/reorder
- [ ] Test density modes
- [ ] Test state persistence across restarts

**Acceptance Criteria:**
✅ Columns resizable/reorderable  
✅ Density modes work  
✅ State persists  
✅ Visual improvements look professional

---

## 🏃 **Sprint 2: Search & Categories (Weeks 4-5)**

### **Week 4: Advanced Search System**

#### **Monday-Tuesday: Query Parser**

**Tasks:**

- [ ] Create `com.documentvault.search` package
- [ ] Implement `QueryParser` for operator-based queries
- [ ] Support field search: `title:keyword`, `content:text`
- [ ] Support boolean: `AND`, `OR`, `NOT`
- [ ] Support exact phrases: `"exact phrase"`
- [ ] Support wildcards: `key*`
- [ ] Support date ranges: `created:2025-01-01..2025-12-31`

```java
public class QueryParser {
    public SearchQuery parse(String queryString) {
        // Parse "title:security AND category:work" into SearchQuery
    }
}

public record SearchQuery(
    List<FieldFilter> filters,
    BooleanOperator operator,
    DateRange dateRange
) {}
```

**Deliverable:** Working query parser

---

#### **Wednesday-Thursday: Search Engine**

**Tasks:**

- [ ] Implement `SearchEngine.java`
- [ ] Create in-memory search index
- [ ] Implement search execution
- [ ] Add result highlighting
- [ ] Optimize for 10,000+ items

```java
public class SearchEngine {
    public List<LibraryItem> search(SearchQuery query) {
        // Execute search, return results
    }

    public List<SearchResult> searchWithHighlights(SearchQuery query) {
        // Return results with match highlights
    }
}
```

**Deliverable:** Working search engine

---

#### **Friday: Search UI & Features**

**Tasks:**

- [ ] Add search mode toggle (Simple/Advanced/Regex)
- [ ] Implement search history (last 20 queries)
- [ ] Add saved searches feature
- [ ] Update search bar UI

**Deliverable:** Complete search system

**Acceptance Criteria:**
✅ All operators work correctly  
✅ Search < 100ms for 10,000 items  
✅ Search history and saved searches work  
✅ UI intuitive and responsive

---

### **Week 5: Enhanced Categories**

#### **Monday-Tuesday: Extended Category Model**

**Tasks:**

- [ ] Extend `Category` record with color and icon
- [ ] Create `CategoryColor` enum (16 colors)
- [ ] Create `CategoryIcon` enum (50+ icons)
- [ ] Update `StorageService` for new schema
- [ ] Migrate existing categories

```java
public record Category(
    String id,
    String name,
    String description,
    CategoryColor color,
    CategoryIcon icon,
    LocalDateTime createdDate
) {}

public enum CategoryColor {
    BLUE("#007acc"), GREEN("#4ec9b0"), ORANGE("#ce9178"),
    RED("#f48771"), PURPLE("#c586c0"), YELLOW("#dcdcaa"),
    // ... 10 more
}
```

**Deliverable:** Extended category model

---

#### **Wednesday-Thursday: Category Management UI**

**Tasks:**

- [ ] Create category management dialog
- [ ] Add color picker (16-color palette)
- [ ] Add icon selector (grid of icons)
- [ ] Add visual preview
- [ ] Update category dropdown with colors/icons

**Deliverable:** Category management dialog

---

#### **Friday: Visual Integration**

**Tasks:**

- [ ] Add color badges in table view
- [ ] Add icons to category dropdown
- [ ] Update category statistics with colors
- [ ] Test visual consistency

**Acceptance Criteria:**
✅ All colors render correctly  
✅ All icons display properly  
✅ Category management intuitive  
✅ Visual cues clear and professional

---

## 🏃 **Sprint 3: Data Management (Weeks 6-8)**

### **Week 6: Export System**

#### **Monday-Tuesday: Export Framework**

**Tasks:**

- [ ] Create `com.documentvault.export` package
- [ ] Define `Exporter` interface
- [ ] Implement `ExportService` coordinator
- [ ] Create export dialog UI
- [ ] Add progress tracking

```java
public interface Exporter {
    String getFormatName();
    String getFileExtension();
    void export(List<LibraryItem> items, File destination) throws ExportException;
}

public class ExportService {
    private Map<String, Exporter> exporters = new HashMap<>();

    public void registerExporter(Exporter exporter) { }
    public void export(ExportOptions options) { }
}
```

**Deliverable:** Export framework

---

#### **Wednesday: JSON & CSV Exporters**

**Tasks:**

- [ ] Implement `JsonExporter`
- [ ] Add OpenCSV dependency to pom.xml
- [ ] Implement `CsvExporter`
- [ ] Test both formats

**Deliverable:** JSON and CSV exporters

---

#### **Thursday: Markdown Exporter**

**Tasks:**

- [ ] Implement `MarkdownExporter`
- [ ] Format with frontmatter metadata
- [ ] Organize by categories
- [ ] Add table of contents

**Deliverable:** Markdown exporter

---

#### **Friday: Export Dialog & Testing**

**Tasks:**

- [ ] Complete export dialog UI
- [ ] Add format selector with preview
- [ ] Test large exports (10,000 items)
- [ ] Performance optimization

**Acceptance Criteria:**
✅ JSON, CSV, Markdown export working  
✅ Large exports succeed (10,000 items)  
✅ Export < 10 seconds for 1000 items  
✅ Dialog intuitive and responsive

---

### **Week 7: Import System**

#### **Monday-Tuesday: Import Framework**

**Tasks:**

- [ ] Create `com.documentvault.import` package
- [ ] Define `Importer` interface
- [ ] Implement `ImportService` coordinator
- [ ] Implement `ImportValidator`
- [ ] Create import preview dialog

```java
public interface Importer {
    String getFormatName();
    ImportResult importData(File source, ImportOptions options) throws ImportException;
}

public class ImportValidator {
    public List<ValidationError> validate(List<LibraryItem> items) { }
}
```

**Deliverable:** Import framework

---

#### **Wednesday-Thursday: JSON & CSV Importers**

**Tasks:**

- [ ] Implement `JsonImporter`
- [ ] Implement `CsvImporter` with column mapping
- [ ] Add duplicate detection
- [ ] Implement conflict resolution strategies

**Conflict Strategies:**

- Skip: Keep existing
- Replace: Overwrite
- Rename: Import with new ID
- Ask: Prompt user

**Deliverable:** JSON and CSV importers

---

#### **Friday: Import Dialog & Testing**

**Tasks:**

- [ ] Complete import dialog UI
- [ ] Add import preview with conflict highlighting
- [ ] Test import validation
- [ ] Test round-trip: export → import

**Acceptance Criteria:**
✅ JSON and CSV import working  
✅ Validation catches errors  
✅ Conflict resolution works  
✅ Round-trip produces identical data

---

### **Week 8: Backup & Restore**

#### **Monday-Tuesday: Backup Service**

**Tasks:**

- [ ] Create `com.documentvault.backup` package
- [ ] Implement `BackupService`
- [ ] Design backup format (ZIP with JSON + metadata)
- [ ] Add SHA-256 checksum generation
- [ ] Implement manual backup

```java
// Backup format (.dvbackup is a ZIP)
backup-2026-02-01-103000.dvbackup
├── vault.json           // Exported data
├── metadata.json        // {timestamp, version, itemCount}
└── checksums.sha256     // Integrity verification
```

**Deliverable:** Backup service

---

#### **Wednesday: Backup Scheduler**

**Tasks:**

- [ ] Implement `BackupScheduler`
- [ ] Add configurable schedule (daily/weekly/monthly)
- [ ] Implement backup rotation (keep last N)
- [ ] Add background execution
- [ ] Add backup notifications

**Deliverable:** Automated backups

---

#### **Thursday: Restore Service**

**Tasks:**

- [ ] Implement `RestoreService`
- [ ] Create backup browser UI
- [ ] Add restore preview
- [ ] Implement full restore
- [ ] Add restore to new location option

**Deliverable:** Restore functionality

---

#### **Friday: Backup UI & Testing**

**Tasks:**

- [ ] Create backup management dialog
- [ ] Add status bar backup indicator
- [ ] Test backup → restore cycle
- [ ] Test integrity verification
- [ ] Test backup rotation

**Acceptance Criteria:**
✅ Automatic backups work on schedule  
✅ Manual backups succeed  
✅ Restore recreates exact data  
✅ Integrity verification catches corruption  
✅ Rotation works correctly

---

## 🏃 **Sprint 4: Integration & Release (Weeks 9-12)**

### **Week 9-10: Integration Testing**

#### **Comprehensive Feature Testing**

**Tasks:**

- [ ] Test all features together
- [ ] Test feature interactions
- [ ] Test settings applied to all features
- [ ] Test theme across all features
- [ ] Performance testing with 10,000 items

**Test Scenarios:**

1. Dark mode + advanced search + export
2. Settings changes affect all features
3. Import → search → export round-trip
4. Backup → restore → verify data
5. Category colors in all contexts

---

#### **Cross-Platform Testing**

**Tasks:**

- [ ] Test on macOS (primary)
- [ ] Test on Windows 10/11
- [ ] Test on Ubuntu 20.04+
- [ ] Fix platform-specific issues

---

#### **Performance Testing**

**Tasks:**

- [ ] Startup time with 10,000 items
- [ ] Theme switch performance
- [ ] Search performance
- [ ] Export/import performance
- [ ] Memory usage monitoring

**Performance Targets:**

- Startup: < 4 seconds
- Theme switch: < 200ms
- Search: < 100ms (10,000 items)
- Export 1000 items: < 10 seconds
- Memory: < 200MB (10,000 items)

---

#### **Bug Fixes**

**Tasks:**

- [ ] Fix all critical bugs
- [ ] Fix all high-priority bugs
- [ ] Triage medium/low bugs
- [ ] Update bug tracker

---

### **Week 11: Documentation & Polish**

#### **User Documentation**

**Tasks:**

- [ ] Update USER_GUIDE.md with all features
- [ ] Create SETTINGS_REFERENCE.md
- [ ] Create SEARCH_GUIDE.md with examples
- [ ] Create EXPORT_IMPORT_GUIDE.md
- [ ] Create BACKUP_GUIDE.md

---

#### **Developer Documentation**

**Tasks:**

- [ ] Update DEVELOPMENT.md
- [ ] Document new packages
- [ ] Update architecture diagrams
- [ ] Code cleanup and comments

---

#### **CHANGELOG Update**

**Tasks:**

- [ ] Write comprehensive v1.3.0 entry
- [ ] List all new features
- [ ] List all improvements
- [ ] Add upgrade notes

---

#### **UI Polish**

**Tasks:**

- [ ] Final UI tweaks
- [ ] Fix any visual inconsistencies
- [ ] Test accessibility
- [ ] User acceptance testing

---

### **Week 12: Release Preparation**

#### **Final Testing**

**Tasks:**

- [ ] Run full test suite (379+ tests)
- [ ] Verify all acceptance criteria
- [ ] Final cross-platform testing
- [ ] Performance verification

---

#### **Version Update**

**Tasks:**

- [ ] Update version to 1.3.0 in pom.xml
- [ ] Update version in BrandConstants.java
- [ ] Remove -SNAPSHOT suffix
- [ ] Update all documentation versions

---

#### **Build & Package**

**Tasks:**

- [ ] Clean build: `mvn clean package`
- [ ] Verify JAR: `documentvault-1.3.0.jar`
- [ ] Build platform installers (if available)
- [ ] Test installers on each platform

---

#### **Release**

**Tasks:**

- [ ] Commit version bump
- [ ] Tag v1.3.0
- [ ] Push to GitHub
- [ ] Create GitHub Release
- [ ] Upload artifacts
- [ ] Publish release notes

---

## 📊 **Sprint Metrics**

### **Story Points by Sprint**

| Sprint         | Focus                 | Points      |
| -------------- | --------------------- | ----------- |
| 1 (Weeks 1-3)  | UI/UX Foundation      | 40 pts      |
| 2 (Weeks 4-5)  | Search & Categories   | 30 pts      |
| 3 (Weeks 6-8)  | Data Management       | 50 pts      |
| 4 (Weeks 9-12) | Integration & Release | 30 pts      |
| **Total**      |                       | **150 pts** |

### **Feature Breakdown**

| Feature             | Complexity | Points | Week |
| ------------------- | ---------- | ------ | ---- |
| Dark Mode           | Medium     | 13     | 1    |
| Settings System     | Medium     | 13     | 2    |
| Table Enhancements  | Low        | 8      | 3    |
| Advanced Search     | Medium     | 15     | 4    |
| Enhanced Categories | Medium     | 13     | 5    |
| Export System       | Medium     | 15     | 6    |
| Import System       | Medium     | 15     | 7    |
| Backup & Restore    | High       | 20     | 8    |
| Integration Testing | -          | 20     | 9-10 |
| Documentation       | -          | 8      | 11   |
| Release Prep        | -          | 10     | 12   |

---

## ✅ **Definition of Done**

### **Feature Complete:**

- [ ] All code written and committed
- [ ] Unit tests passing (90%+ coverage)
- [ ] Integration tests passing
- [ ] Manual testing completed
- [ ] Code reviewed
- [ ] Documentation updated
- [ ] No critical/high bugs
- [ ] Performance benchmarks met

### **Sprint Complete:**

- [ ] All planned features done
- [ ] All tests passing
- [ ] Sprint demo recorded (if applicable)
- [ ] Code merged to develop
- [ ] CI/CD passing

### **Release Complete:**

- [ ] All sprints completed
- [ ] Full regression testing passed
- [ ] Documentation complete
- [ ] Release notes finalized
- [ ] Artifacts built and tested
- [ ] GitHub release published
- [ ] Version tagged in git

---

## 🚧 **Risk Management**

| Risk                              | Probability | Impact | Mitigation                   |
| --------------------------------- | ----------- | ------ | ---------------------------- |
| Feature complexity underestimated | Medium      | High   | Buffer time in weeks 9-10    |
| Cross-platform issues             | Medium      | Medium | Test early and often         |
| Performance regression            | Low         | High   | Benchmark after each feature |
| Scope creep                       | Low         | High   | Strict adherence to plan     |

---

## 🎯 **Key Success Factors**

1. **Stay Focused** - No features beyond original v1.2.0 plan
2. **Test Continuously** - Don't wait until the end
3. **Document As You Go** - Update docs with each feature
4. **Ship On Time** - 12 weeks, no extensions
5. **Maintain Quality** - 90%+ test coverage non-negotiable

---

**Sprint Plan v1.3.0 - Focused Execution** 🎯

_"Ship what we promised, on time, with quality."_
