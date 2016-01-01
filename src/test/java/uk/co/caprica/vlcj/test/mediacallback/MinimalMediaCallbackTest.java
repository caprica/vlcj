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

package uk.co.caprica.vlcj.test.mediacallback;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.seekable.RandomAccessFileMedia;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.test.minimal.MinimalTestPlayer;

/**
 * Minimal test for the media callbacks.
 */
public class MinimalMediaCallbackTest extends VlcjTest {

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
                mediaPlayerComponent.release(true);
            }
        });
        f.setContentPane(mediaPlayerComponent);
        f.setVisible(true);

        Media media = new RandomAccessFileMedia(new File(args[0]));

        mediaPlayerComponent.getMediaPlayer().playMedia(media);

        Thread.currentThread().join();
    }
}
