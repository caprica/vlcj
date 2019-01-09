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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.factory;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Simple test of application identification methods.
 * <p>
 * This test simply checks that the methods don't fail.
 * <p>
 * This test requires libvlc 2.1.0 or later.
 */
public class ApplicationIdTest extends VlcjTest {

    public static void main(String[] args) {
        MediaPlayerFactory factory = new MediaPlayerFactory();

        factory.application().setUserAgent("Test Application", "Test Application HTTP");
        factory.application().setApplicationId("Test Application Id", "1.0.0", "icon.png");

        factory.release();
    }
}
