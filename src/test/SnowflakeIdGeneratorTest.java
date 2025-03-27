package test;
import main.java.SnowFlakeIdGenerator;

/**
 * Test class for SnowflakeIdGenerator
 */
public class SnowflakeIdGeneratorTest {

    public static void main(String[] args) {
        System.out.println("SNOWFLAKE ID GENERATOR EDGE CASE TESTSadde");

        // Test 1: Clock moves backward
        testClockMovingBackwards();

        // Test 2: Sequence overflows (more than 4095 IDs in 1ms)
        testSequenceOverflow();

        // Test 3: Multiple nodes generating at the same time
        testMultipleNodes();

        // Test 4: High concurrency (multi-threaded generation)
        testHighConcurrency();

        // Test 5: Timestamp reaches max (simulate by creating custom generator)
        testTimestampOverflow();

        // Test 6: Node ID out of range
        testNodeIdOutOfRange();

        System.out.println("\nAll edge case tests completed successfully!");
    }
}