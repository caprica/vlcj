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
public enum libvlc_audio_output_channel_t {

    libvlc_AudioChannel_Error  (-1),
    libvlc_AudioChannel_Stereo ( 1),
    libvlc_AudioChannel_RStereo( 2),
    libvlc_AudioChannel_Left   ( 3),
    libvlc_AudioChannel_Right  ( 4),
    libvlc_AudioChannel_Dolbys ( 5);

    private final int intValue;

    private libvlc_audio_output_channel_t(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
