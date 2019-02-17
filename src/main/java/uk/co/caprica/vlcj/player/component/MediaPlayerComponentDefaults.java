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

package uk.co.caprica.vlcj.player.component;

/**
 * Consolidated VLC command-line options/switches/arguments used by default in the media player components when creating
 * a {@link uk.co.caprica.vlcj.factory.MediaPlayerFactory}.
 * <pre>
 * --intf=dummy
 *     This is the main interface used by VLC.
 *
 * --no-snapshot-preview
 *     Display the snapshot preview in the screen’s top-left corner.
 *
 * --quiet
 *     Turn off all messages on the console.
 *
 * --quiet-synchro
 *     This avoids flooding the message log with debug output from the video output synchronization mechanism.
 *
 * --video-title
 *     Custom title for the video window
 * </pre>
 */
final class MediaPlayerComponentDefaults {

    static String[] EMBEDDED_MEDIA_PLAYER_ARGS = {
        "--video-title=vlcj video output",
        "--no-snapshot-preview",
        "--quiet",
        "--intf=dummy"
    };

    static String[] AUDIO_MEDIA_PLAYER_ARGS = {
        "--quiet",
        "--intf=dummy"
    };

    private MediaPlayerComponentDefaults() {
    }

}
