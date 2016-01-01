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

package uk.co.caprica.vlcj.player.list;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_playback_mode_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.AbstractMediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.NativeString;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEvent;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEventFactory;
import uk.co.caprica.vlcj.player.list.events.MediaListPlayerEventType;

import com.sun.jna.Pointer;

/**
 * Implementation of a media list player.
 */
public class DefaultMediaListPlayer extends AbstractMediaPlayer implements MediaListPlayer {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DefaultMediaListPlayer.class);

    /**
     * Collection of event listeners.
     */
    private final List<MediaListPlayerEventListener> eventListenerList = new ArrayList<MediaListPlayerEventListener>();

    /**
     * Factory to create media player events from native events.
     */
    private final MediaListPlayerEventFactory eventFactory = new MediaListPlayerEventFactory(this);

    /**
     * Background service to notify event listeners.
     */
    private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

    /**
     * Event listener to handle next item events.
     */
    private final NextItemHandler nextItemHandler = new NextItemHandler();

    /**
     * Native media player instance.
     */
    private libvlc_media_list_player_t mediaListPlayerInstance;

    /**
     * Native event manager instance.
     */
    private libvlc_event_manager_t mediaListPlayerEventManager;

    /**
     * Native event call-back.
     */
    private libvlc_callback_t callback;

    /**
     * Associated native media player instance.
     * <p>
     * This may be <code>null</code>.
     */
    private MediaPlayer mediaPlayer;

    /**
     * Mask of the native events that will cause notifications to be sent to listeners.
     */
    private long eventMask = MediaListPlayerEventType.ALL.value();

    /**
     * Media list.
     */
    private MediaList mediaList;

    /**
     * Opaque reference to user/application-specific data associated with this media player.
     */
    private Object userData;

    /**
     * MRL of the current media item.
     */
    private final AtomicReference<String> currentMrl = new AtomicReference<String>();

    /**
     * Set to true when the player has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Create a new media list player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public DefaultMediaListPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        super(libvlc, instance);
        logger.debug("DefaultMediaListPlayer(libvlc={}, instance={})", libvlc, instance);
        createInstance();
    }

    @Override
    public void addMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        logger.debug("addMediaPlayerEventListener(listener={})", listener);
        eventListenerList.add(listener);
    }

    @Override
    public void removeMediaListPlayerEventListener(MediaListPlayerEventListener listener) {
        logger.debug("removeMediaPlayerEventListener(listener={})", listener);
        eventListenerList.remove(listener);
    }

    @Override
    public void enableEvents(long eventMask) {
        logger.debug("enableEvents(eventMask={})", eventMask);
        this.eventMask = eventMask;
    }

    @Override
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        logger.debug("setMediaPlayer(mediaPlayer={})", mediaPlayer);
        this.mediaPlayer = mediaPlayer;
        libvlc.libvlc_media_list_player_set_media_player(mediaListPlayerInstance, mediaPlayer.mediaPlayerInstance());
    }

    @Override
    public void setMediaList(MediaList mediaList) {
        logger.debug("setMediaList(mediaList={})", mediaList);
        libvlc.libvlc_media_list_player_set_media_list(mediaListPlayerInstance, mediaList.mediaListInstance());
        this.mediaList = mediaList;
    }

    @Override
    public MediaList getMediaList() {
        logger.debug("getMediaList()");
        return mediaList;
    }

    @Override
    public void play() {
        logger.debug("play()");
        attachVideoSurface();
        libvlc.libvlc_media_list_player_play(mediaListPlayerInstance);
    }

    @Override
    public void pause() {
        logger.debug("pause()");
        libvlc.libvlc_media_list_player_pause(mediaListPlayerInstance);
    }

    @Override
    public void stop() {
        logger.debug("stop()");
        libvlc.libvlc_media_list_player_stop(mediaListPlayerInstance);
    }

    @Override
    public boolean playItem(int itemIndex) {
        logger.debug("playItem(itemIndex={})", itemIndex);
        if(mediaList != null && itemIndex >= 0 && itemIndex < mediaList.size()) {
            attachVideoSurface();
            return libvlc.libvlc_media_list_player_play_item_at_index(mediaListPlayerInstance, itemIndex) == 0;
        }
        else {
            throw new IllegalArgumentException("Item index is out of bounds");
        }
    }

    @Override
    public void playNext() {
        logger.debug("playNext()");
        libvlc.libvlc_media_list_player_next(mediaListPlayerInstance);
    }

    @Override
    public void playPrevious() {
        logger.debug("playPrevious()");
        libvlc.libvlc_media_list_player_previous(mediaListPlayerInstance);
    }

    @Override
    public boolean isPlaying() {
        logger.debug("isPlaying()");
        return libvlc.libvlc_media_list_player_is_playing(mediaListPlayerInstance) != 0;
    }

    @Override
    public libvlc_state_t getMediaListPlayerState() {
        logger.debug("getMediaListPlayerState()");
        return libvlc_state_t.state(libvlc.libvlc_media_list_player_get_state(mediaListPlayerInstance));
    }

    @Override
    public void setMode(MediaListPlayerMode mode) {
        logger.debug("setMode(mode={})", mode);
        libvlc_playback_mode_e playbackMode;
        switch(mode) {
            case DEFAULT:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_default;
                break;

            case LOOP:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_loop;
                break;

            case REPEAT:
                playbackMode = libvlc_playback_mode_e.libvlc_playback_mode_repeat;
                break;

            default:
                throw new IllegalArgumentException("Invalid mode " + mode);
        }
        libvlc.libvlc_media_list_player_set_playback_mode(mediaListPlayerInstance, playbackMode.intValue());
    }

    @Override
    public String mrl(libvlc_media_t mediaInstance) {
        logger.debug("mrl(mediaInstance={})", mediaInstance);
        return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
    }

    @Override
    public Object userData() {
        logger.debug("userData()");
        return userData;
    }

    @Override
    public void userData(Object userData) {
        logger.debug("userData(userData={})", userData);
        this.userData = userData;
    }

    @Override
    public final String currentMrl() {
        logger.debug("currentMrl()");
        return currentMrl.get();
    }

    @Override
    public libvlc_media_player_t getMediaPlayerInstance() {
        logger.debug("getMediaPlayerInstance()");
        return libvlc.libvlc_media_list_player_get_media_player(mediaListPlayerInstance);
    }

    @Override
    public final void release() {
        logger.debug("release()");
        if(released.compareAndSet(false, true)) {
            destroyInstance();
        }
    }

    /**
     *
     */
    private void createInstance() {
        logger.debug("createInstance()");

        mediaListPlayerInstance = libvlc.libvlc_media_list_player_new(instance);

        mediaListPlayerEventManager = libvlc.libvlc_media_list_player_event_manager(mediaListPlayerInstance);
        logger.debug("mediaListPlayerEventManager={}", mediaListPlayerEventManager);

        registerEventListener();

        addMediaListPlayerEventListener(nextItemHandler);
    }

    /**
     *
     */
    private void destroyInstance() {
        logger.debug("destroyInstance()");

        logger.debug("Detach events...");
        deregisterEventListener();
        logger.debug("Events detached.");

        eventListenerList.clear();

        nextItemHandler.release();

        if(mediaListPlayerInstance != null) {
            logger.debug("Release media list player...");
            libvlc.libvlc_media_list_player_release(mediaListPlayerInstance);
            logger.debug("Media list player released");
        }

        logger.debug("Shut down listeners...");
        listenersService.shutdown();
        logger.debug("Listeners shut down.");
    }

    /**
     *
     */
    private void registerEventListener() {
        logger.debug("registerEventListener()");
        callback = new VlcVideoPlayerCallback();
        for(libvlc_event_e event : libvlc_event_e.values()) {
            // The native event manager reports that it does not support
            // libvlc_MediaListPlayerPlayed
            if(event.intValue() >= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListPlayerStopped.intValue()) {
                logger.debug("event={}", event);
                int result = libvlc.libvlc_event_attach(mediaListPlayerEventManager, event.intValue(), callback, null);
                logger.debug("result={}", result);
            }
        }
    }

    /**
     *
     */
    private void deregisterEventListener() {
        logger.debug("deregisterEventListener()");
        if(callback != null) {
            for(libvlc_event_e event : libvlc_event_e.values()) {
                // The native event manager reports that it does not support
                // libvlc_MediaListPlayerPlayed
                if(event.intValue() >= libvlc_event_e.libvlc_MediaListPlayerNextItemSet.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListPlayerStopped.intValue()) {
                    logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaListPlayerEventManager, event.intValue(), callback, null);
                }
            }
            callback = null;
        }
    }

    /**
     * If there is an associated media player then make sure the video surface is attached.
     */
    private void attachVideoSurface() {
        if(mediaPlayer instanceof EmbeddedMediaPlayer) {
            ((EmbeddedMediaPlayer)mediaPlayer).attachVideoSurface();
        }
    }

    /**
     * A call-back to handle events from the native media player.
     * <p>
     * There are some important implementation details for this callback:
     * <ul>
     * <li>First, the event notifications are off-loaded to a different thread so as to prevent
     * application code re-entering libvlc in an event call-back which may lead to a deadlock in the
     * native code;</li>
     * <li>Second, the native event union structure refers to natively allocated memory which will
     * not be in the scope of the thread used to actually dispatch the event notifications.</li>
     * </ul>
     * Without copying the fields at this point from the native union structure, the native memory
     * referred to by the native event is likely to get deallocated and overwritten by the time the
     * notification thread runs. This would lead to unreliable data being sent with the
     * notification, or even a fatal JVM crash.
     */
    private final class VlcVideoPlayerCallback implements libvlc_callback_t {

        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            logger.trace("callback(event={},userData={})", event, userData);
            if(!eventListenerList.isEmpty()) {
                // Create a new media player event for the native event
                MediaListPlayerEvent mediaListPlayerEvent = eventFactory.newMediaListPlayerEvent(event, eventMask);
                logger.trace("mediaListPlayerEvent={}", mediaListPlayerEvent);
                if(mediaListPlayerEvent != null) {
                    listenersService.submit(new NotifyListenersRunnable(mediaListPlayerEvent));
                }
            }
        }
    }

    /**
     *
     */
    private final class NotifyListenersRunnable implements Runnable {

        /**
         *
         */
        private final MediaListPlayerEvent mediaListPlayerEvent;

        /**
         *
         *
         * @param mediaListPlayerEvent
         */
        private NotifyListenersRunnable(MediaListPlayerEvent mediaListPlayerEvent) {
            this.mediaListPlayerEvent = mediaListPlayerEvent;
        }

        @Override
        public void run() {
            logger.trace("run()");
            for(int i = eventListenerList.size() - 1; i >= 0; i -- ) {
                MediaListPlayerEventListener listener = eventListenerList.get(i);
                try {
                    mediaListPlayerEvent.notify(listener);
                }
                catch(Exception e) {
                    logger.warn("Event listener {} threw an exception", e, listener);
                    // Continue with the next listener...
                }
            }
            logger.trace("runnable exits");
        }
    }

    /**
     *
     */
    private class NextItemHandler extends MediaListPlayerEventAdapter {

        /**
         * Current media instance.
         */
        private libvlc_media_t mediaInstance;

        @Override
        public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
            logger.debug("nextItem(item={},itemMrl={})", item, itemMrl);
            deregisterMediaEventListener();
            this.mediaInstance = item;
            currentMrl.set(itemMrl);
            registerMediaEventListener();
        }

        /**
         *
         */
        private void registerMediaEventListener() {
            logger.debug("registerMediaEventListener()");
            // If there is a media, register a new listener...
            if(mediaInstance != null) {
                libvlc.libvlc_media_retain(mediaInstance);
                libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
                for(libvlc_event_e event : libvlc_event_e.values()) {
                    if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
                        logger.debug("event={}", event);
                        int result = libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                        logger.debug("result={}", result);
                    }
                }
            }
        }

        /**
         *
         */
        private void deregisterMediaEventListener() {
            logger.debug("deregisterMediaEventListener()");
            // If there is a media, deregister the listener...
            if(mediaInstance != null) {
                libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
                for(libvlc_event_e event : libvlc_event_e.values()) {
                    if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
                        logger.debug("event={}", event);
                        libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                    }
                }
                mediaEventManager = null;
                libvlc.libvlc_media_release(mediaInstance);
                mediaInstance = null;
            }
        }

        /**
         *
         */
        private void release() {
            if (mediaInstance != null) {
                libvlc.libvlc_media_release(mediaInstance);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        logger.debug("finalize()");
        logger.debug("Media list player has been garbage collected");
        super.finalize();
    }
}
