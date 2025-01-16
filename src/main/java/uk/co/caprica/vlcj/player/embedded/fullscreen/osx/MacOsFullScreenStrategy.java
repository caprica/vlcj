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

package uk.co.caprica.vlcj.player.embedded.fullscreen.osx;

import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import java.awt.Window;

/**
 * Implementation of a full-screen strategy that does nothing on macOS.
 * <p>
 * The EAWT classes are not exported for use on contemporary JDKs and there is no satisfactory replacement, so
 * full-screen on macOS is not supported (at least not by vlcj-core).
 */
public class MacOsFullScreenStrategy implements FullScreenStrategy {

    public MacOsFullScreenStrategy(Window window) {
    }

    @Override
    public void enterFullScreenMode() {
        System.err.println("Full-screen not supported on macOS");
    }

    @Override
    public void exitFullScreenMode() {
        System.err.println("Full-screen not supported on macOS");
    }

    @Override
    public boolean isFullScreenMode() {
        System.err.println("Full-screen not supported on macOS");
        return false;
    }

    /**
     * Template method invoked before full-screen mode is entered.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode for example to hide
     * other on-screen components.
     */
    protected void onBeforeEnterFullScreenMode() {
    }

    /**
     * Template method invoked after exiting full-screen mode.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode for example to
     * restore other on-screen components.
     */
    protected void onAfterExitFullScreenMode() {
    }
}
