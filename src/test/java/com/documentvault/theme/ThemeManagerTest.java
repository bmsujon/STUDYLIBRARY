package com.documentvault.theme;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ThemeManager.
 */
@ExtendWith(ApplicationExtension.class)
class ThemeManagerTest {

    private Scene scene;

    @Start
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testGetInstance() {
        ThemeManager instance1 = ThemeManager.getInstance();
        ThemeManager instance2 = ThemeManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "ThemeManager should be a singleton");
    }

    @Test
    void testDefaultTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        ThemeManager.Theme currentTheme = themeManager.getCurrentTheme();

        assertNotNull(currentTheme);
        assertTrue(currentTheme == ThemeManager.Theme.LIGHT ||
                currentTheme == ThemeManager.Theme.DARK,
                "Current theme should be either LIGHT or DARK");
    }

    @Test
    void testApplyLightTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();

        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);

        assertEquals(ThemeManager.Theme.LIGHT, themeManager.getCurrentTheme());
        assertFalse(scene.getStylesheets().isEmpty());
        assertTrue(scene.getStylesheets().get(0).contains("light-theme.css"));
    }

    @Test
    void testApplyDarkTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();

        themeManager.applyTheme(scene, ThemeManager.Theme.DARK);

        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());
        assertFalse(scene.getStylesheets().isEmpty());
        assertTrue(scene.getStylesheets().get(0).contains("dark-theme.css"));
    }

    @Test
    void testToggleThemeFromLightToDark() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Start with light theme
        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);
        assertEquals(ThemeManager.Theme.LIGHT, themeManager.getCurrentTheme());

        // Toggle to dark
        themeManager.toggleTheme(scene);

        // Wait for animation to complete
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());
    }

    @Test
    void testToggleThemeFromDarkToLight() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Start with dark theme
        themeManager.applyTheme(scene, ThemeManager.Theme.DARK);
        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());

        // Toggle to light
        themeManager.toggleTheme(scene);

        // Wait for animation to complete
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(ThemeManager.Theme.LIGHT, themeManager.getCurrentTheme());
    }

    @Test
    void testApplyNullScene() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Should not throw exception
        assertDoesNotThrow(() -> themeManager.applyTheme(null, ThemeManager.Theme.LIGHT));
    }

    @Test
    void testApplyNullTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        ThemeManager.Theme beforeTheme = themeManager.getCurrentTheme();

        // Should not throw exception and theme should remain unchanged
        assertDoesNotThrow(() -> themeManager.applyTheme(scene, null));

        assertEquals(beforeTheme, themeManager.getCurrentTheme());
    }

    @Test
    void testThemeEnumValues() {
        assertEquals(2, ThemeManager.Theme.values().length);
        assertEquals("LIGHT", ThemeManager.Theme.LIGHT.name());
        assertEquals("DARK", ThemeManager.Theme.DARK.name());
    }

    @Test
    void testThemeDisplayNames() {
        assertEquals("Light", ThemeManager.Theme.LIGHT.getDisplayName());
        assertEquals("Dark", ThemeManager.Theme.DARK.getDisplayName());
    }

    @Test
    void testThemeCssFiles() {
        assertEquals("light-theme.css", ThemeManager.Theme.LIGHT.getCssFile());
        assertEquals("dark-theme.css", ThemeManager.Theme.DARK.getCssFile());
    }

    @Test
    void testGetSavedTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        ThemeManager.Theme savedTheme = themeManager.getSavedTheme();

        assertNotNull(savedTheme);
        assertTrue(savedTheme == ThemeManager.Theme.LIGHT ||
                savedTheme == ThemeManager.Theme.DARK);
    }

    @Test
    void testThemePersistence() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Apply dark theme
        themeManager.applyTheme(scene, ThemeManager.Theme.DARK);

        // Get saved theme
        ThemeManager.Theme savedTheme = themeManager.getSavedTheme();

        assertEquals(ThemeManager.Theme.DARK, savedTheme);
    }

    @Test
    void testApplyThemeWithTransition() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Should not throw exception
        assertDoesNotThrow(() -> themeManager.applyThemeWithTransition(scene, ThemeManager.Theme.DARK, 150));

        // Give time for transition (though test may complete before animation)
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());
    }

    @Test
    void testMultipleThemeSwitches() {
        ThemeManager themeManager = ThemeManager.getInstance();

        // Switch multiple times
        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);
        assertEquals(ThemeManager.Theme.LIGHT, themeManager.getCurrentTheme());

        themeManager.applyTheme(scene, ThemeManager.Theme.DARK);
        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());

        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);
        assertEquals(ThemeManager.Theme.LIGHT, themeManager.getCurrentTheme());

        themeManager.applyTheme(scene, ThemeManager.Theme.DARK);
        assertEquals(ThemeManager.Theme.DARK, themeManager.getCurrentTheme());
    }
}
