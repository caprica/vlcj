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

package uk.co.caprica.vlcj.player;

import java.io.Serializable;

/**
 * Description of a track, e.g. a video or audio track.
 */
public class TrackDescription implements Serializable {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identifier.
     */
    private final int id;

    /**
     * Description.
     */
    private final String description;

    /**
     * Create a track description.
     *
     * @param id track identifier
     * @param description track description
     */
    public TrackDescription(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Get the track identifier
     *
     * @return identifier
     */
    public int id() {
        return id;
    }

    /**
     * Get the track description
     *
     * @return description
     */
    public String description() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("id=").append(id).append(',');
        sb.append("description=").append(description).append(']');
        return sb.toString();
    }
}
