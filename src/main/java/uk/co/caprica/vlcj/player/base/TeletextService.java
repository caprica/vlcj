package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.TeletextKey;

public final class TeletextService extends BaseService {

    TeletextService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the current teletext page.
     *
     * @return page number
     */
    public int getTeletextPage() {
        return libvlc.libvlc_video_get_teletext(mediaPlayerInstance);
    }

    /**
     * Set the new teletext page to retrieve.
     *
     * @param pageNumber page number
     */
    public void setTeletextPage(int pageNumber) {
        libvlc.libvlc_video_set_teletext(mediaPlayerInstance, pageNumber);
    }

    /**
     * Set ("press") a teletext key.
     *
     * @param key teletext key
     */
    public void setTeletextKey(TeletextKey key) {
        libvlc.libvlc_video_set_teletext(mediaPlayerInstance, key.intValue());
    }

}
