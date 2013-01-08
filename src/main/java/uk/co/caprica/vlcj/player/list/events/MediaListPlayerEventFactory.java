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

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.media_duration_changed;
import uk.co.caprica.vlcj.binding.internal.media_list_player_next_item_set;
import uk.co.caprica.vlcj.binding.internal.media_meta_changed;
import uk.co.caprica.vlcj.binding.internal.media_parsed_changed;
import uk.co.caprica.vlcj.binding.internal.media_state_changed;
import uk.co.caprica.vlcj.binding.internal.media_subitem_added;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

/**
 * A factory that creates a media player event instance for a native media list player event.
 */
public class MediaListPlayerEventFactory {

    /**
     * Media player to create events for.
     */
    private final MediaListPlayer mediaListPlayer;

    /**
     * Create a media player event factory.
     *
     * @param mediaPlayer media list player to create events for
     */
    public MediaListPlayerEventFactory(MediaListPlayer mediaPlayer) {
        this.mediaListPlayer = mediaPlayer;
    }

    /**
     * Create a new media player event for a given native event.
     *
     * @param event native event
     * @param eventMask bit mask of enabled events (i.e. events to send notifications for)
     * @return media player event, or <code>null</code> if the native event type is not enabled or otherwise could not be handled
     */
    public MediaListPlayerEvent newMediaListPlayerEvent(libvlc_event_t event, int eventMask) {
        // Create an event suitable for the native event type...
        MediaListPlayerEvent result = null;
        switch(libvlc_event_e.event(event.type)) {

        // === Events relating to the media list player =========================

            case libvlc_MediaListPlayerNextItemSet:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_CHANGED)) {
                    libvlc_media_t media = ((media_list_player_next_item_set)event.u.getTypedValue(media_list_player_next_item_set.class)).item;
                    result = new MediaListPlayerNextItemSetEvent(mediaListPlayer, media, mediaListPlayer.mrl(media));
                }
                break;

            // === Events relating to the current media =============================

            case libvlc_MediaMetaChanged:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_META_CHANGED)) {
                    result = new MediaListMediaMetaChangedEvent(mediaListPlayer, ((media_meta_changed)event.u.getTypedValue(media_meta_changed.class)).meta_type);
                }
                break;

            case libvlc_MediaSubItemAdded:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_SUB_ITEM_ADDED)) {
                    result = new MediaListMediaSubItemAddedEvent(mediaListPlayer, ((media_subitem_added)event.u.getTypedValue(media_subitem_added.class)).new_child);
                }
                break;

            case libvlc_MediaDurationChanged:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_DURATION_CHANGED)) {
                    result = new MediaListMediaDurationChangedEvent(mediaListPlayer, ((media_duration_changed)event.u.getTypedValue(media_duration_changed.class)).new_duration);
                }
                break;

            case libvlc_MediaParsedChanged:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_PARSED_CHANGED)) {
                    result = new MediaListMediaParsedChangedEvent(mediaListPlayer, ((media_parsed_changed)event.u.getTypedValue(media_parsed_changed.class)).new_status);
                }
                break;

            case libvlc_MediaFreed:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_FREED)) {
                    result = new MediaListMediaFreedEvent(mediaListPlayer);
                }
                break;

            case libvlc_MediaStateChanged:
                if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_STATE_CHANGED)) {
                    result = new MediaListMediaStateChangedEvent(mediaListPlayer, ((media_state_changed)event.u.getTypedValue(media_state_changed.class)).new_state);
                }
                break;
        }
        return result;
    }
}
