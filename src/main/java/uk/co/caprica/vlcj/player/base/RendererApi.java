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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.renderer.RendererItem;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_renderer;

/**
 * Behaviour pertaining to the media player renderer (e.g. Chromecast).
 */
public final class RendererApi extends BaseApi {

    /**
     * Optional alternate renderer.
     */
    private RendererItem rendererItem;

    RendererApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set an alternate media renderer.
     * <p>
     * If the supplied renderer item is not <code>null</code> this component will invoke {@link RendererItem#hold()}.
     * <p>
     * If a renderer was previously set, RendererItem{@link RendererItem#release()} will be invoked.
     * <p>
     * A client application therefore need not, although it may, concern itself with hold/release.
     * <p>
     * If the new renderer could not be set it will be properly released (i.e. the hold acquired in this method will not
     * be kept).
     *
     * @param rendererItem media renderer, or <code>null</code> to render as normal
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public final boolean setRenderer(RendererItem rendererItem) {
        if (rendererItem != null) {
            if (!rendererItem.hold()) {
                return false;
            }
        }
        libvlc_renderer_item_t rendererItemInstance = rendererItem != null ? rendererItem.rendererItemInstance() : null;
        boolean result = libvlc_media_player_set_renderer(mediaPlayerInstance, rendererItemInstance) == 0;
        if (result) {
            if (this.rendererItem != null) {
                this.rendererItem.release();
            }
            this.rendererItem = rendererItem;
        } else {
            if (rendererItem != null) {
                rendererItem.release();
            }
        }
        return result;
    }

}
