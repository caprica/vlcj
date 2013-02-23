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

package uk.co.caprica.vlcj.medialist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_meta_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.medialist.events.MediaListEvent;
import uk.co.caprica.vlcj.medialist.events.MediaListEventFactory;
import uk.co.caprica.vlcj.player.MediaResourceLocator;
import uk.co.caprica.vlcj.player.NativeString;

import com.sun.jna.Pointer;

/**
 * A media list (i.e. a play-list).
 * <p>
 * To do anything more advanced than the functionality provided by this class, the underlying native
 * media list instance is accessible via {@link #mediaListInstance}.
 */
public class MediaList {

    /**
     * Collection of media player event listeners.
     */
    private final List<MediaListEventListener> eventListenerList = new ArrayList<MediaListEventListener>();

    /**
     * Factory to create media player events from native events.
     */
    private final MediaListEventFactory eventFactory = new MediaListEventFactory(this);

    /**
     * Background thread to event notifications.
     * <p>
     * The single-threaded nature of this executor service ensures that events are delivered to
     * listeners in a thread-safe manner and in their proper sequence.
     */
    private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

    /**
     * Native interface.
     */
    private final LibVlc libvlc;

    /**
     * Native library instance.
     */
    private final libvlc_instance_t instance;

    /**
     * Play-list instance.
     */
    private libvlc_media_list_t mediaListInstance;

    /**
     * Event manager instance.
     */
    private libvlc_event_manager_t mediaListEventManager;

    /**
     * Call-back to handle native media player events.
     */
    private libvlc_callback_t callback;

    /**
     * Set to true when the media list has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Standard media options to be applied to each media item that is played.
     */
    private String[] standardMediaOptions;

    /**
     * Create a new media list.
     *
     * @param libvlc native interface
     * @param instance native library instance
     */
    public MediaList(LibVlc libvlc, libvlc_instance_t instance) {
        this(libvlc, instance, null);
    }

    /**
     * Create a media list for a given native media list instance.
     *
     * @param libvlc native interface
     * @param instance native library instance
     * @param mediaListInstance media list instance
     */
    public MediaList(LibVlc libvlc, libvlc_instance_t instance, libvlc_media_list_t mediaListInstance) {
        this.libvlc = libvlc;
        this.instance = instance;
        createInstance(mediaListInstance);
    }

    /**
     * Add a component to be notified of media list events.
     *
     * @param listener component to add
     */
    public final void addMediaListEventListener(MediaListEventListener listener) {
        Logger.debug("addMediaListEventListener(listener={})", listener);
        eventListenerList.add(listener);
    }

    /**
     * Remove a component previously added so that it no longer receives media
     * list events.
     *
     * @param listener component to remove
     */
    public final void removeListEventListener(MediaListEventListener listener) {
        Logger.debug("removeMediaListEventListener(listener={})", listener);
        eventListenerList.remove(listener);
    }

    /**
     * Set standard media options for all media items subsequently played.
     * <p>
     * This will <strong>not</strong> affect any currently playing media item.
     *
     * @param standardMediaOptions options to apply to all subsequently played media items
     */
    public final void setStandardMediaOptions(String... standardMediaOptions) {
        Logger.debug("setStandardMediaOptions(standardMediaOptions={})", Arrays.toString(standardMediaOptions));
        this.standardMediaOptions = standardMediaOptions;
    }

    /**
     * Add a media item, with options, to the play-list.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     */
    public final void addMedia(String mrl, String... mediaOptions) {
        Logger.debug("addMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        try {
            lock();
            // Create a new native media descriptor
            libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
            // Insert the media descriptor into the media list
            libvlc.libvlc_media_list_add_media(mediaListInstance, mediaDescriptor);
            // Release the native reference
            releaseMediaDescriptor(mediaDescriptor);
        }
        finally {
            unlock();
        }
    }

    /**
     * Insert a media item, with options, to the play-list.
     *
     * @param index position at which to insert the media item (counting from zero)
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     */
    public final void insertMedia(int index, String mrl, String... mediaOptions) {
        Logger.debug("insertMedia(index={},mrl={},mediaOptions={})", index, mrl, Arrays.toString(mediaOptions));
        try {
            lock();
            // Create a new native media descriptor
            libvlc_media_t mediaDescriptor = newMediaDescriptor(mrl, mediaOptions);
            // Insert the media descriptor into the media list
            libvlc.libvlc_media_list_insert_media(mediaListInstance, mediaDescriptor, index);
            // Release the native reference
            releaseMediaDescriptor(mediaDescriptor);
        }
        finally {
            unlock();
        }
    }

