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

package uk.co.caprica.vlcj.test.meta;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

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

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        // Create a media player
        MediaPlayerFactory factory = new MediaPlayerFactory();

        // Create media
        final Media media = factory.media().newMedia(args[0]);

        // Parsing is asynchronous, we use a conditional waiter to parse the media and wait for it to finish parsing
        ParsedWaiter parsed = new ParsedWaiter(media) {
            @Override
            protected boolean onBefore(Media component) {
                return media.parsing().parse();
            }
        };
        parsed.await();

        // Get the meta data and dump it out
        MetaData metaData = media.meta().asMetaData();
        System.out.println(metaData);

        // Load the artwork into a buffered image (if available)
        String artworkUrl = metaData.get(Meta.ARTWORK_URL);
        System.out.println(artworkUrl);

        // Orderly clean-up
        media.release();
        factory.release();

        if (artworkUrl != null) {
            final BufferedImage artwork = ImageIO.read(new URL(artworkUrl));

            JPanel cp = new JPanel() {
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
