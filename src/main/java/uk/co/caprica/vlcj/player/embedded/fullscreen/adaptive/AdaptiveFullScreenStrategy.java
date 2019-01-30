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
public class AdaptiveFullScreenStrategy extends BaseAdaptiveFullScreenStrategy {

    /**
     * Create a full-screen strategy.
     *
     * @param window window to manage as full-screen or not
     */
    public AdaptiveFullScreenStrategy(Window window) {
        super(
            new XFullScreenStrategy(window),
            new Win32FullScreenStrategy(window),
            new OsxFullScreenStrategy(window),
            new ExclusiveModeFullScreenStrategy(window)
        );
    }

    /**
     *
     *
     * @param linuxStrategy
     * @param windowsScreenStrategy
     * @param osxStrategy
     * @param otherStrategy
     */
    public AdaptiveFullScreenStrategy(FullScreenStrategy linuxStrategy, FullScreenStrategy windowsScreenStrategy, FullScreenStrategy osxStrategy, FullScreenStrategy otherStrategy) {
        super(linuxStrategy, windowsScreenStrategy, osxStrategy, otherStrategy);
    }

}
