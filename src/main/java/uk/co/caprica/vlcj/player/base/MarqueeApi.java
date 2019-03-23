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

import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;

import java.awt.*;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_marquee_int;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_marquee_string;

/**
 * Behaviour pertaining to the marquee.
 */
public final class MarqueeApi extends BaseApi {

    MarqueeApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Enable/disable the marquee.
     * <p>
     * The marquee will not be enabled if there is currently no video being played.
     *
     * @param enable <code>true</code> to show the marquee, <code>false</code> to hide it
     */
    public void enable(boolean enable) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
    }

    /**
     * Set the marquee text.
     * <p>
     * Format variables are available, some examples:
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
     * You can use any format specifiers used by the strftime C function ("man strftime" for more information).
     * <p>
     * If you want to use new-lines in the marquee text make sure you use the "\r\n" escape sequence - "\n" on its own
     * will not work.
     *
     * @param text text
     */
    public void setText(String text) {
        libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
    }

    /**
     * Set the marquee colour.
     *
     * @param colour colour, any alpha component will be masked off
     */
    public void setColour(Color colour) {
        setColour(colour.getRGB() & 0x00ffffff);
    }

    /**
     * Set the marquee colour.
     *
     * @param colour RGB colour value
     */
    public void setColour(int colour) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
    }

    /**
     * Set the marquee opacity.
     *
     * @param opacity opacity in the range 0 to 100 where 255 is fully opaque
     */
    public void setOpacity(int opacity) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
    }

    /**
     * Set the marquee opacity.
     *
     * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
     */
    public void setOpacity(float opacity) {
        int opacityValue = Math.round(opacity * 255.0f);
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
    }

    /**
     * Set the marquee size.
     *
     * @param size size, height of the marquee text in pixels
     */
    public void setSize(int size) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
    }

    /**
     * Set the marquee timeout.
     *
     * @param timeout timeout, in milliseconds
     */
    public void setTimeout(int timeout) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
    }

    /**
     * Set the marquee location.
     *
     * @param x x co-ordinate for the top left of the marquee
     * @param y y co-ordinate for the top left of the marquee
     */
    public void setLocation(int x, int y) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_X.intValue(), x);
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Y.intValue(), y);
    }

    /**
     * Set the marquee position.
     *
     * @param position position
     */
    public void setPosition(MarqueePosition position) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Position.intValue(), position.intValue());
    }

    /**
     * Set the delay before refreshing the marquee text.
     *
     * @param refresh refresh delay, milliseconds
     */
    public void setRefresh(int refresh) {
        libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Refresh.intValue(), refresh);
    }

    /**
     * Set a marquee from a builder.
     *
     * @param marquee marquee builder
     */
    public void set(Marquee marquee) {
        marquee.apply(mediaPlayer);
    }

}
