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
import uk.co.caprica.vlcj.binding.internal.libvlc_media_tracklist_t;

final public class AudioTrackList extends TrackList<AudioTrack> {

    public AudioTrackList(libvlc_media_tracklist_t instance) {
        super(instance);
    }

    @Override
    protected AudioTrack initTrack(libvlc_media_track_t trackInstance) {
        return new AudioTrack(trackInstance);
    }
}
