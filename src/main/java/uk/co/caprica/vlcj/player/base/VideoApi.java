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

import com.sun.jna.ptr.IntByReference;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_viewpoint_t;

import java.awt.*;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_video_title_display;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_adjust_float;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_adjust_int;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_aspect_ratio;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_crop_geometry;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_scale;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_size;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_track;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_get_track_count;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_new_viewpoint;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_adjust_float;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_adjust_int;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_aspect_ratio;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_crop_geometry;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_deinterlace;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_scale;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_set_track;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_video_update_viewpoint;

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
     * @param deinterlaceMode mode, or null to disable the de-interlace filter
     */
    public void setDeinterlace(DeinterlaceMode deinterlaceMode) {
        libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlaceMode != null ? deinterlaceMode.stringValue() : null);
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
     * @return aspect ratio
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
     * Get the current video crop geometry.
     *
     * @return crop geometry
     */
    public String cropGeometry() {
        return NativeString.copyAndFreeNativeString(libvlc_video_get_crop_geometry(mediaPlayerInstance));
    }

    /**
     * Set the crop geometry.
     * <p>
     * The format for the crop geometry is one of:
     * <ul>
     * <li>numerator:denominator</li>
     * <li>widthxheight+x+y</li>
     * <li>left:top:right:bottom</li>
     * </ul>
     * For example:
     * <pre>
     * mediaPlayer.setCropGeometry(&quot;4:3&quot;);         // W:H
     * mediaPlayer.setCropGeometry(&quot;719x575+0+0&quot;); // WxH+L+T
     * mediaPlayer.setCropGeometry(&quot;6+10+6+10&quot;);   // L+T+R+B
     * </pre>
     *
     * @param cropGeometry formatted string describing the desired crop geometry
     */
    public void setCropGeometry(String cropGeometry) {
        libvlc_video_set_crop_geometry(mediaPlayerInstance, cropGeometry);
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
     * Get the number of available video tracks.
     *
     * @return number of tracks
     */
    public int trackCount() {
        return libvlc_video_get_track_count(mediaPlayerInstance);
    }

    /**
     * Get the current video track.
     *
     * @return track identifier, see {@link #trackDescriptions()}
     */
    public int track() {
        return libvlc_video_get_track(mediaPlayerInstance);
    }

    /**
     * Set a new video track to play.
     * <p>
     * The track identifier must be one of those returned by {@link #trackDescriptions()}.
     * <p>
     * Video can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available track identifiers go in sequence from zero up to
     * {@link #trackCount()}-1. The {@link #trackDescriptions()} method should always
     * be used to ascertain the available track identifiers.
     *
     * @param track track identifier
     * @return current video track identifier
     */
    public int setTrack(int track) {
        libvlc_video_set_track(mediaPlayerInstance, track);
        return track();
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
     * Get the video (i.e. "title") track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public List<TrackDescription> trackDescriptions() {
        return Descriptions.videoTrackDescriptions(mediaPlayerInstance);
    }

}
