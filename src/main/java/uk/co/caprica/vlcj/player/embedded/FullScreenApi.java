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

package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.player.embedded.fullscreen.FullScreenStrategy;

/**
 * Behaviour pertaining to full-screen functionality.
 */
public final class FullScreenApi extends BaseApi {

    /**
     * Full-screen strategy implementation, may be <code>null</code>.
     */
    private FullScreenStrategy fullScreenStrategy;

    FullScreenApi(EmbeddedMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set a full-screen strategy implementation.
     * <p>
     * The preferred way to set a full-screen strategy is via a constructor argument when creating the media player.
     *
     * @param fullScreenStrategy full-screen strategy
     */
    public void strategy(FullScreenStrategy fullScreenStrategy) {
        this.fullScreenStrategy = fullScreenStrategy;
    }

    /**
     * Toggle whether the video display is in full-screen or not.
     * <p>
     * Setting the display into or out of full-screen mode is delegated to the currently set {@link FullScreenStrategy},
     * which may be <code>null</code> (in which case full-screen mode is not supported).
     */
    public void toggle() {
        if (fullScreenStrategy != null) {
            set(!fullScreenStrategy.isFullScreenMode());
        }
    }

    /**
     * Set full-screen mode.
     * <p>
     * Setting the display into or out of full-screen mode is delegated to the currently set {@link FullScreenStrategy},
     * which may be <code>null</code> (in which case full-screen mode is not supported).
     *
     * @param fullScreen true for full-screen, otherwise false
     */
    public void set(boolean fullScreen) {
        if (fullScreenStrategy != null) {
            if (fullScreen) {
                fullScreenStrategy.enterFullScreenMode();
            } else {
                fullScreenStrategy.exitFullScreenMode();
            }
        }
    }

    /**
     * Check whether the full-screen mode is currently active or not.
     * <p>
     * Testing whether or not the display is in full-screen mode is delegated to the currently set
     * {@link FullScreenStrategy} which may be <code>null</code> (in which case full-screen mode is not supported).
     *
     * @return true if full-screen is active, otherwise false
     */
    public boolean isFullScreen() {
        if (fullScreenStrategy != null) {
            return fullScreenStrategy.isFullScreenMode();
        } else {
            return false;
        }
    }

}
