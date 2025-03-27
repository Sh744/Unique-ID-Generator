package main.java;

public class SnowFlakeIdGenerator {
    //following a basic ID structure
    private static final long TIMESTAMP_BITS = 41L;
    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    //Epoch-January 1, 2024 Midnight
    private static final long CUSTOM_EPOCH = 1704067200000L;

    //Max values for bit allocation
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) -1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) -1;

    //Bit Shift Calculations
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    //instance fields
    private final long machineId;
    private final long sequence = 0L;
    private long lastTimestamp = -1L;

    //constructor for machineId
    public SnowFlakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(
                    String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        this.machineId = machineId;
    }
}
