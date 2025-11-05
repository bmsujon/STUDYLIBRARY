package com.documentvault.viewmodel;

import com.documentvault.model.Category;
import com.documentvault.model.LibraryItem;
import com.documentvault.service.LibraryService;
import com.documentvault.service.LibraryServiceImpl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the main library view.
 * Manages the state and data binding for the UI.
 */
public class LibraryViewModel {

    private final LibraryService libraryService;
    private final ObservableList<LibraryItem> items;
    private final ObservableList<Category> categories;
    private final StringProperty searchQuery;
    private final ObjectProperty<Category> selectedCategory;
    private final ObjectProperty<LibraryItem.ItemType> selectedItemType;

    public LibraryViewModel() {
        this.libraryService = LibraryServiceImpl.getInstance();
        this.items = FXCollections.observableArrayList();
        this.categories = FXCollections.observableArrayList();
        this.searchQuery = new SimpleStringProperty("");
        this.selectedCategory = new SimpleObjectProperty<>();
        this.selectedItemType = new SimpleObjectProperty<>();

        loadData();
        setupListeners();
    }

    /**
     * Loads all data from the service.
     */
    private void loadData() {
        refreshItems();
        refreshCategories();
    }

    /**
     * Sets up property change listeners.
     */
    private void setupListeners() {
        searchQuery.addListener((obs, oldVal, newVal) -> filterItems());
        selectedCategory.addListener((obs, oldVal, newVal) -> filterItems());
        selectedItemType.addListener((obs, oldVal, newVal) -> filterItems());
    }

    /**
     * Refreshes the items list from the service.
     */
    public void refreshItems() {
        items.setAll(libraryService.getAllItems());
    }

    /**
     * Refreshes the categories list from the service.
     */
    public void refreshCategories() {
        categories.setAll(libraryService.getAllCategories());
    }

    /**
     * Filters items based on current search and filter criteria.
     */
    public void filterItems() {
        var allItems = libraryService.getAllItems();

        // Apply search filter
        String query = searchQuery.get();
        if (query != null && !query.trim().isEmpty()) {
            allItems = libraryService.searchItems(query);
        }

        // Apply category filter
        Category category = selectedCategory.get();
        if (category != null) {
            allItems = allItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .toList();
        }

        // Apply type filter
        LibraryItem.ItemType type = selectedItemType.get();
        if (type != null) {
            allItems = allItems.stream()
                    .filter(item -> type.equals(item.getItemType()))
                    .toList();
        }

        items.setAll(allItems);
    }

    /**
     * Adds a new item.
     */
    public void addItem(LibraryItem item) {
        libraryService.addItem(item);
        refreshItems();
    }

    /**
     * Updates an existing item.
     */
    public void updateItem(LibraryItem item) {
        libraryService.updateItem(item);
        refreshItems();
    }

    /**
     * Deletes an item.
     */
    public void deleteItem(String itemId) {
        libraryService.deleteItem(itemId);
        refreshItems();
    }

    /**
     * Adds a new category.
     */
    public void addCategory(Category category) {
        libraryService.addCategory(category);
        refreshCategories();
    }

    /**
     * Updates an existing category.
     */
    public void updateCategory(Category category) {
        libraryService.updateCategory(category);
        refreshCategories();
    }

    /**
     * Deletes a category.
     */
    public void deleteCategory(String categoryId) {
        libraryService.deleteCategory(categoryId);
        refreshCategories();
        refreshItems(); // Refresh items as category references may have changed
    }

    // Property getters

    public ObservableList<LibraryItem> getItems() {
        return items;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery.get();
    }

    public void setSearchQuery(String query) {
        searchQuery.set(query);
    }

    public ObjectProperty<Category> selectedCategoryProperty() {
        return selectedCategory;
    }

    public Category getSelectedCategory() {
        return selectedCategory.get();
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.set(category);
    }

    public ObjectProperty<LibraryItem.ItemType> selectedItemTypeProperty() {
        return selectedItemType;
    }

    public LibraryItem.ItemType getSelectedItemType() {
        return selectedItemType.get();
    }

    public void setSelectedItemType(LibraryItem.ItemType type) {
        selectedItemType.set(type);
    }

    /**
     * Gets statistics about the library.
     */
    public int getTotalItemCount() {
        return libraryService.getItemCount();
    }

    public int getItemCountByType(LibraryItem.ItemType type) {
        return libraryService.getItemCountByType(type);
    }
}
