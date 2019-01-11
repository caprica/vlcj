package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.player.base.DefaultMediaPlayer;

// FIXME "Aspect" is gonna be better than "Service"

abstract class BaseService {

    protected final DefaultEmbeddedMediaPlayer mediaPlayer;

    protected final LibVlc libvlc;

    protected final libvlc_instance_t libvlcInstance;

    protected final libvlc_media_player_t mediaPlayerInstance;

    protected BaseService(DefaultEmbeddedMediaPlayer mediaPlayer) {
        this.mediaPlayer         = mediaPlayer;
        this.libvlc              = mediaPlayer.libvlc;
        this.libvlcInstance      = mediaPlayer.libvlcInstance;
        this.mediaPlayerInstance = mediaPlayer.mediaPlayerInstance();
    }

    protected void release() {
    }

}
