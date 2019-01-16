package uk.co.caprica.vlcj.player.events.media;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_parsed_status_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.media.Media;

public class MediaEventAdapter implements MediaEventListener {

    @Override
    public void mediaMetaChanged(Media media, int metaType) {
    }

    @Override
    public void mediaSubItemAdded(Media media, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(Media media, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(Media media, libvlc_media_parsed_status_e newStatus) {
    }

    @Override
    public void mediaFreed(Media media) {
    }

    @Override
    public void mediaStateChanged(Media media, libvlc_state_t newState) {
    }

    @Override
    public void mediaSubItemTreeAdded(Media media, libvlc_media_t item) {
    }

}
