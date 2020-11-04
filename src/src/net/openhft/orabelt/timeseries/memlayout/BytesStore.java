package net.openhft.orabelt.timeseries.memlayout;

/**
 * Created 01/11/20
 */
public interface BytesStore {

    public long getAllocatedBytes();

    public boolean ensureCapacity(long bytes);

    public double readDouble(long offset);

    public void writeDouble(long offset, double value);

    public float readFloat(long offset);

    public void writeFloat(long offset, float value);

    public short readShort(long offset);

    public void writeShort(long offset, short i);

    public int readInt(long offset);

    public void writeInt(long offset, int i);

    public long readLong(long offset);

    public void writeLong(long offset, long value);

    public byte readByte(long offset);

    public void writeByte(long offset, byte b);

    // as unsigned byte
    public void writeUnsignedByte(long offset, char c);

    public char readUnsignedByte(long offset);



}

