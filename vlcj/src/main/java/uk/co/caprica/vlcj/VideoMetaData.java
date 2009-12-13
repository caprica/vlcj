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
 * Copyright 2009 Caprica Software Limited.
 */

package uk.co.caprica.vlcj;

import java.awt.Dimension;

/**
 * Basic video meta data.
 */
public class VideoMetaData {

  /**
   * Video width/height.
   */
  private Dimension videoDimension;
  
  public Dimension getVideoDimension() {
    return videoDimension;
  }

  public void setVideoDimension(Dimension videoDimension) {
    this.videoDimension = videoDimension;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(20);
    sb.append("videoDimension=").append(videoDimension);
    return sb.toString();
  }
}
