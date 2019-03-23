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
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.test.VlcjTest;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_path;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_parse_with_options;
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
 * <p>
 * This code is enough, when running under Java7 only, to trigger a fatal JVM crash
 * on 32-bit Ubuntu - the associated segfault occurs in libc when invoked by the VLC
 * LUA metadata script.
 */
public class BareBonesParseTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        libvlc_instance_t instance = libvlc_new(0, new StringArray(new String[0]));

        libvlc_media_t media = libvlc_media_new_path(instance, args[0]);

        libvlc_media_parse_with_options(media, 0, 0); // <--- FATAL VM CRASH IF RUNNING on 32-bit Ubuntu and Java7

        Thread.sleep(1000);

        libvlc_media_release(media);

        libvlc_release(instance);

        System.exit(0);
    }

}
