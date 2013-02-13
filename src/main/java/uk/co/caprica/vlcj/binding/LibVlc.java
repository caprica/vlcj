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

package uk.co.caprica.vlcj.binding;

import uk.co.caprica.vlcj.Info;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_cleanup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_drain_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_flush_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_pause_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_play_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_resume_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_set_volume_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_setup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_display_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_lock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_log_subscriber_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_discoverer_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_module_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_unlock_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_cleanup_cb;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_format_cb;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * JNA interface to the libvlc native library.
 * <p>
 * This is <strong>not a complete</strong> interface to libvlc, although most functions are present.
 * <p>
 * This interface specifies the exposed methods only, the types and structures are all factored out
 * separately in the "internal" sub-package.
 * <p>
 * This code and that in the internal sub-package is structured out of necessity to interoperate
 * with the libvlc native library. This code was originally derived (but has now been completely
 * re-written) from the original JVLC source code, the copyright of which belongs to the VideoLAN
 * team, which was distributed under GPL version 2 or later.
 * <p>
 * This binding is for version 1.1 and later of vlc. Earlier versions of vlc are radically different
 * and will not work with these bindings.
 * <p>
 * Some functions are only available <em>after</em> version 1.1.0 of libvlc.
 * <p>
 * Some functions are only available <em>after</em> version 2.0.0 of libvlc.
 * <p>
 * This system property may be useful for debugging:
 * <pre>
 * -Djna.dump_memory=true
 * </pre>
 * In the native header file, generally "char*" types must be freed, but "const char*" need (must)
 * not.
 */
public interface LibVlc extends Library {

    /**
     * Application information.
     */
    Info INFO = Info.getInstance();

    /**
     * Native library instance.
     */
    LibVlc INSTANCE = (LibVlc)Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

    /**
     * Synchronised native library instance.
     */
    LibVlc SYNC_INSTANCE = (LibVlc)Native.synchronizedLibrary(INSTANCE);

    // === libvlc.h =============================================================

    /**
     * A human-readable error message for the last LibVLC error in the calling thread. The resulting
     * string is valid until another error occurs (at least until the next LibVLC call).
     * <p>
     * This will be NULL if there was no error.
     */
    String libvlc_errmsg();

    /**
     * Clears the LibVLC error status for the current thread. This is optional. By default, the
     * error status is automatically overridden when a new error occurs, and destroyed when the
     * thread exits.
     */
    void libvlc_clearerr();

    /**
     * Create and initialize a libvlc instance.
     *
     * @param argc the number of arguments
     * @param argv command-line-type arguments
     * @return the libvlc instance or NULL in case of error
     */
    libvlc_instance_t libvlc_new(int argc, String[] argv);

    /**
     * Create and initialize a libvlc instance.
     *
     * @param argc the number of arguments
     * @param argv command-line-type arguments
     * @param builtins a NULL terminated array of \see vlc_plugin.
     * @return the libvlc instance or NULL in case of error
     *
     * <pre>
     * {
     *     vlc_declare_plugin(mp4);
     *     vlc_declare_plugin(dummy);
     *     const void **builtins = { vlc_plugin(mp4), vlc_plugin(dummy), NULL };
     *     libvlc_instance_t *vlc = libvlc_new_with_builtins(argc, argv, builtins);
     * }
     * </pre>
     */
    libvlc_instance_t libvlc_new_with_builtins(int argc, String[] argv, Pointer[] builtins);

    /**
     * Decrement the reference count of a libvlc instance, and destroy it if it reaches zero.
     *
     * @param p_instance the instance to destroy
     */
    void libvlc_release(libvlc_instance_t p_instance);

    /**
     * Increments the reference count of a libvlc instance. The initial reference count is 1 after
     * libvlc_new() returns.
     *
     * @param p_instance the instance to reference
     */
    void libvlc_retain(libvlc_instance_t p_instance);

    /**
     * Try to start a user interface for the libvlc instance.
     *
     * @param p_instance the instance
     * @param name interface name, or NULL for default
     * @return 0 on success, -1 on error.
     */
    int libvlc_add_intf(libvlc_instance_t p_instance, String name);

    /**
     * Waits until an interface causes the instance to exit. You should start at least one interface
     * first, using libvlc_add_intf().
     *
     * @param p_instance the instance
     */
    void libvlc_wait(libvlc_instance_t p_instance);

    /**
     * Sets the application name. LibVLC passes this as the user agent string when a protocol
     * requires it.
     *
     * @param p_instance LibVLC instance
     * @param name human-readable application name, e.g. "FooBar player 1.2.3"
     * @param http HTTP User Agent, e.g. "FooBar/1.2.3 Python/2.6.0"
     * @since LibVLC 1.1.1
     */
    void libvlc_set_user_agent(libvlc_instance_t p_instance, String name, String http);

    /**
     * Retrieve libvlc version. Example: "1.1.0-git The Luggage"
     *
     * @return a string containing the libvlc version
     */
    String libvlc_get_version();

    /**
     * Retrieve libvlc compiler version. Example: "gcc version 4.2.3 (Ubuntu 4.2.3-2ubuntu6)"
     *
     * @return a string containing the libvlc compiler version
     */
    String libvlc_get_compiler();

    /**
     * Retrieve libvlc changeset. Example: "aa9bce0bc4"
     *
     * @return a string containing the libvlc changeset
     */
    String libvlc_get_changeset();

    /**
     * Frees an heap allocation returned by a LibVLC function. If you know you're using the same
     * underlying C run-time as the LibVLC implementation, then you can call ANSI C free() directly
     * instead.
     *
     * @param ptr the pointer
     */
    void libvlc_free(Pointer ptr);

    /**
     * Register for an event notification.
     *
     * @param p_event_manager the event manager to which you want to attach to. Generally it is
     *            obtained by vlc_my_object_event_manager() where my_object is the object you want
     *            to listen to.
     * @param i_event_type the desired event to which we want to listen
     * @param f_callback the function to call when i_event_type occurs
     * @param user_data user provided data to carry with the event
     * @return 0 on success, ENOMEM on error
     */
    int libvlc_event_attach(libvlc_event_manager_t p_event_manager, int i_event_type, libvlc_callback_t f_callback, Pointer user_data);

    /**
     * Unregister an event notification.
     *
     * @param p_event_manager the event manager
     * @param i_event_type the desired event to which we want to unregister
     * @param f_callback the function to call when i_event_type occurs
     * @param p_user_data user provided data to carry with the event
     */
    void libvlc_event_detach(libvlc_event_manager_t p_event_manager, int i_event_type, libvlc_callback_t f_callback, Pointer p_user_data);

    /**
     * Get an event's type name.
     *
     * @param event_type the desired event
     */
    String libvlc_event_type_name(int event_type);

    /**
     * Registers a logging callback to LibVLC.
     * <p>
     * This function is thread-safe.
     * <p>
     * Some log messages (especially debug) are emitted by LibVLC while initializing,
     * before any LibVLC instance even exists. Thus this function does not require a
     * LibVLC instance parameter.
     * <p>
     * <strong>Warning: as a consequence of not depending on a LibVLC instance, all
     * logging callbacks are shared by all LibVLC instances within the process/address
     * space. This also enables log messages to be emitted by LibVLC components that
     * are not specific to any given LibVLC instance.</strong>
     * <p>
     * <strong>Do not call this function from within a logging callback. It
     * would trigger a dead lock.</strong>
     *
     * @param sub uninitialized subscriber structure
     * @param cb callback function pointer
     * @param data opaque data pointer for the callback function
     *
     * @since LibVLC 2.1.0 or later
     */
    void libvlc_log_subscribe(libvlc_log_subscriber_t sub, libvlc_log_cb cb, Pointer data);

    /**
     * Deregisters a logging callback from LibVLC.
     * <p>
     * This function is thread-safe.
     * <p>
     * Note: after (and only after) libvlc_log_unsubscribe() has returned, LibVLC
     * warrants that there are no more pending calls of the subscription callback
     * function.
     * <p>
     * <strong>Warning: do not call this function from within a logging callback. It
     * would trigger a dead lock.</strong>
     *
     * @param sub initialized subscriber structure
     * @since LibVLC 2.1.0 or later
     */
    void libvlc_log_unsubscribe(libvlc_log_subscriber_t sub);

    /**
     * Release a list of module descriptions.
     *
     * @param p_list the list to be released
     */
    void libvlc_module_description_list_release(libvlc_module_description_t p_list);

    /**
     * Returns a list of audio filters that are available.
     *
     * @param p_instance libvlc instance
     * @return a list of module descriptions. It should be freed with
     *         libvlc_module_description_list_release(). In case of an error, NULL is returned.
     * @see libvlc_module_description_t
     * @see #libvlc_module_description_list_release(libvlc_module_description_t)
     */
    libvlc_module_description_t libvlc_audio_filter_list_get(libvlc_instance_t p_instance);

    /**
     * Returns a list of video filters that are available.
     *
     * @param p_instance libvlc instance
     * @return a list of module descriptions. It should be freed with
     *         libvlc_module_description_list_release(). In case of an error, NULL is returned.
     * @see libvlc_module_description_t
     * @see #libvlc_module_description_list_release(libvlc_module_description_t)
     */
    libvlc_module_description_t libvlc_video_filter_list_get(libvlc_instance_t p_instance);

