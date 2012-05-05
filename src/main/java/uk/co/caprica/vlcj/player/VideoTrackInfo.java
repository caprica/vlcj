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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

/**
 * Video track info.
 */
public class VideoTrackInfo extends TrackInfo {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Video width.
     */
    private final int width;

    /**
     * Video height.
     */
    private final int height;

    /**
     * Create a new video track info.
     * 
     * @param codec video codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param width width
     * @param height height
     */
    protected VideoTrackInfo(int codec, int id, int profile, int level, int width, int height) {
        super(codec, id, profile, level);
        this.width = width;
        this.height = height;
    }

    /**
     * Get the video width.
     * 
     * @return width
     */
    public int width() {
        return width;
    }

    /**
     * Get the video height.
     * 
     * @return height
     */
    public int height() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("width=").append(width).append(',');
        sb.append("height=").append(height).append(']');
        return sb.toString();
    }
}
