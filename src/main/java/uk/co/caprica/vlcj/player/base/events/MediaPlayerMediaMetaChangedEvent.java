package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

final class MediaPlayerMediaMetaChangedEvent extends MediaPlayerEvent {

    private final MetaData metaData;

    MediaPlayerMediaMetaChangedEvent(MediaPlayer mediaPlayer, MetaData metaData) {
        super(mediaPlayer);
        this.metaData = metaData;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.mediaMetaChanged(mediaPlayer, metaData);
    }
}
