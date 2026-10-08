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

package uk.co.caprica.vlcj.parser;

import uk.co.caprica.vlcj.binding.internal.libvlc_picture_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_picture_t;
import uk.co.caprica.vlcj.binding.support.types.size_t;
import uk.co.caprica.vlcj.media.Picture;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_picture_list_at;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_picture_list_count;

public final class Pictures {

    private final List<Picture> pictures = new ArrayList<>();

    public Pictures(libvlc_picture_list_t pictureListInstance) {
        int count = libvlc_picture_list_count(pictureListInstance).intValue();
        for (int i = 0; i < count; i++) {
            // This is a weak reference that must NOT be destroyed
            libvlc_picture_t pictureInstance = libvlc_picture_list_at(pictureListInstance, new size_t(i));
            pictures.add(new Picture(pictureInstance));
        }
    }

    public List<Picture> pictures() {
        return unmodifiableList(pictures);
    }
}
