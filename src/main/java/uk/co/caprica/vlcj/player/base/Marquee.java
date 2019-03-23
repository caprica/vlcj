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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import java.awt.*;

/**
 * Builder for a Marquee.
 * <p>
 * Use like this, with a static import of {@link #marquee()}:
 *
 * <pre>
 *   marquee()
 *     .opacity(200)
 *     .position(MarqueePosition.BOTTOM)
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
    private MarqueePosition position;

    /**
     * Amount of time before the marquee text is refreshed.
     */
    private Integer refresh;

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
     * See <code>http://wiki.videolan.org/index.php?title=Documentation:Modules/marq</code>.
     * <p>
     * If you want to use new-lines in the marquee text make sure you use the "\r\n" escape sequence - "\n" on its own
     * will not work.
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
    public Marquee position(MarqueePosition position) {
        this.position = position;
        return this;
    }

    /**
     * Set the time before the marquee text will refresh.
     *
     * @param refresh refresh time, milliseconds
     * @return this
     */
    public Marquee refresh(int refresh) {
        this.refresh = refresh;
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
     * Get the text.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the colour if set as Color.
     *
     * @return colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Get the colour if set as RGB.
     *
     * @return RGB
     */
    public Integer getRgb() {
        return rgb;
    }

    /**
     * Get the opacity if set as integer.
     *
     * @return opacity
     */
    public Integer getIntegerOpacity() {
        return intOpacity;
    }

    /**
     * Get the opacity if set as float.
     *
     * @return opacity
     */
    public Float getFloatOpacity() {
        return floatOpacity;
    }

    /**
     * Get the text size.
     *
     * @return text size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Get the timeout.
     *
     * @return timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Get the location x co-ordinate.
     *
     * @return location x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Get the location y co-ordinate.
     *
     * @return location y
     */
    public Integer getY() {
        return y;
    }

    /**
     * Get the position.
     *
     * @return position
     */
    public MarqueePosition getPosition() {
        return position;
    }

    /**
     * Get the text refresh.
     *
     * @return refresh time, milliseconds
     */
    public Integer getRefresh() {
        return refresh;
    }

    /**
     * Get the enabled flag.
     *
     * @return enabled/disabled
     */
    public boolean getEnable() {
        return enable;
    }

    /**
     * Apply the marquee to the media player.
     *
     * @param mediaPlayer media player
     */
    public void apply(MediaPlayer mediaPlayer) {
        if (text != null) {
            mediaPlayer.marquee().setText(text);
        }
        if (colour != null) {
            mediaPlayer.marquee().setColour(colour);
        }
        if (rgb != null) {
            mediaPlayer.marquee().setColour(rgb);
        }
        if (intOpacity != null) {
            mediaPlayer.marquee().setOpacity(intOpacity);
        }
        if (floatOpacity != null) {
            mediaPlayer.marquee().setOpacity(floatOpacity);
        }
        if (size != null) {
            mediaPlayer.marquee().setSize(size);
        }
        if (timeout != null) {
            mediaPlayer.marquee().setTimeout(timeout);
        }
        if (x != null && y != null && x >= 0 && y >= 0) {
            mediaPlayer.marquee().setLocation(x, y);
        }
        if (position != null) {
            mediaPlayer.marquee().setPosition(position);
        }
        if (refresh != null) {
            mediaPlayer.marquee().setRefresh(refresh);
        }
        if (enable) {
            mediaPlayer.marquee().enable(true);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(getClass().getSimpleName()).append('[')
            .append("text=").append(text).append(',')
            .append("colour=").append(colour).append(',')
            .append("rgb=").append(rgb).append(',')
            .append("intOpacity=").append(intOpacity).append(',')
            .append("floatOpacity=").append(floatOpacity).append(',')
            .append("size=").append(size).append(',')
            .append("timeout=").append(timeout).append(',')
            .append("x=").append(x).append(',')
            .append("y=").append(y).append(',')
            .append("position=").append(position).append(',')
            .append("refresh=").append(refresh).append(',')
            .append("enable=").append(enable).append(']')
            .toString();
    }
}
