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

package uk.co.caprica.vlcj.binding;

import uk.co.caprica.vlcj.binding.internal.LibVlcCallback;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventManager;
import uk.co.caprica.vlcj.binding.internal.LibVlcInstance;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaDescriptor;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaInstance;
import uk.co.caprica.vlcj.binding.internal.libvlc_exception_t;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

/**
 * JNA interface to the libvlc native library.
 * <p>
 * This is <strong>not a complete</strong> interface to libvlc.
 * <p>
 * This interface specifies the exposed methods only, the types and structures
 * are all factored out separately in the "internal" sub-package.
 * <p>
 * This code and that in the internal sub-package is structured out of necessity
 * to interoperate with the libvlc native library. This code was derived from 
 * the original JVLC source code, the copyright of which belongs to the VideoLAN 
 * team, which was distributed under GPL version 2 or later. 
 */
public interface LibVlc extends Library {
  
  /**
   * Native library instance.
   */
  LibVlc INSTANCE = (LibVlc)Native.loadLibrary(Platform.isWindows() ? "libvlc" : "vlc", LibVlc.class);

  /**
   * Synchronised native library instance.
   */
  LibVlc SYNC_INSTANCE = (LibVlc)Native.synchronizedLibrary(INSTANCE);

  /**
   * 
   * 
   * @param argc
   * @param argv
   * @param exception
   * @return
   */
  LibVlcInstance libvlc_new(int argc, String[] argv, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   */
  void libvlc_release(LibVlcInstance instance);

  /**
   * 
   * 
   * @param mediaInstance
   * @param drawable
   * @param exception
   */
  // TODO the drawable parameter should be really be a long?
  void libvlc_media_player_set_drawable(LibVlcMediaInstance mediaInstance, int drawable, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_state(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   */
  void libvlc_media_player_play(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   */
  void libvlc_media_player_stop(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   */
  void libvlc_media_player_pause(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_has_vout(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param time
   * @param exception
   */
  void libvlc_media_player_set_time(LibVlcMediaInstance mediaInstance, long time, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  long libvlc_media_player_get_time(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  long libvlc_media_player_get_length(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  float libvlc_media_player_get_position(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param position
   * @param exception
   */
  void libvlc_media_player_set_position(LibVlcMediaInstance mediaInstance, float position, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param instance
   * @param exception
   */
  void libvlc_audio_toggle_mute(LibVlcInstance instance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   * @param mute
   * @param exception
   */
  void libvlc_audio_set_mute(LibVlcInstance instance, int mute, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   * @param exception
   * @return
   */
  int libvlc_audio_get_mute(LibVlcInstance instance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   * @param exception
   * @return
   */
  int libvlc_audio_get_volume(LibVlcInstance instance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   * @param volume
   * @param exception
   * @return
   */
  int libvlc_audio_set_volume(LibVlcInstance instance, int volume, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_chapter_count(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param chapter
   * @param exception
   */
  void libvlc_media_player_set_chapter(LibVlcMediaInstance mediaInstance, int chapter, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_chapter(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param instance
   * @param exception
   * @return
   */
  LibVlcMediaInstance libvlc_media_player_new(LibVlcInstance instance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaDescriptor
   * @param exception
   * @return
   */
  LibVlcMediaInstance libvlc_media_player_new_from_media(LibVlcMediaDescriptor mediaDescriptor, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param instance
   */
  void libvlc_media_player_release(LibVlcMediaInstance instance);

  /**
   * 
   * 
   * @param instance
   * @param mrl
   * @param exception
   * @return
   */
  LibVlcMediaDescriptor libvlc_media_new(LibVlcInstance instance, String mrl, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaDescriptor
   * @param option
   * @param exception
   */
  void libvlc_media_add_option(LibVlcMediaDescriptor mediaDescriptor, String option, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param mediaDescriptor
   * @param exception
   */
  void libvlc_media_player_set_media(LibVlcMediaInstance mediaInstance, LibVlcMediaDescriptor mediaDescriptor, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaDescriptor
   */
  void libvlc_media_release(LibVlcMediaDescriptor mediaDescriptor);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  LibVlcEventManager libvlc_media_player_event_manager(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param eventManager
   * @param eventType
   * @param callback
   * @param userData
   * @param exception
   */
  void libvlc_event_attach(LibVlcEventManager eventManager, int eventType, LibVlcCallback callback, Pointer userData, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param eventManager
   * @param eventType
   * @param callback
   * @param userData
   * @param exception
   */
  void libvlc_event_detach(LibVlcEventManager eventManager, int eventType, LibVlcCallback callback, Pointer userData, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_video_get_width(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_video_get_height(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
}
