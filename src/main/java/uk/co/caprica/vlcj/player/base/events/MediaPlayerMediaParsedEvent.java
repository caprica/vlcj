package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

final class MediaPlayerMediaParsedEvent extends MediaPlayerEvent {

    private final MediaRef mediaRef;

    MediaPlayerMediaParsedEvent(MediaPlayer mediaPlayer, MediaRef mediaRef) {
        super(mediaPlayer);
        this.mediaRef = mediaRef;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.mediaParsed(mediaPlayer, mediaRef);
    }
}
