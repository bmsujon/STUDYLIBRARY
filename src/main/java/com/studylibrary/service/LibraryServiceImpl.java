package com.studylibrary.service;

import com.studylibrary.model.Category;
import com.studylibrary.model.LibraryItem;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the LibraryService interface.
 * Manages library items and categories with in-memory storage.
 */
public class LibraryServiceImpl implements LibraryService {

    private static LibraryServiceImpl instance;
    private final Map<String, LibraryItem> items;
    private final Map<String, Category> categories;
    private final StorageService storageService;

    private LibraryServiceImpl() {
        this.items = new HashMap<>();
        this.categories = new HashMap<>();
        this.storageService = StorageService.getInstance();
        loadData();
    }

    /**
     * Returns the singleton instance of LibraryService.
     */
    public static synchronized LibraryServiceImpl getInstance() {
        if (instance == null) {
            instance = new LibraryServiceImpl();
        }
        return instance;
    }

    /**
     * Loads data from storage.
     */
    private void loadData() {
        // Load categories
        List<Category> loadedCategories = storageService.loadCategories();
        for (Category category : loadedCategories) {
            categories.put(category.getId(), category);
        }

        // Load items
        List<LibraryItem> loadedItems = storageService.loadItems();
        for (LibraryItem item : loadedItems) {
            items.put(item.getId(), item);
        }
    }

    @Override
    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<LibraryItem> getItemById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void addItem(LibraryItem item) {
        if (item != null && item.getId() != null) {
            items.put(item.getId(), item);
            storageService.saveItems(getAllItems());
        }
    }

    @Override
    public void updateItem(LibraryItem item) {
        if (item != null && item.getId() != null && items.containsKey(item.getId())) {
            item.touch();
            items.put(item.getId(), item);
            storageService.saveItems(getAllItems());
        }
    }

    @Override
    public void deleteItem(String id) {
        if (id != null && items.containsKey(id)) {
            items.remove(id);
            storageService.saveItems(getAllItems());
        }
    }

    @Override
    public List<LibraryItem> searchItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllItems();
        }

        String searchQuery = query.toLowerCase().trim();
        return items.values().stream()
                .filter(item -> item.getSearchableText().contains(searchQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<LibraryItem> getItemsByCategory(Category category) {
        if (category == null) {
            return getAllItems();
        }

        return items.values().stream()
                .filter(item -> category.equals(item.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LibraryItem> getItemsByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return getAllItems();
        }

        String searchTag = tag.toLowerCase().trim();
        return items.values().stream()
                .filter(item -> item.hasTag(searchTag))
                .collect(Collectors.toList());
    }

    @Override
    public List<LibraryItem> getItemsByType(LibraryItem.ItemType type) {
        if (type == null) {
            return getAllItems();
        }

        return items.values().stream()
                .filter(item -> type.equals(item.getItemType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Optional<Category> getCategoryById(String id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public void addCategory(Category category) {
        if (category != null && category.getId() != null) {
            categories.put(category.getId(), category);
            storageService.saveCategories(getAllCategories());
        }
    }

    @Override
    public void updateCategory(Category category) {
        if (category != null && category.getId() != null && categories.containsKey(category.getId())) {
            categories.put(category.getId(), category);
            storageService.saveCategories(getAllCategories());
        }
    }

    @Override
    public void deleteCategory(String id) {
        if (id != null && categories.containsKey(id)) {
            Category categoryToDelete = categories.get(id);

            // Remove category from all items
            items.values().stream()
                    .filter(item -> categoryToDelete.equals(item.getCategory()))
                    .forEach(item -> item.setCategory(null));

            categories.remove(id);
            storageService.saveCategories(getAllCategories());
            storageService.saveItems(getAllItems());
        }
    }

    @Override
    public List<String> getAllTags() {
        Set<String> allTags = new HashSet<>();
        items.values().forEach(item -> allTags.addAll(item.getTags()));
        return new ArrayList<>(allTags).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemCountByType(LibraryItem.ItemType type) {
        if (type == null) {
            return 0;
        }
        return (int) items.values().stream()
                .filter(item -> type.equals(item.getItemType()))
                .count();
    }
}
