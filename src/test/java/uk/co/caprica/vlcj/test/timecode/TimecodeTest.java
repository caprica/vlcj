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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.timecode;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.TextTrackInfo;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Minimal example to show how to enable the new timecode subtitle track.
 * <p>
 * This requires vlc 2.1.0 or later since that is when the timecode plugin was
 * added.
 * <p>
 * Timecodes are not available for all media types - for example this does not
 * work when playing DVD ISO files.
 * <p>
 * This example is very basic and purely demonstrative - it won't even terminate
 * the JVM properly when you close the video window, and it doesn't clean up the
 * media player resources.
 */
public class TimecodeTest extends VlcjTest {

    /**
     * Codec name for the timecode.
     */
    private static final String TIMECODE_CODEC = "t140";

    /**
     * Media player factory.
     */
    private final MediaPlayerFactory factory;

    /**
     * Media player.
     */
    private final MediaPlayer player;

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        new TimecodeTest().start(args[0]);

        // Wait forever
        Thread.currentThread().join();
    }

    /**
     * Create a media player.
     */
    public TimecodeTest() {
        factory = new MediaPlayerFactory(
            "--input-slave", "timecode://",
            "--timecode-fps", "25/1"            // <--- Tweak this to suit
        );

        player = factory.mediaPlayers().newMediaPlayer();
    }

    /**
     * Start the media.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        player.media().play(mrl);
        showTimecode();
    }

    /**
     * Show the timecode.
     */
    private void showTimecode() {
        // We have to search for the text/spu track containing the timecode...
        Integer timecodeTrack = null;
        for (TextTrackInfo trackInfo : player.media().info().textTracks()) {
            if (TIMECODE_CODEC.equals(trackInfo.codecName())) {
                timecodeTrack = trackInfo.id();
                break;
            }
        }

        System.out.println("timecodeTrack=" + timecodeTrack);

        // If we found the timecode track, enable it
        if (timecodeTrack != null) {
            player.subpictures().setTrack(timecodeTrack);
        }
    }

    /**
     * Hide the timecode.
     * <p>
     * Unused for this demo.
     */
//    private void hideTimecode() {
//        player.setSpu(-1);
//    }
}
