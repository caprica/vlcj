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
 * Audio track info.
 */
public class AudioTrackInfo extends TrackInfo {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Number of audio channels.
     */
    private final int channels;

    /**
     * Rate.
     */
    private final int rate;

    /**
     * Create a new audio track info
     *
     * @param codec audio codec
     * @param originalCodec original audio codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     * @param channels number of channels
     * @param rate rate
     */
    protected AudioTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, int channels, int rate) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description);
        this.channels = channels;
        this.rate = rate;
    }

    /**
     * Get the number of channels.
     *
     * @return channel count
     */
    public final int channels() {
        return channels;
    }

    /**
     * Get the rate.
     *
     * @return rate
     */
    public final int rate() {
        return rate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("channels=").append(channels).append(',');
        sb.append("rate=").append(rate).append(']');
        return sb.toString();
    }
}
