package com.studylibrary.viewmodel;

import com.studylibrary.model.*;
import com.studylibrary.service.LibraryService;
import com.studylibrary.service.LibraryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for LibraryViewModel.
 * Tests JavaFX property binding, filtering, and service interactions.
 */
@DisplayName("LibraryViewModel Tests")
class LibraryViewModelTest {

    @TempDir
    Path tempDir;

    private LibraryViewModel viewModel;
    private LibraryService libraryService;

    @BeforeEach
    void setUp() throws Exception {
        // Reset singleton instances
        resetLibraryServiceSingleton();

        // Set up temporary storage
        System.setProperty("user.home", tempDir.toString());

        // Get fresh service instance
        libraryService = LibraryServiceImpl.getInstance();

        // Create ViewModel (will use the fresh service)
        viewModel = new LibraryViewModel();
    }

    private void resetLibraryServiceSingleton() throws Exception {
        Field instanceField = LibraryServiceImpl.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    // ========== Initialization Tests ==========

    @Test
    @DisplayName("Should initialize with empty items and categories")
    void testInitialization() {
        assertThat(viewModel.getItems()).isEmpty();
        assertThat(viewModel.getCategories()).isEmpty();
        assertThat(viewModel.getSearchQuery()).isEmpty();
        assertThat(viewModel.getSelectedCategory()).isNull();
        assertThat(viewModel.getSelectedItemType()).isNull();
    }

    @Test
    @DisplayName("Should have non-null observable properties")
    void testObservableProperties() {
        assertThat(viewModel.searchQueryProperty()).isNotNull();
        assertThat(viewModel.selectedCategoryProperty()).isNotNull();
        assertThat(viewModel.selectedItemTypeProperty()).isNotNull();
    }

    @Test
    @DisplayName("Should return observable lists")
    void testObservableLists() {
        assertThat(viewModel.getItems()).isInstanceOf(javafx.collections.ObservableList.class);
        assertThat(viewModel.getCategories()).isInstanceOf(javafx.collections.ObservableList.class);
    }

    // ========== Search Query Filter Tests ==========

    @Test
    @DisplayName("Should filter items when search query changes")
    void testSearchQueryFilter() {
        // Add test data
        Note note1 = new Note();
        note1.setTitle("Java Programming");
        note1.setContent("Java content");

        Note note2 = new Note();
        note2.setTitle("Python Basics");
        note2.setContent("Python content");

        Note note3 = new Note();
        note3.setTitle("JavaScript Guide");
        note3.setContent("JS content");

        libraryService.addItem(note1);
        libraryService.addItem(note2);
        libraryService.addItem(note3);

        // Refresh to load all items
        viewModel.refreshItems();
        assertThat(viewModel.getItems()).hasSize(3);

        // Filter by search query
        viewModel.setSearchQuery("java");

        assertThat(viewModel.getItems()).hasSize(2);
        assertThat(viewModel.getItems()).extracting(LibraryItem::getTitle)
                .containsExactlyInAnyOrder("Java Programming", "JavaScript Guide");
    }

    @Test
    @DisplayName("Should show all items when search query is empty")
    void testEmptySearchQuery() {
        Note note1 = new Note();
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setTitle("Note 2");

        libraryService.addItem(note1);
        libraryService.addItem(note2);

        viewModel.refreshItems();
        viewModel.setSearchQuery("test");
        viewModel.setSearchQuery(""); // Clear search

        assertThat(viewModel.getItems()).hasSize(2);
    }

    @Test
    @DisplayName("Should update items automatically when search query property changes")
    void testSearchQueryPropertyListener() {
        Note note1 = new Note();
        note1.setTitle("FirstItem");
        Note note2 = new Note();
        note2.setTitle("SecondItem");

        libraryService.addItem(note1);
        libraryService.addItem(note2);

        viewModel.refreshItems();

        // Change property directly
        viewModel.searchQueryProperty().set("first");

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0).getTitle()).isEqualTo("FirstItem");
    }

    @Test
    @DisplayName("Should search case-insensitively")
    void testCaseInsensitiveSearch() {
        Note note = new Note();
        note.setTitle("UPPERCASE");

        libraryService.addItem(note);

        viewModel.refreshItems();
        viewModel.setSearchQuery("uppercase");

        assertThat(viewModel.getItems()).hasSize(1);
    }

    // ========== Category Filter Tests ==========

    @Test
    @DisplayName("Should filter items by selected category")
    void testCategoryFilter() {
        Category programming = new Category("Programming", "#4CAF50");
        Category design = new Category("Design", "#2196F3");
        libraryService.addCategory(programming);
        libraryService.addCategory(design);

        Note note1 = new Note();
        note1.setTitle("Java");
        note1.setCategory(programming);

        Note note2 = new Note();
        note2.setTitle("UI Design");
        note2.setCategory(design);

        Note note3 = new Note();
        note3.setTitle("Python");
        note3.setCategory(programming);

        libraryService.addItem(note1);
        libraryService.addItem(note2);
        libraryService.addItem(note3);

        viewModel.refreshItems();
        viewModel.setSelectedCategory(programming);

        assertThat(viewModel.getItems()).hasSize(2);
        assertThat(viewModel.getItems()).extracting(LibraryItem::getTitle)
                .containsExactlyInAnyOrder("Java", "Python");
    }

    @Test
    @DisplayName("Should show all items when no category selected")
    void testNoCategoryFilter() {
        Category cat = new Category("Test");
        libraryService.addCategory(cat);

        Note note1 = new Note();
        note1.setTitle("Note 1");
        note1.setCategory(cat);

        Note note2 = new Note();
        note2.setTitle("Note 2");

        libraryService.addItem(note1);
        libraryService.addItem(note2);

        viewModel.refreshItems();
        viewModel.setSelectedCategory(null);

        assertThat(viewModel.getItems()).hasSize(2);
    }

    @Test
    @DisplayName("Should update items automatically when category property changes")
    void testCategoryPropertyListener() {
        Category cat = new Category("Test");
        libraryService.addCategory(cat);

        Note note1 = new Note();
        note1.setTitle("Categorized");
        note1.setCategory(cat);

        Note note2 = new Note();
        note2.setTitle("Uncategorized");

        libraryService.addItem(note1);
        libraryService.addItem(note2);

        viewModel.refreshItems();
        viewModel.selectedCategoryProperty().set(cat);

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0).getTitle()).isEqualTo("Categorized");
    }

    // ========== Item Type Filter Tests ==========

    @Test
    @DisplayName("Should filter items by type")
    void testItemTypeFilter() {
        Note note = new Note();
        note.setTitle("Note");

        PdfDocument pdf = new PdfDocument();
        pdf.setTitle("Document");
        pdf.setFilePath("/path/to/file.pdf");

        MediaLink link = new MediaLink();
        link.setTitle("Video");
        link.setUrl("https://youtube.com/watch?v=123");

        libraryService.addItem(note);
        libraryService.addItem(pdf);
        libraryService.addItem(link);

        viewModel.refreshItems();
        viewModel.setSelectedItemType(LibraryItem.ItemType.NOTE);

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0)).isInstanceOf(Note.class);
    }

    @Test
    @DisplayName("Should filter PDF documents")
    void testPdfFilter() {
        PdfDocument pdf1 = new PdfDocument();
        pdf1.setTitle("Doc1");
        pdf1.setFilePath("/path1.pdf");

        PdfDocument pdf2 = new PdfDocument();
        pdf2.setTitle("Doc2");
        pdf2.setFilePath("/path2.pdf");

        Note note = new Note();
        note.setTitle("Note");

        libraryService.addItem(pdf1);
        libraryService.addItem(pdf2);
        libraryService.addItem(note);

        viewModel.refreshItems();
        viewModel.setSelectedItemType(LibraryItem.ItemType.PDF);

        assertThat(viewModel.getItems()).hasSize(2);
        assertThat(viewModel.getItems()).allMatch(item -> item instanceof PdfDocument);
    }

    @Test
    @DisplayName("Should show all items when no type filter selected")
    void testNoTypeFilter() {
        Note note = new Note();
        note.setTitle("Note");

        PdfDocument pdf = new PdfDocument();
        pdf.setTitle("PDF");
        pdf.setFilePath("/path.pdf");

        libraryService.addItem(note);
        libraryService.addItem(pdf);

        viewModel.refreshItems();
        viewModel.setSelectedItemType(null);

        assertThat(viewModel.getItems()).hasSize(2);
    }

    @Test
    @DisplayName("Should update items automatically when type property changes")
    void testTypePropertyListener() {
        MediaLink link = new MediaLink();
        link.setTitle("Link");
        link.setUrl("https://example.com");

        Note note = new Note();
        note.setTitle("Note");

        libraryService.addItem(link);
        libraryService.addItem(note);

        viewModel.refreshItems();
        viewModel.selectedItemTypeProperty().set(LibraryItem.ItemType.MEDIA_LINK);

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0)).isInstanceOf(MediaLink.class);
    }

    // ========== Combined Filter Tests ==========

    @Test
    @DisplayName("Should apply multiple filters simultaneously")
    void testCombinedFilters() {
        Category programming = new Category("Programming");
        libraryService.addCategory(programming);

        Note note1 = new Note();
        note1.setTitle("Java Programming");
        note1.setCategory(programming);

        Note note2 = new Note();
        note2.setTitle("Python Programming");
        note2.setCategory(programming);

        PdfDocument pdf = new PdfDocument();
        pdf.setTitle("Java Guide");
        pdf.setFilePath("/path.pdf");
        pdf.setCategory(programming);

        libraryService.addItem(note1);
        libraryService.addItem(note2);
        libraryService.addItem(pdf);

        viewModel.refreshItems();
        viewModel.setSelectedCategory(programming);
        viewModel.setSelectedItemType(LibraryItem.ItemType.NOTE);
        viewModel.setSearchQuery("java");

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0).getTitle()).isEqualTo("Java Programming");
    }

    // ========== Item Management Tests ==========

    @Test
    @DisplayName("Should add item and refresh list")
    void testAddItem() {
        Note note = new Note();
        note.setTitle("New Note");
        note.setContent("Content");

        viewModel.addItem(note);

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getItems().get(0).getTitle()).isEqualTo("New Note");
    }

    @Test
    @DisplayName("Should update item and refresh list")
    void testUpdateItem() {
        Note note = new Note();
        note.setTitle("Original");
        libraryService.addItem(note);
        viewModel.refreshItems();

        note.setTitle("Updated");
        viewModel.updateItem(note);

        assertThat(viewModel.getItems().get(0).getTitle()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("Should delete item and refresh list")
    void testDeleteItem() {
        Note note = new Note();
        note.setTitle("To Delete");
        libraryService.addItem(note);
        viewModel.refreshItems();

        assertThat(viewModel.getItems()).hasSize(1);

        viewModel.deleteItem(note.getId());

        assertThat(viewModel.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should maintain filters after adding item")
    void testFiltersAfterAdd() {
        viewModel.setSearchQuery("test");

        Note note = new Note();
        note.setTitle("Test Note");
        viewModel.addItem(note);

        assertThat(viewModel.getItems()).hasSize(1);
        assertThat(viewModel.getSearchQuery()).isEqualTo("test");
    }

    // ========== Category Management Tests ==========

    @Test
    @DisplayName("Should add category and refresh list")
    void testAddCategory() {
        Category category = new Category("New Category");

        viewModel.addCategory(category);

        assertThat(viewModel.getCategories()).hasSize(1);
        assertThat(viewModel.getCategories().get(0).getName()).isEqualTo("New Category");
    }

    @Test
    @DisplayName("Should update category and refresh list")
    void testUpdateCategory() {
        Category category = new Category("Original");
        libraryService.addCategory(category);
        viewModel.refreshCategories();

        category.setName("Updated");
        viewModel.updateCategory(category);

        assertThat(viewModel.getCategories().get(0).getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("Should delete category and refresh both lists")
    void testDeleteCategory() {
        Category category = new Category("To Delete");
        libraryService.addCategory(category);

        Note note = new Note();
        note.setTitle("Note");
        note.setCategory(category);
        libraryService.addItem(note);

        viewModel.refreshCategories();
        viewModel.refreshItems();

        assertThat(viewModel.getCategories()).hasSize(1);

        viewModel.deleteCategory(category.getId());

        assertThat(viewModel.getCategories()).isEmpty();
        // Items should also be refreshed
        assertThat(viewModel.getItems()).hasSize(1);
    }

    // ========== Refresh Tests ==========

    @Test
    @DisplayName("Should refresh items from service")
    void testRefreshItems() {
        // Add item directly to service
        Note note = new Note();
        note.setTitle("Test");
        libraryService.addItem(note);

        // Items not yet in ViewModel
        viewModel.getItems().clear();
        assertThat(viewModel.getItems()).isEmpty();

        // Refresh should load from service
        viewModel.refreshItems();

        assertThat(viewModel.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should refresh categories from service")
    void testRefreshCategories() {
        // Add category directly to service
        Category category = new Category("Test");
        libraryService.addCategory(category);

        // Categories not yet in ViewModel
        viewModel.getCategories().clear();
        assertThat(viewModel.getCategories()).isEmpty();

        // Refresh should load from service
        viewModel.refreshCategories();

        assertThat(viewModel.getCategories()).hasSize(1);
    }

    // ========== Statistics Tests ==========

    @Test
    @DisplayName("Should get total item count")
    void testGetTotalItemCount() {
        Note note1 = new Note();
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setTitle("Note 2");

        libraryService.addItem(note1);
        libraryService.addItem(note2);

        assertThat(viewModel.getTotalItemCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should get item count by type")
    void testGetItemCountByType() {
        Note note = new Note();
        note.setTitle("Note");

        PdfDocument pdf1 = new PdfDocument();
        pdf1.setTitle("PDF 1");
        pdf1.setFilePath("/path1.pdf");

        PdfDocument pdf2 = new PdfDocument();
        pdf2.setTitle("PDF 2");
        pdf2.setFilePath("/path2.pdf");

        libraryService.addItem(note);
        libraryService.addItem(pdf1);
        libraryService.addItem(pdf2);

        assertThat(viewModel.getItemCountByType(LibraryItem.ItemType.NOTE)).isEqualTo(1);
        assertThat(viewModel.getItemCountByType(LibraryItem.ItemType.PDF)).isEqualTo(2);
        assertThat(viewModel.getItemCountByType(LibraryItem.ItemType.MEDIA_LINK)).isZero();
    }

    // ========== Observable Collection Tests ==========

    @Test
    @DisplayName("Should notify listeners when items change")
    void testItemsListChangeNotification() {
        AtomicInteger changeCount = new AtomicInteger(0);

        viewModel.getItems().addListener((javafx.collections.ListChangeListener<LibraryItem>) c -> {
            changeCount.incrementAndGet();
        });

        Note note = new Note();
        note.setTitle("Test");
        viewModel.addItem(note);

        assertThat(changeCount.get()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should notify listeners when categories change")
    void testCategoriesListChangeNotification() {
        AtomicInteger changeCount = new AtomicInteger(0);

        viewModel.getCategories().addListener((javafx.collections.ListChangeListener<Category>) c -> {
            changeCount.incrementAndGet();
        });

        Category category = new Category("Test");
        viewModel.addCategory(category);

        assertThat(changeCount.get()).isGreaterThan(0);
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Should handle empty library")
    void testEmptyLibrary() {
        viewModel.setSearchQuery("nonexistent");

        assertThat(viewModel.getItems()).isEmpty();
        assertThat(viewModel.getTotalItemCount()).isZero();
    }

    @Test
    @DisplayName("Should handle filter with no matches")
    void testNoMatchingItems() {
        Note note = new Note();
        note.setTitle("Test");
        libraryService.addItem(note);

        viewModel.refreshItems();
        viewModel.setSearchQuery("nonexistent");

        assertThat(viewModel.getItems()).isEmpty();
    }

    @Test
    @DisplayName("Should handle all item types")
    void testAllItemTypes() {
        Note note = new Note();
        note.setTitle("Note");

        PdfDocument pdf = new PdfDocument();
        pdf.setTitle("PDF");
        pdf.setFilePath("/path.pdf");

        MediaLink link = new MediaLink();
        link.setTitle("Link");
        link.setUrl("https://example.com");

        TextSnippet snippet = new TextSnippet();
        snippet.setTitle("Snippet");
        snippet.setContent("Code here");
        snippet.setLanguage("java");

        libraryService.addItem(note);
        libraryService.addItem(pdf);
        libraryService.addItem(link);
        libraryService.addItem(snippet);

        viewModel.refreshItems();

        assertThat(viewModel.getItems()).hasSize(4);
        assertThat(viewModel.getTotalItemCount()).isEqualTo(4);
    }
}
