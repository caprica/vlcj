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

package uk.co.caprica.vlcj.player.embedded;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.Arrays;

import uk.co.caprica.vlcj.logger.Logger;

/**
 * Default implementation of a full-screen strategy that attempts to use the JDK full-screen
 * exclusive mode support.
 * <p>
 * Client applications may wish to select a screen device other than the default - extend this class
 * and override {@link #getScreenDevice()} to do so.
 * <p>
 * Client applications may wish to explicitly set the DisplayMode - extend this class and override
 * {@link #getDisplayMode(DisplayMode[])} to do so.
 * <p>
 * Client applications may also have other requirements such as hiding other on-screen controls when
 * in full-screen mode - extend this class and over- ride {@link #onBeforeEnterFullScreenMode} and
 * {@link #onAfterExitFullScreenMode()} to do so.
 */
public class DefaultFullScreenStrategy implements FullScreenStrategy {

    /**
     * The component that will be made full-screen.
     */
    private final Window window;

    /**
     * Create a new full-screen strategy.
     *
     * @param window component that will be made full-screen
     */
    public DefaultFullScreenStrategy(Window window) {
        Logger.debug("DefaultFullScreenStrategy(window={})", window);
        if(window != null) {
            this.window = window;
        }
        else {
            throw new IllegalArgumentException("Window must not be null");
        }
    }

    @Override
    public void enterFullScreenMode() {
        Logger.debug("enterFullScreenMode()");
        GraphicsDevice graphicsDevice = getScreenDevice();
        Logger.debug("graphicsDevice={}", graphicsDevice);
        boolean fullScreenSupported = graphicsDevice.isFullScreenSupported();
        Logger.debug("fullScreenSupported={}", fullScreenSupported);
        onBeforeEnterFullScreenMode();
        graphicsDevice.setFullScreenWindow(window);
        DisplayMode displayMode = getDisplayMode(graphicsDevice.getDisplayModes());
        Logger.debug("displayMode={}", displayMode);
        if(displayMode != null) {
            Logger.debug("Setting new display mode");
            graphicsDevice.setDisplayMode(displayMode);
        }
        else {
            Logger.debug("Using default display mode");
        }
    }

    @Override
    public void exitFullScreenMode() {
        Logger.debug("exitFullScreenMode()");
        getScreenDevice().setFullScreenWindow(null);
        onAfterExitFullScreenMode();
    }

    @Override
    public boolean isFullScreenMode() {
        Logger.debug("isFullScreenMode()");
        return getScreenDevice().getFullScreenWindow() != null;
    }

    /**
     * Get the desired screen device.
     * <p>
     * The default implementation simply returns the default screen device.
     *
     * @return screen device, must not be <code>null</code>
     */
    protected GraphicsDevice getScreenDevice() {
        Logger.debug("getScreenDevice()");
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    /**
     * Get the desired display mode.
     * <p>
     * The default implementation returns <code>null</code> to accept the default display mode.
     *
     * @param displayModes available display modes
     * @return display mode, may be <code>null</code>
     */
    protected DisplayMode getDisplayMode(DisplayMode[] displayModes) {
        Logger.debug("getDisplayMode()", Arrays.toString(displayModes));
        return null;
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
