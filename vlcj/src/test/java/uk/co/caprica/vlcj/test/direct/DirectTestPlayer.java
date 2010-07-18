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

package uk.co.caprica.vlcj.test.direct;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;

import uk.co.caprica.vlcj.experimental.DirectVideo;
import uk.co.caprica.vlcj.experimental.RenderCallback;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

import com.sun.jna.Memory;

/**
 * This simple test player shows how to get direct access to the video frame
 * data.
 * <p>
 * This implementation uses the new (1.1.1) libvlc video call-backs function.
 * <p>
 * Since the video frame data is made available, the Java callback may modify
 * the contents of the frame if required.
 * <p>
 * The frame data may also be rendered components such as an OpenGL texture.
 */
public class DirectTestPlayer {
	
	// The size does NOT need to match the video size - it's the size that the video will be scaled to
	// Matching the native size will be faster of course
  private final int width = 720;
  private final int height = 576;

  /**
   * Buffer to hold the video frame data.
   */
  private final int[] imageBuffer = new int[width * height];

  /**
   * Image to render the video frame data.
   */
	private final BufferedImage image;
	
	private final MediaPlayerFactory factory;
	private final DirectVideo video;
	
	private ImagePane imagePane;
	
	public DirectTestPlayer(String media, String[] args) throws InterruptedException, InvocationTargetException {		
    image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);

	  SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				imagePane = new ImagePane(image);
				imagePane.setSize(width, height);
        imagePane.setMinimumSize(new Dimension(width, height));
        imagePane.setPreferredSize(new Dimension(width, height));
				frame.getContentPane().setLayout(new BorderLayout());
				frame.getContentPane().add(imagePane, BorderLayout.CENTER);
				frame.pack();
				frame.setResizable(false);
				frame.setVisible(true);
				
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent evt) {
						video.release();
						factory.release();
						System.exit(0);
					}
				});
			}
			
		});
		
		factory = new MediaPlayerFactory(args);
		video = factory.newOffscreenVideo(width, height, new TestRenderCallback());
		video.playMedia(media);
	}
	
	public static void main(String[] args) throws InterruptedException, InvocationTargetException {
		BasicConfigurator.configure();
		
		if(args.length < 1) {
			System.out.println("Specify a single media URL");
			System.exit(1);
		}
		
		String[] vlcArgs = (args.length == 1) ? new String[] { } : Arrays.copyOfRange(args, 1, args.length);
		
		new DirectTestPlayer(args[0], vlcArgs);
		
		// Application will not exit since the UI thread is running
	}

  private final class ImagePane extends JPanel {
    private final BufferedImage image;
    
    public ImagePane(BufferedImage image) {
      this.image = image;
    }
    
    @Override
    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      g2.drawImage(image, null, 0, 0);
      // You could draw on top of the image here...
//      g2.setColor(Color.red);
//      g2.fillRect(200, 100, 100, 100);
    }
  }

	private final class TestRenderCallback implements RenderCallback {
	  
	  @Override
	  public void display(Memory buffer) {
	    buffer.read(0, imageBuffer, 0, width * height);
	    image.setRGB(0, 0, width, height, imageBuffer, 0, width);
	    // The image could be manipulated here... 
	    imagePane.repaint();
	  }

	  @Override
	  public void lock() {
	  }

	  @Override
	  public void unlock() {
	  }
	}
}
