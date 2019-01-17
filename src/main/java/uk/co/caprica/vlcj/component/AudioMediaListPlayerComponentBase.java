package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventListener;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

abstract class AudioMediaListPlayerComponentBase extends AudioMediaPlayerComponent implements MediaListPlayerEventListener, MediaListEventListener {

    protected AudioMediaListPlayerComponentBase(MediaPlayerFactory mediaPlayerFactory) {
        super(mediaPlayerFactory);
    }

    // === MediaListPlayerEventListener =========================================

    @Override
    public void mediaListPlayerFinished(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item) {
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
    }

    // === MediaListEventListener ===============================================

    @Override
    public void mediaListWillAddItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemAdded(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListWillDeleteItem(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListItemDeleted(MediaList mediaList, libvlc_media_t mediaInstance, int index) {
    }

    @Override
    public void mediaListEndReached(MediaList mediaList) {
    }

}
