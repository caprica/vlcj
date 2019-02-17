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

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;
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
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MediaEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Behaviour pertaining to the current media.
 */
public final class MediaService extends BaseService {

    private final List<MediaEventListener> persistentMediaEventListeners = new ArrayList<MediaEventListener>();

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
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(String mrl, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlc, libvlcInstance, mrl, options));
    }

    /**
     * Set new media and play it.
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean play(String mrl, String... options) {
        if (prepare(mrl, options)) {
            return play();
        } else {
            return false;
        }
    }

    /**
     * Set new media, play it, and wait for it to start playing (or error).
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean start(String mrl, String... options) {
        if (prepare(mrl, options)) {
            return start();
        } else {
            return false;
        }
    }

    /**
     * Prepare new media (set it, do not play it).
     *
     * @param callbackMedia callback media
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(CallbackMedia callbackMedia, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlc, libvlcInstance, callbackMedia, options));
    }

    /**
     * Set new media and play it.
     *
     * @param callbackMedia callback media
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean play(CallbackMedia callbackMedia, String... options) {
        if (prepare(callbackMedia, options)) {
            return play();
        } else {
            return false;
        }
    }

    /**
     * Set new media, play it, and wait for it to start playing (or error).
     *
     * @param callbackMedia callback media
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean start(CallbackMedia callbackMedia, String... options) {
        if (prepare(callbackMedia, options)) {
            return start();
        } else {
            return false;
        }
    }

    /**
     * Prepare new media (set it, do not play it).
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     *
     * @param mediaRef media reference
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(MediaRef mediaRef, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlc, libvlcInstance, mediaRef, options));
    }

    /**
     * Set new media and play it.
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     *
     * @param mediaRef media reference
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean play(MediaRef mediaRef, String... options) {
        if (prepare(mediaRef, options)) {
            return play();
        } else {
            return false;
        }
    }

    /**
     * Set new media, play it, and wait for it to start playing (or error).
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     *
     * @param mediaRef media reference
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean start(MediaRef mediaRef, String... options) {
        if (prepare(mediaRef, options)) {
            return start();
        } else {
            return false;
        }
    }

    /**
     * Create a new {@link Media} component for the current media.
     * <p>
     * The caller <em>must</em> release the returned {@link Media} when it no longer has any use for it.
     *
     * @return media
     */
    public Media newMedia() {
        return media != null ? media.newMedia() : null;
    }

    /**
     * Create a new {@link MediaRef} for the current media.
     * <p>
     * The caller <em>must</em> release the returned {@link MediaRef} when it no longer has any use for it.
     *
     * @return media reference
     */
    public MediaRef newMediaRef() {
        return media != null ? media.newMediaRef() : null;
    }

    /**
     * Is the media valid?
     * <p>
     * This method could be used to check there is media before using the various behaviours.
     *
     * @return <code>true</code> if there is a valid media; <code>false</code> if there is not
     */
    public boolean isValid() {
        return media != null;
    }

    /**
     * Delegated behaviour pertaining to the associated media events.
     *
     * @return media event behaviour
     */
    public EventService events() {
        return media != null ? media.events() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media information.
     *
     * @return media information behaviour
     */
    public InfoService info() {
        return media != null ? media.info() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media meta data.
     *
     * @return media meta data behaviour
     */
    public MetaService meta() {
        return media != null ? media.meta() : null;
    }

    /**
     * Delegated behaviour pertaining to media options.
     *
     * @return media option behaviour
     */
    public OptionsService options() {
        return media != null ? media.options() : null;
    }

    /**
     * Delegated behaviour pertaining to parsing of the associated media.
     *
     * @return parsing behaviour
     */
    public ParseService parsing() {
        return media != null ? media.parsing() : null;
    }

    /**
     * Delegated behaviour pertaining to media slaves for the associated media.
     *
     * @return media slave behaviour
     */
    public SlaveService slaves() {
        return media != null ? media.slaves() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media subitems.
     *
     * @return subitem behaviour
     */
    public SubitemService subitems() {
        return media != null ? media.subitems() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media userdata.
     *
     * @return userdata behaviour
     */
    public UserDataService userData() {
        return media != null ? media.userData() : null;
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
     * @return <code>true</code> if the media was successfully changed; <code>false</code> on error, or if newMedia was <code>null</code>
     */
    private boolean changeMedia(Media newMedia) {
        if (this.media != null) {
            this.media.release();
        }
        if (newMedia != null) {
            this.media = newMedia;
            setPersistentEventListeners();
            applyMedia();
            return true;
        } else {
            this.media = null;
            return false;
        }
    }

    private boolean play() {
        mediaPlayer.controls().play();
        return true;
    }

    private boolean start() {
        return mediaPlayer.controls().start();
    }

    private void applyMedia() {
        libvlc_media_t mediaInstance = media.mediaInstance();
        libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaInstance);
        mediaPlayer.subitems().changeMedia(mediaInstance);
    }

    void addPersistentMediaEventListener(MediaEventListener listener) {
        persistentMediaEventListeners.add(listener);
        if (this.media != null) {
            media.events().addMediaEventListener(listener);
        }
    }

    void removePersistentMediaEventListener(MediaEventListener listener) {
        persistentMediaEventListeners.remove(listener);
        if (this.media != null) {
            media.events().removeMediaEventListener(listener);
        }
    }

    /**
     * Invoked each time the media is changed to add all registered persistent media event listeners.
     * <p>
     * Changing the media already wipes out any previously registered listeners on that media so there is no need to
     * explicitly unregister them.
     */
    private void setPersistentEventListeners() {
        if (this.media != null) {
            for (MediaEventListener listener : persistentMediaEventListeners) {
                media.events().addMediaEventListener(listener);
            }
        }
    }

    @Override
    protected void release() {
        if (media != null) {
            media.release();
        }
    }

}
