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

package uk.co.caprica.vlcj.test.thumbs;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A simple application to generate a single thumbnail image from a media file.
 * <p>
 * This application shows how to implement the "vlc-thumb" sample (written in "C") that is part of
 * the vlc code-base.
 * <p>
 * The original "C" implementation is available in the vlc code-base here:
 *
 * <pre>
 *   /vlc/doc/libvlc/vlc-thumb.c
 * </pre>
 *
 * This implementation tries to stick as closely to the original "C" implementation as possible, but
 * uses a different synchronisation technique.
 * <p>
 * Since this is a test, the implementation is not very tolerant to errors.
 */
public class ThumbsTest extends VlcjTest {

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

    private static final float VLC_THUMBNAIL_POSITION = 30.0f / 100.0f;

    public static void main(String[] args) throws Exception {
        if(args.length != 3) {
            System.err.println("Usage: <mrl> <width> <snapshot-file>");
            System.exit(1);
        }

        String mrl = args[0];
        int imageWidth = Integer.parseInt(args[1]);
        File snapshotFile = new File(args[2]);

        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        final CountDownLatch inPositionLatch = new CountDownLatch(1);
        final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                if(newPosition >= VLC_THUMBNAIL_POSITION * 0.9f) { /* 90% margin */
                    inPositionLatch.countDown();
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });

        if(mediaPlayer.startMedia(mrl)) {
            mediaPlayer.setPosition(VLC_THUMBNAIL_POSITION);
            inPositionLatch.await(); // Might wait forever if error

            mediaPlayer.saveSnapshot(snapshotFile, imageWidth, 0);
            snapshotTakenLatch.await(); // Might wait forever if error

            mediaPlayer.stop();
        }

        mediaPlayer.release();
        factory.release();
    }
}
