package net.openhft.orabelt.timeseries.memlayout.carrier;

/**
 * Created by orabelt 01/11/20
 * Classic unsafe API
 * Doesn't align.
 */
public class UnsafeBytesStoreVolatile extends UnsafeBytesStore  {

    public UnsafeBytesStoreVolatile() { super(); }

    public byte readByte(long offset)           { return UU.getByteVolatile(null,base + offset); }
    public char readUnsignedByte(long offset)   { return UU.getCharVolatile(null,base + offset); }
    public short readShort(long offset)         { return UU.getShortVolatile(null,base + offset); }
    public int readInt(long offset)             { return UU.getIntVolatile(null,base + offset); }
    public long readLong(long offset)           { return UU.getLongVolatile(null,base + offset); }
    public float readFloat(long offset)         { return UU.getFloatVolatile(null,base + offset); }
    public double readDouble(long offset)       { return UU.getDoubleVolatile(null,base + offset); }

    public void writeDouble(long offset, double value)  { UU.putDoubleVolatile(null,base + offset, value); }
    public void writeFloat(long offset, float value)    { UU.putFloatVolatile(null,base + offset, value); }
    public void writeShort(long offset, short value)    { UU.putShortVolatile(null,base + offset, value); }
    public void writeInt(long offset, int value)        { UU.putIntVolatile(null,base + offset, value); }
    public void writeLong(long offset, long value)      { UU.putLongVolatile(null,base + offset, value); }
    public void writeByte(long offset, byte value)      { UU.putByteVolatile(null,base + offset, value); }
    public void writeUnsignedByte(long offset, char c)  { UU.putCharVolatile(null,base + offset, c); }

//    public byte readByte(long offset)           { return UU.getByte(base + offset); }
//    public char readUnsignedByte(long offset)   { return UU.getChar(base + offset); }
//    public short readShort(long offset)         { return UU.getShort(base + offset); }
//    public int readInt(long offset)             { return UU.getInt(base + offset); }
//    public long readLong(long offset)           { return UU.getLong(base + offset); }
//    public float readFloat(long offset)         { return UU.getFloat(base + offset); }
//    public double readDouble(long offset)       { return UU.getDouble(base + offset); }
//
//    public void writeDouble(long offset, double value)  { UU.putDouble(base + offset, value); }
//    public void writeFloat(long offset, float value)    { UU.putFloat(base + offset, value); }
//    public void writeShort(long offset, short value)    { UU.putShort(base + offset, value); }
//    public void writeInt(long offset, int value)        { UU.putInt(base + offset, value); }
//    public void writeLong(long offset, long value)      { UU.putLong(base + offset, value); }
//    public void writeByte(long offset, byte value)      { UU.putByte(base + offset, value); }
//    public void writeUnsignedByte(long offset, char c)  { UU.putChar(base + offset, c); }
}
