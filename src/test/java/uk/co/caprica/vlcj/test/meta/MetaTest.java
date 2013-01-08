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

package uk.co.caprica.vlcj.test.meta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test to show local file meta data.
 * <p>
 * Specify a single local media file as the first (and only) command-line argument.
 * <p>
 * An interesting feature of vlc is that if artwork associated with the media can be obtained, the
 * <code>ARTWORKURL</code> meta data field will point to a valid <em>local</em> file for that
 * artwork.
 * <p>
 * If the artwork is available, this test opens a frame to display it.
 */
public class MetaTest extends VlcjTest {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Get the meta data and dump it out
        MediaMeta mediaMeta = factory.getMediaMeta(args[0], true);
        Logger.debug("mediaMeta={}", mediaMeta);

        // Load the artwork into a buffered image (if available)
        final BufferedImage artwork = mediaMeta.getArtwork();
        System.out.println(artwork);

        // Orderly clean-up
        mediaMeta.release();
        factory.release();

        if(artwork != null) {
            JPanel cp = new JPanel() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setPaint(Color.black);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    double sx = (double)getWidth() / (double)artwork.getWidth();
                    double sy = (double)getHeight() / (double)artwork.getHeight();
                    sx = Math.min(sx, sy);
                    sy = Math.min(sx, sy);
                    AffineTransform tx = AffineTransform.getScaleInstance(sx, sy);
                    g2.drawImage(artwork, new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR), 0, 0);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(artwork.getWidth(), artwork.getHeight());
                }
            };
            JFrame f = new JFrame("vlcj meta artwork");
            f.setIconImage(new ImageIcon(MetaTest.class.getResource("/icons/vlcj-logo.png")).getImage());
            f.setContentPane(cp);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setVisible(true);
        }
    }
}
