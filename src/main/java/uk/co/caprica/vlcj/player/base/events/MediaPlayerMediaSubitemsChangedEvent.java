package uk.co.caprica.vlcj.player.base.events;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

final class MediaPlayerMediaSubitemsChangedEvent extends MediaPlayerEvent {

    private final MediaList mediaList;

    MediaPlayerMediaSubitemsChangedEvent(MediaPlayer mediaPlayer, MediaList mediaList) {
        super(mediaPlayer);
        this.mediaList = mediaList;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.mediaSubitemsChanged(mediaPlayer, mediaList);
    }
}
