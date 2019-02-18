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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media;

/**
 * Text track info.
 * <p>
 * "Text" includes subtitles, sub-pictures and so on.
 */
public final class TextTrackInfo extends TrackInfo {

    /**
     * Encoding.
     */
    private final String encoding;

    /**
     * Create a new SPU (subtitle) track info.
     *
     * @param codec codec
     * @param originalCodec original codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     * @param encoding encoding
     * @param codecDescription codec description
     */
    public TextTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, String encoding, String codecDescription) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description, codecDescription);
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
