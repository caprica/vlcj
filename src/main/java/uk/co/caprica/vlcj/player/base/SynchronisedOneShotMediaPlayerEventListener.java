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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import java.util.concurrent.CountDownLatch;

/**
 * Implementation of a {@link OneShotMediaPlayerEventListener} that decrements a {@link CountDownLatch} on completion.
 * <p>
 * This can be used e.g. to wait one time for a particular media player event to occur.
 */
public abstract class SynchronisedOneShotMediaPlayerEventListener extends OneShotMediaPlayerEventListener {

    /**
     * Completion synchronisation latch.
     */
    private final CountDownLatch latch;

    /**
     * Create an event listener.
     *
     * @param latch completion synchronisation latch
     */
    public SynchronisedOneShotMediaPlayerEventListener(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    protected final void onDone(MediaPlayer mediaPlayer) {
        latch.countDown();
    }
}
