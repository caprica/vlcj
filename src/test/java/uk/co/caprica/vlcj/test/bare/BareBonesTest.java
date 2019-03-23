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

package uk.co.caprica.vlcj.test.bare;

import com.sun.jna.StringArray;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.test.VlcjTest;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_path;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_play;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_media;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_stop;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_release;

/**
 * The most minimal test that uses the raw bindings rather than any higher level
 * framework provided by vlcj.
 * <p>
 * This is only used for testing.
 * <p>
 * Specify a media file as the only command-line argument.
 */
public class BareBonesTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        libvlc_instance_t instance = libvlc_new(0, new StringArray(new String[0]));

        libvlc_media_player_t mediaPlayer = libvlc_media_player_new(instance);
        libvlc_media_t media = libvlc_media_new_path(instance, args[0]);
        libvlc_media_player_set_media(mediaPlayer, media);
        libvlc_media_player_play(mediaPlayer);

        Thread.sleep(10000);

        libvlc_media_player_stop(mediaPlayer);
        libvlc_media_release(media);
        libvlc_media_player_release(mediaPlayer);

        libvlc_release(instance);

        System.exit(0);
    }
}
