package com.documentvault.service;

import com.documentvault.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for LibraryServiceImpl.
 * Tests business logic with actual storage (using temporary directory).
 * Note: These are integration tests rather than pure unit tests due to
 * singleton constraints.
 */
@DisplayName("LibraryServiceImpl Tests")
class LibraryServiceImplTest {

    @TempDir
    Path tempDir;

    private LibraryServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        // Reset singleton instances before each test
        resetServiceSingleton();
        resetStorageSingleton();

        // Set up temporary storage directory
        System.setProperty("user.home", tempDir.toString());

        // Get fresh instance for each test
        service = LibraryServiceImpl.getInstance();
    }

    private void resetServiceSingleton() throws Exception {
        Field instanceField = LibraryServiceImpl.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    private void resetStorageSingleton() throws Exception {
        Field instanceField = StorageService.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    // ========== Item Management Tests ==========

    @Test
    @DisplayName("Should add item and persist to storage")
    void testAddItem() {
        Note note = new Note();
        note.setTitle("Test Note");

        service.addItem(note);

        assertThat(service.getAllItems()).containsExactly(note);
    }

    @Test
    @DisplayName("Should not add null item")
    void testAddNullItem() {
        service.addItem(null);

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should not add item with null ID")
    void testAddItemWithNullId() {
        Note note = new Note();
        note.setId(null);

        service.addItem(note);

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should retrieve item by ID")
    void testGetItemById() {
        Note note = new Note();
        note.setTitle("Test Note");
        service.addItem(note);

        Optional<LibraryItem> result = service.getItemById(note.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(note);
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void testGetItemByIdNotFound() {
        Optional<LibraryItem> result = service.getItemById("non-existent-id");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty optional for null ID")
    void testGetItemByIdNull() {
        Optional<LibraryItem> result = service.getItemById(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update existing item")
    void testUpdateItem() {
        Note note = new Note();
        note.setTitle("Original Title");
        service.addItem(note);

        note.setTitle("Updated Title");
        service.updateItem(note);

        assertThat(service.getItemById(note.getId()).get().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName("Should not update non-existent item")
    void testUpdateNonExistentItem() {
        Note note = new Note();
        note.setTitle("Test Note");

        service.updateItem(note);

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should not update null item")
    void testUpdateNullItem() {
        service.updateItem(null);

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should not update item with null ID")
    void testUpdateItemWithNullId() {
        Note note = new Note();
        note.setId(null);

        service.updateItem(note);

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should delete item")
    void testDeleteItem() {
        Note note = new Note();
        note.setTitle("Test Note");
        service.addItem(note);

        service.deleteItem(note.getId());

        assertThat(service.getAllItems()).isEmpty();
    }

    @Test
    @DisplayName("Should not delete non-existent item")
    void testDeleteNonExistentItem() {
        int initialCount = service.getItemCount();

        service.deleteItem("non-existent-id");

        assertThat(service.getItemCount()).isEqualTo(initialCount);
    }

    @Test
    @DisplayName("Should not delete with null ID")
    void testDeleteNullId() {
        Note note = new Note();
        service.addItem(note);

        service.deleteItem(null);

        assertThat(service.getAllItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should get all items")
    void testGetAllItems() {
        Note note1 = new Note();
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setTitle("Note 2");

        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> items = service.getAllItems();

        assertThat(items).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return empty list when no items")
    void testGetAllItemsEmpty() {
        List<LibraryItem> items = service.getAllItems();

        assertThat(items).isEmpty();
    }

    @Test
    @DisplayName("Should get item count")
    void testGetItemCount() {
        service.addItem(new Note());
        service.addItem(new Note());
        service.addItem(new PdfDocument());

        assertThat(service.getItemCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should return zero count for empty library")
    void testGetItemCountEmpty() {
        assertThat(service.getItemCount()).isZero();
    }

    // ========== Search and Filter Tests ==========

    @Test
    @DisplayName("Should search items by query")
    void testSearchItems() {
        Note note1 = new Note();
        note1.setTitle("Java Programming");
        Note note2 = new Note();
        note2.setTitle("Python Tutorial");
        Note note3 = new Note();
        note3.setTitle("JavaScript Guide");

        service.addItem(note1);
        service.addItem(note2);
        service.addItem(note3);

        List<LibraryItem> results = service.searchItems("java");

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note3);
    }

    @Test
    @DisplayName("Should search case-insensitively")
    void testSearchItemsCaseInsensitive() {
        Note note = new Note();
        note.setTitle("Java Programming");
        service.addItem(note);

        List<LibraryItem> results = service.searchItems("JAVA");

        assertThat(results).containsExactly(note);
    }

    @Test
    @DisplayName("Should trim search query")
    void testSearchItemsTrimmed() {
        Note note = new Note();
        note.setTitle("Java Programming");
        service.addItem(note);

        List<LibraryItem> results = service.searchItems("  java  ");

        assertThat(results).containsExactly(note);
    }

    @Test
    @DisplayName("Should return all items for null query")
    void testSearchItemsNullQuery() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.searchItems(null);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return all items for empty query")
    void testSearchItemsEmptyQuery() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.searchItems("");

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return all items for whitespace query")
    void testSearchItemsWhitespaceQuery() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.searchItems("   ");

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return empty list for no matches")
    void testSearchItemsNoMatches() {
        Note note = new Note();
        note.setTitle("Java Programming");
        service.addItem(note);

        List<LibraryItem> results = service.searchItems("python");

        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should filter items by category")
    void testGetItemsByCategory() {
        Category category1 = new Category("Programming");
        Category category2 = new Category("Design");

        Note note1 = new Note();
        note1.setCategory(category1);
        Note note2 = new Note();
        note2.setCategory(category1);
        Note note3 = new Note();
        note3.setCategory(category2);

        service.addItem(note1);
        service.addItem(note2);
        service.addItem(note3);

        List<LibraryItem> results = service.getItemsByCategory(category1);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return all items for null category")
    void testGetItemsByNullCategory() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.getItemsByCategory(null);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should filter items by tag")
    void testGetItemsByTag() {
        Note note1 = new Note();
        note1.addTag("java");
        Note note2 = new Note();
        note2.addTag("java");
        Note note3 = new Note();
        note3.addTag("python");

        service.addItem(note1);
        service.addItem(note2);
        service.addItem(note3);

        List<LibraryItem> results = service.getItemsByTag("java");

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should filter by tag case-insensitively")
    void testGetItemsByTagCaseInsensitive() {
        Note note = new Note();
        note.addTag("Java");
        service.addItem(note);

        List<LibraryItem> results = service.getItemsByTag("JAVA");

        assertThat(results).containsExactly(note);
    }

    @Test
    @DisplayName("Should return all items for null tag")
    void testGetItemsByNullTag() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.getItemsByTag(null);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return all items for empty tag")
    void testGetItemsByEmptyTag() {
        Note note1 = new Note();
        Note note2 = new Note();
        service.addItem(note1);
        service.addItem(note2);

        List<LibraryItem> results = service.getItemsByTag("");

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should filter items by type")
    void testGetItemsByType() {
        Note note1 = new Note();
        Note note2 = new Note();
        PdfDocument pdf = new PdfDocument();

        service.addItem(note1);
        service.addItem(note2);
        service.addItem(pdf);

        List<LibraryItem> results = service.getItemsByType(LibraryItem.ItemType.NOTE);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note1, note2);
    }

    @Test
    @DisplayName("Should return all items for null type")
    void testGetItemsByNullType() {
        Note note = new Note();
        PdfDocument pdf = new PdfDocument();
        service.addItem(note);
        service.addItem(pdf);

        List<LibraryItem> results = service.getItemsByType(null);

        assertThat(results).hasSize(2).containsExactlyInAnyOrder(note, pdf);
    }

    @Test
    @DisplayName("Should get item count by type")
    void testGetItemCountByType() {
        service.addItem(new Note());
        service.addItem(new Note());
        service.addItem(new PdfDocument());
        service.addItem(new MediaLink());

        assertThat(service.getItemCountByType(LibraryItem.ItemType.NOTE)).isEqualTo(2);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.PDF)).isEqualTo(1);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.MEDIA_LINK)).isEqualTo(1);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.TEXT_SNIPPET)).isZero();
    }

    @Test
    @DisplayName("Should return zero count for null type")
    void testGetItemCountByNullType() {
        service.addItem(new Note());

        assertThat(service.getItemCountByType(null)).isZero();
    }

    // ========== Tag Management Tests ==========

    @Test
    @DisplayName("Should get all unique tags")
    void testGetAllTags() {
        Note note1 = new Note();
        note1.addTag("java");
        note1.addTag("programming");
        Note note2 = new Note();
        note2.addTag("java");
        note2.addTag("tutorial");

        service.addItem(note1);
        service.addItem(note2);

        List<String> tags = service.getAllTags();

        assertThat(tags).hasSize(3).containsExactly("java", "programming", "tutorial");
    }

    @Test
    @DisplayName("Should return sorted tags")
    void testGetAllTagsSorted() {
        Note note = new Note();
        note.addTag("zebra");
        note.addTag("apple");
        note.addTag("monkey");
        service.addItem(note);

        List<String> tags = service.getAllTags();

        assertThat(tags).containsExactly("apple", "monkey", "zebra");
    }

    @Test
    @DisplayName("Should return empty list when no tags")
    void testGetAllTagsEmpty() {
        service.addItem(new Note());

        List<String> tags = service.getAllTags();

        assertThat(tags).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when no items")
    void testGetAllTagsNoItems() {
        List<String> tags = service.getAllTags();

        assertThat(tags).isEmpty();
    }

    // ========== Category Management Tests ==========

    @Test
    @DisplayName("Should add category")
    void testAddCategory() {
        Category category = new Category("Programming", "#4CAF50");

        service.addCategory(category);

        assertThat(service.getAllCategories()).containsExactly(category);
    }

    @Test
    @DisplayName("Should not add null category")
    void testAddNullCategory() {
        service.addCategory(null);

        assertThat(service.getAllCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should not add category with null ID")
    void testAddCategoryWithNullId() {
        Category category = new Category("Test");
        category.setId(null);

        service.addCategory(category);

        assertThat(service.getAllCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should retrieve category by ID")
    void testGetCategoryById() {
        Category category = new Category("Programming");
        service.addCategory(category);

        Optional<Category> result = service.getCategoryById(category.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(category);
    }

    @Test
    @DisplayName("Should return empty optional for non-existent category")
    void testGetCategoryByIdNotFound() {
        Optional<Category> result = service.getCategoryById("non-existent-id");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty optional for null category ID")
    void testGetCategoryByIdNull() {
        Optional<Category> result = service.getCategoryById(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update existing category")
    void testUpdateCategory() {
        Category category = new Category("Programming");
        service.addCategory(category);

        category.setName("Advanced Programming");
        service.updateCategory(category);

        assertThat(service.getCategoryById(category.getId()).get().getName()).isEqualTo("Advanced Programming");
    }

    @Test
    @DisplayName("Should not update non-existent category")
    void testUpdateNonExistentCategory() {
        Category category = new Category("Test");

        service.updateCategory(category);

        assertThat(service.getAllCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should not update null category")
    void testUpdateNullCategory() {
        service.updateCategory(null);

        assertThat(service.getAllCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should not update category with null ID")
    void testUpdateCategoryWithNullId() {
        Category category = new Category("Test");
        category.setId(null);

        service.updateCategory(category);

        assertThat(service.getAllCategories()).isEmpty();
    }

    @Test
    @DisplayName("Should delete category and remove from items")
    void testDeleteCategory() {
        Category category = new Category("Programming");
        service.addCategory(category);

        Note note1 = new Note();
        note1.setCategory(category);
        Note note2 = new Note();
        note2.setCategory(category);
        service.addItem(note1);
        service.addItem(note2);

        service.deleteCategory(category.getId());

        assertThat(service.getAllCategories()).isEmpty();
        assertThat(service.getAllItems()).allMatch(item -> item.getCategory() == null);
    }

    @Test
    @DisplayName("Should not delete non-existent category")
    void testDeleteNonExistentCategory() {
        int initialCount = service.getAllCategories().size();

        service.deleteCategory("non-existent-id");

        assertThat(service.getAllCategories()).hasSize(initialCount);
    }

    @Test
    @DisplayName("Should not delete category with null ID")
    void testDeleteCategoryNullId() {
        Category category = new Category("Test");
        service.addCategory(category);

        service.deleteCategory(null);

        assertThat(service.getAllCategories()).hasSize(1);
    }

    @Test
    @DisplayName("Should get all categories")
    void testGetAllCategories() {
        Category cat1 = new Category("Programming");
        Category cat2 = new Category("Design");

        service.addCategory(cat1);
        service.addCategory(cat2);

        List<Category> categories = service.getAllCategories();

        assertThat(categories).hasSize(2).containsExactlyInAnyOrder(cat1, cat2);
    }

    @Test
    @DisplayName("Should return empty list when no categories")
    void testGetAllCategoriesEmpty() {
        List<Category> categories = service.getAllCategories();

        assertThat(categories).isEmpty();
    }

    // ========== Singleton Tests ==========

    @Test
    @DisplayName("Should return same instance on multiple calls")
    void testSingletonInstance() {
        LibraryServiceImpl instance1 = LibraryServiceImpl.getInstance();
        LibraryServiceImpl instance2 = LibraryServiceImpl.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("Should handle complex search scenario")
    void testComplexSearch() {
        Category category = new Category("Programming");

        Note note1 = new Note();
        note1.setTitle("Java Basics");
        note1.setDescription("Introduction to Java");
        note1.addTag("java");
        note1.setCategory(category);

        Note note2 = new Note();
        note2.setTitle("Python Guide");
        note2.setDescription("Learn Python with Java comparison");
        note2.addTag("python");

        service.addItem(note1);
        service.addItem(note2);

        // Should find both because note2 mentions "java" in description
        List<LibraryItem> results = service.searchItems("java");

        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Should handle multiple filter operations")
    void testMultipleFilters() {
        Category category1 = new Category("Programming");
        Category category2 = new Category("Design");

        Note note1 = new Note();
        note1.setCategory(category1);
        note1.addTag("java");

        Note note2 = new Note();
        note2.setCategory(category1);
        note2.addTag("python");

        PdfDocument pdf = new PdfDocument();
        pdf.setCategory(category2);
        pdf.addTag("java");

        service.addItem(note1);
        service.addItem(note2);
        service.addItem(pdf);

        List<LibraryItem> byCategory = service.getItemsByCategory(category1);
        List<LibraryItem> byTag = service.getItemsByTag("java");
        List<LibraryItem> byType = service.getItemsByType(LibraryItem.ItemType.NOTE);

        assertThat(byCategory).hasSize(2);
        assertThat(byTag).hasSize(2);
        assertThat(byType).hasSize(2);
    }

    @Test
    @DisplayName("Should maintain data consistency across operations")
    void testDataConsistency() {
        Note note = new Note();
        note.setTitle("Test Note");
        service.addItem(note);

        String noteId = note.getId();

        // Update
        note.setTitle("Updated Note");
        service.updateItem(note);

        LibraryItem retrieved = service.getItemById(noteId).get();
        assertThat(retrieved.getTitle()).isEqualTo("Updated Note");

        // Delete
        service.deleteItem(noteId);
        assertThat(service.getItemById(noteId)).isEmpty();
    }

    @Test
    @DisplayName("Should add multiple items of different types")
    void testMultipleItemTypes() {
        Note note = new Note();
        note.setTitle("My Note");
        PdfDocument pdf = new PdfDocument();
        pdf.setTitle("My PDF");
        MediaLink link = new MediaLink();
        link.setTitle("My Link");
        TextSnippet snippet = new TextSnippet();
        snippet.setTitle("My Snippet");

        service.addItem(note);
        service.addItem(pdf);
        service.addItem(link);
        service.addItem(snippet);

        assertThat(service.getItemCount()).isEqualTo(4);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.NOTE)).isEqualTo(1);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.PDF)).isEqualTo(1);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.MEDIA_LINK)).isEqualTo(1);
        assertThat(service.getItemCountByType(LibraryItem.ItemType.TEXT_SNIPPET)).isEqualTo(1);
    }
}
