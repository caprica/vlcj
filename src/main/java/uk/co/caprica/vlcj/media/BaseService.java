package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

abstract class BaseService {

    protected final Media media;

    protected final LibVlc libvlc;

    protected final libvlc_media_t mediaInstance;

    BaseService(Media media) {
        this.media = media;
        this.libvlc = media.libvlc;
        this.mediaInstance = media.mediaInstance();
    }

    protected void release() {
    }

}
