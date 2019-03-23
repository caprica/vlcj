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

package uk.co.caprica.vlcj.test.condition;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;

/**
 * Demonstration of the synchronous approach to media player programming when using media player conditional waiter
 * objects.
 * <p>
 * This example asynchronously parses the media to get available meta data.
 * <p>
 * Specify the MRL for the media on the command line.
 */
public class ConditionMetaTest extends VlcjTest {

    // Some standard options for headless operation
    private static final String[] VLC_ARGS = {
        "--intf", "dummy",          /* no interface */
        "--vout", "dummy",          /* we don't want video (output) */
        "--no-audio",               /* we don't want audio (decoding) */
        "--no-snapshot-preview",    /* no blending in dummy vout */
    };

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: <mrl>");
            System.exit(1);
        }

        final String mrl = args[0];

        final MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        final MediaPlayer mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.snapshots().setSnapshotDirectory(new File(".").getAbsolutePath());

        // The sequence for getting the meta is...
        //
        // Play the media
        // Wait for parsed condition
        //
        // There is a small overhead of actually having to start the media - but
        // to mitigate this the media player factory configuration has disabled
        // audio and video outputs so there will be no visible/audible sign of
        // the media playing

        mediaPlayer.media().prepare(mrl);

        Media media = mediaPlayer.media().newMedia();

        ParsedWaiter parsedWaiter = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(Media media) {
                // Some media, such as mpg, must be played before all meta data (e.g. duration) is available
                mediaPlayer.controls().play();
                mediaPlayer.media().parsing().parse(); // asynchronous invocation
                return true;
            }

            @Override
            protected void onAfter(Media media, Object result) {
                mediaPlayer.controls().stop();
            }
        };
        parsedWaiter.await();

        MetaData metaData = mediaPlayer.media().meta().asMetaData();
        System.out.println(metaData);

        media.release();

        mediaPlayer.release();
        factory.release();
    }

}
