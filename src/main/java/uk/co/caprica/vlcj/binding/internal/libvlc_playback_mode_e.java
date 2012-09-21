package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of media list player playback modes.
 */
public enum libvlc_playback_mode_e {

    libvlc_playback_mode_default (0),
    libvlc_playback_mode_loop    (1),
    libvlc_playback_mode_repeat  (2);

    private static final Map<Integer, libvlc_playback_mode_e> INT_MAP = new HashMap<Integer, libvlc_playback_mode_e>();

    static {
        for(libvlc_playback_mode_e event : libvlc_playback_mode_e.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static libvlc_playback_mode_e event(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_playback_mode_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
