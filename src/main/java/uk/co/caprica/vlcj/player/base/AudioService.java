package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_equalizer_t;
import uk.co.caprica.vlcj.player.*;

import java.util.ArrayList;
import java.util.List;

public final class AudioService extends BaseService implements EqualizerListener {

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

    AudioService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set the desired audio output.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The output name comes from {@link MediaPlayerFactory#getAudioOutputs()}.
     *
     * @param output name of the desired audio output
     * @return <code>true</code> if the output was successfully set, otherwise <code>false</code>
     */
    public boolean setAudioOutput(String output) {
        return 0 == libvlc.libvlc_audio_output_set(mediaPlayerInstance, output);
    }

    /**
     * Get the identifier of the current audio output device, if available.
     * <p>
     * To return a useful value, an audio output must be active (i.e. the media must be playing).
     *
     * @return identifier of the current audio output device, or <code>null</code> if not available
     */
    public String getAudioOutputDevice() {
        return NativeString.copyAndFreeNativeString(libvlc, libvlc.libvlc_audio_output_device_get(mediaPlayerInstance));
    }

    /**
     * Set the desired audio output device.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The output name comes from {@link MediaPlayerFactory#getAudioOutputs()}.
     * <p>
     * The device id comes from the {@link AudioDevice#getDeviceId()} returned by
     * {@link MediaPlayerFactory#getAudioOutputs()}.
     *
     * @param output name of the desired audio output
     * @param outputDeviceId id of the desired audio output device
     */
    public void setAudioOutputDevice(String output, String outputDeviceId) {
        libvlc.libvlc_audio_output_device_set(mediaPlayerInstance, output, outputDeviceId);
    }

    /**
     * Get the available audio devices for the media player audio output.
     *
     * @return list of audio devices, or <code>null</code> if not available
     */
    public List<AudioDevice> getAudioOutputDevices() {
        List<AudioDevice> result = new ArrayList<AudioDevice>();
        libvlc_audio_output_device_t audioDevices = libvlc.libvlc_audio_output_device_enum(mediaPlayerInstance);
        if (audioDevices != null) {
            libvlc_audio_output_device_t audioDevice = audioDevices;
            while (audioDevice != null) {
                String device = NativeString.copyNativeString(audioDevice.psz_device);
                String description = NativeString.copyNativeString(audioDevice.psz_description);
                result.add(new AudioDevice(device, description));
                audioDevice = audioDevice.p_next;
            }
            libvlc.libvlc_audio_output_device_list_release(audioDevices);
        }
        return result;
    }

    /**
     * Toggle volume mute.
     *
     * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
     */
    public boolean mute() {
        libvlc.libvlc_audio_toggle_mute(mediaPlayerInstance);
        return isMute();
    }

    /**
     * Mute or un-mute the volume.
     *
     * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
     */
    public void mute(boolean mute) {
        libvlc.libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
    }

    /**
     * Test whether or not the volume is currently muted.
     *
     * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
     */
    public boolean isMute() {
        return libvlc.libvlc_audio_get_mute(mediaPlayerInstance) != 0;
    }

    /**
     * Get the current volume.
     *
     * @return volume, a percentage of full volume in the range 0 to 200
     */
    public int getVolume() {
        return libvlc.libvlc_audio_get_volume(mediaPlayerInstance);
    }

    /**
     * Set the volume.
     * <p>
     * The volume is actually a percentage of full volume, setting a volume over
     * 100 may cause audible distortion.
     *
     * @param volume volume, a percentage of full volume in the range 0 to 200
     */
    public void setVolume(int volume) {
        libvlc.libvlc_audio_set_volume(mediaPlayerInstance, volume);
    }

    /**
     * Get the current audio channel.
     *
     * For channel values see {@link libvlc_audio_output_channel_t}.
     *
     * @return audio channel
     */
    public int getAudioChannel() {
        return libvlc.libvlc_audio_get_channel(mediaPlayerInstance);
    }

    /**
     * Set the audio channel.
     *
     * For channel values see {@link libvlc_audio_output_channel_t}.
     *
     * @param channel channel
     */
    public void setAudioChannel(int channel) {
        libvlc.libvlc_audio_set_channel(mediaPlayerInstance, channel);
    }

    /**
     * Get the audio delay.
     *
     * @return audio delay, in microseconds
     */
    public long getAudioDelay() {
        return libvlc.libvlc_audio_get_delay(mediaPlayerInstance);
    }

    /**
     * Set the audio delay.
     * <p>
     * The audio delay is set for the current item only and will be reset to zero each time the
     * media changes.
     *
     * @param delay desired audio delay, in microseconds
     */
    public void setAudioDelay(long delay) {
        libvlc.libvlc_audio_set_delay(mediaPlayerInstance, delay);
    }

    /**
     * Get the current audio equalizer.
     *
     * @return equalizer, or <code>null</code> if there is no active equalizer
     */
    public Equalizer getEqualizer() {
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
            libvlc.libvlc_audio_equalizer_release(equalizerInstance);
            equalizerInstance = null;
        }
        this.equalizer = equalizer;
        if (this.equalizer != null) {
            equalizerInstance = libvlc.libvlc_audio_equalizer_new();
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
            libvlc.libvlc_audio_equalizer_set_preamp(equalizerInstance, equalizer.getPreamp());
            for(int i = 0; i < libvlc.libvlc_audio_equalizer_get_band_count(); i ++ ) {
                libvlc.libvlc_audio_equalizer_set_amp_at_index(equalizerInstance, equalizer.getAmp(i), i);
            }
            libvlc.libvlc_media_player_set_equalizer(mediaPlayerInstance, equalizerInstance);
        }
        else {
            libvlc.libvlc_media_player_set_equalizer(mediaPlayerInstance, null);
        }
    }

    /**
     * Get the number of available audio tracks.
     *
     * @return track count
     */
    public int getAudioTrackCount() {
        return libvlc.libvlc_audio_get_track_count(mediaPlayerInstance);
    }

    /**
     * Get the current audio track.
     *
     * @return track identifier, see {@link #getAudioDescriptions()}
     */
    public int getAudioTrack() {
        return libvlc.libvlc_audio_get_track(mediaPlayerInstance);
    }

    /**
     * Set a new audio track to play.
     * <p>
     * The track identifier must be one of those returned by {@link #getAudioDescriptions()}.
     * <p>
     * Audio can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available track identifiers go in sequence from zero up to
     * {@link #getAudioTrackCount()}-1. The {@link #getAudioDescriptions()} method should always
     * be used to ascertain the available track identifiers.
     *
     * @param track track identifier
     * @return current audio track identifier
     */
    public int setAudioTrack(int track) {
        libvlc.libvlc_audio_set_track(mediaPlayerInstance, track);
        return getAudioTrack();
    }

    /**
     * Get the audio track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public java.util.List<TrackDescription> getAudioDescriptions() {
        return Descriptions.audioTrackDescriptions(libvlc, mediaPlayerInstance);
    }

    @Override
    protected void release() {
        setEqualizer(null);
    }

}
