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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.fullscreen;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple full-screen test.
 */
public class FullScreenTest extends VlcjTest {

    public static void main(final String[] args) {
        if(args.length != 1) {
            System.err.println("Specify a single MRL");
            System.exit(1);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FullScreenTest(args);
            }
        });
    }

    public FullScreenTest(String[] args) {
        Canvas c = new Canvas();
        c.setBackground(Color.black);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(c, BorderLayout.CENTER);

        final JFrame f = new JFrame("VLCJ");
        f.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);
        // f.setUndecorated(true);

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new FullScreenStrategy() {

            @Override
            public void enterFullScreenMode() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // f.dispose();
                        // f.setUndecorated(true);
                        // f.setBounds(0, 0, 1920, 1080);
                        f.toFront();
                        f.setVisible(true);
                    }
                });
            }

            @Override
            public void exitFullScreenMode() {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isFullScreenMode() {
                // TODO Auto-generated method stub
                return false;
            }
        });

        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

        f.setVisible(true);

        mediaPlayer.setFullScreen(true);
        mediaPlayer.startMedia(args[0]);
    }
}
