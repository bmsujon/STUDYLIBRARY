package com.documentvault.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time formatting operations.
 */
public class DateUtil {

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Formats a LocalDateTime for display in the UI.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as a short date (no time).
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(SHORT_DATE_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as time only.
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(TIME_FORMATTER);
    }

    /**
     * Returns a relative time string (e.g., "2 hours ago", "3 days ago").
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }

        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(dateTime, now).toMinutes();

        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (minutes < 1440) { // Less than 24 hours
            long hours = minutes / 60;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes < 10080) { // Less than 7 days
            long days = minutes / 1440;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return formatDate(dateTime);
        }
    }
}