    /**
     * Return the current time as defined by LibVLC. The unit is the microsecond. Time increases
     * monotonically (regardless of time zone changes and RTC adjustments). The origin is arbitrary
     * but consistent across the whole system (e.g. the system uptime, the time since the system was
     * booted). \note On systems that support it, the POSIX monotonic clock is used.
     */
    long libvlc_clock();

    // === libvlc.h =============================================================

    // === libvlc_media.h =======================================================

    /**
     * Create a media with a certain given media resource location.
     *
     * @see #libvlc_media_release(libvlc_media_t)
     * @param p_instance the instance
     * @param psz_mrl the MRL to read
     * @return the newly created media or NULL on error
     */
    libvlc_media_t libvlc_media_new_location(libvlc_instance_t p_instance, String psz_mrl);

    /**
     * Create a media with a certain file path.
     *
     * @see #libvlc_media_release(libvlc_media_t)
     * @param p_instance the instance
     * @param path local filesystem path
     * @return the newly created media or NULL on error
     */
    libvlc_media_t libvlc_media_new_path(libvlc_instance_t p_instance, String path);

    /**
     * Create a media as an empty node with a given name.
     *
     * @see #libvlc_media_release(libvlc_media_t)
     * @param p_instance the instance
     * @param psz_name the name of the node
     * @return the new empty media or NULL on error
     */
    libvlc_media_t libvlc_media_new_as_node(libvlc_instance_t p_instance, String psz_name);

    /**
     * Add an option to the media. This option will be used to determine how the media_player will
     * read the media. This allows to use VLC's advanced reading/streaming options on a per-media
     * basis. The options are detailed in vlc --long-help, for instance "--sout-all"
     *
     * @param p_md the media descriptor
     * @param ppsz_options the options (as a string)
     */
    void libvlc_media_add_option(libvlc_media_t p_md, String ppsz_options);

    /**
     * Add an option to the media with configurable flags. This option will be used to determine how
     * the media_player will read the media. This allows to use VLC's advanced reading/streaming
     * options on a per-media basis. The options are detailed in vlc --long-help, for instance
     * "--sout-all"
     *
     * @param p_md the media descriptor
     * @param ppsz_options the options (as a string)
     * @param i_flags the flags for this option
     */
    void libvlc_media_add_option_flag(libvlc_media_t p_md, String ppsz_options, int i_flags);

    /**
     * Retain a reference to a media descriptor object (libvlc_media_t). Use libvlc_media_release()
     * to decrement the reference count of a media descriptor object.
     *
     * @param p_md the media descriptor
     */
    void libvlc_media_retain(libvlc_media_t p_md);

    /**
     * Decrement the reference count of a media descriptor object. If the reference count is 0, then
     * libvlc_media_release() will release the media descriptor object. It will send out an
     * libvlc_MediaFreed event to all listeners. If the media descriptor object has been released it
     * should not be used again.
     *
     * @param p_md the media descriptor
     */
    void libvlc_media_release(libvlc_media_t p_md);

    /**
     * Get the media resource locator (mrl) from a media descriptor object
     *
     * @param p_md a media descriptor object
     * @return string with mrl of media descriptor object
     */
    Pointer libvlc_media_get_mrl(libvlc_media_t p_md);

    /**
     * Duplicate a media descriptor object.
     *
     * @param p_md a media descriptor object.
     */
    libvlc_media_t libvlc_media_duplicate(libvlc_media_t p_md);

    /**
     * Read the meta of the media. If the media has not yet been parsed this will return NULL. This
     * methods automatically calls libvlc_media_parse_async(), so after calling it you may receive a
     * libvlc_MediaMetaChanged event. If you prefer a synchronous version ensure that you call
     * libvlc_media_parse() before get_meta().
     *
     * @see #libvlc_media_parse(libvlc_media_t)
     * @see #libvlc_media_parse_async(libvlc_media_t)
     * @see libvlc_event_e#libvlc_MediaMetaChanged
     * @param p_md the media descriptor
     * @param e_meta the meta to read
     * @return the media's meta
     */
    Pointer libvlc_media_get_meta(libvlc_media_t p_md, int e_meta);

    /**
     * Set the meta of the media (this function will not save the meta, call libvlc_media_save_meta
     * in order to save the meta)
     *
     * @param p_md the media descriptor
     * @param e_meta the meta to write
     * @param psz_value the media's meta
     */
    void libvlc_media_set_meta(libvlc_media_t p_md, int e_meta, String psz_value);

    /**
     * Save the meta previously set
     *
     * @param p_md the media desriptor
     * @return true if the write operation was successfull
     */
    int libvlc_media_save_meta(libvlc_media_t p_md);

    /**
     * Get current state of media descriptor object. Possible media states are defined in
     * libvlc_structures.c (libvlc_NothingSpecial=0, libvlc_Opening, libvlc_Buffering,
     * libvlc_Playing, libvlc_Paused, libvlc_Stopped, libvlc_Ended, libvlc_Error).
     *
     * @see libvlc_state_t
     * @param p_meta_desc a media descriptor object
     * @return state of media descriptor object
     */
    int libvlc_media_get_state(libvlc_media_t p_meta_desc);

    /**
     * get the current statistics about the media
     *
     * @param p_md media descriptor object
     * @param p_stats structure that contain the statistics about the media (this structure must be
     *            allocated by the caller)
     * @return true if the statistics are available, false otherwise
     */
    int libvlc_media_get_stats(libvlc_media_t p_md, libvlc_media_stats_t p_stats);

    /**
     * Get subitems of media descriptor object. This will increment the reference count of supplied
     * media descriptor object. Use libvlc_media_list_release() to decrement the reference counting.
     *
     * @param p_md media descriptor object
     * @return list of media descriptor subitems or NULL This method uses libvlc_media_list_t,
     *         however, media_list usage is optional and this is here for convenience
     */
    libvlc_media_list_t libvlc_media_subitems(libvlc_media_t p_md);

    /**
     * Get event manager from media descriptor object. NOTE: this function doesn't increment
     * reference counting.
     *
     * @param p_md a media descriptor object
     * @return event manager object
     */
    libvlc_event_manager_t libvlc_media_event_manager(libvlc_media_t p_md);

    /**
     * Get duration (in ms) of media descriptor object item.
     *
     * @param p_md media descriptor object
     * @return duration of media item or -1 on error
     */
    long libvlc_media_get_duration(libvlc_media_t p_md);

    /**
     * Parse a media. This fetches (local) meta data and tracks information. The method is
     * synchronous.
     *
     * @see #libvlc_media_parse_async(libvlc_media_t)
     * @see #libvlc_media_get_meta(libvlc_media_t, int)
     * @see #libvlc_media_get_tracks_info(libvlc_media_t, PointerByReference)
     * @param media media descriptor object
     */
    void libvlc_media_parse(libvlc_media_t media);

    /**
     * Parse a media. This fetches (local) meta data and tracks information. The method is the
     * asynchronous of libvlc_media_parse(). To track when this is over you can listen to
     * libvlc_MediaParsedChanged event. However if the media was already parsed you will not receive
     * this event.
     *
     * @see #libvlc_media_parse(libvlc_media_t)
     * @see libvlc_event_e#libvlc_MediaParsedChanged
     * @see #libvlc_media_get_meta(libvlc_media_t, int)
     * @see #libvlc_media_get_tracks_info(libvlc_media_t, PointerByReference)
     * @param media media descriptor object
     */
    void libvlc_media_parse_async(libvlc_media_t media);

    /**
     * Get Parsed status for media descriptor object.
     *
     * @see libvlc_event_e#libvlc_MediaParsedChanged
     * @param p_md media descriptor object
     * @return true if media object has been parsed otherwise it returns false
     */
    int libvlc_media_is_parsed(libvlc_media_t p_md);

    /**
     * Sets media descriptor's user_data. user_data is specialized data accessed by the host
     * application, VLC.framework uses it as a pointer to an native object that references a
     * libvlc_media_t pointer
     *
     * @param p_md media descriptor object
     * @param p_new_user_data pointer to user data
     */
    void libvlc_media_set_user_data(libvlc_media_t p_md, Pointer p_new_user_data);

    /**
     * Get media descriptor's user_data. user_data is specialized data accessed by the host
     * application, VLC.framework uses it as a pointer to an native object that references a
     * libvlc_media_t pointer
     *
     * @param p_md media descriptor object
     */
    Pointer libvlc_media_get_user_data(libvlc_media_t p_md);

    /**
     * Get media descriptor's elementary streams description.
     * <p>
     * Note, you need to call {@link #libvlc_media_parse(libvlc_media_t)} or play the media at least once before calling
     * this function.
     * <p>
     * Not doing this will result in an empty array.
     *
     * @deprecated Use libvlc_media_tracks_get instead.
     *
     * @param p_md media descriptor object
     * @param tracks address to store an allocated array of Elementary Streams descriptions (must be
     *            freed by the caller)
     * @return the number of Elementary Streams
     */
    @Deprecated
    int libvlc_media_get_tracks_info(libvlc_media_t p_md, PointerByReference tracks);

    /**
     * Get media descriptor's elementary streams description
     * <p>
     * Note, you need to call {@link #libvlc_media_parse(libvlc_media_t)} or play the media at least once
     * before calling this function.
     * <p>
     * Not doing this will result in an empty array.
     *
     * @since LibVLC 2.1.0 and later.
     *
     * @param p_md media descriptor object
     * @param tracks address to store an allocated array of Elementary Streams
     *        descriptions (must be freed with libvlc_media_tracks_release
     *        by the caller) [OUT]
     *
     * @return the number of Elementary Streams (zero on error)
     */
    int libvlc_media_tracks_get(libvlc_media_t p_md, PointerByReference tracks);

