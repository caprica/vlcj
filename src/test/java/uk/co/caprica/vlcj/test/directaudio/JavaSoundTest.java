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

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.callback.AudioCallbackAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.CountDownLatch;

/**
 * Test showing how to use the direct audio player to play through the JavaSound API.
 * <p>
 * The native decoded audio samples from vlc are simply played via Javasound.
 */
public class JavaSoundTest extends VlcjTest {

    private static final String FORMAT = "S16N";

    private static final int RATE = 44100;

    private static final int CHANNELS = 2;

    private final CountDownLatch sync = new CountDownLatch(1);

    private final MediaPlayerFactory mediaPlayerFactory;
    private final MediaPlayer mediaPlayer;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        new JavaSoundTest().play(args[0]);

        System.out.println("Exit normally");
    }

    public JavaSoundTest() {
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    }

    private void play(String mrl) throws Exception {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void error(MediaPlayer mediaPlayer) {
                sync.countDown();
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                sync.countDown();
            }

        });

        mediaPlayer.audio().callback(FORMAT, RATE, CHANNELS, new JavaSoundCallback(FORMAT, RATE, CHANNELS));

        mediaPlayer.media().play(mrl);

        try {
            sync.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        mediaPlayer.controls().stop();
        mediaPlayer.release();
        mediaPlayerFactory.release();
    }

    /**
     *
     */
    private class JavaSoundCallback extends AudioCallbackAdapter {

        private static final int BLOCK_SIZE = 4;

        private static final int SAMPLE_BITS = 16; // BLOCK_SIZE * 8 / channels ???

        private final AudioFormat audioFormat;

        private final Info info;

        private final SourceDataLine dataLine;

        public JavaSoundCallback(String format, int rate, int channels) throws Exception {
            this.audioFormat = new AudioFormat(rate, SAMPLE_BITS, channels, true, false);
            this.info = new Info(SourceDataLine.class, audioFormat);
            this.dataLine = (SourceDataLine)AudioSystem.getLine(info);
            start();
        }

        private void start() throws Exception {
            System.out.println("start()");
            dataLine.open(audioFormat);
            dataLine.start();
        }

        private void stop() {
            System.out.println("stop()");
            dataLine.close();
        }

        @Override
        public void play(MediaPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
            // There may be more efficient ways to do this...
            int bufferSize = sampleCount * BLOCK_SIZE;
            // You could process these samples in some way before playing them...
            byte[] data = samples.getByteArray(0, bufferSize);
            dataLine.write(data, 0, bufferSize);
        }

        @Override
        public void drain(MediaPlayer mediaPlayer) {
            System.out.println("drain()");
            dataLine.drain();
        }
    }

}
