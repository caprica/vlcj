package uk.co.caprica.vlcj.medialist;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

// FIXME dammit this is a copy of package private class in base
final class MediaOptions {

    static boolean addMediaOptions(LibVlc libvlc, libvlc_media_t media, String... mediaOptions) {
        if (media != null) {
            if (mediaOptions != null) {
                for (String mediaOption : mediaOptions) {
                    libvlc.libvlc_media_add_option(media, mediaOption);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private MediaOptions() {
    }

}
