package uk.co.caprica.vlcj.player.base;

import java.util.HashMap;
import java.util.Map;

public enum FrameStatus {
    SUCCESS(0),
    PAUSED(-11), // First call
    ERROR(-16),
    NOT_SUPPORTED(-95),
    INVALID(-22);

    private static final Map<Integer, FrameStatus> INT_MAP = new HashMap<Integer, FrameStatus>();

    static {
        for (FrameStatus status : FrameStatus.values()) {
            INT_MAP.put(status.intValue, status);
        }
    }

    /**
     * Get an enumerated value for a native value.
     *
     * @param intValue native value
     * @return enumerated value
     */
    public static FrameStatus frameStatus(int intValue) {
        return INT_MAP.get(intValue);
    }

    /**
     * Native value.
     */
    private final int intValue;

    /**
     * Create an enumerated value.
     *
     * @param intValue native value
     */
    FrameStatus(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Get the native value.
     *
     * @return value
     */
    public int intValue() {
        return intValue;
    }

}
