package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.events.semantic.SemanticEventFactory;

/**
 * Event listener implementation that handles a new item being played.
 * <p>
 * This is not for sub-items.
 */
final class NewMediaEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media) {
        if (mediaPlayer.subItems().subItemIndex() == -1) {
            mediaPlayer.events().raiseEvent(SemanticEventFactory.createMediaNewEvent(mediaPlayer));
        }
    }

}
