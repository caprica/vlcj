/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;

/**
 * Implementation of a {@link FullScreenStrategy} that uses the native LibVLC fullscreen API.
 * <p>
 * <em>This can only be used if you are not embedding the media player video surface in your application.</em>
 */
public class VlcNativeFullScreenStrategy implements FullScreenStrategy {

    private final LibVlc libvlc;

    private final libvlc_media_player_t mediaPlayerInstance;

    public VlcNativeFullScreenStrategy(LibVlc libvlc, libvlc_media_player_t mediaPlayerInstance) {
        this.libvlc              = libvlc;
        this.mediaPlayerInstance = mediaPlayerInstance;
    }

    @Override
    public void enterFullScreenMode() {
        libvlc.libvlc_set_fullscreen(mediaPlayerInstance, 1);
    }

    @Override
    public void exitFullScreenMode() {
        libvlc.libvlc_set_fullscreen(mediaPlayerInstance, 0);
    }

    @Override
    public boolean isFullScreenMode() {
        return libvlc.libvlc_get_fullscreen(mediaPlayerInstance) != 0;
    }

}
