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

package uk.co.caprica.vlcj.player.component.callback;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Implementation of a painter that scales to fit the container while preserving the original aspect ratio.
 * <p>
 * The default implementation uses bilinear interpolation when painting the scaled image.
 * <p>
 * The aspect ratio of the original image is preserved when scaling is applied.
 */
public class ScaledCallbackImagePainter implements CallbackImagePainter {

    private int lastWidth;
    private int lastHeight;

    private AffineTransform transform;

    @Override
    public void prepare(Graphics2D g2, JComponent component) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    @Override
    public void paint(Graphics2D g2, JComponent component, BufferedImage image) {
        if (image != null) {
            int width = component.getWidth();
            int height = component.getHeight();

            if (width != lastWidth || height != lastHeight) {
                lastWidth = width;
                lastHeight = height;

                float sx = (float) width / image.getWidth();
                float sy = (float) height / image.getHeight();
                float sf = Math.min(sx, sy);
                float scaledW = image.getWidth() * sf;
                float scaledH = image.getHeight() * sf;
                float offsetX = (width - scaledW) / 2;
                float offsetY = (height - scaledH) / 2;

                transform = AffineTransform.getTranslateInstance(offsetX, offsetY);
                if (sf != 1.0) {
                    transform.concatenate(AffineTransform.getScaleInstance(sf, sf));
                }
            }

            g2.setColor(component.getBackground());
            g2.fillRect(0, 0,width, height);

            g2.setTransform(transform);
            g2.drawImage(image, null, 0, 0);
        }
    }

}
