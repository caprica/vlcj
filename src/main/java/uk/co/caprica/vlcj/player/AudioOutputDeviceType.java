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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

/**
 * Enumeration of audio output device types.
 */
public enum AudioOutputDeviceType {

    AUDIO_ERROR(-1),
    AUDIO_MONO(1),
    AUDIO_STEREO(2),
    AUDIO_2F2R(4),
    AUDIO_3F2R(5),
    AUDIO_5_1(6),
    AUDIO_6_1(7),
    AUDIO_7_1(8),
    AUDIO_SPDIF(10);

    private final int intValue;

    private AudioOutputDeviceType(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    /**
     * Get an enumerated value for a native value.
     *
     * @param intValue native value
     * @return enumerated value, or <code>null</code> if the native value is not recognised
     */
    public static AudioOutputDeviceType valueOf(int intValue) {
        for(AudioOutputDeviceType val : AudioOutputDeviceType.values()) {
            if(val.intValue == intValue) {
                return val;
            }
        }
        return null;
    }
}
