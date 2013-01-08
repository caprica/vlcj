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

package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media resource locator for Audio CDs.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new CcdMrl().device("/dev/cdrom")
 *                          .track(3)
 *                          .value();
 * </pre>
 * This will generate <code>"cdda:///dev/cdrom/@3"</code>.
 * <p>
 * <strong>Note that vlc currently ignores the track number.</strong>
 */
public class CdMrl implements Mrl {

    /**
     *
     */
    private static final String CD_TYPE = "cdda";

    /**
     *
     */
    private String device;

    /**
     *
     */
    private int track = -1;

    /**
     *
     */
    private String value;

    /**
     *
     *
     * @param device
     * @return this
     */
    public final CdMrl device(String device) {
        this.device = device;
        return this;
    }

    /**
     *
     *
     * @param track
     * @return this
     */
    public final CdMrl track(int track) {
        this.track = track;
        return this;
    }

    @Override
    public final String value() {
        if(value == null) {
            value = constructValue();
        }
        return value;
    }

    /**
     * Construct the MRL from the internal state.
     *
     * @return media resource locator
     */
    private String constructValue() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(CD_TYPE);
        sb.append("://");
        sb.append(device);
        if(track != -1) {
            sb.append('@');
            sb.append(track);
        }
        return sb.toString();
    }
}
