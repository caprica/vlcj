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
import com.sun.jna.Pointer;

/**
 * Specification for a callback that handles native log messages.
 */
public interface libvlc_log_cb extends Callback {

    /**
     * Callback prototype for LibVLC log message handler.
     * <p>
     * <em>Log message handlers <b>must</b> be thread-safe.</em>
     *
     * @param data data pointer as given to libvlc_log_set()
     * @param level message level {@link libvlc_log_level_e}
     * @param ctx message context (meta-informations about the message)
     * @param format printf() format string (as defined by ISO C11)
     * @param args variable argument list for the format
     */
    void log(Pointer data, int level, libvlc_log_t ctx, String format, Pointer args);
}
