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

package uk.co.caprica.vlcj.media;

/**
 * Video track info.
 */
public final class VideoTrackInfo extends TrackInfo {

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
    private final float fov;

    /**
     * Video multiview type.
     */
    private final Multiview multiview;

    /**
     * Create a new video track info.
     *
     * @param codec video codec
     * @param originalCodec original video codec
     * @param id track id
     * @param profile profile
     * @param level level
     * @param bitRate bit-rate
     * @param language language
     * @param description description
     * @param width width
     * @param height height
     * @param sampleAspectRatio sample aspect ratio
     * @param sampleAspectRatioBase sample aspect ratio base
     * @param frameRate frame rate
     * @param frameRateBase frame rate base
     * @param orientation video orientation
     * @param projection video projection
     * @param yaw yaw (degrees)
     * @param pitch pitch (degrees)
     * @param roll roll (degrees)
     * @param fov field of view (degrees)
     * @param multiview multiview video type
     * @param codecDescription codec description
     */
    public VideoTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, int width, int height, int sampleAspectRatio, int sampleAspectRatioBase, int frameRate, int frameRateBase, VideoOrientation orientation, VideoProjection projection, float yaw, float pitch, float roll, float fov, Multiview multiview, String codecDescription) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description, codecDescription);
        this.width = width;
        this.height = height;
        this.sampleAspectRatio = sampleAspectRatio;
        this.sampleAspectRatioBase = sampleAspectRatioBase;
        this.frameRate = frameRate;
        this.frameRateBase = frameRateBase;
        this.orientation = orientation;
        this.projection = projection;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.fov = fov;
        this.multiview = multiview;
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
    public final float fov() {
        return fov;
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
        sb.append("fov=").append(fov).append(',');
        sb.append("multiview=").append(multiview).append(']');
        return sb.toString();
    }
}
