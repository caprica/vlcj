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

import java.awt.image.RenderedImage;
import java.io.File;

import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;

/**
 * Builder for a Logo.
 * <p>
 * Use like this, with a static import of {@link #logo()}:
 * <pre>
 *   logo()
 *     .opacity(200)
 *     .location(100, 100)
 *     .file("logo.png")
 *     .enable()
 *     .apply(mediaPlayer)
 *   ;
 * </pre>
 */
public final class Logo {

  private Integer intOpacity;
  private Float floatOpacity;
  private Integer x;
  private Integer y;
  private libvlc_logo_position_e position;
  private String file;
  private RenderedImage image;
  private boolean enable;
  
  /**
   * Create a logo.
   * 
   * @return logo
   */
  public static Logo logo() {
    return new Logo();
  }
  
  private Logo() {
  }
  
  public Logo opacity(int opacity) {
    this.intOpacity = opacity;
    return this;
  }
  
  public Logo opacity(float opacity) {
    this.floatOpacity = opacity;
    return this;
  }
  
  public Logo location(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }
  
  public Logo position(libvlc_logo_position_e position) {
    this.position = position;
    return this;
  }

  public Logo file(String file) {
    this.file = file;
    return this;
  }
  
  public Logo file(File file) {
    this.file = file.getAbsolutePath();
    return this;
  }
  
  public Logo image(RenderedImage image) {
    this.image = image;
    return this;
  }
  
  public Logo enable(boolean enable) {
    this.enable = enable;
    return this;
  }
  
  public Logo enable() {
    this.enable = true;
    return this;
  }

  public Logo disable() {
    this.enable = false;
    return this;
  }
  
  /**
   * Apply the logo to the media player.
   * 
   * @param mediaPlayer media player
   */
  public void apply(MediaPlayer mediaPlayer) {
    if(intOpacity != null) {
      mediaPlayer.setLogoOpacity(intOpacity);
    }
    if(floatOpacity != null) {
      mediaPlayer.setLogoOpacity(floatOpacity);
    }
    if(x != null && y != null && x >= 0 && y >= 0) {
      mediaPlayer.setLogoLocation(x, y);
    }
    if(position != null) {
      mediaPlayer.setLogoPosition(position);
    }
    if(file != null) {
      mediaPlayer.setLogoFile(file);
    }
    if(image != null) {
      mediaPlayer.setLogoImage(image);
    }
    if(enable) {
      mediaPlayer.enableLogo(true);
    }
  }
}
