package net.openhft.orabelt.timeseries.memlayout.carrier;

/**
 * Created 01/11/20
 */
public class DoubleArrayByteStoreVolatile extends DoubleArrayByteStore {

    private volatile double[] array;

    public DoubleArrayByteStoreVolatile() {
        allocArray(0);
    }

    protected void allocArray(long capacity) { array = new double[Math.toIntExact(capacity)]; }

    //TODO
}

