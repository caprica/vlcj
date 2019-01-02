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
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Native open media callback.
 */
public interface libvlc_media_open_cb extends Callback {

    /**
     * Callback prototype to open a custom bitstream input media.
     * <p>
     * The same media item can be opened multiple times. Each time, this callback
     * is invoked. It should allocate and initialize any instance-specific
     * resources, then store them in *datap. The instance resources can be freed
     * in the @ref libvlc_close_cb callback.
     * <p>
     * For convenience, datap is initially NULL and sizep is initially 0.
     *
     * @param opaque private pointer as passed to libvlc_media_new_callbacks()
     * @param datap storage space for a private data pointer [OUT]
     * @param sizep byte length of the bitstream or 0 if unknown [OUT]
     * @return 0 on success, non-zero on error. In case of failure, the other
     * callbacks will not be invoked and any value stored in datap and sizep is
     * discarded.
     */
    int open(Pointer opaque, PointerByReference datap, LongByReference sizep);
}
