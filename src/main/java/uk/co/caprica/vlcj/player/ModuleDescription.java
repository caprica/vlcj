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
 * Description of a module (e.g. audio/video filter).
 */
public class ModuleDescription {

    /**
     * Name.
     */
    private final String name;

    /**
     * Short name.
     */
    private final String shortName;

    /**
     * Long name.
     */
    private final String longName;

    /**
     * Help text.
     */
    private final String help;

    /**
     * Create a new module description
     *
     * @param name name
     * @param shortName short name
     * @param longName long name
     * @param help help text
     */
    public ModuleDescription(String name, String shortName, String longName, String help) {
        this.name = name;
        this.shortName = shortName;
        this.longName = longName;
        this.help = help;
    }

    /**
     * Get the module name.
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Get the module short name.
     *
     * @return short name
     */
    public String shortName() {
        return shortName;
    }

    /**
     * Get the module long name.
     *
     * @return long name
     */
    public String longName() {
        return longName;
    }

    /**
     * Get the module help text.
     *
     * @return help text
     */
    public String help() {
        return help;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("shortName=").append(shortName).append(',');
        sb.append("longName=").append(longName).append(',');
        sb.append("help=").append(help).append(']');
        return sb.toString();
    }
}
