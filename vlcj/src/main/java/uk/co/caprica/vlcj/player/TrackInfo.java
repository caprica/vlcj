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
public abstract class TrackInfo {

  /**
   * 
   */
  private final int codec;
  
  /**
   * 
   */
  private final String codecName;

  /**
   * 
   */
  private final int id;
  
  /**
   * 
   */
  private final int profile;
  
  /**
   * 
   */
  private final int level;
  
  /**
   * 
   * 
   * @param codec
   * @param id
   * @param profile
   * @param level
   */
  protected TrackInfo(int codec, int id, int profile, int level) {
    this.codec = codec;
    this.codecName = codec != 0 ? new String(new byte[] {(byte)(codec >>> 24), (byte)(codec >>> 16), (byte)(codec >>> 8), (byte)codec}).trim() : null;
    this.id = id;
    this.profile = profile;
    this.level = level;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int codec() {
    return codec;
  }
  
  /**
   * 
   * 
   * @param value
   * @return
   */
  public String codecName() {
    return codecName;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int id() {
    return id;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int profile() {
    return profile;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int level() {
    return level;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(100);
    sb.append(getClass().getSimpleName()).append('[');
    sb.append("codec=").append(codec).append(',');
    sb.append("codecName=").append(codecName).append(',');
    sb.append("id=").append(id).append(',');
    sb.append("profile=").append(profile).append(',');
    sb.append("level=").append(level).append(']');
    return sb.toString();
  }
}
