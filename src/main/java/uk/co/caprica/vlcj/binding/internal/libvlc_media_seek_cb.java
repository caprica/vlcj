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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * Native seek media callback.
 */
public interface libvlc_media_seek_cb extends Callback {

    /**
     * Callback prototype to seek a custom bitstream input media.
     *
     * @param opaque private pointer as set by the @ref libvlc_media_open_cb callback
     * @param offset absolute byte offset to seek to
     * @return 0 on success, -1 on error.
     */
    int seek(Pointer opaque, long offset);
}
