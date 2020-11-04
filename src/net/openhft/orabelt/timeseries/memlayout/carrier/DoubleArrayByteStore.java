package net.openhft.orabelt.timeseries.memlayout.carrier;

import net.openhft.orabelt.timeseries.BytesDoubleLookup;
import net.openhft.orabelt.timeseries.memlayout.BytesStore;

import java.io.Closeable;
import java.util.Arrays;

/**
 * Created by orabelt 01/11/20
 * Add a scalars to the heap doubles array.
 * Data alignment 64-bit.
 */
public class DoubleArrayByteStore implements BytesDoubleLookup, BytesStore, Closeable {
    private double[] array;

    public DoubleArrayByteStore() {
        allocArray(0);
    }

    protected void allocArray(long capacity) {
        array = new double[Math.toIntExact(capacity)];
    }

    @Override public double get(BytesStore bytes, long index) { return array[(int)index]; }
    @Override public void set(BytesStore bytes, long index, double value) { array[(int)index] = value; }
    @Override public long sizeFor(long capacity) { return capacity; }

    public byte   readByte(long offset)         { return (byte)array[(int) offset]; }
    public char   readUnsignedByte(long offset) { return (char)array[(int) offset]; }
    public short  readShort(long offset)        { return (short)array[(int) offset >> 1]; }
    public int    readInt(long offset)          { return (int)array[(int) offset >> 2]; }
    public long   readLong(long offset)         { return (long)array[(int) offset >> 3]; }
    public float  readFloat(long offset)        { return (float)array[(int) offset >> 2]; }
    public double readDouble(long offset)       { return (double)array[(int) offset >> 3]; }

    public void writeDouble(long offset, double value)  { array[(int) offset << 3] = (double)value; }
    public void writeFloat(long offset, float value)    { array[(int) offset << 2] = (double)value; }
    public void writeShort(long offset, short value)    { array[(int) offset << 1] = (double)value; }
    public void writeInt(long offset, int value)        { array[(int) offset << 2] = (double)value; }
    public void writeLong(long offset, long value)      { array[(int) offset << 3] = (double)value; }
    public void writeByte(long offset, byte value)      { array[(int) offset] = (double)value; }
    public void writeUnsignedByte(long offset, char c)  { array[(int) offset] = (double)c; }

    public long getAllocatedBytes() { return array.length * Long.BYTES; }

    @Override public boolean ensureCapacity(long capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("capacity less than 0");
        if (array.length >= capacity )
            return true;

        array = Arrays.copyOf(array, Math.toIntExact(capacity));

        return true;
    }

    @Override public void close() {
        array = null;
    }
}
