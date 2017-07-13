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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.binding.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Structure;

/**
 *
 */
public class libvlc_video_track_t extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("i_height", "i_width", "i_sar_num", "i_sar_den", "i_frame_rate_num", "i_frame_rate_den"));

    public static class ByValue extends libvlc_video_track_t implements Structure.ByValue {}

    public int i_height;
    public int i_width;

    public int i_sar_num;
    public int i_sar_den;
    public int i_frame_rate_num;
    public int i_frame_rate_den;

    public int i_orientation;
    public int i_projection;
    public float f_yaw_degrees;    /* Viewpoint yaw in degrees -180 to 180  */
    public float f_pitch_degrees;  /* Viewpoint pitch in degrees -90 to 90  */
    public float f_roll_degrees;   /* Viewpoint roll in degrees -180 to 180 */
    public float f_fov_degrees;    /* Viewpoint fov in degrees 0 to 180     */

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
