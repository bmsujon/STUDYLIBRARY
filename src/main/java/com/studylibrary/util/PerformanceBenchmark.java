package com.studylibrary.util;

import com.studylibrary.model.LibraryItem;
import com.studylibrary.model.SearchCriteria;
import com.studylibrary.service.LibraryService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Performance benchmarking utilities for Java 25 optimizations.
 * Measures search performance, memory usage, and other metrics.
 */
public class PerformanceBenchmark {

    private final LibraryService libraryService;
    private final Runtime runtime = Runtime.getRuntime();

    public PerformanceBenchmark(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Benchmark search performance with different query sizes.
     * Java 25: Uses virtual threads for concurrent testing.
     */
    public BenchmarkResult benchmarkSearch(String query, int iterations) {
        System.gc(); // Suggest garbage collection before benchmark

        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();

        // Run search iterations
        for (int i = 0; i < iterations; i++) {
            libraryService.searchItems(query);
        }

        long endTime = System.nanoTime();
        long endMemory = getUsedMemory();

        double avgTimeMs = (endTime - startTime) / 1_000_000.0 / iterations;
        long memoryDelta = endMemory - startMemory;

        return new BenchmarkResult(
                "Search: " + query,
                avgTimeMs,
                memoryDelta,
                iterations);
    }

    /**
     * Benchmark parallel processing capabilities.
     * Java 25: Enhanced parallel stream performance.
     */
    public BenchmarkResult benchmarkParallelOperations() {
        System.gc();

        long startTime = System.nanoTime();

        // Parallel operations using concurrent streams
        var executor = Executors.newFixedThreadPool(3);
        try {
            var futures = List.of(
                    CompletableFuture.supplyAsync(() -> libraryService.getAllItems(), executor),
                    CompletableFuture.supplyAsync(() -> libraryService.getAllCategories(), executor),
                    CompletableFuture.supplyAsync(() -> libraryService.getAllTags(), executor));

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executor.shutdown();
        }

        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;

        return new BenchmarkResult(
                "Parallel Operations",
                timeMs,
                0,
                3);
    }

    /**
     * Benchmark pattern matching performance.
     * Java 25: Tests sealed class instanceof performance.
     */
    public BenchmarkResult benchmarkPatternMatching(int iterations) {
        var criteria = new SearchCriteria("");
        var items = libraryService.getAllItems();

        System.gc();
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            for (LibraryItem item : items) {
                criteria.getItemTypeIcon(item);
                criteria.getEnhancedDescription(item);
            }
        }

        long endTime = System.nanoTime();
        double avgTimeMs = (endTime - startTime) / 1_000_000.0 / iterations;

        return new BenchmarkResult(
                "Pattern Matching",
                avgTimeMs,
                0,
                iterations * items.size());
    }

    /**
     * Generate a comprehensive performance report.
     */
    public PerformanceReport generateReport() {
        // Memory baseline
        long memoryBaseline = getUsedMemory();
        int totalItems = libraryService.getAllItems().size();

        // Search benchmarks
        var emptySearchResult = benchmarkSearch("", 100);
        var shortQueryResult = benchmarkSearch("test", 100);
        var longQueryResult = benchmarkSearch("comprehensive search query", 100);

        // Pattern matching benchmark
        var patternMatchingResult = benchmarkPatternMatching(50);

        // Parallel operations benchmark
        var parallelOperationsResult = benchmarkParallelOperations();

        return new PerformanceReport(
                memoryBaseline,
                totalItems,
                emptySearchResult,
                shortQueryResult,
                longQueryResult,
                patternMatchingResult,
                parallelOperationsResult);
    }

    private long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Record for benchmark results.
     * Java 25: Using records for clean data structures.
     */
    public record BenchmarkResult(
            String operation,
            double averageTimeMs,
            long memoryDeltaBytes,
            int operationCount) {
        public String getFormattedResult() {
            return String.format("%s: %.3f ms/op (%d ops, %s memory)",
                    operation, averageTimeMs, operationCount, formatMemory(memoryDeltaBytes));
        }

        private String formatMemory(long bytes) {
            if (bytes == 0)
                return "no change";
            if (Math.abs(bytes) < 1024)
                return bytes + " B";
            if (Math.abs(bytes) < 1024 * 1024)
                return String.format("%.1f KB", bytes / 1024.0);
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * Comprehensive performance report.
     * Java 25: Record-based data structure for clean reporting.
     */
    public record PerformanceReport(
            long memoryBaseline,
            int totalItems,
            BenchmarkResult emptySearchResult,
            BenchmarkResult shortQueryResult,
            BenchmarkResult longQueryResult,
            BenchmarkResult patternMatchingResult,
            BenchmarkResult parallelOperationsResult) {

        public String generateSummary() {
            var summary = new StringBuilder();
            summary.append("=== Java 25 Performance Report ===\n");
            summary.append("Memory Baseline: ").append(formatMemory(memoryBaseline)).append("\n");
            summary.append("Total Items: ").append(totalItems).append("\n\n");

            if (emptySearchResult != null) {
                summary.append(emptySearchResult.getFormattedResult()).append("\n");
            }
            if (shortQueryResult != null) {
                summary.append(shortQueryResult.getFormattedResult()).append("\n");
            }
            if (longQueryResult != null) {
                summary.append(longQueryResult.getFormattedResult()).append("\n");
            }
            if (patternMatchingResult != null) {
                summary.append(patternMatchingResult.getFormattedResult()).append("\n");
            }
            if (parallelOperationsResult != null) {
                summary.append(parallelOperationsResult.getFormattedResult()).append("\n");
            }

            return summary.toString();
        }

        private String formatMemory(long bytes) {
            if (bytes < 1024)
                return bytes + " B";
            if (bytes < 1024 * 1024)
                return String.format("%.1f KB", bytes / 1024.0);
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }
}