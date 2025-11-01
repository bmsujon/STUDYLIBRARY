package com.studylibrary.benchmark;

import com.studylibrary.model.*;
import com.studylibrary.service.LibraryServiceImpl;
import com.studylibrary.util.PerformanceBenchmark;

/**
 * Headless Performance Benchmark for CI/CD
 * Runs performance tests without any GUI dependencies.
 */
public class HeadlessBenchmark {

    public static void main(String[] args) {
        System.out.println("🚀 Headless Performance Benchmark");
        System.out.println("=================================\n");

        try {
            // Initialize service
            var service = LibraryServiceImpl.getInstance();
            setupSampleData(service);

            // Run benchmarks
            runPerformanceBenchmarks(service);

            System.out.println("\n✅ Benchmark completed successfully!");

            // Exit cleanly
            System.exit(0);

        } catch (Exception e) {
            System.err.println("❌ Benchmark failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void setupSampleData(LibraryServiceImpl service) {
        System.out.println("Setting up test data...");

        // Create minimal sample data for testing
        for (int i = 1; i <= 10; i++) {
            // Notes
            var note = new Note();
            note.setTitle("Sample Note " + i);
            note.setContent("Test content " + i);
            note.addTag("sample");
            service.addItem(note);

            // PDFs
            var pdf = new PdfDocument();
            pdf.setTitle("Document " + i);
            pdf.setPageCount(i * 5);
            pdf.addTag("document");
            service.addItem(pdf);
        }

        System.out.println("Created " + service.getAllItems().size() + " test items");
    }

    private static void runPerformanceBenchmarks(LibraryServiceImpl service) {
        System.out.println("Running performance benchmarks...");

        var benchmark = new PerformanceBenchmark(service);

        // Quick benchmarks suitable for CI
        var searchResult = benchmark.benchmarkSearch("sample", 10);
        var parallelResult = benchmark.benchmarkParallelOperations();
        var patternResult = benchmark.benchmarkPatternMatching(10);

        // Output results
        System.out.println("Performance Results:");
        System.out.printf("  Search: %.3f ms/op%n", searchResult.averageTimeMs());
        System.out.printf("  Parallel: %.3f ms/op%n", parallelResult.averageTimeMs());
        System.out.printf("  Pattern Matching: %.3f ms/op%n", patternResult.averageTimeMs());

        // Quick validation
        if (searchResult.averageTimeMs() > 100.0) {
            throw new RuntimeException("Search performance degraded: " + searchResult.averageTimeMs() + " ms");
        }

        System.out.println("All performance benchmarks passed ✅");
    }
}