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

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
     * Duration for the logo, milliseconds.
     */
    private Integer duration;

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
    private LogoPosition position;

    /**
     * Number of times to repeat the sequence of logos, or -1 for indefinite, or 0 for no looping.
     */
    private Integer repeat;

    /**
     * File name.
     * <p>
     * May include extended syntax, see {@link #file(String)}.
     */
    private List<String> files = new ArrayList<String>();

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
     * Set the amount of time the logo will be displayed before displaying the next in sequence (if there is one).
     *
     * @param duration duration, milliseconds
     * @return this
     */
    public Logo duration(int duration) {
        this.duration = duration;
        return this;
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
    public Logo position(LogoPosition position) {
        this.position = position;
        return this;
    }

    /**
     * Set the number of times the logo sequence will repeat.
     * <p>
     * Note that with current versions of VLC you may need to set a repeat count one more than you might expect - this
     * is because on the last loop iteration it appears to stop after only the first logo has been displayed.
     *
     * @param repeat number of times to repeat the sequence of logos, or -1 for indefinite, or 0 for no looping.
     * @return this
     */
    public Logo repeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    /**
     * Apply the logo file.
     * <p>
     * It is possible to simply specify the name of the file, or the extended syntax supported by
     * libvlc - e.g. "file,d,t;file,d,t;...", see {@link #file(String, Integer, Integer)}.
     *
     * @param file name of the file
     * @return this
     */
    public Logo file(String file) {
        return addFileSpec(file);
    }

    public Logo file(String file, Integer duration, Integer opacity) {
        return addFileSpec(String.format("%s,%s,%s", file, duration == null ? "" : duration, opacity == null ? "" : opacity));
    }

    /**
     * Apply the logo file.
     *
     * @param file logo file
     * @return this
     */
    public Logo file(File file) {
        return file(file.getAbsolutePath());
    }

    public Logo file(File file, Integer duration, Integer opacity) {
        return file(file.getAbsolutePath(), duration, opacity);
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
     * Get the duration for the logo.
     *
     * @return duration
     */
    public Integer getDuration() {
        return duration;
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
    public LogoPosition getPosition() {
        return position;
    }

    /**
     * Get the file name.
     *
     * @return file name
     */
    public String getFile() {
        return convertFileSpecs();
    }

    /**
     * Get the image.
     *
     * @return image
     */
    public RenderedImage getImage() {
        return image;
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
     * Apply the logo to the media player.
     * <p>
     * All previously applied properties will be set on the media player.
     *
     * @param mediaPlayer media player
     */
    public void apply(MediaPlayer mediaPlayer) {
        if (duration != null) {
            mediaPlayer.logo().setDuration(duration);
        }
        if (intOpacity != null) {
            mediaPlayer.logo().setOpacity(intOpacity);
        }
        if (floatOpacity != null) {
            mediaPlayer.logo().setOpacity(floatOpacity);
        }
        if (x != null && y != null && x >= 0 && y >= 0) {
            mediaPlayer.logo().setLocation(x, y);
        }
        if (position != null) {
            mediaPlayer.logo().setPosition(position);
        }
        if (repeat != null) {
            mediaPlayer.logo().setRepeat(repeat);
        }
        if (!files.isEmpty()) {
            mediaPlayer.logo().setFile(convertFileSpecs());
        }
        if (image != null) {
            mediaPlayer.logo().setImage(image);
        }
        if (enable) {
            mediaPlayer.logo().enable(true);
        }
    }

    private Logo addFileSpec(String fileSpec) {
        this.files.add(fileSpec);
        return this;
    }

    private String convertFileSpecs() {
        StringBuilder sb = new StringBuilder(files.size() * 40);
        for (String fileSpec : files) {
            sb.append(fileSpec).append(";");
        }
        if (sb.charAt(sb.length() - 1) == ';') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(getClass().getSimpleName()).append('[')
            .append("intOpacity=").append(intOpacity).append(',')
            .append("floatOpacity=").append(floatOpacity).append(',')
            .append("x=").append(x).append(',')
            .append("y=").append(y).append(',')
            .append("position=").append(position).append(',')
            .append("files=").append(files).append(',')
            .append("image=").append(image).append(',')
            .append("enable=").append(enable).append(']')
            .toString();
    }

}
