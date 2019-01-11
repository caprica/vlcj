package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.events.semantic.SemanticEventFactory;

/**
 * Event listener implementation that waits for the first position changed event and raises a synthetic media player
 * ready event.
 * <p>
 * Some media player operations require that the media be definitively playing before they are effective (things like
 * setting logo and marquee amongst others) and the playing event itself does not guarantee this.
 */
final class MediaPlayerReadyEventHandler extends MediaPlayerEventAdapter {

    /**
     * Flag if the event has fired since the media was last started or not.
     */
    private boolean fired;

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
        if (!fired && newPosition > 0) {
            fired = true;
            mediaPlayer.events().raiseEvent(SemanticEventFactory.createMediaPlayerReadyEvent(mediaPlayer));
        }
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        fired = false;
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        fired = false;
    }

}
