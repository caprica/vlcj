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

package uk.co.caprica.vlcj.test.xlib;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.fullscreen.x.XFullScreenStrategy;

import javax.swing.*;

/**
 * Basic test to use the native X-Windows API to set a full-screen window.
 */
public class XFullScreenTest {

    public static void main(String[] args) throws Exception {
        // Creating a factory ensures LibVlc is property initialised, and that LibX is properly initialised on Linux
        MediaPlayerFactory factory = new MediaPlayerFactory();

        JFrame f = new JFrame("LibX Display Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocation(100, 100);
        f.setSize(400, 200);
        f.setVisible(true);

        Thread.sleep(3000);

        XFullScreenStrategy strategy = new XFullScreenStrategy(f);
        strategy.enterFullScreenMode();

        Thread.sleep(3000);

        strategy.exitFullScreenMode();
    }

}
