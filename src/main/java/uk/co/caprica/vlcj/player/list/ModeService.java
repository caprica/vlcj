package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.enums.PlaybackMode;

public final class ModeService extends BaseService {

    ModeService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set the media list play mode.
     * <p>
     * Note that if setting the play mode to {@link PlaybackMode#REPEAT} you can not simply play the media list,
     * you must instead play a particular item (by its index).
     *
     * @param mode mode
     */
    public boolean setMode(PlaybackMode mode) {
        if (mode != null) {
            libvlc.libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, mode.intValue());
            return true;
        } else {
            return false;
        }
    }

}
