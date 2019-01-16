package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public final class MediaPlayerService extends BaseService {

    /**
     * We pin a reference here in case the client application doesn't.
     */
    private MediaPlayer mediaPlayer;

    MediaPlayerService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Associate an actual media player with the media list player.
     *
     * @param mediaPlayer media player
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        libvlc.libvlc_media_list_player_set_media_player(mediaListPlayerInstance, mediaPlayer.mediaPlayerInstance());
    }

    /**
     * Get the media player previously associated with the media list player.
     *
     * @return media player, may be <code>null</code>
     */
    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }

    /**
     * If there is an associated media player then make sure the video surface is attached.
     */
    void attachVideoSurface() {
        if (mediaPlayer instanceof EmbeddedMediaPlayer) {
            ((EmbeddedMediaPlayer) mediaPlayer).videoSurface().attachVideoSurface();
        }
    }

}
