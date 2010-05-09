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

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.LibVlcCallback;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventManager;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventType;
import uk.co.caprica.vlcj.binding.internal.LibVlcInstance;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaDescriptor;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaInstance;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_exception_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.media_player_position_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_time_changed;

import com.sun.jna.Pointer;

/**
 * Simple media player implementation.
 * <p>
 * A more useful implementation will implement chapter and volume controls and 
 * so on.
 */
public abstract class MediaPlayer {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(MediaPlayer.class);
  
  private static final int VOUT_WAIT_PERIOD = 1000;
  
  // TODO
//  protected final LibVlc libvlc = LibVlc.SYNC_INSTANCE;
  protected final LibVlc libvlc = LibVlc.LOGGING_SYNC_INSTANCE;
  
  private final List<MediaPlayerEventListener> eventListenerList = new ArrayList<MediaPlayerEventListener>();

  private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

  private final ExecutorService metaService = Executors.newSingleThreadExecutor();
  
  private final FullScreenStrategy fullScreenStrategy;

  private LibVlcInstance instance;
  private LibVlcMediaInstance mediaPlayerInstance;
  private LibVlcEventManager mediaPlayerEventManager;
  private LibVlcCallback callback;

  private String[] standardMediaOptions;

  private Canvas videoSurface;
  
  private volatile boolean released;

  /**
   * Create a new media player.
   * <p>
   * Full-screen will not be supported.
   * 
   * @param instance
   */
  public MediaPlayer(LibVlcInstance instance) {
    this(null, instance);
  }
  
  /**
   * Create a new media player with a full-screen strategy.
   * 
   * @param fullScreenStrategy
   * @param instance
   */
  public MediaPlayer(FullScreenStrategy fullScreenStrategy, LibVlcInstance instance) {
    if(LOG.isDebugEnabled()) {LOG.debug("MediaPlayer(fullScreenStrategy=" + fullScreenStrategy + ",instance=" + instance + ")");}
    
    this.fullScreenStrategy = fullScreenStrategy;
    this.instance = instance;
    
    createInstance();
  }
  
