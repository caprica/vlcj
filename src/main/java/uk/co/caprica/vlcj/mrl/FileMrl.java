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
 * Implementation for a file MRL.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new FileMrl().file("the-file.mp4")
 *                           .value();
 * </pre>
 * This will generate <code>"file://the-file.mp4"</code>.
 */
public class FileMrl implements Mrl {

    /**
     * File path/name.
     */
    private String file;

    /**
     * MRL value.
     */
    private String value;

    /**
     * Create a new media resource locaator for a file.
     *
     * @param file file path/name
     * @return this
     */
    public final FileMrl file(String file) {
        this.file = file;
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
        sb.append("file");
        sb.append("://");
        sb.append(file);
        return sb.toString();
    }
}
