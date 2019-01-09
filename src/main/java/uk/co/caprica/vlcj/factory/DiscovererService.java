package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.player.discoverer.MediaDiscoverer;

public final class DiscovererService extends BaseService {

    DiscovererService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new native media service discoverer.
     * <p>
     * Not all media discoverers are supported on all platforms.
     *
     * @param name name of the required service discoverer, e.g. "audio", "video".
     * @return native media discoverer component
     */
    public MediaDiscoverer newMediaDiscoverer(String name) {
        return new MediaDiscoverer(libvlc, instance, name);
    }

    /**
     * Create a new native audio media service discoverer.
     * <p>
     * This method is simply a convenient wrapper around {@link #newMediaDiscoverer(String)}.
     *
     * @return native media discoverer component
     */
    public MediaDiscoverer newAudioMediaDiscoverer() {
        return newMediaDiscoverer("audio");
    }

    /**
     * Create a new native video media service discoverer.
     * <p>
     * This should return for example video capture devices currently attached to
     * the system.
     * <p>
     * This method is simply a convenient wrapper around {@link #newMediaDiscoverer(String)}.
     * <p>
     * The video discoverer may not be available on all platforms.
     *
     * @return native media discoverer component
     */
    public MediaDiscoverer newVideoMediaDiscoverer() {
        return newMediaDiscoverer("video");
    }

}
