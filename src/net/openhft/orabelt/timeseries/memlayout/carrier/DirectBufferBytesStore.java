package net.openhft.orabelt.timeseries.memlayout.carrier;

import net.openhft.orabelt.timeseries.memlayout.BytesStore;
import net.openhft.orabelt.timeseries.memlayout.JvmEnv;
import sun.nio.ch.DirectBuffer;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by orabelt 01/11/20
 * Add a scalars to the off-heap byte buffer API.
 * Doesn't align.
 */
public class DirectBufferBytesStore implements BytesStore, Closeable {

    private ByteBuffer bb;

    public DirectBufferBytesStore() { bb = byteBufferAlloc(0); }

    protected ByteBuffer byteBufferAlloc(long capacity) {
        return ByteBuffer.allocateDirect(Math.toIntExact(capacity)).order(ByteOrder.nativeOrder());
    }

    public byte   readByte(long offset)         { return bb.get((int) offset); }
    public char   readUnsignedByte(long offset) { return (char) (bb.get((int) offset) & 0xFF); }
    public short  readShort(long offset)        { return bb.getShort((int) offset); }
    public int    readInt(long offset)          { return bb.getInt((int) offset); }
    public long   readLong(long offset)         { return bb.getLong((int) offset); }
    public float  readFloat(long offset)        { return bb.getFloat((int) offset); }
    public double readDouble(long offset)       { return bb.getDouble((int) offset); }

    public void writeDouble(long offset, double value)  { bb.putDouble((int) offset, value); }
    public void writeFloat(long offset, float value)    { bb.putFloat((int) offset, value); }
    public void writeShort(long offset, short value)    { bb.putShort((int) offset, value); }
    public void writeInt(long offset, int value)        { bb.putInt((int) offset, value); }
    public void writeLong(long offset, long value)      { bb.putLong((int) offset, value); }
    public void writeByte(long offset, byte value)      { bb.put((int) offset, value); }
    public void writeUnsignedByte(long offset, char c)  { bb.put((int) offset, (byte)(c & 0xFF)); }

    public long getAllocatedBytes() { return bb.capacity(); }

    @Override public boolean ensureCapacity(long capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("capacity less than 0");
        if (bb.capacity() >= capacity )
            return true;

        try {
            ByteBuffer bbEx = byteBufferAlloc(capacity);

            if ( bb.capacity() > 0 ) {
                long fromAddress = ((DirectBuffer) bb).address();
                long address = ((DirectBuffer) bbEx).address();
                UnsafeBytesStore.UU.copyMemory(fromAddress, address, bb.capacity());
                //todo: clean fromAddress
            }

            bb = bbEx;
            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }

    @Override public void close() {
        if ( JvmEnv.OS.VERSION <= 8 ) {
            try {
                java.lang.reflect.Method CleanerMethod = bb.getClass().getDeclaredMethod("cleaner");
                CleanerMethod.setAccessible(true);
                Object cleaner = CleanerMethod.invoke(bb);
                if (cleaner == null) return;
                @SuppressWarnings("rawtypes")
                java.lang.reflect.Method CleanMethod = cleaner.getClass().getDeclaredMethod("clean");
                CleanMethod.setAccessible(true); // may be fails here
                CleanMethod.invoke(cleaner);
            } catch (Exception e) {
                throw new IllegalStateException("DirectBufferBytesStore cleanup error", e.getCause());
            } finally {
                bb = null;
            }
        } else {} // TODO
    }
}
