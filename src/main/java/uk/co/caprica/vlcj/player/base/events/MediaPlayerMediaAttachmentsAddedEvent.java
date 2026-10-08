package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.parser.Pictures;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

final class MediaPlayerMediaAttachmentsAddedEvent extends MediaPlayerEvent {

    private final Pictures pictures;

    MediaPlayerMediaAttachmentsAddedEvent(MediaPlayer mediaPlayer, Pictures pictures) {
        super(mediaPlayer);
        this.pictures = pictures;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.mediaAttachmentsAdded(mediaPlayer, pictures);
    }
}
