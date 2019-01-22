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

    public float getZoom() {
        return viewpoint.f_zoom;
    }

    public void setZoom(float zoom) {
        viewpoint.f_zoom = zoom;
    }

    public libvlc_video_viewpoint_t viewpoint() {
        return viewpoint;
    }

    public void release() {
        libvlc.libvlc_free(viewpoint.getPointer());
    }

}
