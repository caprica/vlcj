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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding;

import java.nio.ByteBuffer;

import com.sun.jna.*;

/**
 * Minimal interface to the standard "C" library.
 */
public interface LibC extends Library {

    /**
     * Native library instance.
     */
    LibC INSTANCE = (LibC)Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), LibC.class);

    /**
     * Format a string with a variable arguments list into a fixed size buffer.
     * <p>
     * The resultant string will be truncated to the size of the buffer if it would
     * otherwise exceed it.
     * <p>
     * For example, if the buffer has a capacity of 10 this is a maximum of 9
     * characters plus a null terminator for a total capacity of 10. This means if
     * 10 characters are required, the buffer capacity must be 11 to accommodate
     * the null terminator.
     *
     * @param str buffer
     * @param size capacity of the buffer, including space for a null terminator
     * @param format format string
     * @param args format arguments
     * @return length of the formatted string, which may exceed the capacity of the buffer, or less than zero on error
     */
    int vsnprintf(ByteBuffer str, int size, String format, Pointer args);


    /**
     * Locks (pinns) parts of virtual address space into RAM so it can not be swapped out.
     *
     * http://linux.die.net/man/2/mlock
     *
     * @param addr address pointer
     * @param length length
     * @return 0 is successful or -1 if not and sets errno to an error code
     */
    int mlock(Pointer addr, NativeLong length);
}
