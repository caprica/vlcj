package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.media.Media;

public final class MediaService extends BaseService {

    /**
     *
     */
    private Media media;

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    MediaService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     *
     *
     * @param media
     */
    public Media set(Media media) {
        Media previousMedia = this.media;
        this.media = media;
        if (media != null) {
            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, media.mediaInstance());
        }
        mediaPlayer.subItems().changeMedia(media);
        return previousMedia;
    }

    /**
     *
     *
     * @return
     */
    public Media get() {
        return media;
    }

    /**
     * Reset the current media.
     * <p>
     * This simply sets the last played media again so it may be played again. Without this, the current media would not
     * be able to be played again if it had finished.
     * </p>
     * This is provided as a convenience, the calling application can simply invoke #setMedia again to achieve the same
     * effect.
     */
    public void reset() {
        set(this.media);
    }

    /**
     * Set whether or not the media player should automatically repeat playing the media when it has
     * finished playing.
     * <p>
     * There is <em>no</em> guarantee of seamless play-back when using this method - see instead
     * {@link uk.co.caprica.vlcj.player.list.MediaListPlayer MediaListPlayer}.
     * <p>
     * If the media has sub-items, then it is the sub-items that are repeated. FIXME is this true?
     *
     * @param repeat <code>true</code> to automatically replay the media, otherwise <code>false</code>
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Get whether or not the media player will automatically repeat playing the media when it has
     * finished playing.
     *
     * @return <code>true</code> if the media will be automatically replayed, otherwise <code>false</code>
     */
    public boolean getRepeat() {
        return repeat;
    }

    @Override
    protected void release() {
    }

    // FIXME this method will be removed in short order, but it is currently referenced in a LOT of tests...
    @Deprecated
    public boolean playMedia(String mrl, String... options) {
        throw new UnsupportedOperationException();
    }
}
