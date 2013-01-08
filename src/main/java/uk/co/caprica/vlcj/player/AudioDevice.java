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
 * Description of an audio output device.
 */
public class AudioDevice {

    /**
     * Device identifier.
     */
    private final String deviceId;

    /**
     * Long name.
     */
    private final String longName;

    /**
     * Create an audio device.
     *
     * @param deviceId device identifier
     * @param longName long name
     */
    public AudioDevice(String deviceId, String longName) {
        this.deviceId = deviceId;
        this.longName = longName;
    }

    /**
     * Get the device identifier.
     *
     * @return device identifier
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Get the long name.
     *
     * @return long name
     */
    public String getLongName() {
        return longName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(60);
        sb.append(getClass().getSimpleName()).append('[');
        sb.append("deviceId=").append(deviceId).append(',');
        sb.append("longName=").append(longName).append(']');
        return sb.toString();
    }
}
