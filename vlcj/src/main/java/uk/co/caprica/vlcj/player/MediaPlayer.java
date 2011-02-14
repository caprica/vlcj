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
import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_channel_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;

/**
 * Specification for a media player component.
 * <p>
 * A media player provides the following functions:
 * <ul>
 *   <li>Status controls - e.g. length, time</li> 
 *   <li>Play-back controls - play, pause, stop, skip, back</li>
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
 *   // Create a media player instance (in this example an embedded media player)
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
 *   
 *   // Set standard options as needed to be applied to all subsequently played media items
 *   String[] standardMediaOptions = {"video-filter=logo", "logo-file=vlcj-logo.png", "logo-opacity=25"}; 
 *   mediaPlayer.setStandardMediaOptions(standardMediaOptions);
 *
 *   // Add a component to be notified of player events
 *   mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {...add implementation here...});
 *   
 *   // Create and set a new component to display the rendered video (not shown: add the Canvas to a Frame)
 *   Canvas canvas = new Canvas();
 *   CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
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
public interface MediaPlayer {

  /**
   * Add a component to be notified of media player events.
   * 
   * @param listener component to notify
   */
  void addMediaPlayerEventListener(MediaPlayerEventListener listener);

  /**
   * Remove a component that was previously interested in notifications of
   * media player events.
   * 
   * @param listener component to stop notifying
   */
  void removeMediaPlayerEventListener(MediaPlayerEventListener listener);

  /**
   * Restrict the set of media player events that generate event notifications
   * to listeners.
   * <p>
   * If a set of events is not explicitly enabled, then it is expected that 
   * <strong>all</strong> events be enabled.
   * <p>
   * See {@link MediaPlayerEventType}.
   * 
   * @param eventMask bit mask of events to enable
   */
  void enableEvents(int eventMask);
  
  /**
   * Set standard media options for all media items subsequently played.
   * <p>
   * This will <strong>not</strong> affect any currently playing media item.
   * 
   * @param mediaOptions options to apply to all subsequently played media items
   */
  void setStandardMediaOptions(String... mediaOptions);

  /**
   * Play a new media item, with options.
   * <p>
   * The new media will begin play-back immediately.
   * 
   * @param mrl media resource locator
   * @param mediaOptions zero or more media item options
   */
  void playMedia(String mrl, String... mediaOptions);

  /**
   * Prepare a new media item for play-back, but do not begin playing.
   * 
   * @param mrl media resource locator
   * @param mediaOptions zero or more media item options
   */
  void prepareMedia(String mrl, String... mediaOptions);

  /**
   * Play a new media item, with options, and wait for it to start playing or
   * error.
   * <p> 
   * This call will <strong>block</strong> until the media starts or errors.
   * 
   * @param mrl media resource locator
   * @param mediaOptions zero or more media item options
   * @return <code>true</code> if the media started playing, <code>false</code>
   *         if the media failed to start because of an error 
   */
  boolean playMediaAndWait(String mrl, String... mediaOptions);

  /**
   * Parse local meta data from the current media.
   * <p>
   * This method is synchronous.
   * <p>
   * <strong>Invoking this method on a stream may cause a hang.</strong>
   */
  void parseMedia();

  /**
   * Parse local meta data from the current media.
   * <p>
   * This method is asynchronous and a media player event will be raised when
   * the parsed status changes.
   * <p>
   * If the media has already been parsed when this function calls then <em>no</em>
   * event will be raised.
   * <p>
   * <strong>Invoking this method on a stream may cause a hang.</strong>
   */
  void requestParseMedia();

  /**
   * Add options to the current media. 
   * 
   * @param mediaOptions media options
   */
  void addMediaOptions(String... mediaOptions);

  /**
   * Set whether or not the media player should automatically play media sub-
   * items.
   * 
   * @param playSubItems <code>true</code> to automatically play sub-items, otherwise <code>false</code>
   */
  void setPlaySubItems(boolean playSubItems);

  /**
   * Get the number of sub-items (if any).
   * 
   * @return sub-item count, or -1 if there is no current media
   */
  int subItemCount();
  
  /**
   * Play the next sub-item (if there is one).
   * <p>
   * If any standard media options have been set via {@link #setStandardMediaOptions(String...)}
   * then those options will be applied to the sub-item.
   * 
   * @param mediaOptions zero or more media options for the sub-item
   * @return <code>true</code> if there is a sub-item, otherwise <code>false</code>
   */
  boolean playNextSubItem(String... mediaOptions);

  /**
   * 
   * 
   * @return
   */
  boolean isPlayable();

  /**
   * 
   * 
   * @return
   */
  boolean isPlaying();

  /**
   * 
   * 
   * @return
   */
  boolean isSeekable();

  /**
   * 
   * 
   * @return
   */
  boolean canPause();

  /**
   * Get the length of the current media item.
   * 
   * @return length, in milliseconds
   */
  long getLength();

  /**
   * Get the current play-back time.
   * 
   * @return current time, expressed as a number of milliseconds
   */
  long getTime();

  /**
   * Get the current play-back position.
   * 
   * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
   */
  float getPosition();

  /**
   * 
   * 
   * @return
   */
  float getFps();

  /**
   * Get the current video play rate.
   * 
   * @return rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
   */
  float getRate();

  /**
   * Get the number of video outputs for the media player.
   * 
   * @return number of video outputs, may be zero
   */
  int getVideoOutputs();

  /**
   * Get the video size.
   * <p>
   * The video dimensions are not available until after the video has started
   * playing. 
   * 
   * @return video size if available, or <code>null</code>
   */
  Dimension getVideoDimension();

  /**
   * 
   * 
   * @return
   */
  String getAspectRatio();

  /**
   * 
   * 
   * @return
   */
  float getScale();

  /**
   * 
   * 
   * @return
   */
  String getCropGeometry();

  /**
   * Get the current media statistics. 
   * <p>
   * Statistics are only updated if the video is playing.
   * 
   * @return media statistics
   */
  // FIXME For now I'll simply return the internal binding structure but I don't really want to do that do I?
  libvlc_media_stats_t getMediaStatistics();

  /**
   * Get the current media state.
   * 
   * @return state
   */
  libvlc_state_t getMediaState();

  /**
   * Get the media player current state.
   * 
   * @return state
   */
  libvlc_state_t getMediaPlayerState();

  /**
   * Get the number of titles.
   *
   * @return number of titles, or -1 if none
   */
  int getTitleCount();

  /**
   * Get the current title.
   * 
   * @return title number
   */
  int getTitle();

  /**
   * Set a new title to play.
   * 
   * @param title title number
   */
  void setTitle(int title);

  /**
   * Get the number of available video tracks.
   * 
   * @return number of tracks
   */
  int getVideoTrackCount();

  /**
   * Get the current video track.
   * <p>
   * This does not return the <strong>id</strong> of the track,
   * see {@link #getVideoDescriptions()}.
   * 
   * @return track number, starting at 1, or -1 if the video is currently disabled
   */
  int getVideoTrack();

  /**
   * Set a new video track to play.
   * <p>
   * This does not take the track number returned from {@link #getVideoTrack()},
   * rather it takes the track <strong>id</strong> obtained from
   * see {@link #getVideoDescriptions()}.
   * 
   * @param track track id, or -1 to disable the video
   */
  void setVideoTrack(int track);

  /**
   * Get the number of available audio tracks.
   * 
   * @return track count
   */
  int getAudioTrackCount();

  /**
   * Get the current audio track.
   * 
   * @return track number
   */
  int getAudioTrack();

  /**
   * Set a new audio track to play.
   * 
   * @param track track number
   */
  void setAudioTrack(int track);

  /**
   * Begin play-back.
   * <p>
   * If called when the play-back is paused, the play-back will resume from the
   * current position.
   */
  void play();

  /**
   * Begin play-back and wait for the media to start playing or for an error to
   * occur.
   * <p>
   * If called when the play-back is paused, the play-back will resume from the
   * current position.
   * <p>
   * This call will <strong>block</strong> until the media starts or errors.
   * 
   * @return <code>true</code> if the media started playing, <code>false</code>
   *         if the media failed to start because of an error 
   */
  boolean playAndWait();
  
  /**
   * Stop play-back.
   * <p>
   * A subsequent play will play-back from the start.
   */
  void stop();

  /**
   * Pause/resume.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param pause true to pause, false to play/resume
   */
  void setPause(boolean pause);

  /**
   * Pause play-back.
   * <p>
   * If the play-back is currently paused it will begin playing.
   */
  void pause();

  /**
   * Advance one frame. 
   */
  void nextFrame();

  /**
   * Skip forward or backward by a period of time.
   * <p>
   * To skip backwards specify a negative delta.
   * 
   * @param delta time period, in milliseconds
   */
  void skip(long delta);

  /**
   * Skip forward or backward by a change in position.
   * <p>
   * To skip backwards specify a negative delta.
   * 
   * @param delta
   */
  void skip(float delta);

  /**
   * Jump to a specific moment.
   * 
   * @param time time since the beginning, in milliseconds
   */
  void setTime(long time);

  /**
   * Jump to a specific position.
   * 
   * @param position position value, a percentage (e.g. 0.15 is 15%)
   */
  void setPosition(float position);

  /**
   * Set the video play rate.
   * <p>
   * Some media protocols are not able to change the rate.
   * 
   * @param rate rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
   * @return -1 on error, 0 on success
   */
  int setRate(float rate);

  /**
   * 
   * 
   * @param aspectRatio
   */
  void setAspectRatio(String aspectRatio);

  /**
   * 
   * 
   * @param factor
   */
  void setScale(float factor);

  /**
   * Set the crop geometry.
   * <p>
   * The format for the crop geometry is one of:
   * <ul>
   *   <li>numerator:denominator</li>
   *   <li>widthxheight+x+y</li>
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
  void setCropGeometry(String cropGeometry);

  /**
   * Set the desired audio output.
   * 
   * The change will not be applied until the media player has been stopped and
   * then played again.
   * 
   * @param outputName name of the desired audio output
   */
  void selectAudioOutput(String outputName);

  /**
   * Toggle volume mute.
   */
  void mute();

  /**
   * Mute or un-mute the volume.
   * 
   * @param mute <code>true</code> to mute the volume, <code>false</code> to un-mute it
   */
  void mute(boolean mute);

  /**
   * Test whether or not the volume is currently muted.
   * 
   * @return mute <code>true</code> if the volume is muted, <code>false</code> if the volume is not muted
   */
  boolean isMute();

  /**
   * Get the current volume.
   * 
   * @return volume, in the range 0 to 100 where 100 is full volume
   */
  int getVolume();

  /**
   * Set the volume.
   * 
   * @param volume volume, in the range 0 to 200 where 200 is full volume 
   */
  void setVolume(int volume);

  /**
   * Get the current audio channel.
   * 
   * For channel values see {@link libvlc_audio_output_channel_t}.
   * 
   * <strong>Warning this API is subject to change.</strong>
   * 
   * @return audio channel
   */
  int getAudioChannel();

  /**
   * Set the audio channel.
   * 
   * For channel values see {@link libvlc_audio_output_channel_t}.
   * 
   * <strong>Warning this API is subject to change.</strong>
   * 
   * @param channel channel
   */
  void setAudioChannel(int channel);

  /**
   * Get the audio delay.
   * 
   * @return audio delay, in microseconds
   */
  long getAudioDelay();

  /**
   * Set the audio delay.
   * <p>
   * The audio delay is set for the current item only and will be reset to zero
   * each time the media changes.
   * 
   * @param delay desired audio delay, in microseconds
   */
  void setAudioDelay(long delay);

  /**
   * Get the chapter count.
   * 
   * @return number of chapters, or -1 if no chapters
   */
  int getChapterCount();

  /**
   * Get the current chapter.
   * 
   * @return chapter number, where zero is the first chatper, or -1 if no media
   */
  int getChapter();

  /**
   * Set the chapter.
   * 
   * @param chapterNumber chapter number, where zero is the first chapter
   */
  void setChapter(int chapterNumber);

  /**
   * Jump to the next chapter.
   * <p>
   * If the play-back is already at the last chapter, this will have no effect.
   */
  void nextChapter();

  /**
   * Jump to the previous chapter.
   * <p>
   * If the play-back is already at the first chapter, this will have no effect.
   */
  void previousChapter();

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  void menuActivate();

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  void menuUp();

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  void menuDown();

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  void menuLeft();

  /**
   * 
   * 
   * <strong>Requires vlc 1.2.0 or later.</strong>
   */
  void menuRight();

  /**
   * Get the number of sub-pictures/sub-titles.
   *
   * @return number of sub-titles
   */
  int getSpuCount();

  /**
   * Get the current sub-title track.
   * 
   * @return sub-title number, or -1 if none
   */
  int getSpu();

  /**
   * Set the current sub-title track.
   * 
   * @param spu sub-title number, or -1 for none
   */
  void setSpu(int spu);

  /**
   * Select the next sub-title track (or disable sub-titles).
   */
  void cycleSpu();

  /**
   * Get the title descriptions. 
   * 
   * @return list of descriptions
   */
  List<TrackDescription> getTitleDescriptions();

  /**
   * Get the video (i.e. "title") track descriptions.
   * 
   * @return list of descriptions
   */
  List<TrackDescription> getVideoDescriptions();

  /**
   * Get the audio track descriptions. 
   * 
   * @return list of descriptions
   */
  List<TrackDescription> getAudioDescriptions();

  /**
   * Get the sub-title track descriptions. 
   * 
   * @return list of descriptions
   */
  List<TrackDescription> getSpuDescriptions();

  /**
   * Get the chapter descriptions for a title.
   * 
   * @param title title number
   * @return list of descriptions
   */
  List<String> getChapterDescriptions(int title);

  /**
   * Get the track (i.e. "elementary streams") information.
   * 
   * @return collection of track information, or <code>null</code> if there is no current media
   */
  List<TrackInfo> getTrackInfo();

  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * The snapshot will be created in the user's home directory and be assigned
   * a filename based on the current time.
   */
  void saveSnapshot();

  /**
   * Save a snapshot of the currently playing video.
   * <p>
   * Any missing directory path will be created if it does not exist.
   * 
   * @param file file to contain the snapshot
   */
  void saveSnapshot(File file);

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
  BufferedImage getSnapshot();

  /**
   * Enable/disable the logo.
   * <p>
   * The logo will not be enabled if there is currently no video being played.
   * 
   * @param enable <code>true</code> to show the logo, <code>false</code> to hide it
   */
  void enableLogo(boolean enable);

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity in the range 0 to 255 where 255 is fully opaque
   */
  void setLogoOpacity(int opacity);

  /**
   * Set the logo opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  void setLogoOpacity(float opacity);

  /**
   * Set the logo location.
   * 
   * @param x x co-ordinate for the top left of the logo
   * @param y y co-ordinate for the top left of the logo
   */
  void setLogoLocation(int x, int y);

  /**
   * Set the logo position.
   * 
   * @param position position
   */
  void setLogoPosition(libvlc_logo_position_e position);

  /**
   * Set the logo file.
   * 
   * @param logoFile logo file name
   */
  void setLogoFile(String logoFile);

  /**
   * Enable/disable the marquee.
   * <p>
   * The marquee will not be enabled if there is currently no video being played.
   * 
   * @param enable <code>true</code> to show the marquee, <code>false</code> to hide it
   */
  void enableMarquee(boolean enable);

  /**
   * Set the marquee text.
   * 
   * @param text text
   */
  void setMarqueeText(String text);

  /**
   * Set the marquee colour.
   * 
   * @param colour colour, any alpha component will be masked off
   */
  void setMarqueeColour(Color colour);

  /**
   * Set the marquee colour.
   * 
   * @param colour RGB colour value
   */
  void setMarqueeColour(int colour);

  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity in the range 0 to 100 where 255 is fully opaque
   */
  void setMarqueeOpacity(int opacity);

  /**
   * Set the marquee opacity.
   * 
   * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
   */
  void setMarqueeOpacity(float opacity);

  /**
   * Set the marquee size.
   * 
   * @param size size, height of the marquee text in pixels
   */
  void setMarqueeSize(int size);

  /**
   * Set the marquee timeout. 
   * 
   * @param timeout timeout, in milliseconds
   */
  void setMarqueeTimeout(int timeout);

  /**
   * Set the marquee location.
   * 
   * @param x x co-ordinate for the top left of the marquee
   * @param y y co-ordinate for the top left of the marquee
   */
  void setMarqueeLocation(int x, int y);

  /**
   * Set the de-interlace filter to use.
   * 
   * @param deinterlaceMode mode, or null to disable the de-interlace filter 
   */
  void setDeinterlace(DeinterlaceMode deinterlaceMode);

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
  void setAdjustVideo(boolean adjustVideo);

  /**
   * Test whether or not the video adjustments are enabled.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return true if the video adjustments are enabled, otherwise false
   */
  boolean isAdjustVideo();

  /**
   * Get the current video contrast.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return contrast, in the range from 0.0 to 2.0
   */
  float getContrast();

  /**
   * Set the video contrast.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * 
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param contrast contrast value, in the range from 0.0 to 2.0
   */
  void setContrast(float contrast);

  /**
   * Get the current video brightness.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return brightness, in the range from 0.0 to 2.0
   */
  float getBrightness();

  /**
   * Set the video brightness.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param brightness brightness value, in the range from 0.0 to 2.0
   */
  void setBrightness(float brightness);

  /**
   * Get the current video hue.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return hue, in the range from 0 to 360
   */
  int getHue();

  /**
   * Set the video hue.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param hue hue value, in the range from 0 to 360
   */
  void setHue(int hue);

  /**
   * Get the current video saturation.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return saturation, in the range from 0.0 to 3.0
   */
  float getSaturation();

  /**
   * Set the video saturation.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param saturation saturation value, in the range from 0.0 to 3.0
   */
  void setSaturation(float saturation);

  /**
   * Get the current video gamma.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @return gamma value, in the range from 0.01 to 10.0
   */
  float getGamma();

  /**
   * Set the video gamma.
   * <p>
   * Video adjustments must be enabled for this to have any effect.
   * <p>
   * <strong>Requires vlc 1.1.1 or later.</strong>
   * 
   * @param gamma gamma, in the range from 0.01 to 10.0
   */
  void setGamma(float gamma);

  /**
   * Get the user data associated with the media player. 
   * 
   * @return user data
   */
  Object userData();
  
  /**
   * Set user data to associate with the media player.
   * 
   * @param userData user data
   */
  void userData(Object userData);

  /**
   * Release the media player, freeing all associated (including native) resources.
   */
  void release();

  /**
   * Provide access to the native media player instance. 
   * <p>
   * This is exposed on the interface as an implementation side-effect, ordinary
   * applications are not expected to use this.
   * 
   * @return media player instance
   */
  libvlc_media_player_t mediaPlayerInstance();
}