package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.enums.MediaPlayerRole;

// FIXME rename to simple get/set ? so we end up mediaPlayer().role().set(...)

public final class RoleService extends BaseService {

    RoleService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Get the media player role.
     *
     * @return media player role
     */
    public MediaPlayerRole getRole() {
        return MediaPlayerRole.role(libvlc.libvlc_media_player_get_role(mediaPlayerInstance));
    }

    /**
     * Set the media player role.
     *
     * @param role media player role
     */
    public void setRole(MediaPlayerRole role) {
        libvlc.libvlc_media_player_set_role(mediaPlayerInstance, role.intValue());
    }

}
