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

        new CallbackMediaPlayerComponentTest().start(mrl);
    }

    /**
     * Create a new test.
     */
    private CallbackMediaPlayerComponentTest() {
        final JFrame frame = new JFrame("vlcj Callback Media Player Component Test");

        final Font font = new Font("Sansserif", Font.BOLD, 36);

        mediaPlayerComponent = new CallbackMediaPlayerComponent(null, null, null, true, null, null, null, null) {

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                final Dimension size = mediaPlayer.video().videoDimension();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.getContentPane().setPreferredSize(size);
                        frame.getContentPane().invalidate();
                        frame.pack();
                    }
                });
            }

            @Override
            protected void onPaintOverlay(Graphics2D g2) {
                g2.setColor(Color.white);
                g2.setFont(font);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.drawString("lightweight overlay", 100, 200);
            }

        };

        mediaPlayerComponent.mediaPlayer().overlay().enable(true);

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
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }

}
