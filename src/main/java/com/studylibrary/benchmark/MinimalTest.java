package com.studylibrary.benchmark;

/**
 * Ultra-simple CI test that avoids all dependencies
 */
public class MinimalTest {
    // Performance test constants
    private static final int PATTERN_MATCHING_ITERATIONS = 10000;
    private static final double MAX_PATTERN_MATCHING_TIME_MS = 0.01;
    
    // Volatile field to prevent JIT compiler optimization
    private static volatile String benchmarkResult;

    public static void main(String[] args) {
        System.out.println("🧪 Minimal CI Test");
        System.out.println("==================");

        try {
            // Test basic Java 25 features
            testSealedClassesSimulation();
            testRecordsSimulation();
            testPatternMatchingSimulation();

            System.out.println("✅ All minimal tests passed!");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testSealedClassesSimulation() {
        System.out.println("Testing sealed classes simulation...");

        // Simulate sealed class behavior with enum
        enum ItemType {
            NOTE, PDF, MEDIA, SNIPPET
        }

        var types = ItemType.values();
        if (types.length != 4) {
            throw new RuntimeException("Expected 4 types, got " + types.length);
        }

        System.out.println("  ✓ Sealed classes simulation working");
    }

    private static void testRecordsSimulation() {
        System.out.println("Testing records simulation...");

        // Simple record-like behavior
        record TestRecord(String name, int value) {
        }

        var record1 = new TestRecord("test", 42);
        var record2 = new TestRecord("test", 42);

        if (!record1.equals(record2)) {
            throw new RuntimeException("Records not equal");
        }

        if (!record1.name().equals("test") || record1.value() != 42) {
            throw new RuntimeException("Record accessors failed");
        }

        System.out.println("  ✓ Records simulation working");
    }

    private static void testPatternMatchingSimulation() {
        System.out.println("Testing pattern matching simulation...");

        // Simulate pattern matching performance
        long startTime = System.nanoTime();

        for (int i = 0; i < PATTERN_MATCHING_ITERATIONS; i++) {
            // Use volatile field to prevent JIT compiler optimization
            benchmarkResult = switch (i % 4) {
                case 0 -> "📝";
                case 1 -> "📄";
                case 2 -> "🎵";
                case 3 -> "💻";
                default -> "❓";
            };
        }

        long endTime = System.nanoTime();

        // Prevent dead code elimination by consuming the volatile field
        if (benchmarkResult == null || benchmarkResult.isEmpty()) {
            throw new RuntimeException("Unexpected empty result");
        }
        double avgTimeMs = (endTime - startTime) / 1_000_000.0 / PATTERN_MATCHING_ITERATIONS;

        System.out.printf("  ✓ Pattern matching average: %.6f ms%n", avgTimeMs);

        // Realistic threshold for switch statement performance (10 microseconds per operation)
        if (avgTimeMs > MAX_PATTERN_MATCHING_TIME_MS) {
            throw new RuntimeException("Pattern matching too slow: " + avgTimeMs + " ms (expected < " + MAX_PATTERN_MATCHING_TIME_MS + "ms)");
        }
    }
}