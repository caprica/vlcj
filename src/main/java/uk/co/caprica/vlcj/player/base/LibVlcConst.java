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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

/**
 * Various constants defined by LibVLC, useful for example to set ranges for slider components.
 */
public interface LibVlcConst {

    int MIN_VOLUME = 0;
    int MAX_VOLUME = 200;

    float MIN_CONTRAST = 0.0f;
    float MAX_CONTRAST = 2.0f;

    float MIN_BRIGHTNESS = 0.0f;
    float MAX_BRIGHTNESS = 2.0f;

    float MIN_HUE = -180;
    float MAX_HUE = 180;

    float MIN_SATURATION = 0.0f;
    float MAX_SATURATION = 3.0f;

    float MIN_GAMMA = 0.01f;
    float MAX_GAMMA = 10.0f;

    float MIN_GAIN = -20.0f;
    float MAX_GAIN = 20.0f;

}
