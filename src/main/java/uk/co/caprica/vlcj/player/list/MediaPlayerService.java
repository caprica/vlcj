package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

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

}
