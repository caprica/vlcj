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

package uk.co.caprica.vlcj.player.direct;

import java.util.concurrent.Semaphore;

import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * Media player implementation that provides direct access to the video frame
 * data.
 */
public class DirectMediaPlayer extends MediaPlayer {

  /**
   * Use a semaphore with a single permit to ensure that the lock, display, 
   * unlock cycle goes in a serial manner.
   */
  private final Semaphore semaphore = new Semaphore(1);
  
  /**
   * Video buffer width.
   */
  private final int width;
  
  /**
   * Video buffer height.
   */
  private final int height;
  
  /**
   * Component to call-back for each video frame.
   */
  private final RenderCallback renderCallback;

  /**
   * Native memory buffer.
   */
  private final Memory nativeBuffer;

  /**
   * Lock call-back.
   * <p>
   * A hard reference to the call-back must be kept otherwise the call-back 
   * will get garbage collected and cause a native crash.
   */
  private final libvlc_lock_callback_t lock;

  /**
   * Unlock call-back.
   * <p>
   * A hard reference to the call-back must be kept otherwise the call-back 
   * will get garbage collected and cause a native crash.
   */
  private final libvlc_unlock_callback_t unlock;

  /**
   * Display call-back.
   * <p>
   * A hard reference to the call-back must be kept otherwise the call-back 
   * will get garbage collected and cause a native crash.
   */
  private final libvlc_display_callback_t display;
  
  /**
   * Create a new media player.
   * 
   * @param instance libvlc instance
   * @param width width for the video
   * @param height height for the video
   * @param renderCallback call-back to receive the video frame data
   */
  public DirectMediaPlayer(libvlc_instance_t instance, int width, int height, RenderCallback renderCallback) {
    super(instance);

    this.width = width;
    this.height = height;
    this.renderCallback = renderCallback;
    
    // Memory must be aligned correctly (on a 32-byte boundary) for the libvlc 
    // API functions (extra bytes are allocated to allow for enough memory if
    // the alignment needs to be changed)
    this.nativeBuffer = new Memory(width * height * 4 + 32).align(32);

    this.lock = new libvlc_lock_callback_t() {
//      @Override
      public Pointer lock(Pointer opaque, Pointer plane) {
        Logger.trace("lock");
        // Acquire the single permit from the semaphore to ensure that the 
        // memory buffer is not trashed while display() is invoked
        Logger.trace("acquire");
        semaphore.acquireUninterruptibly();
        Logger.trace("acquired");
        plane.setPointer(0, nativeBuffer);
        Logger.trace("lock finished");
        return null;
      }
    };

    this.unlock = new libvlc_unlock_callback_t() {
//      @Override
      public void unlock(Pointer opaque, Pointer picture, Pointer plane) {
        Logger.trace("unlock");
        
        // Release the semaphore
        Logger.trace("release");
        semaphore.release();
        Logger.trace("released");

        Logger.trace("unlock finished");
      }
    };

    this.display = new libvlc_display_callback_t() {
//      @Override
      public void display(Pointer opaque, Pointer picture) {
        Logger.trace("display");
        
        // Invoke the call-back
        DirectMediaPlayer.this.renderCallback.display(nativeBuffer);

        Logger.trace("display finished");
      }
    };

    // RV15, RV16, RV24, RV32, RGBA, YUYV
    libvlc.libvlc_video_set_format(mediaPlayerInstance(), "RV32", width, height, width * 4);
    libvlc.libvlc_video_set_callbacks(mediaPlayerInstance(), lock, unlock, display, null);
  }
  
  /**
   * Get the buffer width.
   * 
   * @return width
   */
  public int width() {
    return width;
  }

  /**
   * Get the buffer height.
   * 
   * @return height
   */
  public int height() {
    return height;
  }
}
