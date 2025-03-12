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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_equalizer_t;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.player.base.callback.AudioCallback;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_equalizer_get_band_count;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_equalizer_new;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_equalizer_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_equalizer_set_amp_at_index;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_equalizer_set_preamp;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_get_delay;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_get_mixmode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_get_mute;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_get_stereomode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_get_volume;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_device_enum;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_device_get;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_device_list_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_device_set;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_output_set;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_set_delay;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_set_mixmode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_set_mute;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_set_stereomode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_set_volume;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_audio_toggle_mute;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_equalizer;

/**
 * Behaviour pertaining to media player audio.
 */
public final class AudioApi extends BaseApi implements EqualizerListener {

    /**
     * Audio callbacks component.
     */
    private final AudioCallbacks audioCallbacks;

    /**
     * Audio equalizer.
     *
     * May be <code>null</code>.
     */
    private Equalizer equalizer;

    /**
     * Native audio equalizer instance.
     */
    private libvlc_equalizer_t equalizerInstance;

    AudioApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
        audioCallbacks = new AudioCallbacks(mediaPlayer);
    }

    /**
     * Set the desired audio output.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The output name comes from {@link uk.co.caprica.vlcj.factory.AudioApi#audioOutputs()}.
     *
     * @param output name of the desired audio output
     * @return <code>true</code> if the output was successfully set, otherwise <code>false</code>
     */
    public boolean setOutput(String output) {
        return 0 == libvlc_audio_output_set(mediaPlayerInstance, output);
    }

    /**
     * Get the identifier of the current audio output device, if available.
     * <p>
     * To return a useful value, an audio output must be active (i.e. the media must be playing).
     *
     * @return identifier of the current audio output device, or <code>null</code> if not available
     */
    public String outputDevice() {
        return NativeString.copyAndFreeNativeString(libvlc_audio_output_device_get(mediaPlayerInstance));
    }

    /**
     * Set the desired audio output device.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The device id comes from the {@link AudioDevice#getDeviceId()} returned by
     * {@link uk.co.caprica.vlcj.factory.AudioApi#audioOutputs()}.
     *
     * @param outputDeviceId id of the desired audio output device
     */
    public void setOutputDevice(String outputDeviceId) {
        libvlc_audio_output_device_set(mediaPlayerInstance, outputDeviceId);
    }

    /**
     * Get the available audio devices for the media player audio output.
     *
     * @return list of audio devices, may be empty but not <code>null</code>
     */
    public List<AudioDevice> outputDevices() {
        List<AudioDevice> result = new ArrayList<AudioDevice>();
        libvlc_audio_output_device_t audioDevices = libvlc_audio_output_device_enum(mediaPlayerInstance);
        if (audioDevices != null) {
            libvlc_audio_output_device_t audioDevice = audioDevices;
            while (audioDevice != null) {
                String device = NativeString.copyNativeString(audioDevice.psz_device);
                String description = NativeString.copyNativeString(audioDevice.psz_description);
                result.add(new AudioDevice(device, description));
                audioDevice = audioDevice.p_next;
            }
            libvlc_audio_output_device_list_release(audioDevices.getPointer());
        }
        return result;
    }

    /**
     * Toggle volume mute.
     *
     * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
     */
    public boolean mute() {
        libvlc_audio_toggle_mute(mediaPlayerInstance);
        return isMute();
    }

    /**
     * Mute or un-mute the volume.
     *
     * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
     */
    public void setMute(boolean mute) {
        libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
    }

    /**
     * Test whether or not the volume is currently muted.
     *
     * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
     */
    public boolean isMute() {
        return libvlc_audio_get_mute(mediaPlayerInstance) != 0;
    }

    /**
     * Get the current volume.
     *
     * @return volume, a percentage of full volume in the range 0 to 200
     */
    public int volume() {
        return libvlc_audio_get_volume(mediaPlayerInstance);
    }

    /**
     * Set the volume.
     * <p>
     * The volume is actually a percentage of full volume, setting a volume over
     * 100 may cause audible distortion.
     *
     * @param volume volume, a percentage of full volume in the range 0 to 200
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setVolume(int volume) {
        return libvlc_audio_set_volume(mediaPlayerInstance, volume) == 0;
    }

    /**
     * Get the current audio stereo mode.
     * <p>
     * For stereo mode values see {@link AudioStereoMode}.
     *
     * @return audio channel
     */
    public AudioStereoMode stereoMode() {
        return AudioStereoMode.audioStereoMode(libvlc_audio_get_stereomode(mediaPlayerInstance));
    }

    /**
     * Set the audio stereo mode.
     * <p>
     * For channel values see {@link AudioStereoMode}.
     *
     * @param stereoMode channel
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setStereoMode(AudioStereoMode stereoMode) {
        return libvlc_audio_set_stereomode(mediaPlayerInstance, stereoMode.intValue()) == 0;
    }

    /**
     * Get the current audio mix mode.
     * <p>
     * For mix mode values see {@link AudioMixMode}.
     *
     * @return audio mix mode
     */
    public AudioMixMode getMixMode() {
        return AudioMixMode.audioMixMode(libvlc_audio_get_mixmode(mediaPlayerInstance));
    }

    /**
     * Set the audio mix mode.
     * <p>
     * For mix mode values see {@link AudioMixMode}.
     *
     * @param mixMode audio mix mode
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setMixMode(AudioMixMode mixMode) {
        return libvlc_audio_set_mixmode(mediaPlayerInstance, mixMode.intValue()) == 0;
    }

    /**
     * Get the audio delay.
     *
     * @return audio delay, in microseconds
     */
    public long delay() {
        return libvlc_audio_get_delay(mediaPlayerInstance);
    }

    /**
     * Set the audio delay.
     * <p>
     * The audio delay is set for the current item only and will be reset to zero each time the
     * media changes.
     *
     * @param delay desired audio delay, in microseconds
     */
    public void setDelay(long delay) {
        libvlc_audio_set_delay(mediaPlayerInstance, delay);
    }

    /**
     * Get the current audio equalizer.
     *
     * @return equalizer, or <code>null</code> if there is no active equalizer
     */
    public Equalizer equalizer() {
        return equalizer;
    }

    /**
     * Set the audio equalizer.
     *
     * @param equalizer equalizer, or <code>null</code> to disable the audio equalizer
     */
    public void setEqualizer(Equalizer equalizer) {
        if (this.equalizer != null) {
            this.equalizer.removeEqualizerListener(this);
            libvlc_audio_equalizer_release(equalizerInstance);
            equalizerInstance = null;
        }
        this.equalizer = equalizer;
        if (this.equalizer != null) {
            equalizerInstance = libvlc_audio_equalizer_new();
            this.equalizer.addEqualizerListener(this);
        }
        applyEqualizer();
    }

    @Override
    public final void equalizerChanged(Equalizer equalizer) {
        applyEqualizer();
    }

    /**
     * Apply the audio equalizer settings to the native media player.
     */
    private void applyEqualizer() {
        if (equalizerInstance != null) {
            libvlc_audio_equalizer_set_preamp(equalizerInstance, equalizer.preamp());
            for(int i = 0; i < libvlc_audio_equalizer_get_band_count(); i ++ ) {
                libvlc_audio_equalizer_set_amp_at_index(equalizerInstance, equalizer.amp(i), i);
            }
            libvlc_media_player_set_equalizer(mediaPlayerInstance, equalizerInstance);
        }
        else {
            libvlc_media_player_set_equalizer(mediaPlayerInstance, null);
        }
    }

    /**
     * Enable audio callbacks and set the component used to process the audio samples.
     * <p>
     * The callback will not manage audio volume.
     * <p>
     * @see #callback(String, int, int, AudioCallback, boolean)
     *
     * @param format audio buffer format
     * @param rate audio buffer bit-rate
     * @param channels number of audio channels
     * @param audioCallback component that will process the audio samples
     */
    public void callback(String format, int rate, int channels, AudioCallback audioCallback) {
        this.callback(format, rate, channels, audioCallback, false);
    }

    /**
     * Enable audio callbacks and set the component used to process the audio samples.
     * <p>
     * Supported audio buffer formats (as stated in VLC include-file libvlc_media_player.h) are:
     * <ul>
     *     <li>S16M - signed 16-bit PCM</li>
     *     <li>S32M - signed 32-bit PCM</li>
     *     <li>FL32 - single-precision IEEE 754</li>
     * </ul>
     *
     * @param format audio buffer format
     * @param rate audio buffer bit-rate
     * @param channels number of audio channels
     * @param audioCallback component that will process the audio samples
     * @param manageVolume <code>true</code> if the callback will manage audio volume; <code>false</code> if not
     */
    public void callback(String format, int rate, int channels, AudioCallback audioCallback, boolean manageVolume) {
        audioCallbacks.callback(format, rate, channels, audioCallback, manageVolume);
    }

    @Override
    protected void release() {
        setEqualizer(null);
    }
}
