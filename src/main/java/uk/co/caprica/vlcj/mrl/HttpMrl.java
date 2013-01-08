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
 * Implementation of an HTTP media resource locator.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new HttpMrl().host("www.myhost.com")
 *                           .port("8080")
 *                           .path("/media/example.mp4")
 *                           .value();
 * </pre>
 * This will generate <code>"http://www.myhost.com:8080/media/example.mp4"</code>.
 */
public class HttpMrl extends UrlMrl {

    /**
     *
     */
    private static final String HTTP_TYPE = "http";

    /**
     *
     */
    public HttpMrl() {
        type(HTTP_TYPE);
    }
}
