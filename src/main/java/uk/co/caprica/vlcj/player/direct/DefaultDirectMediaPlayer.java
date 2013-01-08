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

package uk.co.caprica.vlcj.player.direct;

import java.util.concurrent.Semaphore;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_cleanup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_format_cb;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Media player implementation that provides direct access to the video frame data.
 * <p>
 * For the pixel format you can use any format that is supported by the vlc video output, for
 * example:
 * <ul>
 *   <li>I420: planar 4:2:0, order YUV</li>
 *   <li>YV12: planar 4:2:0, order YVU</li>
 *   <li>YUY2: packed 4:2:2, order YUYV</li>
 *   <li>UYVY: packed 4:2:2, order UYVY</li>
 *   <li>RV32: 24-bits depth with 8-bits padding</li>
 *   <li>RV24: 24-bits depth (like HTML colours)</li>
 *   <li>RV16: 16-bits depth</li>
 *   <li>RV15: 15-bits depth (5 per component), 1 bit padding</li>
 * </ul>
 * This list is not exhaustive.
 */
public class DefaultDirectMediaPlayer extends DefaultMediaPlayer implements DirectMediaPlayer {

    /**
     * Use a semaphore with a single permit to ensure that the lock, display, unlock cycle goes in a
     * serial manner.
     */
    private final Semaphore semaphore = new Semaphore(1);

    /**
     * Component to call back to set up video buffers.
     */

    private final BufferFormatCallback bufferFormatCallback;

    /**
     * Component to call back for each video frame.
     */
    private final RenderCallback renderCallback;

    /**
     * Setup callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_video_format_cb setup;

    /**
     * Cleanup callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_video_cleanup_cb cleanup;

    /**
     * Lock callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_lock_callback_t lock;

    /**
     * Unlock callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_unlock_callback_t unlock;

    /**
     * Display callback.
     * <p>
     * A hard reference to the callback must be kept otherwise the callback will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_display_callback_t display;

    /**
     * Format of the native buffers.
     */
    private BufferFormat bufferFormat;

    /**
     * Native memory buffers, one for each plane.
     */
    private Memory[] nativeBuffers;

    /**
     * Create a new media player.
     * <p>
     * This constructor does not support formats that require multiple planes (buffers).
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     * @param width width for the video
     * @param height height for the video
     * @param format pixel format (e.g. RV15, RV16, RV24, RV32, RGBA, YUYV)
     * @param pitch pitch, also known as stride
     * @param renderCallback callback to receive the video frame data
     */
    public DefaultDirectMediaPlayer(LibVlc libvlc, libvlc_instance_t instance, final String format, final int width, final int height, final int pitch, RenderCallback renderCallback) {
        this(libvlc, instance, new DefaultBufferFormatCallback(format, width, height, pitch), renderCallback);
    }

    /**
     * Create a new media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     * @param bufferFormatCallback callback to set the desired buffer format
     * @param renderCallback callback to receive the video frame data
     */
    public DefaultDirectMediaPlayer(LibVlc libvlc, libvlc_instance_t instance, BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback) {
        super(libvlc, instance);
        this.bufferFormatCallback = bufferFormatCallback;
        this.renderCallback = renderCallback;
        // Create the callbacks
        this.setup = new SetupCallback();
        this.cleanup = new CleanupCallback();
        this.lock = new LockCallback();
        this.unlock = new UnlockCallback();
        this.display = new DisplayCallback();
        // Install the native video callbacks
        libvlc.libvlc_video_set_format_callbacks(mediaPlayerInstance(), setup, cleanup);
        libvlc.libvlc_video_set_callbacks(mediaPlayerInstance(), lock, unlock, display, null);
    }

    /**
     * Get the current buffer format.
     *
     * @return the current buffer format
     */
    public final BufferFormat getBufferFormat() {
        return bufferFormat;
    }

