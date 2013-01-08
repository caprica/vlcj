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

import com.sun.jna.Callback;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Callback prototype to setup the audio playback.
 */
public interface libvlc_audio_setup_cb extends Callback {

    /**
     * Callback prototype to setup the audio playback.
     * <p>
     * This is called when the media player needs to create a new audio output.
     *
     * @param data pointer to the data pointer passed to libvlc_audio_set_callbacks()
     * @param format 4 bytes sample format
     * @param rate sample rate
     * @param channels channels count
     * @return 0 on success, anything else to skip audio playback
     */
    int setup(PointerByReference data, String format, IntByReference rate, IntByReference channels);
}
