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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.logger.Logger;

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

    /**
     * Encode, if needed, a local file MRL that may contain Unicode characters as a file URL with
     * "percent" encoding.
     * <p>
     * This method deals only with the special case of an MRL for a local file name containing
     * Unicode characters. Such MRLs must be encoded as file URLs, by adding a "file://" prefix
     * before percent-encoding the filename.
     * <p>
     * Without this, vlc will not be able to play the file since it is using native API that can
     * not handle unencoded Unicode characters.
     * <p>
     * This method does not deal with any MRLs that are URLs since Unicode characters are forbidden
     * by specification for any URL.
     * <p>
     * What this means in practical terms is that if an MRL is specified that contains a "scheme"
     * like "http", or "file" then that MRL will <em>not</em> be encoded by this method, even if it
     * contains Unicode characters. This situation, if it arises, is considered a client
     * application vaildation failure.
     *
     * @param mrl MRL
     * @return the original MRL if no encoding is required, or a percent-encoded file URL
     */
    public static String encodeMrl(String mrl) {
        Logger.debug("encodeMrl(mrl={})", mrl);
        // Assume no change needed
        String result = mrl;
        // If the supplied MRL contains any Unicode characters...
        if (containsUnicode(mrl)) {
            Logger.debug("MRL contains Unicode characters");
            try {
                URI uri = new URI(mrl);
                Logger.debug("uri={}", uri);
                String scheme = uri.getScheme();
                Logger.debug("scheme={}", uri.getScheme());
                // If there is no URI scheme, then this is a local file...
                if (scheme == null) {
                    Logger.debug("MRL has no scheme, assuming a local file name that should be encoded");
                    // Encode the local file as ASCII, and fix the scheme prefix
                    result = new File(mrl).toURI().toASCIIString().replaceFirst("file:/", "file:///");
                }
                else {
                    Logger.debug("Ignoring MRL with scheme '{}'", scheme);
                }
            }
            catch(URISyntaxException e) {
                // Can't do anything, return the original string
                Logger.debug("Can not obtain a valid URI from the MRL");
            }
        }
        else {
            Logger.debug("MRL does not contain any Unicode characters");
        }
        Logger.debug("result={}", result);
        return result;
    }

    /**
     * Does a String contain any Unicode characters?
     *
     * @param value string to test
     * @return <code>true</code> if the supplied String contains any Unicode characters; <code>false</code> if it does not
     */
    private static boolean containsUnicode(String value) {
        boolean result = false;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) >= '\u0080') {
                result = true;
                break;
            }
        }
        return result;
    }
}
