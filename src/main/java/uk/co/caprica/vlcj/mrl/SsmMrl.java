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
 * Implementation of a media resource locator for SSM streams.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new RtpMrl().serverAddress("myhost.com")
 *                          .multicastAddress("234.0.0.1")
 *                          .port(5401)
 *                          .value();
 * </pre>
 * This will generate <code>"rtp://myhost.com@234.0.0.1:5401"</code>.
 */
public class SsmMrl implements Mrl {

    /**
     *
     */
    private static final String RTP_TYPE = "rtp";

    /**
     *
     */
    private String serverAddress;

    /**
     *
     */
    private String multicastAddress;

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
     * @param serverAddress
     * @return this
     */
    public final SsmMrl serverAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    /**
     *
     *
     * @param multicastAddress
     * @return this
     */
    public final SsmMrl multicastAddress(String multicastAddress) {
        this.multicastAddress = multicastAddress;
        return this;
    }

    /**
     *
     *
     * @param port
     * @return this
     */
    public final SsmMrl port(int port) {
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
        sb.append(RTP_TYPE);
        sb.append("://");
        sb.append(serverAddress);
        sb.append('@');
        sb.append(multicastAddress);
        sb.append(':');
        sb.append(port);
        return sb.toString();
    }
}
