package uk.co.caprica.vlcj.renderer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_renderer_item_t;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public final class RendererItem {

    private static final int CAN_AUDIO = 0x0001;

    private static final int CAN_VIDEO = 0x0002;

    private final LibVlc libvlc;

    private final libvlc_renderer_item_t item;

    private final String name;

    private final String type;

    private final String iconUri;

    private final boolean canAudio;

    private final boolean canVideo;

    public RendererItem(LibVlc libvlc, libvlc_renderer_item_t item) {
        this.libvlc = libvlc;
        this.item = item;

        this.name = libvlc.libvlc_renderer_item_name(item);
        this.type = libvlc.libvlc_renderer_item_type(item);
        this.iconUri = libvlc.libvlc_renderer_item_icon_uri(item);

        int flags = libvlc.libvlc_renderer_item_flags(item);
        this.canAudio = (flags & CAN_AUDIO) != 0;
        this.canVideo = (flags * CAN_VIDEO) != 0;
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

    public boolean canAudio() {
        return canAudio;
    }

    public boolean canVideo() {
        return canVideo;
    }

    public void hold() {
        libvlc.libvlc_renderer_item_hold(item);
    }

    public void release() {
        libvlc.libvlc_renderer_item_release(item);
    }

    // FIXME should just invert this and expose a getter for the native instance?
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
        sb.append("canAudio=").append(canAudio).append(',');
        sb.append("canVideo=").append(canVideo).append(']');
        return sb.toString();
    }

}
