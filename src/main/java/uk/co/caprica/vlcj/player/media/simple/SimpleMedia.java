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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.media.simple;

import uk.co.caprica.vlcj.player.MediaResourceLocator;
import uk.co.caprica.vlcj.player.media.AbstractMedia;

/**
 * Encapsulation of 'simple' media, i.e. media specified by a Media Resource Locator (MRL).
 */
public class SimpleMedia extends AbstractMedia {

    /**
     * Media Resource Locator.
     */
    private final String mrl;

    /**
     * Create a media instance.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media options
     */
    public SimpleMedia(String mrl, String... mediaOptions) {
        super(mediaOptions);
        // Encode the MRL if necessary (e.g. if it is a local file that contains Unicode characters)
        this.mrl = MediaResourceLocator.encodeMrl(mrl);
    }

    /**
     * Get the MRL.
     *
     * @return MRL
     */
    public String mrl() {
        return mrl;
    }

    @Override
    public String toString() {
        return new StringBuilder(40)
            .append(getClass().getSimpleName()).append('[')
            .append("mrl=").append(mrl).append(',')
            .append("mediaOptions=").append(mediaOptions()).append(']')
            .toString();
    }
}
