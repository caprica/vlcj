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

package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.media_player_length_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_pausable_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_position_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_seekable_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_snapshot_taken;
import uk.co.caprica.vlcj.binding.internal.media_player_time_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_title_changed;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * A factory that creates a media player event instance for a native media
 * player event.
 */
public class MediaPlayerEventFactory {

  /**
   * Media player to create events for.
   */
  private final MediaPlayer mediaPlayer;
  
  /**
   * Create a media player event factory.
   * 
   * @param mediaPlayer media player to create events for
   */
  public MediaPlayerEventFactory(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }
  
  /**
   * Create a new media player event for a given native event.
   * 
   * @param event native event
   * @return media player event, or <code>null</code> if the native event type could not be handled
   */
  public MediaPlayerEvent newMediaPlayerEvent(libvlc_event_t event) {
    MediaPlayerEvent result = null;
    
    // Create an event suitable for the native event type...
    switch(libvlc_event_e.event(event.type)) {
      case libvlc_MediaPlayerMediaChanged:
        result = new MediaPlayerMediaChangedEvent(mediaPlayer);
        break;
      
      case libvlc_MediaPlayerNothingSpecial:
        result = new MediaPlayerNothingSpecialEvent(mediaPlayer);
        break;

      case libvlc_MediaPlayerOpening:
        result = new MediaPlayerOpeningEvent(mediaPlayer);
        break;
        
      case libvlc_MediaPlayerBuffering:
        result = new MediaPlayerBufferingEvent(mediaPlayer);
        break;
        
      case libvlc_MediaPlayerPlaying:
        result = new MediaPlayerPlayingEvent(mediaPlayer);
        break;
    
      case libvlc_MediaPlayerPaused:
        result = new MediaPlayerPausedEvent(mediaPlayer);
        break;
    
      case libvlc_MediaPlayerStopped:
        result = new MediaPlayerStoppedEvent(mediaPlayer);
        break;
    
      case libvlc_MediaPlayerForward:
        result = new MediaPlayerForwardEvent(mediaPlayer);
        break;
        
      case libvlc_MediaPlayerBackward:
        result = new MediaPlayerBackwardEvent(mediaPlayer);
        break;
        
      case libvlc_MediaPlayerEndReached:
        result = new MediaPlayerEndReachedEvent(mediaPlayer);
        break;
    
      case libvlc_MediaPlayerEncounteredError:
        result = new MediaPlayerEncounteredErrorEvent(mediaPlayer);
        break;
        
      case libvlc_MediaPlayerTimeChanged:
        result = new MediaPlayerTimeChangedEvent(mediaPlayer, ((media_player_time_changed)event.u.getTypedValue(media_player_time_changed.class)).new_time);
        break;

      case libvlc_MediaPlayerPositionChanged:
        result = new MediaPlayerPositionChangedEvent(mediaPlayer, ((media_player_position_changed)event.u.getTypedValue(media_player_position_changed.class)).new_position);
        break;
        
      case libvlc_MediaPlayerSeekableChanged:
        result = new MediaPlayerSeekableChangedEvent(mediaPlayer, ((media_player_seekable_changed)event.u.getTypedValue(media_player_seekable_changed.class)).new_seekable);
        break;
        
      case libvlc_MediaPlayerPausableChanged:
        result = new MediaPlayerPausableChangedEvent(mediaPlayer, ((media_player_pausable_changed)event.u.getTypedValue(media_player_pausable_changed.class)).new_pausable);
        break;
      
      case libvlc_MediaPlayerTitleChanged:
        result = new MediaPlayerTitleChangedEvent(mediaPlayer, ((media_player_title_changed)event.u.getTypedValue(media_player_title_changed.class)).new_title);
        break;
        
      case libvlc_MediaPlayerSnapshotTaken:
        result = new MediaPlayerSnapshotTakenEvent(mediaPlayer, ((media_player_snapshot_taken)event.u.getTypedValue(media_player_snapshot_taken.class)).filename);
        break;

      case libvlc_MediaPlayerLengthChanged:
        result = new MediaPlayerLengthChangedEvent(mediaPlayer, ((media_player_length_changed)event.u.getTypedValue(media_player_length_changed.class)).new_length);
        break;
    }
    
    return result;
  }
}
