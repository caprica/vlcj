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

import java.io.Serializable;

/**
 * Base track info.
 */
public abstract class TrackInfo implements Serializable {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Codec (fourcc).
     */
    private final int codec;

    /**
     * Codec name.
     */
    private final String codecName;

    /**
     * Track id.
     */
    private final int id;

    /**
     * Profile.
     */
    private final int profile;

    /**
     * Level.
     */
    private final int level;

    /**
     * Create a new track info.
     * 
     * @param codec codec
     * @param id track id
     * @param profile profile
     * @param level level
     */
    protected TrackInfo(int codec, int id, int profile, int level) {
        this.codec = codec;
        this.codecName = codec != 0 ? new String(new byte[] {(byte)codec, (byte)(codec >>> 8), (byte)(codec >>> 16), (byte)(codec >>> 24)}).trim() : null;
        this.id = id;
        this.profile = profile;
        this.level = level;
    }

    /**
     * Get the codec (fourcc).
     * 
     * @return codec
     */
    public int codec() {
        return codec;
    }

    /**
     * Get the codec name.
     * 
     * @return codec name
     */
    public String codecName() {
        return codecName;
    }

    /**
     * Get the track id.
     * 
     * @return track id
     */
    public int id() {
        return id;
    }

    /**
     * Get the profile.
     * 
     * @return profile
     */
    public int profile() {
        return profile;
    }

    /**
     * Get the level.
     * 
     * @return level
     */
    public int level() {
        return level;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("codec=").append(codec).append(',');
        sb.append("codecName=").append(codecName).append(',');
        sb.append("id=").append(id).append(',');
        sb.append("profile=").append(profile).append(',');
        sb.append("level=").append(level).append(']');
        return sb.toString();
    }
}
