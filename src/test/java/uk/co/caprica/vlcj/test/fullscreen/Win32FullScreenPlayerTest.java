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

package uk.co.caprica.vlcj.test.fullscreen;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.fullscreen.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static uk.co.caprica.vlcj.player.component.MediaPlayerSpecs.embeddedMediaPlayerSpec;

/**
 * An example of using the native "Win32" full-screen strategy.
 * <p>
 * This uses the native Win32 API.
 */
public class Win32FullScreenPlayerTest extends VlcjTest {

    private static Win32FullScreenPlayerTest app;

    private final JFrame frame;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Specify an MRL to play");
            System.exit(1);
        }

        final String mrl = args[0];

        app = new Win32FullScreenPlayerTest(mrl);
    }

    public Win32FullScreenPlayerTest(String mrl) {
        frame = new JFrame("Win32 Full Screen Strategy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.mediaPlayer().controls().stop();
                mediaPlayerComponent.release();
            }
        });

        mediaPlayerComponent = embeddedMediaPlayerSpec()
            .withFullScreenStrategy(new Win32FullScreenStrategy(frame))
            .embeddedMediaPlayer();

        frame.setContentPane(mediaPlayerComponent);

        frame.setVisible(true);

        mediaPlayerComponent.mediaPlayer().media().play(mrl);
        mediaPlayerComponent.mediaPlayer().fullScreen().set(true);
    }

}
