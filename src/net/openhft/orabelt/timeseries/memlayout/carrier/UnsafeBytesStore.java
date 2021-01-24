package net.openhft.orabelt.timeseries.memlayout.carrier;

import net.openhft.orabelt.timeseries.memlayout.BytesStore;
import sun.misc.Unsafe;

import java.io.Closeable;
import java.lang.reflect.Field;

/**
 * Created by orabelt 01/11/20
 * Classic unsafe API
 * Doesn't align.
 */
public class UnsafeBytesStore implements BytesStore, Closeable {
    static final Unsafe UU;

    long base = 0;
    long capacity = 0;

    public UnsafeBytesStore() {
    }

    protected void unsafeAlloc(long capacity) {
        base = UU.allocateMemory(capacity);
        if (base == 0L)
            throw new OutOfMemoryError("Not enough free native memory, capacity attempted: " + capacity / 1024L + " KiB");
        this.capacity = capacity;
    }

    public byte readByte(long offset)           { return UU.getByte(base + offset); }
    public char readUnsignedByte(long offset)   { return UU.getChar(base + offset); }
    public short readShort(long offset)         { return UU.getShort(base + offset); }
    public int readInt(long offset)             { return UU.getInt(base + offset); }
    public long readLong(long offset)           { return UU.getLong(base + offset); }
    public float readFloat(long offset)         { return UU.getFloat(base + offset); }
    public double readDouble(long offset)       { return UU.getDouble(base + offset); }

    public void writeDouble(long offset, double value)  { UU.putDouble(base + offset, value); }
    public void writeFloat(long offset, float value)    { UU.putFloat(base + offset, value); }
    public void writeShort(long offset, short value)    { UU.putShort(base + offset, value); }
    public void writeInt(long offset, int value)        { UU.putInt(base + offset, value); }
    public void writeLong(long offset, long value)      { UU.putLong(base + offset, value); }
    public void writeByte(long offset, byte value)      { UU.putByte(base + offset, value); }
    public void writeUnsignedByte(long offset, char c)  { UU.putChar(base + offset, c); }

    public long getAllocatedBytes() { return capacity; }

    @Override public boolean ensureCapacity(long capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("capacity less than 0");
        if (this.capacity >= capacity )
            return true;

        final long fromAddress = this.base;
        final long fromSize = this.capacity;

        try {
            unsafeAlloc(capacity);
            if (fromSize > 0) {
                UnsafeBytesStore.UU.copyMemory(fromAddress, base, fromSize);
                if (fromAddress != 0L)
                    UU.freeMemory(fromAddress);
            }

            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }

    @Override public void close() {
        if (base != 0L) {
            UU.freeMemory(base);
        }
    }

    static { // -XaddExports:java.base/jdk.internal.misc=ALL-UNNAMED
        try {
            Class<?> c = Class.forName("sun.misc.Unsafe", false, String.class.getClassLoader());
            Field theUnsafe = c.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UU = (Unsafe) theUnsafe.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
