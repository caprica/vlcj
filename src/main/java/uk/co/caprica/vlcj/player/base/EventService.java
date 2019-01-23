package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.events.standard.MediaPlayerEvent;

public final class EventService extends BaseService {

    private final MediaPlayerNativeEventManager eventManager;

    EventService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);

        eventManager = new MediaPlayerNativeEventManager(libvlc, mediaPlayer);

        // Add event handlers used for internal implementation
        addMediaPlayerEventListener(new RepeatPlayEventHandler      ());
        addMediaPlayerEventListener(new MediaPlayerReadyEventHandler());
    }

    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.addEventListener(listener);
    }

    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        eventManager.removeEventListener(listener);
    }

    void raiseEvent(MediaPlayerEvent event) {
        eventManager.raiseEvent(event);
    }

    @Override
    protected void release() {
        eventManager.release();
    }

}
