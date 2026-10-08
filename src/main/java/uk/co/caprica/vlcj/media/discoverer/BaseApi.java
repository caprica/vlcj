package uk.co.caprica.vlcj.media.discoverer;

/**
 * Internal base implementation.
 */
abstract class BaseApi {

    protected final MediaDiscoverer mediaDiscoverer;

    BaseApi(MediaDiscoverer mediaDiscoverer) {
        this.mediaDiscoverer = mediaDiscoverer;
    }

    protected void release() {
    }
}
