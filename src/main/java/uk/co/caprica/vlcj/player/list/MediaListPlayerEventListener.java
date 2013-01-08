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
 * Specification for a component that is interested in receiving event notifications from the media
 * list player.
 */
public interface MediaListPlayerEventListener {

    // === Events relating to the media list player =============================

    /**
     * Place-holder, do not use.
     *
     * <strong>Warning: the native media list player event manager reports that it does not support
     * this event.</strong>
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void played(MediaListPlayer mediaListPlayer);

    /**
     * The media list player started playing the next item in the list.
     *
     * @param mediaListPlayer media list player that raised the event
     * @param item next item instance
     * @param itemMrl MRL of the next item
     */
    void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl);

    /**
     * The media list player stopped.
     *
     * @param mediaListPlayer media list player that raised the event
     * @since vlc 2.1.0
     */
    void stopped(MediaListPlayer mediaListPlayer);

    // === Events relating to the current media =================================

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     * @param metaType meta data type
     */
    void mediaMetaChanged(MediaListPlayer mediaListPlayer, int metaType);

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     * @param subItem media sub-item instance handle
     */
    void mediaSubItemAdded(MediaListPlayer mediaListPlayer, libvlc_media_t subItem);

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     * @param newDuration
     */
    void mediaDurationChanged(MediaListPlayer mediaListPlayer, long newDuration);

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     * @param newStatus
     */
    void mediaParsedChanged(MediaListPlayer mediaListPlayer, int newStatus);

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void mediaFreed(MediaListPlayer mediaListPlayer);

    /**
     *
     *
     * @param mediaListPlayer media list player that raised the event
     * @param newState
     */
    void mediaStateChanged(MediaListPlayer mediaListPlayer, int newState);
}
