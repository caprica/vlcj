package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_role_e;

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
    public libvlc_media_player_role_e getRole() {
        return libvlc_media_player_role_e.role(libvlc.libvlc_media_player_get_role(mediaPlayerInstance));
    }

    /**
     * Set the media player role.
     *
     * @param role media player role
     */
    public void setRole(libvlc_media_player_role_e role) {
        libvlc.libvlc_media_player_set_role(mediaPlayerInstance, role.intValue());
    }

}
