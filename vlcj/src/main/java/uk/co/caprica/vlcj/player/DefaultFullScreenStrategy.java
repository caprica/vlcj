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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.GraphicsEnvironment;
import java.awt.Window;

/**
 * Default implementation of a full screen strategy that attempts to use the
 * JDK full-screen support.
 */
public class DefaultFullScreenStrategy implements FullScreenStrategy {

  /**
   * 
   */
  private final Window window;
  
  /**
   * 
   * 
   * @param window
   */
  public DefaultFullScreenStrategy(Window window) {
    if(window != null) {
      this.window = window;
    }
    else {
      throw new IllegalArgumentException("Window must not be null");
    }
  }
  
  @Override
  public void enterFullScreenMode() {
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(window);
  }

  @Override
  public void exitFullScreenMode() {
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
  }

  @Override
  public boolean isFullScreenMode() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() != null;
  }
}
