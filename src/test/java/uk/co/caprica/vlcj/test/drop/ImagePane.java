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

package uk.co.caprica.vlcj.test.drop;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * A panel that paints a background image.
 */
public class ImagePane extends JComponent {

    private static final long serialVersionUID = 1L;

    public enum Mode {
        DEFAULT,
        CENTER,
        FIT
    }

    private final Mode mode;
    private BufferedImage sourceImage;
    private final float opacity;

    private BufferedImage image;

    private int lastWidth;
    private int lastHeight;

    public ImagePane(Mode mode, URL imageUrl, float opacity) {
        this.mode = mode;
        this.opacity = opacity;
        newImage(imageUrl);
    }

    public void setImage(URL imageUrl) {
        newImage(imageUrl);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return image != null ? new Dimension(image.getWidth(), image.getHeight()) : super.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        prepareImage();

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.black);
        g2.fill(getBounds());

        if(image != null) {
            int x = 0;
            int y = 0;

            if(mode != Mode.DEFAULT) {
                x = (getWidth() - image.getWidth()) / 2;
                y = (getHeight() - image.getHeight()) / 2;
            }

            Composite oldComposite = g2.getComposite();

            if(opacity != 1.0f) {
                g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
            }
            g2.drawImage(image, null, x, y);

            g2.setComposite(oldComposite);
        }
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    private void newImage(URL imageUrl) {
        image = null;
        if(imageUrl != null) {
            try {
                sourceImage = ImageIO.read(imageUrl);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareImage() {
        if(lastWidth != getWidth() || lastHeight != getHeight()) {
            lastWidth = getWidth();
            lastHeight = getHeight();
            if(sourceImage != null) {
                switch(mode) {
                    case DEFAULT:
                    case CENTER:
                        image = sourceImage;
                        break;

                    case FIT:
                        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2 = image.createGraphics();
                        AffineTransform at = AffineTransform.getScaleInstance((double)image.getWidth() / sourceImage.getWidth(), (double)image.getHeight() / sourceImage.getHeight());
                        g2.drawRenderedImage(sourceImage, at);
                        g2.dispose();
                        break;
                }
            }
            else {
                image = null;
            }
        }
    }
}
