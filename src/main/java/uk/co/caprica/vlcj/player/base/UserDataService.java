package uk.co.caprica.vlcj.player.base;

public final class UserDataService extends BaseService {

    private Object userData;

    UserDataService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the user data associated with the media player.
     *
     * @return user data
     */
    public Object userData() {
        return userData;
    }

    /**
     * Set user data to associate with the media player.
     *
     * @param userData user data
     */
    public void userData(Object userData) {
        this.userData = userData;
    }

}
