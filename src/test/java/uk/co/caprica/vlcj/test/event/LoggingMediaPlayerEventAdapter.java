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

package uk.co.caprica.vlcj.test.event;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * Implementation of a {@link MediaPlayerEventListener} that logs all invocations.
 * <p>
 * Useful only for testing.
 */
public class LoggingMediaPlayerEventAdapter implements MediaPlayerEventListener {

    // === Events relating to the media player ==================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
        Logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})", mediaPlayer, media, mrl);
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
        Logger.debug("opening(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
        Logger.debug("buffering(mediaPlayer={},newCache={})", mediaPlayer);
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        Logger.debug("playing(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        Logger.debug("paused(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        Logger.debug("stopped(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
        Logger.debug("forward(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
        Logger.debug("backward(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        Logger.debug("finished(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        Logger.debug("timeChanged(mediaPlayer={},newTime={})", mediaPlayer, newTime);
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
        Logger.debug("positionChanged(mediaPlayer={},newPosition={})", mediaPlayer, newPosition);
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
        Logger.debug("seekableChanged(mediaPlayer={},newSeekable={})", mediaPlayer, newSeekable);
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
        Logger.debug("pausableChanged(mediaPlayer={},newPausable={})", mediaPlayer, newPausable);
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
        Logger.debug("titleChanged(mediaPlayer={},newTitle={})", mediaPlayer, newTitle);
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
        Logger.debug("snapshotTaken(mediaPlayer={},filename={})", mediaPlayer, filename);
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
        Logger.debug("timeChanged(mediaPlayer={},newLength={})", mediaPlayer, newLength);
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
        Logger.debug("videoOutput(mediaPlayer={},newCount={})", mediaPlayer, newCount);
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        Logger.debug("error(mediaPlayer={})", mediaPlayer);
    }

    // === Events relating to the current media =================================

    @Override
    public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
        Logger.debug("mediaSubItemAdded(mediaPlayer={},subItem={})", mediaPlayer, subItem);
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
        Logger.debug("mediaDurationChanged(mediaPlayer={},newDuration={})", mediaPlayer, newDuration);
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
        Logger.debug("mediaParsedChanged(mediaPlayer={},newStatus={})", mediaPlayer, newStatus);
    }

    @Override
    public void mediaFreed(MediaPlayer mediaPlayer) {
        Logger.debug("mediaFreed(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
        Logger.debug("mediaStateChanged(mediaPlayer={},newState={})", mediaPlayer, newState);
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
        Logger.debug("mediaMetaChanged(mediaPlayer={},metaType={})", mediaPlayer, metaType);
    }

    // === Synthetic/semantic events ============================================

    @Override
    public void newMedia(MediaPlayer mediaPlayer) {
        Logger.debug("newMedia(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
        Logger.debug("subItemPlayed(mediaPlayer={},subItemIndex={})", mediaPlayer, subItemIndex);
    }

    @Override
    public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
        Logger.debug("subItemFinished(mediaPlayer={},subItemIndex={})", mediaPlayer, subItemIndex);
    }

    @Override
    public void endOfSubItems(MediaPlayer mediaPlayer) {
        Logger.debug("endOfSubItems(mediaPlayer={})", mediaPlayer);
    }
}
