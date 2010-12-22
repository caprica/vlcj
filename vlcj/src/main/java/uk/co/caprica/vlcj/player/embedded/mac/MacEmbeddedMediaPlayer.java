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

package uk.co.caprica.vlcj.player.embedded.mac;

import java.awt.Canvas;

import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.log.Logger;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

/**
 *
 */
public class MacEmbeddedMediaPlayer extends EmbeddedMediaPlayer {

  /**
   * 
   * 
   * @param instance
   * @param fullScreenStrategy
   */
  public MacEmbeddedMediaPlayer(libvlc_instance_t instance, FullScreenStrategy fullScreenStrategy) {
    super(instance, fullScreenStrategy);
  }

  @Override
  protected void nativeSetVideoSurface(libvlc_media_player_t mediaPlayerInstance, Canvas videoSurface) {
    Logger.debug("nativeSetVideoSurface(mediaPlayerInstance={},videoSurface={})", mediaPlayerInstance, videoSurface);

    // FIXME how?
    throw new UnsupportedOperationException("Send patches!");
  }
}
