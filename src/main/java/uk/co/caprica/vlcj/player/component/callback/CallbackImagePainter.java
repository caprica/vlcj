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

import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Specification for a component that paints a video frame in the {@link CallbackMediaPlayerComponent}.
 */
public interface CallbackImagePainter {

    /**
     * Prepare the drawing context.
     * <p>
     * This is intended to set context attributes like {@link RenderingHints} before painting the image itself.
     *
     * @param g2 graphics context
     * @param component component to paint
     */
    void prepare(Graphics2D g2, JComponent component);

    /**
     * Paint the image.
     *
     * @param g2 graphics context
     * @param component component to paint
     * @param image image to paint
     */
    void paint(Graphics2D g2, JComponent component, BufferedImage image);

}
