package com.studylibrary.service;

import com.google.gson.*;
import com.studylibrary.model.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service for handling file I/O operations.
 * Manages persistence of library items and categories using JSON files.
 */
public class StorageService {

    private static StorageService instance;
    private static final String APP_DIR_NAME = ".studylibrary";
    private static final String ITEMS_FILE = "library-items.json";
    private static final String CATEGORIES_FILE = "categories.json";

    private final Path storageDirectory;
    private final Path itemsFilePath;
    private final Path categoriesFilePath;
    private final Gson gson;

    private StorageService() {
        // Initialize storage directory
        String userHome = System.getProperty("user.home");
        this.storageDirectory = Paths.get(userHome, APP_DIR_NAME);
        this.itemsFilePath = storageDirectory.resolve(ITEMS_FILE);
        this.categoriesFilePath = storageDirectory.resolve(CATEGORIES_FILE);

        // Initialize Gson with custom adapters
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LibraryItem.class, new LibraryItemAdapter())
                .create();

        // Create storage directory if it doesn't exist
        initializeStorage();
    }

    /**
     * Returns the singleton instance of StorageService.
     */
    public static synchronized StorageService getInstance() {
        if (instance == null) {
            instance = new StorageService();
        }
        return instance;
    }

    /**
     * Initializes the storage directory and creates default files if needed.
     */
    private void initializeStorage() {
        try {
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
                System.out.println("Created storage directory: " + storageDirectory);
            }

            // Create empty files if they don't exist
            if (!Files.exists(itemsFilePath)) {
                Files.writeString(itemsFilePath, "[]");
            }
            if (!Files.exists(categoriesFilePath)) {
                Files.writeString(categoriesFilePath, "[]");
            }
        } catch (IOException e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all library items from storage.
     */
    public List<LibraryItem> loadItems() {
        try {
            String json = Files.readString(itemsFilePath);
            LibraryItem[] itemsArray = gson.fromJson(json, LibraryItem[].class);
            return itemsArray != null ? Arrays.asList(itemsArray) : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all library items to storage.
     */
    public void saveItems(List<LibraryItem> items) {
        try {
            String json = gson.toJson(items);
            Files.writeString(itemsFilePath, json);
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all categories from storage.
     */
    public List<Category> loadCategories() {
        try {
            String json = Files.readString(categoriesFilePath);
            Category[] categoriesArray = gson.fromJson(json, Category[].class);
            return categoriesArray != null ? Arrays.asList(categoriesArray) : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading categories: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all categories to storage.
     */
    public void saveCategories(List<Category> categories) {
        try {
            String json = gson.toJson(categories);
            Files.writeString(categoriesFilePath, json);
        } catch (IOException e) {
            System.err.println("Error saving categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves all data (items and categories).
     */
    public void saveAll() {
        // This will be called by the controllers when needed
        // The actual saving happens in the service layer
    }

    /**
     * Returns the storage directory path.
     */
    public Path getStorageDirectory() {
        return storageDirectory;
    }

    /**
     * Custom Gson adapter for LocalDateTime serialization/deserialization.
     */
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,
            JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime dateTime, java.lang.reflect.Type type,
                JsonSerializationContext context) {
            return new JsonPrimitive(dateTime.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type type,
                JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }

    /**
     * Custom Gson adapter for LibraryItem polymorphic
     * serialization/deserialization.
     */
    private static class LibraryItemAdapter implements JsonSerializer<LibraryItem>,
            JsonDeserializer<LibraryItem> {
        @Override
        public JsonElement serialize(LibraryItem item, java.lang.reflect.Type type,
                JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", item.getItemType().name());
            jsonObject.add("data", context.serialize(item, item.getClass()));
            return jsonObject;
        }

        @Override
        public LibraryItem deserialize(JsonElement json, java.lang.reflect.Type type,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String itemType = jsonObject.get("type").getAsString();
            JsonElement data = jsonObject.get("data");

            return switch (itemType) {
                case "NOTE" -> context.deserialize(data, Note.class);
                case "PDF" -> context.deserialize(data, PdfDocument.class);
                case "MEDIA_LINK" -> context.deserialize(data, MediaLink.class);
                case "TEXT_SNIPPET" -> context.deserialize(data, TextSnippet.class);
                default -> throw new JsonParseException("Unknown item type: " + itemType);
            };
        }
    }
}
