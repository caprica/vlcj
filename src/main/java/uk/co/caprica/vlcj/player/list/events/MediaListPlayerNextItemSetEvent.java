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

package uk.co.caprica.vlcj.player.list.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 *
 */
class MediaListPlayerNextItemSetEvent extends AbstractMediaListPlayerEvent {

    /**
     * Media instance.
     */
    private final libvlc_media_t item;

    /**
     * Media resource locator.
     */
    private final String mrl;

    /**
     * Create a media player event.
     *
     * @param mediaListPlayer media player the event relates to
     * @param metaType meta data type
     * @param mrl media resource locator
     */
    MediaListPlayerNextItemSetEvent(MediaListPlayer mediaListPlayer, libvlc_media_t item, String mrl) {
        super(mediaListPlayer);
        this.item = item;
        this.mrl = mrl;
    }

    @Override
    public void notify(MediaListPlayerEventListener listener) {
        listener.nextItem(mediaListPlayer, item, mrl);
    }
}
