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

package uk.co.caprica.vlcj.media.events;

import uk.co.caprica.vlcj.enums.MediaParsedStatus;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.enums.State;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.model.Picture;

public class MediaEventAdapter implements MediaEventListener {

    @Override
    public void mediaMetaChanged(Media media, int metaType) {
    }

    @Override
    public void mediaSubItemAdded(Media media, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(Media media, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
    }

    @Override
    public void mediaFreed(Media media) {
    }

    @Override
    public void mediaStateChanged(Media media, State newState) {
    }

    @Override
    public void mediaSubItemTreeAdded(Media media, libvlc_media_t item) {
    }

    @Override
    public void mediaThumbnailGenerated(Media media, Picture picture) {
    }

}
