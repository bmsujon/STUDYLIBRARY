package com.documentvault.service;

import com.documentvault.model.Category;
import com.documentvault.model.LibraryItem;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing library items and categories.
 * Provides business logic operations for the study library.
 */
public interface LibraryService {

    /**
     * Retrieves all library items.
     */
    List<LibraryItem> getAllItems();

    /**
     * Retrieves an item by its ID.
     */
    Optional<LibraryItem> getItemById(String id);

    /**
     * Adds a new item to the library.
     */
    void addItem(LibraryItem item);

    /**
     * Updates an existing item.
     */
    void updateItem(LibraryItem item);

    /**
     * Deletes an item from the library.
     */
    void deleteItem(String id);

    /**
     * Searches for items matching the query string.
     */
    List<LibraryItem> searchItems(String query);

    /**
     * Filters items by category.
     */
    List<LibraryItem> getItemsByCategory(Category category);

    /**
     * Filters items by tag.
     */
    List<LibraryItem> getItemsByTag(String tag);

    /**
     * Filters items by type.
     */
    List<LibraryItem> getItemsByType(LibraryItem.ItemType type);

    /**
     * Retrieves all categories.
     */
    List<Category> getAllCategories();

    /**
     * Retrieves a category by its ID.
     */
    Optional<Category> getCategoryById(String id);

    /**
     * Adds a new category.
     */
    void addCategory(Category category);

    /**
     * Updates an existing category.
     */
    void updateCategory(Category category);

    /**
     * Deletes a category.
     */
    void deleteCategory(String id);

    /**
     * Retrieves all unique tags from all items.
     */
    List<String> getAllTags();

    /**
     * Gets the total count of items.
     */
    int getItemCount();

    /**
     * Gets count of items by type.
     */
    int getItemCountByType(LibraryItem.ItemType type);
}
