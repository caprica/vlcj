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

/**
 * Enumeration of track types.
 */
public enum TrackType {

    /**
     * Unknown.
     */
    UNKNOWN (-1),

    /**
     * Audio track.
     */
    AUDIO   ( 0),

    /**
     * Video track.
     */
    VIDEO   ( 1),

    /**
     * Text track.
     * <p>
     * Also known as "subtitle" or "sub-picture".
     */
    TEXT    ( 2);

    /**
     * Raw value.
     */
    private final int value;

    /**
     * Raw value.
     *
     * @param value raw value
     */
    private TrackType(int value) {
        this.value = value;
    }

    /**
     * Get the raw value.
     *
     * @return raw value
     */
    public int value() {
        return value;
    }
}
