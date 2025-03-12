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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_track_t;
import uk.co.caprica.vlcj.media.Multiview;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.media.VideoOrientation;
import uk.co.caprica.vlcj.media.VideoProjection;

final public class VideoTrack extends Track {

    /**
     * Video width.
     */
    private final int width;

    /**
     * Video height.
     */
    private final int height;

    /**
     * Sample/pixel aspect ratio.
     */
    private final int sampleAspectRatio;

    /**
     * Sample/pixel aspect ratio base.
     */
    private final int sampleAspectRatioBase;

    /**
     * Frame rate.
     */
    private final int frameRate;

    /**
     * Frame rate base.
     */
    private final int frameRateBase;

    /**
     * Video orientation.
     */
    private final VideoOrientation orientation;

    /**
     * Video projection.
     */
    private final VideoProjection projection;

    /**
     * Yaw, degrees, for spherical video.
     */
    private final float yaw;

    /**
     * Pitch, degrees, for spherical video.
     */
    private final float pitch;

    /**
     * Roll, degrees, for spherical video.
     */
    private final float roll;

    /**
     * Field of View, degrees, for spherical video.
     */
    private final float fieldOfView;

    /**
     * Video multiview type.
     */
    private final Multiview multiview;

    VideoTrack(libvlc_media_track_t instance) {
        super(TrackType.VIDEO, instance);
        instance.u.setType(libvlc_video_track_t.class);
        instance.u.read();
        this.width = instance.u.video.i_width;
        this.height = instance.u.video.i_height;
        this.sampleAspectRatio = instance.u.video.i_sar_num;
        this.sampleAspectRatioBase = instance.u.video.i_sar_den;
        this.frameRate = instance.u.video.i_frame_rate_num;
        this.frameRateBase = instance.u.video.i_frame_rate_den;
        this.orientation = VideoOrientation.videoOrientation(instance.u.video.i_orientation);
        this.projection = VideoProjection.videoProjection(instance.u.video.i_projection);
        this.yaw = instance.u.video.pose.f_yaw;
        this.pitch = instance.u.video.pose.f_pitch;
        this.roll = instance.u.video.pose.f_roll;
        this.fieldOfView = instance.u.video.pose.f_field_of_view;
        this.multiview = Multiview.multiview(instance.u.video.i_multiview);
    }

    /**
     * Get the video width.
     *
     * @return width
     */
    public final int width() {
        return width;
    }

    /**
     * Get the video height.
     *
     * @return height
     */
    public final int height() {
        return height;
    }

    /**
     * Get the sample aspect ratio.
     *
     * @return sample aspect ratio
     */
    public final int sampleAspectRatio() {
        return sampleAspectRatio;
    }

    /**
     * Get the sample aspect ratio base.
     *
     * @return sample aspect ratio base
     */
    public final int sampleAspectRatioBase() {
        return sampleAspectRatioBase;
    }

    /**
     * Get the frame rate.
     *
     * @return frame rate
     */
    public final int frameRate() {
        return frameRate;
    }

    /**
     * Get the frame rate base.
     *
     * @return frame rate base
     */
    public final int frameRateBase() {
        return frameRateBase;
    }

    /**
     * Get the video orientation.
     *
     * @return video orientation
     */
    public final VideoOrientation orientation() {
        return orientation;
    }

    /**
     * Get the video projection.
     *
     * @return video projection
     */
    public final VideoProjection projection() {
        return projection;
    }

    /**
     * Get the yaw, for spherical video.
     *
     * @return yaw (degrees)
     */
    public final float yaw() {
        return yaw;
    }

    /**
     * Get the pitch, for spherical video.
     *
     * @return pitch (degrees)
     */
    public final float pitch() {
        return pitch;
    }

    /**
     * Get the roll, for spherical video.
     *
     * @return roll (degrees)
     */
    public final float roll() {
        return roll;
    }

    /**
     * Get the field of view, for spherical video.
     *
     * @return field of view (degrees)
     */
    public final float fieldOfView() {
        return fieldOfView;
    }

    /**
     * Get the video multiview type.
     *
     * @return multiview type
     */
    public final Multiview multiview() {
        return multiview;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("width=").append(width).append(',');
        sb.append("height=").append(height).append(',');
        sb.append("sampleAspectRatio=").append(sampleAspectRatio).append(',');
        sb.append("sampleAspectRatioBase=").append(sampleAspectRatioBase).append(',');
        sb.append("frameRate=").append(frameRate).append(',');
        sb.append("frameRateBase=").append(frameRateBase).append(',');
        sb.append("orientation=").append(orientation).append(',');
        sb.append("projection=").append(projection).append(',');
        sb.append("yaw=").append(yaw).append(',');
        sb.append("pitch=").append(pitch).append(',');
        sb.append("roll=").append(roll).append(',');
        sb.append("fieldOfView=").append(fieldOfView).append(',');
        sb.append("multiview=").append(multiview).append(']');
        return sb.toString();
    }
}
