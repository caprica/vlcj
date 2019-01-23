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

public final class FullScreenService extends BaseService {

    /**
     * Full-screen strategy implementation, may be <code>null</code>.
     */
    private FullScreenStrategy fullScreenStrategy;

    FullScreenService(DefaultEmbeddedMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set a full-screen strategy implementation.
     * <p>
     * The preferred way to set a full-screen strategy is via a constructor argument, nevertheless
     * there are scenarios where it is more convenient to set the full-screen strategy <em>after</em>
     * creation of the media player (depends how the application UI is created).
     * <p>
     * <em>Applications should not change the full-screen strategy implementation after initialisation
     * of the media player. Doing so makes no practical sense and the resultant behaviour is
     * undefined.</em>
     *
     * @param fullScreenStrategy full-screen strategy
     */
    public void setFullScreenStrategy(FullScreenStrategy fullScreenStrategy) {
        this.fullScreenStrategy = fullScreenStrategy;
    }

    /**
     * Toggle whether the video display is in full-screen or not.
     * <p>
     * Setting the display into or out of full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
     */
    public void toggleFullScreen() {
        if (fullScreenStrategy != null) {
            setFullScreen(!fullScreenStrategy.isFullScreenMode());
        }
    }

    /**
     * Set full-screen mode.
     * <p>
     * Setting the display into or out of full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
     *
     * @param fullScreen true for full-screen, otherwise false
     */
    public void setFullScreen(boolean fullScreen) {
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
     * Testing whether or not the display is in full-screen mode is delegate to the
     * {@link FullScreenStrategy} that was used when the media player was created.
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
