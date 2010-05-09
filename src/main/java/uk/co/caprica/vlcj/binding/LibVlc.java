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


import java.lang.reflect.Proxy;

import uk.co.caprica.vlcj.binding.internal.LibVlcCallback;
import uk.co.caprica.vlcj.binding.internal.LibVlcEventManager;
import uk.co.caprica.vlcj.binding.internal.LibVlcInstance;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaDescriptor;
import uk.co.caprica.vlcj.binding.internal.LibVlcMediaInstance;
import uk.co.caprica.vlcj.binding.internal.libvlc_exception_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.log.LoggingProxy;

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
   */
  LibVlc LOGGING_INSTANCE = (LibVlc)Proxy.newProxyInstance(LibVlc.class.getClassLoader(), new Class<?>[] {LibVlc.class}, new LoggingProxy(LibVlc.INSTANCE));

  /**
   * 
   */
  LibVlc LOGGING_SYNC_INSTANCE = (LibVlc)Proxy.newProxyInstance(LibVlc.class.getClassLoader(), new Class<?>[] {LibVlc.class}, new LoggingProxy(LibVlc.SYNC_INSTANCE));

  
  // === libvlc.h =============================================================
  
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
   * @param p_instance
   * @param name
   * @param p_exception
   */
  void libvlc_add_intf(LibVlcInstance p_instance, String name, libvlc_exception_t p_exception);
  
  /**
   * 
   * 
   * @param p_instance
   */
  void libvlc_wait(LibVlcInstance p_instance);
  
  /**
   * Retrieve libvlc version.
   *
   * Example: "1.1.0-git The Luggage"
   *
   * @return a string containing the libvlc version
   */
  String libvlc_get_version();

  /**
   * Retrieve libvlc compiler version.
   *
   * Example: "gcc version 4.2.3 (Ubuntu 4.2.3-2ubuntu6)"
   *
   * @return a string containing the libvlc compiler version
   */
  String libvlc_get_compiler();

  /**
   * Retrieve libvlc changeset.
   *
   * Example: "aa9bce0bc4"
   *
   * @return a string containing the libvlc changeset
   */
  String libvlc_get_changeset();
  
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
  
  // === libvlc.h =============================================================
  
  
  // === libvlc_media_h =======================================================
  
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
   * @param p_instance
   * @param psz_name
   * @param p_e
   * @return
   */
  LibVlcMediaDescriptor libvlc_media_new_as_node(LibVlcInstance p_instance, String psz_name, libvlc_exception_t p_e);

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
   * @param p_meta_desc
   */
  void libvlc_media_retain(LibVlcMediaDescriptor p_meta_desc);
  
  /**
   * 
   * 
   * @param mediaDescriptor
   */
  void libvlc_media_release(LibVlcMediaDescriptor mediaDescriptor);
  
  /**
   *
   * 
   * @param p_md
   * @param p_e
   * @return
   */
  String libvlc_media_get_mrl(LibVlcMediaDescriptor p_md, libvlc_exception_t p_e);

  /**
   * 
   * 
   * @param p_md
   * @return
   */
  LibVlcMediaDescriptor libvlc_media_duplicate(LibVlcMediaDescriptor p_md);
  
  /**
   * 
   * 
   * @param p_meta_desc
   * @param e_meta
   * @param p_e
   * @return
   */
//  String libvlc_media_get_meta(LibVlcMediaDescriptor p_meta_desc, libvlc_meta_t e_meta, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_meta_desc
   * @param p_e
   * @return
   */
