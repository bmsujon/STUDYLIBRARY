package com.documentvault.model;

import java.util.Objects;

/**
 * Represents a category for organizing library items.
 */
public class Category {

    private String id;
    private String name;
    private String color; // Hex color code for UI display
    private String description;

    public Category() {
        this.id = java.util.UUID.randomUUID().toString();
        this.color = "#3498db"; // Default blue color
    }

    public Category(String name) {
        this();
        this.name = name;
    }

    public Category(String name, String color) {
        this();
        this.name = name;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name != null ? name : "Unnamed Category";
    }
}
