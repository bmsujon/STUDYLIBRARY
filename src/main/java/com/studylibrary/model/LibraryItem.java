package com.studylibrary.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract base class for all library items.
 * Provides common properties and functionality for Notes, PDFs, Media Links,
 * and Text Snippets.
 */
public abstract class LibraryItem {

    private String id;
    private String title;
    private String description;
    private Category category;
    private Set<String> tags;
    private LocalDateTime dateAdded;
    private LocalDateTime lastModified;
    private ItemType itemType;

    /**
     * Enum representing the different types of library items.
     */
    public enum ItemType {
        NOTE("Note"),
        PDF("PDF Document"),
        MEDIA_LINK("Media Link"),
        TEXT_SNIPPET("Text Snippet");

        private final String displayName;

        ItemType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Constructor for creating a new library item.
     */
    public LibraryItem(ItemType itemType) {
        this.id = UUID.randomUUID().toString();
        this.itemType = itemType;
        this.tags = new HashSet<>();
        this.dateAdded = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    /**
     * Updates the last modified timestamp.
     */
    public void touch() {
        this.lastModified = LocalDateTime.now();
    }

    /**
     * Adds a tag to this item.
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.trim().toLowerCase());
            touch();
        }
    }

    /**
     * Removes a tag from this item.
     */
    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag.trim().toLowerCase());
            touch();
        }
    }

    /**
     * Checks if this item has a specific tag.
     */
    public boolean hasTag(String tag) {
        return tag != null && this.tags.contains(tag.trim().toLowerCase());
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        touch();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        touch();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
        touch();
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    public void setTags(Set<String> tags) {
        this.tags = new HashSet<>(tags);
        touch();
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * Returns a string representation suitable for search/filter operations.
     */
    public String getSearchableText() {
        StringBuilder sb = new StringBuilder();
        sb.append(title != null ? title : "").append(" ");
        sb.append(description != null ? description : "").append(" ");
        sb.append(String.join(" ", tags)).append(" ");
        if (category != null) {
            sb.append(category.getName());
        }
        return sb.toString().toLowerCase();
    }

    @Override
    public String toString() {
        return title != null ? title : "Untitled " + itemType.getDisplayName();
    }
}
