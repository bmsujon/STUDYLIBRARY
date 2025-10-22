package com.studylibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.studylibrary.service.StorageService;
import com.studylibrary.util.AlertUtil;

/**
 * Main application class for Study Library Manager.
 * This class initializes the JavaFX application and loads the main view.
 */
public class StudyLibraryApp extends Application {

    private static final String APP_TITLE = "Study Library Manager";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize storage service
            StorageService storageService = StorageService.getInstance();

            // Load the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();

            // Create and configure the scene
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

            // Configure the primary stage
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            // Set application icon (if available)
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app-icon.png")));
            } catch (Exception e) {
                // Icon not found, continue without it
                System.out.println("Application icon not found");
            }

            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                event.consume();
                handleExit(primaryStage);
            });

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Startup Error",
                    "Failed to start the application",
                    e.getMessage());
        }
    }

    /**
     * Handles application exit with auto-save.
     */
    private void handleExit(Stage stage) {
        try {
            // Save all data before closing
            StorageService.getInstance().saveAll();
            stage.close();
        } catch (Exception e) {
            boolean confirmed = AlertUtil.showConfirmation(
                    "Error Saving Data",
                    "An error occurred while saving data",
                    "Do you still want to exit? Unsaved changes will be lost.");
            if (confirmed) {
                stage.close();
            }
        }
    }

    @Override
    public void stop() {
        // Clean up resources
        try {
            StorageService.getInstance().saveAll();
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
