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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.media.callback.seekable;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.Objects;

/**
 * Implementation of seekable callback media that uses a {@link MappedByteBuffer}.
 * <p>
 * The supplied buffer is used as-is, it is the responsibility of the caller to open/close the resources associated with
 * the buffer.
 * <p>
 * Operations on a {@link MappedByteBuffer} are limited to int values by {@link MappedByteBuffer#position(int)}, whereas
 * {@link #onSeek(long)} takes a long.
 * <p>
 * That means this media can not support file sizes greater than {@link Integer#MAX_VALUE}.
 * <p>
 * A custom implementation could support long-based operations by encapsulating multiple {@link MappedByteBuffer}
 * instances at various offsets within the file.
 */
public class MappedByteBufferCallbackMedia extends SeekableCallbackMedia {

    /**
     * Underlying mapped byte buffer.
     */
    @Nullable
    private MappedByteBuffer mappedByteBuffer;

    /**
     * Create a callback media.
     *
     * @param mappedByteBuffer mapped byte buffer
     */
    public MappedByteBufferCallbackMedia(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    /**
     * Create a callback media.
     * <p>
     * This constructor is for sub-classes that provide their own buffer via {@link #getBuffer()}.
     */
    protected MappedByteBufferCallbackMedia() {
    }

    @Override
    protected final long onGetSize() {
        Objects.requireNonNull(mappedByteBuffer);
        return mappedByteBuffer.capacity();
    }

    @Override
    protected final boolean onOpen() {
        // The buffer is initialised like this so sub-classes can provide their own buffer by overriding getBuffer()
        mappedByteBuffer = getBuffer();
        return mappedByteBuffer != null;
    }

    @Override
    protected final int onRead(byte[] buffer, int bufferSize) throws IOException {
        Objects.requireNonNull(mappedByteBuffer);
        int read = Math.min(bufferSize, mappedByteBuffer.remaining());
        mappedByteBuffer.get(buffer, 0, read);
        return read;
    }

    @Override
    protected final boolean onSeek(long offset) {
        Objects.requireNonNull(mappedByteBuffer);
        return mappedByteBuffer.position(((Long) offset).intValue()).position() == offset;
    }

    @Override
    protected void onClose() {
    }

    /**
     * Template method to enable sub-classes to provide their own buffer.
     *
     * @return mapped byte buffer
     */
    protected @Nullable MappedByteBuffer getBuffer() {
        return mappedByteBuffer;
    }
}
