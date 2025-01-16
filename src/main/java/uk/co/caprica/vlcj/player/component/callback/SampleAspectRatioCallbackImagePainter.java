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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.component.callback;

import uk.co.caprica.vlcj.player.base.VideoTrack;

import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Implementation of a painter that scales to fit the container while preserving the original aspect ratio, and also
 * taking into account the Sample Aspect Ratio (SAR) reported for the selected video track.
 * <p>
 * In most cases the SAR is 1:1 (square pixels), but for some media types (e.g. DVD) the SAR may be something like 64:45
 * (rectangular pixels, wider than they are tall). Using the normal {@link ScaledCallbackImagePainter} in those cases
 * would result in a horizontally squashed image.
 * <p>
 * Note that the SAR may not be available immediately when the media starts playing, and this implementation will not
 * paint frames until a valid SAR is notified so a small number of frames may be skipped - in practice no actual video
 * frames appear to be missed or rendered with the wrong/default SAR so this concern may be moot.
 * <p>
 * The default implementation uses bilinear interpolation when painting the scaled image.
 * <p>
 * The aspect ratio of the original image is preserved when scaling is applied.
 */
public class SampleAspectRatioCallbackImagePainter extends BaseCallbackImagePainter {

    /**
     * Current sample aspect ratio (SAR).
     * <p>
     * May be <code>null</code>.
     */
    private volatile AspectRatio sar;

    @Override
    public void videoTrackChanged(VideoTrack videoTrack) {
        if (videoTrack != null) {
            // Use 1:1 if a valid SAR is not available for some reason
            sar = new AspectRatio(Math.max(1, videoTrack.sampleAspectRatio()), Math.max(videoTrack.sampleAspectRatioBase(), 1));
        } else {
            sar = null;
        }
    }

    @Override
    public void prepare(Graphics2D g2, JComponent component) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    @Override
    public void paint(Graphics2D g2, JComponent component, BufferedImage image) {
        int width = component.getWidth();
        int height = component.getHeight();

        g2.setColor(component.getBackground());
        g2.fillRect(0, 0, width, height);

        if (image != null && sar != null) {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            int adjustedImageWidth = imageWidth * sar.numerator / sar.denominator;

            float sx = (float) width / adjustedImageWidth;
            float sy = (float) height / imageHeight;

            float sf = Math.min(sx, sy);

            float scaledW = adjustedImageWidth * sf;
            float scaledH = imageHeight * sf;

            g2.translate(
                (width - scaledW) / 2,
                (height - scaledH) / 2
            );

            // Stash the transform before scaling
            AffineTransform savedTransform = g2.getTransform();

            // Set the scale to render the image, compensating for the SAR
            g2.scale(sf * sar.numerator / sar.denominator, sf);

            g2.drawImage(image, null, 0, 0);

            // Restore the previously stashed transform, and re-scale without SAR to render the overlay (otherwise the
            // overlay would appear stretched)
            g2.setTransform(savedTransform);
            g2.scale(sf, sf);
        }
    }

    /**
     * Immutable value object class for an aspect ratio.
     */
    private static class AspectRatio {
        private final int numerator;
        private final int denominator;

        private AspectRatio(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }
}
