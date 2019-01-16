package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

// FIXME really need to nail down and document who owns the MediaList and when/where it is released
// FIXME do i need to expose the medialistplayer somehow? i shouldn't really have a getter but i could set loop mode and the controls and so on?
//          controls() in here could return subitem controls used to access the media list player?

public final class SubItemService extends BaseService {

    /**
     * Media list player used to play the sub-items.
     */
    private final MediaListPlayer mediaListPlayer;

    /**
     * Flag whether or not to automatically play media sub-items if there are any.
     */
    private boolean playSubItems;

    SubItemService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);

        this.mediaListPlayer = new DefaultMediaListPlayer(libvlc, libvlcInstance);

        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);
    }

    /**
     * Set whether or not the media player should automatically play media sub-items.
     *
     * @param playSubItems <code>true</code> to automatically play sub-items, otherwise <code>false</code>
     */
    public void setPlaySubItems(boolean playSubItems) {
        this.playSubItems = playSubItems;
    }

    public boolean getPlaySubItems() {
        return this.playSubItems;
    }

    public MediaListPlayer player() {
        return mediaListPlayer;
    }

    /**
     *
     *
     * Set a new list for this media's sub-items - this component owns the list so must release it at the appropriate
     * time.
     *
     * @param media
     */
    void changeMedia(Media media) {
        releaseMediaList();

        if (playSubItems) {
            // Simply setting the media list on a media list player with attached media player will play the list
            mediaListPlayer.list().setMediaList(media.subitems().get());
        }
    }

    private void releaseMediaList() {
        MediaList oldList = mediaListPlayer.list().getMediaList();
        if (oldList != null) {
            oldList.release();
        }
    }

    @Override
    protected void release() {
        releaseMediaList();
        mediaListPlayer.release();
    }

}
