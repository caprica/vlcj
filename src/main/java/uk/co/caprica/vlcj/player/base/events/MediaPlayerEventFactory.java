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

package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * A factory that creates a media player event instance for a native media player event.
 */
public final class MediaPlayerEventFactory {

    /**
     * Create a new media player event for a given native event.
     * <p>
     * Events generally are expected to copy values from the native structure as needed (specifically this applies to
     * non-primitive values like Strings) because once the event handler returns the native memory will be gone. Without
     * copying such structure values pointers will become invalid.
     *
     * @param libvlcInstance native library instance
     * @param mediaPlayer component the event relates to
     * @param event native event
     * @return media player event, or <code>null</code> if the native event type is not enabled or otherwise could not be handled
     */
    public static MediaPlayerEvent createEvent(libvlc_instance_t libvlcInstance, MediaPlayer mediaPlayer, libvlc_event_t event) {
        switch(libvlc_event_e.event(event.type)) {
            case libvlc_MediaPlayerMediaChanged    : return new MediaPlayerMediaChangedEvent    (libvlcInstance, mediaPlayer, event);
            case libvlc_MediaPlayerNothingSpecial  : return new MediaPlayerNothingSpecialEvent  (                mediaPlayer       );
            case libvlc_MediaPlayerOpening         : return new MediaPlayerOpeningEvent         (                mediaPlayer       );
            case libvlc_MediaPlayerBuffering       : return new MediaPlayerBufferingEvent       (                mediaPlayer, event);
            case libvlc_MediaPlayerPlaying         : return new MediaPlayerPlayingEvent         (                mediaPlayer       );
            case libvlc_MediaPlayerPaused          : return new MediaPlayerPausedEvent          (                mediaPlayer       );
            case libvlc_MediaPlayerStopped         : return new MediaPlayerStoppedEvent         (                mediaPlayer       );
            case libvlc_MediaPlayerForward         : return new MediaPlayerForwardEvent         (                mediaPlayer       );
            case libvlc_MediaPlayerBackward        : return new MediaPlayerBackwardEvent        (                mediaPlayer       );
            case libvlc_MediaPlayerEndReached      : return new MediaPlayerEndReachedEvent      (                mediaPlayer       );
            case libvlc_MediaPlayerEncounteredError: return new MediaPlayerEncounteredErrorEvent(                mediaPlayer       );
            case libvlc_MediaPlayerTimeChanged     : return new MediaPlayerTimeChangedEvent     (                mediaPlayer, event);
            case libvlc_MediaPlayerPositionChanged : return new MediaPlayerPositionChangedEvent (                mediaPlayer, event);
            case libvlc_MediaPlayerSeekableChanged : return new MediaPlayerSeekableChangedEvent (                mediaPlayer, event);
            case libvlc_MediaPlayerPausableChanged : return new MediaPlayerPausableChangedEvent (                mediaPlayer, event);
            case libvlc_MediaPlayerTitleChanged    : return new MediaPlayerTitleChangedEvent    (                mediaPlayer, event);
            case libvlc_MediaPlayerSnapshotTaken   : return new MediaPlayerSnapshotTakenEvent   (                mediaPlayer, event);
            case libvlc_MediaPlayerLengthChanged   : return new MediaPlayerLengthChangedEvent   (                mediaPlayer, event);
            case libvlc_MediaPlayerVout            : return new MediaPlayerVoutEvent            (                mediaPlayer, event);
            case libvlc_MediaPlayerScrambledChanged: return new MediaPlayerScrambledChangedEvent(                mediaPlayer, event);
            case libvlc_MediaPlayerESAdded         : return new MediaPlayerESAddedEvent         (                mediaPlayer, event);
            case libvlc_MediaPlayerESDeleted       : return new MediaPlayerESDeletedEvent       (                mediaPlayer, event);
            case libvlc_MediaPlayerESSelected      : return new MediaPlayerESSelectedEvent      (                mediaPlayer, event);
            case libvlc_MediaPlayerCorked          : return new MediaPlayerCorkedEvent          (                mediaPlayer       );
            case libvlc_MediaPlayerUncorked        : return new MediaPlayerUncorkedEvent        (                mediaPlayer       );
            case libvlc_MediaPlayerMuted           : return new MediaPlayerMutedEvent           (                mediaPlayer       );
            case libvlc_MediaPlayerUnmuted         : return new MediaPlayerUnmutedEvent         (                mediaPlayer       );
            case libvlc_MediaPlayerAudioVolume     : return new MediaPlayerAudioVolumeEvent     (                mediaPlayer, event);
            case libvlc_MediaPlayerAudioDevice     : return new MediaPlayerAudioDeviceEvent     (                mediaPlayer, event);
            case libvlc_MediaPlayerChapterChanged  : return new MediaPlayerChapterChangedEvent  (                mediaPlayer, event);

            default                                : return null;
        }
    }

    /**
     * Create a media player ready event.
     * <p>
     * This event is a "semantic" event, it has no direct native event counterpart.
     *
     * @param mediaPlayer component the event relates to
     * @return event
     */
    public static MediaPlayerEvent createMediaPlayerReadyEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerReadyEvent(mediaPlayer);
    }

    private MediaPlayerEventFactory() {
    }

}
