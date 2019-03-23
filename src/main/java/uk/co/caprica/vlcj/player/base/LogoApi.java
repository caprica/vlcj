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

import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_logo_int;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_logo_string;

/**
 * Behaviour pertaining to the logo.
 */
public final class LogoApi extends BaseApi {

    LogoApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Enable/disable the logo.
     * <p>
     * The logo will not be enabled if there is currently no video being played.
     *
     * @param enable <code>true</code> to show the logo, <code>false</code> to hide it
     */
    public void enable(boolean enable) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
    }

    /**
     * Set the time that the logo will be displayed.
     * <p>
     * After this time passes, the logo will be removed and the next one (if there is one) displayed.
     *
     * @param duration duration, milliseconds
     */
    public void setDuration(int duration) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_delay.intValue(), duration);
    }

    /**
     * Set the logo opacity.
     *
     * @param opacity opacity in the range 0 to 255 where 255 is fully opaque
     */
    public void setOpacity(int opacity) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
    }

    /**
     * Set the logo opacity.
     *
     * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
     */
    public void setOpacity(float opacity) {
        int opacityValue = Math.round(opacity * 255.0f);
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
    }

    /**
     * Set the logo location.
     *
     * @param x x co-ordinate for the top left of the logo
     * @param y y co-ordinate for the top left of the logo
     */
    public void setLocation(int x, int y) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
    }

    /**
     * Set the logo position.
     *
     * @param position position
     */
    public void setPosition(LogoPosition position) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    }

    /**
     * Set how many times the logo sequence should repeat.
     * <p>
     * Note that with current versions of VLC you may need to set a repeat count one more than you might expect - this
     * is because on the last loop iteration it appears to stop after only the first logo has been displayed.
     *
     * @param repeat number of times to repeat the logos, or -1 for indefinite, or 0 to disable looping
     */
    public void setRepeat(int repeat) {
        libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_repeat.intValue(), repeat);
    }

    /**
     * Set the logo file.
     * <p>
     * It is possible to set multiple logo files here, each with their own optional delay and opacity.
     * <p>
     * The format of the string is:
     * <pre>
     *     filename1[,delay1[,opacity1];filename2[,delay2[,opacity2];filename3[,delay3[,opacity3];
     * </pre>
     * Since the delay and opacity values are optional, simply leave them out but make sure to include the expected
     * number of commas.
     * <p>
     * When an optional value is not present, a default will be used as per {@link #setOpacity(int)} amd
     * {@link #setDuration(int)}.
     * <p>
     * In addition, {@link #setRepeat(int)} can be used to loop the sequence of logos.
     *
     * @param logoFile logo file name
     */
    public void setFile(String logoFile) {
        libvlc_video_set_logo_string(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_file.intValue(), logoFile);
    }

    /**
     * Set the logo image.
     * <p>
     * The image will first be written to a temporary file, before invoking {@link #setFile(String)}. This is not
     * optimal, but creating a temporary file for the logo in this way is unavoidable.
     * <p>
     * The temporary file will persist until the JVM exits. The file can not be deleted immediately due to the
     * asynchronous nature of the native API call that sets the logo from the file.
     * <p>
     * There are circumstances under which this temporary file may <em>fail</em> to be deleted, as per
     * {@link File#deleteOnExit()} - i.e. "Deletion will be attempted only for normal termination of the virtual
     * machine".
     *
     * @param logoImage logo image
     */
    public void setImage(RenderedImage logoImage) {
        File file = null;
        try {
            // Create a temporary file for the logo...
            file = File.createTempFile("vlcj-logo-", ".png");
            ImageIO.write(logoImage, "png", file);
            if (file.exists()) {
                // ...then set the logo as normal
                setFile(file.getAbsolutePath());
                // Flag the temporary file to be deleted when the JVM exits - the file can not be
                // deleted immediately because setLogoFile ultimately invokes an asynchronous
                // native method to set the logo from the file
                file.deleteOnExit();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to set logo image", e);
        }
    }

    /**
     * Set a logo from a builder.
     *
     * @param logo logo builder
     */
    public void set(Logo logo) {
        logo.apply(mediaPlayer);
    }

}
