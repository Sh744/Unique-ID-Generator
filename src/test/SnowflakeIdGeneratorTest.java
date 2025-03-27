package test;
import main.java.BackwardClockGenerator;
import main.java.SnowFlakeIdGenerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static java.lang.Thread.sleep;

/**
 * Test class for SnowflakeIdGenerator
 */
public class SnowflakeIdGeneratorTest {

    public static void main(String[] args) {
        System.out.println("SNOWFLAKE ID GENERATOR EDGE CASE TESTS");

        basicTest();

        // Test uniqueness
        uniquenessTest();

        // Test to check if Clock moves backward
        testClockMovingBackwards();

        //test for time ordering(Checks if IDs are sortable by time)
        timeOrderingTest();

        testConcurrencyGeneration();

        System.out.println("\nAll edge case tests completed successfully!");
    }
    private static void basicTest() {
        System.out.println("Running basic test...");

        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(1);
        long id = generator.nextId();

        System.out.println("Generated ID: " + id);
        System.out.println("In binary: " + Long.toBinaryString(id));

        // Verify ID is positive and fits in a 64-bit long
        assert id > 0 : "ID should be positive";
        assert Long.toBinaryString(id).length() <= 63 : "ID should fit in 63 bits (excluding sign bit)";

        System.out.println("Basic test passed!");
    }

    private static void uniquenessTest() {
        System.out.println("\nRunning uniqueness test...");

        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(2);
        Set<Long> ids = new HashSet<>();

        // Generate 10,000 IDs and ensure they're all unique
        int count = 10000;
        System.out.println("Generating " + count + " IDs...");

        for (int i = 0; i < count; i++) {
            long id = generator.nextId();
            if (ids.contains(id)) {
                throw new AssertionError("Duplicate ID generated: " + id);
            }
            ids.add(id);

            if (i % 1000 == 0 && i > 0) {
                System.out.println(i + " unique IDs generated so far");
            }
        }

        System.out.println("Generated " + count + " unique IDs successfully");
        System.out.println("Uniqueness test passed!");
    }
    private static void testClockMovingBackwards() {
        System.out.println("\nRunning Clock Moving Backwards test...");

        // Create a custom generator with a manipulated clock
        BackwardClockGenerator generator = new BackwardClockGenerator(30);

        // First call should work fine
        long id1 = generator.nextId();
        System.out.println("First ID generated successfully: " + id1);

        // Simulate clock moving backwards
        generator.simulateClockMovingBackward(50); // 50 ms backward

        // Second call should throw exception
        try {
            long id2 = generator.nextId();
            generator.fail("Should have thrown exception when clock moved backwards. Generated ID: " + id2);
        } catch (RuntimeException e) {
            System.out.println("Correctly threw exception when clock moved backwards: " + e.getMessage());
        }

        System.out.println("Clock backward test passed");
    }

    private static void timeOrderingTest() {
        System.out.println("\nRunning time ordering test...");
        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(15);
        long id1 = generator.nextId();
        long timeStamp1 = generator.getTimestampFromId(id1);
        System.out.println("First ID generated successfully: " + id1);

        //sleeps for 5 milliseconds
        try {
            Thread.sleep(5000); // sleeps for 5 seconds
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted!");
        }

        long id2 = generator.nextId();
        long timeStamp2 = generator.getTimestampFromId(id2);

        // Later ID should be greater than earlier ID
        assert id2 > id1 : "Later ID should be greater than earlier ID";

        // Later timestamp should be >= earlier timestamp
        assert timeStamp2 >= timeStamp1 : "Later timestamp should be >= earlier timestamp";

        System.out.println("✅ Time ordering test passed");
    }

    private static void testConcurrencyGeneration(){
        System.out.println("\nRunning concurrency generation test...");

        final int THREAD_COUNT = 5;
        final int IDS_PER_THREAD = 100;
        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(1);

        Set<Long> ids = Collections.synchronizedSet(new HashSet<>());

        Thread[] threads = new Thread[THREAD_COUNT];

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                try {
                    for (int i = 0; i < IDS_PER_THREAD; i++) {
                        long id = generator.nextId();
                        if (!ids.add(id)) {
                            System.err.println("Thread " + threadId + " generated duplicate ID: " + id);
                            return;
                        }
                    }
                    System.out.println("Thread " + threadId + " completed successfully");
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " error: " + e.getMessage());
                }
            });
            threads[t].start();
        }
        // Wait for all threads to finish
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }

        // Verify results
        int expectedCount = THREAD_COUNT * IDS_PER_THREAD;
        boolean success = ids.size() == expectedCount;

        System.out.println("\nTest result: " + (success ? "Success" : "Failed"));
        System.out.println("Expected " + expectedCount + " unique IDs");
        System.out.println("Actually generated " + ids.size() + " unique IDs");

    }

}