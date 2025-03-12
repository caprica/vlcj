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

import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_resize_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_move_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_press_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_output_mouse_release_cb;
import uk.co.caprica.vlcj.player.base.MouseButton;

/**
 * Handler component that bridges a vlcj application with the native video engine window callback.
 * <p>
 * The various callback method invocations <strong>must not</strong> be made concurrently.
 */
final class VideoEngineWindowCallbackHandler implements VideoEngineWindowCallback {

    /**
     * Opaque pointer associated with the callbacks.
     */
    private final Long opaque;

    /**
     * Opaque pointer for the native report size changed callback.
     * <p>
     * This pointer <strong>must</strong> be passed with the native callback method.
     */
    private final Long reportOpaque;

    /**
     * Native callback.
     */
    private final libvlc_video_output_resize_cb resize;

    private final libvlc_video_output_mouse_move_cb mouseMove;

    private final libvlc_video_output_mouse_press_cb mousePress;

    private final libvlc_video_output_mouse_release_cb mouseRelease;

    /**
     * Create a window callback handler.
     *
     * @param opaque opaque pointer associated with the callbacks
     * @param reportOpaque opaque pointer for the native report callbacks
     * @param resize native callback for window resizes
     * @param mouseMove native callback for mouse moves
     * @param mousePress native callback for mouse presses
     * @param mouseRelease native callback for mouse releases
     */
    public VideoEngineWindowCallbackHandler(Long opaque, Long reportOpaque, libvlc_video_output_resize_cb resize, libvlc_video_output_mouse_move_cb mouseMove, libvlc_video_output_mouse_press_cb mousePress, libvlc_video_output_mouse_release_cb mouseRelease) {
        this.opaque = opaque;
        this.reportOpaque = reportOpaque;
        this.resize = resize;
        this.mouseMove = mouseMove;
        this.mousePress = mousePress;
        this.mouseRelease = mouseRelease;
    }

    @Override
    public void setSize(int width, int height) {
        if (resize != null) {
            resize.reportSizeChanged(reportOpaque, width, height);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (mouseMove != null) {
            mouseMove.mouseMove(reportOpaque, x, y);
        }
    }

    @Override
    public void mousePressed(MouseButton mouseButton) {
        if (mousePress != null) {
            mousePress.mousePress(reportOpaque, mouseButton.intValue());
        }
    }

    @Override
    public void mouseReleased(MouseButton mouseButton) {
        if (mouseRelease != null) {
            mouseRelease.mouseRelease(reportOpaque, mouseButton.intValue());
        }
    }
}
