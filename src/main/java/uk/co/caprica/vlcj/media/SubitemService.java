package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.medialist.MediaList;

public class SubitemService extends BaseService {

    SubitemService(Media media) {
        super(media);
    }

    public MediaList get() {
        libvlc_media_list_t list = libvlc.libvlc_media_subitems(mediaInstance);
        if (list != null) {
            return new MediaList(libvlc, list);
        } else {
            return null;
        }
    }

}
