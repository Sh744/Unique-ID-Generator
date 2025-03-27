package test;
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
}