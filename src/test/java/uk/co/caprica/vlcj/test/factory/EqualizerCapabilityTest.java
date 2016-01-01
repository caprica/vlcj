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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.factory;

import java.util.List;
import java.util.Map;

import uk.co.caprica.vlcj.player.Equalizer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test of the static equalizer preset data provided by the media player factory.
 */
public class EqualizerCapabilityTest extends VlcjTest {

    public static void main(String[] args) {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        if(!factory.isEqualizerAvailable()) {
            System.out.println("Equalizer not available on this platform");
            System.exit(0);
        }

        System.out.println("Preset names:");
        List<String> presetNames = factory.getEqualizerPresetNames();
        for(String presetName : presetNames) {
            System.out.printf(" %s%n", presetName);
        }
        System.out.println();

        System.out.println("Band frequencies:");
        List<Float> bandFrequencies = factory.getEqualizerBandFrequencies();
        for(Float freq : bandFrequencies) {
            System.out.printf(" %f Hz%n", freq);
        }
        System.out.println();

        // You can get individual presets by name, or get them all in one call as is done here...

        Map<String, Equalizer> allPresets = factory.getAllPresetEqualizers();
        for(String presetName : allPresets.keySet()) {
            System.out.printf("%s:%n", presetName);
            Equalizer preset = allPresets.get(presetName);
            System.out.printf("%10s : %f Hz%n", "preamp", preset.getPreamp());
            float[] amps = preset.getAmps();
            for(int i = 0; i < amps.length; i++) {
                float freq = bandFrequencies.get(i);
                String unit;
                if(freq < 1000f) {
                    unit = "Hz";
                }
                else {
                    freq = freq / 1000f;
                    unit = "kHz";
                }
                System.out.printf("%7.0f %-3s: %f Hz%n", freq, unit, amps[i]);
            }
            System.out.println();
        }
    }
}
