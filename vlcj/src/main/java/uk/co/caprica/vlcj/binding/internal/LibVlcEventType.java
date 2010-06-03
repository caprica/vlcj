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

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum LibVlcEventType {

  libvlc_MediaMetaChanged(0),
  libvlc_MediaSubItemAdded(1),
  libvlc_MediaDurationChanged(2),
  libvlc_MediaPreparsedChanged(3),
  libvlc_MediaFreed(4),
  libvlc_MediaStateChanged(5),

  libvlc_MediaPlayerNothingSpecial(6),
  libvlc_MediaPlayerOpening(7),
  libvlc_MediaPlayerBuffering(8),
  libvlc_MediaPlayerPlaying(9),
  libvlc_MediaPlayerPaused(10),
  libvlc_MediaPlayerStopped(11),
  libvlc_MediaPlayerForward(12),
  libvlc_MediaPlayerBackward(13),
  libvlc_MediaPlayerEndReached(14),
  libvlc_MediaPlayerEncounteredError(15),
  libvlc_MediaPlayerTimeChanged(16),
  libvlc_MediaPlayerPositionChanged(17),
  libvlc_MediaPlayerSeekableChanged(18),
  libvlc_MediaPlayerPausableChanged(19),

  libvlc_MediaListItemAdded(20),
  libvlc_MediaListWillAddItem(21),
  libvlc_MediaListItemDeleted(22),
  libvlc_MediaListWillDeleteItem(23),

  libvlc_MediaListViewItemAdded(24),
  libvlc_MediaListViewWillAddItem(25),
  libvlc_MediaListViewItemDeleted(26),
  libvlc_MediaListViewWillDeleteItem(27),

  libvlc_MediaListPlayerPlayed(28),
  libvlc_MediaListPlayerNextItemSet(29),
  libvlc_MediaListPlayerStopped(30),

  libvlc_MediaDiscovererStarted(31),
  libvlc_MediaDiscovererEnded(32);

  private static final Map<Integer, LibVlcEventType> INT_MAP = new HashMap<Integer, LibVlcEventType>(); 

  static {
    for(LibVlcEventType event : LibVlcEventType.values()) {
      INT_MAP.put(event.intValue, event);
    }
  }

  public static LibVlcEventType event(int intValue) {
    return INT_MAP.get(intValue);
  }
  
  private final int intValue;
  
  private LibVlcEventType(int intValue) {
    this.intValue = intValue;
  }
  
  public int intValue() {
    return intValue;
  }
}
