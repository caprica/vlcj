package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;

public final class StatusService extends BaseService {

    StatusService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    public boolean isPlaying() {
        return libvlc.libvlc_media_list_player_is_playing(mediaListPlayerInstance) != 0;
    }

    public libvlc_state_t getMediaListPlayerState() {
        return libvlc_state_t.state(libvlc.libvlc_media_list_player_get_state(mediaListPlayerInstance));
    }

}
