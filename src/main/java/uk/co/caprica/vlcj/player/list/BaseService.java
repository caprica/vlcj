package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;

abstract class BaseService {

    protected final DefaultMediaListPlayer mediaListPlayer;

    protected final LibVlc libvlc;

    private final libvlc_instance_t libvlcInstance;

    protected final libvlc_media_list_player_t mediaListPlayerInstance;

    protected BaseService(DefaultMediaListPlayer mediaListPlayer) {
        this.mediaListPlayer         = mediaListPlayer;
        this.libvlc                  = mediaListPlayer.libvlc;
        this.libvlcInstance          = mediaListPlayer.libvlcInstance;
        this.mediaListPlayerInstance = mediaListPlayer.mediaListPlayerInstance();
    }

    protected void release() {
    }

}
