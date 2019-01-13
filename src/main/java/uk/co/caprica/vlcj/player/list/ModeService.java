package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_playback_mode_e;

public final class ModeService extends BaseService {

    ModeService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Set the media list play mode.
     * <p>
     * Note that if setting the play mode to {@link MediaListPlayerMode#REPEAT} you can not simply play the media list,
     * you must instead play a particular item (by its index).
     *
     * @param mode mode
     */
    public boolean setMode(MediaListPlayerMode mode) {
        libvlc_playback_mode_e playbackMode;
        switch(mode) {
            case DEFAULT:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_default;
                break;

            case LOOP:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_loop;
                break;

            case REPEAT:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_repeat;
                break;

            default:
                playbackMode = null;
                break;
        }
        if (playbackMode != null) {
            libvlc.libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, playbackMode.intValue());
            return true;
        } else {
            return false;
        }
    }

}
