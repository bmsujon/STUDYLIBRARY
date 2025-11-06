package com.documentvault.settings;

import com.documentvault.theme.ThemeManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Handles loading and saving settings to/from JSON files.
 */
public class SettingsStorage {
    private final Gson gson;

    /**
     * Creates a new SettingsStorage with pretty-printed JSON output.
     */
    public SettingsStorage() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Loads settings from a JSON file.
     * 
     * @param filePath the path to the settings file
     * @param settings the map of all settings to populate
     * @throws IOException if reading the file fails
     */
    public void load(Path filePath, Map<String, Setting<?>> settings) throws IOException {
        String json = Files.readString(filePath);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        for (Map.Entry<String, Setting<?>> entry : settings.entrySet()) {
            String key = entry.getKey();
            Setting<?> setting = entry.getValue();

            if (jsonObject.has(key)) {
                loadSetting(setting, jsonObject.get(key).getAsString());
            }
        }
    }

    /**
     * Saves all settings to a JSON file.
     * 
     * @param filePath the path to save the settings file
     * @param settings the map of all settings to save
     * @throws IOException if writing the file fails
     */
    public void save(Path filePath, Map<String, Setting<?>> settings) throws IOException {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Setting<?>> entry : settings.entrySet()) {
            String key = entry.getKey();
            Setting<?> setting = entry.getValue();
            jsonObject.addProperty(key, serializeValue(setting.getValue()));
        }

        String json = gson.toJson(jsonObject);
        Files.writeString(filePath, json);
    }

    /**
     * Loads a setting value from a string representation.
     * 
     * @param setting the setting to populate
     * @param value the string value to parse
     */
    @SuppressWarnings("unchecked")
    private void loadSetting(Setting<?> setting, String value) {
        try {
            Object parsedValue = parseValue(setting, value);
            ((Setting<Object>) setting).setValue(parsedValue);
        } catch (Exception e) {
            System.err.println("Failed to load setting " + setting.getKey() + ": " + e.getMessage());
        }
    }

    /**
     * Parses a string value based on the setting's type.
     * 
     * @param setting the setting whose value to parse
     * @param value the string value
     * @return the parsed value
     */
    private Object parseValue(Setting<?> setting, String value) {
        Object defaultValue = setting.getDefaultValue();

        if (defaultValue instanceof String) {
            return value;
        } else if (defaultValue instanceof Integer) {
            return Integer.parseInt(value);
        } else if (defaultValue instanceof Boolean) {
            return Boolean.parseBoolean(value);
        } else if (defaultValue instanceof ThemeManager.Theme) {
            return ThemeManager.Theme.valueOf(value);
        } else if (defaultValue instanceof SettingsManager.TableDensity) {
            return SettingsManager.TableDensity.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported setting type: " + defaultValue.getClass());
        }
    }

    /**
     * Serializes a setting value to a string.
     * 
     * @param value the value to serialize
     * @return the string representation
     */
    private String serializeValue(Object value) {
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        } else {
            return value.toString();
        }
    }
}
