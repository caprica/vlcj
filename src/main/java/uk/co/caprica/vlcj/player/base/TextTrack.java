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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_subtitle_track_t;
import uk.co.caprica.vlcj.media.TrackType;

import static uk.co.caprica.vlcj.binding.support.strings.NativeString.copyNativeString;

final public class TextTrack extends Track {

    private final String encoding;

    TextTrack(libvlc_media_track_t instance) {
        super(TrackType.TEXT, instance);
        instance.u.setType(libvlc_subtitle_track_t.class);
        instance.u.read();
        this.encoding = copyNativeString(instance.u.subtitle.psz_encoding);
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
