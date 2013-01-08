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

package uk.co.caprica.vlcj.runtime.install;

/**
 * Specification for a component interested in receiving event notifications from the
 * {@link NativeLibraryManager}.
 *
 * @see NativeLibraryManagerEventAdapter
 */
public interface NativeLibraryManagerEventListener {

    /**
     * Unpacking has started.
     *
     * @param installTo name of the directory files are being unpacked to
     * @param installCount total number of files being unpacked
     */
    void start(String installTo, int installCount);

    /**
     * A file is being installed.
     *
     * @param number number (not index) of the file currently being installed
     * @param name name of the file currently being installed
     */
    void install(int number, String name);

    /**
     * Unpacking has completed.
     */
    void end();

    /**
     * Purging of the installation directory has started.
     *
     * @param installTo name of the installation directory being purged
     */
    void purge(String installTo);

    /**
     * Purging of the installation directory has completed.
     *
     * @param result <code>true</code> if all files and directories were deleted, otherwise <code>false</code>
     */
    void purged(boolean result);
}
