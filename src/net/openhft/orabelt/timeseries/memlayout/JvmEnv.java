package net.openhft.orabelt.timeseries.memlayout;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Created 01/11/20
 */
public enum JvmEnv {
    OS; // GraalVM statics compile (obtain the name and zone at runtime)
    public final String NAME = System.getProperty("os.name").toLowerCase();

    public final ZoneOffset TIME_ZONE = OffsetDateTime.now().getOffset();
    public final int OFFSET = TimeZone.getDefault().getRawOffset();
    public final boolean IS_DST = TimeZone.getDefault().useDaylightTime();

    public final boolean IS_LINUX = NAME.startsWith("linux");
    public final boolean IS_MAC = NAME.contains("mac");
    public final boolean IS_WIN = NAME.startsWith("win");
    public final boolean IS_WIN10 = NAME.equals("windows 10");

    public final int VERSION = getVersion();
    public final boolean IS_64BIT = is64Bit();

    /**
     * Java 8 or lower: 1.6.0_23, 1.7.0, 1.7.0_80, 1.8.0_211
     * Java 9 or higher: 9.0.1, 11.0.4, 12, 12.0.1
     * if(getVersion() < 6) {..}
     */
    int getVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
    }

    boolean is64Bit() {
        String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) {
            return "64".equals(model);
        } else {
            model = System.getProperty("java.vm.version");
            return model != null && model.contains("_64");
        }
    }

}
