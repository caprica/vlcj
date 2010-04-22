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

/**
 *
 */
public enum LibVlcEventType {

  libvlc_MediaMetaChanged,
  libvlc_MediaSubItemAdded,
  libvlc_MediaDurationChanged,
  libvlc_MediaPreparsedChanged,
  libvlc_MediaFreed,
  libvlc_MediaStateChanged,

  libvlc_MediaPlayerNothingSpecial,
  libvlc_MediaPlayerOpening,
  libvlc_MediaPlayerBuffering,
  libvlc_MediaPlayerPlaying,
  libvlc_MediaPlayerPaused,
  libvlc_MediaPlayerStopped,
  libvlc_MediaPlayerForward,
  libvlc_MediaPlayerBackward,
  libvlc_MediaPlayerEndReached,
  libvlc_MediaPlayerEncounteredError,
  libvlc_MediaPlayerTimeChanged,
  libvlc_MediaPlayerPositionChanged,
  libvlc_MediaPlayerSeekableChanged,
  libvlc_MediaPlayerPausableChanged,

  libvlc_MediaListItemAdded,
  libvlc_MediaListWillAddItem,
  libvlc_MediaListItemDeleted,
  libvlc_MediaListWillDeleteItem,

  libvlc_MediaListViewItemAdded,
  libvlc_MediaListViewWillAddItem,
  libvlc_MediaListViewItemDeleted,
  libvlc_MediaListViewWillDeleteItem,

  libvlc_MediaListPlayerPlayed,
  libvlc_MediaListPlayerNextItemSet,
  libvlc_MediaListPlayerStopped,

  libvlc_MediaDiscovererStarted,
  libvlc_MediaDiscovererEnded;
}