    /**
     * Release media descriptor's elementary streams description array
     *
     * @since LibVLC 2.1.0 and later.
     *
     * @param p_tracks tracks info array to release
     * @param i_count number of elements in the array
     */
    void libvlc_media_tracks_release(Pointer p_tracks, int i_count);

    // === libvlc_media.h =======================================================

    // === libvlc_media_player.h ================================================

    /**
     * Create an empty Media Player object
     *
     * @param p_libvlc_instance the libvlc instance in which the Media Player should be created.
     * @return a new media player object, or NULL on error.
     */
    libvlc_media_player_t libvlc_media_player_new(libvlc_instance_t p_libvlc_instance);

    /**
     * Create a Media Player object from a Media
     *
     * @param p_md the media. Afterwards the p_md can be safely destroyed.
     * @return a new media player object, or NULL on error.
     */
    libvlc_media_player_t libvlc_media_player_new_from_media(libvlc_media_t p_md);

    /**
     * Release a media_player after use Decrement the reference count of a media player object. If
     * the reference count is 0, then libvlc_media_player_release() will release the media player
     * object. If the media player object has been released, then it should not be used again.
     *
     * @param p_mi the Media Player to free
     */
    void libvlc_media_player_release(libvlc_media_player_t p_mi);

    /**
     * Retain a reference to a media player object. Use libvlc_media_player_release() to decrement
     * reference count.
     *
     * @param p_mi media player object
     */
    void libvlc_media_player_retain(libvlc_media_player_t p_mi);

    /**
     * Set the media that will be used by the media_player. If any, previous md will be released.
     *
     * @param p_mi the Media Player
     * @param p_md the Media. Afterwards the p_md can be safely destroyed.
     */
    void libvlc_media_player_set_media(libvlc_media_player_t p_mi, libvlc_media_t p_md);

    /**
     * Get the media used by the media_player.
     * <p>
     * You do <strong>not</strong> need to invoke libvlc_media_player_release().
     *
     * @param p_mi the Media Player
     * @return the media associated with p_mi, or NULL if no media is associated
     */
    libvlc_media_t libvlc_media_player_get_media(libvlc_media_player_t p_mi);

    /**
     * Get the Event Manager from which the media player send event.
     *
     * @param p_mi the Media Player
     * @return the event manager associated with p_mi
     */
    libvlc_event_manager_t libvlc_media_player_event_manager(libvlc_media_player_t p_mi);

    /**
     * is_playing
     *
     * @param p_mi the Media Player
     * @return 1 if the media player is playing, 0 otherwise
     */
    int libvlc_media_player_is_playing(libvlc_media_player_t p_mi);

    /**
     * Play
     *
     * @param p_mi the Media Player
     * @return 0 if playback started (and was already started), or -1 on error.
     */
    int libvlc_media_player_play(libvlc_media_player_t p_mi);

    /**
     * Pause or resume (no effect if there is no media)
     *
     * @param mp the Media Player
     * @param do_pause play/resume if zero, pause if non-zero
     * @since LibVLC 1.1.1
     */
    void libvlc_media_player_set_pause(libvlc_media_player_t mp, int do_pause);

    /**
     * Toggle pause (no effect if there is no media)
     *
     * @param p_mi the Media Player
     */
    void libvlc_media_player_pause(libvlc_media_player_t p_mi);

    /**
     * Stop (no effect if there is no media)
     *
     * @param p_mi the Media Player
     */
    void libvlc_media_player_stop(libvlc_media_player_t p_mi);

    /**
     * Set callbacks and private data to render decoded video to a custom area in memory.
     * <p>
     * Use libvlc_video_set_format() or libvlc_video_set_format_callbacks() to configure the decoded
     * format.
     *
     * @param mp the media player
     * @param lock callback to allocate video memory
     * @param unlock callback to release video memory
     * @param display callback when ready to display a video frame
     * @param opaque private pointer for the three callbacks (as first parameter)
     * @since LibVLC 1.1.1
     */
    void libvlc_video_set_callbacks(libvlc_media_player_t mp, libvlc_lock_callback_t lock, libvlc_unlock_callback_t unlock, libvlc_display_callback_t display, Pointer opaque);

    /**
     * Set decoded video chroma and dimensions.
     * <p>
     * This only works in combination with libvlc_video_set_callbacks(), and is mutually exclusive
     * with libvlc_video_set_format_callbacks().
     *
     * @param mp the media player
     * @param chroma a four-characters string identifying the chroma (e.g. "RV32" or "YUYV")
     * @param width pixel width
     * @param height pixel height
     * @param pitch line pitch (in bytes)
     * @since LibVLC 1.1.1
     *
     * bug: All pixel planes are expected to have the same pitch. To use the YCbCr color space with
     *      chrominance subsampling, consider using libvlc_video_set_format_callback() instead.
     */
    void libvlc_video_set_format(libvlc_media_player_t mp, String chroma, int width, int height, int pitch);

    /**
     * Set decoded video chroma and dimensions. This only works in combination with
     * libvlc_video_set_callbacks().
     *
     * @param mp the media player
     * @param setup callback to select the video format (cannot be NULL)
     * @param cleanup callback to release any allocated resources (or NULL)
     * @since LibVLC 2.0.0 or later
     */
    void libvlc_video_set_format_callbacks(libvlc_media_player_t mp, libvlc_video_format_cb setup, libvlc_video_cleanup_cb cleanup);

    /**
     * Set the NSView handler where the media player should render its video output. Use the vout
     * called "macosx". The drawable is an NSObject that follow the VLCOpenGLVideoViewEmbedding
     * protocol:
     * <pre>
     *     \@protocol VLCOpenGLVideoViewEmbedding <NSObject> - (void)addVoutSubview:(NSView*)view; - (void)removeVoutSubview:(NSView *)view; \@end
     * </pre>
     * Or it can be an NSView object. If you want to use it along with Qt4 see the
     * QMacCocoaViewContainer. Then the following code should work:
     * <pre>
     * {
     *     NSView *video = [[NSView alloc] init];
     *     QMacCocoaViewContainer *container = new QMacCocoaViewContainer(video, parent);
     *     libvlc_media_player_set_nsobject(mp, video);
     *     [video release];
     * }
     * </pre>
     * You can find a live example in VLCVideoView in VLCKit.framework.
     *
     * @param p_mi the Media Player
     * @param drawable the drawable that is either an NSView or an object following the
     *            VLCOpenGLVideoViewEmbedding protocol.
     */
    void libvlc_media_player_set_nsobject(libvlc_media_player_t p_mi, long drawable);

    /**
     * Get the NSView handler previously set with libvlc_media_player_set_nsobject().
     *
     * @param p_mi the Media Player
     * @return the NSView handler or 0 if none where set
     */
    Pointer libvlc_media_player_get_nsobject(libvlc_media_player_t p_mi);

    /**
     * Set the agl handler where the media player should render its video output.
     *
     * @param p_mi the Media Player
     * @param drawable the agl handler
     */
    void libvlc_media_player_set_agl(libvlc_media_player_t p_mi, int drawable);

    /**
     * Get the agl handler previously set with libvlc_media_player_set_agl().
     *
     * @param p_mi the Media Player
     * @return the agl handler or 0 if none where set
     */
    int libvlc_media_player_get_agl(libvlc_media_player_t p_mi);

    /**
     * Set an X Window System drawable where the media player should render its video output. If
     * LibVLC was built without X11 output support, then this has no effects. The specified
     * identifier must correspond to an existing Input/Output class X11 window. Pixmaps are
     * <b>not</b> supported. The caller shall ensure that the X11 server is the same as the one the
     * VLC instance has been configured with.
     *
     * @param p_mi the Media Player
     * @param drawable the ID of the X window
     */
    void libvlc_media_player_set_xwindow(libvlc_media_player_t p_mi, int drawable);

    /**
     * Get the X Window System window identifier previously set with
     * libvlc_media_player_set_xwindow(). Note that this will return the identifier even if VLC is
     * not currently using it (for instance if it is playing an audio-only input).
     *
     * @param p_mi the Media Player
     * @return an X window ID, or 0 if none where set.
     */
    int libvlc_media_player_get_xwindow(libvlc_media_player_t p_mi);

    /**
     * Set a Win32/Win64 API window handle (HWND) where the media player should render its video
     * output. If LibVLC was built without Win32/Win64 API output support, then this has no effects.
     *
     * @param p_mi the Media Player
     * @param drawable windows handle of the drawable
     */
    void libvlc_media_player_set_hwnd(libvlc_media_player_t p_mi, Pointer drawable);

    /**
     * Get the Windows API window handle (HWND) previously set with libvlc_media_player_set_hwnd().
     * The handle will be returned even if LibVLC is not currently outputting any video to it.
     *
     * @param p_mi the Media Player
     * @return a window handle or NULL if there are none.
     */
    Pointer libvlc_media_player_get_hwnd(libvlc_media_player_t p_mi);

