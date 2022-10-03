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
 * Copyright 2009-2022 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface.videoengine;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.ReportSizeChanged;

/**
 * Handler component that bridges a vlcj application with the native video engine resize callback.
 */
public final class VideoEngineResizeCallbackHandler implements VideoEngineResizeCallback {

    /**
     * Opaque pointer associated with the callbacks.
     */
    private final Pointer opaque;

    /**
     * Opaque pointer for the native report size changed callback.
     * <p>
     * This pointer <strong>must</strong> be passed with the native callback method.
     */
    private final Pointer reportOpaque;

    /**
     * Native callback.
     */
    private final ReportSizeChanged reportSizeChanged;

    /**
     * Create a resize callback handler.
     *
     * @param opaque opaque pointer associated with the callbacks
     * @param reportOpaque opaque pointer for the native report size changed callback
     * @param reportSizeChanged native callback
     */
    public VideoEngineResizeCallbackHandler(Pointer opaque, Pointer reportOpaque, ReportSizeChanged reportSizeChanged) {
        this.opaque = opaque;
        this.reportOpaque = reportOpaque;
        this.reportSizeChanged = reportSizeChanged;
    }

    @Override
    public void setSize(int width, int height) {
        if (reportSizeChanged != null) {
            reportSizeChanged.reportSizeChanged(reportOpaque, width, height);
        }
    }
}
