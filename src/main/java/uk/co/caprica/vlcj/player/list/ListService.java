package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.medialist.MediaList;

public final class ListService extends BaseService {

    private MediaList mediaList;

    ListService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set the media list (i.e. the "play" list).
     * <p>
     * The caller still "owns" the {@link MediaList} and is responsible for invoke {@link MediaList#release()} at the
     * appropriate time.
     *
     * @param mediaList media list
     */
    public void setMediaList(MediaList mediaList) {
        libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaList.mediaListInstance());
        this.mediaList = mediaList;
    }

    /**
     * Get the media list.
     *
     * @return media list
     */
    public MediaList getMediaList() {
        return mediaList;
    }

}
