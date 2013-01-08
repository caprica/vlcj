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

package uk.co.caprica.vlcj.test.visualisation;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An example of embedded audio visualisation.
 * <p>
 * Specify a single audio media item on the command line.
 * <p>
 * When running the application, choose a visualisation type, change the effect width/height if you
 * want, and hit "Play".
 */
public class VisualisationPlayer extends VlcjTest {

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;

    private final String mrl;

    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas canvas;
    private final JPanel controlsPane;
    private final JLabel typeLabel;
    private final JComboBox comboBox;
    private final JButton playButton;
    private final JLabel widthLabel;
    private final JTextField widthTextField;
    private final JLabel heightLabel;
    private final JTextField heightTextField;

    public static void main(final String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify a single audio MRL");
            System.exit(1);
        }

        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VisualisationPlayer(args[0]).start();
            }
        });
    }

    public VisualisationPlayer(String mrl) {
        this.mrl = mrl;

        factory = new MediaPlayerFactory("--no-video-title-show");
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        canvas = new Canvas();
        canvas.setBackground(Color.black);

        controlsPane = new JPanel();
        controlsPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));
        typeLabel = new JLabel("Type:");
        typeLabel.setDisplayedMnemonic('t');
        comboBox = new JComboBox(new String[] {"scope", "spectrometer", "spectrum", "vuMeter"});
        typeLabel.setLabelFor(comboBox);
        widthLabel = new JLabel("Width:");
        widthLabel.setDisplayedMnemonic('w');
        widthTextField = new JTextField(5);
        widthTextField.setFocusAccelerator('w');
        heightLabel = new JLabel("Height:");
        heightLabel.setDisplayedMnemonic('h');
        heightTextField = new JTextField(5);
        heightTextField.setFocusAccelerator('h');
        playButton = new JButton("Play");
        playButton.setMnemonic('p');
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPlay();
            }
        });
        controlsPane.add(typeLabel);
        controlsPane.add(comboBox);
        controlsPane.add(Box.createHorizontalStrut(8));
        controlsPane.add(widthLabel);
        controlsPane.add(widthTextField);
        controlsPane.add(Box.createHorizontalStrut(8));
        controlsPane.add(heightLabel);
        controlsPane.add(heightTextField);
        controlsPane.add(Box.createHorizontalStrut(8));
        controlsPane.add(playButton);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout(8, 8));
        contentPane.add(canvas, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        frame = new JFrame("vlcj audio visualisation");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setSize(850, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                factory.release();
            }
        });

        videoSurface = factory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);

        widthTextField.setText("1000");
        heightTextField.setText("100");
    }

    private void start() {
        frame.setVisible(true);
    }

    private void doPlay() {
        String[] options = {"audio-visual=visual", "effect-list=" + comboBox.getSelectedItem(), "effect-width=" + widthTextField.getText(), "effect-height=" + heightTextField.getText()};
        mediaPlayer.playMedia(mrl, options);
    }
}
