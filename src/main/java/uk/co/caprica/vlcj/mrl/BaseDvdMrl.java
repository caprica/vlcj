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
 * Base implementation of a media resource locator for DVD MRLs.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 *
 * <pre>
 * String mrl = new BaseDvdMrl().type("dvdsimple").
 *                              .device("/media/dvd")
 *                              .title(0)
 *                              .chapter(3)
 *                              .angle(1)
 *                              .value();
 * </pre>
 *
 * This will generate <code>"dvdsimple:///media/dvd/@dev/cdrom#0:3:1"</code>.
 */
public abstract class BaseDvdMrl implements Mrl {

    /**
     *
     */
    private String type;

    /**
     *
     */
    private String device;

    /**
     *
     */
    private int title = -1;

    /**
     *
     */
    private int chapter = -1;

    /**
     *
     */
    private int angle = -1;

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
    public final BaseDvdMrl type(String type) {
        this.type = type;
        return this;
    }

    /**
     *
     *
     * @param device
     * @return this
     */
    public final BaseDvdMrl device(String device) {
        this.device = device;
        return this;
    }

    /**
     *
     *
     * @param title
     * @return this
     */
    public final BaseDvdMrl title(int title) {
        this.title = title;
        return this;
    }

    /**
     *
     *
     * @param chapter
     * @return this
     */
    public final BaseDvdMrl chapter(int chapter) {
        this.chapter = chapter;
        return this;
    }

    /**
     *
     *
     * @param angle
     * @return this
     */
    public final BaseDvdMrl angle(int angle) {
        this.angle = angle;
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
        sb.append(device);
        if(title != -1) {
            sb.append('#');
            sb.append(title);
            if(chapter != -1) {
                sb.append(':');
                sb.append(chapter);
                if(angle != -1) {
                    sb.append(':');
                    sb.append(angle);
                }
            }
        }
        return sb.toString();
    }
}
