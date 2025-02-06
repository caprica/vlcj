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

import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Implementation of a painter that rotates the drawing context before rendering the video via a delegate painter.
 * <p>
 * The rotation is applied around the center of the component.
 * <p>
 * Note that after rendering the video, this painter does <strong>not</strong> restore the previous affine transform to
 * undo the rotation. This is so that things like overlays could be painted at the same rotation.
 * <p>
 * If this is not desired, then the transform can be reset by <code>g2.setTransform(new AffineTransform());</code> which
 * will reapply the identity transform.
 */
public class RotatedCallbackImagePainter extends BaseCallbackImagePainter {

    /**
     * Painter that renders the video.
     */
    private final CallbackImagePainter delegate;

    /**
     * Angle of rotation, in radians.
     */
    private volatile double angle;

    /**
     * Create a new painter.
     *
     * @param delegate painter that will render the video
     */
    public RotatedCallbackImagePainter(CallbackImagePainter delegate) {
        this(delegate, 0);
    }

    /**
     * Create a new painter.
     *
     * @param delegate painter that will render the video
     * @param initialAngle initial angle of rotation, in radians
     */
    public RotatedCallbackImagePainter(CallbackImagePainter delegate, double initialAngle) {
        this.delegate = delegate;
        this.angle = initialAngle;
    }

    /**
     * Set a new rotation angle.
     *
     * @param angle angle of rotation, in radians
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public void prepare(Graphics2D g2, JComponent component) {
        delegate.prepare(g2, component);
    }

    @Override
    public void paint(Graphics2D g2, JComponent component, BufferedImage image) {
        // Since this painter does not fill the entire rectangle (because of the rotation) it must explicitly fill it
        // with the component background colour before rendering the video
        g2.setColor(component.getBackground());
        g2.fillRect(0, 0, component.getWidth(), component.getHeight());
        g2.rotate(angle, (double) component.getWidth() / 2, (double) component.getHeight() / 2);
        delegate.paint(g2, component, image);
    }
}
