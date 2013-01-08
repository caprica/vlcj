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

package uk.co.caprica.vlcj.test.xlib;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.runtime.x.LibXUtil;

/**
 * Basic test to use the native X-Windows API to set a full-screen window.
 */
public class XFullScreenTest {

    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame("LibX Display Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocation(100, 100);
        f.setSize(400, 200);
        f.setVisible(true);

        Thread.sleep(3000);
        LibXUtil.setFullScreenWindow(f, true);

        Thread.sleep(3000);
        LibXUtil.setFullScreenWindow(f, false);
    }
}
