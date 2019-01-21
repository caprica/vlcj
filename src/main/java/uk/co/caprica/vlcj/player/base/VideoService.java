package uk.co.caprica.vlcj.player.base;

import com.sun.jna.ptr.IntByReference;
import uk.co.caprica.vlcj.enums.Position;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_viewpoint_t;
import uk.co.caprica.vlcj.player.DeinterlaceMode;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.Viewpoint;

import java.awt.*;

public final class VideoService extends BaseService {

    VideoService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set the de-interlace filter to use.
     *
     * @param deinterlaceMode mode, or null to disable the de-interlace filter
     */
    public void setDeinterlace(DeinterlaceMode deinterlaceMode) {
        libvlc.libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlaceMode != null ? deinterlaceMode.mode() : null);
    }

    /**
     * Enable/disable the video adjustments.
     * <p>
     * The video adjustment controls must be enabled after the video has started playing.
     *
     * @param adjustVideo true if the video adjustments are enabled, otherwise false
     */
    public void setAdjustVideo(boolean adjustVideo) {
        libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue(), adjustVideo ? 1 : 0);
    }

    /**
     * Test whether or not the video adjustments are enabled.
     *
     * @return true if the video adjustments are enabled, otherwise false
     */
    public boolean isAdjustVideo() {
        return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue()) == 1;
    }

    /**
     * Get the current video contrast.
     *
     * @return contrast, in the range from 0.0 to 2.0
     */
    public float getContrast() {
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
    }

    /**
     * Set the video contrast.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param contrast contrast value, in the range from 0.0 to 2.0
     */
    public void setContrast(float contrast) {
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue(), contrast);
    }

    /**
     * Get the current video brightness.
     *
     * @return brightness, in the range from 0.0 to 2.0
     */
    public float getBrightness() {
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
    }

    /**
     * Set the video brightness.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     * <p>
     * <strong>Requires vlc 1.1.1 or later.</strong>
     *
     * @param brightness brightness value, in the range from 0.0 to 2.0
     */
    public void setBrightness(float brightness) {
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue(), brightness);
    }

    /**
     * Get the current video hue.
     *
     * @return hue, in the range from -180.0 to 180.0
     */
    public float getHue() {
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
    }

    /**
     * Set the video hue.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param hue hue value, in the range from -180.0 to 180.0
     */
    public void setHue(float hue) {
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue(), hue);
    }

    /**
     * Get the current video saturation.
     *
     * @return saturation, in the range from 0.0 to 3.0
     */
    public float getSaturation() {
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
    }

    /**
     * Set the video saturation.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     *
     * @param saturation saturation value, in the range from 0.0 to 3.0
     */
    public void setSaturation(float saturation) {
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
    }

    /**
     * Get the current video gamma.
     * <p>
     * <strong>Requires vlc 1.1.1 or later.</strong>
     *
     * @return gamma value, in the range from 0.01 to 10.0
     */
    public float getGamma() {
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
    }

    /**
     * Set the video gamma.
     * <p>
     * Video adjustments must be enabled for this to have any effect.
     * <p>
     * <strong>Requires vlc 1.1.1 or later.</strong>
     * <p>
     * Changing gamma may not be supported by all video outputs, e.g. vdpau
     *
     * @param gamma gamma, in the range from 0.01 to 10.0
     */
    public void setGamma(float gamma) {
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
    }

    /**
     * Set if, and how, the video title will be shown when playing media.
     *
     * @param position position, {@link Position#DISABLE} to prevent the title from appearing
     * @param timeout time to display the title in milliseconds (ignored when the title is disabled)
     */
    public void setVideoTitleDisplay(Position position, int timeout) {
        libvlc.libvlc_media_player_set_video_title_display(mediaPlayerInstance, position.intValue(), timeout);
    }

    /**
     * Get the video aspect ratio.
     *
     * @return aspect ratio
     */
    public String getAspectRatio() {
        return NativeString.copyAndFreeNativeString(libvlc, libvlc.libvlc_video_get_aspect_ratio(mediaPlayerInstance));
    }

    /**
     * Set the video aspect ratio
     *
     * @param aspectRatio aspect ratio, e.g. "16:9", "4:3", "185:100" for 1:85.1 and so on
     */
    public void setAspectRatio(String aspectRatio) {
        libvlc.libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
    }

    /**
     * Get the current video scale (zoom).
     *
     * @return scale
     */
    public float getScale() {
        return libvlc.libvlc_video_get_scale(mediaPlayerInstance);
    }

    /**
     * Set the video scaling factor.
     *
     * @param factor scaling factor, or zero to scale the video the size of the container
     */
    public void setScale(float factor) {
        libvlc.libvlc_video_set_scale(mediaPlayerInstance, factor);
    }

    /**
     * Get the current video crop geometry.
     *
     * @return crop geometry
     */
    public String getCropGeometry() {
        return NativeString.copyAndFreeNativeString(libvlc, libvlc.libvlc_video_get_crop_geometry(mediaPlayerInstance));
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
        libvlc.libvlc_video_set_crop_geometry(mediaPlayerInstance, cropGeometry);
    }

    /**
     * Get the video size.
     * <p>
     * The video dimensions are not available until after the video has started playing and a video
     * output has been created.
     *
     * @return video size if available, or <code>null</code>
     */
    public Dimension getVideoDimension() {
        IntByReference px = new IntByReference();
        IntByReference py = new IntByReference();
        int result = libvlc.libvlc_video_get_size(mediaPlayerInstance, 0, px, py);
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
    public int getVideoTrackCount() {
        return libvlc.libvlc_video_get_track_count(mediaPlayerInstance);
    }

    /**
     * Get the current video track.
     *
     * @return track identifier, see {@link #getVideoDescriptions()}
     */
    public int getVideoTrack() {
        return libvlc.libvlc_video_get_track(mediaPlayerInstance);
    }

    /**
     * Set a new video track to play.
     * <p>
     * The track identifier must be one of those returned by {@link #getVideoDescriptions()}.
     * <p>
     * Video can be disabled by passing here the identifier of the track with a description of
     * "Disable".
     * <p>
     * There is no guarantee that the available track identifiers go in sequence from zero up to
     * {@link #getVideoTrackCount()}-1. The {@link #getVideoDescriptions()} method should always
     * be used to ascertain the available track identifiers.
     *
     * @param track track identifier
     * @return current video track identifier
     */
    public int setVideoTrack(int track) {
        libvlc.libvlc_video_set_track(mediaPlayerInstance, track);
        return getVideoTrack(); // FIXME does this actually update synchronously?
    }

    /**
     *
     *
     * @return
     */
    public Viewpoint newViewpoint() {
        libvlc_video_viewpoint_t viewpoint = libvlc.libvlc_video_new_viewpoint();
        if (viewpoint != null) {
            return new Viewpoint(libvlc, viewpoint);
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @param viewpoint
     * @param absolute
     * @return
     */
    public boolean updateViewpoint(Viewpoint viewpoint, boolean absolute) {
        return libvlc.libvlc_video_update_viewpoint(mediaPlayerInstance, viewpoint.viewpoint(), absolute ? 1 : 0) == 0;
    }

    /**
     * Get the video (i.e. "title") track descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    // FIXME rename trackDescrptions()? or videoTrackDescriptions()?
    public java.util.List<TrackDescription> getVideoDescriptions() {
        return Descriptions.videoTrackDescriptions(libvlc, mediaPlayerInstance);
    }

}
