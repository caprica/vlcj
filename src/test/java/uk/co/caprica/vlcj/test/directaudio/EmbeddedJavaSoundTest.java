package uk.co.caprica.vlcj.test.directaudio;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.callback.AudioCallbackAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class EmbeddedJavaSoundTest extends VlcjTest {

    private static final String FORMAT = "S16N";

    private static final int RATE = 44100;

    private static final int CHANNELS = 2;

    private final CountDownLatch sync = new CountDownLatch(1);

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(String[] args) throws Exception {
        args = new String[] {"/home/mark/1.mp4"};

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
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayerComponent.mediaPlayer().audio().callback(FORMAT, RATE, CHANNELS, new JavaSoundCallback(FORMAT, RATE, CHANNELS));

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.add(mediaPlayerComponent, BorderLayout.CENTER);

        JFrame f = new JFrame();
        f.setContentPane(cp);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setBounds(100, 100, 1000, 800);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
    }

    private void play(String mrl) throws Exception {
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
        mediaPlayerComponent.mediaPlayer().controls().setPosition(0.98f);
        try {
            sync.await();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        mediaPlayerComponent.release();
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
