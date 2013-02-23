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

import uk.co.caprica.vlcj.binding.LibVlc;

import com.sun.jna.Pointer;

/**
 * Encapsulation of access to native strings.
 * <p>
 * This class takes care of freeing the memory that was natively allocated for the string.
 */
public final class NativeString {

    /**
     * Prevent direct instantiation by others.
     */
    private NativeString() {
    }

    /**
     * Get a String from a native string pointer, freeing the native string pointer when done.
     * <p>
     * If the native string pointer is not freed then a native memory leak will occur.
     *
     * @param pointer pointer to native string, may be <code>null</code>
     * @return string, or <code>null</code> if the pointer was <code>null</code>
     */
    public static final String getNativeString(LibVlc libvlc, Pointer pointer) {
        if(pointer != null) {
            String result = pointer.getString(0, false);
            libvlc.libvlc_free(pointer);
            return result;
        }
        else {
            return null;
        }
    }
}
