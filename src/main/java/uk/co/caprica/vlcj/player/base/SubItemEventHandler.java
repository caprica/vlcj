package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.events.semantic.SemanticEventFactory;

/**
 * Event listener implementation that handles media sub-items.
 * <p>
 * Some media types when you 'play' them do not actually play any media and instead sub-items
 * are created and attached to the current media descriptor.
 * <p>
 * This event listener responds to the media player "finished" event by getting the current
 * media from the player and automatically playing the first sub-item (if there is one).
 * <p>
 * If there is more than one sub-item, then they will simply be played in order, and repeated
 * depending on the value of the "repeat" property.
 */
final class SubItemEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        int subItemIndex = mediaPlayer.subItems().subItemIndex();
        if (subItemIndex != -1) {
            mediaPlayer.events().raiseEvent(SemanticEventFactory.createMediaSubItemFinishedEvent(mediaPlayer, subItemIndex));
        }
        if (mediaPlayer.subItems().getPlaySubItems()) {
            // It is not allowed to call back into LibVLC from this native thread, so offload to a task to play next
            mediaPlayer.submit(new PlayNextSubItemTask(mediaPlayer));
        }
    }

    private static class PlayNextSubItemTask implements Runnable {

        private final MediaPlayer mediaPlayer;

        private PlayNextSubItemTask(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        public void run() {
            mediaPlayer.subItems().playNextSubItem();
        }
    }

}
