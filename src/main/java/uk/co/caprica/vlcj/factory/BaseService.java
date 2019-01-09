package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;

abstract class BaseService {

    protected final MediaPlayerFactory factory;

    protected final LibVlc libvlc;

    protected final libvlc_instance_t instance;

    BaseService(MediaPlayerFactory factory) {
        this.factory  = factory;
        this.libvlc   = factory.libvlc();
        this.instance = factory.instance();
    }

}
