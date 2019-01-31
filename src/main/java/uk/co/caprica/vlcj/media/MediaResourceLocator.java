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

package uk.co.caprica.vlcj.media;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Utility class to help detect the type of media resource locator.
 * <p>
 * This is needed since local files must be played differently to non-local MRLs
 * like streaming URLs or "screen://".
 * <p>
 * This is essentially an internal class.
 */
final class MediaResourceLocator {

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
    static boolean isLocation(String mrl) {
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
    static String encodeMrl(String mrl) {
        String result = mrl;
        if (containsUnicode(mrl)) {
            try {
                URI uri = new URI(mrl);
                if (uri.getScheme() == null) {
                    result = encode(mrl);
                }
            }
            catch (URISyntaxException e) {
                if (RuntimeUtil.isWindows()) {
                    if (isAbsolutePath(mrl)) {
                        result = encode(mrl);
                    }
                }
            }
        }
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

    /**
     * Tests if the supplied value represents an absolute filesystem path.
     *
     * @param value value to test
     * @return <code>true</code> if the supplied String represents an absolute filesystem path; <code>false</code> for a local path
     */
    private static boolean isAbsolutePath(String value) {
        return new File(value).isAbsolute();
    }

    /**
     * Encode a string using ASCII escape sequences as necessary.
     *
     * @param value value to encode
     * @return encoded value
     */
    private static String encode(String value) {
        return new File(value).toURI().toASCIIString().replaceFirst("file:/", "file:///");
    }

}
