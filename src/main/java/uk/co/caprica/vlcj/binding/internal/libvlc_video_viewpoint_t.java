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

package uk.co.caprica.vlcj.binding.internal;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class libvlc_video_viewpoint_t extends Structure {

    /**
     *
     */
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList("f_yaw", "f_pitch", "f_roll", "f_field_of_view", "f_zoom"));

    public static class ByValue extends libvlc_video_viewpoint_t implements Structure.ByValue {}

    public float f_yaw;           /** view point yaw in degrees */
    public float f_pitch;         /** view point pitch in degrees */
    public float f_roll;          /** view point roll in degrees */
    public float f_field_of_view; /** field of view in degrees (default 80.0f) */

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
