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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.x;

import java.awt.Window;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

/**
 * Implementation of a full-screen strategy that attempts to use the native X11
 * window manager.
 * <p>
 * With this full-screen strategy, a full-screen transparent overlay 
 * <em>will</em> work correctly, see {@link EmbeddedMediaPlayer#setOverlay(Window)}.
 */
public class XFullScreenStrategy implements FullScreenStrategy {

  /**
   * The component that will be made full-screen. 
   */
  private final Window window;
  
  /**
   * Is the window currently in full-screen mode? 
   */
  private boolean isFullScreenMode;

  /**
   * Create a new full-screen strategy.
   * 
   * @param window component that will be made full-screen
   */
  public XFullScreenStrategy(Window window) {
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
    LibXUtil.setFullScreenWindow(window, true);
    isFullScreenMode = true;
  }

  @Override
  public void exitFullScreenMode() {
    Logger.debug("exitFullScreenMode()");
    LibXUtil.setFullScreenWindow(window, false);
    isFullScreenMode = false;
  }

  @Override
  public boolean isFullScreenMode() {
    Logger.debug("isFullScreenMode()");
    return isFullScreenMode;
  }
}
