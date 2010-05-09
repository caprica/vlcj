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

package uk.co.caprica.vlcj.player.windows;

import java.awt.Canvas;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.FullScreenStrategy;
import uk.co.caprica.vlcj.player.MediaPlayer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class WindowsMediaPlayer extends MediaPlayer {

  private static final Logger LOG = Logger.getLogger(WindowsMediaPlayer.class);
  
  public WindowsMediaPlayer(FullScreenStrategy fullScreenStrategy, libvlc_instance_t instance) {
    super(fullScreenStrategy, instance);
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    if(LOG.isDebugEnabled()) {LOG.debug("nativeSetVideoSurface(mediaPlayerInstance=" + mediaPlayerInstance + ",videoSurface=" + videoSurface + ")");}

    // Thanks for patch from BraCa
    long drawable = Native.getComponentID(videoSurface);
    Pointer ptr = Pointer.createConstant(drawable);
    libvlc.libvlc_media_player_set_hwnd(mediaPlayerInstance, ptr);
  }
}
