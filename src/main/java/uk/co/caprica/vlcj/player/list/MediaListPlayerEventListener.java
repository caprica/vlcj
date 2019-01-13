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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Specification for a component that is interested in receiving event notifications from the media list player.
 */
public interface MediaListPlayerEventListener {

    /**
     * The media list player finished playing the last item in the list.
     * <p>
     * When the media list player mode is {@link MediaListPlayerMode#DEFAULT}, this event will fire after the last item
     * has been played.
     * <p>
     * When the mode is {@link MediaListPlayerMode#LOOP}, the event will not fire at all.
     * <p>
     * When the mode is {@link MediaListPlayerMode#REPEAT}, the event will fire immediately if the media list player is
     * played (nothing plays in this case); the event will not fire at all if a particular item is played (by index).
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void mediaListPlayerFinished(MediaListPlayer mediaListPlayer);

    /**
     * The media list player started playing the next item in the list.
     *
     * @param mediaListPlayer media list player that raised the event
     * @param item next item instance
     */
    void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item);

    /**
     * The media list player stopped.
     *
     * @param mediaListPlayer media list player that raised the event
     */
    void stopped(MediaListPlayer mediaListPlayer);

}
