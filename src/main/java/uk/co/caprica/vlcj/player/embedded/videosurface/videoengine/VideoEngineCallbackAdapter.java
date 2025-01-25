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
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_render_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_setup_device_cfg_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_setup_device_info_t;

/**
 * Base implementation for a video engine callback.
 * <p>
 * Default implementations are provided only where they make sense.
 */
public abstract class VideoEngineCallbackAdapter implements VideoEngineCallback {

    @Override
    public boolean onSetup(Long opaque, libvlc_video_setup_device_cfg_t deviceConfiguration, libvlc_video_setup_device_info_t deviceInformation) {
        return true;
    }

    @Override
    public void onCleanup(Long opaque) {
    }

    @Override
    public void onSetWindowCallback(VideoEngineWindowCallback windowCallback) {
    }

    @Override
    public boolean onUpdateOutput(Long opaque, libvlc_video_render_cfg_t renderConfig, libvlc_video_output_cfg_t outputConfig) {
        return true;
    }
}
