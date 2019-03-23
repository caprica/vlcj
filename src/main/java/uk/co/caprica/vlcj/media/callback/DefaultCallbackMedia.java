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

package uk.co.caprica.vlcj.media.callback;

import com.sun.jna.Pointer;

import java.io.IOException;

/**
 * Implementation of a {@link CallbackMedia} media that uses a Java byte array for an IO buffer when reading media data.
 * <p>
 * A sub-class need only provide an implementation for {@link #onRead(byte[], int)}.
 * <p>
 * This implementation uses a Java byte array, you can instead get direct access to the native buffer by sub-classing
 * {@link AbstractCallbackMedia} directly.
 */
public abstract class DefaultCallbackMedia extends AbstractCallbackMedia {

    /**
     * Default IO buffer size.
     */
    private static final int DEFAULT_BUFFER_SIZE = 10240;

    /**
     * IO buffer.
     */
    private final byte[] ioBuffer;

    /**
     * Create a new media instance with a default IO buffer size.
     *
     * @param seekable <code>true</code> if the media is seekable; <code>false</code> if it is not
     */
    public DefaultCallbackMedia(boolean seekable) {
        this(seekable, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Create a new media instance.
     *
     * @param seekable <code>true</code> if the media is seekable; <code>false</code> if it is not
     * @param ioBufferSize IO buffer size
     */
    public DefaultCallbackMedia(boolean seekable, int ioBufferSize) {
        super(seekable);
        this.ioBuffer = new byte[ioBufferSize];
    }

    @Override
    protected final int onRead(Pointer buffer, int bufferSize) throws IOException {
        int bytesRead = onRead(ioBuffer, Math.min(ioBuffer.length, bufferSize));
        if (bytesRead > 0) {
            buffer.write(0, ioBuffer, 0, bytesRead);
        }
        return bytesRead;
    }

    /**
     * Template method used by sub-classes to populate the buffer with media data.
     * <p>
     * It <em>is</em> allowable for implementations to block in this method waiting for IO, but care must be taken not
     * to block indefinitely otherwise the native media player can not be stopped.
     *
     * @param buffer IO buffer
     * @param bufferSize maximum number of bytes to fill
     * @return number of bytes read, or -1 if the end of the media was reached
     * @throws IOException if an error occurs
     */
    protected abstract int onRead(byte[] buffer, int bufferSize) throws IOException;

}
