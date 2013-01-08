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

package uk.co.caprica.vlcj.test.factory;

import java.util.List;

import uk.co.caprica.vlcj.player.AudioDevice;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to dump out audio output devices.
 */
public class AudioOutputsTest extends VlcjTest {

    private static final String FORMAT_PATTERN = "%3s %-12s %-40s %-40s %s\n";

    public static void main(String[] args) throws Exception {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        List<AudioOutput> audioOutputs = factory.getAudioOutputs();

        System.out.println("Audio Outputs:");
        System.out.println();

        System.out.printf(FORMAT_PATTERN, "#", "Name", "Description", "Devices", "Long Name");
        System.out.printf(FORMAT_PATTERN, "=", "====", "===========", "=======", "=========");
        for(int i = 0; i < audioOutputs.size(); i ++ ) {
            dump(i, audioOutputs.get(i));
        }
    }

    private static void dump(int i, AudioOutput audioOutput) {
        List<AudioDevice> audioDevices = audioOutput.getDevices();
        System.out.printf(FORMAT_PATTERN, String.valueOf(i + 1), audioOutput.getName(), audioOutput.getDescription(), "(" + audioDevices.size() + ")", "");
        for(int j = 0; j < audioDevices.size(); j ++ ) {
            AudioDevice audioDevice = audioOutput.getDevices().get(j);
            System.out.printf(FORMAT_PATTERN, "", "", "", audioDevice.getDeviceId(), formatLongName(audioDevice.getLongName()));
        }
    }

    private static String formatLongName(String longName) {
        return longName != null ? longName.replaceAll("\\n", " ") : "";
    }
}
