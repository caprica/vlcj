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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.parser.Pictures;
import uk.co.caprica.vlcj.player.base.Capabilties;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.FrameStatus;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.TitleDescription;

/**
 * A factory that creates semantic media player events (events with no direct native counterpart).
 */
public final class MediaPlayerEventFactory {

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

    /**
     * Create a media player finished event.
     * <p>
     * This event is a "semantic" event, it has no direct native event counterpart.
     *
     * @param mediaPlayer component the event relates to
     * @return event
     */
    public static MediaPlayerEvent createMediaPlayerFinishedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerFinishedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createNothingSpecialEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerNothingSpecialEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createOpeningEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerOpeningEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createPlayingEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerPlayingEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createPausedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerPausedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createStoppedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerStoppedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createStoppingEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerStoppingEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createEncounteredErrorEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerEncounteredErrorEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createCorkedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerCorkedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createUncorkedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerUncorkedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createMutedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerMutedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createUnmutedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerUnmutedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createTitleListChangedEvent(MediaPlayer mediaPlayer) {
        return new MediaPlayerTitleListChangedEvent(mediaPlayer);
    }

    public static MediaPlayerEvent createMediaChangedEvent(MediaPlayer mediaPlayer, MediaRef newMedia) {
        return new MediaPlayerMediaChangedEvent(mediaPlayer, newMedia);
    }

    public static MediaPlayerEvent createBufferingChangedEvent(MediaPlayer mediaPlayer, float newCache) {
        return new MediaPlayerBufferingChangedEvent(mediaPlayer, newCache);
    }

    public static MediaPlayerEvent createRateChangedEvent(MediaPlayer mediaPlayer, float newRate) {
        return new MediaPlayerRateChangedEvent(mediaPlayer, newRate);
    }

    public static MediaPlayerEvent createCapabilitiesChangedEvent(MediaPlayer mediaPlayer, Capabilties oldCapabilties, Capabilties newCapabilties) {
        return new MediaPlayerCapabilitiesChangedEvent(mediaPlayer, oldCapabilties, newCapabilties);
    }

    public static MediaPlayerEvent createPositionChangedEvent(MediaPlayer mediaPlayer, long newTime, double newPosition) {
        return new MediaPlayerPositionChangedEvent(mediaPlayer, newTime, newPosition);
    }

    public static MediaPlayerEvent createScreenshotTakenEvent(MediaPlayer mediaPlayer, String filename) {
        return new MediaPlayerScreenshotTakenEvent(mediaPlayer, filename);
    }

    public static MediaPlayerEvent createMediaParsedEvent(MediaPlayer mediaPlayer, MediaRef mediaRef) {
        return new MediaPlayerMediaParsedEvent(mediaPlayer, mediaRef);
    }

    public static MediaPlayerEvent createMediaMetaChangedEvent(MediaPlayer mediaPlayer, MetaData metaData) {
        return new MediaPlayerMediaMetaChangedEvent(mediaPlayer, metaData);
    }

    public static MediaPlayerEvent createMediaSubitemsChangedEvent(MediaPlayer mediaPlayer, MediaList mediaList) {
        return new MediaPlayerMediaSubitemsChangedEvent(mediaPlayer, mediaList);
    }

    public static MediaPlayerEvent createMediaAttachmentsAddedEvent(MediaPlayer mediaPlayer, Pictures pictures) {
        return new MediaPlayerMediaAttachmentsAddedEvent(mediaPlayer, pictures);
    }

    public static MediaPlayerEvent createLengthChangedEvent(MediaPlayer mediaPlayer, long newLength) {
        return new MediaPlayerLengthChangedEvent(mediaPlayer, newLength);
    }

    public static MediaPlayerEvent createTrackAddedEvent(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
        return new MediaPlayerTrackAddedEvent(mediaPlayer, trackType, trackId);
    }

    public static MediaPlayerEvent createTrackRemovedEvent(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
        return new MediaPlayerTrackRemovedEvent(mediaPlayer, trackType, trackId);
    }

    public static MediaPlayerEvent createTrackUpdatedEvent(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
        return new MediaPlayerTrackUpdatedEvent(mediaPlayer, trackType, trackId);
    }

    public static MediaPlayerEvent createTrackSelectionChangedEvent(MediaPlayer mediaPlayer, TrackType trackType, String unselectedTrackId, String selectedTrackId) {
        return new MediaPlayerTrackSelectionChangedEvent(mediaPlayer, trackType, unselectedTrackId, selectedTrackId);
    }

    public static MediaPlayerEvent createNextFrameStatusEvent(MediaPlayer mediaPlayer, FrameStatus frameStatus) {
        return new MediaPlayerNextFrameStatusEvent(mediaPlayer, frameStatus);
    }

    public static MediaPlayerEvent createPreviousFrameStatusEvent(MediaPlayer mediaPlayer, FrameStatus frameStatus) {
        return new MediaPlayerPreviousFrameStatusEvent(mediaPlayer, frameStatus);
    }

    public static MediaPlayerEvent createVoutEvent(MediaPlayer mediaPlayer, int newCount) {
        return new MediaPlayerVoutEvent(mediaPlayer, newCount);
    }

    public static MediaPlayerEvent createProgramAddedEvent(MediaPlayer mediaPlayer, int id) {
        return new MediaPlayerProgramAddedEvent(mediaPlayer, id);
    }

    public static MediaPlayerEvent createProgramRemovedEvent(MediaPlayer mediaPlayer, int id) {
        return new MediaPlayerProgramRemovedEvent(mediaPlayer, id);
    }

    public static MediaPlayerEvent createProgramUpdatedEvent(MediaPlayer mediaPlayer, int id) {
        return new MediaPlayerProgramUpdatedEvent(mediaPlayer, id);
    }

    public static MediaPlayerEvent createProgramSelectedEvent(MediaPlayer mediaPlayer, int unselectedId, int selectedId) {
        return new MediaPlayerProgramSelectedEvent(mediaPlayer, unselectedId, selectedId);
    }

    public static MediaPlayerEvent createTitleSelectionChangedEvent(MediaPlayer mediaPlayer, TitleDescription titleDescription, int index) {
        return new MediaPlayerTitleSelectionChangedEvent(mediaPlayer, titleDescription, index);
    }

    public static MediaPlayerEvent createChapterSelectionChangedEvent(MediaPlayer mediaPlayer, TitleDescription titleDescription, int titleIndex, ChapterDescription chapterDescription, int chapterIndex) {
        return new MediaPlayerChapterSelectionChangedEvent(mediaPlayer, titleDescription, titleIndex, chapterDescription, chapterIndex);
    }

    public static MediaPlayerEvent createAudioVolumeEvent(MediaPlayer mediaPlayer, float volume) {
        return new MediaPlayerAudioVolumeEvent(mediaPlayer, volume);
    }

    public static MediaPlayerEvent createAudioDeviceEvent(MediaPlayer mediaPlayer, String device) {
        return new MediaPlayerAudioDeviceEvent(mediaPlayer, device);
    }

    public static MediaPlayerEvent createRecordChangedEvent(MediaPlayer mediaPlayer, boolean recording, String recordedFilePath) {
        return new MediaPlayerRecordChangedEvent(mediaPlayer, recording, recordedFilePath);
    }

    private MediaPlayerEventFactory() {
    }
}
