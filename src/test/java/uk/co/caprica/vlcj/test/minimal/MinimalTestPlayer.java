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

package uk.co.caprica.vlcj.test.minimal;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An absolute minimum test player.
 */
public class MinimalTestPlayer extends VlcjTest {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify an MRL to play");
            System.exit(1);
        }

        final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        JFrame f = new JFrame("Test Player");
        f.setIconImage(new ImageIcon(MinimalTestPlayer.class.getResource("/icons/vlcj-logo.png")).getImage());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
            }
        });
        f.setContentPane(mediaPlayerComponent);
        f.setVisible(true);

        mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);

        Thread.currentThread().join();
    }
}
