package uk.co.caprica.vlcj.test.directaudio;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.DirectAudioPlayerComponent;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Pointer;

public class EmbeddedJavaSoundTest extends VlcjTest {

    private static final String FORMAT = "S16N";

    private static final int RATE = 44100;

    private static final int CHANNELS = 2;

    private final CountDownLatch sync = new CountDownLatch(1);

    private final JavaSoundDirectAudioPlayerComponent audioPlayerComponent;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify an MRL");
            System.exit(1);
        }

        new EmbeddedJavaSoundTest().play(args[0]);

        System.out.println("Exit normally");

        // Force exit since the JFrame keeps us running
        System.exit(0);
    }

    public EmbeddedJavaSoundTest() throws Exception {
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        cp.add(canvas, BorderLayout.CENTER);

        JFrame f = new JFrame();
        f.setContentPane(cp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(100, 100, 1000, 800);
        f.setVisible(true);

        audioPlayerComponent = new JavaSoundDirectAudioPlayerComponent(FORMAT, RATE, CHANNELS);
        audioPlayerComponent.getMediaPlayer().videoSurface().setVideoSurface(audioPlayerComponent.getMediaPlayerFactory().videoSurfaces().newVideoSurface(canvas));
    }

    private void play(String mrl) throws Exception {
        audioPlayerComponent.start();
        audioPlayerComponent.getMediaPlayer().media().playMedia(mrl);
        audioPlayerComponent.getMediaPlayer().controls().setPosition(0.98f);
        try {
            sync.await();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        audioPlayerComponent.stop();
        audioPlayerComponent.release();
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
            System.out.println("start()");
            dataLine.open(audioFormat);
            dataLine.start();
        }

        private void stop() {
            System.out.println("stop()");
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
            System.out.println("drain()");
            dataLine.drain();
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            System.out.println("finished()");
            sync.countDown();
        }
    }
}
