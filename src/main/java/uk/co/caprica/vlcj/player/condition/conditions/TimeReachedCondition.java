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

package uk.co.caprica.vlcj.player.condition.conditions;

import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.condition.DefaultCondition;

/**
 * Implementation of a condition that waits for the media player to report that
 * it has reached/passed a particular point in time.
 */
public class TimeReachedCondition extends DefaultCondition<Long> {

    /**
     * Target time (number of milliseconds since start of media).
     */
    protected final long targetTime;

    /**
     * Create a condition.
     *
     * @param mediaPlayer media player
     * @param targetTime target time (milliseconds since start)
     */
    public TimeReachedCondition(MediaPlayer mediaPlayer, long targetTime) {
        super(mediaPlayer);
        this.targetTime = targetTime;
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        if(newTime >= targetTime) {
            Logger.debug("Target time {} reached", targetTime);
            ready(targetTime);
        }
    }
}
