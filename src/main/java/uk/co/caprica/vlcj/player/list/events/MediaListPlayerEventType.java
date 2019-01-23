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

package uk.co.caprica.vlcj.player.list.events;

// FIXME obsolete, remove

/**
 * Enumeration of media list player event bit-masks.
 */
public enum MediaListPlayerEventType {

    /**
     *
     */
    NONE          (0x0000000000000000L),

    /**
     *
     */
    NEXT_ITEM_SET (0x0000000000000001L),

    /**
     *
     */
    ALL           (0xffffffffffffffffL);

    /**
     * Bit-mask.
     */
    private long value;

    /**
     * Create an enumerated value.
     *
     * @param value bit-mask
     */
    private MediaListPlayerEventType(long value) {
        this.value = value;
    }

    /**
     * Get the bit-mask.
     *
     * @return value
     */
    public final long value() {
        return value;
    }

    /**
     * Get a bit-mask for one or more event types.
     *
     * @param types one or more event types
     * @return bit-mask
     */
    public static long events(MediaListPlayerEventType... types) {
        long eventMask = 0;
        for(MediaListPlayerEventType type : types) {
            eventMask |= type.value();
        }
        return eventMask;
    }

    /**
     * Get an inverse bit-mask for one or more event types.
     *
     * @param types one or more event types
     * @return bit-mask
     */
    public static long notEvents(MediaListPlayerEventType... types) {
        long eventMask = ALL.value;
        for(MediaListPlayerEventType type : types) {
            eventMask &= type.value() ^ -1;
        }
        return eventMask;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>true</code> if the type value is set in the bit-mask, otherwise <code>false</code>
     */
    public static boolean set(long value, MediaListPlayerEventType type) {
        return (value & type.value()) != 0;
    }

    /**
     * Test whether or not a media player event type is set in a bit-mask.
     *
     * @param value bit-mask
     * @param type event type to test for
     * @return <code>false</code> if the type value is set in the bit-mask, otherwise <code>true</code>
     */
    public static boolean notSet(long value, MediaListPlayerEventType type) {
        return (value & type.value()) == 0;
    }
}
