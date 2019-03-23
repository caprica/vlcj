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

package uk.co.caprica.vlcj.player.renderer;

import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_flags;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_hold;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_icon_uri;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_name;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_renderer_item_type;

/**
 * Encapsulation of a renderer item.
 */
public final class RendererItem {

    /**
     * Native flag value signifying this item can play audio.
     */
    private static final int CAN_AUDIO = 0x0001;

    /**
     * Native flag value signifying this item can play video.
     */
    private static final int CAN_VIDEO = 0x0002;

    /**
     * Native renderer item instance.
     */
    private final libvlc_renderer_item_t item;

    /**
     * Renderer item name.
     */
    private final String name;

    /**
     * Renderer item type.
     */
    private final String type;

    /**
     * Renderer item icon URI.
     */
    private final String iconUri;

    /**
     * Flag if this item can render audio or not.
     */
    private final boolean canAudio;

    /**
     * Flag if this item can render video or not.
     */
    private final boolean canVideo;

    /**
     * Create a new renderer item.
     *
     * @param item native renderer item instance
     */
    public RendererItem(libvlc_renderer_item_t item) {
        this.item = item;

        this.name = libvlc_renderer_item_name(item);
        this.type = libvlc_renderer_item_type(item);
        this.iconUri = libvlc_renderer_item_icon_uri(item);

        int flags = libvlc_renderer_item_flags(item);
        this.canAudio = (flags & CAN_AUDIO) != 0;
        this.canVideo = (flags & CAN_VIDEO) != 0;
    }

    /**
     * Get the item name.
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Get the item type.
     *
     * @return type
     */
    public String type() {
        return type;
    }

    /**
     * Get the item icon URI.
     *
     * @return icon URI
     */
    public String iconUri() {
        return iconUri;
    }

    /**
     * Can this renderer item play audio?
     *
     * @return <code>true</code> if this item can play audio; <code>false</code> if it can not
     */
    public boolean canAudio() {
        return canAudio;
    }

    /**
     * Can this renderer item play video?
     *
     * @return <code>true</code> if this item can play video; <code>false</code> if it can not
     */
    public boolean canVideo() {
        return canVideo;
    }

    /**
     * Hold this renderer item.
     * <p>
     * The item <em>must</em> be held prior to setting it via a call to {@link MediaPlayer#setRenderer(RendererItem)}
     * on {@link MediaPlayer}.
     * <p>
     * The item <em>must</em> be released when the caller no longer has any use for it.
     *
     * @return <code>true</code> if the renderer item was successfully held; <code>false</code> on error
     */
    public boolean hold() {
        return libvlc_renderer_item_hold(item) != null;
    }

    /**
     * Release the renderer item.
     * <p>
     * The renderer item may still be kept and re-used, invoke {@link #hold()} again.
     */
    public void release() {
        libvlc_renderer_item_release(item);
    }

    /**
     * Get the native renderer item instance.
     *
     * @return renderer item instance
     */
    public libvlc_renderer_item_t rendererItemInstance() {
        return item;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("type=").append(type).append(',');
        sb.append("iconUri=").append(iconUri).append(',');
        sb.append("canAudio=").append(canAudio).append(',');
        sb.append("canVideo=").append(canVideo).append(']');
        return sb.toString();
    }

}
