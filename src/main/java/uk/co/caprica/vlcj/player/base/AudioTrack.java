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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.media.TrackType;

final public class AudioTrack extends Track {

    private final int channels;

    private final int rate;

    AudioTrack(libvlc_media_track_t instance) {
        super(TrackType.AUDIO, instance);
        instance.u.setType(libvlc_audio_track_t.class);
        instance.u.read();
        this.channels = instance.u.audio.i_channels;
        this.rate = instance.u.audio.i_rate;
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
