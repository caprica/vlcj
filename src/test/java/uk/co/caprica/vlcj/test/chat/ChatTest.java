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

package uk.co.caprica.vlcj.test.chat;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An example showing a possible two-way video chat application.
 * <p>
 * The local media source is expected to be an MRL for a local video capture device. This is
 * displayed locally and streamed via RTP.
 * <p>
 * Examples of video capture device MRLs are:
 * <ul>
 * <li>v4l2:///dev/video0</li>
 * <li>dshow://</li>
 * </ul>
 * <p>
 * The remote media source is expected to be an incoming RTP stream.
 * <p>
 * A remote client would use the inverse MRLs.
 * <p>
 * Any MRL will do for local media, i.e. you do not have to use a video capture device, so you can
 * test with regular media files if you do not have a video capture device.
 * <p>
 * You are not required to use RTP in your own application of course, you can stream however you
 * like.
 * <p>
 * You should also be able to adapt this to use IPv6 addresses.
 * <p>
 * If you want to capture audio, you should a media option similar to the following (on Linux at
 * least):
 *
 * <pre>
 *   :input-slave=alsa://hw:0,0
 * </pre>
 */
public class ChatTest extends VlcjTest {

    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer localMediaPlayer;
    private final EmbeddedMediaPlayer remoteMediaPlayer;

    private final JFrame frame;
    private final JPanel contentPane;
    private final JPanel sourceControls;
    private final JPanel videoPanel;
    private final JPanel localPanel;
    private final JPanel remotePanel;
    private final Canvas localCanvas;
    private final Canvas remoteCanvas;
    private final JPanel localStreamControls;
    private final JPanel remoteStreamControls;

    private final JLabel mediaLabel;
    private final JTextField mediaTextField;

    private final JLabel streamToLabel;
    private final JTextField streamToTextField;
    private final JButton sendButton;
    private final JButton sendSnapshotButton;

    private final JLabel streamFromLabel;
    private final JTextField streamFromTextField;
    private final JButton receiveButton;
    private final JButton receiveSnapshotButton;

    private final CanvasVideoSurface localVideoSurface;
    private final CanvasVideoSurface remoteVideoSurface;

