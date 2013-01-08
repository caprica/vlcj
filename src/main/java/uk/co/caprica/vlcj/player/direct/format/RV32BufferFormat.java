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

package uk.co.caprica.vlcj.player.direct.format;

import uk.co.caprica.vlcj.player.direct.BufferFormat;

/**
 * Implementation of a buffer format for RV32.
 * <p>
 * RV32 is a 24-bit BGR format with 8-bit of padding (no alpha) in a single plane.
 */
public class RV32BufferFormat extends BufferFormat {

    /**
     * Creates a RV32 buffer format with the given width and height.
     *
     * @param width width of the buffer
     * @param height height of the buffer
     */
    public RV32BufferFormat(int width, int height) {
        super("RV32", width, height, new int[] {width * 4}, new int[] {height});
    }
}
