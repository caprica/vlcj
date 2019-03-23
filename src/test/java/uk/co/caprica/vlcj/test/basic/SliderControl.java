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

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Composite slider control, used only for this test example.
 */
@SuppressWarnings("serial")
public class SliderControl extends JPanel implements ChangeListener {

    private final float factor = 100f;
    private final String valueFormat;

    private final JLabel label;
    private final JSlider slider;
    private final JLabel valueLabel;

    public SliderControl(String text, float min, float max, float value, String valueFormat) {
        this.valueFormat = valueFormat;

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(4,  4,  4,  4)));

        label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);

        int modelMin = (int)(min * factor);
        int modelMax = (int)(max * factor);
        int modelValue = (int)(value * factor);

        modelValue = Math.min(modelValue, modelMax);
        modelValue = Math.max(modelValue, modelMin);

//        System.out.printf("min max value -> %d %d %d%n", modelMin, modelMax, modelValue);

        slider = new JSlider(JSlider.VERTICAL, modelMin, modelMax, modelValue);
        valueLabel = new JLabel() {
            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = getFontMetrics(getFont());
                return new Dimension(fm.stringWidth("-20.00dB"), fm.getHeight());
            }
        };

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        slider.setAlignmentX(JSlider.CENTER_ALIGNMENT);
        valueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        add(label);
        add(slider);
        add(valueLabel);

        slider.getModel().addChangeListener(this);

        updateValue();
    }

    public final JSlider getSlider() {
        return slider;
    }

    @Override
    public final void stateChanged(ChangeEvent e) {
        updateValue();
    }

    private void updateValue() {
        int value = slider.getValue();
        valueLabel.setText(String.format(valueFormat, value / factor));
    }
}
