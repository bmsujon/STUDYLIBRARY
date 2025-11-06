package com.documentvault.settings;

import com.documentvault.theme.ThemeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SettingsStorage class.
 */
class SettingsStorageTest {

    private SettingsStorage storage;
    private Map<String, Setting<?>> settings;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        storage = new SettingsStorage();
        settings = new HashMap<>();

        // Create test settings
        settings.put("theme", new Setting<>("theme", ThemeManager.Theme.LIGHT));
        settings.put("autoSaveInterval", new Setting<>("autoSaveInterval", 60));
        settings.put("dateFormat", new Setting<>("dateFormat", "yyyy-MM-dd"));
        settings.put("confirmDelete", new Setting<>("confirmDelete", true));
        settings.put("tableDensity", new Setting<>("tableDensity", SettingsManager.TableDensity.COMFORTABLE));
    }

    @SuppressWarnings("unchecked")
    private <T> Setting<T> getSetting(String key) {
        return (Setting<T>) settings.get(key);
    }

    @Test
    void testSaveAndLoad() throws Exception {
        Path settingsFile = tempDir.resolve("test-settings.json");

        // Modify some settings
        Setting<ThemeManager.Theme> themeSetting = getSetting("theme");
        themeSetting.setValue(ThemeManager.Theme.DARK);
        
        Setting<Integer> intervalSetting = getSetting("autoSaveInterval");
        intervalSetting.setValue(120);
        
        Setting<String> formatSetting = getSetting("dateFormat");
        formatSetting.setValue("MM/dd/yyyy");
        
        Setting<Boolean> confirmSetting = getSetting("confirmDelete");
        confirmSetting.setValue(false);
        
        Setting<SettingsManager.TableDensity> densitySetting = getSetting("tableDensity");
        densitySetting.setValue(SettingsManager.TableDensity.COMPACT);

        // Save
        storage.save(settingsFile, settings);
        assertTrue(Files.exists(settingsFile));

        // Reset to defaults
        themeSetting.setValue(ThemeManager.Theme.LIGHT);
        intervalSetting.setValue(60);
        formatSetting.setValue("yyyy-MM-dd");
        confirmSetting.setValue(true);
        densitySetting.setValue(SettingsManager.TableDensity.COMFORTABLE);

        // Load
        storage.load(settingsFile, settings);

        // Verify
        assertEquals(ThemeManager.Theme.DARK, themeSetting.getValue());
        assertEquals(120, intervalSetting.getValue());
        assertEquals("MM/dd/yyyy", formatSetting.getValue());
        assertFalse(confirmSetting.getValue());
        assertEquals(SettingsManager.TableDensity.COMPACT, densitySetting.getValue());
    }

    @Test
    void testSaveCreatesValidJson() throws Exception {
        Path settingsFile = tempDir.resolve("settings.json");

        // Save settings
        storage.save(settingsFile, settings);

        // Read and verify JSON structure
        String json = Files.readString(settingsFile);
        assertTrue(json.contains("\"theme\""));
        assertTrue(json.contains("\"autoSaveInterval\""));
        assertTrue(json.contains("\"dateFormat\""));
        assertTrue(json.contains("\"confirmDelete\""));
        assertTrue(json.contains("\"tableDensity\""));
    }

    @Test
    void testLoadWithMissingFile() {
        Path nonExistentFile = tempDir.resolve("non-existent.json");

        assertThrows(Exception.class, () -> {
            storage.load(nonExistentFile, settings);
        });
    }

    @Test
    void testLoadPartialSettings() throws Exception {
        Path settingsFile = tempDir.resolve("partial-settings.json");

        // Create JSON with only some settings
        String partialJson = """
                {
                  "theme": "DARK",
                  "autoSaveInterval": "90"
                }
                """;
        Files.writeString(settingsFile, partialJson);

        // Load
        storage.load(settingsFile, settings);

        // Verify loaded settings
        Setting<ThemeManager.Theme> themeSetting = getSetting("theme");
        assertEquals(ThemeManager.Theme.DARK, themeSetting.getValue());
        
        Setting<Integer> intervalSetting = getSetting("autoSaveInterval");
        assertEquals(90, intervalSetting.getValue());

        // Unmentioned settings should remain at defaults
        Setting<String> formatSetting = getSetting("dateFormat");
        assertEquals("yyyy-MM-dd", formatSetting.getValue());
        
        Setting<Boolean> confirmSetting = getSetting("confirmDelete");
        assertTrue(confirmSetting.getValue());
    }

    @Test
    void testLoadInvalidTheme() throws Exception {
        Path settingsFile = tempDir.resolve("invalid-theme.json");

        // Create JSON with invalid theme
        String invalidJson = """
                {
                  "theme": "INVALID_THEME"
                }
                """;
        Files.writeString(settingsFile, invalidJson);

        // Should not throw, just skip invalid setting
        assertDoesNotThrow(() -> storage.load(settingsFile, settings));

        // Theme should remain at default
        Setting<ThemeManager.Theme> themeSetting = getSetting("theme");
        assertEquals(ThemeManager.Theme.LIGHT, themeSetting.getValue());
    }

    @Test
    void testLoadInvalidInteger() throws Exception {
        Path settingsFile = tempDir.resolve("invalid-int.json");

        String invalidJson = """
                {
                  "autoSaveInterval": "not_a_number"
                }
                """;
        Files.writeString(settingsFile, invalidJson);

        // Should not throw, just skip invalid setting
        assertDoesNotThrow(() -> storage.load(settingsFile, settings));

        // Should remain at default
        Setting<Integer> intervalSetting = getSetting("autoSaveInterval");
        assertEquals(60, intervalSetting.getValue());
    }

    @Test
    void testLoadInvalidBoolean() throws Exception {
        Path settingsFile = tempDir.resolve("invalid-bool.json");

        String invalidJson = """
                {
                  "confirmDelete": "not_a_boolean"
                }
                """;
        Files.writeString(settingsFile, invalidJson);

        // Boolean.parseBoolean will return false for invalid strings
        assertDoesNotThrow(() -> storage.load(settingsFile, settings));
    }

    @Test
    void testSaveAllSettingTypes() throws Exception {
        Path settingsFile = tempDir.resolve("all-types.json");

        // Set various types
        Setting<ThemeManager.Theme> themeSetting = getSetting("theme");
        themeSetting.setValue(ThemeManager.Theme.DARK);
        
        Setting<Integer> intervalSetting = getSetting("autoSaveInterval");
        intervalSetting.setValue(100);
        
        Setting<String> formatSetting = getSetting("dateFormat");
        formatSetting.setValue("custom-format");
        
        Setting<Boolean> confirmSetting = getSetting("confirmDelete");
        confirmSetting.setValue(false);
        
        Setting<SettingsManager.TableDensity> densitySetting = getSetting("tableDensity");
        densitySetting.setValue(SettingsManager.TableDensity.SPACIOUS);

        // Save
        storage.save(settingsFile, settings);

        // Verify file content
        String json = Files.readString(settingsFile);
        assertTrue(json.contains("DARK"));
        assertTrue(json.contains("100"));
        assertTrue(json.contains("custom-format"));
        assertTrue(json.contains("false"));
        assertTrue(json.contains("SPACIOUS"));
    }

    @Test
    void testEmptySettingsMap() throws Exception {
        Path settingsFile = tempDir.resolve("empty-settings.json");
        Map<String, Setting<?>> emptySettings = new HashMap<>();

        // Should create an empty JSON object
        storage.save(settingsFile, emptySettings);

        String json = Files.readString(settingsFile);
        assertTrue(json.contains("{"));
        assertTrue(json.contains("}"));
    }

    @Test
    void testPrettyPrintedJson() throws Exception {
        Path settingsFile = tempDir.resolve("pretty.json");

        storage.save(settingsFile, settings);

        String json = Files.readString(settingsFile);
        // Check for formatting (multiple lines with indentation)
        assertTrue(json.lines().count() > 1);
        assertTrue(json.contains("  ")); // Has indentation
    }
}
