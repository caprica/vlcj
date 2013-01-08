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

package uk.co.caprica.vlcj.test.aspect;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A test for video aspect ratio.
 * <p>
 * The red colouring on the video canvas shows the unused portion of the video surface - the client
 * application can resize the video canvas to reclaim this unused area without affecting the video
 * size/aspect (ordinarily resizing would stretch or compress the video).
 * <p>
 * Any black area shown on the top/left/bottom/right of the video are the black bars present in the
 * source material.
 * <p>
 * The panel with the white background is simply to provide context.
 */
public class AspectTest extends VlcjTest {

    /**
     * The standard aspect ratios.
     */
    private static final String[][] ASPECTS = {
        {"<choose...>",     null },
        {      "16:10",  "16:10" },
        {      "16:9" ,  "16:9"  },
        {    "1.85:1" , "185:100"},
        {    "2.21:1" , "221:100"},
        {    "2.35:1" , "235:100"},
        {    "2.39:1" , "239:100"},
        {       "5:3" ,   "5:3"  },
        {       "4:3" ,   "4:3"  },
        {       "5:4" ,   "5:4"  },
        {       "1:1" ,   "1:1"  }
    };

    private static final String HELP_TEXT =
        "<html>Select a standard apsect ratio from the list box, or enter a custom aspect ratio and press enter/return.<br/><br/>" +
      	"For the custom aspect ratio, use:<ul>" +
      	"<li>W:H, e.g. 16:9 and the values must be integers</li>" +
      	"</ul></html>";

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface videoSurface;

    private JFrame frame;
    private JPanel contentPane;
    private JPanel videoPane;
    private Canvas videoCanvas;
    private JPanel controlsPane;
    private JLabel standardAspectLabel;
    private JComboBox standardAspectComboBox;
    private JLabel customAspectLabel;
    private JTextField customAspectTextField;
    private JButton pauseButton;
    private JLabel helpText;

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(0);
        }

        setLookAndFeel();

        final String mrl = args[0];

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AspectTest().start(mrl);
            }
        });
    }

    @SuppressWarnings("serial")
    public AspectTest() {
        factory = new MediaPlayerFactory("--no-video-title-show");
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        videoPane = new JPanel();
        videoPane.setBorder(new CompoundBorder(new LineBorder(Color.black, 2), new EmptyBorder(16, 16, 16, 16)));
        videoPane.setLayout(new BorderLayout());
        videoPane.setBackground(Color.white);

        videoCanvas = new Canvas();
        videoCanvas.setBackground(Color.red);
        videoCanvas.setSize(720, 350);

        videoPane.add(videoCanvas, BorderLayout.CENTER);

        videoSurface = factory.newVideoSurface(videoCanvas);

        mediaPlayer.setVideoSurface(videoSurface);

        standardAspectLabel = new JLabel("Standard Aspect:");
        standardAspectLabel.setDisplayedMnemonic('s');

        standardAspectComboBox = new JComboBox(ASPECTS);
        standardAspectComboBox.setEditable(false);
        standardAspectComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String[] val = (String[])value;
                l.setText(val[0]);
                return l;
            }
        });

        standardAspectLabel.setLabelFor(standardAspectComboBox);

        customAspectLabel = new JLabel("Custom Aspect:");
        customAspectLabel.setDisplayedMnemonic('c');

        customAspectTextField = new JTextField(10);
        customAspectTextField.setFocusAccelerator('c');

        pauseButton = new JButton("Pause");
        pauseButton.setMnemonic('p');

        controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));
        controlsPane.add(standardAspectLabel);
        controlsPane.add(Box.createHorizontalStrut(4));
        controlsPane.add(standardAspectComboBox);
        controlsPane.add(Box.createHorizontalStrut(12));
        controlsPane.add(customAspectLabel);
        controlsPane.add(Box.createHorizontalStrut(4));
        controlsPane.add(customAspectTextField);
        controlsPane.add(Box.createHorizontalStrut(4));
        controlsPane.add(pauseButton);

        helpText = new JLabel(HELP_TEXT);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setLayout(new BorderLayout(16, 16));
        contentPane.add(helpText, BorderLayout.NORTH);
        contentPane.add(videoPane, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        frame = new JFrame("vlcj aspect ratio test");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        standardAspectComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = standardAspectComboBox.getSelectedItem();
                if(selectedItem != null) {
                    String[] value = (String[])selectedItem;
                    mediaPlayer.setAspectRatio(value[1]);
                }
            }
        });

        customAspectTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = customAspectTextField.getText();
                mediaPlayer.setAspectRatio(value);
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.pause();
            }
        });
    }

    private void start(String mrl) {
        frame.setVisible(true);

        mediaPlayer.playMedia(mrl);
    }
}
