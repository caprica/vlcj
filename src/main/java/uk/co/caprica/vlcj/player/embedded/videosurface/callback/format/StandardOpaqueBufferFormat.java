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

package uk.co.caprica.vlcj.player.embedded.videosurface.callback.format;

import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;

/**
 * Implementation of a standard opaque buffer format.
 * <p>
 * RV24 is used, a 24-bit RGB format in a single plane.
 */
public class StandardOpaqueBufferFormat extends BufferFormat {

    /**
     * Creates a RV24 buffer format with the given width and height.
     *
     * @param width width of the buffer
     * @param height height of the buffer
     */
    public StandardOpaqueBufferFormat(int width, int height) {
        super("RV24", width, height, 3);
    }
}
