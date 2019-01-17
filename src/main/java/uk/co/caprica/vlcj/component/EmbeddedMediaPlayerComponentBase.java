package uk.co.caprica.vlcj.component;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.awt.*;
import java.awt.event.*;

/**
 *
 *
 * <p>
 * Having this class saves cluttering the {@link EmbeddedMediaPlayerComponent} with all of these empty event listener
 * methods.
 */
abstract class EmbeddedMediaPlayerComponentBase extends Panel implements MediaPlayerEventListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener  {

    protected EmbeddedMediaPlayerComponentBase() {
    }

    // === MediaPlayerEventListener =============================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media) {
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable) {
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id) {
    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {
    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {
    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaPlayerReady(MediaPlayer mediaPlayer) {
    }

    // === MouseListener ========================================================

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // === MouseMotionListener ==================================================

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // === MouseWheelListener ===================================================

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    // === KeyListener ==========================================================

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
