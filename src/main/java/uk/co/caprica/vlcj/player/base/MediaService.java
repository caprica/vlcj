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
import uk.co.caprica.vlcj.media.EventService;
import uk.co.caprica.vlcj.media.InfoService;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.media.MetaService;
import uk.co.caprica.vlcj.media.OptionsService;
import uk.co.caprica.vlcj.media.ParseService;
import uk.co.caprica.vlcj.media.SlaveService;
import uk.co.caprica.vlcj.media.SubitemService;
import uk.co.caprica.vlcj.media.UserDataService;

/**
 *
 */
public final class MediaService extends BaseService {

    /**
     * Current media.
     * <p>
     * It is an important principle that this component "owns" this {@link Media} instance and that the instance is
     * never exposed directly to client applications. The instance will be freed each time new media is prepared/played
     * or started.
     * <p>
     * Various aspects of the media are exposed to clients by methods here that delegate to the corresponding methods on
     * the media instance.
     * <p>
     * This media instance may be <code>null</code>, this will be the case if no media has been played yet, or there was
     * an error when trying to play new media.
     * <p>
     * A client application must therefore be prepared to handle the case where these delegating methods actually return
     * <code>null</code> - either by testing the return value from those methods, or by checking the result of
     * {@link #isValid()} beforehand.
     */
    private Media media;

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    MediaService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
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

    public boolean isValid() {
        return media != null;
    }

    public EventService events() {
        return media != null ? media.events() : null;
    }

    public InfoService info() {
        return media != null ? media.info() : null;
    }

    public MetaService meta() {
        return media != null ? media.meta() : null;
    }

    public OptionsService options() {
        return media != null ? media.options() : null;
    }

    public ParseService parsing() {
        return media != null ? media.parsing() : null;
    }

    public SlaveService slaves() {
        return media != null ? media.slaves() : null;
    }

    public SubitemService subitems() {
        return media != null ? media.subitems() : null;
    }

    public UserDataService userData() {
        return media != null ? media.userData() : null;
    }

    /**
     * Reset the current media, if there is one.
     * <p>
     * This simply sets the last played media again so it may be played again. Without this, the current media would not
     * be able to be played again if it had finished.
     */
    public void reset() {
        if (this.media != null) {
            applyMedia();
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
        if (this.media != null) {
            this.media.release();
        }
        if (newMedia != null) {
            this.media = newMedia;
            applyMedia();
            return true;
        } else {
            this.media = null;
            return false;
        }
    }

    private void applyMedia() {
        libvlc.libvlc_media_player_set_media(mediaPlayerInstance, media.mediaInstance());
        mediaPlayer.subItems().changeMedia(media);
    }

    @Override
    protected void release() {
        if (media != null) {
            media.release();
        }
    }

}
