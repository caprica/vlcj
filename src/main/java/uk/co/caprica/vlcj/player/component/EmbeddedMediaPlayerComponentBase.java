/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.component;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.parser.Pictures;
import uk.co.caprica.vlcj.player.base.Capabilties;
import uk.co.caprica.vlcj.player.base.ChapterDescription;
import uk.co.caprica.vlcj.player.base.FrameStatus;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.TitleDescription;

import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

/**
 * Base implementation of an embedded media player.
 * <p>
 * This class serves to keep the {@link EmbeddedMediaPlayerComponent} concrete implementation clean and un-cluttered.
 */
@SuppressWarnings("serial")
abstract class EmbeddedMediaPlayerComponentBase extends JPanel implements MediaPlayerEventListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener  {

    /**
     * Blank cursor to use when the cursor is disabled.
     */
    @Nullable
    private Cursor blankCursor;

    /**
     * Create a media player component.
     */
    protected EmbeddedMediaPlayerComponentBase() {
    }

    /**
     * Enable or disable the mouse cursor when it is over the component.
     * <p>
     * Note that you may see glitchy behaviour if you try and disable the cursor <em>after</em> you
     * show the window/frame that contains your video surface.
     * <p>
     * If you want to disable the cursor for this component you should do so before you show the
     * window.
     *
     * @param enabled <code>true</code> to enable (show) the cursor; <code>false</code> to disable (hide) it
     */
    public final void setCursorEnabled(boolean enabled) {
        setCursor(enabled ? null : getBlankCursor());
    }

    /**
     * Create a blank 1x1 image to use when the cursor is disabled.
     *
     * @return cursor
     */
    private Cursor getBlankCursor() {
        if (blankCursor == null) {
            Image blankImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImage, new Point(0, 0), "");
        }
        return blankCursor;
    }

    /**
     * Template method invoked at the end of the media player constructor.
     */
    protected void onAfterConstruct() {
    }

    /**
     * Template method invoked immediately prior to releasing the media player and media player factory instances.
     */
    protected void onBeforeRelease() {
    }

    /**
     * Template method invoked immediately after releasing the media player and media player factory instances.
     */
    protected void onAfterRelease() {
    }

    // === MediaPlayerEventListener =============================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
    }

    @Override
    public void bufferingChanged(MediaPlayer mediaPlayer, float newCache) {
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
    public void stopping(MediaPlayer mediaPlayer) {
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, long newTime, double newPosition) {
    }

    @Override
    public void titleListChanged(MediaPlayer mediaPlayer) {
    }

    @Override
    public void titleSelectionChanged(MediaPlayer mediaPlayer, TitleDescription titleDescription, int index) {
    }

    @Override
    public void screenshotTaken(MediaPlayer mediaPlayer, String filename) {
    }

    @Override
    public void mediaParsed(MediaPlayer mediaPlayer, MediaRef mediaRef) {
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, MetaData metaData) {
    }

    @Override
    public void mediaSubitemsChanged(MediaPlayer mediaPlayer, MediaList mediaList) {
    }

    @Override
    public void mediaAttachmentsAdded(MediaPlayer mediaPlayer, Pictures pictures) {
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
    }

    @Override
    public void trackAdded(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
    }

    @Override
    public void trackRemoved(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
    }

    @Override
    public void trackUpdated(MediaPlayer mediaPlayer, TrackType trackType, String trackId) {
    }

    @Override
    public void trackSelectionChanged(MediaPlayer mediaPlayer, TrackType trackType, String unselectedTrackId, String selectedTrackId) {
    }

    @Override
    public void nextFrameStatus(MediaPlayer mediaPlayer, FrameStatus newStatus) {
    }

    @Override
    public void previousFrameStatus(MediaPlayer mediaPlayer, FrameStatus newStatus) {
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
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
    public void chapterSelectionChanged(MediaPlayer mediaPlayer, TitleDescription titleDescription, int titleIndex, ChapterDescription chapterDescription, int chapterIndex) {
    }

    @Override
    public void recordChanged(MediaPlayer mediaPlayer, boolean recording, String recordedFilePath) {
    }

    @Override
    public void programAdded(MediaPlayer mediaPlayer, int id) {
    }

    @Override
    public void programRemoved(MediaPlayer mediaPlayer, int id) {
    }

    @Override
    public void programUpdated(MediaPlayer mediaPlayer, int id) {
    }

    @Override
    public void programSelected(MediaPlayer mediaPlayer, int unselectedId, int selectedId) {
    }

    @Override
    public void capabilitiesChanged(MediaPlayer mediaPlayer, Capabilties oldCapabilties, Capabilties newCapabilties) {
    }

    @Override
    public void rateChanged(MediaPlayer mediaPlayer, float newRate) {
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
    }

    // === Synthetic/semantic events ============================================

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
