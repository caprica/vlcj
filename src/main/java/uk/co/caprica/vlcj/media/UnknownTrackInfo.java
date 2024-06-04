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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

/**
 * Unknown track info.
 */
public final class UnknownTrackInfo extends TrackInfo {

    /**
     * Create an unknown track info.
     *
     * @param codec codec
     * @param originalCodec original codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     * @param trackId textual track identifier
     * @param idStable flag whether the textual track identifier is "stable" or not
     * @param name track name
     * @param selected <code>true</code> if this track is currently selected; <code>false</code> if not
     * @param codecDescription codec description
     */
    public UnknownTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, String trackId, boolean idStable, String name, boolean selected, String codecDescription) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description, trackId, idStable, name, selected, codecDescription);
    }
}
