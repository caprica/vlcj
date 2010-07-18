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

package uk.co.caprica.vlcj.experimental;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * 
 * 
 * <strong>This class is experimental and is subject to change.</strong>
 */
public class DirectVideo {
  
  private static final Logger LOG = Logger.getLogger(DirectVideo.class);

  // This is somewhat strange, but it seems like a new thread is invoking the
  // callbacks each time, so we need a mutex that doesn't ensure the same
  // thread that unlocked it locked it. A little loose, but assuming the
  // invoking code is not invoking unlock without calling lock first, we're
  // okay.
  private final AtomicBoolean mutex = new AtomicBoolean();

  private final RenderCallback renderCallback;

  private final Memory buffer;

  private libvlc_media_player_t player;

  private libvlc_instance_t instance;

  private final LibVlc libvlc = LibVlc.SYNC_INSTANCE;

  // These get garbage collected and cause a crash if we don't keep a reference to them
  private libvlc_lock_callback_t lock;

  private libvlc_unlock_callback_t unlock;

  private libvlc_display_callback_t display;

  private volatile boolean released;
  
  public DirectVideo(libvlc_instance_t instance, int width, int height, RenderCallback callback) {
    this.renderCallback = callback;
    this.instance = instance;

    // Memory must be aligned correctly for the libvlc API functions
    // FIXME For some reason 16 extra bytes must be requested on certain platforms (e.g. 64-bit Linux)
    buffer = new Memory(width * height * 4 + 16).align(32);

    player = libvlc.libvlc_media_player_new(instance);

    libvlc.libvlc_video_set_format(player, "RV32", width, height, width * 4);

    initialize();
  }

  // TODO is there a better way to do the synchronisation?
  private void initialize() {
    lock = new libvlc_lock_callback_t() {
      @Override
      public Pointer lock(Pointer opaque, Pointer plane) {
        // A very simple spin lock - should we yield to not throttle the CPU?
        while(mutex.compareAndSet(false, true)) {}

        plane.setPointer(0, buffer);
        renderCallback.lock();
        return null;
      }
    };

    unlock = new libvlc_unlock_callback_t() {
      @Override
      public void unlock(Pointer opaque, Pointer picture, Pointer plane) {
        mutex.compareAndSet(true, false);
        renderCallback.unlock();
      }
    };

    display = new libvlc_display_callback_t() {
      @Override
      public void display(Pointer opaque, Pointer picture) {
        renderCallback.display(buffer);
      }
    };

    libvlc.libvlc_video_set_callbacks(player, lock, unlock, display, null);
  }

  public void playMedia(String media) {
    setMedia(media);

    play();
  }

  private void play() {
    libvlc.libvlc_media_player_play(player);
  }

  private void setMedia(String media) {
    LOG.debug("setMedia()");

    libvlc_media_t mediaDescriptor = libvlc.libvlc_media_new_path(instance, media);
    libvlc.libvlc_media_player_set_media(player, mediaDescriptor);
    libvlc.libvlc_media_release(mediaDescriptor);
  }

  public void release() {
    LOG.debug("release()");

    if( ! released) {
      destroyInstance();
      released = true;
    }
  }

  @Override
  protected synchronized void finalize() throws Throwable {
    LOG.debug("finalize()");

    release();
  }

  /**
   * Clean up the native media player resources.
   */
  private void destroyInstance() {
    LOG.debug("destroyInstance()");

    if(player != null) {
      LOG.debug("Release media player...");
      libvlc.libvlc_media_player_release(player);
      LOG.debug("Media player released");
      player = null;
    }
  }
}
