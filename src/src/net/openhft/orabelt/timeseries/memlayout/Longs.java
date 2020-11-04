package net.openhft.orabelt.timeseries.memlayout;

/**
 * Created by peter on 20/02/16.
 * modified 01/11/20
 */
public class Longs {

    public static byte toInt8(long x) throws IllegalArgumentException {
        if ((byte) x == x)
            return (byte) x;
        throw new IllegalArgumentException("Byte " + x + " out of range");
    }

    public static short toInt16(long x) throws IllegalArgumentException {
        if ((short) x == x)
            return (short) x;
        throw new IllegalArgumentException("Short " + x + " out of range");
    }

    public static int toInt32(long x) throws IllegalArgumentException {
        if ((int) x == x)
            return (int) x;
        throw new IllegalArgumentException("Int " + x + " out of range");
    }

    public static char toUInt8(long x) throws IllegalArgumentException {
        if ((x & 0xFF) == x)
            return (char) x;
        throw new IllegalArgumentException("Unsigned Byte " + x + " out of range");
    }

    public static int toUInt16(long x) throws IllegalArgumentException {
        if ((x & 0xFFFF) == x)
            return (int) x;
        throw new IllegalArgumentException("Unsigned Short " + x + " out of range");
    }

    public static int toUInt31(long x) throws IllegalArgumentException {
        if ((x & 0x7FFFFFFFL) == x)
            return (int) x;
        throw new IllegalArgumentException("Unsigned Int 31-bit " + x + " out of range");
    }

    public static long toUInt32(long x) throws IllegalArgumentException {
        if ((x & 0xFFFFFFFFL) == x)
            return x;
        throw new IllegalArgumentException("Unsigned Int " + x + " out of range");
    }

}
