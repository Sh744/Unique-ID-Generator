package test;
import main.java.BackwardClockGenerator;
import main.java.SnowFlakeIdGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for SnowflakeIdGenerator
 */
public class SnowflakeIdGeneratorTest {

    public static void main(String[] args) {
        System.out.println("SNOWFLAKE ID GENERATOR EDGE CASE TESTS");

        basicTest();

        // Test uniqueness
        uniquenessTest();

        // Test 1: Clock moves backward
        testClockMovingBackwards();

        //Tests Clock moving Forward
        testClockMovingForward();

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
            System.out.println("✓ Correctly threw exception when clock moved backwards: " + e.getMessage());
        }

        System.out.println("Clock backward test passed");
    }

    private static void testClockMovingForward() {
        System.out.println("\nRunning Clock Moving Forward test...");

        // Create a custom generator with a manipulated clock
        BackwardClockGenerator generator = new BackwardClockGenerator(30);

        // Generate first ID
        long id1 = generator.nextId();
        System.out.println("First ID generated successfully: " + id1);

        // Simulate clock moving forward
        long forwardMs = 1000; // 1 second forward
        generator.simulateClockMovingForward(forwardMs);

        // Second call should work fine and have a greater timestamp
        try {
            long id2 = generator.nextId();
            System.out.println("Second ID generated successfully: " + id2);

            // Verify ID2 is greater than ID1
            assert id2 > id1 : "Later ID should be greater than earlier ID";

            // Extract timestamps and verify gap
            long ts1 = SnowFlakeIdGenerator.getTimestampFromId(id1);
            long ts2 = SnowFlakeIdGenerator.getTimestampFromId(id2);
            long actualGap = ts2 - ts1;

            System.out.println("Time gap: " + actualGap + "ms (expected ~" + forwardMs + "ms)");
            assert actualGap > 0 : "Timestamp gap should be positive";
            assert Math.abs(actualGap - forwardMs) < 100 :
                    "Timestamp gap should be approximately " + forwardMs + "ms";

        } catch (Exception e) {
            generator.fail("Should not throw exception when clock moves forward: " + e.getMessage());
        }

        System.out.println("Clock forward test passed");
    }
}