    /**
     * Set callbacks and private data for decoded audio.
     * <p>
     * Use libvlc_audio_set_format() or libvlc_audio_set_format_callbacks() to configure the decoded
     * audio format.
     *
     * @param mp the media player
     * @param play callback to play audio samples (must not be NULL)
     * @param pause callback to pause playback (or NULL to ignore)
     * @param resume callback to resume playback (or NULL to ignore)
     * @param flush callback to flush audio buffers (or NULL to ignore)
     * @param drain callback to drain audio buffers (or NULL to ignore)
     * @param opaque private pointer for the audio callbacks (as first parameter)
     * @since LibVLC 2.0.0 or later
     */
    void libvlc_audio_set_callbacks(libvlc_media_player_t mp, libvlc_audio_play_cb play, libvlc_audio_pause_cb pause, libvlc_audio_resume_cb resume, libvlc_audio_flush_cb flush, libvlc_audio_drain_cb drain, Pointer opaque);

    /**
     * Set callbacks and private data for decoded audio. Use libvlc_audio_set_format() or
     * libvlc_audio_set_format_callbacks() to configure the decoded audio format.
     *
     * @param mp the media player
     * @param set_volume callback to apply audio volume, or NULL to apply volume in software
     * @since LibVLC 2.0.0 or later
     */
    void libvlc_audio_set_volume_callback(libvlc_media_player_t mp, libvlc_audio_set_volume_cb set_volume);

    /**
     * Set decoded audio format. This only works in combination with libvlc_audio_set_callbacks().
     *
     * @param mp the media player
     * @param setup callback to select the audio format (cannot be NULL)
     * @param cleanup callback to release any allocated resources (or NULL)
     * @since LibVLC 2.0.0 or later
     */
    void libvlc_audio_set_format_callbacks(libvlc_media_player_t mp, libvlc_audio_setup_cb setup, libvlc_audio_cleanup_cb cleanup);

    /**
     * Set decoded audio format. This only works in combination with libvlc_audio_set_callbacks(),
     * and is mutually exclusive with libvlc_audio_set_format_callbacks().
     *
     * @param mp the media player
     * @param format a four-characters string identifying the sample format (e.g. "S16N" or "FL32")
     * @param rate sample rate (expressed in Hz)
     * @param channels channels count
     * @since LibVLC 2.0.0 or later
     */
    void libvlc_audio_set_format(libvlc_media_player_t mp, String format, int rate, int channels);

    /** \bug This might go away ... to be replaced by a broader system */

    /**
     * Get the current movie length (in ms).
     *
     * @param p_mi the Media Player
     * @return the movie length (in ms), or -1 if there is no media.
     */
    long libvlc_media_player_get_length(libvlc_media_player_t p_mi);

    /**
     * Get the current movie time (in ms).
     *
     * @param p_mi the Media Player
     * @return the movie time (in ms), or -1 if there is no media.
     */
    long libvlc_media_player_get_time(libvlc_media_player_t p_mi);

    /**
     * Set the movie time (in ms). This has no effect if no media is being played. Not all formats
     * and protocols support this.
     *
     * @param p_mi the Media Player
     * @param i_time the movie time (in ms).
     */
    void libvlc_media_player_set_time(libvlc_media_player_t p_mi, long i_time);

    /**
     * Get movie position.
     *
     * @param p_mi the Media Player
     * @return movie position, or -1. in case of error
     */
    float libvlc_media_player_get_position(libvlc_media_player_t p_mi);

    /**
     * Set movie position. This has no effect if playback is not enabled. This might not work
     * depending on the underlying input format and protocol.
     *
     * @param p_mi the Media Player
     * @param f_pos the position
     */
    void libvlc_media_player_set_position(libvlc_media_player_t p_mi, float f_pos);

    /**
     * Set movie chapter (if applicable).
     *
     * @param p_mi the Media Player
     * @param i_chapter chapter number to play
     */
    void libvlc_media_player_set_chapter(libvlc_media_player_t p_mi, int i_chapter);

    /**
     * Get movie chapter.
     *
     * @param p_mi the Media Player
     * @return chapter number currently playing, or -1 if there is no media.
     */
    int libvlc_media_player_get_chapter(libvlc_media_player_t p_mi);

    /**
     * Get movie chapter count
     *
     * @param p_mi the Media Player
     * @return number of chapters in movie, or -1.
     */
    int libvlc_media_player_get_chapter_count(libvlc_media_player_t p_mi);

    /**
     * Is the player able to play
     *
     * @param p_mi the Media Player
     * @return boolean
     */
    int libvlc_media_player_will_play(libvlc_media_player_t p_mi);

    /**
     * Get title chapter count
     *
     * @param p_mi the Media Player
     * @param i_title title
     * @return number of chapters in title, or -1
     */
    int libvlc_media_player_get_chapter_count_for_title(libvlc_media_player_t p_mi, int i_title);

    /**
     * Set movie title
     *
     * @param p_mi the Media Player
     * @param i_title title number to play
     */
    void libvlc_media_player_set_title(libvlc_media_player_t p_mi, int i_title);

    /**
     * Get movie title
     *
     * @param p_mi the Media Player
     * @return title number currently playing, or -1
     */
    int libvlc_media_player_get_title(libvlc_media_player_t p_mi);

    /**
     * Get movie title count
     *
     * @param p_mi the Media Player
     * @return title number count, or -1
     */
    int libvlc_media_player_get_title_count(libvlc_media_player_t p_mi);

    /**
     * Set previous chapter (if applicable)
     *
     * @param p_mi the Media Player
     */
    void libvlc_media_player_previous_chapter(libvlc_media_player_t p_mi);

    /**
     * Set next chapter (if applicable)
     *
     * @param p_mi the Media Player
     */
    void libvlc_media_player_next_chapter(libvlc_media_player_t p_mi);

    /**
     * Get the requested movie play rate.
     * <p>
     * Depending on the underlying media, the requested rate may be different from the real
     *          playback rate.
     * @param p_mi the Media Player
     * @return movie play rate
     */
    float libvlc_media_player_get_rate(libvlc_media_player_t p_mi);

    /**
     * Set movie play rate
     *
     * @param p_mi the Media Player
     * @param rate movie play rate to set
     * @return -1 if an error was detected, 0 otherwise (but even then, it might not actually work
     *         depending on the underlying media protocol)
     */
    int libvlc_media_player_set_rate(libvlc_media_player_t p_mi, float rate);

    /**
     * Get current movie state
     *
     * @param p_mi the Media Player
     * @return the current state of the media player (playing, paused, ...) @see libvlc_state_t
     */
    int libvlc_media_player_get_state(libvlc_media_player_t p_mi);

    /**
     * Get movie fps rate
     *
     * @param p_mi the Media Player
     * @return frames per second (fps) for this playing movie, or 0 if unspecified
     */
    float libvlc_media_player_get_fps(libvlc_media_player_t p_mi);

    /** end bug */

    /**
     * How many video outputs does this media player have?
     *
     * @param p_mi the media player
     * @return the number of video outputs
     */
    int libvlc_media_player_has_vout(libvlc_media_player_t p_mi);

    /**
     * Is this media player seekable?
     *
     * @param p_mi the media player
     * @return true if the media player can seek
     */
    int libvlc_media_player_is_seekable(libvlc_media_player_t p_mi);

    /**
     * Can this media player be paused?
     *
     * @param p_mi the media player
     * @return true if the media player can pause
     */
    int libvlc_media_player_can_pause(libvlc_media_player_t p_mi);

    /**
     * Display the next frame (if supported)
     *
     * @param p_mi the media player
     */
    void libvlc_media_player_next_frame(libvlc_media_player_t p_mi);

    /**
     * Navigate through DVD Menu
     *
     * @param p_mi the Media Player
     * @param navigate the Navigation mode
     * @since libVLC 2.0.0
     */
    void libvlc_media_player_navigate(libvlc_media_player_t p_mi, int navigate);

    /**
     * Release (free) libvlc_track_description_t
     *
     * @param p_track_description the structure to release
     * @deprecated Use {@link #libvlc_track_description_list_release(Pointer)} instead
     */
    @Deprecated
    void libvlc_track_description_release(Pointer p_track_description);

    /**
     * Release (free) libvlc_track_description_t
     *
     * @param p_track_description the structure to release
     */
    void libvlc_track_description_list_release(Pointer p_track_description);

    /**
     * Toggle fullscreen status on non-embedded video outputs.
     * <p>
     * The same limitations applies to this function as to libvlc_set_fullscreen().
     *
     * @param p_mi the media player
     */
    void libvlc_toggle_fullscreen(libvlc_media_player_t p_mi);

    /**
     * Enable or disable fullscreen.
     * <p>
     * With most window managers, only a top-level windows can be in full-screen mode.
     * Hence, this function will not operate properly if libvlc_media_player_set_xid() was
     * used to embed the video in a non-top-level window. In that case, the embedding
     * window must be reparented to the root window <b>before</b> fullscreen mode is
     * enabled. You will want to reparent it back to its normal parent when disabling
     * fullscreen.
     *
     * @param p_mi the media player
     * @param b_fullscreen boolean for fullscreen status
     */
    void libvlc_set_fullscreen(libvlc_media_player_t p_mi, int b_fullscreen);

    /**
     * Get current fullscreen status.
     *
     * @param p_mi the media player
     * @return the fullscreen status (boolean)
     */
    int libvlc_get_fullscreen(libvlc_media_player_t p_mi);

