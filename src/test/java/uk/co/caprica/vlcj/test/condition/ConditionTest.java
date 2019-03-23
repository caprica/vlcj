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

package uk.co.caprica.vlcj.test.condition;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;
import uk.co.caprica.vlcj.waiter.UnexpectedWaiterErrorException;
import uk.co.caprica.vlcj.waiter.UnexpectedWaiterFinishedException;
import uk.co.caprica.vlcj.waiter.mediaplayer.ReadyWaiter;
import uk.co.caprica.vlcj.waiter.mediaplayer.SnapshotTakenWaiter;
import uk.co.caprica.vlcj.waiter.mediaplayer.TimeReachedWaiter;

import java.io.File;

/**
 * Demonstration of the synchronous approach to media player programming when using media player conditional waiter
 * objects.
 * <p>
 * This example generates a series of snapshots for a video file.
 * <p>
 * The snapshots will be saved in the current directory.
 * <p>
 * Specify two options on the command-line: first the MRL to play, second the period at which to take snapshots (e.g.
 * "20" for every 20 seconds).
 * <p>
 * Note that with contemporary versions of VLC grabbing a snapshot while paused is not going to work, at least not
 * reliably.
 */
public class ConditionTest extends VlcjTest {

    // Some standard options for headless operation
    private static final String[] VLC_ARGS = {
        "--intf", "dummy",          /* no interface */
        "--vout", "dummy",          /* we don't want video (output) */
        "--no-audio",               /* we don't want audio (decoding) */
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
        MediaPlayer mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();

        mediaPlayer.snapshots().setSnapshotDirectory(new File(".").getAbsolutePath());

        // The sequence for creating the snapshots is...
        //
        // Start the media
        // Wait until ready (it must be "ready", not "playing")
        // Loop...
        //  Set the target time
        //  Wait until the target time is reached
        //  Save the snapshot
        //  Wait until snapshot taken
        //
        // The media player must be playing or else the required time changed events will not be fired.

        try {
            ReadyWaiter readyWaiter = new ReadyWaiter(mediaPlayer) {
                @Override
                protected boolean onBefore(MediaPlayer mediaPlayer) {
                    // You do not have to use onBefore(), but sometimes it is very convenient, and guarantees
                    // that the required media player event listener is added before your condition is tested
                    mediaPlayer.media().play(mrl);
                    return true;
                }
            };
            readyWaiter.await();

            long time = period;

            for (int i = 0; ; i++) {
                // Some special cases here...
                //
                // 1. The duration may not be available yet, even if the media player is playing
                // 2. For some media types it is not possible to set the position past the end - this means that you
                //    would have to wait for playback to reach the end normally
                long duration = mediaPlayer.status().length();
                if (duration > 0 && time >= duration) {
                    break;
                }

                System.out.println("Snapshot " + i);

                TimeReachedWaiter timeReachedWaiter = new TimeReachedWaiter(mediaPlayer, time) {
                    @Override
                    protected boolean onBefore(MediaPlayer mediaPlayer) {
                        mediaPlayer.controls().setTime(targetTime);
                        return true;
                    }
                };
                timeReachedWaiter.await();

                // This step may not be necessary, but the purpose of this test is to demo these conditional waiters
                SnapshotTakenWaiter snapshotTakenWaiter = new SnapshotTakenWaiter(mediaPlayer) {
                    @Override
                    protected boolean onBefore(MediaPlayer mediaPlayer) {
                        return mediaPlayer.snapshots().save(640, 480);
                    }
                };
                snapshotTakenWaiter.await();

                time += period;
            }
        }
        catch(UnexpectedWaiterErrorException e) {
            System.out.println("ERROR!");
        }
        catch(UnexpectedWaiterFinishedException e) {
            System.out.println("FINISHED!");
        }

        System.out.println("All done");

        mediaPlayer.release();
        factory.release();
    }
}
