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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.condition;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * Default implementation of a media player condition that triggers if the media
 * player reports an error or unexpectedly reaches the end of the media.
 * <p>
 * This is useful when waiting for a condition that is normally expected (such as
 * waiting until a particular media player state is reached, or a particular play-
 * back time is reached) and the media player unexpectedly reports an error or
 * reports that the end of the media has been reached.
 * <p>
 * Without this, a condition may wait forever, since after an error or after the
 * media has finished <em>no more events</em> will be received.
 * <p>
 * It is expected that <em>most</em> but not necessarily all {@link Condition}
 * implementations will need this behaviour.
 * <p>
 * Do not override {@link #error(MediaPlayer)} or {@link #finished()} unless you
 * are sure you know what you are doing. Some use-cases may require that those
 * methods can be overridden, so if you do provide an override you must be careful
 * to invoke the superclass method for correct operation.
 *
 * @param <T> optional type of result
 */
public class DefaultCondition<T> extends Condition<T> {

    /**
     * Create a media player condition.
     *
     * @param mediaPlayer media player
     */
    public DefaultCondition(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        Logger.debug("error(mediaPlayer={})", mediaPlayer);
        error();
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        Logger.debug("finished(mediaPlayer={})", mediaPlayer);
        finished();
    }
}
