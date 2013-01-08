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

package uk.co.caprica.vlcj.test.snapshot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.test.thumbs.ThumbsTest;

/**
 * Test the various snapshot functions.
 * <p>
 * Note that you do <em>not</em> need the video to be displayed to capture snapshots, see
 * {@link ThumbsTest}.
 */
public class SnapshotTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("snapshotTaken(filename=" + filename + ")");
            }
        });

        mediaPlayer.startMedia(args[0]);

        mediaPlayer.setPosition(0.25f);
        Thread.sleep(1000); // Don't do this, use events instead

        // I might get around to this some day
        // mediaPlayer.saveSnapshot();
        // mediaPlayer.saveSnapshot(200, 300);

        File file3 = new File("vlcj-snapshot1.png");
        file3.deleteOnExit();
        mediaPlayer.saveSnapshot(file3);
        BufferedImage image3 = ImageIO.read(file3);

        File file4 = new File("vlcj-snapshot2.png");
        file4.deleteOnExit();
        mediaPlayer.saveSnapshot(file4, 500, 0);
        BufferedImage image4 = ImageIO.read(file4);

        BufferedImage image5 = mediaPlayer.getSnapshot();
        BufferedImage image6 = mediaPlayer.getSnapshot(300, 600);

        show("Named file saveSnapshot", image3, 3);
        show("Named file sized saveSnapshot", image4, 4);
        show("Image getSnapshot", image5, 5);
        show("Image sized getSnapshot", image6, 6);

        mediaPlayer.stop();
    }

    @SuppressWarnings("serial")
    private static void show(String title, final BufferedImage img, int i) {
        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new JPanel() {
            @Override
            protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(img, null, 0, 0);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(img.getWidth(), img.getHeight());
            }
        });
        f.pack();
        f.setLocation(50 + (i * 50), 50 + (i * 50));
        f.setVisible(true);
    }
}
