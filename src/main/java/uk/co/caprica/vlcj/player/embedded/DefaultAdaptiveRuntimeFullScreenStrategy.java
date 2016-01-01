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

package uk.co.caprica.vlcj.player.embedded;

import java.awt.Window;

import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeType;

/**
 * Default implementation of an full-screen strategy based on the current run-time operating
 * system.
 * <p>
 * This implementation uses the available native full-screen strategy implementations.
 * <p>
 * Sub-classes can provide more, or override existing, default implementation choices.
 */
public class DefaultAdaptiveRuntimeFullScreenStrategy extends AdaptiveRuntimeFullScreenStrategy {

    /**
     * Create a full-screen strategy.
     *
     * @param window window to manage as full-screen or not
     */
    public DefaultAdaptiveRuntimeFullScreenStrategy(Window window) {
        setStrategy(RuntimeType.NIX, new NixFullScreenStrategy(window));
        setStrategy(RuntimeType.WINDOWS, new WindowsFullScreenStrategy(window));
    }

    /**
     * Extension of the native {@link XFullScreenStrategy} to invoke common template methods.
     */
    private class NixFullScreenStrategy extends XFullScreenStrategy {

        /**
         * Create a full-screen strategy.
         *
         * @param window window for full-screen
         */
        private NixFullScreenStrategy(Window window) {
            super(window);
        }

        @Override
        protected void onBeforeEnterFullScreenMode() {
            beforeEnterFullScreen();
        }

        @Override
        protected void onAfterExitFullScreenMode() {
            afterExitFullScreen();
        }
    }

    /**
     * Extension of the native {@link Win32FullScreenStrategy} to invoke common template methods.
     */
    private class WindowsFullScreenStrategy extends Win32FullScreenStrategy {

        /**
         * Create a full-screen strategy.
         *
         * @param window window for full-screen
         */
        private WindowsFullScreenStrategy(Window window) {
            super(window);
        }

        @Override
        protected void onBeforeEnterFullScreenMode() {
            beforeEnterFullScreen();
        }

        @Override
        protected void onAfterExitFullScreenMode() {
            afterExitFullScreen();
        }
    }

    /**
     * Template method invoked before entering full-screen.
     * <p>
     * Sub-classes may like to use this to e.g. hide screen furniture in full-screen mode.
     */
    protected void beforeEnterFullScreen() {
    }

    /**
     * Template method invoked after exiting full-screen.
     * <p>
     * Sub-classes may like to use this to e.g. restore previously hidden screen furniture.
     */
    protected void afterExitFullScreen() {
    }
}
