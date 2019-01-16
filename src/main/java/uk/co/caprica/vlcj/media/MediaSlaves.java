package uk.co.caprica.vlcj.media;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_slave_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_slave_type_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.NativeString;

import java.util.ArrayList;
import java.util.List;

final class MediaSlaves {

    static List<MediaSlave> getMediaSlaves(LibVlc libvlc, libvlc_media_t media) {
        PointerByReference slavesPointer = new PointerByReference();
        int numberOfSlaves = libvlc.libvlc_media_slaves_get(media, slavesPointer);
        List<MediaSlave> result = new ArrayList<MediaSlave>(numberOfSlaves);
        if (numberOfSlaves > 0) {
            Pointer[] pointers = slavesPointer.getValue().getPointerArray(0, numberOfSlaves);
            for (Pointer pointer : pointers) {
                libvlc_media_slave_t slave = new libvlc_media_slave_t(pointer);
                result.add(new MediaSlave(NativeString.copyNativeString(slave.psz_uri), libvlc_media_slave_type_t.mediaSlaveType(slave.i_type), slave.i_priority));
            }
            libvlc.libvlc_media_slaves_release(slavesPointer.getValue(), numberOfSlaves);
        }
        return result;

    }
}
