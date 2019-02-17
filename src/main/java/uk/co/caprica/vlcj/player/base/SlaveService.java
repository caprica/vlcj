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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.media.MediaSlavePriority;
import uk.co.caprica.vlcj.media.MediaSlaveType;

// FIXME rename to simple add?

/**
 * Behaviour pertaining to media slaves.
 */
public final class SlaveService extends BaseService {

    SlaveService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Add an input slave to the current media.
     * <p>
     * See {@link uk.co.caprica.vlcj.media.SlaveService#add(MediaSlaveType, MediaSlavePriority, String)}  for further
     * important information regarding this method.
     *
     * @param type type of slave to add
     * @param uri URI of the slave to add
     * @param select <code>true</code> if this slave should be automatically selected when added
     * @return <code>true</code> on success; <code>false</code> otherwise
     */
    public boolean addSlave(MediaSlaveType type, String uri, boolean select) {
        return libvlc.libvlc_media_player_add_slave(mediaPlayerInstance, type.intValue(), uri, select ? 1 : 0) == 0;
    }

}