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

package uk.co.caprica.vlcj.player.embedded.fullscreen.x;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import java.awt.*;

/**
 * Implementation of a full-screen strategy that uses the native X11 window manager.
 * <p>
 * With this full-screen strategy, a full-screen transparent overlay <em>will</em> work correctly,
 * see {@link EmbeddedMediaPlayer#overlay()}.
 */
public class XFullScreenStrategy implements FullScreenStrategy {

    /**
     * The component that will be made full-screen.
     */
    private final Window window;

    /**
     * Is the window currently in full-screen mode?
     */
    private boolean isFullScreenMode;

    /**
     * Create a new full-screen strategy.
     *
     * @param window component that will be made full-screen
     */
    public XFullScreenStrategy(Window window) {
        if (window != null) {
            this.window = window;
        }
        else {
            throw new IllegalArgumentException("Window must not be null");
        }
    }

    @Override
    public final void enterFullScreenMode() {
        onBeforeEnterFullScreenMode();
        XFullScreenHandler.setFullScreenWindow(window, true);
        isFullScreenMode = true;
    }

    @Override
    public final void exitFullScreenMode() {
        XFullScreenHandler.setFullScreenWindow(window, false);
        isFullScreenMode = false;
        onAfterExitFullScreenMode();
    }

    @Override
    public final boolean isFullScreenMode() {
        return isFullScreenMode;
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
