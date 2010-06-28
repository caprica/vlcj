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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
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

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;
import uk.co.caprica.vlcj.binding.internal.media_duration_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_length_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_position_changed;
import uk.co.caprica.vlcj.binding.internal.media_player_time_changed;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

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
 *   <li>Full-screen controls</li>
 *   <li>Video adjustment controls - contrast, brightness, hue, saturation, gamma</li>
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
 *   MediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer(fullScreenStrategy);
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
 */
public abstract class MediaPlayer {

  /**
   * Log.
   */
  private static final Logger LOG = Logger.getLogger(MediaPlayer.class);
  
  private static final int VOUT_WAIT_PERIOD = 1000;
  
  // TODO
//  protected final LibVlc libvlc = LibVlc.SYNC_INSTANCE;
  protected final LibVlc libvlc = LibVlc.LOGGING_INSTANCE;
  
  private final List<MediaPlayerEventListener> eventListenerList = new ArrayList<MediaPlayerEventListener>();

  private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

  private final ExecutorService metaService = Executors.newSingleThreadExecutor();
  
  private final FullScreenStrategy fullScreenStrategy;

  private libvlc_instance_t instance;
  private libvlc_media_player_t mediaPlayerInstance;
  private libvlc_event_manager_t mediaPlayerEventManager;
  private libvlc_callback_t callback;

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
  public MediaPlayer(libvlc_instance_t instance) {
    this(null, instance);
  }
  
