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

package uk.co.caprica.vlcj.binding;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * JNA interface to the Xlib native library.
 * <p>
 * The only exposed API is that used to initialise XLib for multi-threaded access.
 * <p>
 * Usage is simple, at the start of an application:
 *
 * <pre>
 * int result = LibX11.INSTANCE.XInitThreads();
 * // &quot;result&quot; will be non-zero if the native library call succeeded
 * </pre>
 */
public interface LibX11 extends Library {

    /**
     * Native library instance.
     * <p>
     * Conceivably Xlib could be present on Windows.
     */
    LibX11 INSTANCE = (LibX11)Native.loadLibrary(Platform.isWindows() ? "libX11" : "X11", LibX11.class);

    /**
     * Initialise Xlib support for concurrent threads.
     * <p>
     * Invoking this at the start of an application can reduce the chance of a fatal JVM crash when
     * using multiple media players.
     *
     * @return non-zero on success, zero on failure (or if threading is not supported)
     */
    int XInitThreads();
}
