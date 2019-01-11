package uk.co.caprica.vlcj.player.base;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.binding.internal.*;
import uk.co.caprica.vlcj.player.*;
import uk.co.caprica.vlcj.player.events.standard.StandardEventFactory;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.CallbackMedia;
import uk.co.caprica.vlcj.player.media.simple.SimpleMedia;

public final class MediaService extends BaseService {

    private final MediaEventCallback callback = new MediaEventCallback();

    private String[] standardMediaOptions;

    private Media lastPlayedMedia;

    private libvlc_media_t mediaInstance;

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    MediaService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Set standard media options for all media items subsequently played.
     * <p>
     * This will <strong>not</strong> affect any currently playing media item.
     *
     * @param mediaOptions options to apply to all subsequently played media items
     */
    public void setStandardMediaOptions(String... mediaOptions) {
        this.standardMediaOptions = mediaOptions;
    }

    /**
     * Play a new media item, with options.
     * <p>
     * The new media will begin play-back <em>asynchronously</em>. This means that some
     * media player functions will likely not work if you invoke them immediately after
     * invoking this method - you will in some circumstances need to wait for an appropriate
     * event to be fired before some API functions will have an effect.
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path. This
     * should actually <em>not</em> be required.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    public boolean playMedia(String mrl, String... mediaOptions) {
        return playMedia(new SimpleMedia(mrl, mediaOptions));
    }

    /**
     * Play a new media item.
     * <p>
     * The new media will begin play-back <em>asynchronously</em>. This means that some
     * media player functions will likely not work if you invoke them immediately after
     * invoking this method - you will in some circumstances need to wait for an appropriate
     * event to be fired before some API functions will have an effect.
     *
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path. This
     * should actually <em>not</em> be required.
     *
     * @param media media, with options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    public boolean playMedia(Media media) {
        if (prepareMedia(media)) {
            mediaPlayer.controls().play();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prepare a new media item for play-back, but do not begin playing.
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    public boolean prepareMedia(String mrl, String... mediaOptions) {
        return prepareMedia(new SimpleMedia(mrl, mediaOptions));
    }

    /**
     * Prepare a new media item for play-back, but do not begin playing.
     * <p>
     * When playing files, depending on the run-time Operating System it may be necessary
     * to pass a URL here (beginning with "file://") rather than a local file path.
     *
     * @param media media, with options
     * @return <code>true</code> if the media item was created; <code>false</code> otherwise
     */
    public boolean prepareMedia(Media media) {
        return setMedia(media);
    }

    /**
     * Play a new media item, with options, and wait for it to start playing or error.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @param mrl media resource locator
     * @param mediaOptions zero or more media item options
     * @return <code>true</code> if the media started playing, <code>false</code> if the media failed to start because of an error
     */
    public boolean startMedia(String mrl, String... mediaOptions) {
        return startMedia(new SimpleMedia(mrl, mediaOptions));
    }

    /**
     * Play a new media item, with options, and wait for it to start playing or error.
     * <p>
     * This call will <strong>block</strong> until the media starts or errors.
     *
     * @param media media, with options
     * @return <code>true</code> if the media started playing, <code>false</code> if the media failed to start because of an error
     */
    public boolean startMedia(Media media) {
        // First 'prepare' the media...
        if (prepareMedia(media)) {
            // ...then play it and wait for it to start (or error)
            return new MediaPlayerLatch(mediaPlayer).play();
        } else {
            return false;
        }
    }

    /**
     * Add options to the current media.
     *
     * @param mediaOptions media options
     * @return <code>true</code> if the options were added; <code>false</code> otherwise (e.g. if no current media)
     */
    public boolean addMediaOptions(String... mediaOptions) {
        return MediaOptions.addMediaOptions(libvlc, mediaInstance, mediaOptions);
    }

    /**
     * Test whether or not the media player will automatically repeat playing the media when it has
     * finished playing.
     *
     * @return <code>true</code> if the media will be automatically replayed, otherwise <code>false</code>
     */
    public boolean getRepeat() {
        return repeat;
    }

    /**
     * Set whether or not the media player should automatically repeat playing the media when it has
     * finished playing.
     * <p>
     * There is <em>no</em> guarantee of seamless play-back when using this method - see instead
     * {@link uk.co.caprica.vlcj.player.list.MediaListPlayer MediaListPlayer}.
     * <p>
     * If the media has sub-items, then it is the sub-items that are repeated.
     *
     * @param repeat <code>true</code> to automatically replay the media, otherwise <code>false</code>
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Get the current media state.
     *
     * @return state, or <code>null</code> if there is no current media
     */
    // FIXME internal return type here
    public libvlc_state_t getMediaState() {
        if (mediaInstance != null) {
            return libvlc_state_t.state(libvlc.libvlc_media_get_state(mediaInstance));
        } else {
            return null;
        }
    }

