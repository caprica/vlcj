package uk.co.caprica.vlcj.model;

// FIXME not sure about class name

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.Media;

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

    /**
     * Return a new {@link Media} component for this {@link MediaRef}.
     * <p>
     * The returned media component shares the native media instance with any others that may be created subsequently.
     * <p>
     * The caller <em>must</em> release the returned media when it is of no further use.
     *
     * @return
     */
    public Media newMedia() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new Media(libvlc, mediaInstance);
    }

    public MediaRef newMediaRef() {
        libvlc.libvlc_media_retain(mediaInstance);
        return new MediaRef(libvlc, mediaInstance);
    }

    public void release() {
        libvlc.libvlc_media_release(mediaInstance);
    }

}
