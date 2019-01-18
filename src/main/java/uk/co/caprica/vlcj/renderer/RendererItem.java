package uk.co.caprica.vlcj.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public final class RendererItem {

    private final LibVlc libvlc;

    private final libvlc_renderer_item_t item;

    private final String name;

    private final String type;

    private final String iconUri;

    private final int flags;

    public RendererItem(LibVlc libvlc, libvlc_renderer_item_t item) {
        this.libvlc = libvlc;
        this.item = item;
        this.name = libvlc.libvlc_renderer_item_name(item);
        this.type = libvlc.libvlc_renderer_item_type(item);
        this.iconUri = libvlc.libvlc_renderer_item_icon_uri(item);
        this.flags = libvlc.libvlc_renderer_item_flags(item);
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public String iconUri() {
        return iconUri;
    }

    // FIXME maybe canVideo canAudio instead?
    public int flags() {
        return flags;
    }

    public void hold() {
        libvlc.libvlc_renderer_item_hold(item);
    }

    public void release() {
        libvlc.libvlc_renderer_item_release(item);
    }

    // FIXME
    public boolean setRenderer(MediaPlayer mediaPlayer) {
        return libvlc.libvlc_media_player_set_renderer(mediaPlayer.mediaPlayerInstance(), item) == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("type=").append(type).append(',');
        sb.append("iconUri=").append(iconUri).append(',');
        sb.append("flags=").append(flags).append(']');
        return sb.toString();
    }

}
