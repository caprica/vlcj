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

package uk.co.caprica.vlcj.player.list;

import uk.co.caprica.vlcj.media.MediaRef;

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
 * to call back into LibVLC it should use the {@link MediaListPlayer#submit(Runnable)} method to submit a task for
 * asynchronous execution.
 */
public class MediaListPlayerEventAdapter implements MediaListPlayerEventListener {

    @Override
    public void mediaListPlayerFinished(MediaListPlayer mediaListPlayer) {
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, MediaRef item) {
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
    }

}

