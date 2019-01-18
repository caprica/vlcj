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
    libvlc_MediaSubItemTreeAdded        (0x006),
    libvlc_MediaThumbnailGenerated      (0x007),

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
    libvlc_MediaPlayerScrambledChanged  (0x113),
    libvlc_MediaPlayerESAdded           (0x114),
    libvlc_MediaPlayerESDeleted         (0x115),
    libvlc_MediaPlayerESSelected        (0x116),
    libvlc_MediaPlayerCorked            (0x117),
    libvlc_MediaPlayerUncorked          (0x118),
    libvlc_MediaPlayerMuted             (0x119),
    libvlc_MediaPlayerUnmuted           (0x11a),
    libvlc_MediaPlayerAudioVolume       (0x11b),
    libvlc_MediaPlayerAudioDevice       (0x11c),
    libvlc_MediaPlayerChapterChanged    (0x11d),

    libvlc_MediaListItemAdded           (0x200),
    libvlc_MediaListWillAddItem         (0x201),
    libvlc_MediaListItemDeleted         (0x202),
    libvlc_MediaListWillDeleteItem      (0x203),
    libvlc_MediaListEndReached          (0x204),

    libvlc_MediaListViewItemAdded       (0x300),
    libvlc_MediaListViewWillAddItem     (0x301),
    libvlc_MediaListViewItemDeleted     (0x302),
    libvlc_MediaListViewWillDeleteItem  (0x303),

    libvlc_MediaListPlayerPlayed        (0x400),
    libvlc_MediaListPlayerNextItemSet   (0x401),
    libvlc_MediaListPlayerStopped       (0x402),

    libvlc_RendererDiscovererItemAdded  (0x502),
    libvlc_RendererDiscovererItemDeleted(0x503);

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

    libvlc_event_e(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

}
