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

package uk.co.caprica.vlcj.test.directaudio;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.callback.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * Test for the {@link DirectAudioPlayer}.
 * <p>
 * This test converts an mp3 to a "raw" PCM file.
 * <p>
 * The resulting file can be played back by vlc with a command similar to:
 * <pre>
 * vlc --demux=rawaud --rawaud-channels 2 --rawaud-samplerate 44100 test.raw
 * </pre>
 * Note that seeking in raw streams seems can introduce long-ish pauses.
 */
public class DirectAudioPlayerTest extends VlcjTest {

    /**
     * Synchronisation object to wait for the audio to finish.
     */
    private static final Semaphore sync = new Semaphore(0);

    /**
     * Factory.
     */
    private final MediaPlayerFactory factory;

    /**
     * Media player.
     */
    private final MediaPlayer audioPlayer;

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        new DirectAudioPlayerTest().start(args[0]);

        System.out.println("Exit normally");
    }

    /**
     * Create a test audio player.
     */
    public DirectAudioPlayerTest() throws IOException {
        factory = new MediaPlayerFactory();
        audioPlayer = factory.mediaPlayers().newMediaPlayer();

        audioPlayer.audio().callback("S16N", 44100, 2, new TestAudioCallbackAdapter(new File("test.raw")));

        audioPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                System.out.println("playing()");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                sync.release();
                System.out.println("After release waiter");
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("error()");
            }
        });
    }

    /**
     * Play the audio file.
     *
     * @param mrl media resource locator
     */
    private void start(String mrl) {
        audioPlayer.media().play(mrl);

        System.out.println("Waiting for finished...");

        try {
            sync.acquire(); // Slight race condition in theory possible if the audio finishes immediately (but this is just a test so it's good enough)...
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished, releasing native resources...");

        audioPlayer.release();
        factory.release();

        System.out.println("All done");
    }

    /**
     * Implementation of an audio callback that writes the decoded data to a local file.
     */
    private class TestAudioCallbackAdapter extends DefaultAudioCallbackAdapter {

        /**
         * Output stream.
         */
        private final BufferedOutputStream out;

        /**
         * Create an audio callback.
         */
        public TestAudioCallbackAdapter(File output) throws IOException {
            super(4); // 4 is the block size for the audio samples
            out = new BufferedOutputStream(new FileOutputStream(output));
        }

        @Override
        protected void onPlay(MediaPlayer mediaPlayer, byte[] data, int sampleCount, long pts) {
            try {
                out.write(data);
            }
            catch(IOException e) {
                // Can't really do anything, should stop the media player I suppose...
                e.printStackTrace();
            }
        }

        @Override
        public void flush(MediaPlayer mediaPlayer, long pts) {
            System.out.println("flush()");
        }

        @Override
        public void drain(MediaPlayer mediaPlayer) {
            System.out.println("drain()");
            try {
                out.flush();
                out.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
