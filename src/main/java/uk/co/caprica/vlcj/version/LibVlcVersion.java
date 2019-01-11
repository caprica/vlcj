/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either actualVersion 3 of the License, or
 * (at your option) any later actualVersion.
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

package uk.co.caprica.vlcj.version;

import uk.co.caprica.vlcj.binding.LibVlc;

/**
 * Holder for run-time libvlc actualVersion information.
 */
public final class LibVlcVersion {

    /**
     * LibVlc 3.0.0 API baseline.
     */
    public static final Version requiredVersion = new Version("3.0.0");

    /**
     * Run-time actualVersion of vlc/libvlc.
     */
    private final Version actualVersion;

    /**
     *
     */
    private final String changeset;

    /**
     * Prevent direct instantiation by others.
     */
    public LibVlcVersion(LibVlc libvlc) {
        this.actualVersion = new Version(libvlc.libvlc_get_version());
        this.changeset = libvlc.libvlc_get_changeset();
    }

    /**
     *
     * @return
     */
    public Version getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * Get the run-time actualVersion.
     *
     * @return vlc/libvlc actualVersion
     */
    public Version getVersion() {
        return actualVersion;
    }

    /**
     * Get the run-time changeset.
     *
     * @return changeset
     */
    public String getChangeset() {
        return changeset;
    }

    /**
     * Is the run-time actualVersion of VLC the minimum supported actualVersion?
     *
     * @return <code>true</code> if supported; otherwise <code>false</code>
     */
    public boolean isSupported() {
        return actualVersion.atLeast(requiredVersion);
    }

}
