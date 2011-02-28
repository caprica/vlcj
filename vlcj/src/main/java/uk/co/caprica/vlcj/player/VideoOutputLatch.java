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

import uk.co.caprica.vlcj.log.Logger;

/**
 * Wait for a video output to be created.
 * <p> 
 * With vlc, even after the "playing" event has fired there may not actually
 * be any video output for a short while longer.
 * <p>
 * After the "playing" event has fired, media information (such as the audio/
 * video/sub-title tracks and chapter details) <em>is</em> available, but
 * information relating to the video itself (i.e. the size of the video) is
 * </em>not</em> available.
 * <p>
 * This implementation loops, sleeping and polling the native media player,
 * until a video output is reported. Polling must be used since there is no
 * native event available to handle this.
 * <p>
 * Most applications are not expected to use this class and instead use
 * {@link uk.co.caprica.vlcj.player.events.VideoOutputEventListener VideoOutputListener}.
 */
public class VideoOutputLatch {

  /**
   * Media player instance.
   */
  private final MediaPlayer mediaPlayer;
  
  /**
   * Wait period, in milliseconds.
   */
  private final int period;
  
  /**
   * Timeout, in milliseconds.
   */
  private final long timeout;
  
  /**
   * Create a new video output latch.
   * 
   * @param mediaPlayer media player instance
   * @param period wait period, in milliseconds
   * @param timeout timeout, in milliseconds
   */
  public VideoOutputLatch(MediaPlayer mediaPlayer, int period, int timeout) {
    this.mediaPlayer = mediaPlayer;
    this.period = period;
    this.timeout = timeout;
  }
  
  /**
   * Wait for a video output to be created.
   * <p>
   * This method call will block until a video output is created (which may be
   * never) or the timeout expires.
   * 
   * @return <code>true</code> if a video output definitely started, <code>false</code> if the time-out expired (a video output might yet start)
   */
  public boolean waitForVideoOutput() { 
    Logger.debug("waitForVideoOutput()");
    long start = System.currentTimeMillis();
    for(;;) {
      Logger.trace("Checking for video output...");
      // Check if a video output has been created yet...
      if(mediaPlayer.getVideoOutputs() > 0) {
        Logger.trace("Got video output.");
        return true;
      }
      // Check for time-out...
      if((System.currentTimeMillis()-start) >= timeout) {
        Logger.warn("Timed out waiting for video output.");
        return false;
      }
      // Sleep for a while...
      try {
        Logger.trace("Sleeping...");
        Thread.sleep(period);
      }
      catch(InterruptedException e) {
        // Keep going...
      }
    }
  }
}
