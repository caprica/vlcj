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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_channel_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.medialist.MediaList;
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
 *
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
 *   String[] mediaOptions = {...add options here...};
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
 *
 * With regard to overlaying logos there are three approaches.
 * <p>
 * The first way is to specify standard options for the media player - this will set the logo for
 * any subsequently played media item, for example:
 *
 * <pre>
 * String[] standardMediaOptions = {&quot;video-filter=logo&quot;, &quot;logo-file=vlcj-logo.png&quot;, &quot;logo-opacity=25&quot;};
 * mediaPlayer.setStandardMediaOptions(standardMediaOptions);
 * </pre>
 *
 * The second way is to specify options when playing the media item, for example:
 *
 * <pre>
 * String[] mediaOptions = {&quot;video-filter=logo&quot;, &quot;logo-file=vlcj-logo.png&quot;, &quot;logo-opacity=25&quot;};
 * mediaPlayer.playMedia(mediaPath, mediaOptions);
 * </pre>
 *
 * The final way is to use the methods of this class to set various logo properties, for example:
 *
 * <pre>
 * mediaPlayer.setLogoFile(&quot;vlcj-logo.png&quot;);
 * mediaPlayer.setLogoOpacity(25);
 * mediaPlayer.setLogoLocation(10, 10);
 * mediaPlayer.enableLogo(true);
 * </pre>
 *
 * For this latter method, it is not possible to enable the logo until after the video has started
 * playing. There is also a noticeable stutter in video play-back when enabling the logo filter in
 * this way.
 * <p>
 * With regard to overlaying marquees, again there are three approaches (similar to those for
 * logos).
 * <p>
 * In this instance only the final way showing the usage of the API is used, for example:
 *
 * <pre>
 * mediaPlayer.setMarqueeText(&quot;VLCJ is quite good&quot;);
 * mediaPlayer.setMarqueeSize(60);
 * mediaPlayer.setMarqueeOpacity(70);
 * mediaPlayer.setMarqueeColour(Color.green);
 * mediaPlayer.setMarqueeTimeout(3000);
 * mediaPlayer.setMarqueeLocation(300, 400);
 * mediaPlayer.enableMarquee(true);
 * </pre>
 *
 * With regard to video adjustment controls, after the video has started playing:
 *
 * <pre>
 * mediaPlayer.setAdjustVideo(true);
 * mediaPlayer.setGamma(0.9f);
 * mediaPlayer.setHue(10);
 * </pre>
 *
 * Some media when played may cause one or more media sub-items to created. These sub-items
 * subsequently need to be played. The media player can be set to automatically play these sub-items
 * via {@link #setPlaySubItems(boolean)}, otherwise {@link #playNextSubItem(String...)} can be
 * invoked in response to a {@link MediaPlayerEventListener#finished(MediaPlayer)} event.
 *
 * @see EmbeddedMediaPlayerComponent
 */
public interface MediaPlayer {

    /**
     * Add a component to be notified of media player events.
     *
     * @param listener component to notify
     */
    void addMediaPlayerEventListener(MediaPlayerEventListener listener);

    /**
     * Remove a component that was previously interested in notifications of media player events.
     *
     * @param listener component to stop notifying
     */
    void removeMediaPlayerEventListener(MediaPlayerEventListener listener);

    /**
     * Restrict the set of media player events that generate event notifications to listeners.
     * <p>
     * If a set of events is not explicitly enabled, then it is expected that <strong>all</strong>
     * events be enabled.
     * <p>
     * See {@link MediaPlayerEventType}.
     * <p>
     * This setting applies to <em>all</em> registered event listeners - it is not (currently)
     * possible to set a different event mask for each listener.
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
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    boolean playMedia(String mrl, String... mediaOptions);

    /**
     * Prepare a new media item for play-back, but do not begin playing.
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    boolean prepareMedia(String mrl, String... mediaOptions);

    /**
     * Play a new media item, with options, and wait for it to start playing or error.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media started playing, <code>false</code> if the media failed to start because of an error
     */
    boolean startMedia(String mrl, String... mediaOptions);

    /**
     * Parse local meta data from the current media.
     * <p>
     * This method is synchronous.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover- art.
     * <p>
     * <strong>Invoking this method on a stream or DVB channel may cause a hang.</strong>
     */
    void parseMedia();

    /**
     * Parse local meta data from the current media.
     * <p>
     * This method is asynchronous and a media player event will be raised when the parsed status
     * changes.
     * <p>
     * Parsing media may cause an HTTP request to be made to search for cover- art.
     * <p>
     * If the media has already been parsed when this function calls then <em>no</em> event will be
     * raised.
     * <p>
     * <strong>Invoking this method on a stream or DVB channel may cause a hang.</strong>
     */
    void requestParseMedia();

    /**
     * Test whether or not the current media has been parsed.
     *
     * @return <code>true</code> if the current media has been parsed, otherwise <code>false</code>
     */
    boolean isMediaParsed();

    /**
     * Get local meta data for the current media.
     * <p>
     * Some media types require that the media be parsed before accessing meta data - it is the
     * responsibility of the client application to parse the media if required, see
     * {@link #parseMedia()}.
     * <p>
     * Note that requesting meta data may cause one or more HTTP connections to
     * be made to external web-sites to attempt download of album art.
     *
     * @return meta data
     */
    MediaMeta getMediaMeta();

    /**
     * Get local meta data for a media instance.
     * <p>
     * See {@link #getMediaMeta()}, the same notes with regard to parsing hold here.
     *
     * @param mediaInstance media instance, may be a sub-item
     * @return meta data
     */
    MediaMeta getMediaMeta(libvlc_media_t mediaInstance);

    /**
     * Get local meta data for all of the current media sub-items (if there are any).
     * <p>
     * See {@link #getMediaMeta()}, the same notes with regard to parsing hold here.
     *
     * @return collection of meta data for the media sub-items
     */
    List<MediaMeta> getSubItemMediaMeta();

    /**
     * Add options to the current media.
     *
     * @param mediaOptions media options
     */
    void addMediaOptions(String... mediaOptions);

    /**
     * Set whether or not the media player should automatically repeat playing the media when it has
     * finished playing.
     * <p>
     * There is <em>no</em> guarantee of seamless play-back when using this method - see instead
     * {@link uk.co.caprica.vlcj.player.list.MediaListPlayer MediaListPlayer}.
     * <p>
     * If the media has sub-items, then it is the sub-items that are repeated.
     *
     * @param repeat <code>true</code> to automatically replay the media, otherwise <code>false</code>
     */
    void setRepeat(boolean repeat);

    /**
     * Test whether or not the media player will automatically repeat playing the media when it has
     * finished playing.
     *
     * @return <code>true</code> if the media will be automatically replayed, otherwise <code>false</code>
     */
    boolean getRepeat();

    /**
     * Set whether or not the media player should automatically play media sub- items.
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
     * Get the index of the current sub-item.
     *
     * @return sub-item index, or -1 if no sub-items or no current sub-item
     */
    int subItemIndex();

    /**
     * Get the list of sub-items (if any).
     * <p>
     * The MRL of each sub-item is returned in the list.
     *
     * @return sub-item list, or <code>null</code> if there is no current media
     */
    List<String> subItems();

    /**
     * Get the list of sub-item media instances (if any).
     * <p>
     * The native media instance of each sub-item is returned in the list.
     *
     * @return sub-item list, or <code>null</code> if there is no current media
     */
    List<libvlc_media_t> subItemsMedia();

    /**
     * Get the sub-items as a {@link MediaList}.
     *
     * @return sub-item media list, or <code>null</code> if there is no current media
     */
    MediaList subItemsMediaList();

    /**
     * Play the next sub-item (if there is one).
     * <p>
     * If any standard media options have been set via {@link #setStandardMediaOptions(String...)}
     * then those options will be applied to the sub-item.
     * <p>
     * If the media player has been set to automatically repeat, then the sub- items will be
     * repeated once the last one has been played.
     *
     * @param mediaOptions zero or more media options for the sub-item
     * @return <code>true</code> if there is a sub-item, otherwise <code>false</code>
     */
    boolean playNextSubItem(String... mediaOptions);

    /**
     * Play a particular sub-item (if there is one).
     * <p>
     * If any standard media options have been set via {@link #setStandardMediaOptions(String...)}
     * then those options will be applied to the sub-item.
     * <p>
     * If the media player has been set to automatically repeat, then the sub- items will be
     * repeated once the last one has been played, or if the requested sub-item index exceeds the
     * currently available sub-items.
     * <p>
     *
     * @param index sub-item index
     * @param mediaOptions zero or more media options for the sub-item
     * @return <code>true</code> if there is a sub-item, otherwise <code>false</code>
     */
    boolean playSubItem(int index, String... mediaOptions);

    /**
     * Is the current media playable?
     *
     * @return <code>true</code> if the current media is playable, otherwise <code>false</code>
     */
    boolean isPlayable();

    /**
     * Is the media player playing?
     *
     * @return <code>true</code> if the media player is playing, otherwise <code>false</code>
     */
    boolean isPlaying();

    /**
     * Is the current media seekable?
     *
     * @return <code>true</code> if the current media is seekable, otherwise <code>false</code>
     */
    boolean isSeekable();

    /**
     * Can the current media be paused?
     *
     * @return <code>true</code> if the current media can be paused, otherwise <code>false</code>
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
     * Get the current play-back frames-per-second.
     *
     * @return number of frames-per-second
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
     * The video dimensions are not available until after the video has started playing and a video
     * output has been created.
     *
     * @return video size if available, or <code>null</code>
     */
    Dimension getVideoDimension();

    /**
     * Get the media details.
     * <p>
     * The details are available after the video has started playing, regardless of whether nor not
     * a video output has been created.
     *
     * @return video meta data, or <code>null</code> if the media meta data is not available
     */
    MediaDetails getMediaDetails();

    /**
     * Get the video aspect ratio.
     *
     * @return aspect ratio
     */
    String getAspectRatio();

    /**
     * Get the current video scale (zoom).
     *
     * @return scale
     */
    float getScale();

    /**
     * Get the current video crop geometry.
     *
     * @return crop geometry
     */
    String getCropGeometry();

    /**
     * Get the current media statistics.
     * <p>
     * Statistics are only updated if the video is playing.
     *
     * @return media statistics
     */
    // FIXME For now I'll simply return the internal binding structure but I don't really want to do
    // that do I?
    libvlc_media_stats_t getMediaStatistics();

    /**
     * Get the current media statistics for a media item (e.g. a sub-item).
     * <p>
     * Statistics are only updated if the video is playing.
     *
     * @param media media item
     * @return media statistics
     */
    // FIXME For now I'll simply return the internal binding structure but I don't really want to do
    // that do I?
    libvlc_media_stats_t getMediaStatistics(libvlc_media_t media);

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
     *
     * @return track identifier, see {@link #getVideoDescriptions()}
     */
    int getVideoTrack();

    /**
     * Set a new video track to play.
     * <p>
     * The track identifier must be one of those returned by {@link #getVideoDescriptions()}.
     * <p>
     * Video can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available track identifiers go in sequence from zero up to
     * {@link #getVideoTrackCount()}-1. The {@link #getVideoDescriptions()} method should always
     * be used to ascertain the available track identifiers.
     *
     * @param track track identifier
     * @return current video track identifier
     */
    int setVideoTrack(int track);

    /**
     * Get the number of available audio tracks.
     *
     * @return track count
     */
    int getAudioTrackCount();

    /**
     * Get the current audio track.
     *
     * @return track identifier, see {@link #getAudioDescriptions()}
     */
    int getAudioTrack();

    /**
     * Set a new audio track to play.
     * <p>
     * The track identifier must be one of those returned by {@link #getAudioDescriptions()}.
     * <p>
     * Audio can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available track identifiers go in sequence from zero up to
     * {@link #getAudioTrackCount()}-1. The {@link #getAudioDescriptions()} method should always
     * be used to ascertain the available track identifiers.
     * <p>
     * The implementation of the corresponding <em>native</em> method in libvlc is bugged before
     * vlc 2.0.5, therefore vlc 2.0.5 or later is required for correct behaviour when using this
     * method.
     *
     * @param track track identifier
     * @return current audio track identifier
     */
    int setAudioTrack(int track);

    /**
     * Begin play-back.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     */
    void play();

    /**
     * Begin play-back and wait for the media to start playing or for an error to occur.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @return <code>true</code> if the media started playing, <code>false</code> if the media failed to start because of an error
     */
    boolean start();

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
    void skipPosition(float delta);

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
     * Set the video aspect ratio
     *
     * @param aspectRatio aspect ratio
     */
    void setAspectRatio(String aspectRatio);

    /**
     * Set the video scaling factor.
     *
     * @param factor scaling factor, or zero to scale the video the size of the container
     */
    void setScale(float factor);

    /**
     * Set the crop geometry.
     * <p>
     * The format for the crop geometry is one of:
     * <ul>
     * <li>numerator:denominator</li>
     * <li>widthxheight+x+y</li>
     * <li>left:top:right:bottom</li>
     * </ul>
     * For example:
     *
     * <pre>
     * mediaPlayer.setCropGeometry(&quot;4:3&quot;);         // W:H
     * mediaPlayer.setCropGeometry(&quot;719x575+0+0&quot;); // WxH+L+T
     * mediaPlayer.setCropGeometry(&quot;6+10+6+10&quot;);   // L+T+R+B
     * </pre>
     *
     * @param cropGeometry formatted string describing the desired crop geometry
     */
    void setCropGeometry(String cropGeometry);

    /**
     * Set the desired audio output.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The output name comes from {@link MediaPlayerFactory#getAudioOutputs()}.
     *
     * @param output name of the desired audio output
     * @return <code>true</code> if the output was successfully set, otherwise <code>false</code>
     */
    boolean setAudioOutput(String output);

    /**
     * Set the desired audio output device.
     * <p>
     * The change will not be applied until the media player has been stopped and then played again.
     * <p>
     * The output name comes from {@link MediaPlayerFactory#getAudioOutputs()}.
     * <p>
     * The device id comes from the {@link AudioDevice#getDeviceId()} returned by
     * {@link MediaPlayerFactory#getAudioOutputs()}.
     *
     * @param output name of the desired audio output
     * @param outputDeviceId id of the desired audio output device
     */
    void setAudioOutputDevice(String output, String outputDeviceId);

    /**
     * Set the audio output device type.
     *
     * @param deviceType device type
     */
    void setAudioOutputDeviceType(AudioOutputDeviceType deviceType);

    /**
     * Get the audio output device type.
     *
     * @return device type
     */
    AudioOutputDeviceType getAudioOutputDeviceType();

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
     * @return volume, a percentage of full volume in the range 0 to 200
     */
    int getVolume();

    /**
     * Set the volume.
     * <p>
     * The volume is actually a percentage of full volume, setting a volume over
     * 100 may cause audible distortion.
     *
     * @param volume volume, a percentage of full volume in the range 0 to 200
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
     * The audio delay is set for the current item only and will be reset to zero each time the
     * media changes.
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
     * Activate a DVD menu.
     *
     * <strong>Requires vlc 2.0.0 or later.</strong>
     */
    void menuActivate();

    /**
     * Navigate up a DVD menu.
     *
     * <strong>Requires vlc 2.0.0 or later.</strong>
     */
    void menuUp();

    /**
     * Navigate down a DVD menu.
     *
     * <strong>Requires vlc 2.0.0 or later.</strong>
     */
    void menuDown();

    /**
     * Navigate left a DVD menu.
     *
     * <strong>Requires vlc 2.0.0 or later.</strong>
     */
    void menuLeft();

    /**
     * Navigate right a DVD menu.
     *
     * <strong>Requires vlc 2.0.0 or later.</strong>
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
     * <p>
     * The track identifier must be one of those returned by {@link #getSpuDescriptions()}.
     * <p>
     * Subtitles can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available subtitle identifiers go in sequence from zero up to
     * {@link #getSpuCount()}-1. The {@link #getSpuDescriptions()} method should always
     * be used to ascertain the available subtitle identifiers.
     * <p>
     * The implementation of the corresponding <em>native</em> method in libvlc is bugged before
     * vlc 2.0.6, therefore vlc 2.0.6 or later is required for correct behaviour when using this
     * method.
     *
     * @param spu sub-title identifier, or -1 for none
     * @return current sub-title identifier
     */
    int setSpu(int spu);

    /**
     * Select the next sub-title track (or disable sub-titles).
     *
     * @return current sub-title identifier
     */
    int cycleSpu();

    /**
     * Get the sub-title delay.
     *
     * @return sub-title delay, in microseconds
     */
    long getSpuDelay();

    /**
     * Set the sub-title delay.
     * <p>
     * The sub-title delay is set for the current item only and will be reset to zero each time the
     * media changes.
     *
     * @param delay desired sub-title delay, in microseconds
     */
    void setSpuDelay(long delay);

    /**
     * Set the sub-title file to use.
     *
     * @param subTitleFileName name of the file containing the sub-titles
     */
    void setSubTitleFile(String subTitleFileName);

    /**
     * Set the sub-title file to use.
     *
     * @param subTitleFile file containing the sub-titles
     */
    void setSubTitleFile(File subTitleFile);

    /**
     * Get the current teletext page.
     *
     * @return page number
     */
    int getTeletextPage();

    /**
     * Set the new teletext page to retrieve.
     *
     * @param pageNumber page number
     */
    void setTeletextPage(int pageNumber);

    /**
     * Toggle teletext status.
     */
    void toggleTeletext();

    /**
     * Get the title descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions
     */
    List<TrackDescription> getTitleDescriptions();

    /**
     * Get the video (i.e. "title") track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions
     */
    List<TrackDescription> getVideoDescriptions();

    /**
     * Get the audio track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions
     */
    List<TrackDescription> getAudioDescriptions();

    /**
     * Get the sub-title track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions
     */
    List<TrackDescription> getSpuDescriptions();

    /**
     * Get the chapter descriptions for a title.
     * <p>
     * The media must be playing before this information is available.
     *
     * @param title title number
     * @return list of descriptions, or <code>null</code> if there is no such title
     */
    List<String> getChapterDescriptions(int title);

    /**
     * Get the chapter descriptions for the current title.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions
     */
    List<String> getChapterDescriptions();

    /**
     * Get all of the chapter descriptions for all available titles.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return a collection of chapter description lists, one list for each title
     */
    List<List<String>> getAllChapterDescriptions();

    /**
     * Get the track (i.e. "elementary streams") information for the current media.
     * <p>
     * The media (if local) should first be parsed, see {@link #parseMedia()}, or be already
     * playing.
     * <p>
     * In the case of DVD media (for example ".iso" files) and streams the media must be played and
     * video output must be available before valid track information becomes available, and even
     * then it is not always available immediately (or it is only partially available) so polling
     * may be required.
     * <p>
     * If you invoke this method "too soon", you may only receive partial track information.
     *
     * @return collection of track information, or <code>null</code> if there is no current media
     */
    List<TrackInfo> getTrackInfo();

    /**
     * Get track (i.e. "elementary streams") information for a media item.
     * <p>
     * See {@link #getTrackInfo()}.
     *
     * @param media media item
     * @return collection of track information, or <code>null</code> if there is no current media
     */
    List<TrackInfo> getTrackInfo(libvlc_media_t media);

    /**
     * Get the track (i.e. "elementary streams") information for all sub-items if there are any.
     * <p>
     * See {@link #getTrackInfo()}.
     *
     * @return collection of track information for each sub-item, or <code>null</code> if there is no current media
     */
    List<List<TrackInfo>> getSubItemTrackInfo();

    /**
     * Set the directory into which snapshots of the video are saved.
     * <p>
     * If the specified directory path does not yet exist, it will be created.
     *
     * @param snapshotDirectoryName name of the directory to save snapshots to
     */
    void setSnapshotDirectory(String snapshotDirectoryName);

    /**
     * Save a snapshot of the currently playing video.
     * <p>
     * The size of the image will be that produced by the libvlc native snapshot function, i.e. the
     * size of the media itself.
     * <p>
     * The snapshot will be created in the directory set via {@link #setSnapshotDirectory(String)},
     * unless that directory has not been set in which case the snapshot will be created in the
     * user's home directory, obtained via the "user.home" system property.
     * <p>
     * The snapshot will be assigned a filename based on the current time.
     * <p>
     * The size of the image will be that produced by the libvlc native snapshot function.
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @return <code>true</code> if the snapshot was saved, otherwise <code>false</code>
     */
    boolean saveSnapshot();

    /**
     * Save a snapshot of the currently playing video.
     * <p>
     * The snapshot will be created in the directory set via {@link #setSnapshotDirectory(String)},
     * unless that directory has not been set in which case the snapshot will be created in the
     * user's home directory, obtained via the "user.home" system property.
     * <p>
     * The snapshot will be assigned a filename based on the current time.
     * <p>
     * If one of width or height is zero the original image aspect ratio will be preserved.
     * <p>
     * If both width and height are zero, the original image size will be used, see
     * {@link #saveSnapshot()}.
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @param width desired image width
     * @param height desired image height
     * @return <code>true</code> if the snapshot was saved, otherwise <code>false</code>
     */
    boolean saveSnapshot(int width, int height);

    /**
     * Save a snapshot of the currently playing video.
     * <p>
     * The size of the image will be that produced by the libvlc native snapshot function, i.e. the
     * size of the media itself.
     * <p>
     * Any missing directory path will be created if it does not exist.
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @param file file to contain the snapshot
     * @return <code>true</code> if the snapshot was saved, otherwise <code>false</code>
     */
    boolean saveSnapshot(File file);

    /**
     * Save a snapshot of the currently playing video.
     * <p>
     * Any missing directory path will be created if it does not exist.
     * <p>
     * If one of width or height is zero the original image aspect ratio will be preserved.
     * <p>
     * If both width and height are zero, the original image size will be used, see
     * {@link #saveSnapshot(File)}.
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @param file file to contain the snapshot
     * @param width desired image width
     * @param height desired image height
     * @return <code>true</code> if the snapshot was saved, otherwise <code>false</code>
     */
    boolean saveSnapshot(File file, int width, int height);

    /**
     * Get a snapshot of the currently playing video.
     * <p>
     * The size of the image will be that produced by the libvlc native snapshot function, i.e. the
     * size of the media itself.
     * <p>
     * This implementation uses the native libvlc method to save a snapshot of the currently playing
     * video. This snapshot is saved to a temporary file and then the resultant image is loaded from
     * the file.
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @return snapshot image, or <code>null</code> if a snapshot could not be taken
     */
    BufferedImage getSnapshot();

    /**
     * Get a snapshot of the currently playing video.
     * <p>
     * This implementation uses the native libvlc method to save a snapshot of the currently playing
     * video. This snapshot is saved to a temporary file and then the resultant image is loaded from
     * the file.
     * <p>
     * If one of width or height is zero the original image aspect ratio will be preserved.
     * <p>
     * If both width and height are zero, the original image size will be used, see
     * {@link #getSnapshot()}
     * <p>
     * Taking a snapshot is an asynchronous function, the snapshot is not available until
     * after the {@link MediaPlayerEventListener#snapshotTaken(MediaPlayer, String)} event
     * is received.
     *
     * @param width desired image width
     * @param height desired image height
     * @return snapshot image, or <code>null</code> if a snapshot could not be taken
     */
    BufferedImage getSnapshot(int width, int height);

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
     * Set the logo image.
     * <p>
     * The image will first be written to a temporary file, before invoking
     * {@link #setLogoFile(String)}. This is not optimal, but creating a temporary file for the logo
     * in this way is unavoidable.
     *
     * @param logoImage logo image
     */
    void setLogoImage(RenderedImage logoImage);

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
     * <p>
     * Format variables are available:
     * <pre>
     * Time related:
     *  %Y = year
     *  %d = day
     *  %H = hour
     *  %M = minute
     *  %S = second
     * </pre>
     * Meta data related:
     * <pre>
     *  $a = artist
     *  $b = album
     *  $c = copyright
     *  $d = description
     *  $e = encoded by
     *  $g = genre
     *  $l = language
     *  $n = track num
     *  $p = now playing
     *  $r = rating
     *  $s = subtitles language
     *  $t = title
     *  $u = url
     *  $A = date
     *  $B = audio bitrate (in kb/s)
     *  $C = chapter
     *  $D = duration
     *  $F = full name with path
     *  $I = title
     *  $L = time left
     *  $N = name
     *  $O = audio language
     *  $P = position (in %)
     *  $R = rate
     *  $S = audio sample rate (in kHz)
     *  $T = time
     *  $U = publisher
     *  $V = volume
     *  $_ = new line
     * </pre>
     * See <code>http://wiki.videolan.org/index.php?title=Documentation:Modules/marq</code>.
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
     * Set the marquee position.
     *
     * @param position position
     */
    void setMarqueePosition(libvlc_marquee_position_e position);

    /**
     * Set the de-interlace filter to use.
     *
     * @param deinterlaceMode mode, or null to disable the de-interlace filter
     */
    void setDeinterlace(DeinterlaceMode deinterlaceMode);

    /**
     * Enable/disable the video adjustments.
     * <p>
     * The video adjustment controls must be enabled after the video has started playing.
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
     * Get the media resource locator for the current media instance.
     * <p>
     * The native media instance may be an automatically/scripted added sub-item.
     *
     * @return URL-encoded media resource locator, or <code>null</code> if there is no current media
     */
    String mrl();

    /**
     * Get the media resource locator for a media instance.
     * <p>
     * The native media instance may be an automatically/scripted added sub-item.
     *
     * @param mediaInstance native media instance
     * @return URL-encoded media resource locator
     */
    String mrl(libvlc_media_t mediaInstance);

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
     * This is exposed on the interface as an implementation side-effect, ordinary applications are
     * not expected to use this.
     *
     * @return media player instance
     */
    libvlc_media_player_t mediaPlayerInstance();
}
