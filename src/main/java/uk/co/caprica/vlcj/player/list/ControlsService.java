package uk.co.caprica.vlcj.player.list;

public final class ControlsService extends BaseService {

    ControlsService(DefaultMediaListPlayer mediaListPlayer) {
        super(mediaListPlayer);
    }

    /**
     * Play the media list.
     * <p>
     * If the play mode is {@link MediaListPlayerMode#REPEAT} no item will be played and a "media list end reached"
     * event will be raised.
     */
    public void play() {
        attachVideoSurface();
        libvlc.libvlc_media_list_player_play(mediaListPlayerInstance);
    }

    /**
     * Toggle-pause the media list.
     */
    public void pause() {
        libvlc.libvlc_media_list_player_pause(mediaListPlayerInstance);
    }

    /**
     * Pause/un-pause the media list.
     *
     * @param pause <code>true</code> to pause; <code>false</code> to un-pause
     */
    public void setPause(boolean pause) {
        libvlc.libvlc_media_list_player_set_pause(mediaListPlayerInstance, pause ? 1 : 0);
    }

    /**
     * Stop the media list.
     */
    public void stop() {
        libvlc.libvlc_media_list_player_stop(mediaListPlayerInstance);
    }

    /**
     * Play a particular item on the media list.
     * <p>
     * When the mode is {@link MediaListPlayerMode#REPEAT} this method is the only way to successfully play media in the
     * list.
     *
     * @param itemIndex index of the item to play
     * @return <code>true</code> if the item could be played, otherwise <code>false</code>
     */
    public boolean playItem(int itemIndex) {
        attachVideoSurface();
        return libvlc.libvlc_media_list_player_play_item_at_index(mediaListPlayerInstance, itemIndex) == 0;
    }

    /**
     * Play the next item in the media list.
     * <p>
     * When the mode is {@link MediaListPlayerMode#REPEAT} this method will replay the current media, not the next one.
     */
    public boolean playNext() {
        attachVideoSurface();
        return libvlc.libvlc_media_list_player_next(mediaListPlayerInstance) == 0;
    }

    /**
     * Play the previous item in the media list.
     * <p>
     * When the mode is {@link MediaListPlayerMode#REPEAT} this method will replay the current media, not the previous
     * one.
     */
    public boolean playPrevious() {
        attachVideoSurface();
        return libvlc.libvlc_media_list_player_previous(mediaListPlayerInstance) == 0;
    }

    private void attachVideoSurface() {
        mediaListPlayer.mediaPlayer().attachVideoSurface();
    }

}
