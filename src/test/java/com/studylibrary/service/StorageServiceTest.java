package com.studylibrary.service;

import com.studylibrary.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for StorageService.
 * Tests file I/O operations and storage initialization.
 * Note: Gson serialization is tested implicitly through LibraryServiceImplTest.
 */
@DisplayName("StorageService Tests")
class StorageServiceTest {

    @TempDir
    Path tempDir;

    private StorageService storageService;

    @BeforeEach
    void setUp() throws Exception {
        // Reset singleton instance before each test
        resetStorageSingleton();

        // Set up temporary storage directory
        System.setProperty("user.home", tempDir.toString());

        // Get fresh instance for each test
        storageService = StorageService.getInstance();
    }

    private void resetStorageSingleton() throws Exception {
        Field instanceField = StorageService.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    // ========== Initialization Tests ==========

    @Test
    @DisplayName("Should create storage directory on initialization")
    void testStorageDirectoryCreation() {
        Path storageDir = storageService.getStorageDirectory();

        assertThat(storageDir).exists();
        assertThat(storageDir.getFileName().toString()).isEqualTo(".studylibrary");
    }

    @Test
    @DisplayName("Should create items file on initialization")
    void testItemsFileCreation() throws IOException {
        Path storageDir = storageService.getStorageDirectory();
        Path itemsFile = storageDir.resolve("library-items.json");

        assertThat(itemsFile).exists();
        assertThat(Files.readString(itemsFile)).isEqualTo("[]");
    }

    @Test
    @DisplayName("Should create categories file on initialization")
    void testCategoriesFileCreation() throws IOException {
        Path storageDir = storageService.getStorageDirectory();
        Path categoriesFile = storageDir.resolve("categories.json");

        assertThat(categoriesFile).exists();
        assertThat(Files.readString(categoriesFile)).isEqualTo("[]");
    }

    @Test
    @DisplayName("Should return singleton instance")
    void testSingletonInstance() {
        StorageService instance1 = StorageService.getInstance();
        StorageService instance2 = StorageService.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Should handle directory creation when directory already exists")
    void testDirectoryAlreadyExists() throws Exception {
        // Directory is already created in setUp
        Path storageDir = storageService.getStorageDirectory();

        // Reset and recreate - should not throw exception
        resetStorageSingleton();
        System.setProperty("user.home", tempDir.toString());
        StorageService newService = StorageService.getInstance();

        assertThat(newService.getStorageDirectory()).isEqualTo(storageDir);
    }

    @Test
    @DisplayName("Should get storage directory path")
    void testGetStorageDirectory() {
        Path storageDir = storageService.getStorageDirectory();

        assertThat(storageDir).isNotNull();
        assertThat(storageDir.toString()).contains(".studylibrary");
        assertThat(storageDir).exists();
        assertThat(storageDir).isDirectory();
    }

    // ========== Category Storage Tests ==========

    @Test
    @DisplayName("Should save and load empty category list")
    void testSaveLoadEmptyCategories() {
        List<Category> categories = new ArrayList<>();

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).isEmpty();
    }

    @Test
    @DisplayName("Should save and load single category")
    void testSaveLoadSingleCategory() {
        Category category = new Category("Programming", "#4CAF50");
        category.setDescription("Programming resources");
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(1);
        Category loadedCat = loaded.get(0);
        assertThat(loadedCat.getId()).isEqualTo(category.getId());
        assertThat(loadedCat.getName()).isEqualTo("Programming");
        assertThat(loadedCat.getColor()).isEqualTo("#4CAF50");
        assertThat(loadedCat.getDescription()).isEqualTo("Programming resources");
    }

    @Test
    @DisplayName("Should save and load multiple categories")
    void testSaveLoadMultipleCategories() {
        Category cat1 = new Category("Programming", "#4CAF50");
        Category cat2 = new Category("Design", "#2196F3");
        Category cat3 = new Category("Business", "#FF9800");
        List<Category> categories = new ArrayList<>();
        categories.add(cat1);
        categories.add(cat2);
        categories.add(cat3);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(3);
        assertThat(loaded).extracting(Category::getName)
                .containsExactlyInAnyOrder("Programming", "Design", "Business");
    }

    @Test
    @DisplayName("Should handle category with null description")
    void testSaveCategoryWithNullDescription() {
        Category category = new Category("Test");
        category.setDescription(null);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0).getDescription()).isNull();
    }

    @Test
    @DisplayName("Should overwrite existing categories on save")
    void testOverwriteCategories() {
        // Save initial data
        Category cat1 = new Category("First");
        List<Category> categories1 = new ArrayList<>();
        categories1.add(cat1);
        storageService.saveCategories(categories1);

        // Save new data (should overwrite)
        Category cat2 = new Category("Second");
        List<Category> categories2 = new ArrayList<>();
        categories2.add(cat2);
        storageService.saveCategories(categories2);

        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0).getName()).isEqualTo("Second");
    }

    @Test
    @DisplayName("Should throw exception when loading from corrupted category file")
    void testLoadCategoriesFromCorruptedFile() throws IOException {
        Path categoriesFile = storageService.getStorageDirectory().resolve("categories.json");
        Files.writeString(categoriesFile, "invalid json content");

        // Gson throws JsonSyntaxException for invalid JSON
        assertThatThrownBy(() -> storageService.loadCategories())
                .isInstanceOf(com.google.gson.JsonSyntaxException.class);
    }

    @Test
    @DisplayName("Should return empty list when category file contains null")
    void testLoadCategoriesFromNullContent() throws IOException {
        Path categoriesFile = storageService.getStorageDirectory().resolve("categories.json");
        Files.writeString(categoriesFile, "null");

        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).isEmpty();
    }

    @Test
    @DisplayName("Should handle large number of categories")
    void testLargeNumberOfCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Category category = new Category("Category " + i, "#" + String.format("%06X", i * 1000));
            categories.add(category);
        }

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(50);
        assertThat(loaded).extracting(Category::getName)
                .contains("Category 0", "Category 25", "Category 49");
    }

    @Test
    @DisplayName("Should maintain category IDs across save/load")
    void testCategoryIdPreservation() {
        Category category = new Category("Test");
        String originalId = category.getId();
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded.get(0).getId()).isEqualTo(originalId);
    }

    @Test
    @DisplayName("Should handle special characters in category names")
    void testCategorySpecialCharacters() {
        Category category = new Category("Test & Special: éàü 中文 🎉", "#FF5722");
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0).getName()).isEqualTo("Test & Special: éàü 中文 🎉");
    }

    // ========== Item Storage Basic Tests ==========

    @Test
    @DisplayName("Should save and load empty item list")
    void testSaveLoadEmptyItems() {
        List<LibraryItem> items = new ArrayList<>();

        storageService.saveItems(items);
        List<LibraryItem> loaded = storageService.loadItems();

        assertThat(loaded).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when loading from corrupted item file")
    void testLoadItemsFromCorruptedFile() throws IOException {
        Path itemsFile = storageService.getStorageDirectory().resolve("library-items.json");
        Files.writeString(itemsFile, "invalid json content");

        // Gson throws JsonSyntaxException for invalid JSON
        assertThatThrownBy(() -> storageService.loadItems())
                .isInstanceOf(com.google.gson.JsonSyntaxException.class);
    }

    @Test
    @DisplayName("Should return empty list when item file contains null")
    void testLoadItemsFromNullContent() throws IOException {
        Path itemsFile = storageService.getStorageDirectory().resolve("library-items.json");
        Files.writeString(itemsFile, "null");

        List<LibraryItem> loaded = storageService.loadItems();

        assertThat(loaded).isEmpty();
    }

    @Test
    @DisplayName("Should verify items file is writable")
    void testItemsFileWritable() throws IOException {
        Path itemsFile = storageService.getStorageDirectory().resolve("library-items.json");

        assertThat(itemsFile).exists();
        assertThat(Files.isWritable(itemsFile)).isTrue();
    }

    @Test
    @DisplayName("Should verify categories file is writable")
    void testCategoriesFileWritable() throws IOException {
        Path categoriesFile = storageService.getStorageDirectory().resolve("categories.json");

        assertThat(categoriesFile).exists();
        assertThat(Files.isWritable(categoriesFile)).isTrue();
    }

    // ========== Multiple Save/Load Cycles ==========

    @Test
    @DisplayName("Should maintain category data consistency across multiple save/load cycles")
    void testMultipleCategorySaveLoadCycles() {
        Category category = new Category("Test Category");
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        // Cycle 1
        storageService.saveCategories(categories);
        List<Category> loaded1 = storageService.loadCategories();

        // Cycle 2: Modify and save
        Category modified = loaded1.get(0);
        modified.setName("Modified Category");
        List<Category> categories2 = new ArrayList<>();
        categories2.add(modified);
        storageService.saveCategories(categories2);
        List<Category> loaded2 = storageService.loadCategories();

        // Cycle 3: Modify again
        Category modified2 = loaded2.get(0);
        modified2.setColor("#00FF00");
        List<Category> categories3 = new ArrayList<>();
        categories3.add(modified2);
        storageService.saveCategories(categories3);
        List<Category> loaded3 = storageService.loadCategories();

        assertThat(loaded3).hasSize(1);
        Category finalCategory = loaded3.get(0);
        assertThat(finalCategory.getName()).isEqualTo("Modified Category");
        assertThat(finalCategory.getColor()).isEqualTo("#00FF00");
    }

    @Test
    @DisplayName("Should handle very long category descriptions")
    void testLongCategoryDescription() {
        Category category = new Category("Test");
        String longDescription = "A".repeat(10000);
        category.setDescription(longDescription);
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        storageService.saveCategories(categories);
        List<Category> loaded = storageService.loadCategories();

        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0).getDescription()).hasSize(10000);
        assertThat(loaded.get(0).getDescription()).isEqualTo(longDescription);
    }
}
