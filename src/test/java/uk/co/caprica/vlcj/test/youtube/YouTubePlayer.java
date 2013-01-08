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

package uk.co.caprica.vlcj.test.youtube;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A minimal YouTube player.
 * <p>
 * The URL/MRL must be in the following format:
 *
 * <pre>
 *   http://www.youtube.com/watch?v=000000
 * </pre>
 *
 * The only thing that makes this different from a 'regular' media player application is the
 * following piece of code:
 *
 * <pre>
 * mediaPlayer.setPlaySubItems(true); // &lt;--- This is very important for YouTube media
 * </pre>
 *
 * Note that it is also possible to programmatically play the sub-item in response to events - this
 * is slightly more complex but more flexible.
 * <p>
 * The YouTube web page format changes from time to time. This means that the lua scripts that vlc
 * provides to parse the YouTube web pages when looking for the media to stream may not work. If you
 * get errors, especially errors alluding to malformed urls, then you may need to update your vlc
 * version to get newer lua scripts.
 */
public class YouTubePlayer extends VlcjTest {

    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final Frame mainFrame;

    private final JLabel urlLabel;
    private final JTextField urlTextField;
    private final JButton playButton;

    public static void main(String[] args) throws Exception {
        setLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new YouTubePlayer().start();
            }
        });
    }

    public YouTubePlayer() {
        mainFrame = new Frame("vlcj YouTube Test");
        mainFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        mainFrame.setSize(800, 600);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        });
        mainFrame.setLayout(new BorderLayout());

        JPanel cp = new JPanel();
        cp.setBackground(Color.black);
        cp.setLayout(new BorderLayout());

        JPanel ip = new JPanel();
        ip.setBorder(new EmptyBorder(4, 4, 4, 4));
        ip.setLayout(new BoxLayout(ip, BoxLayout.X_AXIS));

        urlLabel = new JLabel("URL:");
        urlLabel.setDisplayedMnemonic('u');
        urlLabel.setToolTipText("Enter a URL in the format http://www.youtube.com/watch?v=000000");
        urlTextField = new JTextField(40);
        urlTextField.setFocusAccelerator('u');
        urlTextField.setToolTipText("Enter a URL in the format http://www.youtube.com/watch?v=000000");
        playButton = new JButton("Play");
        playButton.setMnemonic('p');

        ip.add(urlLabel);
        ip.add(urlTextField);
        ip.add(playButton);

        cp.add(ip, BorderLayout.NORTH);

        Canvas vs = new Canvas();
        vs.setBackground(Color.black);
        cp.add(vs, BorderLayout.CENTER);

        mainFrame.add(cp, BorderLayout.CENTER);

        urlTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                play();
            }
        });

        factory = new MediaPlayerFactory();

        mediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

        mediaPlayer.setPlaySubItems(true); // <--- This is very important for YouTube media

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                System.out.println("Buffering " + newCache);
            }

            @Override
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                List<String> items = mediaPlayer.subItems();
                System.out.println(items);
            }
        });
    }

    private void start() {
        mainFrame.setVisible(true);
    }

    private void play() {
        String mrl = urlTextField.getText();
        mediaPlayer.playMedia(mrl);
    }

    private void exit(int value) {
        mediaPlayer.stop();
        mediaPlayer.release();
        factory.release();
        System.exit(value);
    }
}
