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
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_close_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_open_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_read_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_seek_cb;
import uk.co.caprica.vlcj.binding.support.types.size_t;

import java.io.IOException;

/**
 * Base implementation of media that uses the native media callbacks.
 * <p>
 * This implementation mostly encapsulates the native callbacks using template methods, with the exception of the
 * {@link #onRead(Pointer, int)} method. This particular method is used to read data and populate the native buffer. The
 * reason this method exposes a native {@link Pointer} is so sub-classes can access the native buffer as efficiently as
 * possible.
 * <p>
 * In most cases it is likely preferable to deal instead with a Java byte array buffer, for this purpose the
 * {@link DefaultCallbackMedia} sub-class should be used instead of this class.
 */
public abstract class AbstractCallbackMedia implements CallbackMedia {

    /**
     * Native API success indicator.
     */
    private static final int SUCCESS = 0;

    /**
     * Native API error indicator.
     */
    private static final int ERROR = -1;

    /**
     * Native API end-of-stream indicator.
     */
    private static final int END_OF_STREAM = 0;

    /**
     * Is the media seekable?
     */
    private final boolean seekable;

    /**
     * Native media open callback.
     */
    private final Open open;

    /**
     * Native media read callback.
     */
    private final Read read;

    /**
     * Native media seek callback.
     */
    private final Seek seek;

    /**
     * Native media close callback.
     */
    private final Close close;

    /**
     * Native opaque data.
     * <p>
     * Currently unused.
     */
    private final Pointer opaque;

    /**
     * Create a new media instance.
     *
     * @param seekable <code>true</code> if the media is seekable; <code>false</code> if it is not
     */
    public AbstractCallbackMedia(boolean seekable) {
        this.seekable = seekable;
        this.open = new Open();
        this.read = new Read();
        this.seek = seekable ? new Seek() : null;
        this.close = new Close();
        this.opaque = null;
    }

    /**
     * Is the media seekable?
     *
     * @return <code>true</code> if the media is seekable; <code>false</code> if it is not
     */
    public final boolean isSeekable() {
        return seekable;
    }

    /**
     * Get the size of the media, if known.
     *
     * @return size of the media, or 0 if the size is not known
     */
    protected abstract long onGetSize();

    /**
     * Open the media.
     *
     * @return <code>true</code> if the media was opened; <code>false</code> if it was not
     */
    protected abstract boolean onOpen();

    /**
     * Read media data.
     * <p>
     * It <em>is</em> allowable for implementations to block in this method waiting for IO, but care must be taken not
     * to block indefinitely otherwise the native media player can not be stopped.
     *
     * @param buffer buffer to write into
     * @param bufferSize capacity of the buffer to write into
     * @return number of bytes read, or -1 if the end of the stream was reached
     * @throws IOException if an error occurs
     */
    protected abstract int onRead(Pointer buffer, int bufferSize) throws IOException;

    /**
     * Seek to a specific offset within the media.
     *
     * @param offset offset within the media to seek to
     * @return <code>true</code> if the seek was successful; <code>false</code> on error
     */
    protected abstract boolean onSeek(long offset);

    /**
     * Close the media.
     */
    protected abstract void onClose();

    /**
     * Implementation of native media open callback.
     */
    private class Open implements libvlc_media_open_cb {

        @Override
        public int open(Pointer opaque, PointerByReference datap, LongByReference sizep) {
            sizep.setValue(onGetSize());
            return onOpen() ? SUCCESS : ERROR;
        }
    }

    /**
     * Implementation of native media read callback.
     */
    private class Read implements libvlc_media_read_cb {

        @Override
        public size_t read(Pointer opaque, Pointer buf, size_t len) {
            int result;
            try {
                int bytesRead = onRead(buf, len.intValue());
                result = bytesRead >= 0 ? bytesRead : END_OF_STREAM;
            }
            catch (IOException e) {
                result = ERROR;
            }
            return new size_t(result);
        }
    }

    /**
     * Implementation of native media seek callback.
     */
    private class Seek implements libvlc_media_seek_cb {

        @Override
        public int seek(Pointer opaque, long offset) {
            return onSeek(offset) ? SUCCESS : ERROR;
        }
    }

    /**
     * Implementation of native media close callback.
     */
    private class Close implements libvlc_media_close_cb {

        @Override
        public void close(Pointer opaque) {
            onClose();
        }
    }

    @Override
    public final libvlc_media_open_cb getOpen() {
        return open;
    }

    @Override
    public final libvlc_media_read_cb getRead() {
        return read;
    }

    @Override
    public final libvlc_media_seek_cb getSeek() {
        return seek;
    }

    @Override
    public final libvlc_media_close_cb getClose() {
        return close;
    }

    @Override
    public final Pointer getOpaque() {
        return opaque;
    }

}
