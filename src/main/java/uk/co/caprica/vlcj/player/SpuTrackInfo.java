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
 * Sub-picture/sub-title track info.
 */
public class SpuTrackInfo extends TrackInfo {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Encoding.
     */
    private final String encoding;

    /**
     * Create a new SPU (subtitle) track info.
     *
     * @param codec codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     * @param encoding encoding
     */
    protected SpuTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, String encoding) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description);
        this.encoding = encoding;
    }

    /**
     * Get the encoding
     *
     * @return encoding
     */
    public final String encoding() {
        return encoding;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("encoding=").append(encoding).append(']');
        return sb.toString();
    }
}
