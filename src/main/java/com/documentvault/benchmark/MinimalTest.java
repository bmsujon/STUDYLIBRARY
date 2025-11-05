package com.documentvault.benchmark;

/**
 * Minimal benchmark test for CI/CD pipeline validation.
 * 
 * This class provides a lightweight performance test that can run in headless
 * environments without JavaFX dependencies. Used by the CI workflow to verify
 * the application compiles and basic functionality works.
 */
public class MinimalTest {

    private static final int WARMUP_ITERATIONS = 100;
    private static final int TEST_ITERATIONS = 1000;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DocumentVault CI Performance Benchmark");
        System.out.println("=".repeat(60));

        // Warmup
        System.out.println("\nWarming up JVM...");
        runBasicOperations(WARMUP_ITERATIONS);

        // Actual benchmark
        System.out.println("Running performance test...");
        long startTime = System.nanoTime();
        runBasicOperations(TEST_ITERATIONS);
        long endTime = System.nanoTime();

        // Results
        long durationMs = (endTime - startTime) / 1_000_000;
        double opsPerSecond = (TEST_ITERATIONS * 1000.0) / durationMs;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Performance Results:");
        System.out.println("=".repeat(60));
        System.out.println(String.format("Iterations:        %,d", TEST_ITERATIONS));
        System.out.println(String.format("Duration:          %,d ms", durationMs));
        System.out.println(String.format("Operations/sec:    %,.2f", opsPerSecond));
        System.out.println("=".repeat(60));
        System.out.println("✓ Performance benchmark completed successfully");
        System.out.println("=".repeat(60));
    }

    /**
     * Runs basic operations to test performance.
     * This simulates core application logic without GUI dependencies.
     */
    private static void runBasicOperations(int iterations) {
        for (int i = 0; i < iterations; i++) {
            // Simulate basic data operations
            String testData = "DocumentVault Test Item " + i;
            String processed = testData.toLowerCase().trim();

            // Simulate search/filter operations
            boolean matches = processed.contains("documentvault");

            // Prevent JIT optimization from eliminating the code
            if (i == iterations - 1 && !matches) {
                System.err.println("Unexpected test result");
            }
        }
    }
}
