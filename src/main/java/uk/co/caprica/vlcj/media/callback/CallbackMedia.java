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

package uk.co.caprica.vlcj.media.callback;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_close_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_open_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_read_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_seek_cb;

/**
 * Specification for media provided by native callbacks.
 * <p>
 * Implementations <em>are</em> allowed to block the native thread when waiting for IO - however care must be taken,
 * e.g. on error conditions, to not block indefinitely as doing so will prevent the native media player from being
 * stopped.
 * <p>
 * <strong>Implementations of this class by definition rely on the use of native callbacks that are implemented in Java
 * code - steps must be taken to prevent instances of implementation classes from being garbage collected otherwise the
 * native code will crash when the Java object disappears.</strong>
 */
public interface CallbackMedia {

    /**
     * Get the native open media callback.
     *
     * @return open callback
     */
    libvlc_media_open_cb getOpen();

    /**
     * Get the native read media callback.
     *
     * @return read callback
     */
    libvlc_media_read_cb getRead();

    /**
     * Get the native seek media callback.
     *
     * @return seek callback
     */
    libvlc_media_seek_cb getSeek();

    /**
     * Get the native close media callback.
     *
     * @return close callback
     */
    libvlc_media_close_cb getClose();

    /**
     * Get the native opaque handle.
     *
     * @return opaque handle
     */
    Pointer getOpaque();

}
