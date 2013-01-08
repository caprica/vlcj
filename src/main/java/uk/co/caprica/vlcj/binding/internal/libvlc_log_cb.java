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

import uk.co.caprica.vlcj.binding.LibVlc;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * Specification for a callback that handles native log messages.
 */
public interface libvlc_log_cb extends Callback {

    /**
     * Process a log message.
     *
     * @param data opaque data as set by {@link LibVlc#libvlc_log_subscribe(libvlc_log_subscriber_t, libvlc_log_cb, Pointer)}
     * @param level log level
     * @param format printf-style format string
     * @param args format arguments
     */
    void log(Pointer data, int level, String format, Pointer args);
}
