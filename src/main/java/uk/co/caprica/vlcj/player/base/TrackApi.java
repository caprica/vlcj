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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_tracklist_t;
import uk.co.caprica.vlcj.binding.support.size_t;
import uk.co.caprica.vlcj.media.TrackType;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_selected_track;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_track_from_id;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_get_tracklist;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_select_track;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_select_tracks;
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
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.VIDEO.intValue());
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
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.AUDIO.intValue());
        if (trackList != null) {
            return new AudioTrackList(trackList);
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
    public TextTrackList textTracks() {
        libvlc_media_tracklist_t trackList = libvlc_media_player_get_tracklist(mediaPlayerInstance, TrackType.TEXT.intValue());
        if (trackList != null) {
            return new TextTrackList(trackList);
        }
        return null;
    }

    /**
     * Get the currently selected video track.
     * <p>
     * The returned track must be freed by {@link Track#release()} when it is no longer needed.
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
        libvlc_media_player_select_track(mediaPlayerInstance, track.trackType().intValue(), track.instance());
    }

    /**
     * Select multiple tracks.
     *
     * @param tracks tracks, of any type, to select
     */
    public void select(Track... tracks) {
        if (tracks != null && tracks.length > 0) {
            select(TrackType.AUDIO, tracks);
            select(TrackType.VIDEO, tracks);
            select(TrackType.TEXT, tracks);
        }
    }

    /**
     * Select multiple tracks.
     *
     * @param tracks tracks, of any type, to select
     */
    public void select(List<Track> tracks) {
        select(tracks.toArray(new Track[0]));
    }

    /**
     * Helper method to select multiple tracks of a specific type.
     * <p>
     * Developer note: this does not use collection streams so for the time being we can maintain Java 1.6 source-level
     * compatibility.
     *
     * @param type type of track to select
     * @param tracks tracks to select
     */
    private void select(TrackType type, Track[] tracks) {
        List<libvlc_media_track_t> list = new ArrayList<libvlc_media_track_t>(tracks.length);
        // Filter tracks of the request type
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            if (track.trackType() == type) {
                list.add(track.instance());
            }
        }
        if (!list.isEmpty()) {
            // Create and pass the necessary native array
            libvlc_media_track_t[] instances = (libvlc_media_track_t[]) new libvlc_media_track_t().toArray(tracks.length);
            for (int i = 0; i < list.size(); i++) {
                instances[i] = list.get(i);
            }
            libvlc_media_player_select_tracks(mediaPlayerInstance, type.intValue(), instances[0].getPointer(), new size_t(instances.length));
        }
    }
}
