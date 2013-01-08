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

package uk.co.caprica.vlcj.binding.internal;

/**
 *
 */
public enum libvlc_audio_output_device_types_t {

    libvlc_AudioOutputDevice_Error (-1),
    libvlc_AudioOutputDevice_Mono  ( 1),
    libvlc_AudioOutputDevice_Stereo( 2),
    libvlc_AudioOutputDevice_2F2R  ( 4),
    libvlc_AudioOutputDevice_3F2R  ( 5),
    libvlc_AudioOutputDevice_5_1   ( 6),
    libvlc_AudioOutputDevice_6_1   ( 7),
    libvlc_AudioOutputDevice_7_1   ( 8),
    libvlc_AudioOutputDevice_SPDIF (10);

    private final int intValue;

    private libvlc_audio_output_device_types_t(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
