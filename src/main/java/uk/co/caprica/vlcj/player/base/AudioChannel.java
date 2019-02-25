/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of audio channels.
 * <p>
 * Note that {@link #UNSET}, {@link #HEADPHONES}, and {@link #MONO} do <em>not</em> appear in
 * <code>libvlc_audio_output_channel_t</code> although from reading the native source in <code>vlc_aout.h</code> they
 * may work.
 */
public enum AudioChannel {

    ERROR     (-1),
    UNSET     ( 0),
    STEREO    ( 1),
    RSTEREO   ( 2),
    LEFT      ( 3),
    RIGHT     ( 4),
    DOLBYS    ( 5),
    HEADPHONES( 6),
    MONO      ( 7);

    private static final Map<Integer, AudioChannel> INT_MAP = new HashMap<Integer, AudioChannel>();

    static {
        for (AudioChannel value : AudioChannel.values()) {
            INT_MAP.put(value.intValue, value);
        }
    }

    public static AudioChannel audioChannel(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    AudioChannel(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
