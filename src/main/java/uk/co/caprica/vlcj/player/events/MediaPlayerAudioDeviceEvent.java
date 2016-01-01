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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.events;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 *
 */
class MediaPlayerAudioDeviceEvent extends AbstractMediaPlayerEvent {

    /**
     * Device.
     */
    private final String device;

    /**
     * Create a media player event.
     *
     * @param mediaPlayer media player the event relates to
     * @param volume volume
     */
    MediaPlayerAudioDeviceEvent(MediaPlayer mediaPlayer, String device) {
        super(mediaPlayer);
        this.device = device;
    }

    @Override
    public void notify(MediaPlayerEventListener listener) {
        listener.audioDeviceChanged(mediaPlayer, device);
    }
}
