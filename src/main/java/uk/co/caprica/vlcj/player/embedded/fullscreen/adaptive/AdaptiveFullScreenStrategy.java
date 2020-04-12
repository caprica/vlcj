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

package uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive;

import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.exclusivemode.ExclusiveModeFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.osx.OsxFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.fullscreen.x.XFullScreenStrategy;

import java.awt.*;

/**
 * Implementation of an full-screen strategy based on the current run-time operating system.
 * <p>
 * This implementation uses the "best" available of the provided full-screen strategy implementations, which may be a
 * native solution, for each supported operating system.
 */
public class AdaptiveFullScreenStrategy implements FullScreenStrategy {

    /**
     * Strategy chosen depending on runtime operating system.
     */
    private final FullScreenStrategy strategy;

    /**
     * Create a full-screen strategy.
     *
     * @param window window to manage as full-screen or not
     */
    public AdaptiveFullScreenStrategy(Window window) {
        this.strategy = getStrategy(window);
    }

    @Override
    public final void enterFullScreenMode() {
        onBeforeEnterFullScreen();
        strategy.enterFullScreenMode();
    }

    @Override
    public final void exitFullScreenMode() {
        strategy.exitFullScreenMode();
        onAfterExitFullScreen();
    }

    @Override
    public final boolean isFullScreenMode() {
        return strategy.isFullScreenMode();
    }

    /**
     * Template method invoked before full-screen mode is entered.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode for example to hide
     * other on-screen components.
     */
    protected void onBeforeEnterFullScreen() {
    }

    /**
     * Template method invoked after exiting full-screen mode.
     * <p>
     * An application can override this method to provide custom code when entering full-screen mode for example to
     * restore other on-screen components.
     */
    protected void onAfterExitFullScreen() {
    }

    private FullScreenStrategy getStrategy(Window window) {
        if (RuntimeUtil.isNix()) {
            return new XFullScreenStrategy(window);
        } else if (RuntimeUtil.isWindows()) {
            return new Win32FullScreenStrategy(window);
        } else if (RuntimeUtil.isMac()) {
            return new OsxFullScreenStrategy(window);
        } else {
            return new ExclusiveModeFullScreenStrategy(window);
        }
    }

}
