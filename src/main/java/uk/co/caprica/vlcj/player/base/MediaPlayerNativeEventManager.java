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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEvent;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;
import uk.co.caprica.vlcj.support.eventmanager.EventNotification;
import uk.co.caprica.vlcj.support.eventmanager.NativeEventManager;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_event_manager;

/**
 * Native event manager implementation for media player events.
 * <p>
 * There is some special case handling in {@link #onCreateEvent(libvlc_instance_t, libvlc_event_t, MediaPlayer)} to deal
 * with side-effects when changing media where we invoke stop on the media list player associated with the media player
 * instance. This occurs in {@link SubitemApi#changeMedia(libvlc_media_t)} and is required for proper operation of media
 * that creates sub-items (e.g. YouTube or similar). Without this stop call, the sub-items would not play properly for
 * such media.
 * <p>
 * The side-effect is that the media player will receive a spurious  media player stopped event, before the media has
 * even started playing. This is unexpected behaviour and is likely to be confusing to client applications.
 * <p>
 * The special case handling itself suppresses this spurious media player stopped event if a playing event has not been
 * received prior. The heuristic resets each time the media changes. This issue is a specific consequence of handling
 * a change in media.
 * <p>
 * This class is the appropriate place for the implementation as this code will always be called on the native media
 * player event thread and will never be invoked in parallel oor by any other thread.
 */
final class MediaPlayerNativeEventManager extends NativeEventManager<MediaPlayer, MediaPlayerEventListener> {

    private boolean receivedPlayingEvent;

    MediaPlayerNativeEventManager(libvlc_instance_t libvlcInstance, MediaPlayer eventObject) {
        super(libvlcInstance, eventObject, libvlc_event_e.libvlc_MediaPlayerMediaChanged, libvlc_event_e.libvlc_MediaPlayerChapterChanged, "media-player-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(MediaPlayer eventObject) {
        return libvlc_media_player_event_manager(eventObject.mediaPlayerInstance());
    }

    @Override
    protected EventNotification<MediaPlayerEventListener> onCreateEvent(libvlc_instance_t libvlcInstance, libvlc_event_t event, MediaPlayer eventObject) {
        switch (libvlc_event_e.event(event.type)) {
            case libvlc_MediaPlayerMediaChanged:
                // Reset state each time the media changes, subsequent plays and stops are fine
                receivedPlayingEvent = false;
                break;
            case libvlc_MediaPlayerPlaying:
                // Playing event was received
                receivedPlayingEvent = true;
                break;
            case libvlc_MediaPlayerStopped:
                // We do not went to send a stopped event if we never received a playing event
                if (!receivedPlayingEvent) {
                    // Return null here to suppress the event
                    return null;
                }
                break;
        }
        return MediaPlayerEventFactory.createEvent(libvlcInstance, eventObject, event);
    }

}