    /**
     * Enable or disable key press events handling, according to the LibVLC hotkeys configuration.
     * By default and for historical reasons, keyboard events are handled by the LibVLC video
     * widget.
     * <p>
     * On X11, there can be only one subscriber for key press and mouse click events
     * per window. If your application has subscribed to those events for the X window ID of the
     * video widget, then LibVLC will not be able to handle key presses and mouse clicks in any
     * case.
     * <p>
     * This function is only implemented for X11 and Win32 at the moment.
     *
     * @param p_mi the media player
     * @param on true to handle key press events, false to ignore them.
     */
    void libvlc_video_set_key_input(libvlc_media_player_t p_mi, int on);

    /**
     * Enable or disable mouse click events handling. By default, those events are handled. This is
     * needed for DVD menus to work, as well as a few video filters such as "puzzle".
     * <p>
     * See also libvlc_video_set_key_input().
     * <p>
     * This function is only implemented for X11 and Win32 at the moment.
     *
     * @param p_mi the media player
     * @param on true to handle mouse click events, false to ignore them.
     */
    void libvlc_video_set_mouse_input(libvlc_media_player_t p_mi, int on);

    /**
     * Get the pixel dimensions of a video.
     *
     * @param p_mi media player
     * @param num number of the video (starting from, and most commonly 0)
     * @param px pointer to get the pixel width [OUT]
     * @param py pointer to get the pixel height [OUT]
     * @return 0 on success, -1 if the specified video does not exist
     */
    int libvlc_video_get_size(libvlc_media_player_t p_mi, int num, IntByReference px, IntByReference py);

    /**
     * Get current video height.
     *
     * @param p_mi the media player
     * @return the video pixel height or 0 if not applicable
     * @deprecated Use
     *             {@link #libvlc_video_get_size(libvlc_media_player_t, int, IntByReference, IntByReference)}
     *             instead
     */
    @Deprecated
    int libvlc_video_get_height(libvlc_media_player_t p_mi);

    /**
     * Get current video width.
     *
     * @param p_mi the media player
     * @return the video pixel width or 0 if not applicable
     * @deprecated Use
     *             {@link #libvlc_video_get_size(libvlc_media_player_t, int, IntByReference, IntByReference)}
     *             instead
     */
    @Deprecated
    int libvlc_video_get_width(libvlc_media_player_t p_mi);

    /**
     * Get the mouse pointer coordinates over a video. Coordinates are expressed in terms of the
     * decoded video resolution, <b>not</b> in terms of pixels on the screen/viewport (to get the
     * latter, you can query your windowing system directly). Either of the coordinates may be
     * negative or larger than the corresponding dimension of the video, if the cursor is outside
     * the rendering area.
     * <p>
     * The coordinates may be out-of-date if the pointer is not located on the video
     * rendering area. LibVLC does not track the pointer if it is outside of the video
     * widget.
     * <p>
     * LibVLC does not support multiple pointers (it does of course support multiple input
     * devices sharing the same pointer) at the moment.
     *
     * @param p_mi media player
     * @param num number of the video (starting from, and most commonly 0)
     * @param px pointer to get the abscissa [OUT]
     * @param py pointer to get the ordinate [OUT]
     * @return 0 on success, -1 if the specified video does not exist
     */
    int libvlc_video_get_cursor(libvlc_media_player_t p_mi, int num, Pointer px, Pointer py);

    /**
     * Get the current video scaling factor. See also libvlc_video_set_scale().
     *
     * @param p_mi the media player
     * @return the currently configured zoom factor, or 0. if the video is set to fit to the output
     *         window/drawable automatically.
     */
    float libvlc_video_get_scale(libvlc_media_player_t p_mi);

    /**
     * Set the video scaling factor. That is the ratio of the number of pixels on screen to the
     * number of pixels in the original decoded video in each dimension. Zero is a special value; it
     * will adjust the video to the output window/drawable (in windowed mode) or the entire screen.
     * Note that not all video outputs support scaling.
     *
     * @param p_mi the media player
     * @param f_factor the scaling factor, or zero
     */
    void libvlc_video_set_scale(libvlc_media_player_t p_mi, float f_factor);

    /**
     * Get current video aspect ratio.
     *
     * @param p_mi the media player
     * @return the video aspect ratio or NULL if unspecified (the result must be released with
     *         free()).
     */
    Pointer libvlc_video_get_aspect_ratio(libvlc_media_player_t p_mi);

    /**
     * Set new video aspect ratio.
     *
     * @param p_mi the media player
     * @param psz_aspect new video aspect-ratio or NULL to reset to default \note Invalid aspect
     *            ratios are ignored.
     */
    void libvlc_video_set_aspect_ratio(libvlc_media_player_t p_mi, String psz_aspect);

    /**
     * Get current video subtitle.
     *
     * @param p_mi the media player
     * @return the video subtitle selected, or -1 if none
     */
    int libvlc_video_get_spu(libvlc_media_player_t p_mi);

    /**
     * Get the number of available video subtitles.
     *
     * @param p_mi the media player
     * @return the number of available video subtitles
     */
    int libvlc_video_get_spu_count(libvlc_media_player_t p_mi);

    /**
     * Get the description of available video subtitles.
     *
     * @param p_mi the media player
     * @return list containing description of available video subtitles
     */
    libvlc_track_description_t libvlc_video_get_spu_description(libvlc_media_player_t p_mi);

    /**
     * Set new video subtitle.
     *
     * @param p_mi the media player
     * @param i_spu new video subtitle to select
     * @return 0 on success, -1 if out of range
     */
    int libvlc_video_set_spu(libvlc_media_player_t p_mi, int i_spu);

    /**
     * Set new video subtitle file.
     *
     * @param p_mi the media player
     * @param psz_subtitle new video subtitle file
     * @return the success status (boolean)
     */
    int libvlc_video_set_subtitle_file(libvlc_media_player_t p_mi, String psz_subtitle);

    /**
     * Get the current subtitle delay. Positive values means subtitles are being displayed later,
     * negative values earlier.
     *
     * @param p_mi media player
     * @return time (in microseconds) the display of subtitles is being delayed
     * @since LibVLC 2.0.0 or later
     */
    long libvlc_video_get_spu_delay(libvlc_media_player_t p_mi);

    /**
     * Set the subtitle delay. This affects the timing of when the subtitle will be displayed.
     * Positive values result in subtitles being displayed later, while negative values will result
     * in subtitles being displayed earlier.
     *
     * The subtitle delay will be reset to zero each time the media changes.
     *
     * @param p_mi media player
     * @param i_delay time (in microseconds) the display of subtitles should be delayed
     * @return 0 on success, -1 on error
     * @since LibVLC 2.0.0 or later
     */
    int libvlc_video_set_spu_delay(libvlc_media_player_t p_mi, long i_delay);

    /**
     * Get the description of available titles.
     *
     * @param p_mi the media player
     * @return list containing description of available titles
     */
    libvlc_track_description_t libvlc_video_get_title_description(libvlc_media_player_t p_mi);

    /**
     * Get the description of available chapters for specific title.
     *
     * @param p_mi the media player
     * @param i_title selected title
     * @return list containing description of available chapter for title i_title
     */
    libvlc_track_description_t libvlc_video_get_chapter_description(libvlc_media_player_t p_mi, int i_title);

    /**
     * Get current crop filter geometry.
     *
     * @param p_mi the media player
     * @return the crop filter geometry or NULL if unset
     */
    Pointer libvlc_video_get_crop_geometry(libvlc_media_player_t p_mi);

    /**
     * Set new crop filter geometry.
     *
     * @param p_mi the media player
     * @param psz_geometry new crop filter geometry (NULL to unset)
     */
    void libvlc_video_set_crop_geometry(libvlc_media_player_t p_mi, String psz_geometry);

    /**
     * Get current teletext page requested.
     *
     * @param p_mi the media player
     * @return the current teletext page requested.
     */
    int libvlc_video_get_teletext(libvlc_media_player_t p_mi);

    /**
     * Set new teletext page to retrieve.
     *
     * @param p_mi the media player
     * @param i_page teletex page number requested
     */
    void libvlc_video_set_teletext(libvlc_media_player_t p_mi, int i_page);

    /**
     * Toggle teletext transparent status on video output.
     *
     * @param p_mi the media player
     */
    void libvlc_toggle_teletext(libvlc_media_player_t p_mi);

    /**
     * Get number of available video tracks.
     *
     * @param p_mi media player
     * @return the number of available video tracks (int)
     */
    int libvlc_video_get_track_count(libvlc_media_player_t p_mi);

    /**
     * Get the description of available video tracks.
     *
     * @param p_mi media player
     * @return list with description of available video tracks, or NULL on error
     */
    libvlc_track_description_t libvlc_video_get_track_description(libvlc_media_player_t p_mi);

    /**
     * Get current video track.
     *
     * @param p_mi media player
     * @return the video track ID (int) or -1 if no active input
     */
    int libvlc_video_get_track(libvlc_media_player_t p_mi);

    /**
     * Set video track.
     *
     * @param p_mi media player
     * @param i_track the track ID (i_id field from track description)
     * @return 0 on success, -1 if out of range
     */
    int libvlc_video_set_track(libvlc_media_player_t p_mi, int i_track);

    /**
     * Take a snapshot of the current video window. If i_width AND i_height is 0, original size is
     * used. If i_width XOR i_height is 0, original aspect-ratio is preserved.
     *
     * @param p_mi media player instance
     * @param num number of video output (typically 0 for the first/only one)
     * @param psz_filepath the path where to save the screenshot to
     * @param i_width the snapshot's width
     * @param i_height the snapshot's height
     * @return 0 on success, -1 if the video was not found
     */
    int libvlc_video_take_snapshot(libvlc_media_player_t p_mi, int num, String psz_filepath, int i_width, int i_height);