    /**
     * Implementation of a callback invoked by the native library to set up the
     * required video buffer characteristics.
     *
     * This callback is invoked when the video format changes.
     */
    private final class SetupCallback implements libvlc_video_format_cb {
        @Override
        public int format(PointerByReference opaque, PointerByReference chroma, IntByReference width, IntByReference height, PointerByReference pitches, PointerByReference lines) {
            Logger.debug("format(chroma={},width={},height={})", chroma.getPointer().getString(0, false), width.getValue(), height.getValue());
            bufferFormat = bufferFormatCallback.getBufferFormat(width.getValue(), height.getValue());
            Logger.debug("bufferFormat={}", bufferFormat);
            if(bufferFormat == null) {
                throw new IllegalStateException("buffer format can not be null");
            }
            // Set the desired video format properties
            byte[] chromaBytes = bufferFormat.getChroma().getBytes();
            // Space for these structures is already allocated by libvlc, we
            // simply fill the existing memory
            chroma.getPointer().write(0, chromaBytes, 0, chromaBytes.length > 4 ? 4 : chromaBytes.length);
            width.setValue(bufferFormat.getWidth());
            height.setValue(bufferFormat.getHeight());
            int[] pitchValues = bufferFormat.getPitches();
            int[] lineValues = bufferFormat.getLines();
            pitches.getPointer().write(0, pitchValues, 0, pitchValues.length);
            lines.getPointer().write(0, lineValues, 0, lineValues.length);
            // Memory must be aligned correctly (on a 32-byte boundary) for the libvlc
            // API functions (extra bytes are allocated to allow for enough memory if
            // the alignment needs to be changed)
            nativeBuffers = new Memory[bufferFormat.getPlaneCount()];
            for(int i = 0; i < bufferFormat.getPlaneCount(); i ++ ) {
                nativeBuffers[i] = new Memory(pitchValues[i] * lineValues[i] + 32).align(32);
            }
            Logger.trace("format finished");
            return pitchValues.length;
        }
    }

    /**
     * Implementation of a callback invoked by the native library to clean up
     * previously allocated video buffers.
     *
     * This callback is invoked when the video buffer is no longer needed.
     */
    private final class CleanupCallback implements libvlc_video_cleanup_cb {
        @Override
        public void cleanup(Pointer opaque) {
            Logger.trace("cleanup");
            if(nativeBuffers != null) {
                nativeBuffers = null;
            }
            Logger.trace("cleanup finished");
        }
    }

    /**
     * Implementation of a callback invoked by the native library to prepare
     * the video buffer(s) for rendering a video frame.
     *
     * This callback is invoked every frame.
     */
    private final class LockCallback implements libvlc_lock_callback_t {
        @Override
        public Pointer lock(Pointer opaque, PointerByReference planes) {
            Logger.trace("lock");
            // Acquire the single permit from the semaphore to ensure that the
            // memory buffer is not trashed while display() is invoked
            Logger.trace("acquire");
            semaphore.acquireUninterruptibly();
            Logger.trace("acquired");
            // Set the pre-allocated buffers to use for each plane
            planes.getPointer().write(0, nativeBuffers, 0, nativeBuffers.length);
            Logger.trace("lock finished");
            return null;
        }
    }

    /**
     * Implementation of a callback invoked by the native library after each
     * video frame.
     *
     * This callback is invoked every frame.
     */
    private final class UnlockCallback implements libvlc_unlock_callback_t {
        @Override
        public void unlock(Pointer opaque, Pointer picture, Pointer plane) {
            Logger.trace("unlock");
            // Release the semaphore
            Logger.trace("release");
            semaphore.release();
            Logger.trace("released");
            Logger.trace("unlock finished");
        }
    }

    /**
     * Implementation of a callback invoked by the native library to render a
     * single frame of video.
     *
     * This callback is invoked every frame.
     */
    private final class DisplayCallback implements libvlc_display_callback_t {
        @Override
        public void display(Pointer opaque, Pointer picture) {
            Logger.trace("display");
            // Invoke the callback
            DefaultDirectMediaPlayer.this.renderCallback.display(DefaultDirectMediaPlayer.this, nativeBuffers, bufferFormat);
            Logger.trace("display finished");
        }
    }

    /**
     * Default implementation of a {@link BufferFormatCallback} to provide a single-
     * plane buffer format that matches exactly the specified video characteristics.
     */
    private static final class DefaultBufferFormatCallback implements BufferFormatCallback {

        /**
         * Buffer format instance.
         */
        private final BufferFormat bufferFormat;

        /**
         * Create a buffer format.
         *
         * @param format chroma/pixel-format
         * @param width video width
         * @param height video height
         * @param pitch pitch (also called "stride")
         */
        private DefaultBufferFormatCallback(String format, int width, int height, int pitch) {
            bufferFormat = new BufferFormat(format, width, height, new int[] {pitch}, new int[] {height});
        }

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return bufferFormat;
        }
    }
}
