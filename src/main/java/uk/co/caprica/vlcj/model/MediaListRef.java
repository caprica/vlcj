package uk.co.caprica.vlcj.model;

// FIXME not sure about class name

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

public final class MediaListRef {

    private final LibVlc libvlc;

    private final libvlc_media_list_t mediaListInstance;

    public MediaListRef(LibVlc libvlc, libvlc_media_list_t mediaListInstance) {
        this.libvlc = libvlc;
        this.mediaListInstance = mediaListInstance;
    }

    public libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }

    public void release() {
        libvlc.libvlc_media_list_release(mediaListInstance);
    }

}
