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

package uk.co.caprica.vlcj.test.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * Implementation of a {@link MediaPlayerEventListener} that logs all invocations.
 * <p>
 * Useful only for testing.
 */
public class LoggingMediaPlayerEventAdapter implements MediaPlayerEventListener {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(LoggingMediaPlayerEventAdapter.class);

    // === Events relating to the media player ==================================

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
        logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})", mediaPlayer, media, mrl);
    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {
        logger.debug("opening(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {
        logger.debug("buffering(mediaPlayer={},newCache={})", mediaPlayer, newCache);
    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        logger.debug("playing(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        logger.debug("paused(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        logger.debug("stopped(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {
        logger.debug("forward(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {
        logger.debug("backward(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        logger.debug("finished(mediaPlayer={}", mediaPlayer);
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        logger.debug("timeChanged(mediaPlayer={},newTime={})", mediaPlayer, newTime);
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
        logger.debug("positionChanged(mediaPlayer={},newPosition={})", mediaPlayer, newPosition);
    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
        logger.debug("seekableChanged(mediaPlayer={},newSeekable={})", mediaPlayer, newSeekable);
    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
        logger.debug("pausableChanged(mediaPlayer={},newPausable={})", mediaPlayer, newPausable);
    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
        logger.debug("titleChanged(mediaPlayer={},newTitle={})", mediaPlayer, newTitle);
    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
        logger.debug("snapshotTaken(mediaPlayer={},filename={})", mediaPlayer, filename);
    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
        logger.debug("timeChanged(mediaPlayer={},newLength={})", mediaPlayer, newLength);
    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
        logger.debug("videoOutput(mediaPlayer={},newCount={})", mediaPlayer, newCount);
    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
        logger.debug("scrambledChanged(mediaPlayer={},newScrambled={})", mediaPlayer, newScrambled);
    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id) {
        logger.debug("elementaryStreamAdded(mediaPlayer={},type={},id={})", mediaPlayer, type, id);
    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id) {
        logger.debug("elementaryStreamDeleted(mediaPlayer={},type={},id={})", mediaPlayer, type, id);
    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id) {
        logger.debug("elementaryStreamSelected(mediaPlayer={},type={},id={})", mediaPlayer, type, id);
    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {
        logger.debug("corked(mediaPlayer={},corked={})", mediaPlayer, corked);
    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {
        logger.debug("muted(mediaPlayer={},muted={})", mediaPlayer, muted);
    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
        logger.debug("volumeChanged(mediaPlayer={},volume={})", mediaPlayer, volume);
    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
        logger.debug("audioDeviceChanged(mediaPlayer={},audioDevice={})", mediaPlayer, audioDevice);
    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
        logger.debug("chapterChanged(mediaPlayer={},newChapter={})", mediaPlayer, newChapter);
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        logger.debug("error(mediaPlayer={})", mediaPlayer);
    }

    // === Events relating to the current media =================================

    @Override
    public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
        logger.debug("mediaSubItemAdded(mediaPlayer={},subItem={})", mediaPlayer, subItem);
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
        logger.debug("mediaDurationChanged(mediaPlayer={},newDuration={})", mediaPlayer, newDuration);
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
        logger.debug("mediaParsedChanged(mediaPlayer={},newStatus={})", mediaPlayer, newStatus);
    }

    @Override
    public void mediaParsedStatus(MediaPlayer mediaPlayer, int newStatus) {
        logger.debug("mediaParsedStatus(mediaPlayer={},newStatus={})", mediaPlayer, newStatus);
    }

    @Override
    public void mediaFreed(MediaPlayer mediaPlayer) {
        logger.debug("mediaFreed(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
        logger.debug("mediaStateChanged(mediaPlayer={},newState={})", mediaPlayer, newState);
    }

    @Override
    public void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t item) {
        logger.debug("mediaSubItemTreeAdded(mediaPlayer={},newState={})", mediaPlayer, item);
    }

    @Override
    public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
        logger.debug("mediaMetaChanged(mediaPlayer={},metaType={})", mediaPlayer, metaType);
    }

    // === Synthetic/semantic events ============================================

    @Override
    public void newMedia(MediaPlayer mediaPlayer) {
        logger.debug("newMedia(mediaPlayer={})", mediaPlayer);
    }

    @Override
    public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
        logger.debug("subItemPlayed(mediaPlayer={},subItemIndex={})", mediaPlayer, subItemIndex);
    }

    @Override
    public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
        logger.debug("subItemFinished(mediaPlayer={},subItemIndex={})", mediaPlayer, subItemIndex);
    }

    @Override
    public void endOfSubItems(MediaPlayer mediaPlayer) {
        logger.debug("endOfSubItems(mediaPlayer={})", mediaPlayer);
    }
}
