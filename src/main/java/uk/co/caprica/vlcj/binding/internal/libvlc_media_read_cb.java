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

import uk.co.caprica.vlcj.binding.support.size_t;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * Native read media callback.
 */
public interface libvlc_media_read_cb extends Callback {

    /**
     * Callback prototype to read data from a custom bitstream input media.
     * <p>
     * <em>If no data is immediately available, then the callback should sleep.</em>
     * <p>
     * <strong>The application is responsible for avoiding deadlock situations.
     * In particular, the callback should return an error if playback is stopped;
     * if it does not return, then libvlc_media_player_stop() will never return.</strong>
     *
     * @param opaque private pointer as set by the @ref libvlc_media_open_cb callback
     * @param buf start address of the buffer to read data into
     * @param len bytes length of the buffer
     *
     * @return strictly positive number of bytes read, 0 on end-of-stream, or -1 on non-recoverable error
     */
    size_t read(Pointer opaque, Pointer buf, size_t len);
}
