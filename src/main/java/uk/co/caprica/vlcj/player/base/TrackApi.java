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

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_tracklist_t;
import uk.co.caprica.vlcj.binding.support.types.size_t;
import uk.co.caprica.vlcj.media.TrackType;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_selected_track;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_track_from_id;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_get_tracklist;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_select_track;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_select_tracks;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_select_tracks_by_ids;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_unselect_track_type;
import static uk.co.caprica.vlcj.media.TrackType.trackType;

/**
 * Behaviour pertaining to tracks.
 */
public final class TrackApi extends BaseApi {

    TrackApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the current video track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public VideoTrackList videoTracks() {
        return videoTracks(false);
    }

    /**
     * Get the current video track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @param selected if <code>true</code>, return only the selected tracks
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public VideoTrackList videoTracks(boolean selected) {
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.VIDEO.intValue(), selected ? 1 : 0);
        if (trackList != null) {
            return new VideoTrackList(trackList);
        }
        return null;
    }

    /**
     * Get the current audio track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public AudioTrackList audioTracks() {
        return audioTracks(false);
    }

    /**
     * Get the current audio track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @param selected if <code>true</code>, return only the selected tracks
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public AudioTrackList audioTracks(boolean selected) {
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.AUDIO.intValue(), selected ? 1 : 0);
        if (trackList != null) {
            return new AudioTrackList(trackList);
        }
        return null;
    }

    /**
     * Get the current text track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public TextTrackList textTracks() {
        return textTracks(false);
    }

    /**
     * Get the current text track list for the given track type.
     * <p>
     * The returned track list must be freed by {@link TrackList#release()} when it is no longer needed.
     *
     * @param selected if <code>true</code>, return only the selected tracks
     * @return track list, or <code>null</code> if no track list for the requested type is available
     */
    public TextTrackList textTracks(boolean selected) {
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.TEXT.intValue(), selected ? 1 : 0);
        if (trackList != null) {
            return new TextTrackList(trackList);
        }
        return null;
    }

    /**
     * Get the currently selected video track.
     * <p>
     * The returned track must be freed by {@link Track#release()} when it is no longer needed.
     * <p>
     * It is possible that more than one track of the specified type is selected, in such cases use {@link #videoTracks()}
     * and iterate the track list to find all of those selected.
     *
     * @return track, or <code>null</code> if no such track is selected
     */
    public VideoTrack selectedVideoTrack() {
        libvlc_media_track_t track = libvlc_media_player_get_selected_track(mediaPlayerInstance, TrackType.VIDEO.intValue());
        if (track != null) {
            return new VideoTrack(track);
        }
        return null;
    }

    /**
     * Get the currently selected audio track.
     * <p>
     * The returned track must be freed by {@link Track#release()} when it is no longer needed.
     * <p>
     * It is possible that more than one track of the specified type is selected, in such cases use {@link #audioTracks()}
     * and iterate the track list to find all of those selected.
     *
     * @return track, or <code>null</code> if no such track is selected
     */
    public AudioTrack selectedAudioTrack() {
        libvlc_media_track_t track = libvlc_media_player_get_selected_track(mediaPlayerInstance, TrackType.AUDIO.intValue());
        if (track != null) {
            return new AudioTrack(track);
        }
        return null;
    }

    /**
     * Get the currently selected text track.
     * <p>
     * The returned track must be freed by {@link Track#release()} when it is no longer needed.
     * <p>
     * It is possible that more than one track of the specified type is selected, in such cases use {@link #textTracks()}
     * and iterate the track list to find all of those selected.
     *
     * @return track, or <code>null</code> if no such track is selected
     */
    public TextTrack selectedTextTrack() {
        libvlc_media_track_t track = libvlc_media_player_get_selected_track(mediaPlayerInstance, TrackType.TEXT.intValue());
        if (track != null) {
            return new TextTrack(track);
        }
        return null;
    }

    /**
     * Get a track given a track identifier.
     * <p>
     * The returned track must be freed by {@link Track#release()} when it is no longer needed.
     *
     * @param trackId track identifier
     * @return track, or <code>null</code> if no track of the requested identifier is selected
     */
    public Track track(String trackId) {
        libvlc_media_track_t track = libvlc_media_player_get_track_from_id(mediaPlayerInstance, trackId);
        if (track != null) {
            switch (trackType(track.i_type)) {
                case AUDIO:
                    return new AudioTrack(track);
                case VIDEO:
                    return new VideoTrack(track);
                case TEXT:
                    return new TextTrack(track);
            }
        }
        return null;
    }

    /**
     * Select a track.
     *
     * @param track track to select
     */
    public void selectTrack(Track track) {
        libvlc_media_player_select_track(mediaPlayerInstance, track.instance());
    }

    /**
     * Select multiple tracks.
     * <p>
     * Not all track types support multiple selection.
     *
     * @param tracks tracks, of any type, to select
     */
    public <T extends Track> void select(T... tracks) {
        if (tracks != null && tracks.length > 0) {
            select(TrackType.AUDIO, tracks);
            select(TrackType.VIDEO, tracks);
            select(TrackType.TEXT, tracks);
        }
    }

    /**
     * Select multiple tracks.
     * <p>
     * Not all track types support multiple selection.
     *
     * @param tracks tracks, of any type, to select
     */
    public <T extends Track> void select(List<T> tracks) {
        select(tracks.toArray(new Track[0]));
    }

    /**
     * Select tracks based on their id.
     * <p>
     * Not all track types support multiple selection.
     * <p>
     * This method can be used to pre-select a list of tracks before starting the media player, or after the player has
     * been started.
     * <p>
     * Track identifiers are obtained from {@link Track#trackId()}.
     *
     * @param type type of track
     * @param trackIds ids of tracks to select
     */
    public void select(TrackType type, String... trackIds) {
        if (trackIds != null && trackIds.length > 0) {
            libvlc_media_player_select_tracks_by_ids(mediaPlayerInstance, type.intValue(), String.join(",", trackIds));
        }
    }

    /**
     * Select tracks based on their id.
     * <p>
     * Not all track types support multiple selection.
     * <p>
     * Track identifiers are obtained from {@link Track#trackId()}.
     *
     * @param type type of track
     * @param trackIds ids of tracks to select
     */
    public void select(TrackType type, List<String> trackIds) {
        select(type, trackIds.toArray(new String[0]));
    }

    /**
     * Deselect all tracks of the given types.
     *
     * @param types types of tracks to deselect
     */
    public void deselect(TrackType... types) {
        if (types != null && types.length > 0) {
            for (TrackType type : types) {
                libvlc_media_player_unselect_track_type(mediaPlayerInstance, type.intValue());
            }
        }
    }

    /**
     * Deselect all tracks of the given types.
     *
     * @param types types of tracks to deselect
     */
    public void deselect(List<TrackType> types) {
        deselect(types.toArray(new TrackType[0]));
    }

    /**
     * Helper method to select multiple tracks of a specific type.
     * <p>
     * Not all track types allow for multiple selection, additional selections will be ignored.
     * <p>
     * Developer note: this does not use collection streams so for the time being we can maintain Java 1.6 source-level
     * compatibility.
     *
     * @param type type of track to select
     * @param tracks tracks to select
     */
    private void select(TrackType type, Track[] tracks) {
        List<libvlc_media_track_t> list = new ArrayList<libvlc_media_track_t>(tracks.length);
        // Filter tracks of the requested type
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            if (track.trackType() == type) {
                list.add(track.instance());
            }
        }
        if (!list.isEmpty()) {
            libvlc_media_player_select_tracks(mediaPlayerInstance, type.intValue(), getPointerArray(tracks), new size_t(list.size()));
        }
    }

    /**
     * Get a contiguous block of memory containing an array of pointers.
     * <p>
     * Needed for native library direct mapping since there is no automatic mapping of arrays.
     *
     * @param tracks tracks
     * @return block of memory containing pointers
     */
    private static Memory getPointerArray(Track[] tracks) {
        int count = tracks.length;
        Memory memory = new Memory(Native.POINTER_SIZE * count);
        Pointer[] pointers = new Pointer[count];
        for (int i = 0; i < count; i++) {
            pointers[i] = tracks[i].instance().getPointer();
        }
        memory.write(0, pointers, 0, pointers.length);
        return memory;
    }
}
