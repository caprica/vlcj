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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Default implementation of the media player event listener.
 * <p>
 * Simply override the methods you're interested in.
 */
public class MediaListPlayerEventAdapter implements MediaListPlayerEventListener {

    // === Events relating to the media player ==================================

    @Override
    public void played(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
    }

    // === Events relating to the current media =================================

    @Override
    public void mediaMetaChanged(MediaListPlayer mediaListPlayer, int metaType) {
    }

    @Override
    public void mediaSubItemAdded(MediaListPlayer mediaListPlayer, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(MediaListPlayer mediaListPlayer, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(MediaListPlayer mediaListPlayer, int newStatus) {
    }

    @Override
    public void mediaFreed(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void mediaStateChanged(MediaListPlayer mediaListPlayer, int newState) {
    }
}
