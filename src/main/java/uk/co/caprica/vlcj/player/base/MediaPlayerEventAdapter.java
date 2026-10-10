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

package uk.co.caprica.vlcj.player.base;

import org.jspecify.annotations.Nullable;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.parser.Pictures;

import javax.swing.*;

/**
 * Default implementation of the media player event listener.
 * <p>
 * Simply override the methods you're interested in.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread - if an event handler needs
 * to call back into LibVLC it should use the {@link MediaPlayer#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 */
public class MediaPlayerEventAdapter implements MediaPlayerEventListener {

    // === Events relating to the media player ==================================

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
    public void trackSelectionChanged(MediaPlayer mediaPlayer, TrackType trackType, @Nullable String unselectedTrackId, @Nullable String selectedTrackId) {
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

}
