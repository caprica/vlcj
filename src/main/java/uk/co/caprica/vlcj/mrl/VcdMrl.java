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
 * Implementation of a media resource locator for Video CDs.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new VcdMrl().device("/media/dvd")
 *                          .startingPosition("S")
 *                          .number(1)
 *                          .value();
 * </pre>
 * This will generate <code>"vcd:///media/vcd/@S1"</code>.
 */
public class VcdMrl implements Mrl {

    /**
     *
     */
    private static final String VCD_TYPE = "vcd";

    /**
     *
     */
    private String device;

    /**
     *
     */
    private String startingPosition;

    /**
     *
     */
    private int number = -1;

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
    public final VcdMrl device(String device) {
        this.device = device;
        return this;
    }

    /**
     *
     *
     * @param startingPosition
     * @return this
     */
    public final VcdMrl startingPosition(String startingPosition) {
        this.startingPosition = startingPosition;
        return this;
    }

    /**
     *
     *
     * @param number
     * @return this
     */
    public final VcdMrl number(int number) {
        this.number = number;
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
        sb.append(VCD_TYPE);
        sb.append("://");
        sb.append(device);
        sb.append('@');
        sb.append(startingPosition);
        if(number != -1) {
            sb.append(number);
        }
        return sb.toString();
    }
}
