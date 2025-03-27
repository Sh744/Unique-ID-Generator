package main.java;
public class SnowFlakeIdGenerator {

    // Constants for bit allocation
    private static final long TIMESTAMP_BITS = 41L;
    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    // Custom epoch (January 1, 2024 Midnight UTC) - to maximize timestamp range
    private static final long CUSTOM_EPOCH = 1704067200000L;

    // Maximum values (calculated from bit allocation)
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // Bit shift calculations
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    // Instance fields
    private final long machineId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowFlakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(
                    String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        this.machineId = machineId;
    }

    protected long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    private long waitForNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    // Utility methods to extract components from ID

    public long getTimestampFromId(long id) {
        return ((id >> TIMESTAMP_SHIFT) + CUSTOM_EPOCH);
    }

    public static long getMachineIdFromId(long id) {
        return (id >> MACHINE_ID_SHIFT) & MAX_MACHINE_ID;
    }

    public static long getSequenceFromId(long id) {
        return id & MAX_SEQUENCE;
    }

    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();

        // Handle clock moving backwards
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate ID for %d milliseconds",
                            lastTimestamp - currentTimestamp));
        }

        // If we're still in the same millisecond as the last ID generation
        if (currentTimestamp == lastTimestamp) {
            // Increment sequence for this millisecond
            sequence = (sequence + 1) & MAX_SEQUENCE;

            // If sequence overflows (we've generated 4096 IDs in this millisecond)
            if (sequence == 0) {
                // Wait until next millisecond
                currentTimestamp = waitForNextMillis(currentTimestamp);
            }
        } else {
            // We're in a new millisecond, reset sequence
            sequence = 0;
        }

        // Save the timestamp for next check
        lastTimestamp = currentTimestamp;

        // Combine all parts to generate the ID
        return ((currentTimestamp - CUSTOM_EPOCH) << TIMESTAMP_SHIFT) |
                (machineId << MACHINE_ID_SHIFT) |
                sequence;
    }

}
