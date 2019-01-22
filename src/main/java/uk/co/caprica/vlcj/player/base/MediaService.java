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

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.callbackmedia.CallbackMedia;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaFactory;

/**
 *
 */
public final class MediaService extends BaseService {

    /**
     * Current media.
     */
    private Media media;

    /**
     * Flag if this component created and therefore owns the {@link #media} instance or not.
     * <p>
     * This flag will be true if this component created the media instance, e.g. if {@link #playMedia(String, String...)}
     * was used to set the media. In this case this component is responsible for the lifecycle of the media instance and
     * will release it when it is no longer needed.
     * <p>
     * It will be false if the media was supplied by the caller, i.e. via {@link #set(Media)}. In that case, the caller
     * owns the media instance and is responsible for its lifecycle - in particular the caller must release the media
     * instance when it is no longer needed.
     */
    private boolean ownMedia;

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    MediaService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set new media.
     *
     * @param media media to set
     * @return
     */
    public boolean set(Media media) {
        return changeMedia(media, false);
    }

    /**
     * Prepare new media (set it, do not play it).
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return
     */
    // FIXME pending rename prepare()
    public boolean prepareMedia(String mrl, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlc, libvlcInstance, mrl, options), true);
    }

    /**
     * Set new media and play it.
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return
     */
    // FIXME pending rename play()
    public boolean playMedia(String mrl, String... options) {
        if (prepareMedia(mrl, options)) {
            mediaPlayer.controls().play();
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     *
     * @param mrl
     * @param options
     * @return
     */
    // FIXME pending rename start()
    public boolean startMedia(String mrl, String... options) {
        if (prepareMedia(mrl, options)) {
            return mediaPlayer.controls().start();
        } else {
            return false;
        }
    }

    public boolean prepare(CallbackMedia callbackMedia, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlc, libvlcInstance, callbackMedia, options), true);
    }

    public boolean play(CallbackMedia callbackMedia, String... options) {
        if (prepare(callbackMedia, options)) {
            mediaPlayer.controls().play();
            return true;
        } else {
            return false;
        }
    }

    public boolean start(CallbackMedia callbackMedia, String... options) {
        if (prepare(callbackMedia, options)) {
            return mediaPlayer.controls().start();
        } else {
            return false;
        }
    }

    /**
     * Get the current media.
     *
     * @return media
     */
    public Media get() {
        return media;
    }

    /**
     * Reset the current media, if there is one.
     * <p>
     * This simply sets the last played media again so it may be played again. Without this, the current media would not
     * be able to be played again if it had finished.
     */
    public void reset() {
        if (this.media != null) {
            changeMedia(this.media, true);
        }
    }

    /**
     * Set whether or not the media player should automatically repeat playing the media when it has
     * finished playing.
     * <p>
     * There is <em>no</em> guarantee of seamless play-back when using this method - see instead
     * {@link uk.co.caprica.vlcj.player.list.MediaListPlayer MediaListPlayer}.
     *
     * @param repeat <code>true</code> to automatically replay the media, otherwise <code>false</code>
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Get whether or not the media player will automatically repeat playing the media when it has
     * finished playing.
     *
     * @return <code>true</code> if the media will be automatically replayed, otherwise <code>false</code>
     */
    public boolean getRepeat() {
        return repeat;
    }

    /**
     * Change media, i.e. clean up the current media and setup the new one.
     *
     * @param newMedia new media - this may be <code>null</code>, in which case the current media is cleaned up only
     * @param ownMedia <code>true</code> if this component created the media instance; <code>false</code> if it did not
     * @return <code>true</code> if the media was successfully changed; <code>false</code> on error, or if newMedia was <code>null</code>
     */
    private boolean changeMedia(Media newMedia, boolean ownMedia) {
        // The old media must only be released if this component originally created it
        if (this.ownMedia) {
            this.media.release();
        }
        if (newMedia != null) {
            this.media = newMedia;
            this.ownMedia = ownMedia;
            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, media.mediaInstance());
            mediaPlayer.subItems().changeMedia(media);
            return true;
        } else {
            this.media = null;
            this.ownMedia = false;
            return false;
        }
    }

    @Override
    protected void release() {
        if (ownMedia) {
            media.release();
        }
    }

}
