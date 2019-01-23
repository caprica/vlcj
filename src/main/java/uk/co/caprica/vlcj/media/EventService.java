package uk.co.caprica.vlcj.media;

import uk.co.caprica.vlcj.player.events.media.MediaEventListener;

public class EventService extends BaseService {

    private final MediaNativeEventManager eventManager;

    EventService(Media media) {
        super(media);

        this.eventManager = new MediaNativeEventManager(libvlc, media);
    }

    /**
     * Add a component to be notified of media events.
     *
     * @param listener component to notify
     */
    public void addMediaEventListener(MediaEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaEventListener(MediaEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
