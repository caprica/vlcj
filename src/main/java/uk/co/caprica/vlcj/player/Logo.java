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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import java.awt.image.RenderedImage;
import java.io.File;

import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;

/**
 * Builder for a Logo.
 * <p>
 * Use like this, with a static import of {@link #logo()}:
 *
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

    /**
     * Opacity expressed as an integer, 0 to 255, where 255 is fully opaque.
     */
    private Integer intOpacity;

    /**
     * Opacity expressed as a fraction, 0.0 to 1.0, where 1.0 is fully opaque.
     */
    private Float floatOpacity;

    /**
     * X position, in video co-ordinates.
     */
    private Integer x;

    /**
     * Y position, in video co-ordinates.
     */
    private Integer y;

    /**
     * Predefined logo position.
     */
    private libvlc_logo_position_e position;

    /**
     * File name.
     * <p>
     * May include extended syntax, see {@link #file(String)}.
     */
    private String file;

    /**
     * Logo image.
     */
    private RenderedImage image;

    /**
     * Enabled/disabled state.
     */
    private boolean enable;

    /**
     * Create a logo.
     *
     * @return logo
     */
    public static Logo logo() {
        return new Logo();
    }

    /**
     * Private constructor prevents direct instantiation by others.
     */
    private Logo() {
    }

    /**
     * Apply a logo opacity.
     *
     * @param opacity opacity, from 0 to 255, where 255 is fully opaque
     * @return this
     */
    public Logo opacity(int opacity) {
        this.intOpacity = opacity;
        return this;
    }

    /**
     * Apply a logo opacity.
     *
     * @param opacity opacity, from 0.0 to 1.0, where 1.0 is fully opaque
     * @return this
     */
    public Logo opacity(float opacity) {
        this.floatOpacity = opacity;
        return this;
    }

    /**
     * Apply the logo position in video co-ordinates.
     *
     * @param x x ordinate
     * @param y y ordinate
     * @return this
     */
    public Logo location(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Apply the logo position.
     *
     * @param position position enumeration value
     * @return this
     */
    public Logo position(libvlc_logo_position_e position) {
        this.position = position;
        return this;
    }

    /**
     * Apply the logo file.
     * <p>
     * It is possible to simply specify the name of the file, or the extended syntax supported by
     * libvlc - e.g. "file,d,t;file,d,t;...", see
     * {@link libvlc_video_logo_option_t#libvlc_logo_file}.
     *
     * @param file name of the file
     * @return this
     */
    public Logo file(String file) {
        this.file = file;
        return this;
    }

    /**
     * Apply the logo file.
     *
     * @param file logo file
     * @return this
     */
    public Logo file(File file) {
        this.file = file.getAbsolutePath();
        return this;
    }

    /**
     * Apply the logo image.
     * <p>
     * This is not optimal as the image must first be written to disk in a temporary file.
     *
     * @param image logo image
     * @return this
     */
    public Logo image(RenderedImage image) {
        this.image = image;
        return this;
    }

    /**
     * Apply the initial enabled/disabled state.
     *
     * @param enable <code>true</code> to enable the logo; <code>false</code> to disable it
     * @return this
     */
    public Logo enable(boolean enable) {
        this.enable = enable;
        return this;
    }

    /**
     * Enable the logo.
     *
     * @return this
     */
    public Logo enable() {
        this.enable = true;
        return this;
    }

    /**
     * Disable the logo.
     *
     * @return this
     */
    public Logo disable() {
        this.enable = false;
        return this;
    }

    /**
     * Apply the logo to the media player.
     * <p>
     * All previously applied properties will be set on the media player.
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
