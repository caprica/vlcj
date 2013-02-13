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

package uk.co.caprica.vlcj.test.bare;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.VlcjTest;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * A minimal test with video output embedded in a Java frame. This test uses the
 * raw bindings rather than any higher level framework provided by vlcj.
 * <p>
 * This is only used for testing.
 * <p>
 * Specify a media file as the only command-line argument.
 */
public class BareBonesEmbeddedTest extends VlcjTest {

    private final LibVlc libvlc;
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
        libvlc = LibVlc.INSTANCE;

        if(!RuntimeUtil.isMac()) {
            instance = libvlc.libvlc_new(0, new String[] {});
        }
        else {
            instance = libvlc.libvlc_new(1, new String[] {"--vout=macosx"});
        }

        mediaPlayer = libvlc.libvlc_media_player_new(instance);

        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);

        f = new JFrame("Bare Bones Test");
        f.setLayout(new BorderLayout());
        f.add(canvas, BorderLayout.CENTER);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                libvlc.libvlc_media_player_stop(mediaPlayer);
                libvlc.libvlc_media_player_release(mediaPlayer);
                libvlc.libvlc_release(instance);
                System.exit(0);
            }
        });
        f.pack(); // This makes the component "displayable" so we can get its component ID now rather than later when it is shown
        f.setSize(800, 600);

        int videoSurfaceComponentId = (int)Native.getComponentID(canvas);

        if(RuntimeUtil.isNix()) {
            libvlc.libvlc_media_player_set_xwindow(mediaPlayer, videoSurfaceComponentId);
        }
        else if(RuntimeUtil.isMac()) {
            libvlc.libvlc_media_player_set_nsobject(mediaPlayer, videoSurfaceComponentId);
        }
        else if(RuntimeUtil.isWindows()) {
            libvlc.libvlc_media_player_set_hwnd(mediaPlayer, Pointer.createConstant(videoSurfaceComponentId));
        }
    }

    private void run(String mrl) {
        f.setVisible(true);

        try {
            Thread.sleep(1000); // Just in case...
        }
        catch(InterruptedException e) {
        }

        libvlc_media_t media = libvlc.libvlc_media_new_path(instance, mrl);
        libvlc.libvlc_media_player_set_media(mediaPlayer, media);
        libvlc.libvlc_media_release(media);
        libvlc.libvlc_media_player_play(mediaPlayer);
    }
}
