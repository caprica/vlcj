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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_cleanup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_format_cb;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_callbacks;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_format_callbacks;

/**
 * Implementation of a video surface that uses native callbacks to receive video frame data for rendering.
 */
public class CallbackVideoSurface extends VideoSurface {

    private final libvlc_video_format_cb setup = new SetupCallback();
    private final libvlc_video_cleanup_cb cleanup = new CleanupCallback();
    private final libvlc_lock_callback_t lock = new LockCallback();
    private final libvlc_unlock_callback_t unlock = new UnlockCallback();
    private final libvlc_display_callback_t display = new DisplayCallback();

    private final BufferFormatCallback bufferFormatCallback;
    private final RenderCallback renderCallback;

    private final NativeBuffers nativeBuffers;

    private MediaPlayer mediaPlayer;

    private BufferFormat bufferFormat;

    /**
     * Create a video surface.
     *
     * @param bufferFormatCallback callback providing the video buffer format
     * @param renderCallback callback used to render the video frame buffer
     * @param lockBuffers <code>true</code> if the video buffer should be locked; <code>false</code> if not
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public CallbackVideoSurface(BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback, boolean lockBuffers, VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);

        this.bufferFormatCallback = bufferFormatCallback;
        this.renderCallback = renderCallback;
        this.nativeBuffers = new NativeBuffers(lockBuffers);
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        libvlc_video_set_format_callbacks(mediaPlayer.mediaPlayerInstance(), setup, cleanup);
        libvlc_video_set_callbacks(mediaPlayer.mediaPlayerInstance(), lock, unlock, display, null);
    }

    /**
     * Implementation of a callback invoked by the native library to set up the required video buffer characteristics.
     *
     * This callback is invoked when the video format changes.
     */
    private final class SetupCallback implements libvlc_video_format_cb {

        @Override
        public int format(PointerByReference opaque, PointerByReference chroma, IntByReference width, IntByReference height, PointerByReference pitches, PointerByReference lines) {
            bufferFormat = bufferFormatCallback.getBufferFormat(width.getValue(), height.getValue());
            applyBufferFormat(bufferFormat, chroma, width, height, pitches, lines);
            int result = nativeBuffers.allocate(bufferFormat);
            bufferFormatCallback.allocatedBuffers(nativeBuffers.buffers());
            return result;
        }

        /**
         * Set the desired video format properties - space for these structures is already allocated by LibVlc, we
         * simply fill the existing memory.
         * <p>
         * The {@link BufferFormat} class restricts the chroma to maximum four bytes, so we don't need check it here, we
         * do however need to check if it is less than four.
         *
         * @param chroma
         * @param width
         * @param height
         * @param pitches
         * @param lines
         */
        private void applyBufferFormat(BufferFormat bufferFormat, PointerByReference chroma, IntByReference width, IntByReference height, PointerByReference pitches, PointerByReference lines) {
            byte[] chromaBytes = bufferFormat.getChroma().getBytes();
            chroma.getPointer().write(0, chromaBytes, 0, chromaBytes.length < 4 ? chromaBytes.length : 4);
            width.setValue(bufferFormat.getWidth());
            height.setValue(bufferFormat.getHeight());
            int[] pitchValues = bufferFormat.getPitches();
            int[] lineValues = bufferFormat.getLines();
            pitches.getPointer().write(0, pitchValues, 0, pitchValues.length);
            lines.getPointer().write(0, lineValues, 0, lineValues.length);
        }

    }

    /**
     * Implementation of a callback invoked by the native library to clean up previously allocated video buffers.
     *
     * This callback is invoked when the video buffer is no longer needed.
     */
    private final class CleanupCallback implements libvlc_video_cleanup_cb {

        @Override
        public void cleanup(Pointer opaque) {
            nativeBuffers.free();
        }

    }

    /**
     * Implementation of a callback invoked by the native library to prepare the video buffer(s) for rendering a video
     * frame.
     *
     * This callback is invoked every frame.
     */
    private final class LockCallback implements libvlc_lock_callback_t {

        @Override
        public Pointer lock(Pointer opaque, PointerByReference planes) {
            Pointer[] pointers = nativeBuffers.pointers();
            planes.getPointer().write(0, pointers, 0, pointers.length);
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
            CallbackVideoSurface.this.renderCallback.display(mediaPlayer, nativeBuffers.buffers(), bufferFormat);
        }

    }

}
