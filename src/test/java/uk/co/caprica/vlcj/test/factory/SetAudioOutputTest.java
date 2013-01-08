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

package uk.co.caprica.vlcj.test.factory;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.AudioDevice;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Test setting audio outputs.
 * <p>
 * Specify a single MRL on the command-line.
 * <p>
 * Select an audio output from the menu and play the media.
 * <p>
 * <strong>This does NOT work because of a bug or incomplete implementation in libvlc
 * currently.</strong>
 */
public class SetAudioOutputTest extends VlcjTest {

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;
    private final List<AudioOutput> audioOutputs;

    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas canvas;
    private final JPanel controlsPanel;
    private final JLabel audioOutputLabel;
    private final JTextField audioOutputTextField;
    private final JLabel audioDeviceLabel;
    private final JTextField audioDeviceTextField;
    private final JButton stopButton;
    private final JButton playButton;

    private final JMenuBar menuBar;

    public static void main(final String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }

        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetAudioOutputTest(args[0]).start();
            }
        });
    }

    public SetAudioOutputTest(final String mrl) {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.newEmbeddedMediaPlayer();
        audioOutputs = factory.getAudioOutputs();

        canvas = new Canvas();
        canvas.setBackground(Color.black);

        controlsPanel = new JPanel();
        controlsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));

        audioOutputLabel = new JLabel("Audio Output Name:");
        audioOutputLabel.setDisplayedMnemonic('n');
        audioOutputTextField = new JTextField(10);
        audioOutputTextField.setFocusAccelerator('n');
        audioOutputTextField.setEditable(false);
        audioOutputLabel.setLabelFor(audioOutputTextField);

        audioDeviceLabel = new JLabel("Audio Device ID:");
        audioDeviceLabel.setDisplayedMnemonic('d');
        audioDeviceLabel.setDisplayedMnemonicIndex(6);
        audioDeviceTextField = new JTextField(10);
        audioDeviceTextField.setFocusAccelerator('d');
        audioDeviceTextField.setEditable(false);
        audioDeviceLabel.setLabelFor(audioDeviceTextField);

        stopButton = new JButton("Stop");
        stopButton.setMnemonic('s');
        playButton = new JButton("Play");
        playButton.setMnemonic('p');

        controlsPanel.add(audioOutputLabel);
        controlsPanel.add(Box.createHorizontalStrut(4));
        controlsPanel.add(audioOutputTextField);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(audioDeviceLabel);
        controlsPanel.add(Box.createHorizontalStrut(4));
        controlsPanel.add(audioDeviceTextField);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(stopButton);
        controlsPanel.add(Box.createHorizontalStrut(8));
        controlsPanel.add(playButton);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        contentPane.setLayout(new BorderLayout(8, 8));
        contentPane.setBackground(Color.black);
        contentPane.add(canvas, BorderLayout.CENTER);
        contentPane.add(controlsPanel, BorderLayout.SOUTH);

        videoSurface = factory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);

        menuBar = new JMenuBar();

        JMenu audioOutputMenu = new JMenu("Audio");
        audioOutputMenu.setMnemonic('a');

        ActionListener audioMenuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem)e.getSource();
                String audioOutputName = (String)source.getClientProperty("AudioOutputName");
                String audioDeviceId = (String)source.getClientProperty("AudioDeviceId");

                mediaPlayer.setAudioOutput(audioOutputName);
                if(audioDeviceId != null) {
                    mediaPlayer.setAudioOutputDevice(audioOutputName, audioDeviceId);
                }

                audioOutputTextField.setText(audioOutputName);
                audioDeviceTextField.setText(audioDeviceId);

                // If you do not revalidate after setting the text, bizarrely the menu
                // appears behind the heavy-weight Canvas
                controlsPanel.revalidate();
            }
        };

        for(AudioOutput audioOutput : audioOutputs) {
            List<AudioDevice> devices = audioOutput.getDevices();
            if(devices.isEmpty()) {
                JMenuItem audioOutputMenuItem = new JMenuItem(audioOutput.getDescription());
                audioOutputMenuItem.putClientProperty("AudioOutputName", audioOutput.getName());
                audioOutputMenu.add(audioOutputMenuItem);
                audioOutputMenuItem.addActionListener(audioMenuListener);
            }
            else {
                JMenu audioOutputMenuItem = new JMenu(audioOutput.getDescription());
                for(AudioDevice audioDevice : audioOutput.getDevices()) {
                    JMenuItem audioDeviceMenuItem = new JMenuItem("<html><b>" + audioDevice.getDeviceId() + "</b>&nbsp;&nbsp;<i>" + audioDevice.getLongName() + "</i></html>");
                    audioDeviceMenuItem.putClientProperty("AudioOutputName", audioOutput.getName());
                    audioDeviceMenuItem.putClientProperty("AudioDeviceId", audioDevice.getDeviceId());
                    audioOutputMenuItem.add(audioDeviceMenuItem);
                    audioDeviceMenuItem.addActionListener(audioMenuListener);
                }
                audioOutputMenu.add(audioOutputMenuItem);
            }
        }

        menuBar.add(audioOutputMenu);

        frame = new JFrame("Audio Outputs Test");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setJMenuBar(menuBar);
        frame.setContentPane(contentPane);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                factory.release();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.stop();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.playMedia(mrl);
            }
        });
    }

    private void start() {
        frame.setVisible(true);
    }
}
