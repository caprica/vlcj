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

import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.component.DirectAudioPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Pointer;

/**
 * Test showing how to use the direct audio player to play through the JavaSound API.
 * <p>
 * The native decoded audio samples from vlc are simply played via Javasound.
 */
public class JavaSoundTest extends VlcjTest {

    private static final String FORMAT = "S16N";

    private static final int RATE = 44100;

    private static final int CHANNELS = 2;

    private final Logger logger = LoggerFactory.getLogger(JavaSoundTest.class);

    private final Semaphore sync = new Semaphore(0);

    private final JavaSoundDirectAudioPlayerComponent audioPlayerComponent;

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        new JavaSoundTest().play(args[0]);

        System.out.println("Exit normally");
    }

    public JavaSoundTest() throws Exception {
        audioPlayerComponent = new JavaSoundDirectAudioPlayerComponent(FORMAT, RATE, CHANNELS);
    }

    private void play(String mrl) throws Exception {
        audioPlayerComponent.start();
        audioPlayerComponent.getMediaPlayer().playMedia(mrl);
        try {
            sync.acquire(); // Potential race if the media has already finished, but very unlikely, and good enough for a test
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        audioPlayerComponent.stop();

        // audioPlayerComponent.release(true); // FIXME right now this causes a fatal JVM crash just before the JVM terminates, I am not sure why (the other direct audio player example does NOT crash)
        System.exit(0); // so instead do this...
    }

    /**
     *
     */
    private class JavaSoundDirectAudioPlayerComponent extends DirectAudioPlayerComponent {

        private static final int BLOCK_SIZE = 4;

        private static final int SAMPLE_BITS = 16; // BLOCK_SIZE * 8 / channels ???

        private final AudioFormat audioFormat;

        private final Info info;

        private final SourceDataLine dataLine;

        public JavaSoundDirectAudioPlayerComponent(String format, int rate, int channels) throws Exception {
            super(format, rate, channels);
            this.audioFormat = new AudioFormat(rate, SAMPLE_BITS, channels, true, false);
            this.info = new Info(SourceDataLine.class, audioFormat);
            this.dataLine = (SourceDataLine)AudioSystem.getLine(info);
        }

        private void start() throws Exception {
            logger.info("start()");
            dataLine.open(audioFormat);
            dataLine.start();
        }

        private void stop() {
            logger.info("stop()");
            dataLine.close();
        }

        @Override
        public void play(DirectAudioPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
            // There may be more efficient ways to do this...
            int bufferSize = sampleCount * BLOCK_SIZE;
            // You could process these samples in some way before playing them...
            byte[] data = samples.getByteArray(0, bufferSize);
            dataLine.write(data, 0, bufferSize);
        }

        @Override
        public void drain(DirectAudioPlayer mediaPlayer) {
            logger.info("drain()");
            dataLine.drain();
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            logger.info("finished()");
            sync.release();
        }
    }
}
