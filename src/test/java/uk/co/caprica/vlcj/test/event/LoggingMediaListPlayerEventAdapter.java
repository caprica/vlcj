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

package uk.co.caprica.vlcj.test.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

/**
 * Implementation of a {@link MediaPlayerEventListener} that logs all invocations.
 * <p>
 * Useful only for testing.
 */
public class LoggingMediaListPlayerEventAdapter implements MediaListPlayerEventListener {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(LoggingMediaListPlayerEventAdapter.class);

    @Override
    public void mediaListPlayerFinished(MediaListPlayer mediaListPlayer) {
        logger.debug("mediaListFinished()");
    }

    @Override
    public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item) {
        logger.debug("nextItem()");
    }

    @Override
    public void stopped(MediaListPlayer mediaListPlayer) {
        logger.debug("stopped()");
    }

}
