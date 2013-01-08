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

package uk.co.caprica.vlcj.player;

/**
 * Enumeration of deinterlace modes.
 * <p>
 * These are defined in "modules/video_filter/deinterlace.c".
 */
public enum DeinterlaceMode {

    /**
     *
     */
    DISCARD("discard"),

    /**
     *
     */
    BLEND("blend"),

    /**
     *
     */
    MEAN("mean"),

    /**
     *
     */
    BOB("bob"),

    /**
     *
     */
    LINEAR("linear"),

    /**
     *
     */
    X("x"),

    /**
     *
     */
    YADIF("yadif"),

    /**
     *
     */
    YADIF2X("yadif2x"),

    /**
     *
     */
    PHOSPHOR("phosphor"),

    /**
     *
     */
    IVTC("ivtc");

    /**
     * Native mode value.
     */
    private final String mode;

    /**
     * Create an enumerated value.
     *
     * @param mode native mode value
     */
    private DeinterlaceMode(String mode) {
        this.mode = mode;
    }

    /**
     * Get the native mode value.
     *
     * @return mode value
     */
    public final String mode() {
        return mode;
    }
}
