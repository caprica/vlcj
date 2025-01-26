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

package uk.co.caprica.vlcj.player.embedded.videosurface.videoengine;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_resize_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_color_primaries_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_color_space_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_getProcAddress_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_makeCurrent_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_orient_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_cleanup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_move_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_press_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_release_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_set_window_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_setup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_render_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_setup_device_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_setup_device_info_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_swap_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_transfer_func_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_update_output_cb;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MouseButton;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_output_callbacks;

/**
 * Implementation of a video surface that bridges native video engine callbacks to a rendering API (like JOGL, LWJGL and
 * so on).
 * <p>
 * The window callback methods <strong>must not</strong> be invoked concurrently:
 * <ul>
 *     <li>{@link #resize(int, int)}</li>
 *     <li>{@link #mouseMoved(int, int)}</li>
 *     <li>{@link #mousePressed(MouseButton)}</li>
 *     <li>{@link #mouseReleased(MouseButton)}</li>
 * </ul>
 */
public final class VideoEngineVideoSurface extends VideoSurface {

    private static final int GL_RGBA = 6408;

    /**
     * Video engine.
     */
    private final VideoEngine engine;

    /**
     * Component used to bridge the native video engine to the rendering surface.
     */
    private final VideoEngineCallback callback;

    private final libvlc_video_output_setup_cb setup = new SetupCallback();
    private final libvlc_video_output_cleanup_cb cleanup = new CleanupCallback();
    private final libvlc_video_output_set_window_cb setWindowCallback = new SetWindowCallback();
    private final libvlc_video_update_output_cb updateOutput = new UpdateOutputCallback();
    private final libvlc_video_swap_cb swap = new SwapCallback();
    private final libvlc_video_makeCurrent_cb makeCurrent = new MakeCurrentCallback();
    private final libvlc_video_getProcAddress_cb getProcAddress = new GetProcAddressCallback();

    /**
     * Handler to bridge the native video engine resize and mouse event callback.
     */
    private VideoEngineWindowCallbackHandler windowCallbackHandler;

    /**
     * Create a video surface.
     *
     * @param engine video engine
     * @param callback component used to bridge the native video engine to the rendering surface
     * @param videoSurfaceAdapter adapter to attach a video surface to a native media player
     */
    public VideoEngineVideoSurface(VideoEngine engine, VideoEngineCallback callback, VideoSurfaceAdapter videoSurfaceAdapter) {
        super(videoSurfaceAdapter);

        this.engine = engine;
        this.callback = callback;
    }

    /**
     * Report a video surface size change to the native video surface.
     *
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        windowCallbackHandler.setSize(width, height);
    }

    /**
     * Report a mouse moved event to the native video surface
     *
     * @param x new mouse x position
     * @param y new mouse y position
     */
    public void mouseMoved(int x, int y) {
        windowCallbackHandler.mouseMoved(x, y);
    }

    /**
     * Report a mouse pressed event to the native video surface.
     *
     * @param mouseButton button that was pressed
     */
    public void mousePressed(MouseButton mouseButton) {
        windowCallbackHandler.mousePressed(mouseButton);
    }

    /**
     * Report a mouse released event to the native video surface.
     *
     * @param mouseButton button that was pressed
     */
    public void mouseReleased(MouseButton mouseButton) {
        windowCallbackHandler.mouseReleased(mouseButton);
    }

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        libvlc_video_set_output_callbacks(
            mediaPlayer.mediaPlayerInstance(),
            engine.intValue(),
            setup,
            cleanup,
            setWindowCallback,
            updateOutput,
            swap,
            makeCurrent,
            getProcAddress,
            null,
            null,
            null);
    }

    private final class SetupCallback implements libvlc_video_output_setup_cb {
        @Override
        public int setup(Long opaque, libvlc_video_setup_device_cfg_t cfg, libvlc_video_setup_device_info_t out) {
            return callback.onSetup(opaque, cfg, out) ? 1 : 0;
        }
    }

    private final class CleanupCallback implements libvlc_video_output_cleanup_cb {
        @Override
        public void cleanup(Long opaque) {
            callback.onCleanup(opaque);
        }
    }

    private final class SetWindowCallback implements libvlc_video_output_set_window_cb {
        @Override
        public void setWindowCallback(Pointer opaque, libvlc_video_output_resize_cb report_size_change, libvlc_video_output_mouse_move_cb report_mouse_move, libvlc_video_output_mouse_press_cb report_mouse_pressed , libvlc_video_output_mouse_release_cb report_mouse_released, Pointer report_opaque) {
            VideoEngineVideoSurface.this.windowCallbackHandler = new VideoEngineWindowCallbackHandler(Pointer.nativeValue(opaque), Pointer.nativeValue(report_opaque), report_size_change, report_mouse_move, report_mouse_pressed, report_mouse_released);
            callback.onSetWindowCallback(windowCallbackHandler);
        }
    }

    private class UpdateOutputCallback implements libvlc_video_update_output_cb {
        @Override
        public int updateOutput(Long opaque, libvlc_video_render_cfg_t cfg, libvlc_video_output_cfg_t output) {
            output.u.writeField("opengl_format", GL_RGBA);
            output.full_range = 1;
            output.colorspace = libvlc_video_color_space_e.libvlc_video_colorspace_BT709.intValue();
            output.primaries = libvlc_video_color_primaries_e.libvlc_video_primaries_BT709.intValue();
            output.transfer = libvlc_video_transfer_func_e.libvlc_video_transfer_func_SRGB.intValue();
            output.orientation = libvlc_video_orient_t.libvlc_video_orient_top_left.intValue();
            // The return value is not used by the native code
            return callback.onUpdateOutput(opaque, cfg, output) ? 1: 0;
        }
    }

    private class SwapCallback implements libvlc_video_swap_cb {
        @Override
        public void swap(Long opaque) {
            callback.onSwap(opaque);
        }
    }

    private class MakeCurrentCallback implements libvlc_video_makeCurrent_cb {
        @Override
        public int makeCurrent(Long opaque, int enter) {
            return callback.onMakeCurrent(opaque, enter != 0) ? 1 : 0;
        }
    }

    private class GetProcAddressCallback implements libvlc_video_getProcAddress_cb {
        @Override
        public Pointer getProcAddress(Long opaque, String fct_name) {
            return Pointer.createConstant(callback.onGetProcAddress(opaque, fct_name));
        }
    }
}
