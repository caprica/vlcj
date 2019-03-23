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

package uk.co.caprica.vlcj.test.stats;

import uk.co.caprica.vlcj.media.MediaStatistics;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.test.VlcjTest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test to demonstrate media statistics.
 */
public class StatsTest extends VlcjTest {

    private final JFrame frame;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JLabel readBytesLabel;
    private final JLabel inputBitrateLabel;
    private final JLabel demuxReadBytesLabel;
    private final JLabel demuxBitrateLabel;
    private final JLabel demuxCorruptedLabel;
    private final JLabel demuxDiscontinuityLabel;
    private final JLabel decodedVideoLabel;
    private final JLabel decodedAudioLabel;
    private final JLabel displayedPicturesLabel;
    private final JLabel lostPicturesLabel;
    private final JLabel playedABuffersLabel;
    private final JLabel lostABuffersLabel;
    private final JLabel sentPacketsLabel;
    private final JLabel sentBytesLabel;
    private final JLabel sendBitRateLabel;

    private final JLabel readBytesValueLabel;
    private final JLabel inputBitrateValueLabel;
    private final JLabel demuxReadBytesValueLabel;
    private final JLabel demuxBitrateValueLabel;
    private final JLabel demuxCorruptedValueLabel;
    private final JLabel demuxDiscontinuityValueLabel;
    private final JLabel decodedVideoValueLabel;
    private final JLabel decodedAudioValueLabel;
    private final JLabel displayedPicturesValueLabel;
    private final JLabel lostPicturesValueLabel;
    private final JLabel playedABuffersValueLabel;
    private final JLabel lostABuffersValueLabel;
    private final JLabel sentPacketsValueLabel;
    private final JLabel sentBytesValueLabel;
    private final JLabel sendBitRateValueLabel;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final MediaStatistics mediaStatistics = new MediaStatistics();

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Specify an mrl");
            System.exit(1);
        }

        setLookAndFeel();

        final String mrl = args[0];

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StatsTest().start(mrl);
            }
        });
    }

    public StatsTest() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        readBytesLabel = new JLabel("Read Bytes:");
        inputBitrateLabel = new JLabel("Input Bitrate:");
        demuxReadBytesLabel = new JLabel("Deumx Read Bytes:");
        demuxBitrateLabel = new JLabel("Demux Bitrate:");
        demuxCorruptedLabel = new JLabel("Demux Corrupted:");
        demuxDiscontinuityLabel = new JLabel("Demux Discontinuity:");
        decodedVideoLabel = new JLabel("Decoded Video:");
        decodedAudioLabel = new JLabel("Decoded Audio:");
        displayedPicturesLabel = new JLabel("Displayed Pictures:");
        lostPicturesLabel = new JLabel("Lost Pictures:");
        playedABuffersLabel = new JLabel("Played ABuffers:");
        lostABuffersLabel = new JLabel("Lost ABuffers");
        sentPacketsLabel = new JLabel("Sent Packets");
        sentBytesLabel = new JLabel("Sent Bytes");
        sendBitRateLabel = new JLabel("Send Bitrate");

        readBytesValueLabel = new JLabel();
        inputBitrateValueLabel = new JLabel();
        demuxReadBytesValueLabel = new JLabel();
        demuxBitrateValueLabel = new JLabel();
        demuxCorruptedValueLabel = new JLabel();
        demuxDiscontinuityValueLabel = new JLabel();
        decodedVideoValueLabel = new JLabel();
        decodedAudioValueLabel = new JLabel();
        displayedPicturesValueLabel = new JLabel();
        lostPicturesValueLabel = new JLabel();
        playedABuffersValueLabel = new JLabel();
        lostABuffersValueLabel = new JLabel();
        sentPacketsValueLabel = new JLabel();
        sentBytesValueLabel = new JLabel();
        sendBitRateValueLabel = new JLabel();

        JPanel statsComponent = new JPanel();
        statsComponent.setLayout(new GridLayout(0, 2, 8, 8));

        statsComponent.add(readBytesLabel);
        statsComponent.add(readBytesValueLabel);
        statsComponent.add(inputBitrateLabel);
        statsComponent.add(inputBitrateValueLabel);
        statsComponent.add(demuxReadBytesLabel);
        statsComponent.add(demuxReadBytesValueLabel);
        statsComponent.add(demuxBitrateLabel);
        statsComponent.add(demuxBitrateValueLabel);
        statsComponent.add(demuxCorruptedLabel);
        statsComponent.add(demuxCorruptedValueLabel);
        statsComponent.add(demuxDiscontinuityLabel);
        statsComponent.add(demuxDiscontinuityValueLabel);
        statsComponent.add(decodedVideoLabel);
        statsComponent.add(decodedVideoValueLabel);
        statsComponent.add(decodedAudioLabel);
        statsComponent.add(decodedAudioValueLabel);
        statsComponent.add(displayedPicturesLabel);
        statsComponent.add(displayedPicturesValueLabel);
        statsComponent.add(lostPicturesLabel);
        statsComponent.add(lostPicturesValueLabel);
        statsComponent.add(playedABuffersLabel);
        statsComponent.add(playedABuffersValueLabel);
        statsComponent.add(lostABuffersLabel);
        statsComponent.add(lostABuffersValueLabel);
        statsComponent.add(sentPacketsLabel);
        statsComponent.add(sentPacketsValueLabel);
        statsComponent.add(sentBytesLabel);
        statsComponent.add(sentBytesValueLabel);
        statsComponent.add(sendBitRateLabel);
        statsComponent.add(sendBitRateValueLabel);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.add(statsComponent, BorderLayout.NORTH);

        statsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.add(mediaPlayerComponent, BorderLayout.CENTER);
        cp.add(statsPanel, BorderLayout.EAST);

        frame = new JFrame("vlcj Media Statistics");
        frame.setLocation(100, 100);
        frame.setSize(1000, 400);
        frame.setContentPane(cp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    if(mediaPlayerComponent.mediaPlayer().status().isPlaying()) {
                        if (mediaPlayerComponent.mediaPlayer().media().info().statistics(mediaStatistics)) {
                            updateStats(mediaStatistics);
                        }
                    }
                    try {
                        // Update statistics every second, choose more/less if you want
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e) {
                    }
                }
            }
        });
    }

    private void start(String mrl) {
        mediaPlayerComponent.mediaPlayer().media().play(mrl);
    }

    private void updateStats(MediaStatistics stats) {
        readBytesValueLabel.setText(String.valueOf(stats.inputBytesRead()));
        inputBitrateValueLabel.setText(String.valueOf(stats.inputBitrate()));
        demuxReadBytesValueLabel.setText(String.valueOf(stats.demuxBytesRead()));
        demuxBitrateValueLabel.setText(String.valueOf(stats.demuxBitrate()));
        demuxCorruptedValueLabel.setText(String.valueOf(stats.demuxCorrupted()));
        demuxDiscontinuityValueLabel.setText(String.valueOf(stats.demuxDiscontinuity()));
        decodedVideoValueLabel.setText(String.valueOf(stats.decodedVideo()));
        decodedAudioValueLabel.setText(String.valueOf(stats.decodedAudio()));
        displayedPicturesValueLabel.setText(String.valueOf(stats.picturesDisplayed()));
        lostPicturesValueLabel.setText(String.valueOf(stats.picturesLost()));
        playedABuffersValueLabel.setText(String.valueOf(stats.audioBuffersPlayed()));
        lostABuffersValueLabel.setText(String.valueOf(stats.audioBuffersLost()));
        sentPacketsValueLabel.setText(String.valueOf(stats.sentPackets()));
        sentBytesValueLabel.setText(String.valueOf(stats.sentBytes()));
        sendBitRateValueLabel.setText(String.valueOf(stats.sendBitrate()));
    }
}
