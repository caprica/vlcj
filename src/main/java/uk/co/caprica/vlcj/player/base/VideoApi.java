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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import com.sun.jna.ptr.IntByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_viewpoint_t;
import uk.co.caprica.vlcj.binding.support.strings.NativeString;
import uk.co.caprica.vlcj.media.VideoProjection;

import java.awt.Dimension;
import java.awt.Point;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_video_title_display;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_adjust_float;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_adjust_int;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_aspect_ratio;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_cursor;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_display_fit;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_scale;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_size;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_get_video_stereo_mode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_new_viewpoint;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_adjust_float;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_adjust_int;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_aspect_ratio;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_crop_border;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_crop_ratio;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_crop_window;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_deinterlace;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_display_fit;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_projection_mode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_scale;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_set_video_stereo_mode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_unset_projection_mode;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_video_update_viewpoint;
import static uk.co.caprica.vlcj.player.base.VideoFitMode.videoFitMode;
import static uk.co.caprica.vlcj.player.base.VideoStereoMode.videoStereoMode;

/**
 * Behaviour pertaining to media player video.
 */
public final class VideoApi extends BaseApi {

    VideoApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set the de-interlace filter to use.
     *
     * @param deinterlace deinterlace value: -1 for auto (default); 0 disabled; 1 enabled
     * @param deinterlaceMode mode, or null to disable the de-interlace filter
     */
    public void setDeinterlace(int deinterlace, DeinterlaceMode deinterlaceMode) {
        libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlace, deinterlaceMode != null ? deinterlaceMode.stringValue() : null);
    }

    /**
     * Enable/disable the video adjustments.
     * <p>
     * The video adjustment controls must be enabled after the video has started playing.
     *
     * @param adjustVideo true if the video adjustments are enabled, otherwise false
     */
    public void setAdjustVideo(boolean adjustVideo) {
        libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue(), adjustVideo ? 1 : 0);
    }

    /**
     * Test whether or not the video adjustments are enabled.
     *
     * @return true if the video adjustments are enabled, otherwise false
     */
    public boolean isAdjustVideo() {
        return libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue()) == 1;
    }

    /**
     * Get the current video contrast.
     *
     * @return contrast, in the range from 0.0 to 2.0
     */
    public float contrast() {
        return libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
    }

    /**
     * Set the video contrast.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param contrast contrast value, in the range from 0.0 to 2.0
     */
    public void setContrast(float contrast) {
        libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue(), contrast);
    }

    /**
     * Get the current video brightness.
     *
     * @return brightness, in the range from 0.0 to 2.0
     */
    public float brightness() {
        return libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
    }

    /**
     * Set the video brightness.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     * <p>
     *
     * @param brightness brightness value, in the range from 0.0 to 2.0
     */
    public void setBrightness(float brightness) {
        libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue(), brightness);
    }

    /**
     * Get the current video hue.
     *
     * @return hue, in the range from -180.0 to 180.0
     */
    public float hue() {
        return libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
    }

    /**
     * Set the video hue.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param hue hue value, in the range from -180.0 to 180.0
     */
    public void setHue(float hue) {
        libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue(), hue);
    }

    /**
     * Get the current video saturation.
     *
     * @return saturation, in the range from 0.0 to 3.0
     */
    public float saturation() {
        return libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
    }

    /**
     * Set the video saturation.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param saturation saturation value, in the range from 0.0 to 3.0
     */
    public void setSaturation(float saturation) {
        libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
    }

    /**
     * Get the current video gamma.
     *
     * @return gamma value, in the range from 0.01 to 10.0
     */
    public float gamma() {
        return libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
    }

    /**
     * Set the video gamma.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     * <p>
     * Changing gamma may not be supported by all video outputs, e.g. vdpau
     *
     * @param gamma gamma, in the range from 0.01 to 10.0
     */
    public void setGamma(float gamma) {
        libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
    }

    /**
     * Set the video projection mode.
     * <p>
     * This changes how the source is rendered for 360-degree video playback.
     *
     * @param projectionMode new projection mode
     */
    public void setProjectionMode(VideoProjection projectionMode) {
        libvlc_video_set_projection_mode(mediaPlayerInstance, projectionMode.intValue());
    }

    /**
     * Unset the previously set projection mode.
     */
    public void unsetProjectionMode() {
        libvlc_video_unset_projection_mode(mediaPlayerInstance);
    }

    /**
     * Set if, and how, the video title will be shown when playing media.
     *
     * @param position position, {@link Position#DISABLE} to prevent the title from appearing
     * @param timeout time to display the title in milliseconds (ignored when the title is disabled)
     */
    public void setVideoTitleDisplay(Position position, int timeout) {
        libvlc_media_player_set_video_title_display(mediaPlayerInstance, position.intValue(), timeout);
    }

    /**
     * Get the video aspect ratio.
     *
     * @return aspect ratio, e.g. "16:9", "4:3", "185:100" for 1:85.1 and so on
     */
    public String aspectRatio() {
        return NativeString.copyAndFreeNativeString(libvlc_video_get_aspect_ratio(mediaPlayerInstance));
    }

    /**
     * Set the video aspect ratio
     *
     * @param aspectRatio aspect ratio, e.g. "16:9", "4:3", "185:100" for 1:85.1 and so on
     */
    public void setAspectRatio(String aspectRatio) {
        libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
    }

    /**
     * Get the video fit mode.
     *
     * @return fit mode
     */
    public VideoFitMode displayFit() {
        return videoFitMode(libvlc_video_get_display_fit(mediaPlayerInstance));
    }

    /**
     * Set the video fit mode.
     *
     * @param fitMode fit mode
     */
    public void setDisplayFit(VideoFitMode fitMode) {
        libvlc_video_set_display_fit(mediaPlayerInstance, fitMode.intValue());
    }

    /**
     * Get the current video scale (zoom).
     *
     * @return scale
     */
    public float scale() {
        return libvlc_video_get_scale(mediaPlayerInstance);
    }

    /**
     * Set the video scaling factor.
     *
     * @param factor scaling factor, or zero to scale the video the size of the container
     */
    public void setScale(float factor) {
        libvlc_video_set_scale(mediaPlayerInstance, factor);
    }

    /**
     * Set the video crop ratio.
     * <p>
     * For example:
     * <pre>
     * mediaPlayer.setCropGeometry(16, 9);
     * </pre>
     * To disable the crop, set the denominator to zero.
     *
     * @param num numerator for the crop ratio
     * @param den denominator for the crop ratio
     */
    public void setCropRatio(int num, int den) {
        libvlc_video_set_crop_ratio(mediaPlayerInstance, num, den);
    }

    /**
     * Set the video crop window.
     * <p>
     * Pixels outside the crop window will not be shown.
     * <p>
     * For example:
     * <pre>
     * mediaPlayer.setCropWindow(100, 100, 600, 400);
     * </pre>
     * To disable the crop window, use {@link #setCropRatio(int, int)} or {@link #setCropBorder(int, int, int, int)}.
     *
     * @param x window x
     * @param y window y
     * @param width window width
     * @param height window height
     */
    public void setCropWindow(int x, int y, int width, int height) {
        if (width != 0 && height != 0) {
            libvlc_video_set_crop_window(mediaPlayerInstance, x, y, width, height);
        } else {
            throw new IllegalArgumentException("Width and height must be non-zero");
        }
    }

    /**
     * Set the video crop border.
     * <p>
     * Pixels outside the crop window will not be shown.
     * <p>
     * For example:
     * <pre>
     * mediaPlayer.setCropWindow(100, 100, 600, 400);
     * </pre>
     * To unset the border, set all values to zero.
     *
     * @param left number of columns to crop on the left
     * @param top number of rows to crop on the top
     * @param right number of columns to crop on the right
     * @param bottom number ofrows to corp on the bottom
     */
    public void setCropBorder(int left, int top, int right, int bottom) {
        libvlc_video_set_crop_border(mediaPlayerInstance, left, right, top, bottom);
    }

    /**
     * Get the video size.
     * <p>
     * The video dimensions are not available until after the video has started playing and a video
     * output has been created.
     *
     * @return video size if available, or <code>null</code>
     */
    public Dimension videoDimension() {
        IntByReference px = new IntByReference();
        IntByReference py = new IntByReference();
        int result = libvlc_video_get_size(mediaPlayerInstance, 0, px, py);
        if(result == 0) {
            return new Dimension(px.getValue(), py.getValue());
        }
        else {
            return null;
        }
    }

    /**
     * Create a new viewpoint instance for 360 degree video.
     * <p>
     * The caller <em>must</em> release the returned instance when it no longer has a use for it
     *
     * @return viewpoint, or <code>null</code> on error
     */
    public Viewpoint newViewpoint() {
        libvlc_video_viewpoint_t viewpoint = libvlc_video_new_viewpoint();
        if (viewpoint != null) {
            return new Viewpoint(viewpoint);
        } else {
            return null;
        }
    }

    /**
     * Update the viewpoint for 360 degree video.
     *
     * @param viewpoint new viewpoint
     * @param absolute <code>true</code> if viewpoint contains absolute values; <code>false</code> if they are relative
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean updateViewpoint(Viewpoint viewpoint, boolean absolute) {
        return libvlc_video_update_viewpoint(mediaPlayerInstance, viewpoint.viewpoint(), absolute ? 1 : 0) == 0;
    }

    /**
     * Get the video stereo mode.
     *
     * @return video stereo mode
     */
    public VideoStereoMode getVideoStereoMode() {
        return videoStereoMode(libvlc_video_get_video_stereo_mode(mediaPlayerInstance));
    }

    /**
     * Set the video stereo mode.
     * <p>
     * A video output must have been created, {@link MediaPlayerEventListener#videoOutput(MediaPlayer, int)}, before
     * setting the video stereo mode.
     *
     * @param videoStereoMode video stereo mode
     */
    public void setVideoStereoMode(VideoStereoMode videoStereoMode) {
        libvlc_video_set_video_stereo_mode(mediaPlayerInstance, videoStereoMode.intValue());
    }

    /**
     * Get the pointer location, in terms of video resolution/coordinates, for the first video.
     *
     * @return cursor location, or <code>null</code> if not available
     */
    public Point getCursor() {
        return getCursor(0);
    }

    /**
     * Get the pointer location, in terms of video resolution/coordinates, for the specified video.
     *
     * @param videoNum video number, starting from zero
     * @return cursor location, or <code>null</code> if not available
     */
    public Point getCursor(int videoNum) {
        IntByReference px = new IntByReference();
        IntByReference py = new IntByReference();
        int result = libvlc_video_get_cursor(mediaPlayerInstance, videoNum, px.getPointer(), py.getPointer());
        if (result == 0) {
            return new Point(px.getValue(), py.getValue());
        } else {
            return null;
        }
    }
}
