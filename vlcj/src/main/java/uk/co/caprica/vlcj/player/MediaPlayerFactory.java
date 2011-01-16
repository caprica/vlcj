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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.log.Log;
import uk.co.caprica.vlcj.log.LogLevel;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.linux.LinuxEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.mac.MacEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.WindowsEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
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
 * This factory attempts to determine the run-time operating system and 
 * create an appropriate media player instance.
 * <p>
 * You should release the factory when your application terminates to properly
 * clean up native resources.
 * <p>
 * The factory also provides access to the native libvlc Logger.
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
 *   // Create a media player instance for the run-time operating system
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer(fullScreenStrategy);
 * 
 *   // Do some interesting things with the media player
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
   * Native library interface.
   */
  private final LibVlc libvlc;
  
  /**
   * Native library instance.
   */
  private libvlc_instance_t instance;
  
  /**
   * True when the factory has been released.
   */
  private boolean released;

  /**
   * Create a new media player factory.
   * <p>
   * This factory uses an implementation of the interface to the native libvlc
   * library that is synchronised and logs each method invocation - if you do
   * not want this behaviour you must use one of the other constructors.
   */
  public MediaPlayerFactory() {
    this(new String[] {});
  }
  
  /**
   * Create a new media player factory.
   * <p>
   * This factory uses an implementation of the interface to the native libvlc
   * library that is synchronised and logs each method invocation - if you do
   * not want this behaviour you must use one of the other constructors.
   * 
   * @param libvlcArgs initialisation arguments to pass to libvlc
   */
  public MediaPlayerFactory(String[] libvlcArgs) {
    this(libvlcArgs, LibVlcFactory.factory().synchronise().log().create());
  }

  /**
   * Create a new media player factory.
   * 
   * @param libvlc interface to the native library
   */
  public MediaPlayerFactory(LibVlc libvlc) {
    this(new String[] {}, libvlc);
  }
  
  /**
   * Create a new media player factory.
   * 
   * @param libvlcArgs initialisation arguments to pass to libvlc
   * @param libvlc interface to the native library
   */
  public MediaPlayerFactory(String[] libvlcArgs, LibVlc libvlc) {
    Logger.debug("MediaPlayerFactory(libvlcArgs={},libvlc={})", Arrays.toString(libvlcArgs), libvlc);
    
    // JNA will look for the libvlc shared library here...
    Logger.debug("jna.library.path={}", System.getProperty("jna.library.path"));
    
    // libvlc will look for it's plugins here...
    for(String libvlcArg : libvlcArgs) {
      if(libvlcArg.startsWith("--plugin-path=")) {
        Logger.debug(libvlcArg);
      }
    }
    
    this.libvlc = libvlc;
    
    this.instance = libvlc.libvlc_new(libvlcArgs.length, libvlcArgs);
    Logger.debug("instance={}", instance);
    
    if(instance == null) {
      Logger.error("Failed to initialise libvlc");
      throw new IllegalStateException("Unable to initialise libvlc, check your libvlc options and/or check the console for error messages");
    }
  }
  
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
  
  /**
   * Create a new embedded media player.
   * 
   * @param fullScreenStrategy full screen implementation, may be <code>null</code>
   * @return media player instance
   */
  public EmbeddedMediaPlayer newMediaPlayer(FullScreenStrategy fullScreenStrategy) {
    Logger.debug("newMediaPlayer(fullScreenStrategy={})", fullScreenStrategy);
    
    EmbeddedMediaPlayer mediaPlayer;
    
    if(RuntimeUtil.isNix()) {
      mediaPlayer = new LinuxEmbeddedMediaPlayer(instance, fullScreenStrategy);
    }
    else if(RuntimeUtil.isWindows()) {
      mediaPlayer = new WindowsEmbeddedMediaPlayer(instance, fullScreenStrategy);
    }
    else if(RuntimeUtil.isMac()) {
      // Mac is not yet supported
      mediaPlayer = new MacEmbeddedMediaPlayer(instance, fullScreenStrategy);
    }
    else {
      throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
    }
    
    Logger.debug("mediaPlayer={}", mediaPlayer);
    
    return mediaPlayer;
  }

  /**
   * Create a new direct video rendering media player.
   * 
   * @param width
   * @param height
   * @param renderCallback
   * @return media player instance
   */
  public DirectMediaPlayer newMediaPlayer(int width, int height, RenderCallback renderCallback) {
    Logger.debug("newMediaPlayer(width={},height={},renderCallback={})", width ,height, renderCallback);

    DirectMediaPlayer mediaPlayer = new DirectMediaPlayer(instance, width, height, renderCallback);
    return mediaPlayer;
  }
  
  /**
   * Create a new head-less media player.
   * 
   * @return media player instance
   */
  public HeadlessMediaPlayer newMediaPlayer() {
    Logger.debug("newMediaPlayer()");
    
    HeadlessMediaPlayer mediaPlayer = new HeadlessMediaPlayer(instance);
    return mediaPlayer;
  }

  /**
   * 
   * 
   * @return
   */
  public MediaListPlayer newMediaListPlayer() {
    Logger.debug("newMediaListPlayer()");
    
    MediaListPlayer mediaListPlayer = new MediaListPlayer(instance);
    return mediaListPlayer;
  }
  
  /**
   *
   * 
   * @return
   */
  public MediaList newMediaList() {
    Logger.debug("newMediaList()");
    
    MediaList mediaList = new MediaList(instance);
    return mediaList;
  }
  
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
