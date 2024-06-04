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
 * Copyright 2009-2024 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.media.EventApi;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventListener;
import uk.co.caprica.vlcj.media.MediaFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.MediaSlavePriority;
import uk.co.caprica.vlcj.media.MediaSlaveType;
import uk.co.caprica.vlcj.media.MetaApi;
import uk.co.caprica.vlcj.media.OptionsApi;
import uk.co.caprica.vlcj.media.ParseApi;
import uk.co.caprica.vlcj.media.SlaveApi;
import uk.co.caprica.vlcj.media.StatsApi;
import uk.co.caprica.vlcj.media.SubitemApi;
import uk.co.caprica.vlcj.media.callback.CallbackMedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_add_slave;
import static uk.co.caprica.vlcj.binding.lib.LibVlc.libvlc_media_player_set_media;

/**
 * Behaviour pertaining to the current media.
 */
public final class MediaApi extends BaseApi {

    private static final String START_PAUSED_OPTION = "start-paused";

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

    MediaApi(MediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Prepare new media (set it, do not play it).
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     * <p>
     * Where the MRL refers to a file, then <em>native</em> file separators <em>must</em> be used, not the Java platform
     * independent file separator.
     * <p>
     * This means that, for example, on Windows platforms something lke "C:\\Movies\\My Cool Movie.mp4" must be used
     * instead of "C:/Movies/My Cool Movie.mp4". Additionally note that a backslash character must be escaped as would
     * be expected for any other Java string.
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(String mrl, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlcInstance, mrl, options));
    }

    /**
     * Set new media and play it.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     * <p>
     * Where the MRL refers to a file, then <em>native</em> file separators <em>must</em> be used, not the Java platform
     * independent file separator.
     * <p>
     * This means that, for example, on Windows platforms something lke "C:\\Movies\\My Cool Movie.mp4" must be used
     * instead of "C:/Movies/My Cool Movie.mp4". Additionally note that a backslash character must be escaped as would
     * be expected for any other Java string.
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
     * <p>
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     * <p>
     * Where the MRL refers to a file, then <em>native</em> file separators <em>must</em> be used, not the Java platform
     * independent file separator.
     * <p>
     * This means that, for example, on Windows platforms something lke "C:\\Movies\\My Cool Movie.mp4" must be used
     * instead of "C:/Movies/My Cool Movie.mp4". Additionally note that a backslash character must be escaped as would
     * be expected for any other Java string.
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
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     *
     * @param callbackMedia callback media
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(CallbackMedia callbackMedia, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlcInstance, callbackMedia, options));
    }

    /**
     * Set new media and play it.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
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
     * <p>
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
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
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     *
     * @param mediaRef media reference
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean prepare(MediaRef mediaRef, String... options) {
        return changeMedia(MediaFactory.newMedia(libvlcInstance, mediaRef, options));
    }

    /**
     * Set new media and play it.
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
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
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
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
     * Set new media, play it, and wait for it to start playing (or error), pausing immediately on the first frame.
     * <p>
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     *
     * @param mrl media resource locator
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean startPaused(String mrl, String... options) {
        // Starting with start-paused will generate a "playing" event, which this start call waits for
        boolean started = start(mrl, startPausedOptions(options));
        if (started) {
            // First frame does not always paint unless the position is reset
            mediaPlayer.controls().setPosition(0);
        }
        return started;
    }

    /**
     * Set new media, play it, and wait for it to start playing (or error), pausing immediately on the first frame.
     * <p>
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     *
     * @param callbackMedia callback media
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean startPaused(CallbackMedia callbackMedia, String... options) {
        // Starting with start-paused will generate a "playing" event, which this start call waits for
        boolean started = start(callbackMedia, startPausedOptions(options));
        if (started) {
            // First frame does not always paint unless the position is reset
            mediaPlayer.controls().setPosition(0);
        }
        return started;
    }

    /**
     * Set new media, play it, and wait for it to start playing (or error), pausing immediately on the first frame.
     * <p>
     * This method should be used only when the media player is in a stopped state, otherwise behaviour is undefined.
     * <p>
     * The supplied {@link MediaRef} is not kept by this component and <em>must</em> be released by the caller when the
     * caller no longer has any use for it.
     * <p>
     * <strong>Setting media is now an asynchronous operation.</strong>
     *
     * @param mediaRef media reference
     * @param options zero or more options to attach to the new media
     * @return <code>true</code> if successful; <code>false</code> on error
     */
    public boolean startPaused(MediaRef mediaRef, String... options) {
        // Starting with start-paused will generate a "playing" event, which this start call waits for
        boolean started = start(mediaRef, startPausedOptions(options));
        if (started) {
            // First frame does not always paint unless the position is reset
            mediaPlayer.controls().setPosition(0);
        }
        return started;
    }

    /**
     * Add an input slave to the current media.
     * <p>
     * See {@link SlaveApi#add(MediaSlaveType, MediaSlavePriority, String)}  for further
     * important information regarding this method.
     *
     * @param type type of slave to add
     * @param uri URI of the slave to add
     * @param select <code>true</code> if this slave should be automatically selected when added
     * @return <code>true</code> on success; <code>false</code> otherwise
     */
    public boolean addSlave(MediaSlaveType type, String uri, boolean select) {
        return libvlc_media_player_add_slave(mediaPlayerInstance, type.intValue(), uri, select ? 1 : 0) == 0;
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
    public EventApi events() {
        return media != null ? media.events() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media information.
     *
     * @return media information behaviour
     */
    public InfoApi info() {
        return media != null ? media.info() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media meta data.
     *
     * @return media meta data behaviour
     */
    public MetaApi meta() {
        return media != null ? media.meta() : null;
    }

    /**
     * Delegated behaviour pertaining to media options.
     *
     * @return media option behaviour
     */
    public OptionsApi options() {
        return media != null ? media.options() : null;
    }

    /**
     * Delegated behaviour pertaining to parsing of the associated media.
     *
     * @return parsing behaviour
     */
    public ParseApi parsing() {
        return media != null ? media.parsing() : null;
    }

    /**
     * Delegated behaviour pertaining to media slaves for the associated media.
     *
     * @return media slave behaviour
     */
    public SlaveApi slaves() {
        return media != null ? media.slaves() : null;
    }

    /**
     * Delegated behaviour pertaining to stat values for the associated media.
     *
     * @return media stat behaviour
     */
    public StatsApi stats() {
        return media != null ? media.stats() : null;
    }

    /**
     * Delegated behaviour pertaining to the associated media subitems.
     *
     * @return subitem behaviour
     */
    public SubitemApi subitems() {
        return media != null ? media.subitems() : null;
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
        return mediaPlayer.controls().play();
    }

    private boolean start() {
        return mediaPlayer.controls().start();
    }

    private void applyMedia() {
        libvlc_media_t mediaInstance = media.mediaInstance();
        // Setting media is asynchronous
        libvlc_media_player_set_media(mediaPlayerInstance, mediaInstance);
        mediaPlayer.subitems().changeMedia(mediaInstance);
    }

    private String[] startPausedOptions(String... options) {
        List<String> list = new ArrayList<String>(options != null ? options.length + 1 : 1);
        list.add(START_PAUSED_OPTION);
        if (options != null) {
            list.addAll(Arrays.asList(options));
        }
        return list.toArray(new String[0]);
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
