package uk.co.caprica.vlcj.media;

// FIXME replace enum with Java one

import uk.co.caprica.vlcj.binding.internal.libvlc_media_slave_type_t;
import uk.co.caprica.vlcj.player.MediaResourceLocator;

import java.io.File;
import java.util.List;

public class SlaveService extends BaseService {

    SlaveService(Media media) {
        super(media);
    }

    public boolean add(libvlc_media_slave_type_t type, int priority, String uri) {
        uri = MediaResourceLocator.encodeMrl(uri); // FIXME check if needed, maybe factor out it its own thing, since this usage is not specifically an MRL
        return libvlc.libvlc_media_slaves_add(mediaInstance, type.intValue(), priority, uri) == 0;
    }

    public boolean add(libvlc_media_slave_type_t type, int priority, File file) {
        String uri = String.format("file://%s", file.getAbsolutePath());
        return libvlc.libvlc_media_slaves_add(mediaInstance, type.intValue(), priority, uri) == 0;
    }

    public void clear() {
        libvlc.libvlc_media_slaves_clear(mediaInstance);
    }

    public List<MediaSlave> get() {
        return MediaSlaves.getMediaSlaves(libvlc, mediaInstance);
    }

}
