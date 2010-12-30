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

package uk.co.caprica.vlcj.player.list;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.log.Logger;

/**
 *
 */
public class MediaList {

  /**
   * 
   */
  private final LibVlc libvlc = LibVlcFactory.factory().synchronise().log().create();

  /**
   * 
   */
  private libvlc_instance_t instance;

  /**
   * 
   */
  private libvlc_media_list_t mediaListInstance;
  
  /**
   * Set to true when the player has been released.
   */
  private AtomicBoolean released = new AtomicBoolean();

  /**
   * 
   */
  private String[] standardMediaOptions;
  
  /**
   * 
   * 
   * @param instance
   */
  public MediaList(libvlc_instance_t instance) {
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
   */
  public void addMedia(String mrl) {
    Logger.debug("add(mrl={})", mrl);
    
    addMedia(mrl, (String[])null);
  }
  
  /**
   * 
   * 
   * @param media
   * @param mediaOptions
   */
  public void addMedia(String media, String... mediaOptions) {
    Logger.debug("addMedia(media={}, mediaOptions={})", media, mediaOptions);
   
    try {
      lock();
      
      libvlc_media_t mediaDescriptor = newMediaDescriptor(media, mediaOptions);
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
   * @param media
   */
  public void insertMedia(int index, String media) {
    Logger.debug("insertMedia(index={}, media={})", index, media);

    insertMedia(index, media, (String[])null);
  }
  
  /**
   * 
   * 
   * @param index
   * @param media
   * @param mediaOptions
   */
  public void insertMedia(int index, String media, String... mediaOptions) {
    Logger.debug("insertMedia(index={}, media={}, mediaOptions={})", index, media, mediaOptions);
    
    try {
      lock();
      
      libvlc_media_t mediaDescriptor = newMediaDescriptor(media, mediaOptions);
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
      libvlc.libvlc_media_list_remove_index(mediaListInstance, index);
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
   * 
   */
  private void lock() {
    Logger.debug("lock()");
    libvlc.libvlc_media_list_lock(mediaListInstance);
  }
  
  /**
   * 
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
   * @return
   */
  libvlc_media_list_t mediaListInstance() {
    return mediaListInstance;
  }
}
