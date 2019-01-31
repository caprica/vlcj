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

package uk.co.caprica.vlcj.binding;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Functions that deal with URI's that are passed to the native LibVlc library.
 */
public final class NativeUri {

    /**
     * Encode, if needed, a local file MRL that may contain Unicode characters as a file URL with "percent" encoding.
     * <p>
     * This method deals with the special case of an MRL for a local file name containing Unicode characters. Such MRLs
     * must be encoded as file URLs, by adding a "file://" prefix before percent-encoding the filename.
     * <p>
     * Without this, vlc will not be able to play the file since it is using native API that can not handle unencoded
     * Unicode characters.
     * <p>
     * This method does not deal with any MRLs that are URLs since Unicode characters are forbidden by specification for
     * any URL.
     * <p>
     * What this means in practical terms is that if an MRL is specified that contains a "scheme" like "http", or "file"
     * then that MRL will <em>not</em> be encoded by this method, even if it contains Unicode characters. This situation
     * if it arises, is considered a client application vaildation failure.
     *
     * @param uri URI
     * @return the original URI if no encoding is required, or a percent-encoded ASCII file URI
     */
    public static String encodeUri(String uri) {
        String result = uri;
        if (containsUnicode(uri)) {
            try {
                URI validUri = new URI(uri);
                if (validUri.getScheme() == null) {
                    result = toLocalFileUri(uri);
                }
            }
            catch (URISyntaxException e) {
                if (RuntimeUtil.isWindows()) {
                    result = toLocalFileUri(uri);
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
     * Encode a string using ASCII escape sequences as necessary.
     * <p>
     * According to {@link File#toURI()} Javadoc, the format of the URI is system-dependent - this means that we can not
     * rely on the file URI starting "file:/" (bad) or "file://" good, so we must account for either case.
     * <p>
     * With JDK 1.7+ we could have used the Path class to get a URI in the proper format.
     * <p>
     * Implementation notes:
     * <p>
     * The main concern is to check if the URI starts "file:/", or "file://", clearly care must be taken with this test,
     * since the leading characters match, so we must base our condition on the longer string.
     * <p>
     * If the URI starts with "file://", we are done at this point and return.
     * <p>
     * If the URI starts with "file:/", as is almost certainly the case when using {@link File#toURI()}, then we apply a
     * replacement by dropping "file:/" and adding "file:///" in its place. When replacing in this manner, a third slash
     * is added to separate the unused "authority" part of the URI (this could in theory have been something like
     * "file://localhost/path/file.ext", so we return it as "file:///path/file.ext" instead).
     *
     * @param value value to encode
     * @return encoded value
     */
    private static String toLocalFileUri(String value) {
        String asciiString = new File(value).toURI().toASCIIString();
        if (asciiString.startsWith("file://")) {
            // URI already starts with "file://", so simply return the ASCII string
            return asciiString;
        } else {
            // URI therefore starts by so replace the bad prefix with a proper one
            return asciiString.replaceFirst("file:/", "file:///");
        }
    }

    private NativeUri() {
    }

}
