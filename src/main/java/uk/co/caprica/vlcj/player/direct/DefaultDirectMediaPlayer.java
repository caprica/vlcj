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

package uk.co.caprica.vlcj.player.direct;

import java.util.concurrent.Semaphore;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.DefaultMediaPlayer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * Media player implementation that provides direct access to the video frame data.
 * <p>
 * For the pixel format you can use any format that is supported by the vlc video output, for
 * example: </ul>
 * <li>I420: planar 4:2:0, order YUV</li>
 * <li>YV12: planar 4:2:0, order YVU</li>
 * <li>YUY2: packed 4:2:2, order YUYV</li>
 * <li>UYVY: packed 4:2:2, order UYVY</li>
 * <li>RV32: 24-bits depth with 8-bits padding</li>
 * <li>RV24: 24-bits depth (like HTML colours)</li>
 * <li>RV16: 16-bits depth</li>
 * <li>RV15: 15-bits depth (5 per component), 1 bit padding</li>
 * </ul> This list may not be exhaustive.
 */
public class DefaultDirectMediaPlayer extends DefaultMediaPlayer implements DirectMediaPlayer {

    /**
     * Use a semaphore with a single permit to ensure that the lock, display, unlock cycle goes in a
     * serial manner.
     */
    private final Semaphore semaphore = new Semaphore(1);

    /**
     * Video buffer pixel format.
     */
    private final String format;

    /**
     * Video buffer width.
     */
    private final int width;

    /**
     * Video buffer height.
     */
    private final int height;

    /**
     * Video buffer pitch (or "stride").
     */
    private final int pitch;

    /**
     * Component to call-back for each video frame.
     */
    private final RenderCallback renderCallback;

    /**
     * Native memory buffer.
     */
    private final Memory nativeBuffer;

    /**
     * Lock call-back.
     * <p>
     * A hard reference to the call-back must be kept otherwise the call-back will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_lock_callback_t lock;

    /**
     * Unlock call-back.
     * <p>
     * A hard reference to the call-back must be kept otherwise the call-back will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_unlock_callback_t unlock;

    /**
     * Display call-back.
     * <p>
     * A hard reference to the call-back must be kept otherwise the call-back will get garbage
     * collected and cause a native crash.
     */
    private final libvlc_display_callback_t display;

    /**
     * Create a new media player.
     * 
     * @param libvlc native library interface
     * @param instance libvlc instance
     * @param width width for the video
     * @param height height for the video
     * @param format pixel format (e.g. RV15, RV16, RV24, RV32, RGBA, YUYV)
     * @param pitch pitch, also known as stride
     * @param renderCallback call-back to receive the video frame data
     */
    public DefaultDirectMediaPlayer(LibVlc libvlc, libvlc_instance_t instance, String format, int width, int height, int pitch, RenderCallback renderCallback) {
        super(libvlc, instance);

        this.format = format;
        this.width = width;
        this.height = height;
        this.pitch = pitch;
        this.renderCallback = renderCallback;

        // Memory must be aligned correctly (on a 32-byte boundary) for the libvlc
        // API functions (extra bytes are allocated to allow for enough memory if
        // the alignment needs to be changed)
        this.nativeBuffer = new Memory(width * height * 4 + 32).align(32);

        this.lock = new libvlc_lock_callback_t() {
            @Override
            public Pointer lock(Pointer opaque, Pointer plane) {
                Logger.trace("lock");
                // Acquire the single permit from the semaphore to ensure that the
                // memory buffer is not trashed while display() is invoked
                Logger.trace("acquire");
                semaphore.acquireUninterruptibly();
                Logger.trace("acquired");
                plane.setPointer(0, nativeBuffer);
                Logger.trace("lock finished");
                return null;
            }
        };

        this.unlock = new libvlc_unlock_callback_t() {
            @Override
            public void unlock(Pointer opaque, Pointer picture, Pointer plane) {
                Logger.trace("unlock");
                // Release the semaphore
                Logger.trace("release");
                semaphore.release();
                Logger.trace("released");
                Logger.trace("unlock finished");
            }
        };

        this.display = new libvlc_display_callback_t() {
            @Override
            public void display(Pointer opaque, Pointer picture) {
                Logger.trace("display");
                // Invoke the call-back
                DefaultDirectMediaPlayer.this.renderCallback.display(nativeBuffer);
                Logger.trace("display finished");
            }
        };

        // Set the desired video buffer format
        libvlc.libvlc_video_set_format(mediaPlayerInstance(), format, width, height, pitch);
        // Install the video callbacks
        libvlc.libvlc_video_set_callbacks(mediaPlayerInstance(), lock, unlock, display, null);
    }

    /**
     * Get the buffer format.
     * 
     * @return format
     */
    public String format() {
        return format;
    }

    /**
     * Get the buffer width.
     * 
     * @return width
     */
    public int width() {
        return width;
    }

    /**
     * Get the buffer height.
     * 
     * @return height
     */
    public int height() {
        return height;
    }

    /**
     * Get the buffer pitch.
     * 
     * @return pitch
     */
    public int pitch() {
        return pitch;
    }
}
