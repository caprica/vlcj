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

import uk.co.caprica.vlcj.enums.MarqueePosition;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;
import uk.co.caprica.vlcj.model.Marquee;

import java.awt.*;

public final class MarqueeService extends BaseService {

    MarqueeService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Enable/disable the marquee.
     * <p>
     * The marquee will not be enabled if there is currently no video being played.
     *
     * @param enable <code>true</code> to show the marquee, <code>false</code> to hide it
     */
    public void enableMarquee(boolean enable) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
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
    public void setMarqueeText(String text) {
        libvlc.libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
    }

    /**
     * Set the marquee colour.
     *
     * @param colour colour, any alpha component will be masked off
     */
    public void setMarqueeColour(Color colour) {
        setMarqueeColour(colour.getRGB() & 0x00ffffff);
    }

    /**
     * Set the marquee colour.
     *
     * @param colour RGB colour value
     */
    public void setMarqueeColour(int colour) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
    }

    /**
     * Set the marquee opacity.
     *
     * @param opacity opacity in the range 0 to 100 where 255 is fully opaque
     */
    public void setMarqueeOpacity(int opacity) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
    }

    /**
     * Set the marquee opacity.
     *
     * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
     */
    public void setMarqueeOpacity(float opacity) {
        int opacityValue = Math.round(opacity * 255.0f);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
    }

    /**
     * Set the marquee size.
     *
     * @param size size, height of the marquee text in pixels
     */
    public void setMarqueeSize(int size) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
    }

    /**
     * Set the marquee timeout.
     *
     * @param timeout timeout, in milliseconds
     */
    public void setMarqueeTimeout(int timeout) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
    }

    /**
     * Set the marquee location.
     *
     * @param x x co-ordinate for the top left of the marquee
     * @param y y co-ordinate for the top left of the marquee
     */
    public void setMarqueeLocation(int x, int y) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_X.intValue(), x);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Y.intValue(), y);
    }

    /**
     * Set the marquee position.
     *
     * @param position position
     */
    public void setMarqueePosition(MarqueePosition position) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Position.intValue(), position.intValue());
    }

    /**
     *
     *
     * @param refresh
     */
    public void setMarqueeRefresh(int refresh) {
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Refresh.intValue(), refresh);
    }

    /**
     * Set a marquee.
     *
     * @param marquee marquee
     */
    public void setMarquee(Marquee marquee) {
        marquee.apply(mediaPlayer);
    }

}
