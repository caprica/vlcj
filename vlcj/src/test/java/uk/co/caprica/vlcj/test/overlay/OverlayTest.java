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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.overlay;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibX11;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

/**
 * An absolute minimum test player demonstrating how to achieve a transparent
 * overlay and translucent painting.
 * <p>
 * Press SPACE to pause the video play-back.
 * <p>
 * Press F11 to toggle the overlay.
 * <p>
 * If the video looks darker with the overlay enabled, then most likely you are
 * using a compositing window manager that is doing some fancy blending of the
 * overlay window and the main application window. You have to turn off those
 * window effects.
 */
public class OverlayTest {
  
  public static void main(String[] args) throws Exception {
    LibX11.INSTANCE.XInitThreads();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new OverlayTest();
      }
    });
  }
  
  public OverlayTest() {
    Frame f = new Frame("Test Player");
    f.setSize(800, 600);
    f.setBackground(Color.black);
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    f.setLayout(new BorderLayout());
    Canvas vs = new Canvas();
    f.add(vs, BorderLayout.CENTER);
    f.setVisible(true);
    
    MediaPlayerFactory factory = new MediaPlayerFactory(new String[] {});
    
    final EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
    mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

    f.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
          case KeyEvent.VK_F11:
            mediaPlayer.enableOverlay(!mediaPlayer.overlayEnabled());
            break;
            
          case KeyEvent.VK_SPACE:
            mediaPlayer.pause();
            break;
        }
      }
    });
    
    mediaPlayer.setOverlay(new Overlay(f));
    mediaPlayer.enableOverlay(true);
    
    mediaPlayer.playMedia("whatever.mp4");
  }
  
  private class Overlay extends Window {
    public Overlay(Window owner) {
      super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
      
      AWTUtilities.setWindowOpaque(this, false);
      
      setLayout(null);
      
      JButton b = new JButton("JButton");
      b.setBounds(150, 150, 100, 24);
      add(b);
      
      TranslucentComponent c = new TranslucentComponent();
      c.setBounds(150, 200, 300, 40);
      add(c);
    }
    
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      GradientPaint gp = new GradientPaint(180.0f, 280.0f, new Color(255, 255, 255, 255), 250.0f, 380.0f, new Color(255, 255, 0, 0));
      g2.setPaint(gp);
      for(int i = 0; i < 3; i++) {
        g2.drawOval(150, 280, 100, 100);
        g2.fillOval(150, 280, 100, 100);
        g2.translate(120, 20);
      }
    }
  }
  
  private class TranslucentComponent extends JComponent {
    public TranslucentComponent() {
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
      
      g2.setPaint(new Color(255, 128, 128, 64));
      
      g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
      g2.fillRect(0, 0, getWidth(), getHeight());
      
      g2.setPaint(new Color(0, 0, 0, 128));
      g2.setFont(new Font("Sansserif", Font.BOLD, 18));
      g2.drawString("Translucent", 16, 26);
    }
  }
}
