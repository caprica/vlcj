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


package uk.co.caprica.vlcj.model;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_viewpoint_t;

// FIXME this may not be final public API

public final class Viewpoint {

    private final LibVlc libvlc;

    private final libvlc_video_viewpoint_t viewpoint;

    public Viewpoint(LibVlc libvlc, libvlc_video_viewpoint_t viewpoint) {
        this.libvlc = libvlc;
        this.viewpoint = viewpoint;
    }

    public float getYaw() {
        return viewpoint.f_yaw;
    }

    public void setYaw(float yaw) {
        viewpoint.f_yaw = yaw;
    }

    public float getPitch() {
        return viewpoint.f_pitch;
    }

    public void setPitch(float pitch) {
        viewpoint.f_pitch = pitch;
    }

    public float getRoll() {
        return viewpoint.f_roll;
    }

    public void setRoll(float roll) {
        viewpoint.f_roll = roll;
    }

    public float getFov() {
        return viewpoint.f_field_of_view;
    }

    public void setFov(float fov) {
        viewpoint.f_field_of_view = fov;
    }

    public libvlc_video_viewpoint_t viewpoint() {
        return viewpoint;
    }

    public void release() {
        libvlc.libvlc_free(viewpoint.getPointer());
    }

}
