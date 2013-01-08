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

package uk.co.caprica.vlcj.test.condition;

import java.io.File;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.condition.Condition;
import uk.co.caprica.vlcj.player.condition.conditions.ParsedCondition;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Demonstration of the synchronous approach to media player programming when
 * using media player condition objects.
 * <p>
 * This example asynchronously parses the media to get available meta data.
 * <p>
 * This is only useful as an example - in practical terms it is much simpler to
 * use the synchronous {@link MediaPlayer#parseMedia()} method.
 * <p>
 * Specify the MRL for the media on the command line.
 */
public class ConditionMetaTest extends VlcjTest {

    // Some standard options for headless operation
    private static final String[] VLC_ARGS = {
        "--intf", "dummy",          /* no interface */
        "--vout", "dummy",          /* we don't want video (output) */
        "--no-audio",               /* we don't want audio (decoding) */
        "--no-video-title-show",    /* nor the filename displayed */
        "--no-stats",               /* no stats */
        "--no-sub-autodetect-file", /* we don't want subtitles */
        "--no-inhibit",             /* we don't want interfaces */
        "--no-disable-screensaver", /* we don't want interfaces */
        "--no-snapshot-preview",    /* no blending in dummy vout */
    };

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Usage: <mrl>");
            System.exit(1);
        }

        final String mrl = args[0];

        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.setSnapshotDirectory(new File(".").getAbsolutePath());

        // The sequence for getting the meta is...
        //
        // Start the media and wait for it to play
        // Wait for parsed condition
        //
        // There is a small overhead of actually having to start the media - but
        // to mitigate this the media player factory configuration has disabled
        // audio and video outputs so there will be no visible/audible sign of
        // the media playing

        Condition<Integer> parsedCondition = new ParsedCondition(mediaPlayer) {
            @Override
            protected void onBefore() {
                // Some media, such as mpg, must be played before all meta data (e.g. duration) is available
                mediaPlayer.startMedia(mrl); // "start" waits until the media is playing before returning
                mediaPlayer.requestParseMedia(); // asynchronous invocation
            }

            @Override
            protected void onAfter(Integer result) {
                mediaPlayer.stop();
            }
        };
        parsedCondition.await();

        // This is functionally equivalent to the simpler synchronous version:
        /*
        mediaPlayer.startMedia(mrl);  // "start" waits until the media is playing before returning
        mediaPlayer.parseMedia(); // synchronous invocation
        mediaPlayer.stop();
        */

        System.out.println(mediaPlayer.getMediaMeta());

        mediaPlayer.release();
        factory.release();
    }
}
