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

package uk.co.caprica.vlcj.test.rip;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * The basics of DVD encoding.
 * <p>
 * This example is very unsophisticated, there is a minimal progress indicator, but it does show
 * working DVD encoding.
 * <p>
 * Note that you must use a DVD that has no encryption, or you must have the appropriate DVD
 * decoding libraries installed.
 * <p>
 * Specify three things on the command-line:
 * <pre>
 *   [dvd-device] [output-file] [preset]
 * </pre>
 * For example:
 * <pre>
 *   simpledvd:///dev/dvd /home/rip.webm WEBM_LOW
 * </pre>
 * It is recommended to try the WEBM_LOW preset first.
 * <p>
 * Encoding a full DVD is going to be slow.
 * <p>
 * In principle, a start-time and end-time could also be set via media options - e.g. to trim
 * out unnecessary intro or trailing credits from the movie.
 */
public class RipDvdTest extends VlcjTest {

    // These are the same transcode options that are contained in the
    // "libvlc_DVD_ripper.c" sample file
    private static final Map<String, String> PRESETS = new HashMap<String, String>();

    static {
        PRESETS.put("MP4_HIGH", "#transcode{vcodec=h264,venc=x264{cfr=16},scale=1,acodec=mp4a,ab=160,channels=2,samplerate=44100}");
        PRESETS.put("MP4_LOW", "#transcode{vcodec=h264,venc=x264{cfr=40},scale=1,acodec=mp4a,ab=96,channels=2,samplerate=44100}");
        PRESETS.put("OGG_HIGH", "#transcode{vcodec=theo,venc=theora{quality=9},scale=1,acodec=vorb,ab=160,channels=2,samplerate=44100}");
        PRESETS.put("OGG_LOW", "#transcode{vcodec=theo,venc=theora{quality=4},scale=1,acodec=vorb,ab=96,channels=2,samplerate=44100}");
        PRESETS.put("WEBM_HIGH", "#transcode{vcodec=VP80,vb=2000,scale=1,acodec=vorb,ab=160,channels=2,samplerate=44100}");
        PRESETS.put("WEBM_LOW", "#transcode{vcodec=VP80,vb=1000,scale=1,acodec=vorb,ab=96,channels=2,samplerate=44100}");
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 3) {
            System.out.println("Usage: <dvd-device> <output-file> <preset>");
            System.out.println();
            System.out.println("For <dvd-device> use something like dvdsimple:///dev/dvd");
            System.out.println();
            System.out.println("For <output-file> use a local file like /home/rip.webm and make sure the file extension matches the preset, e.g. \".mp4\", \".ogg\", or \".webm\"");
            System.out.println();
            System.out.println("For <preset> use one of:");
            System.out.println(" MP4_HIGH");
            System.out.println(" MP4_LOW");
            System.out.println(" OGG_HIGH");
            System.out.println(" OGG_LOW");
            System.out.println(" WEBM_HIGH");
            System.out.println(" WEBM_LOW");
            System.out.println();
            System.out.println("For example:");
            System.out.println("dvdsimple:///dev/dvd /home/rip.webm WEBM_LOW");
            System.exit(1);
        }

        String mrl = args[0];
        String outputTo = args[1];
        String preset = PRESETS.get(args[2]);
        if(preset == null) {
            System.out.println("Unknown preset '" + args[2] + "'");
            System.exit(1);
        }

        StringBuilder sb = new StringBuilder(100);
        sb.append(":sout=");
        sb.append(preset);
        sb.append(":file{dst=");
        sb.append(outputTo);
        sb.append("}");

        String mediaOptions = sb.toString();

        final MediaPlayerFactory factory = new MediaPlayerFactory();
        final HeadlessMediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            DecimalFormat df = new DecimalFormat("0.00");

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                System.out.println("Playing...");
            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                // This escape sequence to reset the terminal window cursor back to
                // column zero will not work in the Eclipse console window and most
                // likely not work on Windows at all
                System.out.print(df.format(newPosition * 100.0f) + "%" + "\u001b[0G");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println();
                System.out.println("Finished.");
                mediaPlayer.release();
                factory.release();
                try {
                    // Probably not required, but just in case there are any pending
                    // native buffers
                    Thread.sleep(1000);
                }
                catch(InterruptedException e) {
                }
                System.exit(0);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println();
                System.out.println("Error.");
                mediaPlayer.release();
                factory.release();
                System.exit(1);
            }
        });

        System.out.println("          MRL: " + mrl);
        System.out.println("Media Options: " + mediaOptions);

        mediaPlayer.prepareMedia(mrl, mediaOptions);
        boolean started = mediaPlayer.start();
        if(!started) {
            System.out.println("Failed to start");
            System.exit(1);
        }

        Thread.currentThread().join();
    }
}
