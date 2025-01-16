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

/**
 * A listener for media player timer events.
 */
public interface WatchTimeListener {

    /**
     * Timer update.
     *
     * @param mediaPlayer media player that raised the event
     * @param timePoint event data
     * @param data opaque data
     */
    void watchTimeUpdate(MediaPlayer mediaPlayer, TimePoint timePoint, Long data);

    /**
     * Timer paused.
     *
     * @param mediaPlayer media player that raised the event
     * @param systemDate event data
     * @param data opaque data
     */
    void watchTimePaused(MediaPlayer mediaPlayer, long systemDate, Long data);

    /**
     * Timer seek.
     *
     * @param mediaPlayer media player that raised the event
     * @param timePoint event data
     * @param data opaque data
     */
    void watchTimeSeek(MediaPlayer mediaPlayer, TimePoint timePoint, Long data);
}
