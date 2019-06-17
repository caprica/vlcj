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

package uk.co.caprica.vlcj.test.direct;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * This simple test player shows how to get direct access to the video frame data.
 * <p>
 * This implementation uses the new (1.1.1) libvlc video call-backs function.
 * <p>
 * Since the video frame data is made available, the Java call-back may modify the contents of the
 * frame if required.
 * <p>
 * The frame data may also be rendered into components such as an OpenGL texture.
 */
public class DirectTestPlayer extends VlcjTest {

    private static DirectTestPlayer app;

    // The size does NOT need to match the mediaPlayer size - it's the size that the media will be scaled
    // to - matching the native size will be faster of course
    private final int width = 720;
    private final int height = 480;

    /**
     * Image to render the video frame data.
     */
    private final BufferedImage image;

    private final MediaPlayerFactory factory;

    private final EmbeddedMediaPlayer mediaPlayer;

    private ImagePane imagePane;

    public DirectTestPlayer(String media) throws InterruptedException, InvocationTargetException {
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);
        image.setAccelerationPriority(1.0f);

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame("VLCJ Direct Video Test");
                frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
                imagePane = new ImagePane(image);
                imagePane.setSize(width, height);
                imagePane.setMinimumSize(new Dimension(width, height));
                imagePane.setPreferredSize(new Dimension(width, height));
                frame.getContentPane().setLayout(new BorderLayout());
                frame.getContentPane().add(imagePane, BorderLayout.CENTER);
                frame.pack();
                frame.setResizable(false);
                frame.setVisible(true);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent evt) {
                        mediaPlayer.release();
                        factory.release();
                        System.exit(0);
                    }
                });
            }

        });

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(new TestBufferFormatCallback(), new TestRenderCallback(), true));

        mediaPlayer.media().play(media);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Specify a single media URL");
            System.exit(1);
        }

        app = new DirectTestPlayer(args[0]);

        Thread.currentThread().join();
    }

    @SuppressWarnings("serial")
    private final class ImagePane extends JPanel {

        private final BufferedImage image;

        private final Font font = new Font("Sansserif", Font.BOLD, 36);

        public ImagePane(BufferedImage image) {
            this.image = image;
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(image, null, 0, 0);
            // You could draw on top of the image here...
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2.setColor(Color.red);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.3f));
            g2.fillRoundRect(100, 100, 100, 80, 32, 32);
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setColor(Color.white);
            g2.setFont(font);
            g2.drawString("vlcj direct media player", 130, 150);
        }
    }

    private final int[] rgbBuffer = new int[width * height];

    private final class TestRenderCallback implements RenderCallback {

        // This is not optimal, see the CallbackMediaPlayerComponent for a way to render directly into the raster of a
        // Buffered image

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            ByteBuffer bb = nativeBuffers[0];
            IntBuffer ib = bb.asIntBuffer();
            ib.get(rgbBuffer);

            // The image data could be manipulated here...

            /* RGB to GRAYScale conversion example */
            for (int i=0; i < rgbBuffer.length; i++){
                int argb = rgbBuffer[i];
                int b = (argb & 0xFF);
                int g = ((argb >> 8 ) & 0xFF);
                int r = ((argb >> 16 ) & 0xFF);
                int grey = (r + g + b + g) >> 2 ; // performance optimized - not real grey!
                rgbBuffer[i] = (grey << 16) + (grey << 8) + grey;
            }

            image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
            imagePane.repaint();
        }
    }

    private final class TestBufferFormatCallback extends BufferFormatCallbackAdapter {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new RV32BufferFormat(width, height);
        }

    }
}
