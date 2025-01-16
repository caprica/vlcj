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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_list_get;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_list_release;

/**
 * Behaviour pertaining to audio.
 */
public final class AudioApi extends BaseApi {

    AudioApi(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Get the available audio outputs.
     * <p>
     * Each audio output has zero or more audio devices, each device having it's own unique
     * identifier that can be used on a media player to set the select the required output device.
     *
     * @return collection of audio outputs
     */
    public List<AudioOutput> audioOutputs() {
        List<AudioOutput> result = new ArrayList<AudioOutput>();
        libvlc_audio_output_t audioOutputs = libvlc_audio_output_list_get(libvlcInstance);
        if (audioOutputs != null) {
            libvlc_audio_output_t audioOutput = audioOutputs;
            while (audioOutput != null) {
                String name = NativeString.copyNativeString(audioOutput.psz_name);
                String description = NativeString.copyNativeString(audioOutput.psz_description);
                result.add(new AudioOutput(name, description));
                audioOutput = audioOutput.p_next;
            }
            libvlc_audio_output_list_release(audioOutputs.getPointer());
        }
        return result;
    }
}
