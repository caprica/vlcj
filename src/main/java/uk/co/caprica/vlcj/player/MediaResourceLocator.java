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

package uk.co.caprica.vlcj.player;

import java.util.regex.Pattern;

/**
 * Utility class to help detect the type of media resource locator.
 * <p>
 * This is needed since local files must be played differently to non-local MRLs
 * like streaming URLs or "screen://".
 * <p>
 * This is essentially an internal class.
 */
public final class MediaResourceLocator {

    /**
     * Simple pattern to detect locations.
     */
    private static final Pattern MRL_LOCATION_PATTERN = Pattern.compile(".+://.*");

    /**
     * Private constructor to prevent direct instantiation.
     */
    private MediaResourceLocator() {
    }

    /**
     * Does the MRL represent a "location"?
     *
     * @param mrl media resource locator
     * @return <code>true</code> if the supplied MRL should be treated as a "location"; <code>false</code> for a file
     */
    public static boolean isLocation(String mrl) {
        return MRL_LOCATION_PATTERN.matcher(mrl).matches();
    }
}
