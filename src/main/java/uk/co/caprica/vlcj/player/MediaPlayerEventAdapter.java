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
 * Copyright 2009, 2010, 2011, 2012 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

/**
 * Default implementation of the media player event listener.
 * <p>
 * Simply override the methods you're interested in.
 * <p>
 * For example:
 * <pre>
 *     mediaPlayer.addEventListener(new MediaPlayerEventAdapter() {
 *         @Override
 *         public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
 *             // Maybe update a text control showing the current time...
 *         }
 *
 *         @Override
 *         public void finished(MediaPlayer mediaPlayer) {
 *             // Maybe play the next item in a play-list...
 *         }
 *     });
 * </pre>
 * <p>
 * Events are <em>not</em> raised on the Swing Event Dispatch thread so if updating user
 * interface components in response to these events care must be taken to use
 * {@link SwingUtilities#invokeLater(Runnable)}.
 * <p>
 * This class also provides assistance for temporary media player event listeners via the
 * {@link #done(MediaPlayer, Object)}, {@link #done(MediaPlayer)} and {@link #onDone(MediaPlayer, Object)}
 * methods.
 * <p>
 * A temporary event listener is useful to implement various one-off strategies like executing
 * a one-time piece of code after the media player time has passed a certain value.
 * <p>
 * The listener is temporary because when the desired condition is met the event listener is
 * removed from the media player and thrown away.
 * <p>
 * A common use-case is to add a sequence of new temporary listeners with new conditions each
 * time a previous listener is done with its own processing.
 * <p>
 * For example:
 * <pre>
 *     mediaPlayer.addEventListener(new MediaPlayerEventAdapter() {
 *         @Override
 *         public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
 *             if(newTime >= 20000) {
 *                 // Maybe save a snapshot or something...
 *                 done();
 *             }
 *         }
 *
 *         @Override
 *         public void finished(MediaPlayer mediaPlayer) {
 *             done();
 *         }
 *
 *         @Override
 *         public void error(MediaPlayer mediaPlayer) {
 *             done();
 *         }
 *
 *         @Override
 *         protected void onDone(MediaPlayer mediaPlayer, Object result) {
 *             // Maybe add a new temporary listener with a new condition to wait for
 *         }
 *     });
 * </pre>
 */
public class MediaPlayerEventAdapter implements MediaPlayerEventListener {

    // === Events relating to the media player ==================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
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
    public void error(MediaPlayer mediaPlayer) {
    }

    // === Events relating to the current media =================================

    @Override
    public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
    }

    @Override
    public void mediaFreed(MediaPlayer mediaPlayer) {
    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
    }

    // === Synthetic/semantic events ============================================

    @Override
    public void newMedia(MediaPlayer mediaPlayer) {
    }

    @Override
    public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
    }

    @Override
    public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
    }

    @Override
    public void endOfSubItems(MediaPlayer mediaPlayer) {
    }

    // === Implementation =======================================================

    /**
     * Method for sub-classes to invoke when they are done processing and the
     * listener is no longer needed.
     * <p>
     * Invoking this method from an event listener method implementation will
     * <em>remove</em> this media player event listener from the media player and
     * then invoke {@link #onDone(MediaPlayer, Object)} to give the sub-class the
     * opportunity to execute code on completion of the listener's work.
     *
     * @param mediaPlayer media player
     * @param result optional caller-specific data, may be <code>null</code>
     */
    protected final void done(MediaPlayer mediaPlayer, Object result) {
        mediaPlayer.removeMediaPlayerEventListener(this);
        onDone(mediaPlayer, result);
    }

    /**
     * Method for sub-classes to invoke when they are done processing and the
     * listener is no longer needed.
     * <p>
     * Invoking this method from an event listener method implementation will
     * <em>remove</em> this media player event listener from the media player and
     * then invoke {@link #onDone(MediaPlayer, Object)} to give the sub-class the
     * opportunity to execute code on completion of the listener's work.
     * <p>
     * This is a simple convenience method for {@link #done(MediaPlayer, Object)}
     * with no result parameter.
     *
     * @param mediaPlayer media player
     */
    protected final void done(MediaPlayer mediaPlayer) {
        done(null);
    }

    /**
     * Optional template method for sub-classes to specify behaviour that should be
     * executed when the listener is done, as actioned via {@link #done(MediaPlayer, Object)}
     * or {@link #done(MediaPlayer)}.
     *
     * @param mediaPlayer media player
     * @param result optional caller-specific data, may be <code>null</code>
     */
    protected void onDone(MediaPlayer mediaPlayer, Object result) {
    }
}
