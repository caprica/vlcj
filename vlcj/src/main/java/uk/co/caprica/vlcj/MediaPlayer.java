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

import java.awt.Canvas;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.LibVlcCallback;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventManager;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventType;
import uk.co.caprica.vlcj.binding.internal.LibVlcInstance;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaDescriptor;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaInstance;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_exception_t;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Simple media player implementation.
 */
public class MediaPlayer {

  private static final int VOUT_WAIT_PERIOD = 1000;
  
  private List<MediaPlayerEventListener> eventListenerList = new ArrayList<MediaPlayerEventListener>();

  private final LibVlc libvlc = LibVlc.SYNC_INSTANCE;

  private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

  private final ExecutorService metaService = Executors.newSingleThreadExecutor();
  
  private final String[] args;

  private LibVlcInstance instance;
  private LibVlcMediaInstance mediaPlayerInstance;
  private LibVlcEventManager mediaPlayerEventManager;
  private LibVlcCallback callback;

  private Canvas videoSurface;

  private volatile boolean released;

  public MediaPlayer(String[] args) {
    this.args = args;
    createInstance();
  }

  public void setVideoSurface(Canvas videoSurface) {
    this.videoSurface = videoSurface;
  }

  public void playMedia(String media) {
    if(videoSurface == null) {
      throw new IllegalStateException("Must set a video surface");
    }

    long drawable = Native.getComponentID(videoSurface);

    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_set_drawable(mediaPlayerInstance, (int) drawable, exception);
    checkException(exception);

    setMedia(media);

    play();
  }

  public void play() {
    if(mediaPlayerInstance == null) {
      throw new IllegalStateException("No media player instance");
    }

    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_play(mediaPlayerInstance, exception);
    checkException(exception);
  }

  public void stop() {
    if(mediaPlayerInstance == null) {
      throw new IllegalStateException("No media player instance");
    }

    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_stop(mediaPlayerInstance, exception);
    checkException(exception);
  }

  public void pause() {
    if(mediaPlayerInstance == null) {
      throw new IllegalStateException("No media player instance");
    }

    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_pause(mediaPlayerInstance, exception);
    checkException(exception);
  }

