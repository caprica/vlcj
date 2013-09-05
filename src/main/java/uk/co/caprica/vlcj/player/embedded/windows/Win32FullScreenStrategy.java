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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.windows;

import java.awt.Window;

import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

/**
 * Implementation of a full screen strategy that uses the native Win32 API.
 */
public final class Win32FullScreenStrategy implements FullScreenStrategy {

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
    public void enterFullScreenMode() {
        if(!isFullScreenMode) {
            handler.setFullScreen(true);
            isFullScreenMode = true;
        }
    }

    @Override
    public void exitFullScreenMode() {
        if(isFullScreenMode) {
            handler.setFullScreen(false);
            isFullScreenMode = false;
        }
    }

    @Override
    public boolean isFullScreenMode() {
        return isFullScreenMode;
    }
}
