package com.documentvault.settings;

import com.documentvault.theme.ThemeManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager for application settings.
 * Provides centralized access to all application settings and handles persistence.
 */
public class SettingsManager {
    private static final SettingsManager INSTANCE = new SettingsManager();
    
    /**
     * Gets the singleton instance of SettingsManager.
     * 
     * @return the singleton instance
     */
    public static SettingsManager getInstance() {
        return INSTANCE;
    }
    
    // Settings directory - lazy initialization to avoid static initialization issues
    private final String settingsDir;
    private final String settingsFile = "settings.json";
    
    private String getSettingsDir() {
        String userHome = System.getProperty("user.home");
        if (userHome == null || userHome.isEmpty()) {
            // Fallback for test environments
            return System.getProperty("java.io.tmpdir") + "/.documentvault";
        }
        return userHome + "/.documentvault";
    }
    
    private String getBackupLocation() {
        return settingsDir + "/backups";
    }
    
    private String getExportLocation() {
        String userHome = System.getProperty("user.home");
        if (userHome == null || userHome.isEmpty()) {
            return System.getProperty("java.io.tmpdir");
        }
        return userHome + "/Documents";
    }
    
    // All application settings
    public final Setting<ThemeManager.Theme> theme = 
            new Setting<>("theme", ThemeManager.Theme.LIGHT);
    
    public final Setting<Integer> autoSaveIntervalSeconds = 
            new Setting<>("autoSaveInterval", 60);
    
    public final Setting<String> dateFormat = 
            new Setting<>("dateFormat", "yyyy-MM-dd");
    
    public final Setting<String> timeFormat = 
            new Setting<>("timeFormat", "HH:mm:ss");
    
    public final Setting<String> dateTimeFormat = 
            new Setting<>("dateTimeFormat", "yyyy-MM-dd HH:mm:ss");
    
    public final Setting<Integer> fontSize = 
            new Setting<>("fontSize", 14);
    
    public final Setting<TableDensity> tableDensity = 
            new Setting<>("tableDensity", TableDensity.COMFORTABLE);
    
    public final Setting<String> backupLocation;
    
    public final Setting<String> exportLocation;
    
    public final Setting<Boolean> confirmBeforeDelete = 
            new Setting<>("confirmBeforeDelete", true);
    
    public final Setting<Boolean> showDebugInfo = 
            new Setting<>("showDebugInfo", false);
    
    public final Setting<Boolean> enableAutoBackup = 
            new Setting<>("enableAutoBackup", true);
    
    public final Setting<Integer> autoBackupIntervalHours = 
            new Setting<>("autoBackupInterval", 24);
    
    private final SettingsStorage storage;
    private final Map<String, Setting<?>> allSettings;

    /**
     * Private constructor for singleton pattern.
     */
    private SettingsManager() {
        // Initialize directory first
        this.settingsDir = getSettingsDir();
        
        // Initialize location-dependent settings
        this.backupLocation = new Setting<>("backupLocation", getBackupLocation());
        this.exportLocation = new Setting<>("exportLocation", getExportLocation());
        
        this.storage = new SettingsStorage();
        this.allSettings = new HashMap<>();
        registerAllSettings();
        ensureSettingsDirectoryExists();
    }

    /**
     * Registers all settings for easy access.
     */
    private void registerAllSettings() {
        allSettings.put(theme.getKey(), theme);
        allSettings.put(autoSaveIntervalSeconds.getKey(), autoSaveIntervalSeconds);
        allSettings.put(dateFormat.getKey(), dateFormat);
        allSettings.put(timeFormat.getKey(), timeFormat);
        allSettings.put(dateTimeFormat.getKey(), dateTimeFormat);
        allSettings.put(fontSize.getKey(), fontSize);
        allSettings.put(tableDensity.getKey(), tableDensity);
        allSettings.put(backupLocation.getKey(), backupLocation);
        allSettings.put(exportLocation.getKey(), exportLocation);
        allSettings.put(confirmBeforeDelete.getKey(), confirmBeforeDelete);
        allSettings.put(showDebugInfo.getKey(), showDebugInfo);
        allSettings.put(enableAutoBackup.getKey(), enableAutoBackup);
        allSettings.put(autoBackupIntervalHours.getKey(), autoBackupIntervalHours);
    }

    /**
     * Ensures the settings directory exists.
     */
    private void ensureSettingsDirectoryExists() {
        try {
            if (settingsDir == null) {
                System.err.println("settingsDir is null");
                return;
            }
            Path settingsPath = Paths.get(settingsDir);
            if (!Files.exists(settingsPath)) {
                Files.createDirectories(settingsPath);
                System.out.println("Created settings directory: " + settingsPath);
            }
        } catch (Exception e) {
            System.err.println("Failed to create settings directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all settings from storage.
     * If settings file doesn't exist or has errors, defaults are used.
     */
    public void load() {
        try {
            Path settingsFile = Paths.get(settingsDir, this.settingsFile);
            if (Files.exists(settingsFile)) {
                storage.load(settingsFile, allSettings);
                System.out.println("Settings loaded from: " + settingsFile);
            } else {
                System.out.println("No settings file found, using defaults");
            }
        } catch (Exception e) {
            System.err.println("Failed to load settings, using defaults: " + e.getMessage());
        }
    }

    /**
     * Saves all current settings to storage.
     */
    public void save() {
        try {
            Path settingsFile = Paths.get(settingsDir, this.settingsFile);
            storage.save(settingsFile, allSettings);
            System.out.println("Settings saved to: " + settingsFile);
        } catch (Exception e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }

    /**
     * Resets all settings to their default values.
     */
    public void resetToDefaults() {
        for (Setting<?> setting : allSettings.values()) {
            setting.reset();
        }
        save();
        System.out.println("All settings reset to defaults");
    }

    /**
     * Gets the settings directory path.
     * 
     * @return the settings directory path
     */
    public String getSettingsDirectory() {
        return settingsDir;
    }

    /**
     * Gets the settings file path.
     * 
     * @return the settings file path
     */
    public String getSettingsFilePath() {
        return settingsDir + "/" + settingsFile;
    }

    /**
     * Enum for table density modes.
     */
    public enum TableDensity {
        COMPACT("Compact"),
        COMFORTABLE("Comfortable"),
        SPACIOUS("Spacious");

        private final String displayName;

        TableDensity(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