    /**
     * Enable or disable deinterlace filter
     *
     * @param p_mi libvlc media player
     * @param psz_mode type of deinterlace filter, NULL to disable
     */
    void libvlc_video_set_deinterlace(libvlc_media_player_t p_mi, String psz_mode);

    /**
     * Get an integer marquee option value
     *
     * @param p_mi libvlc media player
     * @param option marq option to get @see libvlc_video_marquee_int_option_t
     */
    int libvlc_video_get_marquee_int(libvlc_media_player_t p_mi, int option);

    /**
     * Get a string marquee option value
     *
     * @param p_mi libvlc media player
     * @param option marq option to get @see libvlc_video_marquee_string_option_t
     */
    String libvlc_video_get_marquee_string(libvlc_media_player_t p_mi, int option);

    /**
     * Enable, disable or set an integer marquee option Setting libvlc_marquee_Enable has the side
     * effect of enabling (arg !0) or disabling (arg 0) the marq filter.
     *
     * @param p_mi libvlc media player
     * @param option marq option to set @see libvlc_video_marquee_int_option_t
     * @param i_val marq option value
     */
    void libvlc_video_set_marquee_int(libvlc_media_player_t p_mi, int option, int i_val);

    /**
     * Set a marquee string option
     *
     * @param p_mi libvlc media player
     * @param option marq option to set @see libvlc_video_marquee_string_option_t
     * @param psz_text marq option value
     */
    void libvlc_video_set_marquee_string(libvlc_media_player_t p_mi, int option, String psz_text);

    /**
     * Get integer logo option.
     *
     * @param p_mi libvlc media player instance
     * @param option logo option to get, values of libvlc_video_logo_option_t
     */
    int libvlc_video_get_logo_int(libvlc_media_player_t p_mi, int option);

    /**
     * Set logo option as integer. Options that take a different type value are ignored. Passing
     * libvlc_logo_enable as option value has the side effect of starting (arg !0) or stopping (arg
     * 0) the logo filter.
     *
     * @param p_mi libvlc media player instance
     * @param option logo option to set, values of libvlc_video_logo_option_t
     * @param value logo option value
     */
    void libvlc_video_set_logo_int(libvlc_media_player_t p_mi, int option, int value);

    /**
     * Set logo option as string. Options that take a different type value are ignored.
     *
     * @param p_mi libvlc media player instance
     * @param option logo option to set, values of libvlc_video_logo_option_t
     * @param psz_value logo option value
     */
    void libvlc_video_set_logo_string(libvlc_media_player_t p_mi, int option, String psz_value);

    /**
     * Get integer adjust option.
     *
     * @param p_mi libvlc media player instance
     * @param option adjust option to get, values of libvlc_video_adjust_option_t
     * @return value
     * @since LibVLC 1.1.1
     */
    int libvlc_video_get_adjust_int(libvlc_media_player_t p_mi, int option);

    /**
     * Set adjust option as integer. Options that take a different type value are ignored. Passing
     * libvlc_adjust_enable as option value has the side effect of starting (arg !0) or stopping
     * (arg 0) the adjust filter.
     *
     * @param p_mi libvlc media player instance
     * @param option adust option to set, values of libvlc_video_adjust_option_t
     * @param value adjust option value
     * @since LibVLC 1.1.1
     */
    void libvlc_video_set_adjust_int(libvlc_media_player_t p_mi, int option, int value);

    /**
     * Get float adjust option.
     *
     * @param p_mi libvlc media player instance
     * @param option adjust option to get, values of libvlc_video_adjust_option_t
     * @return value
     * @since LibVLC 1.1.1
     */
    float libvlc_video_get_adjust_float(libvlc_media_player_t p_mi, int option);

    /**
     * Set adjust option as float. Options that take a different type value are ignored.
     *
     * @param p_mi libvlc media player instance
     * @param option adust option to set, values of libvlc_video_adjust_option_t
     * @param value adjust option value
     * @since LibVLC 1.1.1
     */
    void libvlc_video_set_adjust_float(libvlc_media_player_t p_mi, int option, float value);

    /**
     * Gets the list of available audio outputs
     *
     * @param p_instance libvlc instance
     * @return list of available audio outputs. It must be freed it with
     *         {@link #libvlc_audio_output_list_release(libvlc_audio_output_t)}. In case of error,
     *         NULL is returned.
     */
    libvlc_audio_output_t libvlc_audio_output_list_get(libvlc_instance_t p_instance);

    /**
     * Frees the list of available audio outputs
     *
     * @param p_list list with audio outputs for release
     */
    void libvlc_audio_output_list_release(libvlc_audio_output_t p_list);

    /**
     * Sets the audio output.
     * <p>
     * Note: Any change will take be effect only after playback is stopped and
     * restarted. Audio output cannot be changed while playing.
     *
     * @param p_mi media player
     * @param psz_name name of audio output, use psz_name of @see libvlc_audio_output_t
     * @return 0 if function succeded, -1 on error
     */
    int libvlc_audio_output_set(libvlc_media_player_t p_mi, String psz_name);

    /**
     * Get count of devices for audio output, these devices are hardware oriented like analog or
     * digital output of sound card.
     *
     * @param p_instance libvlc instance
     * @param psz_audio_output - name of audio output, @see libvlc_audio_output_t
     * @return number of devices
     */
    @Deprecated
    int libvlc_audio_output_device_count(libvlc_instance_t p_instance, String psz_audio_output);

    /**
     * Get long name of device, if not available short name given
     *
     * @param p_instance libvlc instance
     * @param psz_audio_output - name of audio output, @see libvlc_audio_output_t
     * @param i_device device index
     * @return long name of device
     */
    @Deprecated
    Pointer libvlc_audio_output_device_longname(libvlc_instance_t p_instance, String psz_audio_output, int i_device);

    /**
     * Get id name of device
     *
     * @param p_instance libvlc instance
     * @param psz_audio_output - name of audio output, @see libvlc_audio_output_t
     * @param i_device device index
     * @return id name of device, use for setting device, need to be free after use
     */
    @Deprecated
    Pointer libvlc_audio_output_device_id(libvlc_instance_t p_instance, String psz_audio_output, int i_device);

    /**
     * Gets a list of audio output devices for a given audio output.
     * <p>
     * See {@link #libvlc_audio_output_device_set(libvlc_media_player_t, String, String)}.
     *
     * Not all audio outputs support this. In particular, an empty (NULL)
     * list of devices does <em>not</em> imply that the specified audio output does
     * not work.
     *
     * The list might not be exhaustive.
     *
     * Some audio output devices in the list might not actually work in some
     * circumstances. By default, it is recommended to not specify anyexplicit
     * audio device.
     *
     * @param p_instance libvlc instance
     * @param psz_aout audio output name (as returned by libvlc_audio_output_list_get())
     * @return A NULL-terminated linked list of potential audio output devices. It must be freed it with libvlc_audio_output_device_list_release()
     * @since LibVLC 2.1.0 or later.
     */
    libvlc_audio_output_device_t libvlc_audio_output_device_list_get( libvlc_instance_t p_instance, String psz_aout );

    /**
     * Frees a list of available audio output devices.
     *
     * @param p_list list with audio outputs for release
     * @since LibVLC 2.1.0 or later.
     */
    void libvlc_audio_output_device_list_release(libvlc_audio_output_device_t p_list );

    /**
     * Configures an explicit audio output device for a given audio output plugin.
     * <p>
     * A list of possible devices can be obtained with libvlc_audio_output_device_list_get().
     * <p>
     * This function does not select the specified audio output plugin. libvlc_audio_output_set()
     * is used for that purpose.
     * <p>
     * The syntax for the device parameter depends on the audio output. This is not
     * portable. Only use this function if you know what you are doing.
     * <p>
     * Some audio outputs do not support this function (e.g. PulseAudio, WASAPI).
     * <p>
     * Some audio outputs require further parameters (e.g. ALSA: channels map).
     *
     * @param p_mi media player
     * @param psz_audio_output - name of audio output, \see libvlc_audio_output_t
     * @param psz_device_id device
     */
    void libvlc_audio_output_device_set(libvlc_media_player_t p_mi, String psz_audio_output, String psz_device_id);

    /**
     * Get current audio device type. Device type describes something like character of output sound
     * - stereo sound, 2.1, 5.1 etc
     *
     * @param p_mi media player
     * @return the audio devices type @see libvlc_audio_output_device_types_t
     */
    @Deprecated
    int libvlc_audio_output_get_device_type(libvlc_media_player_t p_mi);

    /**
     * Set current audio device type.
     *
     * @param p_mi vlc instance
     * @param device_type the audio device type, according to @see
     *            libvlc_audio_output_device_types_t
     */
    @Deprecated
    void libvlc_audio_output_set_device_type(libvlc_media_player_t p_mi, int device_type);

    /**
     * Toggle mute status.
     *
     * @param p_mi media player
     */
    void libvlc_audio_toggle_mute(libvlc_media_player_t p_mi);

    /**
     * Get current mute status.
     *
     * @param p_mi media player
     * @return the mute status (boolean)
     */
    int libvlc_audio_get_mute(libvlc_media_player_t p_mi);

    /**
     * Set mute status.
     *
     * @param p_mi media player
     * @param status If status is true then mute, otherwise unmute
     */
    void libvlc_audio_set_mute(libvlc_media_player_t p_mi, int status);

