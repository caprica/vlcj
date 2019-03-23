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

package uk.co.caprica.vlcj.test.bare;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_new_path;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_play;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_hwnd;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_media;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_nsobject;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_set_xwindow;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_player_stop;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_media_release;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_release;

/**
 * A minimal test with video output embedded in a Java frame. This test uses the
 * raw bindings rather than any higher level framework provided by vlcj.
 * <p>
 * This is only used for testing.
 * <p>
 * Specify a media file as the only command-line argument.
 */
public class BareBonesEmbeddedTest extends VlcjTest {

    private final libvlc_instance_t instance;
    private final libvlc_media_player_t mediaPlayer;

    private final JFrame f;

    public static void main(String[] args) throws Exception {
        final String mrl = args[0];
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BareBonesEmbeddedTest().run(mrl);
            }
        });
    }

    public BareBonesEmbeddedTest() {
        if(!RuntimeUtil.isMac()) {
            instance = libvlc_new(0, new StringArray(new String[0]));
        }
        else {
            instance = libvlc_new(1, new StringArray(new String[] {"--vout=macosx"}));
        }

        mediaPlayer = libvlc_media_player_new(instance);

        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);

        f = new JFrame("Bare Bones Test");
        f.setLayout(new BorderLayout());
        f.add(canvas, BorderLayout.CENTER);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                libvlc_media_player_stop(mediaPlayer);
                libvlc_media_player_release(mediaPlayer);
                libvlc_release(instance);
                System.exit(0);
            }
        });
        f.pack(); // This makes the component "displayable" so we can get its component ID now rather than later when it is shown
        f.setSize(800, 600);

        int videoSurfaceComponentId = (int)Native.getComponentID(canvas);

        if(RuntimeUtil.isNix()) {
            libvlc_media_player_set_xwindow(mediaPlayer, videoSurfaceComponentId);
        }
        else if(RuntimeUtil.isMac()) {
            libvlc_media_player_set_nsobject(mediaPlayer, videoSurfaceComponentId);
        }
        else if(RuntimeUtil.isWindows()) {
            libvlc_media_player_set_hwnd(mediaPlayer, Pointer.createConstant(videoSurfaceComponentId));
        }
    }

    private void run(String mrl) {
        f.setVisible(true);

        try {
            Thread.sleep(1000); // Just in case...
        }
        catch(InterruptedException e) {
        }

        libvlc_media_t media = libvlc_media_new_path(instance, mrl);
        libvlc_media_player_set_media(mediaPlayer, media);
        libvlc_media_release(media);
        libvlc_media_player_play(mediaPlayer);
    }
}
