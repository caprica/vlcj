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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class implements a strategy for handling multiple player instances.
 * <p>
 * A strategy is required since there are threading issues in the native 
 * libraries that are exposed as races when repeated calls to play() are made.
 * <p>
 * The strategy is simply to block the play call until a media player playing 
 * event is received.
 * <p>
 * This has limitations in that if an error occurs (e.g. file not found) then
 * it will block forever since no playing event will be received nor in fact 
 * will any error event.
 * <p>
 * For this reason it is possible to specify a timeout - if the timeout expires
 * before a playing event is received then the play method will return (the
 * return value will be <code>false</code>. It is possible that the media will
 * still actually start playing even if <code>false</code> is returned. 
 * <p>
 * Example usage:
 * <pre>
 *   mediaPlayer.prepareMedia(mrl, options);
 *   new MediaPlayerLatch(mediaPlayer).play();
 * </pre>
 * Or:
 * <pre>
 *   mediaPlayer.prepareMedia(mrl, options);
 *   MediaPlayerLatch playerLatch = new MediaPlayerLatch(mediaPlayer);
 *   playerLatch.setTimeout(3, TimeUnit.SECONDS);   
 *   boolean maybeStarted = playerLatch.play();
 * </pre>
 * <strong>This class is experimental and is subject to change/removal.</strong>
 */
public class MediaPlayerLatch {

  private final MediaPlayer mediaPlayer;
  private long timeout;
  private TimeUnit timeUnit;
  
  public MediaPlayerLatch(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }
  
  public void setTimeout(long timeout, TimeUnit timeUnit) {
    this.timeout = timeout;
    this.timeUnit = timeUnit;
  }
  
  public boolean play() { 
    CountDownLatch latch = new CountDownLatch(1);
    MediaPlayerEventListener listener = new LatchListener(latch);
    mediaPlayer.addMediaPlayerEventListener(listener);
    mediaPlayer.play();
    try {
      if(timeout > 0L) {
        return latch.await(timeout, timeUnit);
      }
      else {
        latch.await();
        return true;
      }
    }
    catch(InterruptedException e) {
    }
    finally {
      mediaPlayer.removeMediaPlayerEventListener(listener);
    }
    return false;
  }
  
  private final class LatchListener extends MediaPlayerEventAdapter {
    
    private CountDownLatch latch;
    
    private LatchListener(CountDownLatch latch) {
      this.latch = latch;
    }
    
    @Override
    public void playing(MediaPlayer mediaPlayer) {
      latch.countDown();
      latch = null;
    }
  }
}
