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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.windows;

import java.awt.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

/**
 * Implementation of a full screen strategy that uses the native Win32 API.
 */
public class Win32FullScreenStrategy implements FullScreenStrategy {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(Win32FullScreenStrategy.class);

    /**
     * Native full-screen implementation.
     */
    private final Win32FullScreenHandler handler;

    /**
     * Is the window currently in full-screen mode?
     */
    private boolean isFullScreenMode;

    /**
     * Create a new full-screen strategy.
     *
     * @param window component that will be made full-screen
     */
    public Win32FullScreenStrategy(Window window) {
        if(window != null) {
            this.handler = new Win32FullScreenHandler(window);
        }
        else {
            throw new IllegalArgumentException("Window must not be null");
        }
    }

    @Override
    public final void enterFullScreenMode() {
        logger.debug("enterFullScreenMode()");
        if(!isFullScreenMode) {
            onBeforeEnterFullScreenMode();
            handler.setFullScreen(true);
            isFullScreenMode = true;
        }
    }

    @Override
    public final void exitFullScreenMode() {
        logger.debug("exitFullScreenMode()");
        if(isFullScreenMode) {
            handler.setFullScreen(false);
            isFullScreenMode = false;
            onAfterExitFullScreenMode();
        }
    }

    @Override
    public final boolean isFullScreenMode() {
        logger.debug("isFullScreenMode()");
        return isFullScreenMode;
    }

    /**
     * Template method invoked before full-screen mode is entered.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode
     * for example to hide other on-screen components.
     */
    protected void onBeforeEnterFullScreenMode() {
    }

    /**
     * Template method invoked after exiting full-screen mode.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode
     * for example to restore other on-screen components.
     */
    protected void onAfterExitFullScreenMode() {
    }
}
