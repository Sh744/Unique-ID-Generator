package main.java;

public class BackwardClockGenerator extends SnowFlakeIdGenerator{
    private long simulatedCurrentTimestamp;
    public BackwardClockGenerator(long machineId) {
        super(machineId);
        this.simulatedCurrentTimestamp = System.currentTimeMillis();
    }
    //simulates clock moving backwards
    public void simulateClockMovingBackward(long milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Milliseconds must be positive");
        }
        this.simulatedCurrentTimestamp -= milliseconds;
    }

    //simulates clock moving forwards
    public void simulateClockMovingForward(long milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Milliseconds must be positive");
        }
        this.simulatedCurrentTimestamp += milliseconds;
    }
    public long getSimulatedCurrentTimestamp() {
        return simulatedCurrentTimestamp;
    }
    public void setSimulatedCurrentTimestamp(long timestamp) {
        this.simulatedCurrentTimestamp = timestamp;
    }

    //overrides and returns simulated current time stamp
    @Override
    protected long getCurrentTimestamp() {
        return simulatedCurrentTimestamp;
    }
}
