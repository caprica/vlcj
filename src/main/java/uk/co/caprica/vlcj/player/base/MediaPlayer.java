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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

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
 *   FullScreenStrategy fullScreenStrategy = new ExclusiveModeFullScreenStrategy(mainFrame);
 *
 *   // Create a media player instance (in this example an embedded media player)
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
 *
 *   // Set standard options as needed to be applied to all subsequently played media items
 *   String[] standardMediaOptions = {"video-filter=logo", "logo-file=vlcj-logo.png", "logo-opacity=25"};
 *   mediaPlayer.setStandardMediaOptions(standardMediaOptions);
 *
 *   // Add a component to be notified of player events
 *   mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {...add implementation here...});
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
 * <p>
 * When using options, generally any options that enable/disable modules (e.g. video/audio filters) must be set via the
 * factory instance and not when invoking {@link MediaPlayer#playMedia(String, String...)}. However, the filter-specific
 * options <em>may</em> be able to be passed and be effective via a playMedia call.
 * <p>
 * It is always a better strategy to reuse media player instances, rather than repeatedly creating
 * and destroying instances.
 * <p>
 * Note that media player implementations will guarantee that native media player events are delivered
 * in a single-threaded sequential manner.
 *
 * @see EmbeddedMediaPlayerComponent
 */
public interface MediaPlayer {

    AudioService audio();

    ChapterService chapters();

    ControlsService controls();

    EventService events();

    LogoService logo();

    MediaService media();

    MarqueeService marquee();

    MenuService menu();

    RoleService role();

    SlaveService slave();

    SnapshotService snapshots();

    StatusService status();

    SubItemService subItems();

    SubpictureService subpictures();

    TeletextService teletext();

    TitleService titles();

    VideoService video();

    /**
     * Release the media player, freeing all associated (including native) resources.
     */
    void release();

    /**
     *
     *
     * @param r
     */
    void submit(Runnable r); // FIXME i don't really like this on the interface

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
