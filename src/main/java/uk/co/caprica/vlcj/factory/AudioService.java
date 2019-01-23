package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.model.AudioDevice;
import uk.co.caprica.vlcj.model.AudioOutput;
import uk.co.caprica.vlcj.binding.NativeString;

import java.util.ArrayList;
import java.util.List;

public final class AudioService extends BaseService {

    AudioService(MediaPlayerFactory factory) {
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
        libvlc_audio_output_t audioOutputs = libvlc.libvlc_audio_output_list_get(instance);
        if (audioOutputs != null) {
            libvlc_audio_output_t audioOutput = audioOutputs;
            while (audioOutput != null) {
                String name = NativeString.copyNativeString(audioOutput.psz_name);
                String description = NativeString.copyNativeString(audioOutput.psz_description);
                result.add(new AudioOutput(name, description, getAudioOutputDevices(name)));
                audioOutput = audioOutput.p_next;
            }
            libvlc.libvlc_audio_output_list_release(audioOutputs.getPointer());
        }
        return result;
    }

    /**
     * Get the devices associated with an audio output.
     *
     * @param outputName output
     * @return collection of audio output devices
     */
    private List<AudioDevice> getAudioOutputDevices(String outputName) {
        List<AudioDevice> result = new ArrayList<AudioDevice>();
        libvlc_audio_output_device_t audioDevices = libvlc.libvlc_audio_output_device_list_get(instance, outputName);
        if (audioDevices != null) {
            libvlc_audio_output_device_t audioDevice = audioDevices;
            while(audioDevice != null) {
                String device = NativeString.copyNativeString(audioDevice.psz_device);
                String description = NativeString.copyNativeString(audioDevice.psz_description);
                result.add(new AudioDevice(device, description));
                audioDevice = audioDevice.p_next;
            }
            libvlc.libvlc_audio_output_device_list_release(audioDevices.getPointer());
        }
        return result;
    }

}
