package uk.co.caprica.vlcj.discoverer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.medialist.MediaList;

// FIXME not too happy with having to pass LibVlc down through the listener
//FIXME in this case it looks like events should be managed via the meidalist

// The native API doc implies the event handler has to call HOLD if it wants to use the item, but the API doc also
// says the item is valid until you get the same pointer in a deleted callback
// so i think you can probably choose to hold at any time TBH
// we will need to expose the item so it can be set on a mediaplayer

public class MediaDiscoverer {

    private final LibVlc libvlc;

    private final libvlc_media_discoverer_t discoverer;

    private final MediaList mediaList;

    public MediaDiscoverer(LibVlc libvlc, libvlc_instance_t libvlcInstance, libvlc_media_discoverer_t discoverer) {
        this.libvlc = libvlc;
        this.discoverer = discoverer;
        this.mediaList = new MediaList(libvlc, libvlcInstance, libvlc.libvlc_media_discoverer_media_list(discoverer));
    }

    public boolean start() {
        return libvlc.libvlc_media_discoverer_start(discoverer) == 0;
    }

    public void stop() {
        libvlc.libvlc_media_discoverer_stop(discoverer);
    }

    public boolean isRunning() {
        return libvlc.libvlc_media_discoverer_is_running(discoverer) != 0;
    }

    public MediaList mediaList() {
        return mediaList;
    }

    public void release() {
        libvlc.libvlc_media_discoverer_release(discoverer);
    }

}
