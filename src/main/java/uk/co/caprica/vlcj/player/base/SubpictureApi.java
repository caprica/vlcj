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

import uk.co.caprica.vlcj.binding.NativeUri;
import uk.co.caprica.vlcj.media.MediaSlavePriority;
import uk.co.caprica.vlcj.media.MediaSlaveType;
import uk.co.caprica.vlcj.media.SlaveApi;

import java.io.File;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_spu;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_spu_count;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_spu_delay;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_spu;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_spu_delay;

/**
 * Behaviour pertaining to subpictures (sub-titles).
 */
public final class SubpictureApi extends BaseApi {

    SubpictureApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the number of sub-pictures/sub-title tracks.
     *
     * @return number of sub-title tracks
     */
    public int trackCount() {
        return libvlc_video_get_spu_count(mediaPlayerInstance);
    }

    /**
     * Get the current sub-title track.
     *
     * @return sub-title number, or -1 if none
     */
    public int track() {
        return libvlc_video_get_spu(mediaPlayerInstance);
    }

    /**
     * Set the current sub-title track.
     * <p>
     * The track identifier must be one of those returned by {@link #trackDescriptions()}.
     * <p>
     * Subtitles can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available subtitle identifiers go in sequence from zero up to
     * {@link #trackCount()}-1. The {@link #trackDescriptions()} method should always
     * be used to ascertain the available subtitle identifiers.
     * <p>
     * The implementation of the corresponding <em>native</em> method in libvlc is bugged before
     * vlc 2.0.6, therefore vlc 2.0.6 or later is required for correct behaviour when using this
     * method.
     *
     * @param spu sub-title identifier, or -1 for none
     * @return current sub-title identifier
     */
    public int setTrack(int spu) {
        libvlc_video_set_spu(mediaPlayerInstance, spu);
        return track();
    }

    /**
     * Get the sub-title delay.
     *
     * @return sub-title delay, in microseconds
     */
    public long delay() {
        return libvlc_video_get_spu_delay(mediaPlayerInstance);
    }

    /**
     * Set the sub-title delay.
     * <p>
     * The sub-title delay is set for the current item only and will be reset to zero each time the
     * media changes.
     *
     * @param delay desired sub-title delay, in microseconds
     */
    public void setDelay(long delay) {
        libvlc_video_set_spu_delay(mediaPlayerInstance, delay);
    }

    /**
     * Set the sub-title file to use.
     * <p>
     * These sub-titles will be automatically selected.
     * <p>
     * This method is a convenience for {@link MediaApi#addSlave(MediaSlaveType, String, boolean)}.
     *
     * @param subTitleFileName name of the local file containing the sub-titles
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setSubTitleFile(String subTitleFileName) {
        return setSubTitleUri(NativeUri.encodeFileUri(subTitleFileName));
    }

    /**
     * Set the sub-title file to use.
     * <p>
     * These sub-titles will be automatically selected.
     * <p>
     * This method is a convenience for {@link MediaApi#addSlave(MediaSlaveType, String, boolean)}.
     *
     * @param subTitleFile file containing the sub-titles
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setSubTitleFile(File subTitleFile) {
        return setSubTitleUri(NativeUri.encodeFileUri(subTitleFile.getAbsolutePath()));
    }

    /**
     * Set sub-titles from a URI.
     * <p>
     * These sub-titles will be automatically selected.
     * <p>
     * This method is a convenience for {@link MediaApi#addSlave(MediaSlaveType, String, boolean)}.
     * <p>
     * See {@link SlaveApi#add(MediaSlaveType, MediaSlavePriority, String)} for further
     * important information regarding this method.
     *
     * @param uri sub-title URI
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean setSubTitleUri(String uri) {
        return mediaPlayer.media().addSlave(MediaSlaveType.SUBTITLE, uri, true);
    }

    /**
     * Get the sub-title track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public List<TrackDescription> trackDescriptions() {
        return Descriptions.spuTrackDescriptions(mediaPlayerInstance);
    }

}
