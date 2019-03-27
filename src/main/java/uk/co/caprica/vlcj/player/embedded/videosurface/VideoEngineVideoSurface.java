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
import uk.co.caprica.vlcj.binding.internal.libvlc_video_cleanup_cb;
import uk.co.caprica.vlcj.player.embedded.videosurface.videoengine.VideoEngine;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_getProcAddress_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_makeCurrent_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_setup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_swap_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_update_output_cb;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.videoengine.VideoEngineCallback;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_output_callbacks;

/**
 * Implementation of a video surface that bridges native video engine callbacks to a rendering API (like JOGL, LWJGL and
 * so on).
 */
public final class VideoEngineVideoSurface extends VideoSurface {

    /**
     * Video engine.
     */
    private final VideoEngine engine;

    /**
     * Component used to bridge the native video engine to the rendering surface.
     */
    private final VideoEngineCallback callback;

    private final libvlc_video_setup_cb setup = new SetupCallback();
    private final libvlc_video_cleanup_cb cleanup = new CleanupCallback();
    private final libvlc_video_update_output_cb updateOutput = new UpdateOutputCallback();
    private final libvlc_video_swap_cb swap = new SwapCallback();
    private final libvlc_video_makeCurrent_cb makeCurrent = new MakeCurrentCallback();
    private final libvlc_video_getProcAddress_cb getProcAddress = new GetProcAddressCallback();

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

    @Override
    public void attach(MediaPlayer mediaPlayer) {
        libvlc_video_set_output_callbacks(mediaPlayer.mediaPlayerInstance(), engine.intValue(), setup, cleanup, updateOutput, swap, makeCurrent, getProcAddress, null);
    }

    private final class SetupCallback implements libvlc_video_setup_cb {
        @Override
        public int setup(Pointer opaque) {
            return callback.onSetup(opaque) ? 1 : 0;
        }
    }

    private final class CleanupCallback implements libvlc_video_cleanup_cb {
        @Override
        public void cleanup(Pointer opaque) {
            callback.onCleanup(opaque);
        }
    }

    private class UpdateOutputCallback implements libvlc_video_update_output_cb {
        @Override
        public void updateOutput(Pointer opaque, int width, int height) {
            callback.onUpdateOutput(opaque, width, height);
        }
    }

    private class SwapCallback implements libvlc_video_swap_cb {
        @Override
        public void swap(Pointer opaque) {
            callback.onSwap(opaque);
        }
    }

    private class MakeCurrentCallback implements libvlc_video_makeCurrent_cb {
        @Override
        public int makeCurrent(Pointer opaque, int enter) {
            return callback.onMakeCurrent(opaque, enter != 0) ? 1 : 0;
        }
    }

    private class GetProcAddressCallback implements libvlc_video_getProcAddress_cb {
        @Override
        public Pointer getProcAddress(Pointer opaque, String fct_name) {
            return Pointer.createConstant(callback.onGetProcAddress(opaque, fct_name));
        }
    }

}
