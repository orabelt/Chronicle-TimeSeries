package net.openhft.orabelt.timeseries.memlayout.carrier;

/**
 * Created 01/11/20
 */
public class LongArrayByteStoreVolatile extends LongArrayByteStore {

    private volatile long[] array;

    public LongArrayByteStoreVolatile() { allocArray(0); }

    protected void allocArray(long capacity) { array = new long[Math.toIntExact(capacity)]; }

    //TODO
}
