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

package uk.co.caprica.vlcj.binding.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Structure;

/**
 *
 */
public class libvlc_media_stats_t extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
        "i_read_bytes", "f_input_bitrate",
        "i_demux_read_bytes", "f_demux_bitrate", "i_demux_corrupted", "i_demux_discontinuity",
        "i_decoded_video", "i_decoded_audio",
        "i_displayed_pictures", "i_lost_pictures",
        "i_played_abuffers", "i_lost_abuffers",
        "i_sent_packets", "i_sent_bytes", "f_send_bitrate")
    );

    /* Input */
    public int         i_read_bytes;
    public float       f_input_bitrate;

    /* Demux */
    public int         i_demux_read_bytes;
    public float       f_demux_bitrate;
    public int         i_demux_corrupted;
    public int         i_demux_discontinuity;

    /* Decoders */
    public int         i_decoded_video;
    public int         i_decoded_audio;

    /* Video Output */
    public int         i_displayed_pictures;
    public int         i_lost_pictures;

    /* Audio output */
    public int         i_played_abuffers;
    public int         i_lost_abuffers;

    /* Stream output */
    public int         i_sent_packets;
    public int         i_sent_bytes;
    public float       f_send_bitrate;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