    public static void main(String[] args) throws Exception {
        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatTest().start();
            }
        });
    }

    public ChatTest() {
        mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show");
        localMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        remoteMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setLayout(new BorderLayout(16, 16));

        sourceControls = new JPanel();
        sourceControls.setBorder(new TitledBorder("Source"));
        sourceControls.setLayout(new BoxLayout(sourceControls, BoxLayout.X_AXIS));

        mediaLabel = new JLabel("Media:");
        mediaLabel.setDisplayedMnemonic('m');
        sourceControls.add(mediaLabel);

        sourceControls.add(Box.createHorizontalStrut(4));

        mediaTextField = new JTextField();
        mediaTextField.setFocusAccelerator('m');
        sourceControls.add(mediaTextField);

        contentPane.add(sourceControls, BorderLayout.NORTH);

        videoPanel = new JPanel();
        videoPanel.setLayout(new GridLayout(1, 2, 16, 0));

        localCanvas = new Canvas();
        localCanvas.setBackground(Color.black);
        localCanvas.setSize(320, 180);

        localStreamControls = new JPanel();
        localStreamControls.setLayout(new BoxLayout(localStreamControls, BoxLayout.X_AXIS));

        localStreamControls.add(Box.createHorizontalStrut(4));

        streamToLabel = new JLabel("Stream To:");
        streamToLabel.setDisplayedMnemonicIndex(7);
        localStreamControls.add(streamToLabel);

        streamToTextField = new JTextField();
        streamToTextField.setFocusAccelerator('t');
        streamToTextField.setColumns(12);
        localStreamControls.add(streamToTextField);

        sendButton = new JButton("Send");
        sendButton.setMnemonic('s');
        localStreamControls.add(sendButton);

        sendSnapshotButton = new JButton("Snap");
        sendSnapshotButton.setMnemonic('n');
        localStreamControls.add(sendSnapshotButton);

        localVideoSurface = mediaPlayerFactory.newVideoSurface(localCanvas);
        localMediaPlayer.setVideoSurface(localVideoSurface);

        localPanel = new JPanel();
        localPanel.setBorder(new TitledBorder("Local"));
        localPanel.setLayout(new BorderLayout(0, 8));
        localPanel.add(localCanvas, BorderLayout.CENTER);
        localPanel.add(localStreamControls, BorderLayout.SOUTH);

        remoteCanvas = new Canvas();
        remoteCanvas.setBackground(Color.black);
        remoteCanvas.setSize(320, 180);

        remoteStreamControls = new JPanel();
        remoteStreamControls.setLayout(new BoxLayout(remoteStreamControls, BoxLayout.X_AXIS));

        streamFromLabel = new JLabel("Stream From:");
        streamFromLabel.setDisplayedMnemonicIndex(7);
        remoteStreamControls.add(streamFromLabel);

        remoteStreamControls.add(Box.createHorizontalStrut(4));

        streamFromTextField = new JTextField();
        streamFromTextField.setFocusAccelerator('f');
        streamFromTextField.setColumns(12);
        remoteStreamControls.add(streamFromTextField);

        receiveButton = new JButton("Receive");
        receiveButton.setMnemonic('r');
        remoteStreamControls.add(receiveButton);

        receiveSnapshotButton = new JButton("Snap");
        receiveSnapshotButton.setMnemonic('a');
        remoteStreamControls.add(receiveSnapshotButton);

        remoteVideoSurface = mediaPlayerFactory.newVideoSurface(remoteCanvas);
        remoteMediaPlayer.setVideoSurface(remoteVideoSurface);

        remotePanel = new JPanel();
        remotePanel.setBorder(new TitledBorder("Remote"));
        remotePanel.setLayout(new BorderLayout(0, 8));
        remotePanel.add(remoteCanvas, BorderLayout.CENTER);
        remotePanel.add(remoteStreamControls, BorderLayout.SOUTH);

        videoPanel.add(localPanel);
        videoPanel.add(remotePanel);

        contentPane.add(videoPanel, BorderLayout.CENTER);

        frame = new JFrame("vlcj video chat");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });

        sendSnapshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localMediaPlayer.saveSnapshot();
            }
        });

        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receive();
            }
        });

        receiveSnapshotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remoteMediaPlayer.saveSnapshot();
            }
        });
    }

    private void start() {
        mediaTextField.setText(!RuntimeUtil.isWindows() ? "v4l2:///dev/video0" : "dshow://");

        streamToTextField.setText("230.0.0.1:5555");
        streamFromTextField.setText("230.0.0.1:5555");

        frame.setVisible(true);
    }

    private void send() {
        String mrl = mediaTextField.getText();
        if(mrl.length() > 0) {
            String streamTo = streamToTextField.getText();

            String[] parts = streamTo.split(":");
            if(parts.length == 2) {
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);

                String[] localOptions = {formatRtpStream(host, port), ":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep",};

                localMediaPlayer.playMedia(mrl, localOptions);
            }
            else {
                JOptionPane.showMessageDialog(frame, "You must specify host:port to stream to.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "You must specify source media, e.g. v4l2:///dev/video0 on Linux or dshow:// on Windows.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void receive() {
        String mrl = streamFromTextField.getText();
        remoteMediaPlayer.playMedia("rtp://@" + mrl);
    }

    private static String formatRtpStream(String serverAddress, int serverPort) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#transcode{vcodec=mp4v,vb=2048,scale=1,acodec=mpga,ab=128,channels=2,samplerate=44100}:duplicate{dst=display,dst=rtp{dst=");
        sb.append(serverAddress);
        sb.append(",port=");
        sb.append(serverPort);
        sb.append(",mux=ts}}");
        return sb.toString();
    }
}
