package uk.co.caprica.vlcj.media.discoverer.events;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.discoverer.MediaDiscoverer;

/**
 * A factory that creates a media player event instance for a native media discoverer event.
 */
public final class MediaDiscovererEventFactory {

    private MediaDiscovererEventFactory() {
    }

    public static MediaDiscovererEvent createMediaAddedEvent(MediaDiscoverer mediaDiscoverer, MediaRef media) {
        return new MediaDiscovererMediaAddedEvent(mediaDiscoverer, media);
    }

    public static MediaDiscovererEvent createMediaRemovedEvent(MediaDiscoverer mediaDiscoverer, MediaRef media) {
        return new MediaDiscovererMediaRemovedEvent(mediaDiscoverer, media);
    }
}