  /**
   * Add a component to be notified of media player events.
   * 
   * @param listener component to notify
   */
  public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("addMediaPlayerEventListener(listener=" + listener + ")");}
    
    eventListenerList.add(listener);
  }

  /**
   * Remove a component that was previously interested in notifications of
   * media player events.
   * 
   * @param listener component to stop notifying
   */
  public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
    if(LOG.isDebugEnabled()) {LOG.debug("removeMediaPlayerEventListener(listener=" + listener + ")");}
    
    eventListenerList.remove(listener);
  }

  /**
   * Set standard media options for all media items subsequently played.
   * <p>
   * This will <strong>not</strong> affect any currently playing media item.
   * 
   * @param options options to apply to all subsequently played media items
   */
  public void setStandardMediaOptions(String... options) {
    if(LOG.isDebugEnabled()) {LOG.debug("setStandardMediaOptions(options=" + Arrays.toString(options) + ")");}
    
    this.standardMediaOptions = options;
  }

  /**
   * Set the component used to display the rendered video.
   * 
   * @param videoSurface component
   */
  public void setVideoSurface(Canvas videoSurface) {
    if(LOG.isDebugEnabled()) {LOG.debug("setVideoSurface(videoSurface=" + videoSurface + ")");}
    
    this.videoSurface = videoSurface;
  }

  /**
   * Play a new media item.
   * 
   * @param media media item
   */
  public void playMedia(String media) {
    if(LOG.isDebugEnabled()) {LOG.debug("playMedia(media=" + media + ")");}
    
    playMedia(media, (String)null);
  }
  
  /**
   * Play a new media item, with options.
   * 
   * @param media media item
   * @param mediaOptions media item options
   */
  public void playMedia(String media, String... mediaOptions) {
    if(LOG.isDebugEnabled()) {LOG.debug("playMedia(media=" + media + ",mediaOptions=" + Arrays.toString(mediaOptions) + ")");}

    if(LOG.isDebugEnabled()) {LOG.debug("videoSurface=" + videoSurface);}
    
    if(videoSurface == null) {
      throw new IllegalStateException("Must set a video surface");
    }

    // Delegate to the template method in the OS-specific implementation class
    // to actually set the video surface
    nativeSetVideoSurface(mediaPlayerInstance, videoSurface);

    setMedia(media, mediaOptions);

    play();
  }
  
  // === Status Controls ======================================================

  /**
   * 
   * 
   * @return
   */
  public boolean isPlayable() {
    LOG.trace("isPlayable()");

    libvlc_exception_t exception = new libvlc_exception_t();
    boolean isPlayable = libvlc.libvlc_media_player_will_play(mediaPlayerInstance, exception) == 1;
    checkException(exception);
    return isPlayable;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isPlaying() {
    LOG.trace("isPlaying()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    boolean isPlaying = libvlc.libvlc_media_player_is_playing(mediaPlayerInstance, exception) == 1;
    checkException(exception);
    return isPlaying;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isSeekable() {
    LOG.trace("isSeekable()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    boolean isSeekable = libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance, exception) == 1;
    checkException(exception);
    return isSeekable;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean canPause() {
    LOG.trace("canPause()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    boolean canPause = libvlc.libvlc_media_player_can_pause(mediaPlayerInstance, exception) == 1;
    checkException(exception);
    
    return canPause;
  }
  
  /**
   * Get the length of the current media item.
   * 
   * @return length, in milliseconds
   */
  public long getLength() {
    LOG.trace("getLength()");

    libvlc_exception_t exception = new libvlc_exception_t();
    long length = libvlc.libvlc_media_player_get_length(mediaPlayerInstance, exception);
    checkException(exception);
    
    return length; 
  }

  /**
   * Get the current play-back time.
   * 
   * @return current time, expressed as a number of milliseconds
   */
  public long getTime() {
    LOG.trace("getTime()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    long time = libvlc.libvlc_media_player_get_time(mediaPlayerInstance, exception);
    checkException(exception);
    
    return time;
  }

  /**
   * Get the current play-back position.
   * 
   * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
   */
  public float getPosition() {
    LOG.trace("getPosition()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    float position = libvlc.libvlc_media_player_get_position(mediaPlayerInstance, exception);
    checkException(exception);
    
    return position;
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getFps() {
    LOG.trace("getFps()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    float fps = libvlc.libvlc_media_player_get_fps(mediaPlayerInstance, exception);
    checkException(exception);
    return fps;
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getRate() {
    LOG.trace("getRate()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    float rate = libvlc.libvlc_media_player_get_rate(mediaPlayerInstance, exception);
    checkException(exception);
    return rate;
  }
  
  // === Basic Playback Controls ==============================================
  
  /**
   * Begin play-back.
   * <p>
   * If called when the play-back is paused, the play-back will resume from the
   * current position.
   */
  public void play() {
    LOG.debug("play()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_play(mediaPlayerInstance, exception);
    checkException(exception);
  }

  /**
   * Stop play-back.
   * <p>
   * A subsequent play will play-back from the start.
   */
  public void stop() {
    LOG.debug("stop()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_stop(mediaPlayerInstance, exception);
    checkException(exception);
  }

  /**
   * Pause play-back.
   * <p>
   * If the play-back is currently paused it will begin playing.
   */
  public void pause() {
    LOG.debug("pause()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_pause(mediaPlayerInstance, exception);
    checkException(exception);
  }

  /**
   * Skip forward or backward by a period of time.
   * 
   * @param delta time period, in milliseconds
   */
  public void skip(long delta) {
    if(LOG.isDebugEnabled()) {LOG.debug("skip(delta=" + delta + ")");}
    
    long current = getTime();
    if(LOG.isDebugEnabled()) {LOG.debug("current=" + current);}
    
    if(current != -1) {
      setTime(current + delta);
    }
  }
  
  /**
   * Skip forward or backward by a change in position.
   * 
   * @param delta
   */
  public void skip(float delta) {
    if(LOG.isDebugEnabled()) {LOG.debug("skip(delta=" + delta + ")");}

    float current = getPosition();
    if(LOG.isDebugEnabled()) {LOG.debug("current=" + current);}

    if(current != -1) {
      setPosition(current + delta);
    }
  }
  
  /**
   * Jump to a specific moment.
   * 
   * @param time time since the beginning, in milliseconds
   */
  public void setTime(long time) {
    if(LOG.isDebugEnabled()) {LOG.debug("setTime(time=" + time + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_set_time(mediaPlayerInstance, time, exception);
    checkException(exception);
  }
  
  /**
   *  Jump to a specific position.
   * 
   * @param position, a percentage (e.g. 0.15 is 15%)
   */
  public void setPosition(float position) {
    if(LOG.isDebugEnabled()) {LOG.debug("setPosition(position=" + position + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_set_position(mediaPlayerInstance, position, exception);
    checkException(exception);
  }
  
  // === Audio Controls =======================================================

  /**
   * Toggle volume mute.
   */
  public void mute() {
    LOG.debug("mute()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_audio_toggle_mute(instance, exception);
    checkException(exception);
  }
  
  /**
   * Mute or un-mute the volume.
   * 
   * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
   */
  public void mute(boolean mute) {
    if(LOG.isDebugEnabled()) {LOG.debug("mute(mute=" + mute + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_audio_set_mute(instance, mute ? 1 : 0, exception);
    checkException(exception);
  }
  
  /**
   * Test whether or not the volume is current muted.
   * 
   * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
   */
  public boolean isMute() {
    LOG.debug("isMute()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    boolean mute = libvlc.libvlc_audio_get_mute(instance, exception) != 0;
    checkException(exception);
    return mute;
  }
  
  /**
   * Get the current volume.
   * 
   * @return volume, in the range 0 to 100 where 100 is full volume
   */
  public int getVolume() {
    LOG.debug("getVolume()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int volume = libvlc.libvlc_audio_get_volume(instance, exception);
    checkException(exception);
    return volume;
  }
  
  /**
   * Set the volume.
   * 
   * @param volume volume, in the range 0 to 100 where 100 is full volume 
   */
  public void setVolume(int volume) {
    if(LOG.isDebugEnabled()) {LOG.debug("setVolume(volume=" + volume + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_audio_set_volume(instance, volume, exception);
    checkException(exception);
  }
  
  // === Chapter Controls =====================================================

  /**
   * Get the chapter count.
   * 
   * @return number of chapters, or -1 if no chapters
   */
  public int getChapterCount() {
    LOG.trace("getChapterCount()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int chapterCount = libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance, exception);
    checkException(exception);
    return chapterCount;
  }
  
  /**
   * Get the current chapter.
   * 
   * @return chapter number, where zero is the first chatper, or -1 if no media
   */
  public int getChapter() {
    LOG.trace("getChapter()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int chapter = libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance, exception);
    checkException(exception);
    return chapter;
  }
  
  /**
   * Set the chapter.
   * 
   * @param chapterNumber chapter number, where zero is the first chapter
   */
  public void setChapter(int chapterNumber) {
    if(LOG.isDebugEnabled()) {LOG.debug("setChapter(chapterNumber=" + chapterNumber + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber, exception);
    checkException(exception);
  }
  
  /**
   * Jump to the next chapter.
   * <p>
   * If the play-back is already at the last chapter, this will have no effect.
   */
  public void nextChapter() {
    LOG.debug("nextChapter()");

    int currentChapter = getChapter();
    if(currentChapter != -1) {
      setChapter(currentChapter + 1);
    }
  }
  
  /**
   * Jump to the previous chapter.
   * <p>
   * If the play-back is already at the first chapter, this will have no effect.
   */
  public void previousChapter() {
    LOG.debug("previousChapter()");
    
    int currentChapter = getChapter();
    if(currentChapter > 0) {
      setChapter(currentChapter - 1);
    }
  }
  
  // === Sub-Picture/Sub-Title Controls =======================================
  
  /**
   * 
   * 
   * @return
   */
  public int getSpuCount() {
    LOG.debug("getSpuCount()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int spuCount = libvlc.libvlc_video_get_spu_count(mediaPlayerInstance, exception);
    checkException(exception);
    return spuCount;
  }
  
  /**
   * 
   * 
   * @return
   */
  public int getSpu() {
    LOG.debug("getSpu()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int spu = libvlc.libvlc_video_get_spu(mediaPlayerInstance, exception);
    checkException(exception);
    return spu;
  }
  
  /**
   * 
   * 
   * @param spu
   */
  public void setSpu(int spu) {
    if(LOG.isDebugEnabled()) {LOG.debug("setSpu(spu=" + spu + ")");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu, exception);
    checkException(exception);
  }

  /**
   * 
   * 
   * @return
   */
  public List<String> getSpuDescriptions() {
    if(LOG.isDebugEnabled()) {LOG.debug("getSpuDescriptions()");}
    
    libvlc_exception_t exception = new libvlc_exception_t();
    libvlc_track_description_t trackDescription = libvlc.libvlc_video_get_spu_description(mediaPlayerInstance, exception);
    checkException(exception);

    List<String> spuDescriptions = new ArrayList<String>();
    while(trackDescription != null) {
      spuDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
    return spuDescriptions;
  }  
  
  
  // === Snapshot Controls ====================================================

  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * The snapshot will be created in the user's home directory and be assigned
   * a filename based on the current time.
   */
  public void saveSnapshot() {
    LOG.debug("saveSnapshot()");
    
    File snapshotDirectory = new File(System.getProperty("user.home"));
    File snapshotFile = new File(snapshotDirectory, "vlcj-snapshot-" + System.currentTimeMillis() + ".png");
    saveSnapshot(snapshotFile);
  }
  
  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * Any missing directory path will be created if it does not exist.
   * 
   * @param filename name of the file to contain the snapshot
   */
  public void saveSnapshot(File file) {
    if(LOG.isDebugEnabled()) {LOG.debug("saveSnapshot(file=" + file + ")");}
    
    File snapshotDirectory = file.getParentFile();
    if(!snapshotDirectory.exists()) {
      snapshotDirectory.mkdirs();
    }
    
    if(snapshotDirectory.exists()) {
      libvlc_exception_t exception = new libvlc_exception_t();
      libvlc.libvlc_video_take_snapshot(mediaPlayerInstance, file.getAbsolutePath(), 0, 0, exception);
      checkException(exception);
    }
    else {
      throw new RuntimeException("Directory does not exist and could not be created for '" + file.getAbsolutePath() + "'");
    }
  }
  
  // === User Interface =======================================================
  
  /**
   * 
   */
  public void toggleFullScreen() {
    LOG.debug("toggleFullScreen()");
    
    if(fullScreenStrategy != null) {
      setFullScreen(!fullScreenStrategy.isFullScreenMode());
    }
  }

  /**
   * 
   * 
   * @param fullScreen
   */
  public void setFullScreen(boolean fullScreen) {
    if(LOG.isDebugEnabled()) {LOG.debug("setFullScreen(fullScreen=" + fullScreen + ")");}
    
    if(fullScreenStrategy != null) {
      if(fullScreen) {
        fullScreenStrategy.enterFullScreenMode();
      }
      else {
        fullScreenStrategy.exitFullScreenMode();
      }
    }
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isFullScreen() {
    LOG.debug("isFullScreen()");
    
    if(fullScreenStrategy != null) {
      return fullScreenStrategy.isFullScreenMode();
    }
    else {
      return false;
    }
  }

  // === Implementation =======================================================

  /**
   * Create and prepare the native media player resources.
   */
  private void createInstance() {
    LOG.debug("createInstance()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    mediaPlayerInstance = libvlc.libvlc_media_player_new(instance, exception);
    checkException(exception);
    if(LOG.isDebugEnabled()) {LOG.debug("mediaPlayerInstance=" + mediaPlayerInstance);}
    
    mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance, exception);
    checkException(exception);
    if(LOG.isDebugEnabled()) {LOG.debug("mediaPlayerEventManager=" + mediaPlayerEventManager);}
  
    registerEventListener();
      
    eventListenerList.add(new MetaDataEventHandler());
  }

  /**
   * Clean up the native media player resources.
   */
  private void destroyInstance() {
    LOG.debug("destroyInstance()");
    
    LOG.debug("Detach events...");
    deregisterEventListener();
    LOG.debug("Events detached.");

    eventListenerList.clear();
    
    if(mediaPlayerEventManager != null) {
      mediaPlayerEventManager = null;
    }

    if(mediaPlayerInstance != null) {
      LOG.debug("Release media player...");
      libvlc.libvlc_media_player_release(mediaPlayerInstance);
      LOG.debug("Media player released");
      mediaPlayerInstance = null;
    }

    LOG.debug("Shut down listeners...");
    listenersService.shutdown();
    LOG.debug("Listeners shut down");
    
    metaService.shutdown();
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

  /**
   * 
   * 
   * @param media
   * @param mediaOptions
   */
  private void setMedia(String media, String... mediaOptions) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMedia(media=" + media + ",mediaOptions=" + Arrays.toString(mediaOptions) + ")");}

    libvlc_exception_t exception = new libvlc_exception_t();
    
    LibVlcMediaDescriptor mediaDescriptor = libvlc.libvlc_media_new(instance, media, exception);
    if(LOG.isDebugEnabled()) {LOG.debug("mediaDescriptor=" + mediaDescriptor);}
    
    if(standardMediaOptions != null) {
      for(String standardMediaOption : standardMediaOptions) {
        if(LOG.isDebugEnabled()) {LOG.debug("standardMediaOption=" + standardMediaOption);}
        libvlc.libvlc_media_add_option(mediaDescriptor, standardMediaOption, exception);
        checkException(exception);
      }
    }
    
    if(mediaOptions != null) {
      for(String mediaOption : mediaOptions) {
        if(LOG.isDebugEnabled()) {LOG.debug("mediaOption=" + mediaOption);}
        libvlc.libvlc_media_add_option(mediaDescriptor, mediaOption, exception);
        checkException(exception);
      }
    }
  
    // FIXME parsing causes problems e.g. when playing HTTP URLs
//    libvlc.libvlc_media_parse(mediaDescriptor);
    
//    libvlc_meta_t[] metas = libvlc_meta_t.values();
//    
//    for(libvlc_meta_t meta : metas) {
//      System.out.println("meta=" + libvlc.libvlc_media_get_meta(mediaDescriptor, meta.intValue()));
//    }
    
    libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaDescriptor, exception);
    checkException(exception);
    
    libvlc.libvlc_media_release(mediaDescriptor);
  }

  private Dimension getVideoDimension() {
    LOG.debug("getVideoDimension()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    
    int width = libvlc.libvlc_video_get_width(mediaPlayerInstance, exception);
    checkException(exception);
    
    int height = libvlc.libvlc_video_get_height(mediaPlayerInstance, exception);
    checkException(exception);

    if(width != -1 && height != -1) {
      return new Dimension(width, height);
    }
    else {
      return null;
    }
  }
  
  private boolean hasVideoOut() {
    LOG.trace("hasVideoOut()");
    
    libvlc_exception_t exception = new libvlc_exception_t();
    int hasVideoOut = libvlc.libvlc_media_player_has_vout(mediaPlayerInstance, exception);
    checkException(exception);
    
    return hasVideoOut != 0;
  }
  
  public void release() {
    LOG.debug("release()");
    
    if(!released) {
      destroyInstance();
      released = true;
    }
  }

  @Override
  protected synchronized void finalize() throws Throwable {
    LOG.debug("finalize()");
    
    release();
  }
  
  protected final void checkException(libvlc_exception_t exception) {
    int raised = exception.raised;

    if(raised != 0) {
      int code = exception.code;
      String message = exception.message;
      throw new RuntimeException("Code: " + code + ", Message: " + message);
    }
  }

  private void notifyListeners(libvlc_event_t event) {
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        int eventType = event.type;
        switch(LibVlcEventType.event(eventType)) {
          case libvlc_MediaPlayerPlaying:
            listener.playing(this);
            break;
        
          case libvlc_MediaPlayerPaused:
            listener.paused(this);
            break;
        
          case libvlc_MediaPlayerStopped:
            listener.stopped(this);
            break;
        
          case libvlc_MediaPlayerEndReached:
            listener.finished(this);
            break;
        
          case libvlc_MediaPlayerTimeChanged:
            long newTime = ((media_player_time_changed)event.event_type_specific.getTypedValue(media_player_time_changed.class)).new_time;
            listener.timeChanged(this, newTime);
            break;
  
          case libvlc_MediaPlayerPositionChanged:
            float newPosition = ((media_player_position_changed)event.event_type_specific.getTypedValue(media_player_position_changed.class)).new_position;
            listener.positionChanged(this, newPosition);
            break;
            
          case libvlc_MediaDurationChanged:
//            long newLength = ((media_player_length_changed)event.event_type_specific.getTypedValue(media_player_length_changed.class)).new_length;
//            listener.lengthChanged(this, newLength);
            break;
          }
      }
    }
  }

  private void notifyListeners(VideoMetaData videoMetaData) {
    if(LOG.isTraceEnabled()) {LOG.trace("notifyListeners(videoMetaData=" + videoMetaData + ")");}
    
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        listener.metaDataAvailable(this, videoMetaData);
      }
    }
  }

  private final class VlcVideoPlayerCallback implements LibVlcCallback {

    public void callback(libvlc_event_t event, Pointer userData) {
      if(LOG.isTraceEnabled()) {LOG.trace("callback(event=" + event + ",userData=" + userData + ")");}
      
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
      if(LOG.isTraceEnabled()) {LOG.trace("run()");}
      notifyListeners(event);
      if(LOG.isTraceEnabled()) {LOG.trace("runnable exits");}
    }
  }
  
  /**
   * With vlc, the meta data is not available until after the video output has
   * started.
   * <p>
   * Note that simply using the listener and handling the playing event will
   * <strong>not</strong> work.
   * <p>
   * This implementation loops, sleeping and checking, until libvlc reports that
   * video output is available.
   * <p>
   * This seems to be quite reliable but <strong>not</strong> 100% - on some
   * occasions the event seems not to fire. 
   * 
   * TODO is this still required with libvlc 1.1?
   */
  private final class NotifyMetaRunnable implements Runnable {

    @Override
    public void run() {
      LOG.trace("run()");
      
      for(;;) {
        try {
          Thread.sleep(VOUT_WAIT_PERIOD);

          if(hasVideoOut()) {
            VideoMetaData videoMetaData = new VideoMetaData();
            videoMetaData.setVideoDimension(getVideoDimension());
            videoMetaData.setSpuCount(getSpuCount());
            
            notifyListeners(videoMetaData);
            
            break;
          }
        }
        catch(InterruptedException e) {
        }
      }
      
      LOG.trace("runnable exits");
    }
  }

  /**
   * Event listener implementation that waits for video "playing" events.
   */
  private final class MetaDataEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void playing(MediaPlayer mediaPlayer) {
      if(LOG.isTraceEnabled()) {LOG.trace("playing(" + mediaPlayer + ")");}
      
      // Kick off an asynchronous task to obtain the video meta data (when
      // available)
      metaService.submit(new NotifyMetaRunnable());
    }
  }
  
  /**
   * Template method for setting the video surface natively.
   * <p>
   * Implementing classes should override this method to invoke the appropriate
   * libvlc method to set the video surface.
   * 
   * @param instance media player instance
   * @param videoSurface video surface component
   */
  protected abstract void nativeSetVideoSurface(LibVlcMediaInstance instance, Canvas videoSurface);
}
