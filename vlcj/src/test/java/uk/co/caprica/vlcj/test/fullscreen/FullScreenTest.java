package uk.co.caprica.vlcj.test.fullscreen;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibX11;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class FullScreenTest {

  public static void main(final String[] args) {
    if(args.length != 1) {
      System.err.println("Specify a single MRL");
      System.exit(1);
    }
    
    LibX11.INSTANCE.XInitThreads();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new FullScreenTest(args);
      }
    });
  }
  
  public FullScreenTest(String[] args) {
    Canvas c = new Canvas();
    c.setBackground(Color.black);

    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(c, BorderLayout.CENTER);
    
    JFrame f = new JFrame();
    f.setContentPane(p);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800, 600);

    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newMediaPlayer(new DefaultFullScreenStrategy(f));
    mediaPlayer.setVideoSurface(c);

    f.setVisible(true);
    
    mediaPlayer.setFullScreen(true);
    mediaPlayer.playMedia(args[0]);
  }
}
