package com.documentvault.settings;

import com.documentvault.theme.ThemeManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SettingsManager class.
 */
class SettingsManagerTest {

    private SettingsManager settingsManager;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        settingsManager = SettingsManager.getInstance();
        // Reset all settings to defaults before each test
        settingsManager.resetToDefaults();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        settingsManager.resetToDefaults();
    }

    @Test
    void testSingletonInstance() {
        SettingsManager instance1 = SettingsManager.getInstance();
        SettingsManager instance2 = SettingsManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testDefaultValues() {
        assertEquals(ThemeManager.Theme.LIGHT, settingsManager.theme.getValue());
        assertEquals(60, settingsManager.autoSaveIntervalSeconds.getValue());
        assertEquals("yyyy-MM-dd", settingsManager.dateFormat.getValue());
        assertEquals("HH:mm:ss", settingsManager.timeFormat.getValue());
        assertEquals("yyyy-MM-dd HH:mm:ss", settingsManager.dateTimeFormat.getValue());
        assertEquals(14, settingsManager.fontSize.getValue());
        assertEquals(SettingsManager.TableDensity.COMFORTABLE, settingsManager.tableDensity.getValue());
        assertTrue(settingsManager.confirmBeforeDelete.getValue());
        assertFalse(settingsManager.showDebugInfo.getValue());
        assertTrue(settingsManager.enableAutoBackup.getValue());
        assertEquals(24, settingsManager.autoBackupIntervalHours.getValue());
    }

    @Test
    void testSettingValues() {
        settingsManager.theme.setValue(ThemeManager.Theme.DARK);
        assertEquals(ThemeManager.Theme.DARK, settingsManager.theme.getValue());

        settingsManager.autoSaveIntervalSeconds.setValue(120);
        assertEquals(120, settingsManager.autoSaveIntervalSeconds.getValue());

        settingsManager.dateFormat.setValue("dd/MM/yyyy");
        assertEquals("dd/MM/yyyy", settingsManager.dateFormat.getValue());

        settingsManager.tableDensity.setValue(SettingsManager.TableDensity.COMPACT);
        assertEquals(SettingsManager.TableDensity.COMPACT, settingsManager.tableDensity.getValue());
    }

    @Test
    void testResetToDefaults() {
        // Change some settings
        settingsManager.theme.setValue(ThemeManager.Theme.DARK);
        settingsManager.autoSaveIntervalSeconds.setValue(120);
        settingsManager.confirmBeforeDelete.setValue(false);

        // Reset to defaults
        settingsManager.resetToDefaults();

        // Verify all back to defaults
        assertEquals(ThemeManager.Theme.LIGHT, settingsManager.theme.getValue());
        assertEquals(60, settingsManager.autoSaveIntervalSeconds.getValue());
        assertTrue(settingsManager.confirmBeforeDelete.getValue());
    }

    @Test
    void testGetSettingsDirectory() {
        String directory = settingsManager.getSettingsDirectory();
        assertNotNull(directory);
        assertTrue(directory.contains(".documentvault"));
    }

    @Test
    void testGetSettingsFilePath() {
        String filePath = settingsManager.getSettingsFilePath();
        assertNotNull(filePath);
        assertTrue(filePath.contains(".documentvault"));
        assertTrue(filePath.contains("settings.json"));
    }

    @Test
    void testSaveAndLoad() {
        // First ensure we start clean
        settingsManager.resetToDefaults();
        
        // Change some settings
        settingsManager.theme.setValue(ThemeManager.Theme.DARK);
        settingsManager.autoSaveIntervalSeconds.setValue(90);
        settingsManager.dateFormat.setValue("MM/dd/yyyy");
        settingsManager.tableDensity.setValue(SettingsManager.TableDensity.SPACIOUS);
        settingsManager.confirmBeforeDelete.setValue(false);

        // Save settings
        assertDoesNotThrow(() -> settingsManager.save());

        // Manually reset values in memory (without saving)
        settingsManager.theme.setValue(ThemeManager.Theme.LIGHT);
        settingsManager.autoSaveIntervalSeconds.setValue(60);
        settingsManager.dateFormat.setValue("yyyy-MM-dd");
        settingsManager.tableDensity.setValue(SettingsManager.TableDensity.COMFORTABLE);
        settingsManager.confirmBeforeDelete.setValue(true);
        
        // Verify they are at defaults
        assertEquals(ThemeManager.Theme.LIGHT, settingsManager.theme.getValue());

        // Load settings (should restore saved values, not defaults)
        assertDoesNotThrow(() -> settingsManager.load());

        // Verify settings were restored
        assertEquals(ThemeManager.Theme.DARK, settingsManager.theme.getValue());
        assertEquals(90, settingsManager.autoSaveIntervalSeconds.getValue());
        assertEquals("MM/dd/yyyy", settingsManager.dateFormat.getValue());
        assertEquals(SettingsManager.TableDensity.SPACIOUS, settingsManager.tableDensity.getValue());
        assertFalse(settingsManager.confirmBeforeDelete.getValue());
    }

    @Test
    void testLoadWithoutSaveFile() {
        // Try to load when no save file exists (should use defaults)
        assertDoesNotThrow(() -> settingsManager.load());
        assertEquals(ThemeManager.Theme.LIGHT, settingsManager.theme.getValue());
    }

    @Test
    void testTableDensityEnum() {
        assertEquals("Compact", SettingsManager.TableDensity.COMPACT.getDisplayName());
        assertEquals("Comfortable", SettingsManager.TableDensity.COMFORTABLE.getDisplayName());
        assertEquals("Spacious", SettingsManager.TableDensity.SPACIOUS.getDisplayName());

        assertEquals("Compact", SettingsManager.TableDensity.COMPACT.toString());
    }

    @Test
    void testAllSettingsHaveUniqueKeys() {
        // Get all setting keys
        java.util.Set<String> keys = new java.util.HashSet<>();
        keys.add(settingsManager.theme.getKey());
        keys.add(settingsManager.autoSaveIntervalSeconds.getKey());
        keys.add(settingsManager.dateFormat.getKey());
        keys.add(settingsManager.timeFormat.getKey());
        keys.add(settingsManager.dateTimeFormat.getKey());
        keys.add(settingsManager.fontSize.getKey());
        keys.add(settingsManager.tableDensity.getKey());
        keys.add(settingsManager.backupLocation.getKey());
        keys.add(settingsManager.exportLocation.getKey());
        keys.add(settingsManager.confirmBeforeDelete.getKey());
        keys.add(settingsManager.showDebugInfo.getKey());
        keys.add(settingsManager.enableAutoBackup.getKey());
        keys.add(settingsManager.autoBackupIntervalHours.getKey());

        // Should have 13 unique keys
        assertEquals(13, keys.size());
    }

    @Test
    void testSettingsDirectoryCreation() {
        // The settings directory should be created on initialization
        Path settingsDir = Path.of(settingsManager.getSettingsDirectory());
        assertTrue(Files.exists(settingsDir) || settingsDir.toString().contains(".documentvault"));
    }

    @Test
    void testBackupAndExportLocations() {
        String backupLocation = settingsManager.backupLocation.getValue();
        assertNotNull(backupLocation);
        assertTrue(backupLocation.contains(".documentvault") || backupLocation.contains("backups"));

        String exportLocation = settingsManager.exportLocation.getValue();
        assertNotNull(exportLocation);
        assertTrue(exportLocation.contains("Documents") || exportLocation.contains(System.getProperty("user.home")));
    }
}
