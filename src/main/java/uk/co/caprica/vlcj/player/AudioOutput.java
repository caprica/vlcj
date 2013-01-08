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

import java.util.List;

/**
 * Description of an audio output.
 * <p>
 * An audio output has zero or more associated audio devices. Each device has a unique identifier
 * than can be used to select the required output device for a media player.
 */
public class AudioOutput {

    /**
     * Name.
     */
    private final String name;

    /**
     * Description.
     */
    private final String description;

    /**
     * Long name.
     */
    private final List<AudioDevice> devices;

    /**
     * Create an audio output.
     *
     * @param name name
     * @param description description
     * @param devices collection of audio devices for this output
     */
    public AudioOutput(String name, String description, List<AudioDevice> devices) {
        this.name = name;
        this.description = description;
        this.devices = devices;
    }

    /**
     * Get the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the collection of audio devices for this output.
     *
     * @return audio devices
     */
    public List<AudioDevice> getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("name=").append(name).append(',');
        sb.append("description=").append(description).append(',');
        sb.append("devices=").append(devices).append(']');
        return sb.toString();
    }
}
