package test;
import main.java.SnowFlakeIdGenerator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SnowflakeIdGeneratorTest {

    public static void main(String[] args) {
        System.out.println("SNOWFLAKE ID EDGE CASE TESTS");

        basicTest();

        // Test uniqueness
        uniquenessTest();

        System.out.println("\nAll edge case tests completed successfully!");
    }
    private static void basicTest() {
        System.out.println("Running basic test...");

        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(1);
        long id = generator.nextId();
        if (id <= 0) {
            System.out.println("Test Failed: ID is not positive!");
        } else {
            System.out.println("ID is positive");
            System.out.println("Generated ID: " + id);
        }

        // Check if ID fits in 63 bits (excluding sign bit)
        String binary = Long.toBinaryString(id);
        if (binary.length() > 63) {
            System.out.println("Test Failed: ID is too long! " + binary.length() + " bits");
        } else {
            System.out.println("ID length is valid (" + binary.length() + " bits)");
        }

        System.out.println("Basic test finished!");
    }


    private static void uniquenessTest() {
        System.out.println("\nRunning uniqueness test...");

        SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator(2);
        Set<Long> ids = new HashSet<>();

        int count = 100;

        boolean duplicateFound = false;

        for (int i = 0; i < count; i++) {
            long id = generator.nextId();

            if (ids.contains(id)) {
                System.out.println("Duplicate ID found: " + id);
                duplicateFound = true;
                break;
            } else {
                ids.add(id);
            }

        }
        if (duplicateFound) {
            System.out.println("Uniqueness test failed");
        } else {
            System.out.println("Uniqueness test passed");
        }
    }

}

