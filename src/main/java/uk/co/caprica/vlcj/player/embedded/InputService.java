package uk.co.caprica.vlcj.player.embedded;


public final class InputService extends BaseService {

    InputService(DefaultEmbeddedMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set whether or not to enable native media player mouse input handling.
     * <p>
     * It may be necessary on some platforms to invoke this method with a <code>false</code> parameter
     * value for Java mouse and keyboard listeners to work.
     * <p>
     * Note that clicking the video surface on certain platforms is not guaranteed to capture mouse
     * events - it may be necessary to respond to a mouse pressed event on the video surface and
     * explicitly request the input focus to the video surface.
     *
     * @param enable <code>true</code> to enable, <code>false</code> to disable
     */
    public void setEnableMouseInputHandling(boolean enable) {
        libvlc.libvlc_video_set_mouse_input(mediaPlayerInstance, enable ? 1 : 0);
    }

    /**
     * Set whether or not to enable native media player keyboard input handling.
     * <p>
     * It may be necessary on some platforms to invoke this method with a <code>false</code> parameter
     * value for Java mouse and keyboard listeners to work.
     *
     * @param enable <code>true</code> to enable, <code>false</code> to disable
     */
    public void setEnableKeyInputHandling(boolean enable) {
        libvlc.libvlc_video_set_key_input(mediaPlayerInstance, enable ? 1 : 0);
    }

}
