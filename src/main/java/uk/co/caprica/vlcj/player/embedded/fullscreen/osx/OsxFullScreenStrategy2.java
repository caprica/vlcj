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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.fullscreen.osx;

import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.Window;

/**
 * Implementation of a full-screen strategy for macOS with a JDK later than Java 8.
 * <p>
 * The EAWT classes are no longer available in Java applications using the module system.
 */
public class OsxFullScreenStrategy2 implements FullScreenStrategy {

    /**
     * The component that will be made full-screen.
     */
    private final Window window;

    private final Frame frame;

    /**
     * Is the window currently in full-screen mode?
     */
    private boolean isFullScreenMode;

    public OsxFullScreenStrategy2(Window window) {
        if (window != null) {
            this.window = window;
            this.frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, window);
            if (this.frame != null) {
                throw new IllegalArgumentException(("Window must have a Frame ancestor"));
            }
        }
        else {
            throw new IllegalArgumentException("Window must not be null");
        }
    }

    @Override
    public void enterFullScreenMode() {
        // According to the OpenJDK bug tracker, this is supposed to suffice, but it only maximuses the frame
        // to fit the screen, it does NOT enter true native full-screen mode - there is currently no solution
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        isFullScreenMode = true;
    }

    @Override
    public void exitFullScreenMode() {
        frame.setExtendedState(Frame.NORMAL);
        isFullScreenMode = false;
    }

    @Override
    public boolean isFullScreenMode() {
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
