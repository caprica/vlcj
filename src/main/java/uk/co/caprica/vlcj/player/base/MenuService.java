package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_navigate_mode_e;

public final class MenuService extends BaseService {

    MenuService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Activate a menu.
     */
    public void menuActivate() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_activate);
    }

    /**
     * Navigate up a menu.
     */
    public void menuUp() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_up);
    }

    /**
     * Navigate down a menu.
     */
    public void menuDown() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_down);
    }

    /**
     * Navigate left a menu.
     */
    public void menuLeft() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_left);
    }

    /**
     * Navigate right a menu.
     */
    public void menuRight() {
        doMenuAction(libvlc_navigate_mode_e.libvlc_navigate_right);
    }

    private void doMenuAction(libvlc_navigate_mode_e action) {
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, action.intValue());
    }

}
