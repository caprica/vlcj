package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.player.base.Capabilties;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

final class MediaPlayerCapabilitiesChangedEvent extends MediaPlayerEvent {

    private final Capabilties oldCapabilities;

    private final Capabilties newCapabilities;

    public MediaPlayerCapabilitiesChangedEvent(MediaPlayer mediaPlayer, Capabilties oldCapabilties, Capabilties newCapabilties) {
        super(mediaPlayer);
        this.oldCapabilities = oldCapabilties;
        this.newCapabilities = newCapabilties;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.capabilitiesChanged(mediaPlayer, oldCapabilities, newCapabilities);
    }
}
