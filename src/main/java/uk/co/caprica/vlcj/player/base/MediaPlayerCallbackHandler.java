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

package uk.co.caprica.vlcj.player.base;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_cbs;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_picture_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_stopping_reason_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_title_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_chapter_description_t;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.parser.Pictures;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEvent;
import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;
import uk.co.caprica.vlcj.support.callback.Holder;
import uk.co.caprica.vlcj.support.callback.NativeCallbackHandler;

import static uk.co.caprica.vlcj.binding.internal.libvlc_list_action_t.listAction;
import static uk.co.caprica.vlcj.binding.internal.libvlc_stopping_reason_t.stoppingReason;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_list_release;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_retain;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_subitems;
import static uk.co.caprica.vlcj.binding.support.strings.NativeString.copyNativeString;
import static uk.co.caprica.vlcj.media.TrackType.trackType;
import static uk.co.caprica.vlcj.player.base.Capabilties.capabilities;
import static uk.co.caprica.vlcj.player.base.FrameStatus.frameStatus;
import static uk.co.caprica.vlcj.player.base.TitleFlags.titleFlags;

/**
 * Creates and manages the libvlc_media_player_cbs callback struct for a MediaPlayer.
 * <p>
 * Each callback in the struct creates the appropriate vlcj event and dispatches it through
 * the event manager to registered listeners.
 */
public final class MediaPlayerCallbackHandler extends NativeCallbackHandler<MediaPlayer, libvlc_media_player_cbs, MediaPlayerEventListener> {

    private libvlc_stopping_reason_t stoppingReason;

    private boolean receivedPlayingEvent;

    public MediaPlayerCallbackHandler(Holder<MediaPlayer> holder) {
        super(holder);
    }

    MediaPlayerCallbackHandler(MediaPlayer source) {
        super(source);
    }
    
