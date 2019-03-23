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


package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_video_viewpoint_t;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_free;

/**
 * Viewpoint for 360 degree video.
 * <p>
 * Native viewpoint default field of view is 80 degrees.
 */
public final class Viewpoint {

    private final libvlc_video_viewpoint_t viewpoint;

    /**
     * Create a viewpoint.
     *
     * @param viewpoint native viewpoint instance
     */
    public Viewpoint(libvlc_video_viewpoint_t viewpoint) {
        this.viewpoint = viewpoint;
    }

    /**
     * Get the yaw.
     *
     * @return yaw, degrees
     */
    public float yaw() {
        return viewpoint.f_yaw;
    }

    /**
     * Set the yaw.
     *
     * @param yaw yaw, degrees
     */
    public void setYaw(float yaw) {
        viewpoint.f_yaw = yaw;
    }

    /**
     * Get the pitch.
     *
     * @return pitch
     * @return pitch, degrees
     */
    public float pitch() {
        return viewpoint.f_pitch;
    }

    /**
     * Set the pitch.
     *
     * @param pitch, degrees
     */
    public void setPitch(float pitch) {
        viewpoint.f_pitch = pitch;
    }

    /**
     * Get the roll.
     *
     * @return roll, degrees
     */
    public float roll() {
        return viewpoint.f_roll;
    }

    /**
     * Set the roll.
     *
     * @param roll roll, degrees
     */
    public void setRoll(float roll) {
        viewpoint.f_roll = roll;
    }

    /**
     * Get the field of view.
     *
     * @return field of view, degrees
     */
    public float fov() {
        return viewpoint.f_field_of_view;
    }

    /**
     * Set the field of view.
     *
     * @param fov field of view, degrees
     */
    public void setFov(float fov) {
        viewpoint.f_field_of_view = fov;
    }

    /**
     * Get the native viewpoint instance.
     *
     * @return viewpoint instance
     */
    public libvlc_video_viewpoint_t viewpoint() {
        return viewpoint;
    }

    /**
     * Release the component and associated native resources.
     * <p>
     * The component must no longer be used.
     */
    public void release() {
        libvlc_free(viewpoint.getPointer());
    }

}