    /**
     * Get current software audio volume.
     *
     * @param p_mi media player
     * @return the software volume in percents (0 = mute, 100 = nominal / 0dB)
     */
    int libvlc_audio_get_volume(libvlc_media_player_t p_mi);

    /**
     * Set current software audio volume.
     *
     * @param p_mi media player
     * @param i_volume the volume in percents (0 = mute, 100 = 0dB)
     * @return 0 if the volume was set, -1 if it was out of range
     */
    int libvlc_audio_set_volume(libvlc_media_player_t p_mi, int i_volume);

    /**
     * Get number of available audio tracks.
     *
     * @param p_mi media player
     * @return the number of available audio tracks (int), or -1 if unavailable
     */
    int libvlc_audio_get_track_count(libvlc_media_player_t p_mi);

    /**
     * Get the description of available audio tracks.
     *
     * @param p_mi media player
     * @return list with description of available audio tracks, or NULL
     */
    libvlc_track_description_t libvlc_audio_get_track_description(libvlc_media_player_t p_mi);

    /**
     * Get current audio track.
     *
     * @param p_mi media player
     * @return the audio track ID or -1 if no active input.
     */
    int libvlc_audio_get_track(libvlc_media_player_t p_mi);

    /**
     * Set current audio track.
     *
     * @param p_mi media player
     * @param i_track the track ID (i_id field from track description)
     * @return 0 on success, -1 on error
     */
    int libvlc_audio_set_track(libvlc_media_player_t p_mi, int i_track);

    /**
     * Get current audio channel.
     *
     * @param p_mi media player
     * @return the audio channel @see libvlc_audio_output_channel_t
     */
    int libvlc_audio_get_channel(libvlc_media_player_t p_mi);

    /**
     * Set current audio channel.
     *
     * @param p_mi media player
     * @param channel the audio channel, @see libvlc_audio_output_channel_t
     * @return 0 on success, -1 on error
     */
    int libvlc_audio_set_channel(libvlc_media_player_t p_mi, int channel);

    /**
     * Get current audio delay.
     *
     * @param p_mi media player
     * @return amount audio is being delayed by, in microseconds
     * @since LibVLC 1.1.1
     */
    long libvlc_audio_get_delay(libvlc_media_player_t p_mi);

    /**
     * Set current audio delay. The delay is only active for the current media item and will be
     * reset to zero each time the media changes.
     *
     * @param p_mi media player
     * @param i_delay amount to delay audio by, in microseconds
     * @return 0 on success, -1 on error
     * @since LibVLC 1.1.1
     */
    int libvlc_audio_set_delay(libvlc_media_player_t p_mi, long i_delay);

    // === libvlc_media_player.h ================================================

    // === libvlc_media_list.h ==================================================

    /**
     * Create an empty media list.
     *
     * @param p_instance libvlc instance
     * @return empty media list, or NULL on error
     */
    libvlc_media_list_t libvlc_media_list_new(libvlc_instance_t p_instance);

    /**
     * Release media list created with libvlc_media_list_new().
     *
     * @param p_ml a media list created with libvlc_media_list_new()
     */
    void libvlc_media_list_release(libvlc_media_list_t p_ml);

    /**
     * Retain reference to a media list
     *
     * @param p_ml a media list created with libvlc_media_list_new()
     */
    void libvlc_media_list_retain(libvlc_media_list_t p_ml);

    /**
     * Associate media instance with this media list instance. If another media instance was present
     * it will be released. The libvlc_media_list_lock should NOT be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     * @param p_md media instance to add
     */
    void libvlc_media_list_set_media(libvlc_media_list_t p_ml, libvlc_media_t p_md);

    /**
     * Get media instance from this media list instance. This action will increase the refcount on
     * the media instance. The libvlc_media_list_lock should NOT be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     * @return media instance
     */
    libvlc_media_t libvlc_media_list_media(libvlc_media_list_t p_ml);

    /**
     * Add media instance to media list The libvlc_media_list_lock should be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     * @param p_md a media instance
     * @return 0 on success, -1 if the media list is read-only
     */
    int libvlc_media_list_add_media(libvlc_media_list_t p_ml, libvlc_media_t p_md);

    /**
     * Insert media instance in media list on a position The libvlc_media_list_lock should be held
     * upon entering this function.
     *
     * @param p_ml a media list instance
     * @param p_md a media instance
     * @param i_pos position in array where to insert
     * @return 0 on success, -1 if the media list si read-only
     */
    int libvlc_media_list_insert_media(libvlc_media_list_t p_ml, libvlc_media_t p_md, int i_pos);

    /**
     * Remove media instance from media list on a position The libvlc_media_list_lock should be held
     * upon entering this function.
     *
     * @param p_ml a media list instance
     * @param i_pos position in array where to insert
     * @return 0 on success, -1 if the list is read-only or the item was not found
     */
    int libvlc_media_list_remove_index(libvlc_media_list_t p_ml, int i_pos);

    /**
     * Get count on media list items The libvlc_media_list_lock should be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     * @return number of items in media list
     */
    int libvlc_media_list_count(libvlc_media_list_t p_ml);

    /**
     * List media instance in media list at a position The libvlc_media_list_lock should be held
     * upon entering this function.
     *
     * @param p_ml a media list instance
     * @param i_pos position in array where to insert
     * @return media instance at position i_pos, or NULL if not found. In case of success,
     *         libvlc_media_retain() is called to increase the refcount on the media.
     */
    libvlc_media_t libvlc_media_list_item_at_index(libvlc_media_list_t p_ml, int i_pos);

    /**
     * Find index position of List media instance in media list. Warning: the function will return
     * the first matched position. The libvlc_media_list_lock should be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     * @param p_md media list instance
     * @return position of media instance
     */
    int libvlc_media_list_index_of_item(libvlc_media_list_t p_ml, libvlc_media_t p_md);

    /**
     * This indicates if this media list is read-only from a user point of view
     *
     * @param p_ml media list instance
     * @return 0 on readonly, 1 on readwrite
     */
    int libvlc_media_list_is_readonly(libvlc_media_list_t p_ml);

    /**
     * Get lock on media list items
     *
     * @param p_ml a media list instance
     */
    void libvlc_media_list_lock(libvlc_media_list_t p_ml);

    /**
     * Release lock on media list items The libvlc_media_list_lock should be held upon entering this
     * function.
     *
     * @param p_ml a media list instance
     */
    void libvlc_media_list_unlock(libvlc_media_list_t p_ml);

    /**
     * Get libvlc_event_manager from this media list instance. The p_event_manager is immutable, so
     * you don't have to hold the lock
     *
     * @param p_ml a media list instance
     * @return libvlc_event_manager
     */
    libvlc_event_manager_t libvlc_media_list_event_manager(libvlc_media_list_t p_ml);

    // === libvlc_media_list.h ==================================================

    // === libvlc_media_list_player.h ===========================================

    /**
     * Create new media_list_player.
     *
     * @param p_instance libvlc instance
     * @return media list player instance or NULL on error
     */
    libvlc_media_list_player_t libvlc_media_list_player_new(libvlc_instance_t p_instance);

    /**
     * Release a media_list_player after use.
     *
     * Decrement the reference count of a* media player object. If the reference count is 0, then
     * libvlc_media_list_player_release() will release the media player object. If the media player
     * object has been released, then it should not be used again.
     *
     * @param p_mlp media list player instance
     */
    void libvlc_media_list_player_release(libvlc_media_list_player_t p_mlp);

    /**
     * Retain a reference to a media player list object.
     *
     * Use libvlc_media_list_player_release() to decrement reference count.
     *
     * @param p_mlp media player list object
     */
    void libvlc_media_list_player_retain(libvlc_media_list_player_t p_mlp);

    /**
     * Return the event manager of this media_list_player.
     *
     * @param p_mlp media list player instance
     * @return the event manager
     */
    libvlc_event_manager_t libvlc_media_list_player_event_manager(libvlc_media_list_player_t p_mlp);

    /**
     * Replace media player in media_list_player with this instance.
     *
     * @param p_mlp media list player instance
     * @param p_mi media player instance
     */
    void libvlc_media_list_player_set_media_player(libvlc_media_list_player_t p_mlp, libvlc_media_player_t p_mi);

    /**
     * Set the media list associated with the player
     *
     * @param p_mlp media list player instance
     * @param p_mlist list of media
     */
    void libvlc_media_list_player_set_media_list(libvlc_media_list_player_t p_mlp, libvlc_media_list_t p_mlist);

    /**
     * Play media list
     *
     * @param p_mlp media list player instance
     */
    void libvlc_media_list_player_play(libvlc_media_list_player_t p_mlp);

    /**
     * Pause media list
     *
     * @param p_mlp media list player instance
     */
    void libvlc_media_list_player_pause(libvlc_media_list_player_t p_mlp);

    /**
     * Is media list playing?
     *
     * @param p_mlp media list player instance
     * @return true for playing and false for not playing
     */
    int libvlc_media_list_player_is_playing(libvlc_media_list_player_t p_mlp);

    /**
     * Get current libvlc_state of media list player
     *
     * @param p_mlp media list player instance
     * @return libvlc_state_t for media list player
     */
    libvlc_state_t libvlc_media_list_player_get_state(libvlc_media_list_player_t p_mlp);

