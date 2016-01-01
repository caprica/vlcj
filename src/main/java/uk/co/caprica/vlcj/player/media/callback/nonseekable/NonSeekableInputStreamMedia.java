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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.media.callback.nonseekable;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation for non-seekable media using an {@link InputStream}.
 */
public abstract class NonSeekableInputStreamMedia extends NonSeekableCallbackMedia {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(NonSeekableInputStreamMedia.class);

    /**
     * Input stream.
     */
    private InputStream inputStream;

    /**
     * Create a media instance using a default IO buffer size.
     *
     * @param mediaOptions zero or more media options
     */
    public NonSeekableInputStreamMedia(String... mediaOptions) {
        super(mediaOptions);
    }

    /**
     * Create a media instance.
     *
     * @param ioBufferSize IO buffer size
     * @param mediaOptions zero or more media options
     */
    public NonSeekableInputStreamMedia(int ioBufferSize, String... mediaOptions) {
        super(ioBufferSize, mediaOptions);
    }

    @Override
    protected final boolean onOpen() {
        try {
            inputStream = onOpenStream();
            return true;
        }
        catch (IOException e) {
            logger.error("Failed to open stream", e);
            return false;
        }
    }

    @Override
    protected final int onRead(byte[] buffer, int bufferSize) throws IOException {
        return inputStream.read(buffer, 0, bufferSize);
    }

    @Override
    protected final void onClose() {
        try {
            onCloseStream(inputStream);
        }
        catch (IOException e) {
        }
    }

    /**
     * Template method to open a new stream.
     *
     * @return stream
     * @throws IOException if an error occurs
     */
    protected abstract InputStream onOpenStream() throws IOException;

    /**
     * Template method to close the stream.
     *
     * @param inputStream stream to close (the same that was returned by {@link #onOpenStream()}
     * @throws IOException if an error occurs
     */
    protected abstract void onCloseStream(InputStream inputStream) throws IOException;
}
