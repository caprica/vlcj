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

package uk.co.caprica.vlcj.test.fullscreen;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;

/**
 * Simple full-screen test.
 */
public class FullScreenTest extends VlcjTest {

    private static FullScreenTest app;

    private static MediaPlayerFactory mediaPlayerFactory;

    private static EmbeddedMediaPlayer mediaPlayer;

    public static void main(final String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Specify a single MRL");
            System.exit(1);
        }

        app = new FullScreenTest(args);

        Thread.currentThread().join();
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

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.fullScreen().strategy(new AdaptiveFullScreenStrategy(f));

        mediaPlayer.videoSurface().set(mediaPlayerFactory.videoSurfaces().newVideoSurface(c));

        f.setVisible(true);

        mediaPlayer.fullScreen().set(true);
        mediaPlayer.media().play(args[0]);
    }

}
