package uk.co.caprica.vlcj.player.base;

public final class ControlsService extends BaseService {

    ControlsService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Begin play-back.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     */
    public void play() {
        mediaPlayer.onBeforePlay();
        libvlc.libvlc_media_player_play(mediaPlayerInstance);
    }

    /**
     * Begin play-back and wait for the media to start playing or for an error to occur.
     * <p>
     * If called when the play-back is paused, the play-back will resume from the current position.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @return <code>true</code> if the media started playing, <code>false</code> if the media failed to start because of an error
     */
    public boolean start() {
        return new MediaPlayerLatch(mediaPlayer).play();
    }

    /**
     * Stop play-back.
     * <p>
     * A subsequent play will play-back from the start.
     */
    public void stop() {
        libvlc.libvlc_media_player_stop(mediaPlayerInstance);
    }

    /**
     * Pause/resume.
     *
     * @param pause true to pause, false to play/resume
     */
    public void setPause(boolean pause) {
        libvlc.libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
    }

    /**
     * Pause play-back.
     * <p>
     * If the play-back is currently paused it will begin playing.
     */
    public void pause() {
        libvlc.libvlc_media_player_pause(mediaPlayerInstance);
    }

    /**
     * Advance one frame.
     */
    public void nextFrame() {
        libvlc.libvlc_media_player_next_frame(mediaPlayerInstance);
    }

    /**
     * Skip forward or backward by a period of time.
     * <p>
     * To skip backwards specify a negative delta.
     *
     * @param delta time period, in milliseconds
     */
    public void skip(long delta) {
        long current = mediaPlayer.status().getTime();
        if (current != -1) {
            setTime(current + delta);
        }
    }

    /**
     * Skip forward or backward by a change in position.
     * <p>
     * To skip backwards specify a negative delta.
     *
     * @param delta amount to skip
     */
    public void skipPosition(float delta) {
        float current = mediaPlayer.status().getPosition();
        if (current != -1) {
            setPosition(current + delta);
        }
    }

    /**
     * Jump to a specific moment.
     * <p>
     * If the requested time is less than zero, it is normalised to zero.
     *
     * @param time time since the beginning, in milliseconds
     */
    public void setTime(long time) {
        libvlc.libvlc_media_player_set_time(mediaPlayerInstance, Math.max(time, 0));
    }

    /**
     * Jump to a specific position.
     * <p>
     * If the requested position is less than zero, it is normalised to zero.
     *
     * @param position position value, a percentage (e.g. 0.15 is 15%)
     */
    public void setPosition(float position) {
        libvlc.libvlc_media_player_set_position(mediaPlayerInstance, Math.max(position, 0));
    }

    /**
     * Set the video play rate.
     * <p>
     * Some media protocols are not able to change the rate.
     *
     * @param rate rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
     * @return -1 on error, 0 on success
     */
    public int setRate(float rate) {
        return libvlc.libvlc_media_player_set_rate(mediaPlayerInstance, rate);
    }

}
