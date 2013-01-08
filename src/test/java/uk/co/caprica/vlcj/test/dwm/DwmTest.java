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

package uk.co.caprica.vlcj.test.dwm;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibDwmApi;

import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;

/**
 * Simple test to enable/disable desktop compositing on Windows.
 */
public class DwmTest {

    private final JFrame frame;
    private final JPanel contentPane;
    private final JButton enableButton;
    private final JButton disableButton;

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
                HRESULT hResult;
                hResult = LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_ENABLECOMPOSITION);
                System.out.println("DwmEnableComposition hResult=" + hResult.intValue());
                dumpStatus();
            }
        });

        disableButton = new JButton("Disable");
        disableButton.setMnemonic('d');
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HRESULT hResult;
                hResult = LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_DISABLECOMPOSITION);
                System.out.println("DwmEnableComposition hResult=" + hResult.intValue());
                dumpStatus();
            }
        });

        contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(enableButton);
        contentPane.add(disableButton);

        frame = new JFrame("DWM Test");
        frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void dumpStatus() {
        IntByReference pfEnabled = new IntByReference();
        HRESULT hResult = LibDwmApi.INSTANCE.DwmIsCompositionEnabled(pfEnabled);
        System.out.println("DwmIsCompositionEnabled hResult=" + hResult.intValue());
        if(hResult.intValue() == LibDwmApi.S_OK) {
            System.out.println("Desktop composition is " + (pfEnabled.getValue() != 0 ? "enabled" : "disabled"));
        }
    }
}
