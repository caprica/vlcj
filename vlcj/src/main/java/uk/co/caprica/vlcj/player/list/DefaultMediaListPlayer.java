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

package uk.co.caprica.vlcj.player.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_playback_mode_e;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Pointer;

/**
 * Implementation of a media list player.
 */
public class DefaultMediaListPlayer implements MediaListPlayer {

  /**
   * Native library interface.
   */
  private final LibVlc libvlc;

  /**
   * Collection of event listeners.
   */
  private final List<MediaListPlayerEventListener> eventListenerList = new ArrayList<MediaListPlayerEventListener>();
  
  /**
   * Background service to notify event listeners.
   */
  private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

  /**
   * Native libvlc instance.
   */
  private libvlc_instance_t instance;

  /**
   * Native media player instance.
   */
  private libvlc_media_list_player_t mediaListPlayerInstance;
  
  /**
   * Native event manager instance.
   */
  private libvlc_event_manager_t mediaListPlayerEventManager;
  
  /**
   * Native event call-back.
   */
  private libvlc_callback_t callback;

  /**
   * Set to true when the player has been released.
   */
  private AtomicBoolean released = new AtomicBoolean();

  /**
   * Create a new media list player.
   * 
   * @param libvlc native library interface
   * @param instance libvlc instance
   */
  public DefaultMediaListPlayer(LibVlc libvlc, libvlc_instance_t instance) {
    this.libvlc = libvlc;
    this.instance = instance;
    
    createInstance();
  }

//  @Override
  public void addMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
    Logger.debug("addMediaPlayerEventListener(listener={})", listener);
    eventListenerList.add(listener);
  }

//  @Override
  public void removeMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
    Logger.debug("removeMediaPlayerEventListener(listener={})", listener);
    eventListenerList.remove(listener);
  }

//  @Override
  public void setMediaPlayer(MediaPlayer mediaPlayer) {
    Logger.debug("setMediaPlayer(mediaPlayer={})", mediaPlayer);
    libvlc.libvlc_media_list_player_set_media_player(mediaListPlayerInstance, mediaPlayer.mediaPlayerInstance());
  }
  
//  @Override
  public void setMediaList(MediaList mediaList) {
    Logger.debug("setMediaList(mediaList={})", mediaList);
    libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaList.mediaListInstance());
  }
  
//  @Override
  public void play() {
    Logger.debug("play()");
    libvlc.libvlc_media_list_player_play(mediaListPlayerInstance);
  }
  
//  @Override
  public void pause() {
    Logger.debug("pause()");
    libvlc.libvlc_media_list_player_pause(mediaListPlayerInstance);
  }
  
//  @Override
  public void stop() {
    Logger.debug("stop()");
    libvlc.libvlc_media_list_player_stop(mediaListPlayerInstance);
  }
  
//  @Override
  public boolean playItem(int itemIndex) {
    Logger.debug("playItem(itemIndex={})", itemIndex);
    return libvlc.libvlc_media_list_player_play_item_at_index(mediaListPlayerInstance, itemIndex) == 0;
  }
  
//  @Override
  public void playNext() {
    Logger.debug("playNext()");
    libvlc.libvlc_media_list_player_next(mediaListPlayerInstance);
  }
  
//  @Override
  public void playPrevious() {
    Logger.debug("playPrevious()");
    libvlc.libvlc_media_list_player_previous(mediaListPlayerInstance);
  }
  
//  @Override
  public boolean isPlaying() {
    Logger.debug("isPlaying()");
    return libvlc.libvlc_media_list_player_is_playing(mediaListPlayerInstance) != 0;
  }

//  @Override
  public void setMode(MediaListPlayerMode mode) {
    Logger.debug("setMode(mode={})", mode);
    libvlc_playback_mode_e playbackMode;
    switch(mode) {
      case DEFAULT:
        playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_default;
        break;
        
      case LOOP:
        playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_loop;
        break;
        
      case REPEAT:
        playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_repeat;
        break;
        
      default:
        throw new IllegalArgumentException("Invalid mode " + mode);
    }
    libvlc.libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, playbackMode.intValue());
  }
  
//  @Override
  public final void release() {
    Logger.debug("release()");
    if(released.compareAndSet(false, true)) {
      destroyInstance();
    }
  }
  
  /**
   * 
   */
  private void createInstance() {
    Logger.debug("createInstance()");
    
    mediaListPlayerInstance = libvlc.libvlc_media_list_player_new(instance);

    mediaListPlayerEventManager = libvlc.libvlc_media_list_player_event_manager(mediaListPlayerInstance);
    Logger.debug("mediaListPlayerEventManager={}", mediaListPlayerEventManager);
  
    registerEventListener();
  }
  
  /**
   * 
   */
  private void destroyInstance() {
    Logger.debug("destroyInstance()");
    
    Logger.debug("Detach events...");
    deregisterEventListener();
    Logger.debug("Events detached.");

    eventListenerList.clear();
    if(mediaListPlayerInstance != null) {
      Logger.debug("Release media list player...");
      libvlc.libvlc_media_list_player_release(mediaListPlayerInstance);
      Logger.debug("Media list player released");
    }
  }

  /**
   * 
   */
  private void registerEventListener() {
    Logger.debug("registerEventListener()");
    callback = new VlcVideoPlayerCallback();
    for(libvlc_event_e event : libvlc_event_e.values()) {
      if(event.intValue() >= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue()) {
        Logger.debug("event={}", event);
        int result = libvlc.libvlc_event_attach(mediaListPlayerEventManager, event.intValue(), callback, null);
        Logger.debug("result={}", result);
      }
    }
  }

  /**
   * 
   */
  private void deregisterEventListener() {
    Logger.debug("deregisterEventListener()");
    if(callback != null) {
      for(libvlc_event_e event : libvlc_event_e.values()) {
        if(event.intValue() >= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue()) {
          Logger.debug("event={}", event);
          libvlc.libvlc_event_detach(mediaListPlayerEventManager, event.intValue(), callback, null);
        }
      }
      callback = null;
    }
  }
  
  /**
   * 
   * 
   * @param event
   */
  private void notifyListeners(libvlc_event_t event) {
    Logger.trace("notifyListeners(event={})", event);
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaListPlayerEventListener listener = eventListenerList.get(i);
        int eventType = event.type;
        switch(libvlc_event_e.event(eventType)) {
          case libvlc_MediaListPlayerNextItemSet:
            listener.nextItem(this);
            break;
        }
      }
    }
  }

  /**
   *
   */
  private final class VlcVideoPlayerCallback implements libvlc_callback_t {

    @Override
    public void callback(libvlc_event_t event, Pointer userData) {
      Logger.trace("callback(event={},userData={})", event, userData);
      
      // Notify listeners in a different thread - the other thread is
      // necessary to prevent a potential native library failure if the
      // native library is re-entered
      if(!eventListenerList.isEmpty()) {
        listenersService.submit(new NotifyListenersRunnable(event));
      }
    }
  }

  /**
   * 
   */
  private final class NotifyListenersRunnable implements Runnable {

    /**
     * 
     */
    private final libvlc_event_t event;

    /**
     * 
     * 
     * @param event
     */
    private NotifyListenersRunnable(libvlc_event_t event) {
      this.event = event;
    }

    @Override
    public void run() {
      Logger.trace("run()");
      notifyListeners(event);
      Logger.trace("runnable exits");
    }
  }
}
