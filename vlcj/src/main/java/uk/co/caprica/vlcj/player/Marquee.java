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

import java.awt.Color;

import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;

/**
 * Builder for a Marquee.
 * <p>
 * Use like this, with a static import of {@link #marquee()}:
 * <pre>
 *   marquee()
 *     .opacity(200)
 *     .position(libvlc_marquee_position_e.bottom)
 *     .colour(Color.white)
 *     .timeout(5000)
 *     .text("vlcj is just great")
 *     .size(20)
 *     .enable()
 *     .apply(mediaPlayer)
 *   ;
 * </pre>
 */
public final class Marquee {

  private String text;
  private Color colour;
  private Integer rgb;
  private Integer intOpacity;
  private Float floatOpacity;
  private Integer size;
  private Integer timeout;
  private Integer x;
  private Integer y;
  private libvlc_marquee_position_e position;
  private boolean enable;
  
  /**
   * Create a marquee.
   * 
   * @return marquee
   */
  public static Marquee marquee() {
    return new Marquee();
  }

  private Marquee() {
  }
  
  public Marquee text(String text) {
    this.text = text;
    return this;
  }
  
  public Marquee colour(Color colour) {
    this.colour = colour;
    return this;
  }
  
  public Marquee colour(int rgb) {
    this.rgb = rgb;
    return this;
  }
  
  public Marquee opacity(int opacity) {
    this.intOpacity = opacity;
    return this;
  }
  
  public Marquee opacity(float opacity) {
    this.floatOpacity = opacity;
    return this;
  }
  
  public Marquee size(int size) {
    this.size = size;
    return this;
  }
  
  public Marquee timeout(int timeout) {
    this.timeout = timeout;
    return this;
  }
  
  public Marquee location(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }
  
  public Marquee position(libvlc_marquee_position_e position) {
    this.position = position;
    return this;
  }

  public Marquee enable(boolean enable) {
    this.enable = enable;
    return this;
  }
  
  public Marquee enable() {
    this.enable = true;
    return this;
  }

  public Marquee disable() {
    this.enable = false;
    return this;
  }
  
  /**
   * Apply the marquee to the media player.
   * 
   * @param mediaPlayer media player
   */
  public void apply(MediaPlayer mediaPlayer) {
    if(text != null) {
      mediaPlayer.setMarqueeText(text);
    }
    if(colour != null) {
      mediaPlayer.setMarqueeColour(colour);
    }
    if(rgb != null) {
      mediaPlayer.setMarqueeColour(rgb);
    }
    if(intOpacity != null) {
      mediaPlayer.setMarqueeOpacity(intOpacity);
    }
    if(floatOpacity != null) {
      mediaPlayer.setMarqueeOpacity(floatOpacity);
    }
    if(size != null) {
      mediaPlayer.setMarqueeSize(size);
    }
    if(timeout != null) {
      mediaPlayer.setMarqueeTimeout(timeout);
    }
    if(x != null && y != null && x >= 0 && y >= 0) {
      mediaPlayer.setMarqueeLocation(x, y);
    }
    if(position != null) {
      mediaPlayer.setMarqueePosition(position);
    }
    if(enable) {
      mediaPlayer.enableMarquee(true);
    }
  }
}
