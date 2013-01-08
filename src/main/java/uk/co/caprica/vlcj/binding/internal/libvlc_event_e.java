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

package uk.co.caprica.vlcj.binding.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of native events.
 */
public enum libvlc_event_e {

    libvlc_MediaMetaChanged             (0x000),
    libvlc_MediaSubItemAdded            (0x001),
    libvlc_MediaDurationChanged         (0x002),
    libvlc_MediaParsedChanged           (0x003),
    libvlc_MediaFreed                   (0x004),
    libvlc_MediaStateChanged            (0x005),

    libvlc_MediaPlayerMediaChanged      (0x100),
    libvlc_MediaPlayerNothingSpecial    (0x101),
    libvlc_MediaPlayerOpening           (0x102),
    libvlc_MediaPlayerBuffering         (0x103),
    libvlc_MediaPlayerPlaying           (0x104),
    libvlc_MediaPlayerPaused            (0x105),
    libvlc_MediaPlayerStopped           (0x106),
    libvlc_MediaPlayerForward           (0x107),
    libvlc_MediaPlayerBackward          (0x108),
    libvlc_MediaPlayerEndReached        (0x109),
    libvlc_MediaPlayerEncounteredError  (0x10a),
    libvlc_MediaPlayerTimeChanged       (0x10b),
    libvlc_MediaPlayerPositionChanged   (0x10c),
    libvlc_MediaPlayerSeekableChanged   (0x10d),
    libvlc_MediaPlayerPausableChanged   (0x10e),
    libvlc_MediaPlayerTitleChanged      (0x10f),
    libvlc_MediaPlayerSnapshotTaken     (0x110),
    libvlc_MediaPlayerLengthChanged     (0x111),
    libvlc_MediaPlayerVout              (0x112),

    libvlc_MediaListItemAdded           (0x200),
    libvlc_MediaListWillAddItem         (0x201),
    libvlc_MediaListItemDeleted         (0x202),
    libvlc_MediaListWillDeleteItem      (0x203),

    libvlc_MediaListViewItemAdded       (0x300),
    libvlc_MediaListViewWillAddItem     (0x301),
    libvlc_MediaListViewItemDeleted     (0x302),
    libvlc_MediaListViewWillDeleteItem  (0x303),

    libvlc_MediaListPlayerPlayed        (0x400),
    libvlc_MediaListPlayerNextItemSet   (0x401),
    libvlc_MediaListPlayerStopped       (0x402),

    libvlc_MediaDiscovererStarted       (0x500),
    libvlc_MediaDiscovererEnded         (0x501),

    libvlc_VlmMediaAdded                (0x600),
    libvlc_VlmMediaRemoved              (0x601),
    libvlc_VlmMediaChanged              (0x602),
    libvlc_VlmMediaInstanceStarted      (0x603),
    libvlc_VlmMediaInstanceStopped      (0x604),
    libvlc_VlmMediaInstanceStatusInit   (0x605),
    libvlc_VlmMediaInstanceStatusOpening(0x606),
    libvlc_VlmMediaInstanceStatusPlaying(0x607),
    libvlc_VlmMediaInstanceStatusPause  (0x608),
    libvlc_VlmMediaInstanceStatusEnd    (0x609),
    libvlc_VlmMediaInstanceStatusError  (0x60a);

    private static final Map<Integer, libvlc_event_e> INT_MAP = new HashMap<Integer, libvlc_event_e>();

    static {
        for(libvlc_event_e event : libvlc_event_e.values()) {
            INT_MAP.put(event.intValue, event);
        }
    }

    public static libvlc_event_e event(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    private libvlc_event_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
