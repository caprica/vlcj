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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;

import javax.swing.*;

/**
 * Specification for a component that is interested in receiving event notifications from the media player.
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user interface components in
 * response to these events care must be taken to use {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * Equally, care must be taken not to call back into LibVLC from the event handling thread - if an event handler needs
 * to call back into LibVLC it should use the {@link MediaPlayer#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 *
 * @see MediaPlayerEventAdapter
 */
public interface MediaPlayerEventListener {

    // === Events relating to the media player ==================================

    /**
     * The media changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param media new media instance
     */
    void mediaChanged(MediaPlayer mediaPlayer, MediaRef media);

    /**
     * Opening the media.
     *
     * @param mediaPlayer media player that raised the event
     */
    void opening(MediaPlayer mediaPlayer);

    /**
     * Buffering media.
     *
     * @param mediaPlayer media player that raised the event
     * @param newCache percentage complete, ranging from 0.0 to 100.0
     */
    void buffering(MediaPlayer mediaPlayer, float newCache);

    /**
     * The media started playing.
     * <p>
     * There is no guarantee that a video output has been created at this point.
     *
     * @param mediaPlayer media player that raised the event
     */
    void playing(MediaPlayer mediaPlayer);

    /**
     * Media paused.
     *
     * @param mediaPlayer media player that raised the event
     */
    void paused(MediaPlayer mediaPlayer);

    /**
     * Media stopped.
     * <p>
     * A stopped event may be raised under certain circumstances even if the media player is not playing (e.g. as part
     * of the associated media list player sub-item handling). Client applications must therefore be prepared to handle
     * such a situation.
     *
     * @param mediaPlayer media player that raised the event
     */
    void stopped(MediaPlayer mediaPlayer);

    /**
     * Media skipped forward.
     *
     * @param mediaPlayer media player that raised the event
     */
    void forward(MediaPlayer mediaPlayer);

    /**
     * Media skipped backward.
     *
     * @param mediaPlayer media player that raised the event
     */
    void backward(MediaPlayer mediaPlayer);

    /**
     * Media finished playing (i.e. the end was reached without being stopped).
     *
     * @param mediaPlayer media player that raised the event
     */
    void finished(MediaPlayer mediaPlayer);

    /**
     * Media play-back time changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newTime new time
     */
    void timeChanged(MediaPlayer mediaPlayer, long newTime);

    /**
     * Media play-back position changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newPosition percentage between 0.0 and 1.0
     */
    void positionChanged(MediaPlayer mediaPlayer, float newPosition);

    /**
     * Media seekable status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newSeekable new seekable status
     */
    void seekableChanged(MediaPlayer mediaPlayer, int newSeekable);

    /**
     * Media pausable status changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newPausable new pausable status
     */
    void pausableChanged(MediaPlayer mediaPlayer, int newPausable);

    /**
     * Media title changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newTitle new title
     */
    void titleChanged(MediaPlayer mediaPlayer, int newTitle);

    /**
     * A snapshot was taken.
     *
     * @param mediaPlayer media player that raised the event
     * @param filename name of the file containing the snapshot
     */
    void snapshotTaken(MediaPlayer mediaPlayer, String filename);

    /**
     * Media length changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newLength new length (number of milliseconds)
     */
    void lengthChanged(MediaPlayer mediaPlayer, long newLength);

    /**
     * The number of video outputs changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newCount new number of video outputs
     */
    void videoOutput(MediaPlayer mediaPlayer, int newCount);

    /**
     * Program scrambled changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newScrambled new scrambled value
     */
    void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled);

    /**
     * An elementary stream was added.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id);

    /**
     * An elementary stream was deleted.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id);

    /**
     * An elementary stream was selected.
     *
     * @param mediaPlayer media player that raised the event
     * @param type type of stream
     * @param id identifier of stream
     */
    void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id);

    /**
     * The media player was corked/un-corked.
     * <p>
     * Corking/un-corking can occur e.g. when another media player (or some
     * other application) starts/stops playing media.
     *
     * @param mediaPlayer media player that raised the event
     * @param corked <code>true</code> if corked; otherwise <code>false</code>
     */
    void corked(MediaPlayer mediaPlayer, boolean corked);

    /**
     * The audio was muted/un-muted.
     *
     * @param mediaPlayer media player that raised the event
     * @param muted <code>true</code> if muted; otherwise <code>false</code>
     */
    void muted(MediaPlayer mediaPlayer, boolean muted);

    /**
     * The volume changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param volume new volume
     */
    void volumeChanged(MediaPlayer mediaPlayer, float volume);

    /**
     * The audio device changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param audioDevice new audio device
     */
    void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice);

    /**
     * The chapter changed.
     *
     * @param mediaPlayer media player that raised the event
     * @param newChapter new chapter
     */
    void chapterChanged(MediaPlayer mediaPlayer, int newChapter);

    /**
     * An error occurred.
     *
     * @param mediaPlayer media player that raised the event
     */
    void error(MediaPlayer mediaPlayer);

    // === Synthetic/semantic events ============================================

    /**
     * Media player is ready (to enable features like logo and marquee) after
     * the media has started playing.
     * <p>
     * The implementation will fire this event once on receipt of the first
     * native position-changed event with a position value greater than zero.
     * <p>
     * The event will be fired again if the media is played again after a native
     * stopped or finished event is received.
     * <p>
     * Waiting for this event may be more reliable than using {@link #playing(MediaPlayer)}
     * or {@link #videoOutput(MediaPlayer, int)} in some cases (logo and marquee
     * already mentioned, also setting audio tracks, sub-title tracks and so on).
     *
     * @param mediaPlayer media player that raised the event
     */
    void mediaPlayerReady(MediaPlayer mediaPlayer);

}
