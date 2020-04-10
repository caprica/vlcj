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
 * Copyright 2009-2020 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.crop;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A test for video cropping.
 * <p>
 * The red colouring on the video canvas shows the unused portion of the video surface - the client
 * application can resize the video canvas to reclaim this unused area without affecting the video
 * size/aspect (ordinarily resizing would stretch or compress the video).
 * <p>
 * Any black area shown on the top/left/bottom/right of the video are the black bars present in the
 * source material - this is what we want to crop away.
 * <p>
 * The panel with the white background is simply to provide context.
 */
public class CropTest extends VlcjTest {

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;
    private VideoSurface videoSurface;

    private JFrame frame;
    private JPanel contentPane;
    private JPanel videoPane;
    private Canvas videoCanvas;
    private JPanel controlsPane1;
    private JLabel aspectNumeratorLabel;
    private JTextField ratioNumeratorTextField;
    private JLabel aspectDenominatorLabel;
    private JTextField ratioDenominatorTextField;
    private JButton setRatioButton;
    private JButton pauseButton;
    private JPanel controlsPane2;
    private JLabel xLabel;
    private JTextField xTextField;
    private JLabel yLabel;
    private JTextField yTextField;
    private JLabel wLabel;
    private JTextField wTextField;
    private JLabel hLabel;
    private JTextField hTextField;
    private JButton setWindowButton;
    private JPanel controlsPane3;
    private JLabel lLabel;
    private JTextField lTextField;
    private JLabel rLabel;
    private JTextField rTextField;
    private JLabel tLabel;
    private JTextField tTextField;
    private JLabel bLabel;
    private JTextField bTextField;
    private JButton setBorderButton;

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
                new CropTest().start(mrl);
            }
        });
    }

    @SuppressWarnings("serial")
    public CropTest() {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();

        videoPane = new JPanel();
        videoPane.setBorder(new CompoundBorder(new LineBorder(Color.black, 2), new EmptyBorder(16, 16, 16, 16)));
        videoPane.setLayout(new BorderLayout());
        videoPane.setBackground(Color.white);

        videoCanvas = new Canvas();
        videoCanvas.setBackground(Color.red);
        videoCanvas.setSize(720, 350);

        videoPane.add(videoCanvas, BorderLayout.CENTER);

        videoSurface = factory.videoSurfaces().newVideoSurface(videoCanvas);

        mediaPlayer.videoSurface().set(videoSurface);

        aspectNumeratorLabel = new JLabel("Ratio Numerator:");
        aspectNumeratorLabel.setDisplayedMnemonic('n');

        ratioNumeratorTextField = new JTextField(3);
        ratioNumeratorTextField.setFocusAccelerator('n');

        aspectDenominatorLabel = new JLabel("Denominator:");
        aspectDenominatorLabel.setDisplayedMnemonic('d');

        ratioDenominatorTextField = new JTextField(3);
        ratioDenominatorTextField.setFocusAccelerator('d');

        setRatioButton = new JButton("Set Ratio");
        setRatioButton.setMnemonic('a');

        xLabel = new JLabel("Window X");
        xLabel.setDisplayedMnemonic('x');

        xTextField = new JTextField(4);
        xTextField.setFocusAccelerator('x');

        yLabel = new JLabel("Y");
        yLabel.setDisplayedMnemonic('y');

        yTextField = new JTextField(4);
        yTextField.setFocusAccelerator('y');

        wLabel = new JLabel("W");
        wLabel.setDisplayedMnemonic('w');

        wTextField = new JTextField(4);
        wTextField.setFocusAccelerator('w');

        hLabel = new JLabel("H");
        hLabel.setDisplayedMnemonic('h');

        hTextField = new JTextField(4);
        hTextField.setFocusAccelerator('h');

        setWindowButton = new JButton("Set Window");
        setWindowButton.setMnemonic('w');

        lLabel = new JLabel("Border L");
        lLabel.setDisplayedMnemonic('l');

        lTextField = new JTextField(4);
        lTextField.setFocusAccelerator('l');

        rLabel = new JLabel("R");
        rLabel.setDisplayedMnemonic('r');

        rTextField = new JTextField(4);
        rTextField.setFocusAccelerator('r');

        tLabel = new JLabel("T");
        tLabel.setDisplayedMnemonic('t');

        tTextField = new JTextField(4);
        tTextField.setFocusAccelerator('t');

        bLabel = new JLabel("B");
        bLabel.setDisplayedMnemonic('b');

        bTextField = new JTextField(4);
        bTextField.setFocusAccelerator('b');

        setBorderButton = new JButton("Set Border");
        setBorderButton.setMnemonic('o');

        pauseButton = new JButton("Pause");
        pauseButton.setMnemonic('p');

        controlsPane1 = new JPanel();
        controlsPane1.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsPane1.add(aspectNumeratorLabel);
        controlsPane1.add(Box.createHorizontalStrut(4));
        controlsPane1.add(ratioNumeratorTextField);
        controlsPane1.add(Box.createHorizontalStrut(12));
        controlsPane1.add(aspectDenominatorLabel);
        controlsPane1.add(Box.createHorizontalStrut(4));
        controlsPane1.add(ratioDenominatorTextField);
        controlsPane1.add(setRatioButton);
        controlsPane1.add(pauseButton);

        controlsPane2 = new JPanel();
        controlsPane2.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsPane2.add(xLabel);
        controlsPane2.add(Box.createHorizontalStrut(4));
        controlsPane2.add(xTextField);
        controlsPane2.add(Box.createHorizontalStrut(12));
        controlsPane2.add(yLabel);
        controlsPane2.add(Box.createHorizontalStrut(4));
        controlsPane2.add(yTextField);
        controlsPane2.add(Box.createHorizontalStrut(12));
        controlsPane2.add(wLabel);
        controlsPane2.add(Box.createHorizontalStrut(4));
        controlsPane2.add(wTextField);
        controlsPane2.add(Box.createHorizontalStrut(12));
        controlsPane2.add(hLabel);
        controlsPane2.add(Box.createHorizontalStrut(4));
        controlsPane2.add(hTextField);
        controlsPane2.add(setWindowButton);

        controlsPane3 = new JPanel();
        controlsPane3.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlsPane3.add(lLabel);
        controlsPane3.add(Box.createHorizontalStrut(4));
        controlsPane3.add(lTextField);
        controlsPane3.add(Box.createHorizontalStrut(12));
        controlsPane3.add(rLabel);
        controlsPane3.add(Box.createHorizontalStrut(4));
        controlsPane3.add(rTextField);
        controlsPane3.add(Box.createHorizontalStrut(12));
        controlsPane3.add(tLabel);
        controlsPane3.add(Box.createHorizontalStrut(4));
        controlsPane3.add(tTextField);
        controlsPane3.add(Box.createHorizontalStrut(12));
        controlsPane3.add(bLabel);
        controlsPane3.add(Box.createHorizontalStrut(4));
        controlsPane3.add(bTextField);
        controlsPane3.add(setBorderButton);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.Y_AXIS));
        controlsPane.add(controlsPane1);
        controlsPane.add(controlsPane2);
        controlsPane.add(controlsPane3);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setLayout(new BorderLayout(16, 16));
        contentPane.add(videoPane, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        frame = new JFrame("vlcj crop test");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        setRatioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.video().setCropRatio(
                    Integer.parseInt(ratioNumeratorTextField.getText()),
                    Integer.parseInt(ratioDenominatorTextField.getText())
                );
            }
        });

        setWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.video().setCropWindow(
                    Integer.parseInt(xTextField.getText()),
                    Integer.parseInt(yTextField.getText()),
                    Integer.parseInt(wTextField.getText()),
                    Integer.parseInt(hTextField.getText())
                );
            }
        });

        setBorderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.video().setCropBorder(
                    Integer.parseInt(lTextField.getText()),
                    Integer.parseInt(tTextField.getText()),
                    Integer.parseInt(rTextField.getText()),
                    Integer.parseInt(bTextField.getText())
                );
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.controls().pause();
            }
        });
    }

    private void start(String mrl) {
        frame.setVisible(true);
        mediaPlayer.media().play(mrl);
        mediaPlayer.controls().setPosition(0.1f);
    }
}
