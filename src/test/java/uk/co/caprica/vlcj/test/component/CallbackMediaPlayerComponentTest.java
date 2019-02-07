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

import uk.co.caprica.vlcj.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.callback.format.RV32BufferFormat;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 */
@SuppressWarnings("serial")
public class CallbackMediaPlayerComponentTest extends VlcjTest {

    /**
     * Media player component.
     */
    private final CallbackMediaPlayerComponent mediaPlayerComponent;

    /**
     *
     */
    private final int width = 800;

    /**
     *
     */
    private final int height = 450;

    /**
     * Application entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
        args = new String[] {"/home/mark/1.mp4"};

        if (args.length != 1) {
            System.out.println("Specify an mrl");
            System.exit(1);
        }

        final String mrl = args[0];

        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CallbackMediaPlayerComponentTest().start(mrl);
            }
        });
    }

    /**
     * Create a new test.
     */
    private CallbackMediaPlayerComponentTest() {
        JFrame frame = new JFrame("vlcj Callback Media Player Component Test");

        final Font font = new Font("Sansserif", Font.BOLD, 36);

        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new CallbackMediaPlayerComponent(null, null, new Dimension(width, height), bufferFormatCallback, null, true, null, null) {
            @Override
            protected void onDrawOverlay(Graphics2D g2) {
                g2 = (Graphics2D) g2.create();
                g2.setColor(Color.white);
                g2.setFont(font);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.drawString("lightweight overlay", 100, 200);
                g2.dispose();
            }
        };

        mediaPlayerComponent.getMediaPlayer().overlay().enableOverlay(true);

        frame.setBackground(Color.black);
        frame.setContentPane(mediaPlayerComponent);

        frame.setLocation(100, 100);
        frame.pack();
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
        mediaPlayerComponent.getMediaPlayer().media().playMedia(mrl);
    }

}
