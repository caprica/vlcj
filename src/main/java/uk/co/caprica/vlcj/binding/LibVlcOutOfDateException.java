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

package uk.co.caprica.vlcj.binding;

import uk.co.caprica.vlcj.version.Version;

/**
 * Exception thrown when the detected run-time version of LibVLC is too old.
 */
public class LibVlcOutOfDateException extends RuntimeException {

    /**
     * Default serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Required version of LibVLC.
     */
    private final Version requiredVersion;

    /**
     * Actual detected run-time version of LibVLC.
     */
    private final Version actualVersion;

    /**
     * Create an exception.
     *
     * @param requiredVersion required version of LibVLC
     * @param actualVersion actual detected run-time version of LibVLC
     */
    public LibVlcOutOfDateException(Version requiredVersion, Version actualVersion) {
        this.requiredVersion = requiredVersion;
        this.actualVersion = actualVersion;
    }

    /**
     * Get the required version of LibVLC.
     *
     * @return version
     */
    public Version getRequiredVersion() {
        return requiredVersion;
    }

    /**
     * Get the actual detected run-time version of LibVLC.
     *
     * @return version
     */
    public Version getActualVersion() {
        return actualVersion;
    }

    @Override
    public String toString() {
        return String.format("Detected run-time version of LibVLC out of date, required at least '%s' but found '%s'", requiredVersion, actualVersion);
    }
}
