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
public class AudioTrackInfo extends TrackInfo {

  /**
   * 
   */
  private final int channels;
  
  /**
   * 
   */
  private final int rate;
  
  /**
   * 
   * 
   * @param codec
   * @param id
   * @param profile
   * @param level
   * @param channels
   * @param rate
   */
  protected AudioTrackInfo(int codec, int id, int profile, int level, int channels, int rate) {
    super(codec, id, profile, level);
    this.channels = channels;
    this.rate = rate;
  }

  /**
   * 
   * 
   * @return
   */
  public int channels() {
    return channels;
  }

  /**
   * 
   * 
   * @return
   */
  public int rate() {
    return rate;
  }
}
