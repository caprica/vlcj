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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_channel_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_navigate_mode_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.events.MediaPlayerEvent;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventFactory;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

// TODO this class file is over 2k lines and that is too many
// TODO do i need a new approach for robustness - i.e. check if release has been called, and guard each method - but then each method must lock the libvlc/mediaplayer in case someone else releases it!? - e.g. a critical section?

/**
 * Media player implementation.
 * <p>
 * This is the main class that developers working with VLCJ are expected to 
 * use.
 * <p>
 * This implementation provides the following functions:
 * <ul>
 *   <li>Status controls - e.g. length, time</li> 
 *   <li>Basic play-back controls - play, pause, stop</li>
 *   <li>Volume controls - volume level, mute</li>
 *   <li>Chapter controls - next/previous/set chapter, chapter count</li>
 *   <li>Sub-picture/sub-title controls - get/set, count</li>
 *   <li>Snapshot controls</li>
 *   <li>Logo controls - enable/disable, set opacity, file</li>
 *   <li>Marquee controls - enable/disable, set colour, size, opacity, timeout</li>
 *   <li>Video adjustment controls - contrast, brightness, hue, saturation, gamma</li>
 *   <li>Audio adjustment controls - delay</li>
 * </ul>
 * <p>
 * The basic life-cycle is:
 * <pre>
 *   // Set some options for libvlc
 *   String[] libvlcArgs = {...add options here...};
 * 
 *   // Create a factory
 *   MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
 * 
 *   // Create a full-screen strategy
 *   FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);
 *   
 *   // Create a media player instance for the run-time operating system
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer(fullScreenStrategy);
 *   
 *   // Set standard options as needed to be applied to all subsequently played media items
 *   String[] standardMediaOptions = {"video-filter=logo", "logo-file=vlcj-logo.png", "logo-opacity=25"}; 
 *   mediaPlayer.setStandardMediaOptions(standardMediaOptions);
 *
 *   // Add a component to be notified of player events
 *   mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {...add implementation here...});
 *   
 *   // Create and set a new component to display the rendered video (not shown: add the Canvas to a Frame)
 *   Canvas videoSurface = new Canvas();
 *   mediaPlayer.setVideoSurface(videoSurface);
 *
 *   // Play a particular item, with options if necessary
 *   String mediaPath = "/path/to/some/movie.mpg";
 *   String[] mediaOptions = {};
 *   mediaPlayer.playMedia(mediaPath, mediaOptions);
 *   
 *   // Do some interesting things in the application
 *   ...
 *   
 *   // Cleanly dispose of the media player instance and any associated native resources
 *   mediaPlayer.release();
 *   
 *   // Cleanly dispose of the media player factory and any associated native resources
 *   mediaPlayerFactory.release();
 * </pre>
 * With regard to overlaying logos there are three approaches.
 * <p>
 * The first way is to specify standard options for the media player - this will
 * set the logo for any subsequently played media item, for example:
 * <pre>
 *   String[] standardMediaOptions = {"video-filter=logo", "logo-file=vlcj-logo.png", "logo-opacity=25"};
 *   mediaPlayer.setStandardMediaOptions(standardMediaOptions);
 * </pre>
 * The second way is to specify options when playing the media item, for 
 * example:
 * <pre>
 *   String[] mediaOptions = {"video-filter=logo", "logo-file=vlcj-logo.png", "logo-opacity=25"};
 *   mediaPlayer.playMedia(mediaPath, mediaOptions);
 * </pre>
 * The final way is to use the methods of this class to set various logo 
 * properties, for example:
 * <pre>
 *   mediaPlayer.setLogoFile("vlcj-logo.png");
 *   mediaPlayer.setLogoOpacity(25);
 *   mediaPlayer.setLogoLocation(10, 10);
 *   mediaPlayer.enableLogo(true);
 * </pre>
 * For this latter method, it is not possible to enable the logo until after
 * the video has started playing. There is also a noticeable stutter in video
 * play-back when enabling the logo filter in this way.
 * <p>
 * With regard to overlaying marquees, again there are three approaches (similar
 * to those for logos).
 * <p>
 * In this instance only the final way showing the usage of the API is used, for
 * example:
 * <pre>
 *   mediaPlayer.setMarqueeText("VLCJ is quite good");
 *   mediaPlayer.setMarqueeSize(60);
 *   mediaPlayer.setMarqueeOpacity(70);
 *   mediaPlayer.setMarqueeColour(Color.green);
 *   mediaPlayer.setMarqueeTimeout(3000);
 *   mediaPlayer.setMarqueeLocation(300, 400);
 *   mediaPlayer.enableMarquee(true);
 * </pre>
 * With regard to video adjustment controls, after the video has started playing:
 * <pre>
 *   mediaPlayer.setAdjustVideo(true);
 *   mediaPlayer.setGamma(0.9f);
 *   mediaPlayer.setHue(10);
 * </pre>
 * Some media when played may cause one or more media sub-items to created. These 
 * sub-items subsequently need to be played. The media player can be set to 
 * automatically play these sub-items via {@link #setPlaySubItems(boolean)}, 
 * otherwise {@link #playNextSubItem()} can be invoked in response to a
 * {@link MediaPlayerEventListener#finished()} event.
 */
public abstract class MediaPlayer {

  private static final int VOUT_WAIT_PERIOD = 1000;
  
  protected final LibVlc libvlc = LibVlcFactory.factory().synchronise().log().create();
  
  private final List<MediaPlayerEventListener> eventListenerList = new ArrayList<MediaPlayerEventListener>();

  private final MediaPlayerEventFactory eventFactory = new MediaPlayerEventFactory(this);

  private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

  private final ExecutorService metaService = Executors.newSingleThreadExecutor();

  private libvlc_instance_t instance;
  private libvlc_media_player_t mediaPlayerInstance;
  private libvlc_event_manager_t mediaPlayerEventManager;
  private libvlc_callback_t callback;

  private String[] standardMediaOptions;
  
  private libvlc_media_stats_t libvlcMediaStats;

  /**
   * Flag whether or not to automatically play media sub-items if there are any.
   */
  private boolean playSubItems;
  
  /**
   * Set to true when the player has been released.
   */
  private AtomicBoolean released = new AtomicBoolean();
  
  /**
   * Create a new media player.
   * 
   * @param instance
   */
  public MediaPlayer(libvlc_instance_t instance) {
    Logger.debug("MediaPlayer(instance={})", instance);
    
    this.instance = instance;
    
    createInstance();
  }
  
