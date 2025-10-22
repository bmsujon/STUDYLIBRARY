package com.studylibrary.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for file and URL operations.
 */
public class FileUtil {

    /**
     * Opens a file with the system's default application.
     */
    public static boolean openFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Opens a URL in the system's default browser.
     */
    public static boolean openUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error opening URL: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if a file exists.
     */
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * Gets the file size in bytes.
     */
    public static long getFileSize(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return 0;
        }

        try {
            return Files.size(Path.of(filePath));
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Formats a file size in bytes to a human-readable string.
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * Validates if a string is a valid URL.
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        String lower = url.toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://");
    }
}
