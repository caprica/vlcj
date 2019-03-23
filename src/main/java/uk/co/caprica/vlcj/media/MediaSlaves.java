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

package uk.co.caprica.vlcj.media;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.NativeString;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_slave_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_slaves_get;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_slaves_release;

final class MediaSlaves {

    private MediaSlaves() {
    }

    static List<MediaSlave> getMediaSlaves(libvlc_media_t media) {
        PointerByReference slavesPointer = new PointerByReference();
        int numberOfSlaves = libvlc_media_slaves_get(media, slavesPointer);
        List<MediaSlave> result = new ArrayList<MediaSlave>(numberOfSlaves);
        if (numberOfSlaves > 0) {
            Pointer[] pointers = slavesPointer.getValue().getPointerArray(0, numberOfSlaves);
            for (Pointer pointer : pointers) {
                libvlc_media_slave_t slave = new libvlc_media_slave_t(pointer);
                result.add(new MediaSlave(NativeString.copyNativeString(slave.psz_uri), MediaSlaveType.mediaSlaveType(slave.i_type), slave.i_priority));
            }
        }
        // In this case the native structure must be freed even if the count is zero
        libvlc_media_slaves_release(slavesPointer.getValue(), numberOfSlaves);
        return result;
    }

}
