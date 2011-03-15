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

package uk.co.caprica.vlcj.player;

import java.awt.Canvas;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_module_description_t;
import uk.co.caprica.vlcj.log.Log;
import uk.co.caprica.vlcj.log.LogLevel;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.DefaultEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.mac.MacVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.windows.WindowsVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.headless.DefaultHeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Factory for media player instances.
 * <p>
 * The factory initialises a single libvlc instance and uses that to create
 * media player instances.
 * <p>
 * If required, you can create multiple factory instances each with their own
 * libvlc options.
 * <p>
 * You should release the factory when your application terminates to properly
 * clean up native resources.
 * <p>
 * The factory also provides access to the native libvlc Logger and other
 * resources such as the list of audio outputs, and the list of available audio
 * and video filters.
 * <p>
 * Usage:
 * <pre>
 *   // Set some options for libvlc
 *   String[] libvlcArgs = {...add options here...};
 * 
 *   // Create a factory instance (once), you can keep a reference to this
 *   MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
 *   
 *   // Create a full-screen strategy
 *   FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);
 *   
 *   // Create a media player instance
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
 * 
 *   // Do some interesting things with the media player, like setting a video surface...
 *   
 *   ...
 *   
 *   // Release the media player
 *   mediaPlayer.release();
 *   
 *   // Release the factory
 *   factory.release();
 * </pre>
 */
public class MediaPlayerFactory {

  /**
   * Help text if the plugins failed to load.
   */
  private static final String PLUGIN_PATH_HELP =
    "Failed to initialise libvlc.\n\n" +
    "This is most often caused by libvlc being unable to locate the required plugins.\n\n" +
    "In the descriptions below <libvlc-path> represents the name of the directory containing \"{0}\" and \"{1}\" and <plugins-path> represents the name of the directory containing the vlc plugins.\n\n" +
    "For libvlc to function correctly the vlc plugins must be available, there are a number of different ways to achieve this:\n" +
    " 1. Make sure the plugins are installed in the \"<libvlc-path>/{2}\" directory, this should be the case with a normal vlc installation.\n" +
    " 2. If using vlc 1.2.x, inculde System.setProperty(\"VLC_PLUGIN_PATH\", \"<plugins-path>\"); at the start of your application code.\n" +
    " 3. If using vlc 1.2.x, specify -DVLC_PLUGIN_PATH=<plugins-path> on the command-line when starting your application.\n" +
    " 4. If using vlc 1.1.x, pass \"--plugin-path=<plugins-path>\" as parameters in your application code when you create a MediaPlayerFactory.\n" + 
    "More information may be available in the log, specify -Dvlcj.log=DEBUG on the command-line when starting your application.\n\n";
  
  /**
   * Native library interface.
   */
  protected final LibVlc libvlc;

  /**
   * Native library instance.
   */
  protected final libvlc_instance_t instance;

  /**
   * True when the factory has been released.
   */
  private boolean released;

  /**
   * Create a new media player factory.
   * <p>
   * If you want to enable logging or synchronisation of the native library
   * interface you must use {@link #MediaPlayerFactory(LibVlc)} and
   * {@link LibVlcFactory}.
   */
  public MediaPlayerFactory() {
    this(new String[] {});
  }

  /**
   * Create a new media player factory.
   * <p>
   * If you want to enable logging or synchronisation of the native library
   * interface you must use {@link #MediaPlayerFactory(LibVlc)} and
   * {@link LibVlcFactory}.
   * 
   * @param libvlcArgs initialisation arguments to pass to libvlc
   */
  public MediaPlayerFactory(String... libvlcArgs) {
    this(LibVlcFactory.factory().create(), libvlcArgs);
  }

  /**
   * Create a new media player factory.
   * <p>
   * Use {@link LibVlcFactory} to get a reference to the native library.
   * 
   * @param libvlc interface to the native library
   */
  public MediaPlayerFactory(LibVlc libvlc) {
    this(libvlc, new String[] {});
  }

