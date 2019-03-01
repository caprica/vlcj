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

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.Kernel32;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.binding.support.size_t;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;

import java.nio.ByteBuffer;

/**
 *
 */
final class NativeBuffers {

    /**
     *
     */
    private final boolean lockBuffers;

    /**
     * Native memory buffers, one for each plane.
     */
    private ByteBuffer[] nativeBuffers;

    /**
     * Native memory pointers to each byte buffer.
     */
    private Pointer[] pointers;

    /**
     *
     */
    NativeBuffers(boolean lockBuffers) {
        this.lockBuffers = lockBuffers;
    }

    /**
     *
     * Memory must be aligned correctly (on a 32-byte boundary) for the libvlc API functions, this is all taken care of
     * by the {@link ByteBufferFactory}.
     *
     * @param bufferFormat
     * @return
     */
    int allocate(BufferFormat bufferFormat) {
        int planeCount = bufferFormat.getPlaneCount();
        int[] pitchValues = bufferFormat.getPitches();
        int[] lineValues = bufferFormat.getLines();
        nativeBuffers = new ByteBuffer[planeCount];
        pointers = new Pointer[planeCount];
        for (int i = 0; i < planeCount; i ++ ) {
            ByteBuffer buffer = ByteBufferFactory.allocateAlignedBuffer(pitchValues[i] * lineValues[i]);
            nativeBuffers[i] = buffer;
            pointers[i] = Pointer.createConstant(ByteBufferFactory.getAddress(buffer));
            if (lockBuffers) {
                if (!RuntimeUtil.isWindows()) {
                    LibC.INSTANCE.mlock(pointers[i], new NativeLong(buffer.capacity()));
                } else {
                    Kernel32.INSTANCE.VirtualLock(pointers[i], new size_t(buffer.capacity()));
                }
            }
        }
        return nativeBuffers.length;
    }

    void free() {
        if (nativeBuffers != null) {
            if (lockBuffers) {
                for (int i = 0; i < nativeBuffers.length; i++) {
                    if (!RuntimeUtil.isWindows()) {
                        LibC.INSTANCE.munlock(pointers[i], new NativeLong(nativeBuffers[i].capacity()));
                    } else {
                        Kernel32.INSTANCE.VirtualUnlock(pointers[i], new size_t(nativeBuffers[i].capacity()));
                    }
                }
            }
            nativeBuffers = null;
            pointers = null;
        }
    }

    ByteBuffer[] buffers() {
        return nativeBuffers;
    }

    Pointer[] pointers() {
        return pointers;
    }

}
