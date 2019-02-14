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

package uk.co.caprica.vlcj.factory;

/**
 * Utility class to help determine the native library search path.
 */
final class NativeLibraryPath {

    /**
     * Parse out the complete file path of the native library.
     * <p>
     * This depends on the format of the toString() of the JNA implementation class.
     * <p>
     * The path may not be available on all platforms.
     *
     * @param library native library instance
     * @return native library path, or simply the toString of the instance if the path could not be parsed out
     */
    static String getNativeLibraryPath(Object library) {
        String s = library.toString();
        int start = s.indexOf('<');
        if(start != -1) {
            start ++ ;
            int end = s.indexOf('@', start);
            if(end != -1) {
                s = s.substring(start, end);
                return s;
            }
        }
        return s;
    }

    private NativeLibraryPath() {
    }

}
