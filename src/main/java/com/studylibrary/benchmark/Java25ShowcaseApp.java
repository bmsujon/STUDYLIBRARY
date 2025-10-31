package com.studylibrary.benchmark;

import com.studylibrary.model.*;
import com.studylibrary.service.LibraryServiceImpl;
import com.studylibrary.util.PerformanceBenchmark;

/**
 * Java 25 Performance Test Showcase
 * Demonstrates the optimizations we've implemented.
 */
public class Java25ShowcaseApp {

    public static void main(String[] args) {
        System.out.println("🚀 Java 25 Study Library Optimization Showcase");
        System.out.println("===============================================\n");

        // Initialize service with sample data
        var service = LibraryServiceImpl.getInstance();
        setupSampleData(service);

        // Test 1: Sealed Classes and Pattern Matching
        System.out.println("1️⃣ Sealed Classes & Pattern Matching Demo");
        System.out.println("==========================================");
        demonstratePatternMatching(service);

        // Test 2: Records Performance
        System.out.println("\n2️⃣ Records & SearchCriteria Demo");
        System.out.println("================================");
        demonstrateRecords(service);

        // Test 3: Performance Benchmarking
        System.out.println("\n3️⃣ Performance Benchmarking");
        System.out.println("============================");
        runPerformanceBenchmarks(service);

        // Test 4: Modern String Operations
        System.out.println("\n4️⃣ Modern String Operations");
        System.out.println("============================");
        demonstrateStringOptimizations(service);

        System.out.println("\n🎉 Java 25 optimization showcase completed!");
        System.out.println("All features are working with enhanced performance.");
    }

    private static void setupSampleData(LibraryServiceImpl service) {
        // Create diverse sample data for testing
        for (int i = 1; i <= 50; i++) {
            // Notes
            var note = new Note();
            note.setTitle("Sample Note " + i);
            note.setContent("This is sample content for note " + i +
                    ". It contains various keywords for search testing: java, programming, algorithms.");
            note.addTag("sample");
            note.addTag("note");
            if (i % 5 == 0)
                note.addTag("important");
            service.addItem(note);

            // PDFs
            var pdf = new PdfDocument();
            pdf.setTitle("Document " + i);
            pdf.setDescription("Sample PDF document " + i);
            pdf.setPageCount(i * 10);
            pdf.addTag("document");
            pdf.addTag("pdf");
            service.addItem(pdf);

            // Media Links
            var media = new MediaLink();
            media.setTitle("Video Tutorial " + i);
            media.setDescription("Educational video " + i);
            media.setUrl("https://example.com/video" + i);
            media.setDurationMinutes(i * 5);
            media.addTag("video");
            media.addTag("tutorial");
            service.addItem(media);

            // Text Snippets
            var snippet = new TextSnippet();
            snippet.setTitle("Code Snippet " + i);
            snippet.setContent("function example" + i + "() { return 'Hello World'; }");
            snippet.setLanguage(i % 2 == 0 ? "javascript" : "java");
            snippet.addTag("code");
            snippet.addTag("snippet");
            service.addItem(snippet);
        }
    }

    private static void demonstratePatternMatching(LibraryServiceImpl service) {
        var criteria = new SearchCriteria("");
        var items = service.getAllItems();

        System.out.println("Testing pattern matching with sealed classes:");
        System.out.println("Items processed: " + items.size());

        // Count items by type using pattern matching
        long noteCount = 0, pdfCount = 0, mediaCount = 0, snippetCount = 0;

        for (LibraryItem item : items) {
            // Java 25: Pattern matching with sealed classes
            switch (item.getItemType()) {
                case NOTE -> {
                    noteCount++;
                    String icon = criteria.getItemTypeIcon(item);
                    if (noteCount <= 3) {
                        System.out.printf("  %s %s: %s%n", icon, item.getTitle(),
                                criteria.getEnhancedDescription(item));
                    }
                }
                case PDF -> {
                    pdfCount++;
                    if (pdfCount <= 3) {
                        System.out.printf("  %s %s: %s%n",
                                criteria.getItemTypeIcon(item), item.getTitle(),
                                criteria.getEnhancedDescription(item));
                    }
                }
                case MEDIA_LINK -> {
                    mediaCount++;
                    if (mediaCount <= 3) {
                        System.out.printf("  %s %s: %s%n",
                                criteria.getItemTypeIcon(item), item.getTitle(),
                                criteria.getEnhancedDescription(item));
                    }
                }
                case TEXT_SNIPPET -> {
                    snippetCount++;
                    if (snippetCount <= 3) {
                        System.out.printf("  %s %s: %s%n",
                                criteria.getItemTypeIcon(item), item.getTitle(),
                                criteria.getEnhancedDescription(item));
                    }
                }
            }
        }

        System.out.printf("Type distribution: Notes=%d, PDFs=%d, Media=%d, Snippets=%d%n",
                noteCount, pdfCount, mediaCount, snippetCount);
    }

    private static void demonstrateRecords(LibraryServiceImpl service) {
        System.out.println("Testing SearchCriteria record performance:");

        // Test different search criteria
        var searches = new SearchCriteria[] {
                new SearchCriteria("java"),
                new SearchCriteria("sample"),
                new SearchCriteria("tutorial"),
                new SearchCriteria("")
        };

        for (var criteria : searches) {
            var results = service.searchWithCriteria(criteria);
            System.out.printf("Query: '%s' -> %d results%n",
                    criteria.query().isEmpty() ? "all" : criteria.query(), results.size());
        }
    }

    private static void runPerformanceBenchmarks(LibraryServiceImpl service) {
        var benchmark = new PerformanceBenchmark(service);
        var report = benchmark.generateReport();

        System.out.println("Performance Report:");
        System.out.println(report.generateSummary());
    }

    private static void demonstrateStringOptimizations(LibraryServiceImpl service) {
        System.out.println("Testing optimized string operations:");

        var items = service.getAllItems().stream().limit(5).toList();

        for (LibraryItem item : items) {
            // Test optimized searchable text generation
            String searchText = item.getSearchableText();
            System.out.printf("Item: %s -> Search text length: %d chars%n",
                    item.getTitle(), searchText.length());
        }

        // Test performance stats
        var stats = service.getPerformanceStats();
        System.out.println("\nLibrary Statistics:");
        stats.forEach((key, value) -> System.out.printf("  %s: %s%n", key, value));
    }
}