    /**
     * Remove a media item from the play-list.
     *
     * @param index item to remove (counting from zero)
     */
    public final void removeMedia(int index) {
        Logger.debug("removeMedia(index={})", index);
        try {
            lock();
            libvlc_media_t oldMediaInstance = libvlc.libvlc_media_list_item_at_index(mediaListInstance, index);
            if(oldMediaInstance != null) {
                // Remove the media descriptor from the media list
                libvlc.libvlc_media_list_remove_index(mediaListInstance, index);
                // Release the native media instance
                libvlc.libvlc_media_release(oldMediaInstance);
            }
        }
        finally {
            unlock();
        }
    }

    /**
     * Clear the list.
     */
    public final void clear() {
        Logger.debug("clear()");
        try {
            lock();
            // Traverse the list from the end back to the start...
            for(int i = libvlc.libvlc_media_list_count(mediaListInstance)-1; i >= 0; i--) {
                libvlc.libvlc_media_list_remove_index(mediaListInstance, i);
            }
        }
        finally {
            unlock();
        }
    }

    /**
     * Get the number of items currently in the list.
     *
     * @return item count
     */
    public final int size() {
        Logger.debug("size()");
        try {
            lock();
            int size = libvlc.libvlc_media_list_count(mediaListInstance);
            Logger.debug("size={}", size);
            return size;
        }
        finally {
            unlock();
        }
    }

    /**
     * Test if the play-list is read-only.
     *
     * @return <code>true</code> if the play-list is currently read-only, otherwise <code>false</code>
     */
    public final boolean isReadOnly() {
        Logger.debug("isReadOnly()");
        return libvlc.libvlc_media_list_is_readonly(mediaListInstance) == 0;
    }

    /**
     * Get the list of items.
     *
     * @return list of items
     */
    public final List<MediaListItem> items() {
        Logger.debug("items()");
        List<MediaListItem> result = new ArrayList<MediaListItem>();
        try {
            lock();
            for(int i = 0; i < libvlc.libvlc_media_list_count(mediaListInstance); i++) {
                libvlc_media_t mediaInstance = libvlc.libvlc_media_list_item_at_index(mediaListInstance, i);
                result.add(newMediaListItem(mediaInstance));
                libvlc.libvlc_media_release(mediaInstance);
            }
        }
        finally {
            unlock();
        }
        return result;
    }

