package uk.co.caprica.vlcj.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;

public class PlayerVideoAdjustPanel extends JPanel {

  private final MediaPlayer mediaPlayer;

  private JCheckBox enableVideoAdjustCheckBox;
  
  private JLabel contrastLabel;
  private JSlider contrastSlider;
  
  private JLabel brightnessLabel;
  private JSlider brightnessSlider;

  private JLabel hueLabel;
  private JSlider hueSlider;
  
  private JLabel saturationLabel;
  private JSlider saturationSlider;
  
  private JLabel gammaLabel;
  private JSlider gammaSlider;
  
  public PlayerVideoAdjustPanel(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    
    createUI();
  }
  
  private void createUI() {
    createControls();
    layoutControls();
    registerListeners();
  }
   
  private void createControls() {
    enableVideoAdjustCheckBox = new JCheckBox("Video Adjust");
    
    contrastLabel = new JLabel("Contrast");
    contrastSlider = new JSlider();
    contrastSlider.setOrientation(JSlider.HORIZONTAL);
    contrastSlider.setMinimum(0);
    contrastSlider.setMaximum(200);
    contrastSlider.setPreferredSize(new Dimension(100, 40));
    contrastSlider.setToolTipText("Change Contrast");
    contrastSlider.setEnabled(false);
    contrastSlider.setPaintLabels(true);
    contrastSlider.setPaintTicks(true);

    brightnessLabel = new JLabel("Brightness");
    brightnessSlider = new JSlider();
    brightnessSlider.setOrientation(JSlider.HORIZONTAL);
    brightnessSlider.setMinimum(0);
    brightnessSlider.setMaximum(200);
    brightnessSlider.setPreferredSize(new Dimension(100, 40));
    brightnessSlider.setToolTipText("Change Brightness");
    brightnessSlider.setEnabled(false);

    hueLabel = new JLabel("Hue");
    hueSlider = new JSlider();
    hueSlider.setOrientation(JSlider.HORIZONTAL);
    hueSlider.setMinimum(0);
    hueSlider.setMaximum(360);
    hueSlider.setPreferredSize(new Dimension(100, 40));
    hueSlider.setToolTipText("Change ");
    hueSlider.setEnabled(false);

    saturationLabel = new JLabel("Saturation");
    saturationSlider = new JSlider();
    saturationSlider.setOrientation(JSlider.HORIZONTAL);
    saturationSlider.setMinimum(0);
    saturationSlider.setMaximum(300);
    saturationSlider.setPreferredSize(new Dimension(100, 40));
    saturationSlider.setToolTipText("Change ");
    saturationSlider.setEnabled(false);

    gammaLabel = new JLabel("Gamma");
    gammaSlider = new JSlider();
    gammaSlider.setOrientation(JSlider.HORIZONTAL);
    gammaSlider.setMinimum(1);    //  0.01
    gammaSlider.setMaximum(1000); // 10.00
    gammaSlider.setPreferredSize(new Dimension(100, 40));
    gammaSlider.setToolTipText("Change ");
    gammaSlider.setEnabled(false);
    
    contrastSlider.setValue(Math.round(mediaPlayer.getBrightness() * 100));
    brightnessSlider.setValue(Math.round(mediaPlayer.getContrast() * 100));
    hueSlider.setValue(mediaPlayer.getHue());
    saturationSlider.setValue(Math.round(mediaPlayer.getSaturation() * 100));
    gammaSlider.setValue(Math.round(mediaPlayer.getGamma() * 100));
  }
  
  private void layoutControls() {
    setBorder(new EmptyBorder(4, 4, 4, 4));
    
    setLayout(new BorderLayout());

    JPanel slidersPanel = new JPanel();
    slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));
    slidersPanel.add(enableVideoAdjustCheckBox);
    slidersPanel.add(contrastLabel);
    slidersPanel.add(contrastSlider);
    slidersPanel.add(brightnessLabel);
    slidersPanel.add(brightnessSlider);
    slidersPanel.add(hueLabel);
    slidersPanel.add(hueSlider);
    slidersPanel.add(saturationLabel);
    slidersPanel.add(saturationSlider);
    slidersPanel.add(gammaLabel);
    slidersPanel.add(gammaSlider);
    
    add(slidersPanel, BorderLayout.CENTER);
  }
  
  private void registerListeners() {
    enableVideoAdjustCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean enabled = enableVideoAdjustCheckBox.isSelected();
        contrastSlider.setEnabled(enabled);
        brightnessSlider.setEnabled(enabled);
        hueSlider.setEnabled(enabled);
        saturationSlider.setEnabled(enabled);
        gammaSlider.setEnabled(enabled);
        mediaPlayer.setAdjustVideo(enabled);
      }
    });
    
    contrastSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
          mediaPlayer.setContrast(source.getValue() / 100.0f);
          System.out.println(mediaPlayer.getContrast());
        }
      }
    });

    brightnessSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
          mediaPlayer.setBrightness(source.getValue() / 100.0f);
          System.out.println(mediaPlayer.getBrightness());
        }
      }
    });

    hueSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
          mediaPlayer.setHue(source.getValue());
          System.out.println(mediaPlayer.getHue());
        }
      }
    });
    
    saturationSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
          mediaPlayer.setSaturation(source.getValue() / 100.0f);
          System.out.println(mediaPlayer.getSaturation());
        }
      }
    });
    
    gammaSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if(!source.getValueIsAdjusting()) {
          mediaPlayer.setGamma(source.getValue() / 100.0f);
          System.out.println(mediaPlayer.getGamma());
        }
      }
    });
  }
}
