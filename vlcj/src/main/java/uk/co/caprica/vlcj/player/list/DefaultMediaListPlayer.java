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
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_playback_mode_e;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEvent;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEventFactory;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEventType;

import com.sun.jna.Pointer;

// TODO need to support the per media event listener, dynamically register/de-register and submit events (a bit messy because of the two different event types - go back to having one factory and one event type and one listener type??? 
//      what about the rest of the media player events since event a playlist player has it's own media player (even if we don't set one)

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
   * Factory to create media player events from native events.
   */
  private final MediaListPlayerEventFactory eventFactory = new MediaListPlayerEventFactory(this);

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
   * Mask of the native events that will cause notifications to be sent to
   * listeners.
   */
  private int eventMask = MediaListPlayerEventType.ALL.value();

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
  public void enableEvents(int eventMask) {
    Logger.debug("enableEvents(eventMask={})", eventMask);
    this.eventMask = eventMask;
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
    
    addMediaListPlayerEventListener(new NextItemHandler());
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
      // TODO The native event manager reports that it does not support 
      // libvlc_MediaListPlayerPlayed or libvlc_MediaListPlayerStopped  
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
        // TODO The native event manager reports that it does not support 
        // libvlc_MediaListPlayerPlayed or libvlc_MediaListPlayerStopped  
        if(event.intValue() >= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue()) {
          Logger.debug("event={}", event);
          libvlc.libvlc_event_detach(mediaListPlayerEventManager, event.intValue(), callback, null);
        }
      }
      callback = null;
    }
  }
  
  /**
   * A call-back to handle events from the native media player.
   * <p>
   * There are some important implementation details for this callback:
   * <ul>
   *   <li>First, the event notifications are off-loaded to a different thread
   *       so as to prevent application code re-entering libvlc in an event
   *       call-back which may lead to a deadlock in the native code;</li>
   *   <li>Second, the native event union structure refers to natively 
   *       allocated memory which will not be in the scope of the thread used 
   *       to actually dispatch the event notifications.</li> 
   * </ul>
   * Without copying the fields at this point from the native union structure,
   * the native memory referred to by the native event is likely to get 
   * deallocated and overwritten by the time the notification thread runs. This
   * would lead to unreliable data being sent with the notification, or even a 
   * fatal JVM crash.  
   */
  private final class VlcVideoPlayerCallback implements libvlc_callback_t {

    @Override
    public void callback(libvlc_event_t event, Pointer userData) {
      Logger.trace("callback(event={},userData={})", event, userData);
      if(!eventListenerList.isEmpty()) {
        // Create a new media player event for the native event
        MediaListPlayerEvent mediaListPlayerEvent = eventFactory.newMediaListPlayerEvent(event, eventMask);
        Logger.trace("mediaListPlayerEvent={}", mediaListPlayerEvent);
        if(mediaListPlayerEvent != null) {
          listenersService.submit(new NotifyListenersRunnable(mediaListPlayerEvent));
        }
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
    private final MediaListPlayerEvent mediaListPlayerEvent;

    /**
     * 
     * 
     * @param mediaListPlayerEvent
     */
    private NotifyListenersRunnable(MediaListPlayerEvent mediaListPlayerEvent) {
      this.mediaListPlayerEvent = mediaListPlayerEvent;
    }

    @Override
    public void run() {
      Logger.trace("run()");
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaListPlayerEventListener listener = eventListenerList.get(i);
        try {
          mediaListPlayerEvent.notify(listener);
        }
        catch(Throwable t) {
          Logger.warn("Event listener {} threw an exception", t, listener);
          // Continue with the next listener...
        }
      }
      Logger.trace("runnable exits");
    }
  }
  
  /**
   * 
   */
  private class NextItemHandler extends MediaListPlayerEventAdapter {

    /**
     * 
     */
    private libvlc_media_t mediaInstance; // TODO maybe need to expose this?
    
    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t mediaInstance) {
      Logger.debug("nextItem(mediaInstance={})", mediaInstance);
      deregisterMediaEventListener();
      this.mediaInstance = mediaInstance;
      registerMediaEventListener();
    }

    /**
     * 
     */
    private void registerMediaEventListener() {
      Logger.debug("registerMediaEventListener()");
      // If there is a media, register a new listener...
      if(mediaInstance != null) {
        libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
        for(libvlc_event_e event : libvlc_event_e.values()) {
          if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
            Logger.debug("event={}", event);
            int result = libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
            Logger.debug("result={}", result);
          }
        }
      }
    }

    /**
     * 
     */
    private void deregisterMediaEventListener() {
      Logger.debug("deregisterMediaEventListener()");
      // If there is a media, deregister the listener...
      if(mediaInstance != null) {
        libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
        for(libvlc_event_e event : libvlc_event_e.values()) {
          if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
            Logger.debug("event={}", event);
            libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
          }
        }
        mediaEventManager = null;
      }
    }
  }
}
