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

package uk.co.caprica.vlcj.waiter.media;

import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Picture;

/**
 * Implementation of a condition that waits for the media player to report that a thumbnail picture has been generated
 * successfully.
 */
public class ThumbnailGeneratedWaiter extends MediaWaiter<Picture> {

    /**
     * Create a condition.
     *
     * @param media media
     */
    public ThumbnailGeneratedWaiter(Media media) {
        super(media);
    }

    @Override
    public void mediaThumbnailGenerated(Media media, Picture picture) {
        ready(picture);
    }

}
