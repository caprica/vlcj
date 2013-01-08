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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Media meta data.
 */
public class MediaDetails implements Serializable {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Number of titles.
     */
    private int titleCount;

    /**
     * Number of video tracks.
     */
    private int videoTrackCount;

    /**
     * Number of audio tracks.
     */
    private int audioTrackCount;

    /**
     * Number of sub-picture/sub-title tracks.
     */
    private int spuCount;

    /**
     * Collection of title descriptions.
     */
    private List<TrackDescription> titleDescriptions;

    /**
     * Collection of video track descriptions.
     */
    private List<TrackDescription> videoDescriptions;

    /**
     * Collection of audio track descriptions.
     */
    private List<TrackDescription> audioDescriptions;

    /**
     * Collection of sub-title track descriptions.
     */
    private List<TrackDescription> spuDescriptions;

    /**
     * Collection of chapter descriptions for each title.
     */
    private List<List<String>> chapterDescriptions = new ArrayList<List<String>>();

    public int getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(int titleCount) {
        this.titleCount = titleCount;
    }

    public int getVideoTrackCount() {
        return videoTrackCount;
    }

    public void setVideoTrackCount(int videoTrackCount) {
        this.videoTrackCount = videoTrackCount;
    }

    public int getAudioTrackCount() {
        return audioTrackCount;
    }

    public void setAudioTrackCount(int audioTrackCount) {
        this.audioTrackCount = audioTrackCount;
    }

    public int getSpuCount() {
        return spuCount;
    }

    public void setSpuCount(int spuCount) {
        this.spuCount = spuCount;
    }

    public List<TrackDescription> getTitleDescriptions() {
        return titleDescriptions;
    }

    public void setTitleDescriptions(List<TrackDescription> titleDescriptions) {
        this.titleDescriptions = titleDescriptions;
    }

    public List<TrackDescription> getVideoDescriptions() {
        return videoDescriptions;
    }

    public void setVideoDescriptions(List<TrackDescription> videoDescriptions) {
        this.videoDescriptions = videoDescriptions;
    }

    public List<TrackDescription> getAudioDescriptions() {
        return audioDescriptions;
    }

    public void setAudioDescriptions(List<TrackDescription> audioDescriptions) {
        this.audioDescriptions = audioDescriptions;
    }

    public List<TrackDescription> getSpuDescriptions() {
        return spuDescriptions;
    }

    public void setSpuDescriptions(List<TrackDescription> spuDescriptions) {
        this.spuDescriptions = spuDescriptions;
    }

    public List<List<String>> getChapterDescriptions() {
        return chapterDescriptions;
    }

    public void setChapterDescriptions(List<List<String>> chapterDescriptions) {
        this.chapterDescriptions = chapterDescriptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("titleCount=").append(titleCount).append(',');
        sb.append("videoTrackCount=").append(videoTrackCount).append(',');
        sb.append("audioTrackCount=").append(audioTrackCount).append(',');
        sb.append("spuCount=").append(spuCount).append(',');
        sb.append("titleDescriptions=").append(titleDescriptions).append(',');
        sb.append("videoDescriptions=").append(videoDescriptions).append(',');
        sb.append("audioDescriptions=").append(audioDescriptions).append(',');
        sb.append("spuDescriptions=").append(spuDescriptions).append(',');
        sb.append("chapterDescriptions=").append(chapterDescriptions).append(']');
        return sb.toString();
    }
}
