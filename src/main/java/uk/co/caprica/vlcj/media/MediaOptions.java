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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_add_option;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_add_option_flag;

final class MediaOptions {

    private MediaOptions() {
    }

    static boolean addMediaOptions(libvlc_media_t media, String... mediaOptions) {
        if (media != null) {
            if (mediaOptions != null) {
                for (String mediaOption : mediaOptions) {
                    libvlc_media_add_option(media, mediaOption);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean addMediaOptions(libvlc_media_t media, String[] mediaOptions, OptionFlag... flags) {
        if (media != null) {
            int flagsValue = flagsToInt(flags);
            for (String mediaOption : mediaOptions) {
                libvlc_media_add_option_flag(media, mediaOption, flagsValue);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean addMediaOption(libvlc_media_t media, String mediaOption, OptionFlag... flags) {
        if (media != null) {
            libvlc_media_add_option_flag(media, mediaOption, flagsToInt(flags));
            return true;
        } else {
            return false;
        }
    }

    private static int flagsToInt(OptionFlag... flags) {
        int result = 0;
        if (flags != null) {
            for (OptionFlag flag : flags) {
                result |= flag.intValue();
            }
        }
        return result;
    }

}