  /**
   * Add a component to be notified of media player events.
   * 
   * @param listener component to notify
   */
  public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
    Logger.debug("addMediaPlayerEventListener(listener={})", listener);
    
    eventListenerList.add(listener);
  }

  /**
   * Remove a component that was previously interested in notifications of
   * media player events.
   * 
   * @param listener component to stop notifying
   */
  public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
    Logger.debug("removeMediaPlayerEventListener(listener={})", listener);
    
    eventListenerList.remove(listener);
  }

  // === Media Controls =======================================================
  
  /**
   * Set standard media options for all media items subsequently played.
   * <p>
   * This will <strong>not</strong> affect any currently playing media item.
   * 
   * @param options options to apply to all subsequently played media items
   */
  public void setStandardMediaOptions(String... options) {
    Logger.debug("setStandardMediaOptions(options={})", Arrays.toString(options));
    
    this.standardMediaOptions = options;
  }

  /**
   * Play a new media item.
   * <p>
   * The new media will begin play-back immediately.
   * 
   * @param mrl media resource locator
   */
  public void playMedia(String mrl) {
    Logger.debug("playMedia(mrl={})", mrl);
    
    playMedia(mrl, (String[])null);
  }
  
  /**
   * Play a new media item, with options.
   * <p>
   * The new media will begin play-back immediately.
   * 
   * @param mrl media resource locator
   * @param mediaOptions media item options
   */
  public void playMedia(String mrl, String... mediaOptions) {
    Logger.debug("playMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));

    // First 'prepare' the media...
    prepareMedia(mrl, mediaOptions);
    
    // ...then play it
    play();
  }

  /**
   * Prepare a new media item for play-back, but do not begin playing.
   * 
   * @param mrl media resource locator
   */
  public void prepareMedia(String mrl) {
    Logger.debug("prepareMedia(mrl={})", mrl);
    
    prepareMedia(mrl, (String[])null);
  }
  
  /**
   * Prepare a new media item for play-back, but do not begin playing.
   * 
   * @param mrl media resource locator
   * @param mediaOptions media item options
   */
  public void prepareMedia(String mrl, String... mediaOptions) {
    Logger.debug("prepareMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
    
    setMedia(mrl, mediaOptions);
  }
  
  /**
   * Add options to the current media. 
   * 
   * @param mediaOptions media options
   */
  public void addMediaOptions(String... mediaOptions) {
    Logger.debug("addMediaOptions(mediaOptions={})", Arrays.toString(mediaOptions));
    
    libvlc_media_t media = libvlc.libvlc_media_player_get_media(mediaPlayerInstance);
    Logger.trace("media={}", media);
    
    if(media != null) {
      for(String mediaOption : mediaOptions) {
        Logger.debug("mediaOption={}", mediaOption);
        libvlc.libvlc_media_add_option(media, mediaOption);
      }
      libvlc.libvlc_media_release(media);
    }
    else {
      throw new RuntimeException("No media");
    }
  }
  
  // === Sub-Item Controls ====================================================
  
  /**
   * Set whether or not the media player should automatically play media sub-
   * items.
   * 
   * @param playSubItems <code>true</code> to automatically play sub-items, otherwise <code>false</code>
   */
  public void setPlaySubItems(boolean playSubItems) {
    Logger.debug("setPlaySubItems(playSubItems={})", playSubItems);
    
    this.playSubItems = playSubItems;
  }

  /**
   * Play the next sub-item (if there is one).
   * 
   * @return <code>true</code> if there is a sub-item, otherwise <code>false</code>
   */
  public boolean playNextSubItem() {
    Logger.debug("playNextSubItem()");
    
    boolean subItemPlayed = false;
    
    libvlc_media_t media = libvlc.libvlc_media_player_get_media(mediaPlayerInstance);
    Logger.trace("media={}", media);
    
    if(media != null) {
      libvlc_media_list_t subItems = libvlc.libvlc_media_subitems(media);
      Logger.trace("subItems={}", subItems);
      
      if(subItems != null) {
        Logger.debug("Handling media sub-item...");
        libvlc.libvlc_media_list_lock(subItems);
        libvlc_media_t subItem = libvlc.libvlc_media_list_item_at_index(subItems, 0);
        if(subItem != null) {
          libvlc.libvlc_media_player_set_media(mediaPlayerInstance, subItem);
          libvlc.libvlc_media_player_play(mediaPlayerInstance);
          libvlc.libvlc_media_release(subItem);
          subItemPlayed = true;
        }
        libvlc.libvlc_media_list_unlock(subItems);
        libvlc.libvlc_media_list_release(subItems);
      }
    }
    
    Logger.debug("subItemPlayed={}", subItemPlayed);
    return subItemPlayed;
  }
  
  // === Status Controls ======================================================

  /**
   * 
   * 
   * @return
   */
  public boolean isPlayable() {
    Logger.trace("isPlayable()");

    return libvlc.libvlc_media_player_will_play(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isPlaying() {
    Logger.trace("isPlaying()");
    
    return libvlc.libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isSeekable() {
    Logger.trace("isSeekable()");
    
    return libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean canPause() {
    Logger.trace("canPause()");
    
    return libvlc.libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
  }
  
  /**
   * Get the length of the current media item.
   * 
   * @return length, in milliseconds
   */
  public long getLength() {
    Logger.trace("getLength()");
    
    return libvlc.libvlc_media_player_get_length(mediaPlayerInstance);
  }

  /**
   * Get the current play-back time.
   * 
   * @return current time, expressed as a number of milliseconds
   */
  public long getTime() {
    Logger.trace("getTime()");
    
    return libvlc.libvlc_media_player_get_time(mediaPlayerInstance);
  }

  /**
   * Get the current play-back position.
   * 
   * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
   */
  public float getPosition() {
    Logger.trace("getPosition()");
    
    return libvlc.libvlc_media_player_get_position(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getFps() {
    Logger.trace("getFps()");
    
    return libvlc.libvlc_media_player_get_fps(mediaPlayerInstance);
  }
  
  /**
   * Get the current video play rate.
   * 
   * @return rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
   */
  public float getRate() {
    Logger.trace("getRate()");
    
    return libvlc.libvlc_media_player_get_rate(mediaPlayerInstance);
  }

  /**
   * Get the number of video outputs for the media player.
   * 
   * @return number of video outputs, may be zero
   */
  public int getVideoOutputs() {
    Logger.trace("getVideoOutputs()");
    
    return libvlc.libvlc_media_player_has_vout(mediaPlayerInstance);
  }
  
  /**
   * Get the video size.
   * <p>
   * The video dimensions are not available until after the video has started
   * playing. 
   * 
   * @return video size if available, or <code>null</code>
   */
  public Dimension getVideoDimension() {
    Logger.debug("getVideoDimension()");
    
    IntByReference px = new IntByReference();
    IntByReference py = new IntByReference();
    int result = libvlc.libvlc_video_get_size(mediaPlayerInstance, 0, px, py);
    if(result == 0) {
      return new Dimension(px.getValue(), py.getValue());
    }
    else {
      return null;
    }
  }
  
  /**
   * 
   * 
   * @return
   */
  public String getAspectRatio() {
    Logger.debug("getAspectRatio()");
    
    return libvlc.libvlc_video_get_aspect_ratio(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getScale() {
    Logger.debug("getScale()");
    
    return libvlc.libvlc_video_get_scale(mediaPlayerInstance);
  }

  /**
   * 
   * 
   * @return
   */
  public String getCropGeometry() {
    Logger.debug("getCropGeometry()");
    
    return libvlc.libvlc_video_get_crop_geometry(mediaPlayerInstance);
  }

  /**
   * Get the current media statistics. 
   * <p>
   * Statistics are only updated if the video is playing.
   * 
   * @return media statistics
   */
  // FIXME For now I'll simply return the internal binding structure but I don't really want to do that do I?
  public libvlc_media_stats_t getMediaStatistics() {
    Logger.debug("getMediaStatistics()");
    
    // Must first check that the media is playing otherwise a fatal JVM crash
    // will occur
    if(isPlaying()) {
      libvlc_media_t mediaDescriptor = libvlc.libvlc_media_player_get_media(mediaPlayerInstance);
      if(mediaDescriptor != null) {
        libvlc.libvlc_media_get_stats(mediaDescriptor, libvlcMediaStats);
        libvlc.libvlc_media_release(mediaDescriptor);
      }
    }
    return libvlcMediaStats;
  }

  /**
   * 
   * 
   * @return
   */
  // FIXME For now I'll simply return the internal binding structure but I don't really want to do that do I?
  public libvlc_state_t getMediaState() {
    Logger.debug("getMediaState()");
    
    libvlc_state_t state = null;
    
    libvlc_media_t mediaDescriptor = libvlc.libvlc_media_player_get_media(mediaPlayerInstance);
    if(mediaDescriptor != null) {
      state = libvlc_state_t.state(libvlc.libvlc_media_get_state(mediaDescriptor));
      libvlc.libvlc_media_release(mediaDescriptor);
    }
    
    return state;
  }

  /**
   * 
   * 
   * @return
   */
  // FIXME For now I'll simply return the internal binding structure but I don't really want to do that do I?
  public libvlc_state_t getMediaPlayerState() {
    Logger.debug("getMediaPlayerState()");

    return libvlc_state_t.state(libvlc.libvlc_media_player_get_state(mediaPlayerInstance));
  }
  
  // === Title/Track Controls =================================================
  
  /**
   * Get the number of titles.
   *
   * @return number of titles, or -1 if none
   */
  public int getTitleCount() {
    Logger.debug("getTitleCount()");
    
    return libvlc.libvlc_media_player_get_title_count(mediaPlayerInstance);
  }

  /**
   * Get the current title.
   * 
   * @return title number
   */
  public int getTitle() {
    Logger.debug("getTitle()");
    
    return libvlc.libvlc_media_player_get_title(mediaPlayerInstance);
  }
  
  /**
   * Set a new title to play.
   * 
   * @param title title number
   */
  public void setTitle(int title) {
    Logger.debug("setTitle(title={})", title);
    
    libvlc.libvlc_media_player_set_title(mediaPlayerInstance, title);
  }
  
  /**
   * Get the number of available video tracks.
   * 
   * @return number of tracks
   */
  public int getVideoTrackCount() {
    Logger.debug("getVideoTrackCount()");
    
    return libvlc.libvlc_video_get_track_count(mediaPlayerInstance);
  }
  
  /**
   * Get the current video track.
   * <p>
   * This does not return the <strong>id</strong> of the track,
   * see {@link #getVideoDescriptions()}.
   * 
   * @return track number, starting at 1, or -1 if the video is currently disabled
   */
  public int getVideoTrack() {
    Logger.debug("getVideoTrack()");
    
    return libvlc.libvlc_video_get_track(mediaPlayerInstance);
  }
  
  /**
   * Set a new video track to play.
   * <p>
   * This does not take the track number returned from {@link #getVideoTrack()},
   * rather it takes the track <strong>id</strong> obtained from
   * see {@link #getVideoDescriptions()}.
   * 
   * @param track track id, or -1 to disable the video
   */
  public void setVideoTrack(int track) {
    Logger.debug("setVideoTrack(track={})", track);
    
    libvlc.libvlc_video_set_track(mediaPlayerInstance, track);
  }
  
  /**
   * Get the number of available audio tracks.
   * 
   * @return track count
   */
  public int getAudioTrackCount() {
    Logger.debug("getVideoTrackCount()");

    return libvlc.libvlc_audio_get_track_count(mediaPlayerInstance);
  }

  /**
   * Get the current audio track.
   * 
   * @return track number
   */
  public int getAudioTrack() {
    Logger.debug("getAudioTrack()");
    
    return libvlc.libvlc_audio_get_track(mediaPlayerInstance);
  }
  
  /**
   * Set a new audio track to play.
   * 
   * @param track track number
   */
  public void setAudioTrack(int track) {
    Logger.debug("setAudioTrack(track={})", track);
    
    libvlc.libvlc_audio_set_track(mediaPlayerInstance, track);
  }
  
  // === Basic Playback Controls ==============================================
  
  /**
   * Begin play-back.
   * <p>
   * If called when the play-back is paused, the play-back will resume from the
   * current position.
   */
  public void play() {
    Logger.debug("play()");

    onBeforePlay();

    libvlc.libvlc_media_player_play(mediaPlayerInstance);
  }

  /**
   * Stop play-back.
   * <p>
   * A subsequent play will play-back from the start.
   */
  public void stop() {
    Logger.debug("stop()");
    
    libvlc.libvlc_media_player_stop(mediaPlayerInstance);
  }

  /**
   * Pause/resume.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param pause true to pause, false to play/resume
   */
  public void setPause(boolean pause) {
    Logger.debug("setPause(pause={})", pause);
    
    libvlc.libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
  }
  
  /**
   * Pause play-back.
   * <p>
   * If the play-back is currently paused it will begin playing.
   */
  public void pause() {
    Logger.debug("pause()");
    
    libvlc.libvlc_media_player_pause(mediaPlayerInstance);
  }

  /**
   * Advance one frame. 
   */
  public void nextFrame() {
    Logger.debug("nextFrame()");
    
    libvlc.libvlc_media_player_next_frame(mediaPlayerInstance);
  }
  
  /**
   * Skip forward or backward by a period of time.
   * 
   * @param delta time period, in milliseconds
   */
  public void skip(long delta) {
    Logger.debug("skip(delta={})", delta);
    
    long current = getTime();
    Logger.debug("current={}", current);
    
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
    Logger.debug("skip(delta={})", delta);

    float current = getPosition();
    Logger.debug("current={}", current);

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
    Logger.debug("setTime(time={})", time);
    
    libvlc.libvlc_media_player_set_time(mediaPlayerInstance, time);
  }
  
  /**
   *  Jump to a specific position.
   * 
   * @param position position value, a percentage (e.g. 0.15 is 15%)
   */
  public void setPosition(float position) {
    Logger.debug("setPosition(position={})", position);
    
    libvlc.libvlc_media_player_set_position(mediaPlayerInstance, position);
  }
  
  /**
   * Set the video play rate.
   * <p>
   * Some media protocols are not able to change the rate.
   * 
   * @param rate rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
   * @return -1 on error, 0 on success
   */
  public int setRate(float rate) {
    Logger.debug("setRate(rate={})", rate);
    
    return libvlc.libvlc_media_player_set_rate(mediaPlayerInstance, rate);
  }
  
  /**
   * 
   * 
   * @param aspectRatio
   */
  public void setAspectRatio(String aspectRatio) {
    Logger.debug("setAspectRatio(aspectRatio={})", aspectRatio);
    
    libvlc.libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
  }
  
  /**
   * 
   * 
   * @param factor
   */
  public void setScale(float factor) {
    Logger.debug("setScale(factor={})", factor);
    
    libvlc.libvlc_video_set_scale(mediaPlayerInstance, factor);
  }
  
  /**
   * Set the crop geometry.
   * <p>
   * The format for the crop geometry is one of:
   * <ul>
   *   <li>numerator:denominator</li>
   *   <li>width:height+x+y</li>
   *   <li>left:top:right:bottom</li>
   * </ul>
   * For example:
   * <pre>
   *   mediaPlayer.setCropGeometry("4:3");
   *   mediaPlayer.setCropGeometry("719x575+0+0");
   *   mediaPlayer.setCropGeometry("6:10:6:10");
   * </pre>
   * 
   * @param cropGeometry formatted string describing the desired crop geometry
   */
  public void setCropGeometry(String cropGeometry) {
    Logger.debug("setCropGeometry(cropGeometry={})", cropGeometry);
    
    libvlc.libvlc_video_set_crop_geometry(mediaPlayerInstance, cropGeometry);
  }
  
  // === Audio Controls =======================================================

  /**
   * Set the desired audio output.
   * 
   * The change will not be applied until the media player has been stopped and
   * then played again.
   * 
   * @param outputName name of the desired audio output
   */
  public void selectAudioOutput(String outputName) {
    Logger.debug("selectAudioOutput(outputName={})", outputName);
    
    libvlc.libvlc_audio_output_set(mediaPlayerInstance, outputName);
  }
  
  /**
   * Toggle volume mute.
   */
  public void mute() {
    Logger.debug("mute()");
    
    libvlc.libvlc_audio_toggle_mute(mediaPlayerInstance);
  }
  
  /**
   * Mute or un-mute the volume.
   * 
   * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
   */
  public void mute(boolean mute) {
    Logger.debug("mute(mute={})", mute);
    
    libvlc.libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
  }
  
  /**
   * Test whether or not the volume is currently muted.
   * 
   * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
   */
  public boolean isMute() {
    Logger.debug("isMute()");
    
    return libvlc.libvlc_audio_get_mute(mediaPlayerInstance) != 0;
  }
  
  /**
   * Get the current volume.
   * 
   * @return volume, in the range 0 to 100 where 100 is full volume
   */
  public int getVolume() {
    Logger.debug("getVolume()");
    
    return libvlc.libvlc_audio_get_volume(mediaPlayerInstance);
  }
  
  /**
   * Set the volume.
   * 
   * @param volume volume, in the range 0 to 200 where 200 is full volume 
   */
  public void setVolume(int volume) {
    Logger.debug("setVolume(volume={})", volume);
    
    libvlc.libvlc_audio_set_volume(mediaPlayerInstance, volume);
  }

  /**
   * Get the current audio channel.
   * 
   * For channel values see {@link libvlc_audio_output_channel_t}.
   * 
   * <strong>Warning this API is subject to change.</strong>
   * 
   * @return audio channel
   */
  public int getAudioChannel() {
    Logger.debug("getAudioChannel()");
    
    return libvlc.libvlc_audio_get_channel(mediaPlayerInstance);
  }
  
  /**
   * Set the audio channel.
   * 
   * For channel values see {@link libvlc_audio_output_channel_t}.
   * 
   * <strong>Warning this API is subject to change.</strong>
   * 
   * @param channel channel
   */
  public void setAudioChannel(int channel) {
    Logger.debug("setAudioChannel(channel={})", channel);

    libvlc.libvlc_audio_set_channel(mediaPlayerInstance, channel);
  }
  
  /**
   * Get the audio delay.
   * 
   * @return audio delay, in microseconds
   */
  public long getAudioDelay() {
    Logger.debug("getAudioDelay()");
    
    return libvlc.libvlc_audio_get_delay(mediaPlayerInstance);
  }

  /**
   * Set the audio delay.
   * <p>
   * The audio delay is set for the current item only and will be reset to zero
   * each time the media changes.
   * 
   * @param delay desired audio delay, in microseconds
   */
  public void setAudioDelay(long delay) {
    Logger.debug("setAudioDelay(delay={})", delay);
    
    libvlc.libvlc_audio_set_delay(mediaPlayerInstance, delay);
  }
  
  // === Chapter Controls =====================================================

  /**
   * Get the chapter count.
   * 
   * @return number of chapters, or -1 if no chapters
   */
  public int getChapterCount() {
    Logger.trace("getChapterCount()");
    
    return libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance);
  }
  
  /**
   * Get the current chapter.
   * 
   * @return chapter number, where zero is the first chatper, or -1 if no media
   */
  public int getChapter() {
    Logger.trace("getChapter()");
    
    return libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance);
  }
  
  /**
   * Set the chapter.
   * 
   * @param chapterNumber chapter number, where zero is the first chapter
   */
  public void setChapter(int chapterNumber) {
    Logger.debug("setChapter(chapterNumber={})", chapterNumber);
    
    libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber);
  }
  
  /**
   * Jump to the next chapter.
   * <p>
   * If the play-back is already at the last chapter, this will have no effect.
   */
  public void nextChapter() {
    Logger.debug("nextChapter()");
    
    libvlc.libvlc_media_player_next_chapter(mediaPlayerInstance);
  }
  
  /**
   * Jump to the previous chapter.
   * <p>
   * If the play-back is already at the first chapter, this will have no effect.
   */
  public void previousChapter() {
    Logger.debug("previousChapter()");
    
    libvlc.libvlc_media_player_previous_chapter(mediaPlayerInstance);
  }

  // === DVD Menu Navigation Controls =========================================

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  public void menuActivate() {
    Logger.debug("menuActivate()");
    libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_activate.intValue());
  }
  
  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  public void menuUp() {
    Logger.debug("menuUp()");
    libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_up.intValue());
  }
  
  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  public void menuDown() {
    Logger.debug("menuDown()");
    libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_down.intValue());
  }
  
  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  public void menuLeft() {
    Logger.debug("menuLeft()");
    libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_left.intValue());
  }

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  public void menuRight() {
    Logger.debug("menuRight()");
    libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_right.intValue());
  }
  
  // === Sub-Picture/Sub-Title Controls =======================================
  
  /**
   * Get the number of sub-pictures/sub-titles.
   *
   * @return number of sub-titles
   */
  public int getSpuCount() {
    Logger.debug("getSpuCount()");
    
    return libvlc.libvlc_video_get_spu_count(mediaPlayerInstance);
  }
  
  /**
   * Get the current sub-title track.
   * 
   * @return sub-title number, or -1 if none
   */
  public int getSpu() {
    Logger.debug("getSpu()");
    
    return libvlc.libvlc_video_get_spu(mediaPlayerInstance);
  }
  
  /**
   * Set the current sub-title track.
   * 
   * @param spu sub-title number, or -1 for none
   */
  public void setSpu(int spu) {
    Logger.debug("setSpu(spu={})", spu);
    
    int spuCount = getSpuCount();
    Logger.debug("spuCount={}", spuCount);
    
    if(spuCount != 0 && spu <= spuCount) {
      libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu);
    }
    else {
      Logger.debug("Ignored out of range spu number {} because spu count is {}", spu, spuCount);
    }
  }

  /**
   * Select the next sub-title track (or disable sub-titles).
   */
  public void cycleSpu() {
    Logger.debug("cycleSpu()");
    
    int spu = getSpu();
    int spuCount = getSpuCount();
    
    if(spu >= spuCount) {
      spu = 0;
    }
    else {
      spu++;
    }
    
    setSpu(spu);
  }
  
  // === Description Controls =================================================
  
  /**
   * Get the title descriptions. 
   * 
   * @return list of descriptions
   */
  public List<TrackDescription> getTitleDescriptions() {
    Logger.debug("getTitleDescriptions()");
    
    List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
    libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_title_description(mediaPlayerInstance);
    libvlc_track_description_t trackDescription = trackDescriptions;
    while(trackDescription != null) {
      trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
      trackDescription = trackDescription.p_next;      
    }   
    if(trackDescriptions != null) {
      libvlc.libvlc_track_description_release(trackDescriptions.getPointer());
    }
    return trackDescriptionList;
  }  
  
  /**
   * Get the video (i.e. "title") track descriptions.
   * 
   * @return list of descriptions
   */
  public List<TrackDescription> getVideoDescriptions() {
    Logger.debug("getVideoDescriptions()");
    
    List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
    libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_track_description(mediaPlayerInstance);
    libvlc_track_description_t trackDescription = trackDescriptions;
    while(trackDescription != null) {
      trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
      trackDescription = trackDescription.p_next;      
    }   
    if(trackDescriptions != null) {
      libvlc.libvlc_track_description_release(trackDescriptions.getPointer());
    }
    return trackDescriptionList;
  }
  
  /**
   * Get the audio track descriptions. 
   * 
   * @return list of descriptions
   */
  public List<TrackDescription> getAudioDescriptions() {
    Logger.debug("getAudioDescriptions()");
    
    List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
    libvlc_track_description_t trackDescriptions = libvlc.libvlc_audio_get_track_description(mediaPlayerInstance);
    libvlc_track_description_t trackDescription = trackDescriptions;
    while(trackDescription != null) {
      trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
      trackDescription = trackDescription.p_next;      
    }   
    if(trackDescriptions != null) {
      libvlc.libvlc_track_description_release(trackDescriptions.getPointer());
    }
    return trackDescriptionList;
  }  
  
  /**
   * Get the sub-title track descriptions. 
   * 
   * @return list of descriptions
   */
  public List<TrackDescription> getSpuDescriptions() {
    Logger.debug("getSpuDescriptions()");
    
    List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
    libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_spu_description(mediaPlayerInstance);
    libvlc_track_description_t trackDescription = trackDescriptions;
    while(trackDescription != null) {
      trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
      trackDescription = trackDescription.p_next;      
    }   
    if(trackDescriptions != null) {
      libvlc.libvlc_track_description_release(trackDescriptions.getPointer());
    }
    return trackDescriptionList;
  }  

  /**
   * Get the chapter descriptions for a title.
   * 
   * @param title title number TODO is it number or index?
   * @return list of descriptions
   */
  public List<String> getChapterDescriptions(int title) {
    Logger.debug("getChapterDescriptions(title={})", title);
    
    List<String> trackDescriptionList = new ArrayList<String>();
    libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_chapter_description(mediaPlayerInstance, title);
    libvlc_track_description_t trackDescription = trackDescriptions;
    while(trackDescription != null) {
      trackDescriptionList.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }
    if(trackDescriptions != null) {
      libvlc.libvlc_track_description_release(trackDescriptions.getPointer());
    }
    return trackDescriptionList;
  }  
  
  // === Snapshot Controls ====================================================

  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * The snapshot will be created in the user's home directory and be assigned
   * a filename based on the current time.
   */
  public void saveSnapshot() {
    Logger.debug("saveSnapshot()");
    
    File snapshotDirectory = new File(System.getProperty("user.home"));
    File snapshotFile = new File(snapshotDirectory, "vlcj-snapshot-" + System.currentTimeMillis() + ".png");
    saveSnapshot(snapshotFile);
  }
  
  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * Any missing directory path will be created if it does not exist.
   * 
   * @param file file to contain the snapshot
   */
  public void saveSnapshot(File file) {
    Logger.debug("saveSnapshot(file={})", file);
    
    File snapshotDirectory = file.getParentFile();
    if(!snapshotDirectory.exists()) {
      snapshotDirectory.mkdirs();
    }
    
    if(snapshotDirectory.exists()) {
      libvlc.libvlc_video_take_snapshot(mediaPlayerInstance, 0, file.getAbsolutePath(), 0, 0);
    }
    else {
      throw new RuntimeException("Directory does not exist and could not be created for '" + file.getAbsolutePath() + "'");
    }
  }

  /**
   * Get a snapshot of the currently playing video.
   * <p>
   * This implementation uses the native libvlc method to save a snapshot of
   * the currently playing video. This snapshot is saved to a temporary file
   * and then the resultant image is loaded from the file.
   * <p>
   * The size of the image will be that produced by the libvlc native snapshot
   * function.
   * 
   * @return snapshot image
   */
  public BufferedImage getSnapshot() {
    Logger.debug("getSnapshot()");
    try {
      File file = File.createTempFile("vlcj-snapshot-", ".png");
      Logger.debug("file={}", file.getAbsolutePath());
      saveSnapshot(file);
      BufferedImage snapshotImage = ImageIO.read(file);
      boolean deleted = file.delete();
      Logger.debug("deleted={}", deleted);
      return snapshotImage;
    }
    catch(IOException e) {
      throw new RuntimeException("Failed to get snapshot image", e);
    }
  }
  
  // === Logo Controls ========================================================

  /**
   * Enable/disable the logo.
   * <p>
   * The logo will not be enabled if there is currently no video being played.
   * 
   * @param enable <code>true</code> to show the logo, <code>false</code> to hide it
   */
  public void enableLogo(boolean enable) {
    Logger.debug("enableLogo(enable={})", enable);
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
  }

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity in the range 0 to 255 where 255 is fully opaque
   */
  public void setLogoOpacity(int opacity) {
    Logger.debug("setLogoOpacity(opacity={})", opacity);
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
  }

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  public void setLogoOpacity(float opacity) {
    Logger.debug("setLogoOpacity(opacity={})", opacity);
    
    int opacityValue = Math.round(opacity * 255.0f);
    Logger.debug("opacityValue={}", opacityValue);
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
  }
  
  /**
   * Set the logo location.
   * 
   * @param x x co-ordinate for the top left of the logo
   * @param y y co-ordinate for the top left of the logo
   */
  public void setLogoLocation(int x, int y) {
    Logger.debug("setLogoLocation(x={},y={})", x ,y);
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
  }

  /**
   * Set the logo position.
   * 
   * @param position position
   */
  public void setLogoPosition(libvlc_logo_position_e position) {
    Logger.debug("setLogoPosition(position={})", position);
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    
  }
  
  /**
   * Set the logo file.
   * 
   * @param logoFile logo file name
   */
  public void setLogoFile(String logoFile) {
    Logger.debug("setLogoFile(logoFile={})", logoFile);
    
    libvlc.libvlc_video_set_logo_string(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_file.intValue(), logoFile);
  }
  
  // === Marquee Controls =====================================================

  /**
   * Enable/disable the marquee.
   * <p>
   * The marquee will not be enabled if there is currently no video being played.
   * 
   * @param enable <code>true</code> to show the marquee, <code>false</code> to hide it
   */
  public void enableMarquee(boolean enable) {
    Logger.debug("enableMarquee(enable={})", enable);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
  }

  /**
   * Set the marquee text.
   * 
   * @param text text
   */
  public void setMarqueeText(String text) {
    Logger.debug("setMarqueeText(text={})", text);
    
    libvlc.libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
  }

  /**
   * Set the marquee colour.
   * 
   * @param colour colour, any alpha component will be masked off
   */
  public void setMarqueeColour(Color colour) {
    Logger.debug("setMarqueeColour(colour={})", colour);
    
    setMarqueeColour(colour.getRGB() & 0x00ffffff);
  }

  /**
   * Set the marquee colour.
   * 
   * @param colour RGB colour value
   */
  public void setMarqueeColour(int colour) {
    Logger.debug("setMarqueeColour(colour={})", colour);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
  }
  
  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity in the range 0 to 100 where 255 is fully opaque
   */
  public void setMarqueeOpacity(int opacity) {
    Logger.debug("setMarqueeOpacity(opacity={})", opacity);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
  }
  
  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  public void setMarqueeOpacity(float opacity) {
    Logger.debug("setMarqueeOpacity(opacity={})", opacity);
    
    int opacityValue = Math.round(opacity * 255.0f);
    Logger.debug("opacityValue={}", opacityValue);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
  }
  
  /**
   * Set the marquee size.
   * 
   * @param size size, height of the marquee text in pixels
   */
  public void setMarqueeSize(int size) {
    Logger.debug("setMarqueeSize(size={})", size);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
  }
  
  /**
   * Set the marquee timeout. 
   * 
   * @param timeout timeout, in milliseconds
   */
  public void setMarqueeTimeout(int timeout) {
    Logger.debug("setMarqueeTimeout(timeout={})", timeout);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
  }
  
  /**
   * Set the marquee location.
   * 
   * @param x x co-ordinate for the top left of the marquee
   * @param y y co-ordinate for the top left of the marquee
   */
  public void setMarqueeLocation(int x, int y) {
    Logger.debug("setMarqueeLocation(x={},y={})", x ,y);
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_X.intValue(), x);
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Y.intValue(), y);
  }
  
  // === Filter Controls ======================================================
  
  /**
   * Set the de-interlace filter to use.
   * 
   * Available modes:
   * <ul>
   *   <li>discard</li>
   *   <li>blend</li>
   *   <li>mean</li>
   *   <li>bob</li>
   *   <li>linear</li>
   *   <li>x</li>
   *   <li>yadif</li>
   *   <li>yadif2x</li>
   * </ul>
   * 
   * @param deinterlaceMode mode, or null to disable the de-interlace filter 
   */
  public void setDeinterlace(String deinterlaceMode) {
    Logger.debug("setDeinterlace(deinterlaceMode={})", deinterlaceMode);
    
    libvlc.libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlaceMode);
  }
  
  // === Video Adjustment Controls ============================================
  
  /**
   * Enable/disable the video adjustments.
   * <p>
   * The video adjustment controls must be enabled after the video has started 
   * playing.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param adjustVideo true if the video adjustments are enabled, otherwise false 
   */
  public void setAdjustVideo(boolean adjustVideo) {
    Logger.debug("setAdjustVideo(adjustVideo={})", adjustVideo);
    
    libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue(), adjustVideo ? 1 : 0);
  }
  
  /**
   * Test whether or not the video adjustments are enabled.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return true if the video adjustments are enabled, otherwise false
   */
  public boolean isAdjustVideo() {
    Logger.debug("isAdjustVideo()");
    
    return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue()) == 1;
  }
  
  /**
   * Get the current video contrast.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return contrast, in the range from 0.0 to 2.0
   */
  public float getContrast() {
    Logger.debug("getContrast()");
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
  }
  
  /**
   * Set the video contrast.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * 
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param contrast contrast value, in the range from 0.0 to 2.0
   */
  public void setContrast(float contrast) {
    Logger.debug("setContrast(contrast={})", contrast);
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue(), contrast);
  }
  
  /**
   * Get the current video brightness.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return brightness, in the range from 0.0 to 2.0
   */
  public float getBrightness() {
    Logger.debug("getBrightness()");
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
  }
  
  /**
   * Set the video brightness.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param brightness brightness value, in the range from 0.0 to 2.0
   */
  public void setBrightness(float brightness) {
    Logger.debug("setBrightness(brightness={})", brightness);
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue(), brightness);
  }
  
  /**
   * Get the current video hue.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return hue, in the range from 0 to 360
   */
  public int getHue() {
    Logger.debug("getHue()");
    
    return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
  }
  
  /**
   * Set the video hue.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param hue hue value, in the range from 0 to 360
   */
  public void setHue(int hue) {
    Logger.debug("setHue(hue={})", hue);
    
    libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue(), hue);
  }
  
  /**
   * Get the current video saturation.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return saturation, in the range from 0.0 to 3.0
   */
  public float getSaturation() {
    Logger.debug("getSaturation()");
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
  }
  
  /**
   * Set the video saturation.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param saturation saturation value, in the range from 0.0 to 3.0
   */
  public void setSaturation(float saturation) {
    Logger.debug("setSaturation(saturation={})", saturation);
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
  }
  
  /**
   * Get the current video gamma.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return gamma value, in the range from 0.01 to 10.0
   */
  public float getGamma() {
    Logger.debug("getGamma()");
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
  }
  
  /**
   * Set the video gamma.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param gamma gamma, in the range from 0.01 to 10.0
   */
  public void setGamma(float gamma) {
    Logger.debug("setGamma(gamma={})", gamma);
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
  }
  
  // === User Interface =======================================================
  
  /**
   * 
   * 
   * @param enable
   */
  public void setEnableMouseInputHandling(boolean enable) {
    Logger.debug("setEnableMouseInputHandling(enable={})", enable);
    
    libvlc.libvlc_video_set_mouse_input(mediaPlayerInstance, enable ? 1 : 0);
  }
  
  /**
   * 
   * 
   * @param enable
   */
  public void setEnableKeyInputHandling(boolean enable) {
    Logger.debug("setEnableKeyInputHandling(enable={})", enable);
    
    libvlc.libvlc_video_set_key_input(mediaPlayerInstance, enable ? 1 : 0);
  }
  
  // === Implementation =======================================================

  /**
   * Release the media player, freeing all associated (including native) resources.
   */
  public final void release() {
    Logger.debug("release()");
    
    if(released.compareAndSet(false, true)) {
      destroyInstance();
      onAfterRelease();
    }
  }

  /**
   * Create and prepare the native media player resources.
   */
  private void createInstance() {
    Logger.debug("createInstance()");
    
    mediaPlayerInstance = libvlc.libvlc_media_player_new(instance);
    Logger.debug("mediaPlayerInstance={}", mediaPlayerInstance);
    
    mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance);
    Logger.debug("mediaPlayerEventManager={}", mediaPlayerEventManager);
  
    registerEventListener();

    eventListenerList.add(new MetaDataEventHandler());
    eventListenerList.add(new SubItemEventHandler());
  }

  /**
   * Clean up the native media player resources.
   */
  private void destroyInstance() {
    Logger.debug("destroyInstance()");
    
    Logger.debug("Detach events...");
    deregisterEventListener();
    Logger.debug("Events detached.");

    eventListenerList.clear();
    
    if(mediaPlayerInstance != null) {
      Logger.debug("Release media player...");
      libvlc.libvlc_media_player_release(mediaPlayerInstance);
      Logger.debug("Media player released");
    }

    Logger.debug("Shut down listeners...");
    listenersService.shutdown();
    Logger.debug("Listeners shut down");
    
    metaService.shutdown();
  }

  /**
   * 
   */
  private void registerEventListener() {
    Logger.debug("registerEventListener()");
    
    callback = new VlcVideoPlayerCallback();

    for(libvlc_event_e event : libvlc_event_e.values()) {
      if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaPlayerLengthChanged.intValue()) {
        Logger.debug("event={}", event);
        int result = libvlc.libvlc_event_attach(mediaPlayerEventManager, event.intValue(), callback, null);
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
        if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaPlayerLengthChanged.intValue()) {
          Logger.debug("event={}", event);
          libvlc.libvlc_event_detach(mediaPlayerEventManager, event.intValue(), callback, null);
        }
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
    Logger.debug("setMedia(media={},mediaOptions={})" , media, Arrays.toString(mediaOptions));
    
    libvlc_media_t mediaDescriptor = libvlc.libvlc_media_new_path(instance, media);
    Logger.debug("mediaDescriptor={}", mediaDescriptor);
    
    if(standardMediaOptions != null) {
      for(String standardMediaOption : standardMediaOptions) {
        Logger.debug("standardMediaOption={}", standardMediaOption);
        libvlc.libvlc_media_add_option(mediaDescriptor, standardMediaOption);
      }
    }
    
    if(mediaOptions != null) {
      for(String mediaOption : mediaOptions) {
        Logger.debug("mediaOption={}", mediaOption);
        libvlc.libvlc_media_add_option(mediaDescriptor, mediaOption);
      }
    }
  
    // FIXME parsing causes problems e.g. when playing HTTP URLs
//    libvlc.libvlc_media_parse(mediaDescriptor);
    
//    libvlc_meta_t[] metas = libvlc_meta_t.values();
//    
//    for(libvlc_meta_t meta : metas) {
//      System.out.println("meta=" + libvlc.libvlc_media_get_meta(mediaDescriptor, meta.intValue()));
//    }
    
    libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaDescriptor);
    libvlc.libvlc_media_release(mediaDescriptor);

    // Prepare a new statistics object to re-use for the new media item
    libvlcMediaStats = new libvlc_media_stats_t();
  }

  private void notifyListeners(VideoMetaData videoMetaData) {
    Logger.trace("notifyListeners(videoMetaData={})", videoMetaData);
    
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        listener.metaDataAvailable(this, videoMetaData);
      }
    }
  }

  private final class VlcVideoPlayerCallback implements libvlc_callback_t {

    public void callback(libvlc_event_t event, Pointer userData) {
      Logger.trace("callback(event={},userData={})", event, userData);
      
      // Notify listeners in a different thread - the other thread is
      // necessary to prevent a potential native library failure if the
      // native library is re-entered
      if(!eventListenerList.isEmpty()) {
        // Create a new media player event for the native event
        MediaPlayerEvent mediaPlayerEvent = eventFactory.newMediaPlayerEvent(event);
        Logger.trace("mediaPlayerEvent={}", mediaPlayerEvent);
        if(mediaPlayerEvent != null) {
          // Notify listeners in a different thread - the other thread is
          // necessary to prevent a potential native library failure if the
          // native library is re-entered
          listenersService.submit(new NotifyListenersRunnable(mediaPlayerEvent));
        }
      }
    }
  }

  private final class NotifyListenersRunnable implements Runnable {

    private final MediaPlayerEvent mediaPlayerEvent;

    private NotifyListenersRunnable(MediaPlayerEvent mediaPlayerEvent) {
      this.mediaPlayerEvent = mediaPlayerEvent;
    }

//    @Override
    public void run() {
      Logger.trace("run()");
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        try {
          mediaPlayerEvent.notify(listener);
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
   * TODO think about having a specific event that fires when video output is detect
   * FIXME this should latch on the playing event rather than sleeping and looping
   */
  private final class NotifyMetaRunnable implements Runnable {

//    @Override
    public void run() {
      Logger.trace("run()");
      
      for(;;) {
        try {
          Logger.trace("Waiting for video output...");
          
          Thread.sleep(VOUT_WAIT_PERIOD);

          Logger.trace("Checking for video output...");
          
          int videoOutputs = getVideoOutputs();
          Logger.trace("videoOutputs={}", videoOutputs);
          
          boolean isPlaying = isPlaying();
          
          if(isPlaying) {
            VideoMetaData videoMetaData = new VideoMetaData();
            videoMetaData.setVideoDimension(getVideoDimension());
            
            videoMetaData.setTitleCount(getTitleCount());
            videoMetaData.setVideoTrackCount(getVideoTrackCount());
            videoMetaData.setAudioTrackCount(getAudioTrackCount());
            videoMetaData.setSpuCount(getSpuCount());

            videoMetaData.setTitleDescriptions(getTitleDescriptions());
            videoMetaData.setVideoDescriptions(getVideoDescriptions());
            videoMetaData.setAudioDescriptions(getAudioDescriptions());
            videoMetaData.setSpuDescriptions(getSpuDescriptions());

            Map<Integer, List<String>> allChapterDescriptions = new TreeMap<Integer, List<String>>();
            for(int i = 0; i < getTitleCount(); i++) {
              allChapterDescriptions.put(i, getChapterDescriptions(i));
            }
            videoMetaData.setChapterDescriptions(allChapterDescriptions);

            notifyListeners(videoMetaData);

            break;
          }
        }
        catch(InterruptedException e) {
        }
      }
      
      Logger.trace("runnable exits");
    }
  }

  /**
   * Event listener implementation that responds to video "playing" events to
   * process media meta data.
   */
  private final class MetaDataEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void playing(MediaPlayer mediaPlayer) {
      Logger.trace("playing(mediaPlayer={})", mediaPlayer);
      
      // Kick off an asynchronous task to obtain the video meta data (when
      // available)
      metaService.submit(new NotifyMetaRunnable());
    }
  }
  
  /**
   * Event listener implementation that handles media sub-items.
   * <p>
   * Some media types when you 'play' them do not actually play any media and
   * instead sub-items are created and attached to the current media 
   * descriptor.  
   * <p>
   * This event listener responds to the media player "finished" event by
   * getting the current media from the player and automatically playing the
   * first sub-item (if there is one).
   * <p>
   * Note that the sub-item will be automatically 'consumed' after is has
   * finished playing so even though this listener will be called back at the 
   * end of the sub-item, it will not loop playing that same sub-item forever. 
   * <p>
   * If there is more than one sub-item, then they will simply be played and
   * consumed in order.
   */
  private final class SubItemEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void finished(MediaPlayer mediaPlayer) {
      Logger.trace("finished(mediaPlayer={})", mediaPlayer);

      if(playSubItems) {
        playNextSubItem();
      }
    }
  }
  
  /**
   * Provide access to the native media player instance. 
   * 
   * @return media player instance
   */
  public final libvlc_media_player_t mediaPlayerInstance() {
    return mediaPlayerInstance;
  }

  /**
   * Allow sub-classes to do something just before the video is started.
   */
  protected void onBeforePlay() {
    // Base implementation does nothing
  }
  
  /**
   * Allow sub-classes to clean-up.
   */
  protected void onAfterRelease() {
    // Base implementation does nothing
  }
}
