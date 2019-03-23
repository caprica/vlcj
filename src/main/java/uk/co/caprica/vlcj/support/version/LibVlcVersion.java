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

package uk.co.caprica.vlcj.support.version;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_get_changeset;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_get_version;

/**
 * Holder for runtime LibVLC version information.
 */
public final class LibVlcVersion {

    /**
     * LibVlc 3.0.0 API baseline.
     */
    public static final Version requiredVersion = new Version("3.0.0");

    /**
     * Runtime version of VLC/LibVLC.
     */
    private final Version actualVersion;

    /**
     * Git change-set id of the VLC build.
     */
    private final String changeset;

    /**
     * Create a version component.
     */
    public LibVlcVersion() {
        this.actualVersion = new Version(libvlc_get_version());
        this.changeset = libvlc_get_changeset();
    }

    /**
     * Get the required LibVLC version.
     *
     * @return required version
     */
    public Version getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * Get the runtime actualVersion.
     *
     * @return VLC/LibVLC version.
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
