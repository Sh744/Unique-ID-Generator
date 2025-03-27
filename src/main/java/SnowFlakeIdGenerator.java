package main.java;
public class SnowFlakeIdGenerator {

    private static final long timeStampBits = 41L;
    private static final long machineIDBits = 10L;
    private static final long sequenceBits = 12L;
    private static final long epoch = 1735689600000L;
    private static final long maxMachineId = (1L << machineIDBits) - 1;
    private static final long maxSequence = (1L << sequenceBits) - 1;
    private static final long machineShift = sequenceBits;
    private static final long timestampShift = sequenceBits + machineIDBits;
    private final long machineId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowFlakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > maxMachineId) {
            System.out.println("Machine ID must be between 0 and " + maxMachineId);
        }
        this.machineId = machineId;
    }

    public long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    private long waitForNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();

        // Clock moving backwards
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate ID for %d milliseconds",
                            lastTimestamp - currentTimestamp));
        }

        // increments sequence number if machine generates more than one id at the same time
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;

            // If sequence exceeds 4096 IDs
            if (sequence == 0) {
                currentTimestamp = waitForNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        // ID Generation
        return ((currentTimestamp - epoch) << timestampShift) |
                (machineId << machineShift) |
                sequence;
    }

}
