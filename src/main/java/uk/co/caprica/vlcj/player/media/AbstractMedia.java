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

package uk.co.caprica.vlcj.player.media;

/**
 * Base implementation of Media to handle media options.
 */
public abstract class AbstractMedia implements Media {

    /**
     * Media options, may be <code>null</code>.
     */
    private final String[] mediaOptions;

    /**
     * Create a media instance.
     *
     * @param mediaOptions zero or more media options
     */
    public AbstractMedia(String... mediaOptions) {
        this.mediaOptions = mediaOptions;
    }

    @Override
    public final String[] mediaOptions() {
        return mediaOptions;
    }
}
