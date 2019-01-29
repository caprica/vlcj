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

import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * A base implementation for a runtime strategy that picks a strategy implementation from those registered based on the
 * current runtime operating system.
 */
abstract class BaseAdaptiveFullScreenStrategy implements FullScreenStrategy {

    private final FullScreenStrategy linuxStrategy;
    private final FullScreenStrategy windowsStrategy;
    private final FullScreenStrategy osxStrategy;
    private final FullScreenStrategy otherStrategy;

    BaseAdaptiveFullScreenStrategy(FullScreenStrategy linuxStrategy, FullScreenStrategy windowsScreenStrategy, FullScreenStrategy osxStrategy, FullScreenStrategy otherStrategy) {
        this.linuxStrategy   = linuxStrategy;
        this.windowsStrategy = windowsScreenStrategy;
        this.osxStrategy     = osxStrategy;
        this.otherStrategy   = otherStrategy;
    }

    @Override
    public final void enterFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if (strategy != null) {
            onBeforeEnterFullScreen();
            strategy.enterFullScreenMode();
        }
    }

    @Override
    public final void exitFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if (strategy != null) {
            strategy.exitFullScreenMode();
            onAfterExitFullScreen();
        }
    }

    /**
     *
     */
    protected void onBeforeEnterFullScreen() {
    }

    /**
     *
     */
    protected void onAfterExitFullScreen() {
    }

    @Override
    public final boolean isFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if (strategy != null) {
            return strategy.isFullScreenMode();
        } else {
            return false;
        }
    }

    private FullScreenStrategy strategy() {
        if (RuntimeUtil.isNix()) {
            return linuxStrategy;
        } else if (RuntimeUtil.isWindows()) {
            return windowsStrategy;
        } else if (RuntimeUtil.isMac()) {
            return osxStrategy;
        } else {
            return otherStrategy;
        }
    }

}
