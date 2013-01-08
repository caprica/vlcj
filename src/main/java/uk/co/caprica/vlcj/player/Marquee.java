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

import java.awt.Color;

import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;

/**
 * Builder for a Marquee.
 * <p>
 * Use like this, with a static import of {@link #marquee()}:
 *
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

    /**
     * Text.
     */
    private String text;

    /**
     * Text colour.
     */
    private Color colour;

    /**
     * Text colour expressed as RGB components.
     */
    private Integer rgb;

    /**
     * Opacity expressed as an integer, 0 to 255, where 255 is fully opaque.
     */
    private Integer intOpacity;

    /**
     * Opacity expressed as a fraction, 0.0 to 1.0, where 1.0 is fully opaque.
     */
    private Float floatOpacity;

    /**
     * Text size.
     */
    private Integer size;

    /**
     * Timeout, in milliseconds.
     * <p>
     * The marquee will be removed after this timeout has expired.
     */
    private Integer timeout;

    /**
     * Text X position, in video co-ordinates.
     */
    private Integer x;

    /**
     * Text Y position, in video co-ordinates.
     */
    private Integer y;

    /**
     * Predefined text position.
     */
    private libvlc_marquee_position_e position;

    /**
     * Enabled/disabled state.
     */
    private boolean enable;

    /**
     * Create a marquee.
     *
     * @return marquee
     */
    public static Marquee marquee() {
        return new Marquee();
    }

    /**
     * Private constructor prevents direct instantiation by others.
     */
    private Marquee() {
    }

    /**
     * Apply the text.
     * <p>
     * Format variables are available:
     * <pre>
     * Time related:
     *  %Y = year
     *  %d = day
     *  %H = hour
     *  %M = minute
     *  %S = second
     * </pre>
     * Meta data related:
     * <pre>
     *  $a = artist
     *  $b = album
     *  $c = copyright
     *  $d = description
     *  $e = encoded by
     *  $g = genre
     *  $l = language
     *  $n = track num
     *  $p = now playing
     *  $r = rating
     *  $s = subtitles language
     *  $t = title
     *  $u = url
     *  $A = date
     *  $B = audio bitrate (in kb/s)
     *  $C = chapter
     *  $D = duration
     *  $F = full name with path
     *  $I = title
     *  $L = time left
     *  $N = name
     *  $O = audio language
     *  $P = position (in %)
     *  $R = rate
     *  $S = audio sample rate (in kHz)
     *  $T = time
     *  $U = publisher
     *  $V = volume
     *  $_ = new line
     * </pre>
     * See <code>http://wiki.videolan.org/index.php?title=Documentation:Modules/marq</code>.
     *
     * @param text text
     * @return this
     */
    public Marquee text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Apply the text colour.
     *
     * @param colour text colour
     * @return this
     */
    public Marquee colour(Color colour) {
        this.colour = colour;
        return this;
    }

    /**
     * Apply the text colour as RGB components.
     *
     * @param rgb integer encoded red, green, blue colour components
     * @return this
     */
    public Marquee colour(int rgb) {
        this.rgb = rgb;
        return this;
    }

    /**
     * Apply the text opacity.
     *
     * @param opacity opacity, 0 to 255, where 255 is fully opaque
     * @return this
     */
    public Marquee opacity(int opacity) {
        this.intOpacity = opacity;
        return this;
    }

    /**
     * Apply the text opacity.
     *
     * @param opacity opacity, 0.0 to 1.0, where 1.0 is fully opaque
     * @return this
     */
    public Marquee opacity(float opacity) {
        this.floatOpacity = opacity;
        return this;
    }

    /**
     * Apply the text size.
     *
     * @param size text size
     * @return this
     */
    public Marquee size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Apply the timeout.
     * <p>
     * The marquee will be removed when the timeout expires.
     *
     * @param timeout timeout, in milliseconds
     * @return this
     */
    public Marquee timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Apply the text location in video co-ordinates.
     *
     * @param x x ordinate
     * @param y y ordinate
     * @return this
     */
    public Marquee location(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Apply the text position.
     *
     * @param position predefined text position
     * @return this
     */
    public Marquee position(libvlc_marquee_position_e position) {
        this.position = position;
        return this;
    }

    /**
     * Apply the initial enabled/disabled state.
     *
     * @param enable <code>true</code> to enable the marquee; <code>false</code> to disable it
     * @return this
     */
    public Marquee enable(boolean enable) {
        this.enable = enable;
        return this;
    }

    /**
     * Enable the marquee.
     *
     * @return this
     */
    public Marquee enable() {
        this.enable = true;
        return this;
    }

    /**
     * Disable the marquee.
     *
     * @return this
     */
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
