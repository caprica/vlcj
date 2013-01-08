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

package uk.co.caprica.vlcj.test.minimalmp3;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * The smallest amount of code to play an audio file.
 * <p>
 * Audio is simple since there's no video surface to worry about (unless you want the audio
 * visualisations).
 */
public class Mp3Test extends VlcjTest {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify an MRL to play");
            System.exit(1);
        }

        AudioMediaPlayerComponent audioPlayer = new AudioMediaPlayerComponent() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.exit(0);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("Failed to play media");
                System.exit(1);
            }
        };

        // Play the MRL specified by the first command-line argument
        audioPlayer.getMediaPlayer().playMedia(args[0]);
        // Wait forever
        Thread.currentThread().join();
    }
}
