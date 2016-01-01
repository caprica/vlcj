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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.version;

import uk.co.caprica.vlcj.binding.LibVlc;

/**
 * Holder for run-time libvlc version information.
 */
public final class LibVlcVersion {

    /**
     * Run-time version of vlc/libvlc.
     */
    private static final Version VERSION = new Version(LibVlc.INSTANCE.libvlc_get_version());

    /**
     * LibVlc 3.0.0 API baseline.
     */
    public static final Version LIBVLC_300 = new Version("3.0.0");

    /**
     * LibVlc 2.2.0 API baseline.
     */
    public static final Version LIBVLC_220 = new Version("2.2.0");

    /**
     * LibVlc 2.1.0 API baseline.
     */
    public static final Version LIBVLC_210 = new Version("2.1.0");

    /**
     * Prevent direct instantiation by others.
     */
    private LibVlcVersion() {
    }

    /**
     * Get the run-time version.
     *
     * @return vlc/libvlc version
     */
    public static Version getVersion() {
        return VERSION;
    }

    /**
     * Get the run-time changeset.
     *
     * @return changeset
     */
    public static String getChangeset() {
        return LibVlc.INSTANCE.libvlc_get_changeset();
    }
}
