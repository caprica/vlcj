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

package uk.co.caprica.vlcj.support.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulation of version information and related behaviours.
 * <p>
 * This may be useful to implement version-specific features.
 * <p>
 * This implementation is not exhaustive, but is good enough for the known vlc
 * versions.
 */
public final class Version implements Comparable<Version> {

    /**
     * Regular expression for matching versions strings.
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)[\\-_\\s]?(.*)");

    /**
     * Raw version information.
     */
    private final String version;

    /**
     * Major version number.
     */
    private final int major;

    /**
     * Minor version number.
     */
    private final int minor;

    /**
     * Revision number.
     */
    private final int revision;

    /**
     * Extra.
     */
    private final String extra;

    /**
     * Create a new version.
     *
     * @param version version string
     */
    public Version(final String version) {
        this.version = version;
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (matcher.matches()) {
            this.major = Integer.parseInt(matcher.group(1));
            this.minor = Integer.parseInt(matcher.group(2));
            this.revision = Integer.parseInt(matcher.group(3));
            if (matcher.groupCount() > 3) {
                this.extra = matcher.group(4);
            } else {
                this.extra = null;
            }
        } else {
            throw new IllegalArgumentException("Can't parse version from '" + version + "'");
        }
    }

    /**
     * Get the original version string.
     *
     * @return version
     */
    public String version() {
        return version;
    }

    /**
     * Get the major version.
     *
     * @return major version number
     */
    public int major() {
        return major;
    }

    /**
     * Get the minor version.
     *
     * @return minor version number
     */
    public int minor() {
        return minor;
    }

    /**
     * Get the revision.
     *
     * @return revision number
     */
    public int revision() {
        return revision;
    }

    /**
     * Get the extra.
     *
     * @return extra
     */
    public String extra() {
        return extra;
    }

    /**
     * Test whether or not this version is at least the required version.
     *
     * @param required required version
     * @return <code>true</code> if this version is at least (equal to or greater than) the required version
     */
    public boolean atLeast(Version required) {
        return compareTo(required) >= 0;
    }

    @Override
    public int compareTo(Version o) {
        int delta = major - o.major;
        if (delta == 0) {
            delta = minor - o.minor;
            if (delta == 0) {
                delta = revision - o.revision;
                // We don't care about "extra" - it is a legacy artifact that is no longer used in LibVLC versions
            }
        }
        return delta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append(version);
        return sb.toString();
    }

}
