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

/**
 *
 */
public class VideoTrackInfo extends TrackInfo {

  /**
   * 
   */
  private final int width;
  
  /**
   * 
   */
  private final int height;
  
  /**
   * 
   * 
   * @param codec
   * @param id
   * @param profile
   * @param level
   * @param width
   * @param height
   */
  protected VideoTrackInfo(int codec, int id, int profile, int level, int width, int height) {
    super(codec, id, profile, level);
    this.width = width;
    this.height = height;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int width() {
    return width;
  }

  /**
   * 
   * 
   * @return
   */
  public int height() {
    return height;
  }
}
