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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.bare;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.test.VlcjTest;

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
        LibVlc libvlc = LibVlc.INSTANCE;

        libvlc_instance_t instance = libvlc.libvlc_new(0, new String[] {});

        libvlc_media_player_t mediaPlayer = libvlc.libvlc_media_player_new(instance);
        libvlc_media_t media = libvlc.libvlc_media_new_path(instance, args[0]);
        libvlc.libvlc_media_player_set_media(mediaPlayer, media);
        libvlc.libvlc_media_player_play(mediaPlayer);

        Thread.sleep(10000);

        libvlc.libvlc_media_player_stop(mediaPlayer);
        libvlc.libvlc_media_release(media);
        libvlc.libvlc_media_player_release(mediaPlayer);

        libvlc.libvlc_release(instance);

        System.exit(0);
    }
}
