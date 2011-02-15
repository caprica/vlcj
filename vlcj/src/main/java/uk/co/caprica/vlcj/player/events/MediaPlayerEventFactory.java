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

package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.media_duration_changed;
import uk.co.caprica.vlcj.binding.internal.media_meta_changed;
import uk.co.caprica.vlcj.binding.internal.media_parsed_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_length_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_pausable_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_position_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_seekable_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_snapshot_taken;
import uk.co.caprica.vlcj.binding.internal.media_player_time_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_title_changed;
import uk.co.caprica.vlcj.binding.internal.media_state_changed;
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
   * @param eventMask bit mask of enabled events (i.e. events to send notifications for)
   * @return media player event, or <code>null</code> if the native event type is not enabled or otherwise could not be handled
   */
  public MediaPlayerEvent newMediaPlayerEvent(libvlc_event_t event, int eventMask) {
    // Create an event suitable for the native event type...
    MediaPlayerEvent result = null;
    switch(libvlc_event_e.event(event.type)) {

      // === Events relating to the media player ==============================
      
      case libvlc_MediaPlayerMediaChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_CHANGED)) {
          result = new MediaPlayerMediaChangedEvent(mediaPlayer);
        }
        break;
      
      case libvlc_MediaPlayerNothingSpecial:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_CHANGED)) {
          result = new MediaPlayerNothingSpecialEvent(mediaPlayer);
        }
        break;

      case libvlc_MediaPlayerOpening:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.OPENING)) {
          result = new MediaPlayerOpeningEvent(mediaPlayer);
        }
        break;
        
      case libvlc_MediaPlayerBuffering:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.BUFFERING)) {
          result = new MediaPlayerBufferingEvent(mediaPlayer);
        }
        break;
        
      case libvlc_MediaPlayerPlaying:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.PLAYING)) {
          result = new MediaPlayerPlayingEvent(mediaPlayer);
        }
        break;
    
      case libvlc_MediaPlayerPaused:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.PAUSED)) {
          result = new MediaPlayerPausedEvent(mediaPlayer);
        }
        break;
    
      case libvlc_MediaPlayerStopped:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.STOPPED)) {
          result = new MediaPlayerStoppedEvent(mediaPlayer);
        }
        break;
    
      case libvlc_MediaPlayerForward:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.FORWARD)) {
          result = new MediaPlayerForwardEvent(mediaPlayer);
        }
        break;
        
      case libvlc_MediaPlayerBackward:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.BACKWARD)) {
          result = new MediaPlayerBackwardEvent(mediaPlayer);
        }
        break;
        
      case libvlc_MediaPlayerEndReached:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.FINISHED)) {
          result = new MediaPlayerEndReachedEvent(mediaPlayer);
        }
        break;
    
      case libvlc_MediaPlayerEncounteredError:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.ERROR)) {
          result = new MediaPlayerEncounteredErrorEvent(mediaPlayer);
        }
        break;
        
      case libvlc_MediaPlayerTimeChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.TIME_CHANGED)) {
          result = new MediaPlayerTimeChangedEvent(mediaPlayer, ((media_player_time_changed)event.u.getTypedValue(media_player_time_changed.class)).new_time);
        }
        break;

      case libvlc_MediaPlayerPositionChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.POSITION_CHANGED)) {
          result = new MediaPlayerPositionChangedEvent(mediaPlayer, ((media_player_position_changed)event.u.getTypedValue(media_player_position_changed.class)).new_position);
        }
        break;
        
      case libvlc_MediaPlayerSeekableChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.SEEKABLE_CHANGED)) {
          result = new MediaPlayerSeekableChangedEvent(mediaPlayer, ((media_player_seekable_changed)event.u.getTypedValue(media_player_seekable_changed.class)).new_seekable);
        }
        break;
        
      case libvlc_MediaPlayerPausableChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.PAUSABLE_CHANGED)) {
          result = new MediaPlayerPausableChangedEvent(mediaPlayer, ((media_player_pausable_changed)event.u.getTypedValue(media_player_pausable_changed.class)).new_pausable);
        }
        break;
      
      case libvlc_MediaPlayerTitleChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.TITLE_CHANGED)) {
          result = new MediaPlayerTitleChangedEvent(mediaPlayer, ((media_player_title_changed)event.u.getTypedValue(media_player_title_changed.class)).new_title);
        }
        break;
        
      case libvlc_MediaPlayerSnapshotTaken:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.SNAPSHOT_TAKEN)) {
          result = new MediaPlayerSnapshotTakenEvent(mediaPlayer, ((media_player_snapshot_taken)event.u.getTypedValue(media_player_snapshot_taken.class)).filename);
        }
        break;

      case libvlc_MediaPlayerLengthChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.LENGTH_CHANGED)) {
          result = new MediaPlayerLengthChangedEvent(mediaPlayer, ((media_player_length_changed)event.u.getTypedValue(media_player_length_changed.class)).new_length);
        }
        break;
        
      // === Events relating to the current media =============================

      case libvlc_MediaMetaChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_META_CHANGED)) {
          result = new MediaMetaChangedEvent(mediaPlayer, ((media_meta_changed)event.u.getTypedValue(media_meta_changed.class)).meta_type);
        }
        break;

      case libvlc_MediaSubItemAdded:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_SUB_ITEM_ADDED)) {
          result = new MediaSubItemAddedEvent(mediaPlayer);
        }
        break;

      case libvlc_MediaDurationChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_DURATION_CHANGED)) {
          result = new MediaDurationChangedEvent(mediaPlayer, ((media_duration_changed)event.u.getTypedValue(media_duration_changed.class)).new_duration);
        }
        break;

      case libvlc_MediaParsedChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_PARSED_CHANGED)) {
          result = new MediaParsedChangedEvent(mediaPlayer, ((media_parsed_changed)event.u.getTypedValue(media_parsed_changed.class)).new_status);
        }
        break;

      case libvlc_MediaFreed:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_FREED)) {
          result = new MediaFreedEvent(mediaPlayer);
        }
        break;

      case libvlc_MediaStateChanged:
        if(MediaPlayerEventType.set(eventMask, MediaPlayerEventType.MEDIA_STATE_CHANGED)) {
          result = new MediaStateChangedEvent(mediaPlayer, ((media_state_changed)event.u.getTypedValue(media_state_changed.class)).new_state);
        }
        break;
    }
    return result;
  }
}