    /**
     * Create a new media list item for a give native media instance.
     *
     * @param mediaInstance native media instance
     * @return media list item
     */
    private MediaListItem newMediaListItem(libvlc_media_t mediaInstance) {
        String name = NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_meta(mediaInstance, libvlc_meta_t.libvlc_meta_Title.intValue()));
        String mrl = NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
        List<MediaListItem> subItems;
        libvlc_media_list_t subItemList = libvlc.libvlc_media_subitems(mediaInstance);
        if(subItemList != null) {
            try {
                libvlc.libvlc_media_list_lock(subItemList);
                subItems = new ArrayList<MediaListItem>();
                for(int i = 0; i < libvlc.libvlc_media_list_count(subItemList); i++) {
                    libvlc_media_t subItemInstance = libvlc.libvlc_media_list_item_at_index(subItemList, i);
                    subItems.add(newMediaListItem(subItemInstance));
                    libvlc.libvlc_media_release(subItemInstance);
                }
            }
            finally {
                libvlc.libvlc_media_list_unlock(subItemList);
            }
            libvlc.libvlc_media_list_release(subItemList);
        }
        else {
            subItems = Collections.emptyList();
        }
        return new MediaListItem(name, mrl, subItems);
    }

    /**
     * Clean up media list resources.
     */
    public final void release() {
        Logger.debug("release()");
        if(released.compareAndSet(false, true)) {
            destroyInstance();
        }
    }

    /**
     * Create and initialise a new media list instance.
     */
    private void createInstance(libvlc_media_list_t mediaListInstance) {
        Logger.debug("createInstance()");
        if(mediaListInstance == null) {
            mediaListInstance = libvlc.libvlc_media_list_new(instance);
        }
        else {
            libvlc.libvlc_media_list_retain(mediaListInstance);
        }

        this.mediaListInstance = mediaListInstance;
        Logger.debug("mediaListInstance={}", mediaListInstance);

        mediaListEventManager = libvlc.libvlc_media_list_event_manager(mediaListInstance);
        Logger.debug("mediaListEventManager={}", mediaListEventManager);

        registerEventListener();
    }

    /**
     * Clean up and free the media list instance.
     */
    private void destroyInstance() {
        Logger.debug("destroyInstance()");

        deregisterEventListener();

        if(mediaListInstance != null) {
            libvlc.libvlc_media_list_release(mediaListInstance);
        }

        Logger.debug("Shut down listeners...");
        listenersService.shutdown();
        Logger.debug("Listeners shut down.");
    }

    /**
     * Register a call-back to receive native media player events.
     */
    private void registerEventListener() {
        Logger.debug("registerEventListener()");
        callback = new MediaListCallback();
        for(libvlc_event_e event : libvlc_event_e.values()) {
            if(event.intValue() >= libvlc_event_e.libvlc_MediaListItemAdded.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListWillDeleteItem.intValue()) {
                Logger.debug("event={}", event);
                int result = libvlc.libvlc_event_attach(mediaListEventManager, event.intValue(), callback, null);
                Logger.debug("result={}", result);
            }
        }
    }

    /**
     * De-register the call-back used to receive native media player events.
     */
    private void deregisterEventListener() {
        Logger.debug("deregisterEventListener()");
        if(callback != null) {
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaListItemAdded.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaListWillDeleteItem.intValue()) {
                    Logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaListEventManager, event.intValue(), callback, null);
                }
            }
            callback = null;
        }
    }

    /**
     * Raise an event.
     *
     * @param mediaListEvent event to raise, may be <code>null</code>
     */
    private void raiseEvent(MediaListEvent mediaListEvent) {
        Logger.trace("raiseEvent(mediaListEvent={}", mediaListEvent);
        if(mediaListEvent != null) {
            listenersService.submit(new NotifyEventListenersRunnable(mediaListEvent));
        }
    }

    /**
     * Acquire the media list lock.
     */
    private void lock() {
        Logger.debug("lock()");
        libvlc.libvlc_media_list_lock(mediaListInstance);
    }

    /**
     * Release the media list lock.
     */
    private void unlock() {
        Logger.debug("unlock()");
        libvlc.libvlc_media_list_unlock(mediaListInstance);
    }

    /**
     * Create a new native media instance.
     *
     * @param media media resource locator
     * @param mediaOptions zero or more media options
     * @return native media instance
     * @throws IllegalArgumentException if the supplied MRL could not be parsed
     */
    private libvlc_media_t newMediaDescriptor(String media, String... mediaOptions) {
        Logger.debug("newMediaDescriptor(media={},mediaOptions={})", media, Arrays.toString(mediaOptions));
        libvlc_media_t mediaDescriptor;
        if(MediaResourceLocator.isLocation(media)) {
            Logger.debug("Treating mrl as a location");
            mediaDescriptor = libvlc.libvlc_media_new_location(instance, media);
        }
        else {
            Logger.debug("Treating mrl as a path");
            mediaDescriptor = libvlc.libvlc_media_new_path(instance, media);
        }
        Logger.debug("mediaDescriptor={}", mediaDescriptor);
        if(standardMediaOptions != null) {
            for(String standardMediaOption : standardMediaOptions) {
                Logger.debug("standardMediaOption={}", standardMediaOption);
                libvlc.libvlc_media_add_option(mediaDescriptor, standardMediaOption);
            }
        }
        if(mediaOptions != null) {
            for(String mediaOption : mediaOptions) {
                Logger.debug("mediaOption={}", mediaOption);
                libvlc.libvlc_media_add_option(mediaDescriptor, mediaOption);
            }
        }
        return mediaDescriptor;
    }

    /**
     * Release a native media instance.
     *
     * @param mediaDescripor native media instance
     */
    private void releaseMediaDescriptor(libvlc_media_t mediaDescriptor) {
        Logger.debug("releaseMediaDescriptor(mediaDescriptor={})", mediaDescriptor);
        libvlc.libvlc_media_release(mediaDescriptor);
    }

    /**
     * Get the native media list instance handle.
     *
     * @return native media list handle
     */
    public final libvlc_media_list_t mediaListInstance() {
        return mediaListInstance;
    }

    /**
     * A call-back to handle events from the native media list.
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
    private final class MediaListCallback implements libvlc_callback_t {
        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            Logger.trace("callback(event={},userData={})", event, userData);
            if(!eventListenerList.isEmpty()) {
                // Create a new media player event for the native event
                raiseEvent(eventFactory.createEvent(event));
            }
        }
    }

    /**
     * A runnable task used to fire event notifications.
     * <p>
     * Care must be taken not to re-enter the native library during an event notification so the
     * notifications are off-loaded to a separate thread.
     * <p>
     * These events therefore do <em>not</em> run on the Event Dispatch Thread.
     */
    private final class NotifyEventListenersRunnable implements Runnable {

        /**
         * Event to notify.
         */
        private final MediaListEvent mediaListEvent;

        /**
         * Create a runnable.
         *
         * @param mediaPlayerEvent event to notify
         */
        private NotifyEventListenersRunnable(MediaListEvent mediaListEvent) {
            this.mediaListEvent = mediaListEvent;
        }

        @Override
        public void run() {
            Logger.trace("run()");
            for(int i = eventListenerList.size() - 1; i >= 0; i -- ) {
                MediaListEventListener listener = eventListenerList.get(i);
                try {
                    mediaListEvent.notify(listener);
                }
                catch(Exception e) {
                    Logger.warn("Event listener {} threw an exception", e, listener);
                    // Continue with the next listener...
                }
            }
            Logger.trace("runnable exits");
        }
    }
}
