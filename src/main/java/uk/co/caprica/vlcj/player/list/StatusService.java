package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.enums.State;

public final class StatusService extends BaseService {

    StatusService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    public boolean isPlaying() {
        return libvlc.libvlc_media_list_player_is_playing(mediaListPlayerInstance) != 0;
    }

    public State getMediaListPlayerState() {
        return State.state(libvlc.libvlc_media_list_player_get_state(mediaListPlayerInstance));
    }

}
