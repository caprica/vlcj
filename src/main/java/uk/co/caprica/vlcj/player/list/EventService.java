package uk.co.caprica.vlcj.player.list;

public final class EventService extends BaseService {

    private final MediaListPlayerNativeEventManager eventManager;

    EventService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);

        this.eventManager = new MediaListPlayerNativeEventManager(libvlc, mediaListPlayer);
    }

    /**
     * Add a component to be notified of media player events.
     *
     * @param listener component to notify
     */
    public void addMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        eventManager.addEventListener(listener);
    }

    /**
     * Remove a component that was previously interested in notifications of media player events.
     *
     * @param listener component to stop notifying
     */
    public void removeMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
