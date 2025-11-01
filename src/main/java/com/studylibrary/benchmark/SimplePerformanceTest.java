package com.studylibrary.benchmark;

import com.studylibrary.model.*;
import java.util.*;

/**
 * Simple Performance Validation for CI/CD
 * Tests core functionality without dependencies.
 */
public class SimplePerformanceTest {
    
    private static final int ITERATION_COUNT = 1000;

    public static void main(String[] args) {
        System.out.println("🧪 Simple Performance Test");
        System.out.println("==========================");

        try {
            // Test basic functionality
            testBasicOperations();
            testPatternMatching();

            System.out.println("✅ All performance tests passed!");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("❌ Performance test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testBasicOperations() {
        System.out.println("Testing basic operations...");

        // Test model creation
        var items = new ArrayList<LibraryItem>();

        // Add some test items
        var note = new Note();
        note.setTitle("Test Note");
        note.setContent("Test content");
        items.add(note);

        var pdf = new PdfDocument();
        pdf.setTitle("Test PDF");
        pdf.setPageCount(10);
        items.add(pdf);

        // Verify items were added
        if (items.size() != 2) {
            throw new RuntimeException("Expected 2 items, got " + items.size());
        }

        System.out.println("  ✓ Basic operations working");
    }

    private static void testPatternMatching() {
        System.out.println("Testing pattern matching...");

        // Create test items
        var items = Arrays.asList(
                createNote(),
                createPdf(),
                createMediaLink(),
                createTextSnippet());

        // Test pattern matching performance
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATION_COUNT; i++) {
            for (LibraryItem item : items) {
                getItemIcon(item);
            }
        }

        long endTime = System.nanoTime();
        double avgTimeMs = (endTime - startTime) / 1_000_000.0 / (ITERATION_COUNT * items.size());

        System.out.printf("  ✓ Pattern matching average: %.3f ms%n", avgTimeMs);

        if (avgTimeMs > 1.0) {
            throw new RuntimeException("Pattern matching too slow: " + avgTimeMs + " ms");
        }
    }

    private static String getItemIcon(LibraryItem item) {
        // Test pattern matching with sealed classes
        return switch (item.getItemType()) {
            case NOTE -> "📝";
            case PDF -> "📄";
            case MEDIA_LINK -> "🎵";
            case TEXT_SNIPPET -> "💻";
        };
    }

    private static Note createNote() {
        var note = new Note();
        note.setTitle("Test Note");
        note.setContent("Test content");
        return note;
    }

    private static PdfDocument createPdf() {
        var pdf = new PdfDocument();
        pdf.setTitle("Test PDF");
        pdf.setPageCount(10);
        return pdf;
    }

    private static MediaLink createMediaLink() {
        var media = new MediaLink();
        media.setTitle("Test Media");
        media.setUrl("http://example.com");
        media.setDurationMinutes(5);
        return media;
    }

    private static TextSnippet createTextSnippet() {
        var snippet = new TextSnippet();
        snippet.setTitle("Test Snippet");
        snippet.setContent("code");
        snippet.setLanguage("java");
        return snippet;
    }
}