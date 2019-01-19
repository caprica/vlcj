package uk.co.caprica.vlcj.test.window;

import uk.co.caprica.vlcj.enums.MarqueePosition;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This test uses a Window for the video surface.
 * <p>
 * The Window is kept in sync with a parent frame giving the appearance of it being properly embedded.
 * <p>
 * This is an approach that could be used to get the optimal EmbeddedMediaPlayer solution working on OSX - on OSX after
 * JDK 6 there is no heavyweight AWT so a Canvas embedded in a JFrame can not be used, but a Window should still work.
 */
public class WindowSurfaceTest {

    private MediaPlayerFactory factory;

    private EmbeddedMediaPlayer mediaPlayer;

    private Window window;

    private VideoSurface videoSurface;

    private Media media;

    private final Rectangle bounds = new Rectangle();

    private JFrame frame;

    private JPanel referencePanel;

    public static void main(String[] args) throws InterruptedException {
        new WindowSurfaceTest(args);

        Thread.currentThread().join();
    }

    public WindowSurfaceTest(String[] args) {
        frame = new JFrame("Window Video Surface Test");
        frame.setBounds(50, 50, 800, 600);
        frame.setBackground(Color.black);
        frame.getContentPane().setBackground(Color.black);

        referencePanel = new JPanel();
        referencePanel.setBackground(Color.black);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(referencePanel);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        window = new Window(frame); // What if JFrame was owner?
        window.setBackground(Color.black);
        videoSurface = factory.videoSurfaces().newVideoSurface(window);
        mediaPlayer.videoSurface().setVideoSurface(videoSurface);
        window.setBounds(100, 100, 800, 600);
        window.setIgnoreRepaint(true);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(new JButton("Play"));
        buttonsPanel.add(new JButton("Pause"));
        buttonsPanel.add(new JButton("Stop"));

        frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                syncVideoSurface();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                syncVideoSurface();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                release();
                System.exit(0);
            }
        });

        frame.setVisible(true);
        syncVideoSurface();

        window.setVisible(true);

        mediaPlayer.logo().setLogoFile("etc/vlcj-logo.png");
        mediaPlayer.logo().setLogoOpacity(0.3f);
        mediaPlayer.logo().enableLogo(true);

        mediaPlayer.marquee().setMarqueeText("Window video surface");
        mediaPlayer.marquee().setMarqueePosition(MarqueePosition.BOTTOM_RIGHT);
        mediaPlayer.marquee().setMarqueeLocation(10, 10);
        mediaPlayer.marquee().enableMarquee(true);

        media = factory.media().newMedia(args[0]);
        mediaPlayer.media().set(media);
        mediaPlayer.controls().play();
    }

    private void syncVideoSurface() {
        // We re-size and re-position to the reference panel contained within the frame rather than the frame itself,
        // this makes it easy to add other UI elements without worrying about calculating the correct size and position
        // for the video surface ourselves - the referencePanel is in effect a "proxy" for the video surface
        referencePanel.getBounds(bounds);
        bounds.setLocation(referencePanel.getLocationOnScreen());
        window.setBounds(bounds);
    }

    private void release() {
        mediaPlayer.release();
        media.release();
        factory.release();
    }

}
