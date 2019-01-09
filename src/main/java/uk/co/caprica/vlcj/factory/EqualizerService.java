package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.internal.libvlc_equalizer_t;
import uk.co.caprica.vlcj.player.Equalizer;

import java.util.*;

public final class EqualizerService extends BaseService {

    private final List<Float> bands;

    private final List<String> presets;

    EqualizerService(MediaPlayerFactory factory) {
        super(factory);
        this.bands = cacheBands();
        this.presets = cachePresets();
    }

    /**
     * Get the list of distinct equalizer band frequencies.
     *
     * @return list of frequencies (Hz)
     */
    public final List<Float> bands() {
        return new ArrayList<Float>(bands);
    }

    /**
     * Get the list of names of available equalizer presets.
     *
     * @return list of preset names
     */
    public final List<String> presets() {
        return new ArrayList<String>(presets);
    }

    /**
     * Create a new audio equalizer.
     *
     * @return equalizer
     */
    public final Equalizer newEqualizer() {
        return new Equalizer(libvlc.libvlc_audio_equalizer_get_band_count());
    }

    /**
     * Create a new audio equalizer from a named preset.
     *
     * @param presetName name of the preset
     * @return equalizer
     */
    public final Equalizer newEqualizer(String presetName) {
        int index = presets.indexOf(presetName);
        if (index != -1) {
            libvlc_equalizer_t presetEqualizer = libvlc.libvlc_audio_equalizer_new_from_preset(index);
            if(presetEqualizer != null) {
                Equalizer equalizer = new Equalizer(libvlc.libvlc_audio_equalizer_get_band_count());
                equalizer.setPreamp(libvlc.libvlc_audio_equalizer_get_preamp(presetEqualizer));
                for(int i = 0; i < libvlc.libvlc_audio_equalizer_get_band_count(); i++) {
                    equalizer.setAmp(i, libvlc.libvlc_audio_equalizer_get_amp_at_index(presetEqualizer, i));
                }
                libvlc.libvlc_audio_equalizer_release(presetEqualizer);
                return equalizer;
            }
            else {
                return null;
            }
        }
        else {
            throw new IllegalArgumentException("No such preset named '" + presetName + "'");
        }
    }

    /**
     * Get all of the available preset equalizer instances.
     * <p>
     * This will return new equalizer instances (i.e. they are not cached or shared), so
     * applications are free to change the values in the returned equalizer instances if
     * so desired.
     *
     * @return map of preset name to equalizer instance, sorted by name
     */
    public final Map<String, Equalizer> allPresetEqualizers() {
        Map<String, Equalizer> result = new TreeMap<String, Equalizer>();
        for (String preset : presets) {
            result.put(preset, newEqualizer(preset));
        }
        return result;
    }

    private List<Float> cacheBands() {
        int numBands = libvlc.libvlc_audio_equalizer_get_band_count();
        List<Float> result = new ArrayList<Float>(numBands);
        for (int i = 0; i < numBands; i++) {
            result.add(libvlc.libvlc_audio_equalizer_get_band_frequency(i));
        }
        return Collections.unmodifiableList(result);
    }

    private List<String> cachePresets() {
        int numPresets = libvlc.libvlc_audio_equalizer_get_preset_count();
        List<String> result = new ArrayList<String>(numPresets);
        for (int i = 0; i < numPresets; i++) {
            result.add(libvlc.libvlc_audio_equalizer_get_preset_name(i));
        }
        return Collections.unmodifiableList(result);
    }

}
