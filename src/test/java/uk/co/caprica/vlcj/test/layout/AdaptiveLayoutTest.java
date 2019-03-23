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

package uk.co.caprica.vlcj.test.layout;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static uk.co.caprica.vlcj.player.component.MediaPlayerSpecs.embeddedMediaPlayerSpec;

/**
 * An example showing one way to solve the problem of needing to "move" a media player video
 * surface.
 * <p>
 * It is not possible to change the component hierarchy of a video surface (Canvas) in any way (e.g.
 * by moving to another parent, showing/hiding and so on) and expect the video to continue playing.
 * <p>
 * If the component hierarchy changes, you must stop and play the media player (this is not a vlcj
 * limitation, it is an inherent limitation of libvlc and AWT combined).
 * <p>
 * The most common use case is to have multiple media players in one view, then zoom/expand one of
 * those media players to occupy the whole window.
 * <p>
 * One solution to solve this issue is to use a custom layout manager to change the bounds (move
 * and/or resize) an existing video surface without changing it's component hierarchy.
 * <p>
 * This example shows one such possible implementation.
 * <p>
 * Click a video panel to zoom it, press the Escape key to return to the grid view.
 * <p>
 * The strategy in this example is: when one video play is focused, resize the other video players
 * to a size of 1x1 (setting the size to 0x0 will invalidate the video surface and cause playback
 * to fail), move the video player to 0x0, and then use a CardLayout to switch from the video view
 * to a blank container (without this you might still see colour changes in the 1x1 video player
 * windows).
 * <p>
 * The other videos keep playing, but they do not have to.
 * <p>
 * The key point is that this example demonstrates how you can achieve the effect of moving,
 * resizing, showing or hiding video surfaces without invalidating the video surface component and
 * therefore without causing video playback to fail.
 * <p>
 * Be warned, this example is somewhat of a lash-up for purposes of this demo only.
 * <p>
 * Be warned also that this example uses multiple in-process media players in the same application
 * and this is not without it's own well known risks.
 */
@SuppressWarnings("serial")
public class AdaptiveLayoutTest extends VlcjTest {

    private static final String[] mrls = {
        // Put your MRL's here, this example uses 12
    };

    private final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

    private final JFrame frame;
    private final JPanel cp;

    private final ZoomLayoutManager layoutManager;

    private final List<VideoPane> videoPanes = new ArrayList<VideoPane>();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdaptiveLayoutTest().start();
            }
        });
    }

    private AdaptiveLayoutTest() {
        cp = new JPanel();
        cp.setBackground(Color.black);
        cp.setBorder(new EmptyBorder(16, 16, 16, 16));

        InputMap im = cp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "blur");

        ActionMap am = cp.getActionMap();
        am.put("blur", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layoutManager.blur();
                cp.revalidate();
            }
        });

        int rows = 3; // Change this to suit
        int cols = 4;

        layoutManager = new ZoomLayoutManager(rows, cols);
        cp.setLayout(layoutManager);

        for(int i = 0; i < rows * cols; i++) {
            VideoPane videoPane = new VideoPane(i);
            videoPanes.add(videoPane);
            cp.add(videoPane, String.valueOf(i));
        }

        frame = new JFrame("Custom Layout Test");
        frame.setContentPane(cp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void start() {
        frame.setSize(1200, 800);
        frame.setVisible(true);

        for(int i = 0; i < videoPanes.size() && i < mrls.length; i++) {
            videoPanes.get(i).mediaPlayer().media().play(mrls[i]);
        }
    }

    class VideoPane extends JPanel {

        private final int id;

        private final EmbeddedMediaPlayerComponent mediaPlayer;

        VideoPane(int id) {
            this.id = id;

            setBackground(Color.black);
            setBorder(BorderFactory.createLineBorder(Color.white));

            setLayout(new CardLayout());

            JPanel vp = new JPanel();
            vp.setLayout(new BorderLayout());
            vp.setBackground(Color.pink);

            JPanel top = new JPanel();
            top.setBorder(BorderFactory.createEmptyBorder(2,  6,  2,  6));
            top.setBackground(Color.black);
            top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

            JLabel label = new JLabel(String.valueOf(id+1));
            label.setForeground(Color.white);
            label.setFont(new Font("Sansserif", Font.BOLD, 12));
            top.add(label);
            vp.add(top, BorderLayout.NORTH);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    layoutManager.focus(VideoPane.this.id);
                    cp.revalidate();
                }
            });

            mediaPlayer = new EmbeddedMediaPlayerComponent(embeddedMediaPlayerSpec().withFactory(mediaPlayerFactory));

            mediaPlayer.videoSurfaceComponent().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON3) {
                        mediaPlayer.mediaPlayer().controls().skipTime(10000);
                    }
                    else {
                        layoutManager.focus(VideoPane.this.id);
                        cp.revalidate();
                    }
                }
            });

            vp.add(mediaPlayer, BorderLayout.CENTER);

            add(vp, "video");

            JPanel p = new JPanel();
            p.setBackground(Color.pink);
            add(p, "hide");
        }

        MediaPlayer mediaPlayer() {
            return mediaPlayer.mediaPlayer();
        }
    }

    class ZoomLayoutManager implements LayoutManager {

        private final int hgap = 16;
        private final int vgap = 16;

        private final int rows;
        private final int cols;

        private final List<Component> components;

        private int focusIndex = -1;

        public ZoomLayoutManager(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.components = new ArrayList<Component>(rows * cols);
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            components.add(comp);
        }

        @Override
        public void removeLayoutComponent(Component comp) {
            components.remove(comp);
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return parent.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return parent.getMinimumSize();
        }

        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();

            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);

            int cellWidth = (width - ((cols - 1) * hgap)) / cols;
            int cellHeight = (height - ((rows - 1) * vgap)) / rows;

            int i = 0;
            int x = insets.left;
            int y = insets.top;

            if(focusIndex == -1) {
                outer: for(int r = 0; r < rows; r++) {
                    for(int c = 0; c < cols; c++) {
                        if(i < components.size()) {
                            Component comp = components.get(i++);
                            ((CardLayout)((JComponent) comp).getLayout()).show((Container) comp, "video");
                            comp.setBounds(x, y, cellWidth, cellHeight);
                            x += cellWidth + hgap;
                        }
                        else {
                            break outer;
                        }
                    }
                    x = insets.left;
                    y += cellHeight + vgap;
                }
            }
            else {
                for(i = 0; i < components.size(); i++) {
                    Component comp = components.get(i);
                    if(i != focusIndex) {
                        comp.setBounds(0, 0, 1, 1);
                        ((CardLayout)((JComponent) comp).getLayout()).show((Container) comp, "hide");
                    }
                    else {
                        ((CardLayout)((JComponent) comp).getLayout()).show((Container) comp, "video");
                        comp.setBounds(x, y, width, height);
                    }
                }
            }
        }

        public void focus(int index) {
            focusIndex = index;
        }

        public void blur() {
            focusIndex = -1;
        }
    }
}
