package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;

abstract class BaseService {

    protected final MediaList mediaList;

    protected final LibVlc libvlc;

    protected final libvlc_media_list_t mediaListInstance;

    BaseService(MediaList mediaList) {
        this.mediaList = mediaList;
        this.libvlc = mediaList.libvlc;
        this.mediaListInstance = mediaList.mediaListInstance();
    }

    protected void release() {
    }

}
