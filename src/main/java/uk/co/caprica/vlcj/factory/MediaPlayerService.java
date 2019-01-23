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

package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.directaudio.DefaultDirectAudioPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.player.embedded.DefaultEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.headless.DefaultHeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

public final class MediaPlayerService extends BaseService {

    MediaPlayerService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new embedded media player.
     * <p>
     * Full-screen will not be available, to enable full-screen support see
     * {@link #newEmbeddedMediaPlayer(FullScreenStrategy)}, or use an alternate mechanism to
     * manually set full-screen if needed.
     *
     * @return media player instance
     */
    public EmbeddedMediaPlayer newEmbeddedMediaPlayer() {
        return new DefaultEmbeddedMediaPlayer(libvlc, instance);
    }

    /**
     * Create a new direct video rendering media player.
     *
     * @param bufferFormatCallback callback to set the desired buffer format
     * @param renderCallback callback to receive the video frame data
     * @return media player instance
     */
    public DirectMediaPlayer newDirectMediaPlayer(BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback) {
        return new DefaultDirectMediaPlayer(libvlc, instance, bufferFormatCallback, renderCallback);
    }

    /**
     * Create a new direct audio media player.
     *
     * @param format decoded audio format
     * @param rate decoded audio sample rate
     * @param channels decoded audio channels
     * @param audioCallback callback
     * @return media player instance
     */
    public DirectAudioPlayer newDirectAudioPlayer(String format, int rate, int channels, AudioCallback audioCallback) {
        return new DefaultDirectAudioPlayer(libvlc, instance, format, rate, channels, audioCallback);
    }

    /**
     * Create a new headless media player.
     * <p>
     * The head-less player is intended for audio media players or streaming server media players
     * and may spawn a native video player window unless you set proper media options when playing
     * media.
     *
     * @return media player instance
     */
    public HeadlessMediaPlayer newHeadlessMediaPlayer() {
        return new DefaultHeadlessMediaPlayer(libvlc, instance);
    }

    /**
     * Create a new play-list media player.
     *
     * @return media player instance
     */
    public MediaListPlayer newMediaListPlayer() {
        return new DefaultMediaListPlayer(libvlc, instance);
    }

}
