package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;

// FIXME "Aspect" is gonna be better than "Service"

abstract class BaseService {

    protected final DefaultMediaPlayer mediaPlayer;

    protected final LibVlc libvlc;

    protected final libvlc_instance_t libvlcInstance;

    protected final libvlc_media_player_t mediaPlayerInstance;

    protected BaseService(DefaultMediaPlayer mediaPlayer) {
        this.mediaPlayer         = mediaPlayer;
        this.libvlc              = mediaPlayer.libvlc;
        this.libvlcInstance      = mediaPlayer.libvlcInstance;
        this.mediaPlayerInstance = mediaPlayer.mediaPlayerInstance();
    }

    protected void release() {
    }

}
