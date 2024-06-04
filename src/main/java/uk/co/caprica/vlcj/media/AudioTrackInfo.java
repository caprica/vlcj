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
 * Audio track info.
 */
public final class AudioTrackInfo extends TrackInfo {

    /**
     * Number of audio channels.
     */
    private final int channels;

    /**
     * Rate.
     */
    private final int rate;

    /**
     * Create a new audio track info.
     *
     * @param codec audio codec
     * @param originalCodec original audio codec
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
     * @param channels number of channels
     * @param rate rate
     * @param codecDescription codec description
     */
    public AudioTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, String trackId, boolean idStable, String name, boolean selected, int channels, int rate, String codecDescription) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description, trackId, idStable, name, selected, codecDescription);
        this.channels = channels;
        this.rate = rate;
    }

    /**
     * Get the number of channels.
     *
     * @return channel count
     */
    public int channels() {
        return channels;
    }

    /**
     * Get the rate.
     *
     * @return rate
     */
    public int rate() {
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
