package com.documentvault.theme;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.prefs.Preferences;

/**
 * Manages application themes (Light/Dark mode).
 * Singleton pattern ensures consistent theme across the application.
 */
public class ThemeManager {
    private static final ThemeManager INSTANCE = new ThemeManager();
    private static final String THEME_PREF_KEY = "application.theme";
    private Preferences prefs;

    /**
     * Available themes for the application.
     */
    public enum Theme {
        LIGHT("light-theme.css", "Light"),
        DARK("dark-theme.css", "Dark");

        private final String cssFile;
        private final String displayName;

        Theme(String cssFile, String displayName) {
            this.cssFile = cssFile;
            this.displayName = displayName;
        }

        public String getCssFile() {
            return cssFile;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private Theme currentTheme;

    private ThemeManager() {
        // Initialize preferences safely
        try {
            prefs = Preferences.userNodeForPackage(ThemeManager.class);
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize preferences: " + e.getMessage());
            // Continue without preferences in test/restricted environments
        }

        // Load saved theme preference or default to LIGHT
        String savedTheme = Theme.LIGHT.name();
        if (prefs != null) {
            savedTheme = prefs.get(THEME_PREF_KEY, Theme.LIGHT.name());
        }

        try {
            currentTheme = Theme.valueOf(savedTheme);
        } catch (IllegalArgumentException e) {
            currentTheme = Theme.LIGHT;
        }
    }

    /**
     * Gets the singleton instance.
     */
    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the current active theme.
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Applies a theme to the given scene.
     * 
     * @param scene The scene to apply the theme to
     * @param theme The theme to apply
     */
    public void applyTheme(Scene scene, Theme theme) {
        if (scene == null || theme == null) {
            return;
        }

        scene.getStylesheets().clear();
        String themeUrl = getClass().getResource("/css/" + theme.getCssFile()).toExternalForm();
        scene.getStylesheets().add(themeUrl);

        currentTheme = theme;
        saveThemePreference(theme);
    }

    /**
     * Applies a theme to the given scene with a fade transition effect.
     * 
     * @param scene          The scene to apply the theme to
     * @param theme          The theme to apply
     * @param durationMillis Duration of the fade effect in milliseconds
     */
    public void applyThemeWithTransition(Scene scene, Theme theme, double durationMillis) {
        if (scene == null || theme == null || scene.getRoot() == null) {
            applyTheme(scene, theme);
            return;
        }

        Pane root = (Pane) scene.getRoot();

        // Fade out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(durationMillis / 2), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.8);

        fadeOut.setOnFinished(e -> {
            // Apply theme at lowest opacity
            applyTheme(scene, theme);

            // Fade in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(durationMillis / 2), root);
            fadeIn.setFromValue(0.8);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    /**
     * Toggles between light and dark themes.
     * 
     * @param scene The scene to apply the toggled theme to
     */
    public void toggleTheme(Scene scene) {
        Theme newTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        applyThemeWithTransition(scene, newTheme, 150);
    }

    /**
     * Saves the theme preference for future application launches.
     */
    private void saveThemePreference(Theme theme) {
        if (prefs != null) {
            try {
                prefs.put(THEME_PREF_KEY, theme.name());
            } catch (Exception e) {
                System.err.println("Warning: Could not save theme preference: " + e.getMessage());
            }
        }
    }

    /**
     * Gets the saved theme preference.
     * 
     * @return The saved theme, or LIGHT if no preference is saved
     */
    public Theme getSavedTheme() {
        if (prefs == null) {
            return Theme.LIGHT;
        }

        String savedTheme = prefs.get(THEME_PREF_KEY, Theme.LIGHT.name());
        try {
            return Theme.valueOf(savedTheme);
        } catch (IllegalArgumentException e) {
            return Theme.LIGHT;
        }
    }
}
