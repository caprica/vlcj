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

package uk.co.caprica.vlcj.test.snapshot;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.test.thumbs.ThumbsTest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Test the various snapshot functions.
 * <p>
 * Note that you do <em>not</em> need the video to be displayed to capture snapshots, see
 * {@link ThumbsTest}.
 */
public class SnapshotTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        args = new String[] {"/home/mark/1.mp4"};

        if(args.length != 1) {
            System.err.println("Specify an MRL");
            System.exit(1);
        }

        MediaPlayerFactory factory = new MediaPlayerFactory();
        MediaPlayer mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("snapshotTaken(filename=" + filename + ")");
            }
        });

        mediaPlayer.media().play(args[0]);

        mediaPlayer.controls().setPosition(0.25f);
        Thread.sleep(1000); // Don't do this, use events instead

        // I might get around to this some day
        // mediaPlayer.saveSnapshot();
        // mediaPlayer.saveSnapshot(200, 300);

        File file3 = new File("vlcj-snapshot1.png");
        file3.deleteOnExit();
        mediaPlayer.snapshots().save(file3);
        BufferedImage image3 = ImageIO.read(file3);

        File file4 = new File("vlcj-snapshot2.png");
        file4.deleteOnExit();
        mediaPlayer.snapshots().save(file4, 500, 0);
        BufferedImage image4 = ImageIO.read(file4);

        BufferedImage image5 = mediaPlayer.snapshots().get();
        BufferedImage image6 = mediaPlayer.snapshots().get(300, 600);

        show("Named file saveSnapshot", image3, 3);
        show("Named file sized saveSnapshot", image4, 4);
        show("Image getSnapshot", image5, 5);
        show("Image sized getSnapshot", image6, 6);

        mediaPlayer.controls().stop();
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