    /**
     * Play media list item at position index
     *
     * @param p_mlp media list player instance
     * @param i_index index in media list to play
     * @return 0 upon success -1 if the item wasn't found
     */
    int libvlc_media_list_player_play_item_at_index(libvlc_media_list_player_t p_mlp, int i_index);

    /**
     * Play the given media item
     *
     * @param p_mlp media list player instance
     * @param p_md the media instance
     * @return 0 upon success, -1 if the media is not part of the media list
     */
    int libvlc_media_list_player_play_item(libvlc_media_list_player_t p_mlp, libvlc_media_t p_md);

    /**
     * Stop playing media list
     *
     * @param p_mlp media list player instance
     */
    void libvlc_media_list_player_stop(libvlc_media_list_player_t p_mlp);

    /**
     * Play next item from media list
     *
     * @param p_mlp media list player instance
     * @return 0 upon success -1 if there is no next item
     */
    int libvlc_media_list_player_next(libvlc_media_list_player_t p_mlp);

    /**
     * Play previous item from media list
     *
     * @param p_mlp media list player instance
     * @return 0 upon success -1 if there is no previous item
     */
    int libvlc_media_list_player_previous(libvlc_media_list_player_t p_mlp);

    /**
     * Sets the playback mode for the playlist
     *
     * @param p_mlp media list player instance
     * @param e_mode playback mode specification
     */
    void libvlc_media_list_player_set_playback_mode(libvlc_media_list_player_t p_mlp, int e_mode);

    // === libvlc_media_list_player.h ===========================================

    // === libvlc_media_discoverer.h ============================================

    /**
     * Discover media service by name.
     *
     * @param p_inst libvlc instance
     * @param psz_name service name
     * @return media discover object or NULL in case of error
     */
    libvlc_media_discoverer_t libvlc_media_discoverer_new_from_name(libvlc_instance_t p_inst, String psz_name);

    /**
     * Release media discover object. If the reference count reaches 0, then the object will be
     * released.
     *
     * @param p_mdis media service discover object
     */
    void libvlc_media_discoverer_release(libvlc_media_discoverer_t p_mdis);

    /**
     * Get media service discover object its localized name.
     *
     * @param p_mdis media discover object
     * @return localized name
     */
    Pointer libvlc_media_discoverer_localized_name(libvlc_media_discoverer_t p_mdis);

    /**
     * Get media service discover media list.
     *
     * @param p_mdis media service discover object
     * @return list of media items
     */
    libvlc_media_list_t libvlc_media_discoverer_media_list(libvlc_media_discoverer_t p_mdis);

    /**
     * Get event manager from media service discover object.
     *
     * @param p_mdis media service discover object
     * @return event manager object
     */
    libvlc_event_manager_t libvlc_media_discoverer_event_manager(libvlc_media_discoverer_t p_mdis);

    /**
     * Query if media service discover object is running.
     *
     * @param p_mdis media service discover object
     * @return true if running, false if not
     */
    int libvlc_media_discoverer_is_running(libvlc_media_discoverer_t p_mdis);

    // === libvlc_media_discoverer.h ============================================

    // === libvlc_vlm.h =========================================================

    /**
     * Release the vlm instance related to the given libvlc_instance_t
     *
     * @param p_instance the instance
     */
    void libvlc_vlm_release(libvlc_instance_t p_instance);

    /**
     * Add a broadcast, with one input.
     *
     * @param p_instance the instance
     * @param psz_name the name of the new broadcast
     * @param psz_input the input MRL
     * @param psz_output the output MRL (the parameter to the "sout" variable)
     * @param i_options number of additional options
     * @param ppsz_options additional options
     * @param b_enabled boolean for enabling the new broadcast
     * @param b_loop Should this broadcast be played in loop ?
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_add_broadcast(libvlc_instance_t p_instance, String psz_name, String psz_input, String psz_output, int i_options, String[] ppsz_options, int b_enabled, int b_loop);

    /**
     * Add a vod, with one input.
     *
     * @param p_instance the instance
     * @param psz_name the name of the new vod media
     * @param psz_input the input MRL
     * @param i_options number of additional options
     * @param ppsz_options additional options
     * @param b_enabled boolean for enabling the new vod
     * @param psz_mux the muxer of the vod media
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_add_vod(libvlc_instance_t p_instance, String psz_name, String psz_input, int i_options, String[] ppsz_options, int b_enabled, String psz_mux);

    /**
     * Delete a media (VOD or broadcast).
     *
     * @param p_instance the instance
     * @param psz_name the media to delete
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_del_media(libvlc_instance_t p_instance, String psz_name);

    /**
     * Enable or disable a media (VOD or broadcast).
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param b_enabled the new status
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_set_enabled(libvlc_instance_t p_instance, String psz_name, int b_enabled);

    /**
     * Set the output for a media.
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param psz_output the output MRL (the parameter to the "sout" variable)
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_set_output(libvlc_instance_t p_instance, String psz_name, String psz_output);

    /**
     * Set a media's input MRL. This will delete all existing inputs and add the specified one.
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param psz_input the input MRL
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_set_input(libvlc_instance_t p_instance, String psz_name, String psz_input);

    /**
     * Add a media's input MRL. This will add the specified one.
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param psz_input the input MRL
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_add_input(libvlc_instance_t p_instance, String psz_name, String psz_input);

    /**
     * Set a media's loop status.
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param b_loop the new status
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_set_loop(libvlc_instance_t p_instance, String psz_name, int b_loop);

    /**
     * Set a media's vod muxer.
     *
     * @param p_instance the instance
     * @param psz_name the media to work on
     * @param psz_mux the new muxer
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_set_mux(libvlc_instance_t p_instance, String psz_name, String psz_mux);

    /**
     * Edit the parameters of a media. This will delete all existing inputs and add the specified
     * one.
     *
     * @param p_instance the instance
     * @param psz_name the name of the new broadcast
     * @param psz_input the input MRL
     * @param psz_output the output MRL (the parameter to the "sout" variable)
     * @param i_options number of additional options
     * @param ppsz_options additional options
     * @param b_enabled boolean for enabling the new broadcast
     * @param b_loop Should this broadcast be played in loop ?
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_change_media(libvlc_instance_t p_instance, String psz_name, String psz_input, String psz_output, int i_options, String[] ppsz_options, int b_enabled, int b_loop);

    /**
     * Play the named broadcast.
     *
     * @param p_instance the instance
     * @param psz_name the name of the broadcast
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_play_media(libvlc_instance_t p_instance, String psz_name);

    /**
     * Stop the named broadcast.
     *
     * @param p_instance the instance
     * @param psz_name the name of the broadcast
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_stop_media(libvlc_instance_t p_instance, String psz_name);

    /**
     * Pause the named broadcast.
     *
     * @param p_instance the instance
     * @param psz_name the name of the broadcast
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_pause_media(libvlc_instance_t p_instance, String psz_name);

    /**
     * Seek in the named broadcast.
     *
     * @param p_instance the instance
     * @param psz_name the name of the broadcast
     * @param f_percentage the percentage to seek to
     * @return 0 on success, -1 on error
     */
    int libvlc_vlm_seek_media(libvlc_instance_t p_instance, String psz_name, float f_percentage);

    /**
     * Return information about the named media as a JSON string representation.
     *
     * This function is mainly intended for debugging use, if you want programmatic access to the
     * state of a vlm_media_instance_t, please use the corresponding
     * libvlc_vlm_get_media_instance_xxx -functions. Currently there are no such functions available
     * for vlm_media_t though.
     *
     * @param p_instance the instance
     * @param psz_name the name of the media, if the name is an empty string, all media is described
     * @return string with information about named media, or NULL on error
     */
    String libvlc_vlm_show_media(libvlc_instance_t p_instance, String psz_name);

    /**
     * Get vlm_media instance position by name or instance id
     *
     * @param p_instance a libvlc instance
     * @param psz_name name of vlm media instance
     * @param i_instance instance id
     * @return position as float or -1. on error
     */
    float libvlc_vlm_get_media_instance_position(libvlc_instance_t p_instance, String psz_name, int i_instance);

    /**
     * Get vlm_media instance time by name or instance id
     *
     * @param p_instance a libvlc instance
     * @param psz_name name of vlm media instance
     * @param i_instance instance id
     * @return time as integer or -1 on error
     */
    int libvlc_vlm_get_media_instance_time(libvlc_instance_t p_instance, String psz_name, int i_instance);

    /**
     * Get vlm_media instance length by name or instance id
     *
     * @param p_instance a libvlc instance
     * @param psz_name name of vlm media instance
     * @param i_instance instance id
     * @return length of media item or -1 on error
     */
    int libvlc_vlm_get_media_instance_length(libvlc_instance_t p_instance, String psz_name, int i_instance);

    /**
     * Get vlm_media instance playback rate by name or instance id
     *
     * @param p_instance a libvlc instance
     * @param psz_name name of vlm media instance
     * @param i_instance instance id
     * @return playback rate or -1 on error
     */
    int libvlc_vlm_get_media_instance_rate(libvlc_instance_t p_instance, String psz_name, int i_instance);

    /**
     * Get libvlc_event_manager from a vlm media. The p_event_manager is immutable, so you don't
     * have to hold the lock
     *
     * @param p_instance a libvlc instance
     * @return libvlc_event_manager
     */
    libvlc_event_manager_t libvlc_vlm_get_event_manager(libvlc_instance_t p_instance);

    // === libvlc_vlm.h =========================================================
}
