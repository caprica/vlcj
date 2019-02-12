package uk.co.caprica.vlcj.model;

// FIXME not sure about class name

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

public final class MediaRef {

    private final LibVlc libvlc;

    private final libvlc_media_t mediaInstance;

    public MediaRef(LibVlc libvlc, libvlc_media_t mediaInstance) {
        this.libvlc = libvlc;
        this.mediaInstance = mediaInstance;
    }

    public libvlc_media_t mediaInstance() {
        return mediaInstance;
    }

    public void release() {
        libvlc.libvlc_media_release(mediaInstance);
    }

}
