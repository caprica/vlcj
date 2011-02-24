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

package uk.co.caprica.vlcj.player.list;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.log.Logger;

/**
 * A media list (i.e. a play-list).
 */
public class MediaList {

  /**
   * Native interface.
   */
  private LibVlc libvlc;
  
  /**
   * Native library instance.
   */
  private libvlc_instance_t instance;

  /**
   * Play-list instance.
   */
  private libvlc_media_list_t mediaListInstance;
  
  /**
   * Set to true when the player has been released.
   */
  private AtomicBoolean released = new AtomicBoolean();

  /**
   * Standard media options to be applied to each media item that is played.
   */
  private String[] standardMediaOptions;
  
  /**
   * Map of native media instances.
   */
  private final Map<libvlc_media_t, String> mediaListMap = new HashMap<libvlc_media_t, String>();
  
  /**
   * Create a new media list.
   * 
   * @param libvlc native interface
   * @param instance native library instance
   */
  public MediaList(LibVlc libvlc, libvlc_instance_t instance) {
    this.libvlc = libvlc;
    this.instance = instance;
    
    createInstance();
  }

  /**
   * 
   * 
   * @param standardMediaOptions
   */
  public void setStandardMediaOptions(String... standardMediaOptions) {
    this.standardMediaOptions = standardMediaOptions;
  }
  
  /**
   * 
   * 
   * @param mrl
   * @param mediaOptions
   */
  public void addMedia(String mrl, String... mediaOptions) {
    Logger.debug("addMedia(mrl={},mediaOptions={})", mrl, mediaOptions);
    try {
      lock();
      // Create a new native media descriptor
      libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
      // Store the new native media descriptor in the MRL map
      mediaListMap.put(mediaDescriptor, mrl);
      // Insert the media descriptor into the media list
      libvlc.libvlc_media_list_add_media(mediaListInstance, mediaDescriptor);
      releaseMediaDescriptor(mediaDescriptor);
    }
    finally {
      unlock();
    }
  }
  
  /**
   * 
   * 
   * @param index
   * @param mrl
   * @param mediaOptions
   */
  public void insertMedia(int index, String mrl, String... mediaOptions) {
    Logger.debug("insertMedia(index={},mrl={},mediaOptions={})", index, mrl, mediaOptions);
    try {
      lock();
      // Create a new native media descriptor
      libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
      // Store the new native media descriptor in the MRL map
      mediaListMap.put(mediaDescriptor, mrl);
      // Insert the media descriptor into the media list
      libvlc.libvlc_media_list_insert_media(mediaListInstance, mediaDescriptor, index);
      releaseMediaDescriptor(mediaDescriptor);
    }
    finally {
      unlock();
    }
  }
  
  /**
   * 
   * 
   * @param index
   */
  public void removeMedia(int index) {
    Logger.debug("removeMedia(index={})", index);
    try {
      lock();
      libvlc_media_t oldMediaInstance = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
      if(oldMediaInstance != null) {
        // Remove the old native media descriptor from the MRL map
        mediaListMap.remove(oldMediaInstance);
        // Remove the media descriptor from the media list
        libvlc.libvlc_media_list_remove_index(mediaListInstance, index);
      }
    }
    finally {
      unlock();
    }
  }
  
  /**
   * Get the number of items currently in the list.
   * 
   * @return item count
   */
  public int size() {
    Logger.debug("size()");
    try {
      lock();
      int size = libvlc.libvlc_media_list_count(mediaListInstance);
      return size;
    }
    finally {
      unlock();
    }
  }
  
  /**
   * 
   * 
   * @return
   */
  public boolean isReadOnly() {
    Logger.debug("isReadOnly()");
    boolean readOnly = libvlc.libvlc_media_list_is_readonly(mediaListInstance) == 0;
    return readOnly;
  }
  
  /**
   * 
   */
  public final void release() {
    Logger.debug("release()");
    if(released.compareAndSet(false, true)) {
      destroyInstance();
    }
  }
  
  /**
   * 
   */
  private void createInstance() {
    Logger.debug("createInstance()");
    mediaListInstance = libvlc.libvlc_media_list_new(instance);
  }
  
  /**
   * 
   */
  private void destroyInstance() {
    Logger.debug("destroyInstance()");
    if(mediaListInstance != null) {
      libvlc.libvlc_media_list_release(mediaListInstance);
    }
  }
  
  /**
   * Acquire the media list lock.
   */
  private void lock() {
    Logger.debug("lock()");
    libvlc.libvlc_media_list_lock(mediaListInstance);
  }
  
  /**
   * Release the media list lock. 
   */
  private void unlock() {
    Logger.debug("unlock()");
    libvlc.libvlc_media_list_unlock(mediaListInstance);
  }

  /**
   * 
   * 
   * @param media
   * @param mediaOptions
   * @return 
   */
  private libvlc_media_t newMediaDescriptor(String media, String... mediaOptions) {
    Logger.debug("newMediaDescriptor(media={},mediaOptions={})" , media, Arrays.toString(mediaOptions));
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
    return mediaDescriptor;
  }

  /**
   * 
   * 
   * @param mediaDescripor
   */
  private void releaseMediaDescriptor(libvlc_media_t mediaDescriptor) {
    Logger.debug("releaseMediaDescriptor(mediaDescriptor={})", mediaDescriptor);
    libvlc.libvlc_media_release(mediaDescriptor);
  }
  
  /**
   * 
   * 
   * @param mediaInstance
   * @return
   */
  public String mrl(libvlc_media_t mediaInstance) {
    return mediaListMap.get(mediaInstance);
  }
  
  /**
   * 
   * 
   * @return
   */
  libvlc_media_list_t mediaListInstance() {
    return mediaListInstance;
  }
}
