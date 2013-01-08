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
 * Implementation of a media resource locator for UDP streams.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new RtpMrl().groupAddress("234.0.0.1")
 *                          .port(1234)
 *                          .value();
 * </pre>
 * This will generate <code>"udp://@234.0.0.1:1234"</code>.
 * <p>
 * <strong>UDP is deprecated in VLC.</strong>
 */
@Deprecated
public class UdpMrl implements Mrl {

    /**
     *
     */
    private static final String UDP_TYPE = "udp";

    /**
     *
     */
    private String groupAddress;

    /**
     *
     */
    private int port;

    /**
     *
     */
    private String value;

    /**
     *
     *
     * @param groupAddress
     * @return this
     */
    public final UdpMrl groupAddress(String groupAddress) {
        this.groupAddress = groupAddress;
        return this;
    }

    /**
     *
     *
     * @param port
     * @return this
     */
    public final UdpMrl port(int port) {
        this.port = port;
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
        sb.append(UDP_TYPE);
        sb.append("://@");
        sb.append(groupAddress);
        sb.append(':');
        sb.append(port);
        return sb.toString();
    }
}