    /**
     * Get the media type for the current media.
     * <p>
     * This is a medium type rather than e.g. a specific file type.
     *
     * @return media type, or <code>null</code> if the current media is <code>null</code>
     */
    // FIXME internal binding type
    public libvlc_media_type_e getMediaType() {
        if (mediaInstance != null) {
            return libvlc_media_type_e.mediaType(libvlc.libvlc_media_get_type(mediaInstance));
        } else {
            return null;
        }
    }

    /**
     * Get local meta data for the current media.
     * <p>
     * Some media types require that the media be parsed before accessing meta data - it is the
     * responsibility of the client application to parse the media if required, see
     * {@link #parseMedia()}.
     * <p>
     * Note that requesting meta data may cause one or more HTTP connections to
     * be made to external web-sites to attempt download of album art.
     * <p>
     * This function returns the meta data in a "live" object, any changes to the meta data will potentially cause an
     * update to the meta data stored in the media.
     *
     * @return meta data, or <code>null</code> if there is no current media
     */
    public MediaMeta getMediaMeta() {
        if (mediaInstance != null) {
            return new DefaultMediaMeta(libvlc, mediaInstance);
        } else {
            return null;
        }
    }

    /**
     * Get local meta data for the current media.
     * <p>
     * See {@link #getMediaMeta()}, the same notes with regard to parsing hold here.
     * <p>
     * This function returns the meta data in a "detached" value object, i.e. there is no link to
     * the native media handle (so the meta data can <em>not</em> be updated using this function.
     *
     * @return meta data, never <code>null</code>
     */
    public MediaMetaData getMediaMetaData() {
        return getMediaMeta().asMediaMetaData();
    }

    /**
     * Get the media resource locator for the current media instance.
     * <p>
     * The native media instance may be an automatically/scripted added sub-item.
     *
     * @return URL-encoded media resource locator, or <code>null</code> if there is no current media
     */
    public String mrl() {
        if (mediaInstance != null) {
            return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
        } else {
            return null;
        }
    }

    /**
     * Set new media for the native media player.
     * <p>
     * This method cleans up the previous media if there was one before associating new media with
     * the media player.
     *
     * @param media media and options
     * @throws IllegalArgumentException if the supplied MRL could not be parsed
     */
    private boolean setMedia(Media media) {
        // Remember the the media and options for possible replay later (also to keep the media
        // instance pinned to prevent it from being garbage collected - critical when using the
        // native media callbacks)
        this.lastPlayedMedia = media;
        // If there is a current media, clean it up
        if (mediaInstance != null) {
            // Release the media event listener
            deregisterMediaEventListener();
            // Release the native resource
            libvlc.libvlc_media_release(mediaInstance);
            mediaInstance = null;
        }
        // Reset sub-items
        mediaPlayer.subItems().reset();
        // Create the native media handle for the given media
        mediaInstance = createMediaInstance(media);
        if (mediaInstance != null) {
            // Set the standard media options (if any)...
            addMediaOptions(standardMediaOptions);
            addMediaOptions(media.mediaOptions());
            // Attach a listener to the new media
            registerMediaEventListener();
            // Set the new media on the media player
            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaInstance);
        }
        mediaPlayer.statistics().reset();
        return mediaInstance != null;
    }

    /**
     * Create a native media handle for the given media.
     *
     * @param media media
     * @return native media handle
     */
    private libvlc_media_t createMediaInstance(Media media) {
        libvlc_media_t result;
        if (media instanceof SimpleMedia) {
            String mrl = ((SimpleMedia) media).mrl();
            if (MediaResourceLocator.isLocation(mrl)) {
                result = libvlc.libvlc_media_new_location(libvlcInstance, mrl);
            } else {
                result = libvlc.libvlc_media_new_path(libvlcInstance, mrl);
            }
        } else if (media instanceof CallbackMedia) {
            CallbackMedia callbackMedia = (CallbackMedia)media;
            result = libvlc.libvlc_media_new_callbacks(
                    libvlcInstance,
                callbackMedia.getOpen(),
                callbackMedia.getRead(),
                callbackMedia.getSeek(),
                callbackMedia.getClose(),
                callbackMedia.getOpaque()
            );
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Register a call-back to receive media native events.
     */
    private void registerMediaEventListener() {
        if (mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaThumbnailGenerated.intValue()) {
                    libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    /**
     * De-register the call-back used to receive native media events.
     */
    private void deregisterMediaEventListener() {
        if (mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            for (libvlc_event_e event : libvlc_event_e.values()) {
                if (event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaThumbnailGenerated.intValue()) {
                    libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    private class MediaEventCallback implements libvlc_callback_t {

        private MediaEventCallback() {
            Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "media-events"));
        }

        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            mediaPlayer.events().raiseEvent(StandardEventFactory.createEvent(mediaPlayer, event));
        }
    }

    /**
     * Reset the current media.
     * <p>
     * This simply sets the last played media again so it may be played again. Without this, the current media would not
     * be able to be played again if it had finished.
     * </p>
     */
    void resetMedia() {
        setMedia(lastPlayedMedia);
    }

    libvlc_media_t media() {
        return mediaInstance;
    }

    @Override
    protected void release() {
        deregisterMediaEventListener();
    }

}
