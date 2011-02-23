/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

/**
 * Enumeration of media meta data types.
 */
public enum MediaMetaType {
  
  TITLE      ( 0),
  ARTIST     ( 1),
  GENRE      ( 2),
  COPYRIGHT  ( 3),
  ALBUM      ( 4),
  TRACKNUMBER( 5),
  DESCRIPTION( 6),
  RATING     ( 7),
  DATE       ( 8),
  SETTING    ( 9),
  URL        (10),
  LANGUAGE   (11),
  NOWPLAYING (12),
  PUBLISHER  (13),
  ENCODEDBY  (14),
  ARTWORKURL (15),
  TRACKID    (16);
  
  /**
   * Native value.
   */
  private final int intValue;

  private MediaMetaType(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
