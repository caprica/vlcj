package uk.co.caprica.vlcj.medialist;

public class EventService extends BaseService {

    private final MediaListNativeEventManager eventManager;

    EventService(MediaList mediaList) {
        super(mediaList);

        this.eventManager = new MediaListNativeEventManager(libvlc, mediaList);
    }

    /**
     * Add a component to be notified of media events.
     *
     * @param listener component to notify
     */
    public void addMediaListEventListener(MediaListEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaListEventListener(MediaListEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
