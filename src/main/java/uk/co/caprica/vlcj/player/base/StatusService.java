package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.State;

public final class StatusService extends BaseService {

    StatusService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Is the current media playable?
     *
     * @return <code>true</code> if the current media is playable, otherwise <code>false</code>
     */
    public boolean isPlayable() {
        return libvlc.libvlc_media_player_will_play(mediaPlayerInstance) == 1;
    }

    /**
     * Is the media player playing?
     *
     * @return <code>true</code> if the media player is playing, otherwise <code>false</code>
     */
    public boolean isPlaying() {
        return libvlc.libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
    }

    /**
     * Is the current media seekable?
     *
     * @return <code>true</code> if the current media is seekable, otherwise <code>false</code>
     */
    public boolean isSeekable() {
        return libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
    }

    /**
     * Can the current media be paused?
     *
     * @return <code>true</code> if the current media can be paused, otherwise <code>false</code>
     */
    public boolean canPause() {
        return libvlc.libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
    }

    /**
     * Is the current program scrambled?
     *
     * @return <code>true</code> if the current program is scrambled, otherwise <code>false</code>
     */
    public boolean programScrambled() {
        return libvlc.libvlc_media_player_program_scrambled(mediaPlayerInstance) == 1;
    }

    /**
     * Get the length of the current media item.
     *
     * @return length, in milliseconds
     */
    public long getLength() {
        return libvlc.libvlc_media_player_get_length(mediaPlayerInstance);
    }

    /**
     * Get the current play-back time.
     *
     * @return current time, expressed as a number of milliseconds
     */
    public long getTime() {
        return libvlc.libvlc_media_player_get_time(mediaPlayerInstance);
    }

    /**
     * Get the current play-back position.
     *
     * @return current position, expressed as a percentage (e.g. 0.15 is returned for 15% complete)
     */
    public float getPosition() {
        return libvlc.libvlc_media_player_get_position(mediaPlayerInstance);
    }

    /**
     * Get the current video play rate.
     *
     * @return rate, where 1.0 is normal speed, 0.5 is half speed, 2.0 is double speed and so on
     */
    public float getRate() {
        return libvlc.libvlc_media_player_get_rate(mediaPlayerInstance);
    }

    /**
     * Get the number of video outputs for the media player.
     *
     * @return number of video outputs, may be zero
     */
    public int getVideoOutputs() {
        return libvlc.libvlc_media_player_has_vout(mediaPlayerInstance);
    }

    /**
     * Get the media player current state.
     *
     * @return state current media player state
     */
    public State getMediaPlayerState() {
        return State.state(libvlc.libvlc_media_player_get_state(mediaPlayerInstance));
    }

}
