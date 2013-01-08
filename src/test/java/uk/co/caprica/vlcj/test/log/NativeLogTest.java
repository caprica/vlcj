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

package uk.co.caprica.vlcj.test.log;

import java.util.concurrent.CountDownLatch;

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.log.LogEventListener;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test for the native log component.
 */
public class NativeLogTest extends VlcjTest {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify an MRL to play");
            System.exit(1);
        }

        AudioMediaPlayerComponent mediaPlayerComponent = new AudioMediaPlayerComponent();

        // This latch is used simply to cleanly exit the application when the
        // "finished" event is raised
        final CountDownLatch latch = new CountDownLatch(1);

        NativeLog log = mediaPlayerComponent.getMediaPlayerFactory().newLog();
        if (log == null) {
            System.out.println("Native log not available on this platform");
            System.exit(1);
        }

        log.setLevel(libvlc_log_level_e.DEBUG);
        log.addLogListener(new LogEventListener() {
            @Override
            public void log(libvlc_log_level_e level, String message) {
                System.out.printf("%-7s: %s\n", level, message);
            }
        });

        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                latch.countDown();
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                latch.countDown();
            }
        });

        mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);

        // Wait for finished/error
        latch.await();

        // Must release the components to exit (otherwise threads are left running)
        log.release();
        mediaPlayerComponent.release();
    }
}
