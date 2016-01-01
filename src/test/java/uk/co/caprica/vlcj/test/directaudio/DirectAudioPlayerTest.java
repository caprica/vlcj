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

package uk.co.caprica.vlcj.test.directaudio;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.directaudio.DefaultAudioCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

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
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DirectAudioPlayerTest.class);

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
        if(args.length != 1) {
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
        audioPlayer = factory.newDirectAudioPlayer("S16N", 44100, 2, new TestAudioCallbackAdapter(new File("test.raw")));
        audioPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                logger.info("playing()");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                logger.info("finished()");
                logger.info("Release waiter...");
                sync.release();
                logger.info("After release waiter");
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                logger.info("error()");
            }
        });
    }

    /**
     * Play the audio file.
     *
     * @param mrl media resource locator
     */
    private void start(String mrl) {
        audioPlayer.playMedia(mrl);

        logger.info("Waiting for finished...");

        try {
            sync.acquire(); // Slight race condition in theory possible if the audio finishes immediately (but this is just a test so it's good enough)...
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Finished, releasing native resources...");

        audioPlayer.release();
        factory.release();

        logger.info("All done");
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
        protected void onPlay(DirectAudioPlayer mediaPlayer, byte[] data, int sampleCount, long pts) {
            try {
                out.write(data);
            }
            catch(IOException e) {
                // Can't really do anything, should stop the media player I suppose...
                e.printStackTrace();
            }
        }

        @Override
        public void flush(DirectAudioPlayer mediaPlayer, long pts) {
            logger.info("flush()");
        }

        @Override
        public void drain(DirectAudioPlayer mediaPlayer) {
            logger.info("drain()");
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
