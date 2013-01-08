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

package uk.co.caprica.vlcj.test.condition;

import java.io.File;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.condition.Condition;
import uk.co.caprica.vlcj.player.condition.UnexpectedErrorConditionException;
import uk.co.caprica.vlcj.player.condition.UnexpectedFinishedConditionException;
import uk.co.caprica.vlcj.player.condition.conditions.PausedCondition;
import uk.co.caprica.vlcj.player.condition.conditions.PlayingCondition;
import uk.co.caprica.vlcj.player.condition.conditions.SnapshotTakenCondition;
import uk.co.caprica.vlcj.player.condition.conditions.TimeReachedCondition;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * Demonstration of the synchronous approach to media player programming when
 * using media player condition objects.
 * <p>
 * This example generates a series of snapshots for a video file.
 * <p>
 * The snapshots will be saved in the current directory.
 * <p>
 * Specify two options on the command-line: first the MRL to play, second the
 * period at which to take snapshots (e.g. "20" for every 20 seconds).
 */
public class ConditionTest extends VlcjTest {

    // Some standard options for headless operation
    private static final String[] VLC_ARGS = {
        "--intf", "dummy",          /* no interface */
        "--vout", "dummy",          /* we don't want video (output) */
        "--no-audio",               /* we don't want audio (decoding) */
        "--no-video-title-show",    /* nor the filename displayed */
        "--no-stats",               /* no stats */
        "--no-sub-autodetect-file", /* we don't want subtitles */
        "--no-inhibit",             /* we don't want interfaces */
        "--no-disable-screensaver", /* we don't want interfaces */
        "--no-snapshot-preview",    /* no blending in dummy vout */
    };

    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            System.err.println("Usage: <mrl> <seconds>");
            System.exit(1);
        }

        final String mrl = args[0];
        final int period = Integer.parseInt(args[1]) * 1000;

        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        mediaPlayer.setSnapshotDirectory(new File(".").getAbsolutePath());

        // The sequence for creating the snapshots is...
        //
        // Start the media
        // Wait until playing
        // Loop...
        //  Set the target time
        //  Wait until the target time is reached
        //  Pause the media player
        //  Wait until paused
        //  Save the snapshot
        //  Wait until snapshot taken
        //  Play the media player
        //
        // The media player must be playing or else the required time changed events
        // will not be fired.

        try {
            Condition<?> playingCondition = new PlayingCondition(mediaPlayer) {
                @Override
                protected void onBefore() {
                    // You do not have to use onBefore(), but sometimes it is very convenient, and guarantees
                    // that the required media player event listener is added before your condition is tested
                    mediaPlayer.startMedia(mrl);
                }
            };
            playingCondition.await();

            long time = period;

            for(int i = 0; ; i++) {

                // Some special cases here...
                //
                // 1. The duration may not be available yet, even if the media player is playing
                // 2. For some media types it is not possible to set the position past the end - this
                //    means that you would have to wait for playback to reach the end normally
                long duration = mediaPlayer.getLength();
                if(duration > 0 && time >= duration) {
                    break;
                }

                System.out.println("Snapshot " + i);

                Condition<?> timeReachedCondition = new TimeReachedCondition(mediaPlayer, time) {
                    @Override
                    protected void onBefore() {
                        mediaPlayer.setTime(targetTime);
                    }
                };
                timeReachedCondition.await();

                Condition<?> pausedCondition = new PausedCondition(mediaPlayer) {
                    @Override
                    protected void onBefore() {
                        mediaPlayer.pause();
                    }
                };
                pausedCondition.await();

                Condition<?> snapshotTakenCondition = new SnapshotTakenCondition(mediaPlayer) {
                    @Override
                    protected void onBefore() {
                        mediaPlayer.saveSnapshot();
                    }
                };
                snapshotTakenCondition.await();

                playingCondition = new PlayingCondition(mediaPlayer) {
                    @Override
                    protected void onBefore() {
                        mediaPlayer.play();
                    }
                };
                playingCondition.await();

                time += period;
            }
        }
        catch(UnexpectedErrorConditionException e) {
            System.out.println("ERROR!");
        }
        catch(UnexpectedFinishedConditionException e) {
            System.out.println("FINISHED!");
        }

        System.out.println("All done");

        mediaPlayer.release();
        factory.release();
    }
}
