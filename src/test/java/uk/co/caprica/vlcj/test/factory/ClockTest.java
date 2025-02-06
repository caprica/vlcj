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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.factory;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Simple test to dump out the libvlc clock.
 * <p>
 * Requires libvlc 1.2.x.
 */
public class ClockTest extends VlcjTest {

    public static void main(String[] args) throws Exception {
        final MediaPlayerFactory factory = new MediaPlayerFactory();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.printf("Clock: %d\n", factory.application().clock());
            }
        }, 0, 1, TimeUnit.SECONDS);

        Thread.sleep(10000);

        System.exit(0);
    }
}
