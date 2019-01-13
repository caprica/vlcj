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

package uk.co.caprica.vlcj.player.events.semantic;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.events.*;

/**
 * A factory that creates a media player event instance for a semantic event.
 * <p>
 * A "semantic" event is one that has no directly associated native event, but is instead a higher level event (like
 * "media player ready" or "sub-item finished", there is no such native event but it can be inferred and is a useful
 * event).
 */
public final class SemanticEventFactory {

    /**
     * Create a new semantic event for new media.
     *
     * @param mediaPlayer@return media player event, or <code>null</code> if the event type is not enabled
     */
    public static MediaPlayerEvent createMediaNewEvent(MediaPlayer mediaPlayer) {
        return new MediaNewEvent(mediaPlayer);
    }

    /**
     * Create a new semantic event for a played sub-item.
     *
     * @param mediaPlayer
     * @param subItemIndex index of the sub-item that was played
     * @return media player event, or <code>null</code> if the event type is not enabled
     */
    public static MediaPlayerEvent createMediaSubItemPlayedEvent(MediaPlayer mediaPlayer, int subItemIndex) {
        return new MediaSubItemPlayedEvent(mediaPlayer, subItemIndex);
    }

    /**
     * Create a new semantic event for a finished sub-item.
     *
     * @param mediaPlayer
     * @param subItemIndex index of the sub-item that finished playing
     * @return media player event, or <code>null</code> if the event type is not enabled
     */
    public static MediaPlayerEvent createMediaSubItemFinishedEvent(MediaPlayer mediaPlayer, int subItemIndex) {
        return new MediaSubItemFinishedEvent(mediaPlayer, subItemIndex);
    }

    /**
     * Create a new semantic event for the end of the sub-items being reached.
     *
     * @param mediaPlayer@return media player event, or <code>null</code> if the event type is not enabled
     */
    public static MediaPlayerEvent createMediaEndOfSubItemsEvent(MediaPlayer mediaPlayer) {
        return new MediaEndOfSubItemsEvent(mediaPlayer);
    }

    /**
     *
     * @return
     * @param mediaPlayer
     */
    public static MediaPlayerEvent createMediaPlayerReadyEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerReadyEvent(mediaPlayer);
    }

    private SemanticEventFactory() {
    }

}