  /**
   * Create a new media player factory.
   * <p>
   * Use {@link LibVlcFactory} to get a reference to the native library.
   * @param libvlc interface to the native library
   * @param libvlcArgs initialisation arguments to pass to libvlc
   */
  public MediaPlayerFactory(LibVlc libvlc, String... libvlcArgs) {
    Logger.debug("MediaPlayerFactory(libvlc={},libvlcArgs={})", libvlc, Arrays.toString(libvlcArgs));

    // JNA will look for the libvlc shared library here (and also libvlccore)...
    Logger.debug("jna.library.path={}", System.getProperty("jna.library.path"));

    // Ordinarily libvlc will look for it's plugins in a directory named 
    // "vlc/plugins" relative to the directory where libvlccore is loaded from,
    // this can be overridden by explicitly specifying the "--plugin-path"
    // option although this should not be necessary
    for(String libvlcArg : libvlcArgs) {
      if(libvlcArg.startsWith("--plugin-path=")) {
        Logger.debug(libvlcArg);
      }
    }

    // The "--plugin-path" switch is deprecated in libvlc 1.2.x and is replaced
    // by an environment variable - again as per the above comment it should
    // not be necessary to set this
    String vlcPluginPath = System.getProperty("VLC_PLUGIN_PATH");
    if(vlcPluginPath != null) {
      Logger.debug("VLC_PLUGIN_PATH={}", vlcPluginPath);
    }
    
    this.libvlc = libvlc;

    this.instance = libvlc.libvlc_new(libvlcArgs.length, libvlcArgs);
    Logger.debug("instance={}", instance);

    if(instance == null) {
      Logger.error("Failed to initialise libvlc");
      String msg = MessageFormat.format(PLUGIN_PATH_HELP, new Object[] {RuntimeUtil.getLibVlcName(), RuntimeUtil.getLibVlcCoreName(), RuntimeUtil.getPluginsDirectoryName()});
      throw new RuntimeException(msg);
    }
  }

  /**
   * Release the native resources associated with this factory.
   */
  public void release() {
    Logger.debug("release()");
    if(!released) {
      if(instance != null) {
        libvlc.libvlc_release(instance);
      }
      released = true;
    }
  }

  // === Factory Configuration ================================================

  /**
   * Set the application name.
   * 
   * @param userAgent application name
   */
  public void setUserAgent(String userAgent) {
    Logger.debug("setUserAgent(userAgent={})", userAgent);
    setUserAgent(userAgent, null);
  }

  /**
   * Set the application name.
   * 
   * @param userAgent application name
   * @param httpUserAgent application name for HTTP
   */
  public void setUserAgent(String userAgent, String httpUserAgent) {
    Logger.debug("setUserAgent(userAgent={},httpUserAgent={})", userAgent, httpUserAgent);
    libvlc.libvlc_set_user_agent(instance, userAgent, userAgent);
  }

  /**
   * Get the current log verbosity level.
   * 
   * @return log level
   */
  public int getLogVerbosity() {
    Logger.debug("getLogVerbosity()");
    return libvlc.libvlc_get_log_verbosity(instance);
  }

  /**
   * Set the log verbosity level.
   * <p>
   * The log verbosity level <em>must</em> be set before opening the native
   * log otherwise it will be ignored (at least until the log is closed and
   * then opened again).
   * 
   * @param level log level
   */
  public void setLogLevel(LogLevel level) {
    Logger.debug("setLogVerbosity(level={})", level);
    libvlc.libvlc_set_log_verbosity(instance, level.intValue());
  }

  /**
   * Get the available audio outputs.
   * 
   * @return collection of audio outputs
   */
  public List<AudioOutput> getAudioOutputs() {
    Logger.debug("getAudioOutputs()");
    List<AudioOutput> result = new ArrayList<AudioOutput>();
    libvlc_audio_output_t audioOutput = libvlc.libvlc_audio_output_list_get(instance);
    while(audioOutput != null) {
      result.add(new AudioOutput(audioOutput.psz_name, audioOutput.psz_description));
      audioOutput = audioOutput.p_next;
    }
    libvlc.libvlc_audio_output_list_release(audioOutput);
    return result;
  }

