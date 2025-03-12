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

import uk.co.caprica.vlcj.media.MediaSlavePriority;
import uk.co.caprica.vlcj.media.MediaSlaveType;
import uk.co.caprica.vlcj.media.SlaveApi;

import java.io.File;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_spu_delay;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_spu_text_scale;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_spu_delay;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_spu_text_scale;

/**
 * Behaviour pertaining to subpictures (sub-titles).
 */
public final class SubpictureApi extends BaseApi {

    SubpictureApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
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
     * Get the sub-title text scale.
     *
     * @return text scale factor; 1.0 for normal size (100%), 0.5 for half size, 2.0 for double size etc
     */
    public float getTextScale() {
        return libvlc_video_get_spu_text_scale(mediaPlayerInstance);
    }

    /**
     * Set the sub-title text scale.
     *
     * @param scale text scale factor; 1.0 for normal size (100%), 0.5 for half size, 2.0 for double size etc
     */
    public void setTextScale(float scale) {
        libvlc_video_set_spu_text_scale(mediaPlayerInstance, scale);
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
        return setSubTitleUri(subTitleFileName);
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
        return setSubTitleUri(subTitleFile.getAbsolutePath());
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
}
