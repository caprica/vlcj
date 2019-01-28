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

import uk.co.caprica.vlcj.enums.MediaSlaveType;
import uk.co.caprica.vlcj.model.MediaSlave;
import uk.co.caprica.vlcj.player.MediaResourceLocator;

import java.io.File;
import java.util.List;

public class SlaveService extends BaseService {

    SlaveService(Media media) {
        super(media);
    }

    public boolean add(MediaSlaveType type, int priority, String uri) {
        uri = MediaResourceLocator.encodeMrl(uri); // FIXME check if needed, maybe factor out it its own thing, since this usage is not specifically an MRL
        return libvlc.libvlc_media_slaves_add(mediaInstance, type.intValue(), priority, uri) == 0;
    }

    public boolean add(MediaSlaveType type, int priority, File file) {
        String uri = String.format("file://%s", file.getAbsolutePath());
        return libvlc.libvlc_media_slaves_add(mediaInstance, type.intValue(), priority, uri) == 0;
    }

    public void clear() {
        libvlc.libvlc_media_slaves_clear(mediaInstance);
    }

    public List<MediaSlave> get() {
        return MediaSlaves.getMediaSlaves(libvlc, mediaInstance);
    }

}
