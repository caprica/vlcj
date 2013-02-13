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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

/**
 * Video track info.
 */
public class VideoTrackInfo extends TrackInfo {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

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
     * @param sampleAspectRatio
     * @param sampleAspectRatioBase
     * @param frameRate
     * @param frameRateBase
     */
    protected VideoTrackInfo(int codec, int originalCodec, int id, int profile, int level, int bitRate, String language, String description, int width, int height, int sampleAspectRatio, int sampleAspectRatioBase, int frameRate, int frameRateBase) {
        super(codec, originalCodec, id, profile, level, bitRate, language, description);
        this.width = width;
        this.height = height;
        this.sampleAspectRatio = sampleAspectRatio;
        this.sampleAspectRatioBase = sampleAspectRatioBase;
        this.frameRate = frameRate;
        this.frameRateBase = frameRateBase;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("width=").append(width).append(',');
        sb.append("height=").append(height).append(',');
        sb.append("sampleAspectRatio=").append(sampleAspectRatio).append(',');
        sb.append("sampleAspectRatioBase=").append(sampleAspectRatioBase).append(',');
        sb.append("frameRate=").append(frameRate).append(',');
        sb.append("frameRateBase=").append(frameRateBase).append(']');
        return sb.toString();
    }
}
