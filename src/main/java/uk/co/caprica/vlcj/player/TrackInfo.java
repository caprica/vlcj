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
     * Original codec (fourcc).
     */
    private final int originalCodec;

    /**
     * Original codec name.
     */
    private final String originalCodecName;

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
     * Bit rate.
     */
    private final int bitRate;

    /**
     * Language code, e.g. "en", if available.
     */
    private final String language;

    /**
     * Track description, if available.
     */
    private final String description;

    /**
     * Create a new track info.
     *
     * @param codec codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     */
    protected TrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description) {
        this.codec = codec;
        this.codecName = codecName(codec);
        this.originalCodec = originalCodec;
        this.originalCodecName = codecName(originalCodec);
        this.id = id;
        this.profile = profile;
        this.level = level;
        this.bitRate = bitRate;
        this.language = language;
        this.description = description;
    }

    /**
     * Get the codec (fourcc).
     *
     * @return codec
     */
    public final int codec() {
        return codec;
    }

    /**
     * Get the codec name.
     *
     * @return codec name
     */
    public final String codecName() {
        return codecName;
    }

    /**
     * Get the original codec (fourcc).
     *
     * @return original codec
     */
    public final int orignalCodec() {
        return codec;
    }

    /**
     * Get the original codec name.
     *
     * @return original codec name
     */
    public final String originalCodecName() {
        return originalCodecName;
    }

    /**
     * Get the track id.
     *
     * @return track id
     */
    public final int id() {
        return id;
    }

    /**
     * Get the profile.
     *
     * @return profile
     */
    public final int profile() {
        return profile;
    }

    /**
     * Get the level.
     *
     * @return level
     */
    public final int level() {
        return level;
    }

    /**
     * Get the bit-rate.
     *
     * @return bit-rate
     */
    public final int bitRate() {
        return bitRate;
    }

    /**
     * Get the language.
     *
     * @return language
     */
    public final String language() {
        return language;
    }

    /**
     * Get the description.
     *
     * @return description
     */
    public final String description() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("codec=0x").append(Integer.toHexString(codec)).append(',');
        sb.append("codecName=").append(codecName).append(',');
        sb.append("originalCodec=0x").append(Integer.toHexString(originalCodec)).append(',');
        sb.append("originalCodecName=").append(originalCodecName).append(',');
        sb.append("id=").append(id).append(',');
        sb.append("profile=").append(profile).append(',');
        sb.append("level=").append(level).append(',');
        sb.append("bitRate=").append(bitRate).append(',');
        sb.append("language=").append(language).append(',');
        sb.append("description=").append(description).append(']');
        return sb.toString();
    }

    /**
     * Make a readable string from a codec identifier.
     *
     * @param codec codec identifier
     * @return string representation of the codec identifier
     */
    private final String codecName(int codec) {
        return codec != 0 ? new String(new byte[] {(byte)codec, (byte)(codec >>> 8), (byte)(codec >>> 16), (byte)(codec >>> 24)}).trim() : null;
    }
}
