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

import uk.co.caprica.vlcj.enums.MediaSlaveType;

// FIXME rename to simple add?

public final class SlaveService extends BaseService {

    SlaveService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Add an input slave to the current media.
     * <p>
     * The success of this call does not indicate that the slave being added is actually valid or not, it simply
     * associates a slave URI with the current media player (for example, a sub-title file will not be parsed and
     * checked for validity during this call).
     * <p>
     * If the URI represents a local file, it <em>must</em> be of the form "file://" otherwise it will not work, so this
     * will work:
     * <pre>
     *     file:///home/movies/movie.srt
     * </pre>
     * This will not work:
     * <pre>
     *     file:/home/movies/movie.srt
     * </pre>
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
