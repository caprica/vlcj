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

package uk.co.caprica.vlcj.player.embedded.videosurface.videoengine;

import uk.co.caprica.vlcj.player.base.MouseButton;

/**
 * Specification for a component that informs the video engine when the size of the hosted video window changes or a
 * mouse event occurs.
 */
public interface VideoEngineWindowCallback {

    /**
     * Set the new window size.
     *
     * @param width new width
     * @param height new height
     */
    void setSize(int width, int height);

    /**
     * Report the new mouse position.
     *
     * @param x new x position
     * @param y new y position
     */
    void mouseMoved(int x, int y);

    /**
     * Report mouse pressed.
     *
     * @param mouseButton button that was pressed
     */
    void mousePressed(MouseButton mouseButton);

    /**
     * Report mouse released.
     *
     * @param mouseButton button that was released
     */
    void mouseReleased(MouseButton mouseButton);
}
