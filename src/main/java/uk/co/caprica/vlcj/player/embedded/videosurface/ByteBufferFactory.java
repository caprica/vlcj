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

package uk.co.caprica.vlcj.player.embedded.videosurface;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Factory for creating property aligned native byte buffers.
 * <p>
 * This class uses "unsafe" API which might be restricted/removed in future JDKs.
 * <p>
 * Original credit: http://psy-lob-saw.blogspot.co.uk/2013/01/direct-memory-alignment-in-java.html
 */
final class ByteBufferFactory {

    private static final long addressOffset = getAddressOffset();

    /**
     * Alignment suitable for use by LibVLC video callbacks.
     */
    private static final int LIBVLC_ALIGNMENT = 32;

    /**
     * Allocate a properly aligned native byte buffer, suitable for use by the LibVLC video callbacks.
     *
     * @param capacity required size for the buffer
     * @return aligned byte buffer
     */
    static ByteBuffer allocateAlignedBuffer(int capacity) {
        return allocateAlignedBuffer(capacity, LIBVLC_ALIGNMENT);
    }

    /**
     * Get the value of the native address field from the buffer.
     *
     * @param buffer buffer
     * @return native address pointer
     */
    static long getAddress(ByteBuffer buffer) {
        return UnsafeAccess.UNSAFE.getLong(buffer, addressOffset);
    }

    private static long getAddressOffset() {
        try {
            return UnsafeAccess.UNSAFE.objectFieldOffset(Buffer.class.getDeclaredField("address"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allocate a property aligned native byte buffer.
     *
     * @param capacity required size for the buffer
     * @param alignment alignment alignment
     * @return aligned byte buffer
     */
    private static ByteBuffer allocateAlignedBuffer(int capacity, int alignment) {
        ByteBuffer result;
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity + alignment);
        long address = getAddress(buffer);
        if ((address & (alignment - 1)) == 0) {
            // Stupid cast required see #829
            ((Buffer) buffer).limit(capacity);
            result = buffer.slice().order(ByteOrder.nativeOrder());
        } else {
            int newPosition = (int) (alignment - (address & (alignment - 1)));
            // Stupid casts required see #829
            ((Buffer) buffer).position(newPosition);
            ((Buffer) buffer).limit(newPosition + capacity);
            result = buffer.slice().order(ByteOrder.nativeOrder());
        }
        return result;
    }

    private static class UnsafeAccess {

        private static final Unsafe UNSAFE;

        static {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE = (Unsafe) field.get(null);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private ByteBufferFactory() {
    }

}
