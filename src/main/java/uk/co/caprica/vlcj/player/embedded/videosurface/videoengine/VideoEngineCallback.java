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
 * Specification for a component that provides a bridge from the native video engine to a rendering surface.
 */
public interface VideoEngineCallback {

    /**
     * Client setup.
     *
     * @param opaque opaque data pointer
     * @param deviceConfiguration device configuration
     * @param deviceInformation output device information
     * @return <code>true</code> on success; <code>false</code> on error
     */
    boolean onSetup(Long opaque, libvlc_video_setup_device_cfg_t deviceConfiguration, libvlc_video_setup_device_info_t deviceInformation);

    /**
     * Client clean-up.
     *
     * @param opaque opaque data pointer
     */
    void onCleanup(Long opaque);

    /**
     * Set the window callback.
     * <p>
     * The resize callback must be invoked by the application when the size of the video display surface changes (e.g.
     * due to a window resize event). It can also be used to send mouse events.
     *
     * @param windowCallback window callback
     */
    void onSetWindowCallback(VideoEngineWindowCallback windowCallback);

    /**
     * Update the video output with new dimensions.
     * <p>
     * <em>Note that this currently exposes native structures via renderConfig and outputConfig, this may change in the
     * future.</em>
     *
     * @param opaque opaque data pointer
     * @param renderConfig render configuration
     * @param outputConfig output configuration
     * @return <code>true</code> on success; <code>false</code> on error
     */
    boolean onUpdateOutput(Long opaque, libvlc_video_render_cfg_t renderConfig, libvlc_video_output_cfg_t outputConfig);

    /**
     * A batch of native rendering calls finished.
     *
     * @param opaque opaque data pointer
     */
    void onSwap(Long opaque);

    /**
     * Set or unset the current rendering context.
     *
     * @param opaque opaque data pointer
     * @param enter <code>true</code> if the context should be set; <code>false</code> if it should be un-set
     * @return <code>true</code> on success; <code>false</code> on error
     */
    boolean onMakeCurrent(Long opaque, boolean enter);

    /**
     * Get a pointer to a native procedure.
     *
     * @param opaque opaque data pointer
     * @param functionName native procedure name
     * @return native procedure address
     */
    long onGetProcAddress(Long opaque, String functionName);
}
