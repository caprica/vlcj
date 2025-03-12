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

package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;

/**
 * Media statistics.
 */
public class MediaStatistics {

    private int   inputBytesRead;
    private float inputBitrate;
    private int   demuxBytesRead;
    private float demuxBitrate;
    private int   demuxCorrupted;
    private int   demuxDiscontinuity;
    private int   decodedVideo;
    private int   decodedAudio;
    private int   picturesDisplayed;
    private int   picturesLate;
    private int   picturesLost;
    private int   audioBuffersPlayed;
    private int   audioBuffersLost;

    public MediaStatistics() {
    }

    public MediaStatistics(int inputBytesRead, float inputBitrate, int demuxBytesRead, float demuxBitrate, int demuxCorrupted, int demuxDiscontinuity, int decodedVideo, int decodedAudio, int picturesDisplayed, int picturesLate, int picturesLost, int audioBuffersPlayed, int audioBuffersLost) {
        this.inputBytesRead     = inputBytesRead;
        this.inputBitrate       = inputBitrate;
        this.demuxBytesRead     = demuxBytesRead;
        this.demuxBitrate       = demuxBitrate;
        this.demuxCorrupted     = demuxCorrupted;
        this.demuxDiscontinuity = demuxDiscontinuity;
        this.decodedVideo       = decodedVideo;
        this.decodedAudio       = decodedAudio;
        this.picturesDisplayed  = picturesDisplayed;
        this.picturesLate       = picturesLate;
        this.picturesLost       = picturesLost;
        this.audioBuffersPlayed = audioBuffersPlayed;
        this.audioBuffersLost   = audioBuffersLost;
    }

    public int inputBytesRead() {
        return inputBytesRead;
    }

    public float inputBitrate() {
        return inputBitrate;
    }

    public int demuxBytesRead() {
        return demuxBytesRead;
    }

    public float demuxBitrate() {
        return demuxBitrate;
    }

    public int demuxCorrupted() {
        return demuxCorrupted;
    }

    public int demuxDiscontinuity() {
        return demuxDiscontinuity;
    }

    public int decodedVideo() {
        return decodedVideo;
    }

    public int decodedAudio() {
        return decodedAudio;
    }

    public int picturesDisplayed() {
        return picturesDisplayed;
    }

    public int picturesLate() {
        return picturesLate;
    }

    public int picturesLost() {
        return picturesLost;
    }

    public int audioBuffersPlayed() {
        return audioBuffersPlayed;
    }

    public int audioBuffersLost() {
        return audioBuffersLost;
    }

    public final void apply(libvlc_media_stats_t stats) {
        this.inputBytesRead     = stats.i_read_bytes;
        this.inputBitrate       = stats.f_input_bitrate;
        this.demuxBytesRead     = stats.i_demux_read_bytes;
        this.demuxBitrate       = stats.f_demux_bitrate;
        this.demuxCorrupted     = stats.i_demux_corrupted;
        this.demuxDiscontinuity = stats.i_demux_discontinuity;
        this.decodedVideo       = stats.i_decoded_video;
        this.decodedAudio       = stats.i_decoded_audio;
        this.picturesDisplayed  = stats.i_displayed_pictures;
        this.picturesLate       = stats.i_late_pictures;
        this.picturesLost       = stats.i_lost_pictures;
        this.audioBuffersPlayed = stats.i_played_abuffers;
        this.audioBuffersLost   = stats.i_lost_abuffers;
    }
    
}