  /**
   * Create a new media player with a full-screen strategy.
   * 
   * @param fullScreenStrategy
   * @param instance
   */
  public MediaPlayer(FullScreenStrategy fullScreenStrategy, libvlc_instance_t instance) {
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

    return libvlc.libvlc_media_player_will_play(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isPlaying() {
    LOG.trace("isPlaying()");
    
    return libvlc.libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isSeekable() {
    LOG.trace("isSeekable()");
    
    return libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean canPause() {
    LOG.trace("canPause()");
    
    return libvlc.libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
  }
  
  /**
   * Get the length of the current media item.
   * 
   * @return length, in milliseconds
   */
  public long getLength() {
    LOG.trace("getLength()");
    
    return libvlc.libvlc_media_player_get_length(mediaPlayerInstance);
  }

  /**
   * Get the current play-back time.
   * 
   * @return current time, expressed as a number of milliseconds
   */
  public long getTime() {
    LOG.trace("getTime()");
    
    return libvlc.libvlc_media_player_get_time(mediaPlayerInstance);
  }

  /**
   * Get the current play-back position.
   * 
   * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
   */
  public float getPosition() {
    LOG.trace("getPosition()");
    
    return libvlc.libvlc_media_player_get_position(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getFps() {
    LOG.trace("getFps()");
    
    return libvlc.libvlc_media_player_get_fps(mediaPlayerInstance);
  }
  
  /**
   * Get the current video play rate.
   * 
   * @param rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
   */
  public float getRate() {
    LOG.trace("getRate()");
    
    return libvlc.libvlc_media_player_get_rate(mediaPlayerInstance);
  }

  /**
   * 
   * 
   * @return
   */
  public int getVideoOutputs() {
    LOG.trace("getVideoOutputs()");
    
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
    LOG.debug("getVideoDimension()");
    
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
    LOG.debug("getAspectRatio()");
    
    return libvlc.libvlc_video_get_aspect_ratio(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  public float getScale() {
    LOG.debug("getScale()");
    
    return libvlc.libvlc_video_get_scale(mediaPlayerInstance);
  }
  
  // === Title/Track Controls =================================================
  
  /**
   * Get the number of titles.
   *
   * @return number of titles, or -1 if none
   */
  public int getTitleCount() {
    LOG.debug("getTitleCount()");
    
    return libvlc.libvlc_media_player_get_title_count(mediaPlayerInstance);
  }

  /**
   * 
   * 
   * @return
   */
  public int getTitle() {
    LOG.debug("getTitle()");
    
    return libvlc.libvlc_media_player_get_title(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @param track
   */
  public void setTitle(int title) {
    if(LOG.isDebugEnabled()) {LOG.debug("setTitle(title=" + title + ")");}
    
    libvlc.libvlc_media_player_set_title(mediaPlayerInstance, title);
  }
  
  /**
   * 
   * 
   * @return
   */
  public int getVideoTrackCount() {
    LOG.debug("getVideoTrackCount()");
    
    return libvlc.libvlc_video_get_track_count(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  public int getVideoTrack() {
    LOG.debug("getVideoTrack()");
    
    return libvlc.libvlc_video_get_track(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @param track
   */
  public void setVideoTrack(int track) {
    if(LOG.isDebugEnabled()) {LOG.debug("setVideoTrack(track=" + track + ")");}
    
    libvlc.libvlc_video_set_track(mediaPlayerInstance, track);
  }
  
  /**
   * 
   * 
   * @return
   */
  public int getAudioTrackCount() {
    LOG.debug("getVideoTrackCount()");

    return libvlc.libvlc_audio_get_track_count(mediaPlayerInstance);
  }

  /**
   * 
   * 
   * @return
   */
  public int getAudioTrack() {
    LOG.debug("getAudioTrack()");
    
    return libvlc.libvlc_audio_get_track(mediaPlayerInstance);
  }
  
  /**
   * 
   * 
   * @param track
   */
  public void setAudioTrack(int track) {
    if(LOG.isDebugEnabled()) {LOG.debug("setAudioTrack(track=" + track + ")");}
    
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
    LOG.debug("play()");
    
    libvlc.libvlc_media_player_play(mediaPlayerInstance);
  }

  /**
   * Stop play-back.
   * <p>
   * A subsequent play will play-back from the start.
   */
  public void stop() {
    LOG.debug("stop()");
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("setPause(pause=" + pause + ")");}
    
    libvlc.libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
  }
  
  /**
   * Pause play-back.
   * <p>
   * If the play-back is currently paused it will begin playing.
   */
  public void pause() {
    LOG.debug("pause()");
    
    libvlc.libvlc_media_player_pause(mediaPlayerInstance);
  }

  /**
   * Advance one frame. 
   */
  public void nextFrame() {
    LOG.debug("nextFrame()");
    
    libvlc.libvlc_media_player_next_frame(mediaPlayerInstance);
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
    
    libvlc.libvlc_media_player_set_time(mediaPlayerInstance, time);
  }
  
  /**
   *  Jump to a specific position.
   * 
   * @param position, a percentage (e.g. 0.15 is 15%)
   */
  public void setPosition(float position) {
    if(LOG.isDebugEnabled()) {LOG.debug("setPosition(position=" + position + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("setRate(rate=" + rate + ")");}
    
    return libvlc.libvlc_media_player_set_rate(mediaPlayerInstance, rate);
  }
  
  /**
   * 
   * 
   * @param aspectRatio
   */
  public void setAspectRatio(String aspectRatio) {
    if(LOG.isDebugEnabled()) {LOG.debug("setAspectRatio(aspectRatio=" + aspectRatio + ")");}
    
    libvlc.libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
  }
  
  /**
   * 
   * 
   * @param factor
   */
  public void setScale(float factor) {
    if(LOG.isDebugEnabled()) {LOG.debug("setScale(factor=" + factor + ")");}
    
    libvlc.libvlc_video_set_scale(mediaPlayerInstance, factor);
  }
  
  // === Audio Controls =======================================================

  /**
   * Toggle volume mute.
   */
  public void mute() {
    LOG.debug("mute()");
    
    libvlc.libvlc_audio_toggle_mute(mediaPlayerInstance);
  }
  
  /**
   * Mute or un-mute the volume.
   * 
   * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
   */
  public void mute(boolean mute) {
    if(LOG.isDebugEnabled()) {LOG.debug("mute(mute=" + mute + ")");}
    
    libvlc.libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
  }
  
  /**
   * Test whether or not the volume is currently muted.
   * 
   * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
   */
  public boolean isMute() {
    LOG.debug("isMute()");
    
    return libvlc.libvlc_audio_get_mute(mediaPlayerInstance) != 0;
  }
  
  /**
   * Get the current volume.
   * 
   * @return volume, in the range 0 to 100 where 100 is full volume
   */
  public int getVolume() {
    LOG.debug("getVolume()");
    
    return libvlc.libvlc_audio_get_volume(mediaPlayerInstance);
  }
  
  /**
   * Set the volume.
   * 
   * @param volume volume, in the range 0 to 200 where 200 is full volume 
   */
  public void setVolume(int volume) {
    if(LOG.isDebugEnabled()) {LOG.debug("setVolume(volume=" + volume + ")");}
    
    libvlc.libvlc_audio_set_volume(mediaPlayerInstance, volume);
  }
  
  // === Chapter Controls =====================================================

  /**
   * Get the chapter count.
   * 
   * @return number of chapters, or -1 if no chapters
   */
  public int getChapterCount() {
    LOG.trace("getChapterCount()");
    
    return libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance);
  }
  
  /**
   * Get the current chapter.
   * 
   * @return chapter number, where zero is the first chatper, or -1 if no media
   */
  public int getChapter() {
    LOG.trace("getChapter()");
    
    return libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance);
  }
  
  /**
   * Set the chapter.
   * 
   * @param chapterNumber chapter number, where zero is the first chapter
   */
  public void setChapter(int chapterNumber) {
    if(LOG.isDebugEnabled()) {LOG.debug("setChapter(chapterNumber=" + chapterNumber + ")");}
    
    libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber);
  }
  
  /**
   * Jump to the next chapter.
   * <p>
   * If the play-back is already at the last chapter, this will have no effect.
   */
  public void nextChapter() {
    LOG.debug("nextChapter()");
    
    libvlc.libvlc_media_player_next_chapter(mediaPlayerInstance);
  }
  
  /**
   * Jump to the previous chapter.
   * <p>
   * If the play-back is already at the first chapter, this will have no effect.
   */
  public void previousChapter() {
    LOG.debug("previousChapter()");
    
    libvlc.libvlc_media_player_previous_chapter(mediaPlayerInstance);
  }
  
  // === Sub-Picture/Sub-Title Controls =======================================
  
  /**
   * Get the number of sub-pictures/sub-titles.
   *
   * @return number of sub-titles
   */
  public int getSpuCount() {
    LOG.debug("getSpuCount()");
    
    return libvlc.libvlc_video_get_spu_count(mediaPlayerInstance);
  }
  
  /**
   * Get the current sub-title track.
   * 
   * @return sub-title number, or -1 if none
   */
  public int getSpu() {
    LOG.debug("getSpu()");
    
    return libvlc.libvlc_video_get_spu(mediaPlayerInstance);
  }
  
  /**
   * Set the current sub-title track.
   * 
   * @param spu sub-title number, or -1 for none
   */
  public void setSpu(int spu) {
    if(LOG.isDebugEnabled()) {LOG.debug("setSpu(spu=" + spu + ")");}
    
    libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu);
  }

  // === Description Controls =================================================
  
  /**
   * Get the title descriptions. 
   * 
   * @return list of descriptions
   */
  public List<String> getTitleDescriptions() {
    if(LOG.isDebugEnabled()) {LOG.debug("getTitleDescriptions()");}
    
    List<String> trackDescriptions = new ArrayList<String>();
    libvlc_track_description_t trackDescription = libvlc.libvlc_video_get_title_description(mediaPlayerInstance);
    while(trackDescription != null) {
      trackDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
//    libvlc.libvlc_track_description_release(trackDescription); // Calling release here will cause a native crash on exit
    return trackDescriptions;
  }  
  
  /**
   * Get the video (i.e. "title") track descriptions.
   * 
   * @return list of descriptions
   */
  public List<String> getVideoDescriptions() {
    if(LOG.isDebugEnabled()) {LOG.debug("getVideoDescriptions()");}
    
    List<String> trackDescriptions = new ArrayList<String>();
    libvlc_track_description_t trackDescription = libvlc.libvlc_video_get_track_description(mediaPlayerInstance);
    while(trackDescription != null) {
      trackDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
//    libvlc.libvlc_track_description_release(trackDescription); // Calling release here will cause a native crash on exit
    return trackDescriptions;
  }
  
  /**
   * Get the audio track descriptions. 
   * 
   * @return list of descriptions
   */
  public List<String> getAudioDescriptions() {
    if(LOG.isDebugEnabled()) {LOG.debug("getAudioDescriptions()");}
    
    List<String> trackDescriptions = new ArrayList<String>();
    libvlc_track_description_t trackDescription = libvlc.libvlc_audio_get_track_description(mediaPlayerInstance);
    while(trackDescription != null) {
      trackDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
//    libvlc.libvlc_track_description_release(trackDescription); // Calling release here will cause a native crash on exit
    return trackDescriptions;
  }  
  
  /**
   * Get the sub-title track descriptions. 
   * 
   * @return list of descriptions
   */
  public List<String> getSpuDescriptions() {
    if(LOG.isDebugEnabled()) {LOG.debug("getSpuDescriptions()");}
    
    List<String> trackDescriptions = new ArrayList<String>();
    libvlc_track_description_t trackDescription = libvlc.libvlc_video_get_spu_description(mediaPlayerInstance);
    while(trackDescription != null) {
      trackDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
//    libvlc.libvlc_track_description_release(trackDescription); // Calling release here will cause a native crash on exit
    return trackDescriptions;
  }  

  /**
   * Get the chapter descriptions for a title.
   * 
   * @param title title number TODO is it number or index?
   * @return list of descriptions
   */
  public List<String> getChapterDescriptions(int title) {
    if(LOG.isDebugEnabled()) {LOG.debug("getChapterDescriptions(title=" + title + ")");}
    
    List<String> trackDescriptions = new ArrayList<String>();
    libvlc_track_description_t trackDescription = libvlc.libvlc_video_get_chapter_description(mediaPlayerInstance, title);
    while(trackDescription != null) {
      trackDescriptions.add(trackDescription.psz_name);
      trackDescription = trackDescription.p_next;      
    }   
//    libvlc.libvlc_track_description_release(trackDescription); // Calling release here will cause a native crash on exit
    return trackDescriptions;
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
    LOG.debug("getSnapshot()");
    try {
      File file = File.createTempFile("vlcj-snapshot-", ".png");
      if(LOG.isDebugEnabled()) {LOG.debug("file=" + file.getAbsolutePath());}
      saveSnapshot(file);
      BufferedImage snapshotImage = ImageIO.read(file);
      boolean deleted = file.delete();
      if(LOG.isDebugEnabled()) {LOG.debug("deleted=" + deleted);}
      return snapshotImage;
    }
    catch(IOException e) {
      throw new RuntimeException("Failed to get snapshot image", e);
    }
  }
  
  /**
   * Get the contents of the video surface component. 
   * <p>
   * This implementation uses the AWT Robot class to capture the contents of
   * the video surface component.
   * <p>
   * The size of the returned image will match the current size of the video
   * surface.
   * <p>
   * <strong>Since this implementation uses the AWT Robot class to make a
   * screen capture, care must be taken when invoking this method to ensure 
   * that nothing else is overlaying the video surface!</strong>
   * 
   * @return current contents of the video surface
   */
  public BufferedImage getVideoSurfaceContents() {
    LOG.debug("getVideoSurfaceContents()");
    try {
      Rectangle bounds = videoSurface.getBounds();
      bounds.setLocation(videoSurface.getLocationOnScreen());
      return new Robot().createScreenCapture(bounds);
    } 
    catch(Exception e) {
      throw new RuntimeException("Failed to get video surface contents", e);
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
    if(LOG.isDebugEnabled()) {LOG.debug("enableLogo(enable=" + enable + ")");}
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
  }

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity in the range 0 to 255 where 255 is fully opaque
   */
  public void setLogoOpacity(int opacity) {
    if(LOG.isDebugEnabled()) {LOG.debug("setLogoOpacity(opacity=" + opacity + ")");}
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
  }

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  public void setLogoOpacity(float opacity) {
    if(LOG.isDebugEnabled()) {LOG.debug("setLogoOpacity(opacity=" + opacity + ")");}
    
    int opacityValue = Math.round(opacity * 255.0f);
    if(LOG.isDebugEnabled()) {LOG.debug("opacityValue=" + opacityValue);}
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
  }
  
  /**
   * Set the logo location.
   * 
   * @param x x co-ordinate for the top left of the logo
   * @param y y co-ordinate for the top left of the logo
   */
  public void setLogoLocation(int x, int y) {
    if(LOG.isDebugEnabled()) {LOG.debug("setLogoLocation(x=" + x + ",y=" + y + ")");}
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
  }

  /**
   * Set the logo position.
   * 
   * @param position position
   */
  public void setLogoPosition(libvlc_logo_position_e position) {
    if(LOG.isDebugEnabled()) {LOG.debug("setLogoPosition(position=" + position + ")");}
    
    libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    
  }
  
  /**
   * Set the logo file.
   * 
   * @param logoFile logo file name
   */
  public void setLogoFile(String logoFile) {
    if(LOG.isDebugEnabled()) {LOG.debug("setLogoFile(logoFile=" + logoFile + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("enableMarquee(enable=" + enable + ")");}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
  }

  /**
   * Set the marquee text.
   * 
   * @param text text
   */
  public void setMarqueeText(String text) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeText(text=" + text + ")");}
    
    libvlc.libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
  }

  /**
   * Set the marquee colour.
   * 
   * @param colour colour, any alpha component will be masked off
   */
  public void setMarqueeColour(Color colour) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeColour(colour=" + colour + ")");}
    
    setMarqueeColour(colour.getRGB() & 0x00ffffff);
  }

  /**
   * Set the marquee colour.
   * 
   * @param colour RGB colour value
   */
  public void setMarqueeColour(int colour) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeColour(colour=" + colour + ")");}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
  }
  
  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity in the range 0 to 100 where 255 is fully opaque
   */
  public void setMarqueeOpacity(int opacity) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeOpacity(opacity=" + opacity + ")");}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
  }
  
  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  public void setMarqueeOpacity(float opacity) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeOpacity(opacity=" + opacity + ")");}
    
    int opacityValue = Math.round(opacity * 255.0f);
    if(LOG.isDebugEnabled()) {LOG.debug("opacityValue=" + opacityValue);}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
  }
  
  /**
   * Set the marquee size.
   * 
   * @param size size, height of the marquee text in pixels
   */
  public void setMarqueeSize(int size) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeSize(size=" + size + ")");}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
  }
  
  /**
   * Set the marquee timeout. 
   * 
   * @param timeout timeout, in milliseconds
   */
  public void setMarqueeTimeout(int timeout) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeTimeout(timeout=" + timeout + ")");}
    
    libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
  }
  
  /**
   * Set the marquee location.
   * 
   * @param x x co-ordinate for the top left of the marquee
   * @param y y co-ordinate for the top left of the marquee
   */
  public void setMarqueeLocation(int x, int y) {
    if(LOG.isDebugEnabled()) {LOG.debug("setMarqueeLocation(x=" + x + ",y=" + y + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("setDeinterlace(deinterlaceMode=" + deinterlaceMode + ")");}
    
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
   * @param true if the video adjustments are enabled, otherwise false 
   */
  public void setAdjustVideo(boolean adjustVideo) {
    if(LOG.isDebugEnabled()) {LOG.debug("setAdjustVideo(adjustVideo=" + adjustVideo + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("isAdjustVideo()");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("getContrast()");}
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
  }
  
  /**
   * Set the video contrast.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * 
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param contrast, in the range from 0.0 to 2.0
   */
  public void setContrast(float contrast) {
    if(LOG.isDebugEnabled()) {LOG.debug("setContrast(contrast=" + contrast + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("getBrightness()");}
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
  }
  
  /**
   * Set the video brightness.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param brightness, in the range from 0.0 to 2.0
   */
  public void setBrightness(float brightness) {
    if(LOG.isDebugEnabled()) {LOG.debug("setBrightness(brightness=" + brightness + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("getHue()");}
    
    return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
  }
  
  /**
   * Set the video hue.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param hue, in the range from 0 to 360
   */
  public void setHue(int hue) {
    if(LOG.isDebugEnabled()) {LOG.debug("setHue(hue=" + hue + ")");}
    
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
    if(LOG.isDebugEnabled()) {LOG.debug("getSaturation()");}
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
  }
  
  /**
   * Set the video saturation.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param saturation, in the range from 0.0 to 3.0
   */
  public void setSaturation(float saturation) {
    if(LOG.isDebugEnabled()) {LOG.debug("setSaturation(saturation=" + saturation + ")");}
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
  }
  
  /**
   * Get the current video gamma.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return gamma, in the range from 0.01 to 10.0
   */
  public float getGamma() {
    if(LOG.isDebugEnabled()) {LOG.debug("getGamma()");}
    
    return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
  }
  
  /**
   * Set the video gamma.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param gamma, in the range from 0.01 to 10.0
   */
  public void setGamma(float gamma) {
    if(LOG.isDebugEnabled()) {LOG.debug("setGamma(gamma=" + gamma + ")");}
    
    libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
  }
  
  // === User Interface =======================================================
  
  /**
   * Toggle whether the video display is in full-screen or not.
   * <p>
   * This method defers to the full-screen strategy implementation.
   */
  public void toggleFullScreen() {
    LOG.debug("toggleFullScreen()");
    
    if(fullScreenStrategy != null) {
      setFullScreen(!fullScreenStrategy.isFullScreenMode());
    }
  }

  /**
   * Set full-screen mode. 
   * <p>
   * This method defers to the full-screen strategy implementation.
   * 
   * @param fullScreen true for full-screen, otherwise false
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
   * Test the current full-screen mode.
   * <p>
   * This method defers to the full-screen strategy implementation.
   * 
   * @return true if full-screen is active, otherwise false
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
  
  /**
   * 
   * 
   * @param enable
   */
  public void setEnableMouseInputHandling(boolean enable) {
    if(LOG.isDebugEnabled()) {LOG.debug("setEnableMouseInputHandling(enable=" + enable + ")");}
    
    libvlc.libvlc_video_set_mouse_input(mediaPlayerInstance, enable ? 1 : 0);
  }
  
  /**
   * 
   * 
   * @param enable
   */
  public void setEnableKeyInputHandling(boolean enable) {
    if(LOG.isDebugEnabled()) {LOG.debug("setEnableKeyInputHandling(enable=" + enable + ")");}
    
    libvlc.libvlc_video_set_key_input(mediaPlayerInstance, enable ? 1 : 0);
  }
  
  // === Implementation =======================================================

  /**
   * Create and prepare the native media player resources.
   */
  private void createInstance() {
    LOG.debug("createInstance()");
    
    mediaPlayerInstance = libvlc.libvlc_media_player_new(instance);
    if(LOG.isDebugEnabled()) {LOG.debug("mediaPlayerInstance=" + mediaPlayerInstance);}
    
    mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance);
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

  /**
   * 
   */
  private void registerEventListener() {
    LOG.debug("registerEventListener()");
    
    callback = new VlcVideoPlayerCallback();

    for(libvlc_event_e event : libvlc_event_e.values()) {
      if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() < libvlc_event_e.libvlc_MediaListItemAdded.intValue()) {
        if(LOG.isDebugEnabled()) {LOG.debug("event=" + event);}
        int result = libvlc.libvlc_event_attach(mediaPlayerEventManager, event.intValue(), callback, null);
        if(LOG.isDebugEnabled()) {LOG.debug("result=" + result);}
        if(result == 0) {
        }
        else {
        }
      }
    }
  }

  /**
   * 
   */
  private void deregisterEventListener() {
    LOG.debug("deregisterEventListener()");
    
    if(callback != null) {
      for(libvlc_event_e event : libvlc_event_e.values()) {
        if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() < libvlc_event_e.libvlc_MediaListItemAdded.intValue()) {
          if(LOG.isDebugEnabled()) {LOG.debug("event=" + event);}
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
    if(LOG.isDebugEnabled()) {LOG.debug("setMedia(media=" + media + ",mediaOptions=" + Arrays.toString(mediaOptions) + ")");}
    
    libvlc_media_t mediaDescriptor = libvlc.libvlc_media_new_path(instance, media);
    if(LOG.isDebugEnabled()) {LOG.debug("mediaDescriptor=" + mediaDescriptor);}
    
    if(standardMediaOptions != null) {
      for(String standardMediaOption : standardMediaOptions) {
        if(LOG.isDebugEnabled()) {LOG.debug("standardMediaOption=" + standardMediaOption);}
        libvlc.libvlc_media_add_option(mediaDescriptor, standardMediaOption);
      }
    }
    
    if(mediaOptions != null) {
      for(String mediaOption : mediaOptions) {
        if(LOG.isDebugEnabled()) {LOG.debug("mediaOption=" + mediaOption);}
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

  private void notifyListeners(libvlc_event_t event) {
    if(LOG.isTraceEnabled()) {LOG.trace("notifyListeners(event=" + event + ")");}
    
    if(!eventListenerList.isEmpty()) {
      for(int i = eventListenerList.size() - 1; i >= 0; i--) {
        MediaPlayerEventListener listener = eventListenerList.get(i);
        int eventType = event.type;
        switch(libvlc_event_e.event(eventType)) {
          case libvlc_MediaDurationChanged:
            long newDuration = ((media_duration_changed)event.u.getTypedValue(media_duration_changed.class)).new_duration;
//            listener.durationChanged(this, newDuration);
            break;
        
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
            long newTime = ((media_player_time_changed)event.u.getTypedValue(media_player_time_changed.class)).new_time;
            listener.timeChanged(this, newTime);
            break;

          case libvlc_MediaPlayerPositionChanged:
            float newPosition = ((media_player_position_changed)event.u.getTypedValue(media_player_position_changed.class)).new_position;
            listener.positionChanged(this, newPosition);
            break;
            
          case libvlc_MediaPlayerLengthChanged:
            long newLength = ((media_player_length_changed)event.u.getTypedValue(media_player_length_changed.class)).new_length;
            listener.lengthChanged(this, newLength);
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

  private final class VlcVideoPlayerCallback implements libvlc_callback_t {

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
          LOG.trace("Waiting for video output...");
          
          Thread.sleep(VOUT_WAIT_PERIOD);

          LOG.trace("Checking for video output...");
          
          int videoOutputs = getVideoOutputs();
          if(LOG.isTraceEnabled()) {LOG.trace("videoOutputs=" + videoOutputs);}
          
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
  protected abstract void nativeSetVideoSurface(libvlc_media_player_t instance, Canvas videoSurface);
  
  /**
   * Allow sub-classes to use the native media player instance. 
   * 
   * @return media player instance
   */
  protected final libvlc_media_player_t mediaPlayerInstance() {
    return mediaPlayerInstance;
  }
}