  /**
   * Get the available audio filters.
   * 
   * @return collection of audio filter descriptions
   * 
   * @since libvlc 1.2.0
   */
  public List<ModuleDescription> getAudioFilters() {
    Logger.debug("getAudioFilters()");

    libvlc_module_description_t moduleDescriptions = libvlc.libvlc_audio_filter_list_get(instance);

    // Without disabling auto synch on this JNA structure a fatal crash will 
    // intermittently occur when the release call is made - this is the only 
    // time in all of the vlcj bindings that this is required and I do not 
    // understand why it is needed only in this case
    moduleDescriptions.setAutoSynch(false);

    List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
    libvlc.libvlc_module_description_list_release(moduleDescriptions);
    return result;
  }
  
  /**
   * Get the available video filters.
   * 
   * @return collection of video filter descriptions
   * 
   * @since libvlc 1.2.0
   */
  public List<ModuleDescription> getVideoFilters() {
    Logger.debug("getVideoFilters()");

    libvlc_module_description_t moduleDescriptions = libvlc.libvlc_video_filter_list_get(instance);

    // Without disabling auto synch on this JNA structure a fatal crash will 
    // intermittently occur when the release call is made - this is the only 
    // time in all of the vlcj bindings that this is required and I do not 
    // understand why it is needed only in this case
    moduleDescriptions.setAutoSynch(false);

    List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
    libvlc.libvlc_module_description_list_release(moduleDescriptions);
    return result;
  }

  /**
   * Convert a collection of native module description structures.
   * 
   * @param moduleDescriptions module descriptions
   * @return collection of module descriptions
   */
  private List<ModuleDescription> getModuleDescriptions(libvlc_module_description_t moduleDescriptions) {
    List<ModuleDescription> result = new ArrayList<ModuleDescription>();
    libvlc_module_description_t moduleDescription = moduleDescriptions;
    while(moduleDescription != null) {
      result.add(new ModuleDescription(moduleDescription.psz_name, moduleDescription.psz_shortname, moduleDescription.psz_longname, moduleDescription.psz_help));
      moduleDescription = moduleDescription.p_next;
    }
    return result;
  }
  
  // === Media Player =========================================================

  /**
   * Create a new embedded media player.
   * <p>
   * Full-screen will not be available, to enable full-screen support see
   * {@link #newEmbeddedMediaPlayer(FullScreenStrategy)}.
   * 
   * @return media player instance
   */
  public EmbeddedMediaPlayer newEmbeddedMediaPlayer() {
    Logger.debug("newEmbeddedMediaPlayer()");
    return newEmbeddedMediaPlayer(null);
  }

  /**
   * Create a new embedded media player.
   * 
   * @param fullScreenStrategy full screen implementation, may be <code>null</code>
   * @return media player instance
   */
  public EmbeddedMediaPlayer newEmbeddedMediaPlayer(FullScreenStrategy fullScreenStrategy) {
    Logger.debug("newEmbeddedMediaPlayer(fullScreenStrategy={})", fullScreenStrategy);
    return new DefaultEmbeddedMediaPlayer(libvlc, instance, fullScreenStrategy);
  }

  /**
   * Create a new direct video rendering media player with a pixel format
   * suitable for, amongst other things, rendering into a BufferedImage.
   * <p>
   * The pixel format used is "RV32" (a raw RGB format with padded alpha)
   * and the pitch is width*4.
   * 
   * @param width width for the video
   * @param height height for the video
   * @param renderCallback call-back to receive the video frame data
   * @return direct media player implementation
   */
  public DirectMediaPlayer newDirectMediaPlayer(int width, int height, RenderCallback renderCallback) {
    Logger.debug("newDirectMediaPlayer(width={},height={},renderCallback={})", width, height, renderCallback);
    return newDirectMediaPlayer("RV32", width, height, width * 4, renderCallback);
  }
  
