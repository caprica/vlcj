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
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.dwm;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibDwmApi;

/**
 * Simple test to enable/disable desktop compositing on Windows.
 */
public class DwmTest {

  private JFrame frame;
  private JPanel contentPane;
  private JButton enableButton;
  private JButton disableButton;
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new DwmTest();
      }
    });
  }
  
  public DwmTest() {
    enableButton = new JButton("Enable");
    enableButton.setMnemonic('e');
    enableButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_ENABLECOMPOSITION);
      }
    });
    
    disableButton = new JButton("Disable");
    disableButton.setMnemonic('d');
    disableButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_DISABLECOMPOSITION);
      }
    });
    
    contentPane = new JPanel();
    contentPane.setLayout(new FlowLayout());
    contentPane.add(enableButton);
    contentPane.add(disableButton);
    
    frame = new JFrame("DWM Test");
    frame.setContentPane(contentPane);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
