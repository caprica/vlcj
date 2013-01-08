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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Callback;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Callback prototype to configure picture buffers format.
 */
public interface libvlc_video_format_cb extends Callback {

    /**
     * Callback prototype to configure picture buffers format.
     * <p>
     * This callback gets the format of the video as output by the video decoder and the chain of
     * video filters (if any). It can opt to change any parameter as it needs. In that case, LibVLC
     * will attempt to convert the video format (rescaling and chroma conversion) but these
     * operations can be CPU intensive.
     *
     * @param opaque pointer to the private pointer passed to libvlc_video_set_callbacks() [IN/OUT]
     * @param chroma pointer to the 4 bytes video format identifier [IN/OUT]
     * @param width pointer to the pixel width [IN/OUT]
     * @param height pointer to the pixel height [IN/OUT]
     * @param pitches table of scanline pitches in bytes for each pixel plane (the table is
     *            allocated by LibVLC) [OUT]
     * @param lines table of scanlines count for each plane [OUT]
     * @return the number of picture buffers allocated, 0 indicates failure
     *
     *         Note: For each pixels plane, the scanline pitch must be bigger than or equal to the
     *         number of bytes per pixel multiplied by the pixel width. Similarly, the number of
     *         scanlines must be bigger than of equal to the pixel height. Furthermore, we recommend
     *         that pitches and lines be multiple of 32 to not break assumption that might be made
     *         by various optimizations in the video decoders, video filters and/or video
     *         converters.
     */
    int format(PointerByReference opaque, PointerByReference chroma, IntByReference width, IntByReference height, PointerByReference pitches, PointerByReference lines);
}