    @Override
    protected libvlc_media_player_cbs initCallbacks() {
        libvlc_media_player_cbs cbs = new libvlc_media_player_cbs();
        cbs.version = 0;

        cbs.on_media_changed = (Pointer opaque, libvlc_media_t media) -> {
            onMediaChanged();

            MediaRef mediaRef = new MediaRef(media);
            try {
                raiseEvent(MediaPlayerEventFactory.createMediaChangedEvent(source(), mediaRef));
            } finally {
                mediaRef.release();
            }
        };

        cbs.on_media_stopping = (Pointer opaque, libvlc_media_t media, int stoppingReason) -> {
            this.stoppingReason = stoppingReason(stoppingReason);
            // A stopping event is not dispatched here as one will be raised by the state-changed handler
        };

        cbs.on_state_changed = (Pointer opaque, int state) -> {
            State stateValue = State.state(state);
            if (stateValue != null) {
                switch (stateValue) {
                    case NOTHING_SPECIAL:
                        raiseEvent(MediaPlayerEventFactory.createNothingSpecialEvent(source()));
                        break;
                    case OPENING:
                        raiseEvent(MediaPlayerEventFactory.createOpeningEvent(source()));
                        break;
                    case PLAYING:
                        onPlaying();
                        raiseEvent(MediaPlayerEventFactory.createPlayingEvent(source()));
                        break;
                    case PAUSED:
                        raiseEvent(MediaPlayerEventFactory.createPausedEvent(source()));
                        break;
                    // For STOPPED, the intention is to preserve the stopped/finished/error event semantics of previous
                    // versions of vlcj
                    case STOPPED:
                        if (shouldSuppressStopped()) {
                            break;
                        }
                        switch (stoppingReason) {
                            case libvlc_stopping_reason_error:
                                // An error event is not dispatched here as one will be raised by the state-changed handler
                                break;
                            case libvlc_stopping_reason_eos:
                                raiseEvent(MediaPlayerEventFactory.createMediaPlayerFinishedEvent(source()));
                                break;
                            case libvlc_stopping_reason_user:
                                raiseEvent(MediaPlayerEventFactory.createStoppedEvent(source()));
                                break;
                        }

                        // Reset the stashed stopping reason since it's no longer needed
                        this.stoppingReason = null;
                        break;
                    case STOPPING:
                        raiseEvent(MediaPlayerEventFactory.createStoppingEvent(source()));
                        break;
                    case ERROR:
                        raiseEvent(MediaPlayerEventFactory.createEncounteredErrorEvent(source()));
                        break;
                }
            }
        };

        cbs.on_buffering_changed = (Pointer opaque, float buffering) -> {
            raiseEvent(MediaPlayerEventFactory.createBufferingChangedEvent(source(), buffering));
        };

        cbs.on_rate_changed = (Pointer opaque, float newRate) -> {
            raiseEvent(MediaPlayerEventFactory.createRateChangedEvent(source(), newRate));
        };

        cbs.on_capabilities_changed = (Pointer opaque, int oldCapabilities, int newCapabilities) -> {
            raiseEvent(MediaPlayerEventFactory.createCapabilitiesChangedEvent(
                source(),
                capabilities(oldCapabilities),
                capabilities(newCapabilities)
            ));
        };

        cbs.on_position_changed = (Pointer opaque, long time, double position) -> {
            raiseEvent(MediaPlayerEventFactory.createPositionChangedEvent(source(), time, position));
        };

        cbs.on_length_changed = (Pointer opaque, long length) -> {
            raiseEvent(MediaPlayerEventFactory.createLengthChangedEvent(source(), length));
        };

        cbs.on_track_list_changed = (Pointer opaque, int action, int type, Pointer id) -> {
            TrackType trackType = trackType(type);
            String trackId = copyNativeString(id);
            MediaPlayerEvent event;
            switch (listAction(action)) {
                case libvlc_list_action_added:
                    event = MediaPlayerEventFactory.createTrackAddedEvent(source(), trackType, trackId);
                    break;
                case libvlc_list_action_removed:
                    event = MediaPlayerEventFactory.createTrackRemovedEvent(source(), trackType, trackId);
                    break;
                case libvlc_list_action_updated:
                    event = MediaPlayerEventFactory.createTrackUpdatedEvent(source(), trackType, trackId);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action: " + action);
            }
            raiseEvent(event);
        };

        cbs.on_track_selection_changed = (Pointer opaque, int type, Pointer unselected_id, Pointer selected_id) -> {
            raiseEvent(MediaPlayerEventFactory.createTrackSelectionChangedEvent(
                source(),
                trackType(type),
                copyNativeString(unselected_id),
                copyNativeString(selected_id)
            ));
        };

        cbs.on_program_list_changed = (Pointer opaque, int action, int groupId) -> {
            MediaPlayerEvent event;
            switch (listAction(action)) {
                case libvlc_list_action_added:
                    event = MediaPlayerEventFactory.createProgramAddedEvent(source(), groupId);
                    break;
                case libvlc_list_action_removed:
                    event = MediaPlayerEventFactory.createProgramRemovedEvent(source(), groupId);
                    break;
                case libvlc_list_action_updated:
                    event = MediaPlayerEventFactory.createProgramUpdatedEvent(source(), groupId);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown action: " + action);
            }
            raiseEvent(event);
        };

        cbs.on_program_selection_changed = (Pointer opaque, int unselected_group_id, int selected_group_id) -> {
            raiseEvent(MediaPlayerEventFactory.createProgramSelectedEvent(source(), unselected_group_id, selected_group_id));
        };

        cbs.on_titles_changed = (Pointer opaque) -> {
            raiseEvent(MediaPlayerEventFactory.createTitleListChangedEvent(source()));
        };

        cbs.on_title_selection_changed = (Pointer opaque, libvlc_title_description_t title, int index) -> {
            TitleDescription titleDescription = new TitleDescription(
                title.i_duration,
                copyNativeString(title.psz_name),
                titleFlags(title.i_flags)
            );
            raiseEvent(MediaPlayerEventFactory.createTitleSelectionChangedEvent(
                source(),
                titleDescription,
                index
            ));
        };

        cbs.on_chapter_selection_changed = (Pointer opaque, libvlc_title_description_t title, int titleIndex, libvlc_chapter_description_t chapter, int chapterIndex) -> {
            TitleDescription titleDescription = new TitleDescription(
                title.i_duration,
                copyNativeString(title.psz_name),
                titleFlags(title.i_flags)
            );
            ChapterDescription chapterDescription = new ChapterDescription(
                chapter.i_time_offset,
                chapter.i_duration,
                copyNativeString(chapter.psz_name)
            );
            raiseEvent(MediaPlayerEventFactory.createChapterSelectionChangedEvent(
                source(),
                titleDescription,
                titleIndex,
                chapterDescription,
                chapterIndex
            ));
        };

        cbs.on_recording_changed = (Pointer opaque, int recording, Pointer filePath) -> {
            raiseEvent(MediaPlayerEventFactory.createRecordChangedEvent(
                source(),
                recording != 0,
                copyNativeString(filePath)
            ));
        };

        cbs.on_screenshot_taken = (Pointer opaque, Pointer filePath) -> {
            String filePathStr = copyNativeString(filePath);
            raiseEvent(MediaPlayerEventFactory.createScreenshotTakenEvent(source(), filePathStr));
        };

        cbs.on_media_parsed = (Pointer opaque, libvlc_media_t media) -> {
            MediaRef mediaRef = new MediaRef(media);
            try {
                raiseEvent(MediaPlayerEventFactory.createMediaParsedEvent(source(), mediaRef));
            } finally {
                mediaRef.release();
            }
        };

        cbs.on_media_meta_changed = (Pointer opaque, libvlc_media_t media) -> {
            raiseEvent(MediaPlayerEventFactory.createMediaMetaChangedEvent(source(), new MetaData(media)));
        };

        cbs.on_media_subitems_changed = (Pointer opaque, libvlc_media_t media) -> {
            libvlc_media_list_t list = libvlc_media_subitems(media);
            try {
                raiseEvent(MediaPlayerEventFactory.createMediaSubitemsChangedEvent(source(), new MediaList(list)));
            } finally {
                libvlc_media_list_release(list);
            }
        };

        cbs.on_media_attachments_added = (Pointer opaque, libvlc_media_t media, libvlc_picture_list_t list) -> {
            raiseEvent(MediaPlayerEventFactory.createMediaAttachmentsAddedEvent(source(), new Pictures(list)));
        };

        cbs.on_next_frame_status = (Pointer opaque, int status) -> {
            raiseEvent(MediaPlayerEventFactory.createNextFrameStatusEvent(
                source(),
                frameStatus(status)
            ));
        };

        cbs.on_prev_frame_status = (Pointer opaque, int status) -> {
            raiseEvent(MediaPlayerEventFactory.createPreviousFrameStatusEvent(
                source(),
                frameStatus(status)
            ));
        };

        cbs.on_vout_changed = (Pointer opaque, int voutCount) -> {
            raiseEvent(MediaPlayerEventFactory.createVoutEvent(source(), voutCount));
        };

        cbs.on_cork_changed = (Pointer opaque, int corked) -> {
            if (corked != 0) {
                raiseEvent(MediaPlayerEventFactory.createCorkedEvent(source()));
            } else {
                raiseEvent(MediaPlayerEventFactory.createUncorkedEvent(source()));
            }
        };

        cbs.on_audio_volume_changed = (Pointer opaque, float volume) -> {
            raiseEvent(MediaPlayerEventFactory.createAudioVolumeEvent(source(), volume));
        };

        cbs.on_audio_mute_changed = (Pointer opaque, int muted) -> {
            if (muted != 0) {
                raiseEvent(MediaPlayerEventFactory.createMutedEvent(source()));
            } else {
                raiseEvent(MediaPlayerEventFactory.createUnmutedEvent(source()));
            }
        };

        cbs.on_audio_device_changed = (Pointer opaque, Pointer device) -> {
            String deviceStr = copyNativeString(device);
            raiseEvent(MediaPlayerEventFactory.createAudioDeviceEvent(source(), deviceStr));
        };

        cbs.write();
        return cbs;
    }

    private void onMediaChanged() {
        receivedPlayingEvent = false;
    }

    private void onPlaying() {
        receivedPlayingEvent = true;
    }

    boolean shouldSuppressStopped() {
        if (!receivedPlayingEvent) {
            return true;
        }
        return false;
    }
}
