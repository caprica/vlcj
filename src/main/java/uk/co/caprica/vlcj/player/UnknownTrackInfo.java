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
 * Unknown track info.
 */
public class UnknownTrackInfo extends TrackInfo {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create an unknown track info.
     *
     * @param codec codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     */
    protected UnknownTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description);
    }
}
