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
 * Implementation for a web-style MRL.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new WebMrl().type("http")
 *                          .host("www.myhost.com")
 *                          .port("8080")
 *                          .path("/media/example.mp4")
 *                          .value();
 * </pre>
 * This will generate <code>"http://www.myhost.com:8080/media/example.mp4"</code>.
 */
public abstract class UrlMrl implements Mrl {

    /**
     *
     */
    private String type;

    /**
     *
     */
    private String host;

    /**
     *
     */
    private int port = -1;

    /**
     *
     */
    private String path;

    /**
     *
     */
    private String value;

    /**
     *
     *
     * @param type
     * @return this
     */
    public final UrlMrl type(String type) {
        this.type = type;
        return this;
    }

    /**
     *
     *
     * @param host
     * @return this
     */
    public final UrlMrl host(String host) {
        this.host = host;
        return this;
    }

    /**
     *
     *
     * @param port
     * @return this
     */
    public final UrlMrl port(int port) {
        this.port = port;
        return this;
    }

    /**
     *
     *
     * @param path
     * @return this
     */
    public final UrlMrl path(String path) {
        this.path = path;
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
        sb.append(type);
        sb.append("://");
        sb.append(host);
        if(port != -1) {
            sb.append(':');
            sb.append(port);
        }
        if(path != null) {
            sb.append(path);
        }
        return sb.toString();
    }
}
