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

package uk.co.caprica.vlcj.test.quickstart;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;

/**
 * Minimal quick-start example.
 */
public class Example1 {

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(String[] args) {
        final String mrl = args[0];

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Example1().start(mrl);
            }
        });
    }

    public Example1() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        frame = new JFrame("vlcj quickstart");
        frame.setLocation(50, 50);
        frame.setSize(1400, 800);
        frame.setContentPane(mediaPlayerComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void start(String mrl) {
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }
}
