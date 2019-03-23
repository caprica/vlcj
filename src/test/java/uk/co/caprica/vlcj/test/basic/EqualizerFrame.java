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
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.basic;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.Equalizer;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Separate frame to hold audio equalizer controls.
 * <p>
 * This is just an example, some shortcuts with regards to the slider controls have been taken.
 */
@SuppressWarnings("serial")
public class EqualizerFrame extends JFrame implements ChangeListener, ActionListener, ItemListener {

    private static final String BAND_INDEX_PROPERTY = "equalizerBandIndex";

    private final String dbFormat = "%.2fdB";

    private final MediaPlayerFactory mediaPlayerFactory;
    private final MediaPlayer mediaPlayer;
    private final Equalizer equalizer;

    private final SliderControl preampControl;
    private final SliderControl[] bandControls;

    private final JToggleButton enableButton;
    private final JComboBox presetComboBox;


    public EqualizerFrame(List<Float> list, List<String> presets, MediaPlayerFactory mediaPlayerFactory, MediaPlayer mediaPlayer, Equalizer equalizer) {
        super("Equalizer");

        this.mediaPlayerFactory = mediaPlayerFactory;
        this.mediaPlayer = mediaPlayer;
        this.equalizer = equalizer;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(4,  4,  4,  4));
        contentPane.setLayout(new BorderLayout(0, 4));

        JPanel bandsPane = new JPanel();
        bandsPane.setLayout(new GridLayout(1, 1 + list.size(), 2, 0));

        preampControl = new SliderControl("Preamp", (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
        preampControl.getSlider().addChangeListener(this);
        bandsPane.add(preampControl);

        bandControls = new SliderControl[list.size()];
        for(int i = 0; i < list.size(); i++) {
            bandControls[i] = new SliderControl(formatFrequency(list.get(i)), (int)LibVlcConst.MIN_GAIN, (int)LibVlcConst.MAX_GAIN, 0, dbFormat);
            bandControls[i].getSlider().putClientProperty(BAND_INDEX_PROPERTY, i);
            bandControls[i].getSlider().addChangeListener(this);
            bandsPane.add(bandControls[i]);
        }

        contentPane.add(bandsPane, BorderLayout.CENTER);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));

        enableButton = new JToggleButton("Enable");
        enableButton.setMnemonic('e');
        controlsPane.add(enableButton);

        controlsPane.add(Box.createHorizontalGlue());

        JLabel presetLabel = new JLabel("Preset:");
        presetLabel.setDisplayedMnemonic('p');
        controlsPane.add(presetLabel);

        presetComboBox = new JComboBox();
        presetLabel.setLabelFor(presetComboBox);
        DefaultComboBoxModel presetModel = (DefaultComboBoxModel)presetComboBox.getModel();
        presetModel.addElement(null);
        for(String presetName : presets) {
            presetModel.addElement(presetName);
        }
        presetComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value != null) {
                    label.setText(String.valueOf(value));
                }
                else {
                    label.setText("--Select--");
                }
                return label;
            }
        });
        controlsPane.add(presetComboBox);

        contentPane.add(controlsPane, BorderLayout.SOUTH);

        setContentPane(contentPane);

        enableButton.addActionListener(this);
        presetComboBox.addItemListener(this);
    }

    private String formatFrequency(float hz) {
        if(hz < 1000.0f) {
            return String.format("%.0f Hz", hz);
        }
        else {
            return String.format("%.0f kHz", hz / 1000f);
        }
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        boolean enable = enableButton.isSelected();
        if(!enable) {
            presetComboBox.setSelectedItem(null);
        }
        mediaPlayer.audio().setEqualizer(enable ? equalizer : null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() instanceof JSlider) {
            JSlider slider = (JSlider)e.getSource();

            Integer index = (Integer)slider.getClientProperty(BAND_INDEX_PROPERTY);
            int value = slider.getValue();
            // Band...
            if(index != null) {
                System.out.println(value);
                equalizer.setAmp(index, value / 100f);
            }
            // Preamp...
            else {
                equalizer.setPreamp(value / 100f);
            }

            if(!applyingPreset) {
                presetComboBox.setSelectedItem(null);
            }
        }
    }

    boolean applyingPreset;

    @Override
    public final void itemStateChanged(ItemEvent e) {
        String presetName = (String)presetComboBox.getSelectedItem();
        if(e.getStateChange() == ItemEvent.SELECTED) {
            if(presetName != null) {
                Equalizer presetEqualizer = mediaPlayerFactory.equalizer().newEqualizer(presetName);
                if(presetEqualizer != null) {
                    applyingPreset = true;
                    preampControl.getSlider().setValue((int)(presetEqualizer.preamp() * 100f));
                    float[] amps = presetEqualizer.amps();
                    for(int i = 0; i < amps.length; i++) {
                        bandControls[i].getSlider().setValue((int)(amps[i] * 100f));
                    }

                    applyingPreset = false;
                }
            }
        }
    }
}
