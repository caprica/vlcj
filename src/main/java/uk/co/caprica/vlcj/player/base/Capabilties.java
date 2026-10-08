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

import uk.co.caprica.vlcj.binding.internal.libvlc_capability_t;

/**
 * Media player capabilities.
 */
public final class Capabilties {

    private final boolean canSeek;

    private final boolean canPause;

    private final boolean canChangeRate;

    private final boolean canRewind;

    public static Capabilties capabilities(int capabilities) {
        return new Capabilties(
            (capabilities & libvlc_capability_t.libvlc_capability_seek) != 0,
            (capabilities & libvlc_capability_t.libvlc_capability_pause) != 0,
            (capabilities & libvlc_capability_t.libvlc_capability_change_rate) != 0,
            (capabilities & libvlc_capability_t.libvlc_capability_rewind) != 0
        );
    }

    private Capabilties(boolean canSeek, boolean canPause, boolean canChangeRate, boolean canRewind) {
        this.canSeek = canSeek;
        this.canPause = canPause;
        this.canChangeRate = canChangeRate;
        this.canRewind = canRewind;
    }

    public boolean canSeek() {
        return canSeek;
    }

    public boolean canPause() {
        return canPause;
    }

    public boolean canChangeRate() {
        return canChangeRate;
    }

    public boolean canRewind() {
        return canRewind;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(super.toString()).append('[');
        sb.append("canSeek=").append(canSeek).append(',');
        sb.append("canPause=").append(canPause).append(',');
        sb.append("canChangeRate=").append(canChangeRate).append(',');
        sb.append("canRewind=").append(canRewind).append(']');
        return sb.toString();
    }
}
