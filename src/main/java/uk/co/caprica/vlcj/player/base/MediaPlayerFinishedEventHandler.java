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

import uk.co.caprica.vlcj.player.base.events.MediaPlayerEventFactory;
import uk.co.caprica.vlcj.support.eventmanager.PostEventListener;

/**
 * Event listener implementation that handles finished events from stopped events, where applicable.
 * <p>
 * This is a "special" event that must run after other application events have finished processing, hence the use of
 * the {@link PostEventListener} annotation.
 */
@PostEventListener
final class MediaPlayerFinishedEventHandler extends MediaPlayerEventAdapter {

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.controls().isStopRequested()) {
            mediaPlayer.events().raiseEvent(MediaPlayerEventFactory.createMediaPlayerFinishedEvent(mediaPlayer));
        } else {
            mediaPlayer.controls().clearStopRequested();
        }
    }

}
