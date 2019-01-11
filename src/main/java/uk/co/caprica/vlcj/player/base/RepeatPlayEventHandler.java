package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Event listener implementation that handles auto-repeat.
 * <p>
 * This listener <em>must</em> be added <em>after</em> {@link ResetMediaEventHandler} to ensure correct operation.
 */
final class RepeatPlayEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        if (mediaPlayer.media().getRepeat()) {
            // It is not allowed to call back into LibVLC from this native thread, so offload to a task to repeat play
            mediaPlayer.submit(new ReplayMediaTask(mediaPlayer));
        }
    }

    private static class ReplayMediaTask implements Runnable {

        private final MediaPlayer mediaPlayer;

        private ReplayMediaTask(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        public void run() {
            mediaPlayer.controls().play();
        }
    }
}