//  libvlc_state_t libvlc_media_get_state(LibVlcMediaDescriptor p_meta_desc, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_md
   * @param p_e
   * @return
   */
  LibVlcEventManager libvlc_media_event_manager(LibVlcMediaDescriptor p_md, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_md
   * @param p_e
   * @return
   */
  long libvlc_media_get_duration(LibVlcMediaDescriptor p_md, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_md
   * @param p_e
   * @return
   */
  int libvlc_media_is_preparsed(LibVlcMediaDescriptor p_md, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_md
   * @param p_new_user_data
   * @param p_e
   */
  void libvlc_media_set_user_data(LibVlcMediaDescriptor p_md, Pointer p_new_user_data, libvlc_exception_t p_e);

  /**
   * 
   * 
   * @param p_md
   * @param p_e
   * @return
   */
  Pointer libvlc_media_get_user_data(LibVlcMediaDescriptor p_md, libvlc_exception_t p_e);

  // === libvlc_media_h =======================================================


  // === libvlc_media_player_h ================================================

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
   */
  void libvlc_media_player_retain(LibVlcMediaInstance instance);  
  
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
   * @param mediaInstance
   * @param exception
   * @return
   */
  LibVlcMediaDescriptor libvlc_media_player_get_media(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
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
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_is_playing (LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
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
  void libvlc_media_player_pause(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

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
   * @param p_mi
   * @param drawable
   * @param exception
   */
  void libvlc_media_player_set_nsobject(LibVlcMediaInstance p_mi, Pointer drawable, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @return
   */
  Pointer libvlc_media_player_get_nsobject(LibVlcMediaInstance p_mi);

  /**
   * 
   * 
   * @param p_mi
   * @param drawable
   * @param exception
   */
  void libvlc_media_player_set_agl(LibVlcMediaInstance p_mi, int drawable, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @return
   */
  int libvlc_media_player_get_agl(LibVlcMediaInstance p_mi);

  /**
   * 
   * 
   * @param p_mi
   * @param drawable
   * @param exception
   */
  void libvlc_media_player_set_xwindow(LibVlcMediaInstance p_mi, int drawable, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @return
   */
  int libvlc_media_player_get_xwindow(LibVlcMediaInstance p_mi);

  /**
   * 
   * 
   * @param p_mi
   * @param drawable
   * @param exception
   */
  void libvlc_media_player_set_hwnd(LibVlcMediaInstance p_mi, Pointer drawable, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @return
   */
  Pointer libvlc_media_player_get_hwnd(LibVlcMediaInstance p_mi);
  
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
  long libvlc_media_player_get_time(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
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
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_chapter_count(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   *    
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_will_play(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param title
   * @param exception
   */
  void libvlc_media_player_set_title(LibVlcMediaInstance mediaInstance, int title, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_title(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_media_player_get_title_count(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   */
  void libvlc_media_player_previous_chapter(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   */
  void libvlc_media_player_next_chapter(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  float libvlc_media_player_get_rate(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param rate
   * @param exception
   */
  void libvlc_media_player_set_rate(LibVlcMediaInstance mediaInstance, float rate, libvlc_exception_t exception);
  
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
   * @return
   */
  float libvlc_media_player_get_fps(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
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
   * @param p_mi
   * @param p_e
   * @return
   */
  int libvlc_media_player_is_seekable(LibVlcMediaInstance p_mi, libvlc_exception_t p_e);

  /**
   * 
   * 
   * @param p_mi
   * @param p_e
   * @return
   */
  int libvlc_media_player_can_pause(LibVlcMediaInstance p_mi, libvlc_exception_t p_e);
  
  /**
   * 
   * 
   * @param p_mi
   * @param exception
   */
  void libvlc_toggle_fullscreen(LibVlcMediaInstance p_mi, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @param fullSceren
   * @param exception
   */
  void libvlc_set_fullscreen(LibVlcMediaInstance p_mi, int fullSceren, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param p_mi
   * @param exception
   * @return
   */
  int libvlc_get_fullscreen(LibVlcMediaInstance p_mi, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_video_get_height(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

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
  float libvlc_video_get_scale(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param scale
   * @param exception
   */
  void libvlc_video_set_scale(LibVlcMediaInstance mediaInstance, float scale, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  String libvlc_video_get_aspect_ratio(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param aspectRatio
   * @param exception
   */
  void libvlc_video_set_aspect_ratio(LibVlcMediaInstance mediaInstance, String aspectRatio, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_video_get_spu(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  int libvlc_video_get_spu_count(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param exception
   * @return
   */
  libvlc_track_description_t libvlc_video_get_spu_description(LibVlcMediaInstance mediaInstance, libvlc_exception_t exception);
  
  /**
   * 
   * 
   * @param mediaInstance
   * @param spu
   * @param exception
   */
  void libvlc_video_set_spu(LibVlcMediaInstance mediaInstance, int spu, libvlc_exception_t exception);

  /**
   * 
   * 
   * @param mediaInstance
   * @param fileName
   * @param width
   * @param height
   * @param exception
   */
  void libvlc_video_take_snapshot(LibVlcMediaInstance mediaInstance, String fileName, int width, int height, libvlc_exception_t exception);

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
   * @param exception
   * @return
   */
  int libvlc_audio_get_mute(LibVlcInstance instance, libvlc_exception_t exception);

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
  
  // === libvlc_media_player_h ================================================
  
}
