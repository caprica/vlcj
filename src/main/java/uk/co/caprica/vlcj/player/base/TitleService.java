package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.TitleDescription;

import java.util.List;

public final class TitleService extends BaseService {

    TitleService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the number of titles.
     *
     * @return number of titles, or -1 if none
     */
    public int getTitleCount() {
        return libvlc.libvlc_media_player_get_title_count(mediaPlayerInstance);
    }

    /**
     * Get the current title.
     *
     * @return title number
     */
    public int getTitle() {
        return libvlc.libvlc_media_player_get_title(mediaPlayerInstance);
    }

    /**
     * Set a new title to play.
     *
     * @param title title number
     */
    public void setTitle(int title) {
        libvlc.libvlc_media_player_set_title(mediaPlayerInstance, title);
    }

    /**
     * Get the title descriptions.
     * <p>
     * The media must be playing before this information is available.
     *
     * @return list of descriptions, may be empty but will never be <code>null</code>
     */
    public List<TitleDescription> getTitleDescriptions() {
        return Descriptions.titleDescriptions(libvlc, mediaPlayerInstance);
    }

}