  /**
   * Create a new direct video rendering media player.
   * 
   * @param width width for the video
   * @param height height for the video
   * @param format pixel format (e.g. RV15, RV16, RV24, RV32, RGBA, YUYV)
   * @param pitch pitch, also known as stride
   * @param renderCallback call-back to receive the video frame data
   * @return media player instance
   */
  public DirectMediaPlayer newDirectMediaPlayer(String format, int width, int height, int pitch, RenderCallback renderCallback) {
    Logger.debug("newDirectMediaPlayer(format={},width={},height={},pitch={},renderCallback={})", format, width, height, pitch, renderCallback);
    return new DefaultDirectMediaPlayer(libvlc, instance, format, width, height, pitch, renderCallback);
  }

  /**
   * Create a new head-less media player.
   * <p>
   * The head-less player may spawn a native video player window unless you set
   * proper media options when playing media.
   * 
   * @return media player instance
   */
  public HeadlessMediaPlayer newHeadlessMediaPlayer() {
    Logger.debug("newHeadlessMediaPlayer()");
    return new DefaultHeadlessMediaPlayer(libvlc, instance);
  }

  /**
   * Create a new play-list media player.
   * 
   * @return media player instance
   */
  public MediaListPlayer newMediaListPlayer() {
    Logger.debug("newMediaListPlayer()");
    return new DefaultMediaListPlayer(libvlc, instance);
  }

  // === Video Surface ========================================================

  /**
   * Create a new video surface for a Canvas.
   * 
   * @param canvas canvas
   * @return video surface
   */
  public CanvasVideoSurface newVideoSurface(Canvas canvas) {
    Logger.debug("newVideoSurface(canvas={})", canvas);

    VideoSurfaceAdapter videoSurfaceAdapter;
    if(RuntimeUtil.isNix()) {
      videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
    }
    else if(RuntimeUtil.isWindows()) {
      videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
    }
    else if(RuntimeUtil.isMac()) {
      videoSurfaceAdapter = new MacVideoSurfaceAdapter();
    }
    else {
      throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
    }

    CanvasVideoSurface videoSurface = new CanvasVideoSurface(canvas, videoSurfaceAdapter);
    Logger.debug("videoSurface={}", videoSurface);

    return videoSurface;
  }

  /**
   * Create a new video surface for a native component id.
   * 
   * @param componentId native component id
   * @return video surface
   */
  public ComponentIdVideoSurface newVideoSurface(long componentId) {
    Logger.debug("newVideoSurface(componentId={})", componentId);

    VideoSurfaceAdapter videoSurfaceAdapter;

    if(RuntimeUtil.isNix()) {
      videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
    }
    else if(RuntimeUtil.isWindows()) {
      videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
    }
    else if(RuntimeUtil.isMac()) {
      videoSurfaceAdapter = new MacVideoSurfaceAdapter();
    }
    else {
      throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
    }

    ComponentIdVideoSurface videoSurface = new ComponentIdVideoSurface(componentId, videoSurfaceAdapter);

    Logger.debug("videoSurface={}", videoSurface);

    return videoSurface;
  }

  // === Media List ===========================================================

  /**
   * Create a new media list for a play-list media player.
   * 
   * @return media list instance
   */
  public MediaList newMediaList() {
    Logger.debug("newMediaList()");
    return new MediaList(libvlc, instance);
  }

  // === Log ==================================================================

  /**
   * Get a new message Logger.
   * <p>
   * The log will be opened.
   * 
   * @return new log instance
   */
  public Log newLog() {
    Logger.debug("newLog()");
    Log log = new Log(libvlc, instance);
    log.open();
    return log;
  }
}
