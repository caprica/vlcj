package uk.co.caprica.vlcj.model;

// FIXME not sure about class name

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.medialist.MediaList;

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

    /**
     *
     * <p>
     * The caller <em>must</em> release the returned {@link MediaList} when it has no further use for it.
     *
     * @return
     */
    public MediaList newMediaList() {
        libvlc.libvlc_media_list_retain(mediaListInstance);
        return new MediaList(libvlc, mediaListInstance);
    }

    public void release() {
        libvlc.libvlc_media_list_release(mediaListInstance);
    }

}
