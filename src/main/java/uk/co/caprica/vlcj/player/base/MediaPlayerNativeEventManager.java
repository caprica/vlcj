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

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.eventmanager.EventNotification;
import uk.co.caprica.vlcj.eventmanager.NativeEventManager;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;

final class MediaPlayerNativeEventManager extends NativeEventManager<MediaPlayer, MediaPlayerEventListener> {

    MediaPlayerNativeEventManager(LibVlc libvlc, MediaPlayer eventObject) {
        super(libvlc, eventObject, libvlc_event_e.libvlc_MediaPlayerMediaChanged, libvlc_event_e.libvlc_MediaPlayerChapterChanged, "media-player-events");
    }

    @Override
    protected libvlc_event_manager_t onGetEventManager(LibVlc libvlc, MediaPlayer eventObject) {
        return libvlc.libvlc_media_player_event_manager(eventObject.mediaPlayerInstance());
    }

    @Override
    protected EventNotification<MediaPlayerEventListener> onCreateEvent(LibVlc libvlc, libvlc_event_t event, MediaPlayer eventObject) {
        return MediaPlayerEventFactory.createEvent(eventObject, event);
    }

}
