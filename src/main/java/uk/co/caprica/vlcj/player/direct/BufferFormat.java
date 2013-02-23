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

package uk.co.caprica.vlcj.player.direct;

import java.util.Arrays;

/**
 * Specifies the formats used by the {@link DirectMediaPlayer}.
 * <p>
 * The buffer will contain data of the given width and height in the format specified by the chroma
 * parameter. A buffer can consist of multiple planes depending on the format of the data. For each
 * plane the pitch and height in lines must be supplied.
 * <p>
 * For example, RV32 format has only one plane. Its pitch is width * 4, and its number of lines is
 * the same as the height.
 */
public class BufferFormat {

    /**
     * Chroma (pixel colour format).
     */
    private final String chroma;

    /**
     * Pixel width of the video.
     */
    private final int width;

    /**
     * Pixel height of the video.
     */
    private final int height;

    /**
     * Pitch size for each plane.
     */
    private final int[] pitches;

    /**
     * Number of lines in each plane.
     */
    private final int[] lines;

    /**
     * Constructs a new BufferFormat instance with the given parameters.
     *
     * @param chroma a VLC buffer type, must be exactly 4 characters and cannot contain non-ASCII characters
     * @param width the width, must be > 0
     * @param height the height, must be > 0
     * @param pitches the pitch of each plane that this buffer consists of (usually a multiple of width)
     * @param lines the number of lines of each plane that this buffer consists of (usually same as height)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public BufferFormat(String chroma, int width, int height, int[] pitches, int[] lines) {
        validate(chroma, width, height, pitches, lines);
        this.chroma = chroma;
        this.width = width;
        this.height = height;
        this.pitches = Arrays.copyOf(pitches, pitches.length);
        this.lines = Arrays.copyOf(lines, lines.length);
    }

    /**
     * Get the pixel format.
     *
     * @return pixel format
     */
    public final String getChroma() {
        return chroma;
    }

    /**
     * Get the width.
     *
     * @return width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Get the height.
     *
     * @return height
     */
    public final int getHeight() {
        return height;
    }

    /**
     * Get the pitches for each plane.
     *
     * @return pitches
     */
    public final int[] getPitches() {
        return pitches;
    }

    /**
     * Get the number of lines for each plane.
     *
     * @return lines
     */
    public final int[] getLines() {
        return lines;
    }

    /**
     * Get the number of planes in the buffer.
     *
     * @return number of planes
     */
    public final int getPlaneCount() {
        return pitches.length;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("chroma=").append(chroma).append(',');
        sb.append("width=").append(width).append(',');
        sb.append("height=").append(height).append(',');
        sb.append("pitches=").append(Arrays.toString(pitches)).append(',');
        sb.append("lines=").append(Arrays.toString(lines)).append(']');
        return sb.toString();
    }

    /**
     * Validate the buffer format.
     * <p>
     * Incorrect parameter values can cause fatal crashes, so all are checked here
     * to mitigate.
     *
     * @param chroma
     * @param width
     * @param height
     * @param pitches
     * @param lines
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validate(String chroma, int width, int height, int[] pitches, int[] lines) {
        if(chroma == null || chroma.length() != 4) {
            throw new IllegalArgumentException("chroma must be exactly 4 characters");
        }
        if(width <= 0) {
            throw new IllegalArgumentException("width must be greater than zero");
        }
        if(height <= 0) {
            throw new IllegalArgumentException("height must be greater than zero");
        }
        if(pitches == null || pitches.length == 0) {
            throw new IllegalArgumentException("pitches length must be greater than zero");
        }
        if(lines == null || lines.length == 0) {
            throw new IllegalArgumentException("lines length must be greater than zero");
        }
        if(pitches.length != lines.length) {
            throw new IllegalArgumentException("pitches and lines must have equal length");
        }
        for(int i = 0; i < pitches.length; i++) {
            if(pitches[i] <= 0) {
                throw new IllegalArgumentException("pitch must be greater than zero");
            }
            if(lines[i] <= 0) {
                throw new IllegalArgumentException("line must be greater than zero");
            }
        }
    }
}
