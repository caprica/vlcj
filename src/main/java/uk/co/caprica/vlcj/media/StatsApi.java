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

package uk.co.caprica.vlcj.media;

import com.sun.jna.ptr.LongByReference;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_filestat_e;

import static uk.co.caprica.vlcj.binding.internal.libvlc_media_filestat_e.libvlc_media_filestat_mtime;
import static uk.co.caprica.vlcj.binding.internal.libvlc_media_filestat_e.libvlc_media_filestat_size;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_get_filestat;

/**
 * Behaviour pertaining to media stat values.
 */
public final class StatsApi extends BaseApi {

    StatsApi(Media media) {
        super(media);
    }

    /**
     * Get the media file modified time.
     *
     * @return file modified time, or -1 on error
     */
    public long mtime() {
        return stat(libvlc_media_filestat_mtime);
    }

    /**
     * Get the media file size.
     *
     * @return file size, or -1 on error
     */
    public long size() {
        return stat(libvlc_media_filestat_size);
    }

    private long stat(libvlc_media_filestat_e type) {
        LongByReference out = new LongByReference();
        int result = libvlc_media_get_filestat(mediaInstance, type.intValue(), out);
        if (result == 1) {
            return out.getValue();
        } else {
            return -1;
        }
    }
}