  public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
    eventListenerList.add(listener);
  }

  public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
    eventListenerList.remove(listener);
  }

  /**
   * Create and prepare the native media player resources.
   */
  private void createInstance() {
    libvlc_exception_t exception = new libvlc_exception_t();
    instance = libvlc.libvlc_new(args.length, args, exception);
    checkException(exception);

    mediaPlayerInstance = libvlc.libvlc_media_player_new(instance, exception);
    checkException(exception);

    mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance, exception);
    checkException(exception);

    registerEventListener();
    
    eventListenerList.add(new MetaDataEventHandler());
  }

  /**
   * Clean up the native media player resources.
   */
  private void destroyInstance() {
    deregisterEventListener();

    eventListenerList.clear();
    
    if(mediaPlayerEventManager != null) {
      mediaPlayerEventManager = null;
    }

    if(mediaPlayerInstance != null) {
      libvlc.libvlc_media_player_release(mediaPlayerInstance);
      mediaPlayerInstance = null;
    }

    if(instance != null) {
      libvlc.libvlc_release(instance);
      instance = null;
    }
  }

  private void registerEventListener() {
    if(mediaPlayerEventManager == null) {
      throw new IllegalStateException("No event manager instance");
    }

    callback = new VlcVideoPlayerCallback();

    libvlc_exception_t exception = new libvlc_exception_t();

    for (LibVlcEventType event : EnumSet.range(LibVlcEventType.libvlc_MediaPlayerPlaying, LibVlcEventType.libvlc_MediaPlayerEncounteredError)) {
      libvlc.libvlc_event_attach(mediaPlayerEventManager, event.ordinal(), callback, null, exception);
      checkException(exception);
    }
  }

  private void deregisterEventListener() {
    if(mediaPlayerEventManager == null) {
      throw new IllegalStateException("No event manager instance");
    }

    if(callback != null) {
      libvlc_exception_t exception = new libvlc_exception_t();

      for (LibVlcEventType event : EnumSet.range(LibVlcEventType.libvlc_MediaPlayerPlaying, LibVlcEventType.libvlc_MediaPlayerEncounteredError)) {
        libvlc.libvlc_event_detach(mediaPlayerEventManager, event.ordinal(), callback, null, exception);
        checkException(exception);
      }

      callback = null;
    }
  }

  private void setMedia(String media) {
    libvlc_exception_t exception = new libvlc_exception_t();

    LibVlcMediaDescriptor mediaDescriptor = libvlc.libvlc_media_new(instance, media, exception);
    checkException(exception);

    libvlc.libvlc_media_add_option(mediaDescriptor, media, exception);
    checkException(exception);

    libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaDescriptor, exception);
    checkException(exception);

    libvlc.libvlc_media_release(mediaDescriptor);
  }

  private Dimension getVideoDimension() {
    libvlc_exception_t exception = new libvlc_exception_t();

    int width = libvlc.libvlc_video_get_width(mediaPlayerInstance, exception);
    checkException(exception);

    int height = libvlc.libvlc_video_get_height(mediaPlayerInstance, exception);
    checkException(exception);
    
    return new Dimension(width, height);
  }
  
  private boolean hasVideoOut() {
    libvlc_exception_t exception = new libvlc_exception_t();
    
    int hasVideoOut = libvlc.libvlc_media_player_has_vout(mediaPlayerInstance, exception);
    checkException(exception);
    
    return hasVideoOut != 0;
  }
  
  private void checkException(libvlc_exception_t exception) {
    int raised = exception.raised;

    if(raised != 0) {
      int code = exception.code;
      String message = exception.message;
      throw new RuntimeException("Code: " + code + ", Message: " + message);
    }
  }

  public void release() {
    destroyInstance();
    released = true;
  }

  @Override
  protected synchronized void finalize() throws Throwable {
    if(!released) {
      release();
    }
  }

  private void notifyListeners(libvlc_event_t event) {
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        int eventType = event.type;
        if(eventType == LibVlcEventType.libvlc_MediaPlayerPlaying.ordinal()) {
          listener.playing(this);
        } else if(eventType == LibVlcEventType.libvlc_MediaPlayerPaused.ordinal()) {
          listener.paused(this);
        } else if(eventType == LibVlcEventType.libvlc_MediaPlayerStopped.ordinal()) {
          listener.stopped(this);
        } else if(eventType == LibVlcEventType.libvlc_MediaPlayerEndReached.ordinal()) {
          listener.finished(this);
        }
      }
    }
  }

  private void notifyListeners(VideoMetaData videoMetaData) {
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        listener.metaDataAvailable(this, videoMetaData);
      }
    }
  }

  private final class VlcVideoPlayerCallback implements LibVlcCallback {

    public void callback(libvlc_event_t event, Pointer userData) {
      // Notify listeners in a different thread - the other thread is
      // necessary to prevent a potential native library failure if the
      // native library is re-entered
      if(!eventListenerList.isEmpty()) {
        listenersService.submit(new NotifyListenersRunnable(event));
      }
    }
  }

  private final class NotifyListenersRunnable implements Runnable {

    private final libvlc_event_t event;

    private NotifyListenersRunnable(libvlc_event_t event) {
      this.event = event;
    }

    @Override
    public void run() {
      notifyListeners(event);
    }
  }
  
  private final class NotifyMetaRunnable implements Runnable {

    @Override
    public void run() {
      for(;;) {
        try {
          Thread.sleep(VOUT_WAIT_PERIOD);

          if(hasVideoOut()) {
            VideoMetaData videoMetaData = new VideoMetaData();
            videoMetaData.setVideoDimension(getVideoDimension());
            
            notifyListeners(videoMetaData);
            
            break;
          }
        }
        catch(InterruptedException e) {
        }
      }
    }
  }

  private final class MetaDataEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void playing(MediaPlayer mediaPlayer) {
      metaService.submit(new NotifyMetaRunnable());
    }
  }
}
