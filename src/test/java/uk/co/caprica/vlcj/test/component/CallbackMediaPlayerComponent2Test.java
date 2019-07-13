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

package uk.co.caprica.vlcj.test.component;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Quick example showing how to render cloned video output (using a shared image).
 * <p>
 * With vlcj-5 and VLC 4.x, this should work even better with OpenGL textures.
 */
@SuppressWarnings("serial")
public class CallbackMediaPlayerComponent2Test extends VlcjTest {

    /**
     * Media player component.
     */
    private final CallbackMediaPlayerComponent mediaPlayerComponent;

    private final DefaultBufferFormatCallback defaultBufferFormatCallback = new DefaultBufferFormatCallback();

    private final DefaultRenderCallback defaultRenderCallback = new DefaultRenderCallback();

    private BufferedImage image;

    private JPanel videoSurface1;

    private JPanel videoSurface2;

    /**
     * Default implementation of a buffer format callback that returns a buffer format suitable for rendering into a
     * {@link BufferedImage}.
     */
    private class DefaultBufferFormatCallback extends BufferFormatCallbackAdapter {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            newVideoBuffer(sourceWidth, sourceHeight);
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

    }

    private void newVideoBuffer(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        defaultRenderCallback.setImageBuffer(image);
    }

    /**
     * Default implementation of a render callback that copies video frame data directly to the data buffer of an image
     * raster.
     */
    private class DefaultRenderCallback extends RenderCallbackAdapter {

        private void setImageBuffer(BufferedImage image) {
            setBuffer(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        }

        @Override
        protected void onDisplay(MediaPlayer mediaPlayer, int[] buffer) {
            videoSurface1.repaint();
            videoSurface2.repaint();
        }

    }

    /**
     * Application entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Specify an mrl");
            System.exit(1);
        }

        final String mrl = args[0];

        setLookAndFeel();

        new CallbackMediaPlayerComponent2Test().start(mrl);
    }

    /**
     * Create a new test.
     */
    private CallbackMediaPlayerComponent2Test() {
        final JFrame frame = new JFrame("vlcj Callback Media Player Component Test");

        // You don't have to use a CallbackMediaPlayerComponent, you could just use a CallbackVideoSurface, but this
        // gives us a head-start on a reasonable default implementation
        mediaPlayerComponent = new CallbackMediaPlayerComponent(null, null, null, true, null, defaultRenderCallback, defaultBufferFormatCallback, null);

        videoSurface1 = new VideoPane() {

            private Font font = new Font("Helvetica", Font.PLAIN, 50);

            @Override
            protected void paintExtra(Graphics2D g2) {
                if (image != null) {
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(Color.white);
                    g2.setFont(font);
                    g2.drawString("Hello!", 100, 100);
                }
            }
        };

        videoSurface2 = new VideoPane() {
            @Override
            protected void paintExtra(Graphics2D g2) {
                if (image != null) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 0, 0, 128));
                    g2.fillOval(100, 100, 100, 100);
                }
            }
        };

        videoSurface2.setBackground(Color.black);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(2, 1, 16, 16));
        contentPane.setBackground(Color.black);
        contentPane.add(videoSurface1);
        contentPane.add(videoSurface2);

        frame.setBackground(Color.black);
        frame.setContentPane(contentPane);

        frame.setLocation(100, 100);
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    /**
     * Start playing a movie.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }

    private class VideoPane extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            int width = getWidth();
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setColor(Color.black);
            g2.fillRect(0, 0, width, height);

            if (image != null) {
                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();

                float sx = (float) width / imageWidth;
                float sy = (float) height / imageHeight;

                float sf = Math.min(sx, sy);

                float scaledW = imageWidth * sf;
                float scaledH = imageHeight * sf;

                g2.translate(
                    (width - scaledW) / 2,
                    (height - scaledH) / 2
                );

                if (sf != 1.0) {
                    g2.scale(sf, sf);
                }

                g2.drawImage(image, null, 0, 0);
            }

            paintExtra(g2);
        }

        protected void paintExtra(Graphics2D g2) {
        }

    }

}
