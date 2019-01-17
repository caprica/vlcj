package uk.co.caprica.vlcj.component;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

abstract class DirectAudioPlayerComponentBase extends MediaPlayerEventAdapter implements AudioCallback {

    protected DirectAudioPlayerComponentBase() {
    }

    // === AudioCallback ========================================================

    @Override
    public void play(DirectAudioPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
    }

    @Override
    public void pause(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void resume(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void flush(DirectAudioPlayer mediaPlayer, long pts) {
    }

    @Override
    public void drain(DirectAudioPlayer mediaPlayer) {
    }

}
