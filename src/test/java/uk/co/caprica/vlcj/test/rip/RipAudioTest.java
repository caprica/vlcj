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

package uk.co.caprica.vlcj.test.rip;

import java.util.concurrent.CountDownLatch;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple example to show how to extract the audio track from a media file and encode it as a
 * stand-alone mp3.
 * <p>
 * Specify a MRL and an mp3 output file as command-line arguments.
 * <p>
 * On Linux at least you can have a media player play the mp3 file as it is being generated.
 * <p>
 * Since no video is being displayed the audio is played back and encoded as quickly as possible.
 */
public class RipAudioTest extends VlcjTest {

    private final CountDownLatch completionLatch;

    private final MediaPlayerFactory mediaPlayerFactory;

    private final MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Specify <mrl> and <mp3-output-file>, e.g. dvdsimple:///dvd/dvd ~/rip.mp3");
            System.exit(1);
        }

        new RipAudioTest().rip(args);
    }

    public RipAudioTest() {
        // Create a synchronisation point
        completionLatch = new CountDownLatch(1);

        // Create the media player
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("Rip completed successfully");
                completionLatch.countDown();
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("Rip failed");
                completionLatch.countDown();
            }
        });
    }

    private void rip(String[] args) {
        System.out.println("Rip '" + args[0] + "' to '" + args[1] + "'...");

        // Play the media - the media options can be tweaked to get whatever
        // encoding results you want. Of course "dummy" is not a real video codec
        // but specifying it here prevents any video output being created (there
        // may be a better way)
        mediaPlayer.playMedia(args[0], "sout=#transcode{acodec=mp3,channels=2,ab=192,samplerate=44100,vcodec=dummy}:standard{dst=" + args[1] + ",mux=raw,access=file}");

        try {
            // Wait here until finished or error
            completionLatch.await();
        }
        catch(InterruptedException e) {
        }

        // Clean up
        mediaPlayer.release();
        mediaPlayerFactory.release();
    }
}
