package uk.co.caprica.vlcj.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.MediaPlayer;

public class PlayerControlsPanel extends JPanel {

  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

  private final MediaPlayer mediaPlayer;
  
  private JLabel timeLabel;
  private JProgressBar positionProgressBar;
  private JLabel chapterLabel;
  
  private JButton previousChapterButton;
  private JButton rewindButton;
  private JButton stopButton;
  private JButton pauseButton;
  private JButton playButton;
  private JButton fastForwardButton;
  private JButton nextChapterButton;
  
  private JButton toggleMuteButton;
  
  public PlayerControlsPanel(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    
    createUI();
    
    executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayer), 0L, 1L, TimeUnit.SECONDS);
  }
  
  private void createUI() {
    createControls();
    layoutControls();
    registerListeners();
  }
   
  private void createControls() {
    timeLabel = new JLabel("hh:mm:ss");
    
    positionProgressBar = new JProgressBar();
    positionProgressBar.setMinimum(0);
    positionProgressBar.setMaximum(100);
    
    chapterLabel = new JLabel("00/00");
    
    previousChapterButton = new JButton();
    previousChapterButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_start_blue.png")));

    rewindButton = new JButton();
    rewindButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_rewind_blue.png")));
    
    stopButton = new JButton();
    stopButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_stop_blue.png")));
    
    pauseButton = new JButton();
    pauseButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_pause_blue.png")));
    
    playButton = new JButton();
    playButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_play_blue.png")));
    
    fastForwardButton = new JButton();
    fastForwardButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_fastforward_blue.png")));

    nextChapterButton = new JButton();
    nextChapterButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_end_blue.png")));
    
    toggleMuteButton = new JButton();
    toggleMuteButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/sound_mute.png")));
  }
  
  private void layoutControls() {
    setBorder(new EmptyBorder(4, 4, 4, 4));
    
    setLayout(new BorderLayout());
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout(8, 0));
    topPanel.add(timeLabel, BorderLayout.WEST);
    topPanel.add(positionProgressBar, BorderLayout.CENTER);
    topPanel.add(chapterLabel, BorderLayout.EAST);
    add(topPanel, BorderLayout.NORTH);
    
    JPanel bottomPanel = new JPanel();
    
    bottomPanel.add(previousChapterButton);
    bottomPanel.add(rewindButton);
    bottomPanel.add(stopButton);
    bottomPanel.add(pauseButton);
    bottomPanel.add(playButton);
    bottomPanel.add(fastForwardButton);
    bottomPanel.add(nextChapterButton);
    
    bottomPanel.add(toggleMuteButton);
    
    add(bottomPanel, BorderLayout.SOUTH);
  }
  
  private void registerListeners() {
    previousChapterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.previousChapter();
      }
    });

    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.stop();
      }
    });
    
    pauseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.pause();
      }
    });
    
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.play();
      }
    });
    
    nextChapterButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.nextChapter();
      }
    });
    
    toggleMuteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.mute();
      }
    });
  }
  
  private final class UpdateRunnable implements Runnable {

    private final MediaPlayer mediaPlayer;
    
    private UpdateRunnable(MediaPlayer mediaPlayer) {
      this.mediaPlayer = mediaPlayer;
    }
    
    @Override
    public void run() {
      long time = mediaPlayer.getTime();
      
      long duration = mediaPlayer.getLength();
      int position = (int)Math.round(100.0 * (double)time / (double)duration);

      int chapter = mediaPlayer.getChapter();
      int chapterCount = mediaPlayer.getChapterCount();
      
      updateTime(time);
      updatePosition(position);
      updateChapter(chapter, chapterCount);
    }
  }
  
  private void updateTime(long millis) {
    String s = String.format("%02d:%02d:%02d",
      TimeUnit.MILLISECONDS.toHours(millis),
      TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
      TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
    );
    timeLabel.setText(s);
  }

  private void updatePosition(int value) {
    positionProgressBar.setValue(value);
  }
  
  public void updateChapter(int chapter, int chapterCount) {
    String s = chapter + "/" + chapterCount;
    chapterLabel.setText(s);
  }
}
