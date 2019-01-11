package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Event listener implementation that resets the media ready for replay after is has finished.
 * <p>
 * This enables an application to invoke play() after the media is finished.
 * <p>
 * Without this, an application must invoke stop(), then play().
 * <p>
 * This listener <em>must</em> be added <em>before</em> {@link RepeatPlayEventHandler} to ensure correct operation.
 */
final class ResetMediaEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        // It is not allowed to call back into LibVLC from this native thread, so offload to a task to reset the media
        mediaPlayer.submit(new ResetMediaTask(mediaPlayer));
    }

    private class ResetMediaTask implements Runnable {

        private final MediaPlayer mediaPlayer;

        public ResetMediaTask(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        public void run() {
            mediaPlayer.media().resetMedia();
        }
    }

}
