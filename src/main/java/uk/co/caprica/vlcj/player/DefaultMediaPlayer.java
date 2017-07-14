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

package uk.co.caprica.vlcj.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_chapter_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_equalizer_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_parse_flag_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_role_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_type_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_navigate_mode_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_subtitle_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_teletext_key_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_title_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_type_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_orient_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_projection_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_track_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.condition.BeforeConditionAbortedException;
import uk.co.caprica.vlcj.player.events.MediaPlayerEvent;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventFactory;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;
import uk.co.caprica.vlcj.player.media.Media;
import uk.co.caprica.vlcj.player.media.callback.CallbackMedia;
import uk.co.caprica.vlcj.player.media.simple.SimpleMedia;
import uk.co.caprica.vlcj.version.LibVlcVersion;
import uk.co.caprica.vlcj.version.Version;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Media player implementation.
 */
public abstract class DefaultMediaPlayer extends AbstractMediaPlayer implements MediaPlayer, EqualizerListener {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(DefaultMediaPlayer.class);

    /**
     * Collection of media player event listeners.
     */
    private final CopyOnWriteArrayList<MediaPlayerEventListener> eventListenerList = new CopyOnWriteArrayList<MediaPlayerEventListener>();

    /**
     * Factory to create media player events from native events.
     */
    private final MediaPlayerEventFactory eventFactory = new MediaPlayerEventFactory(this);

    /**
     * Background thread to send event notifications to listeners.
     * <p>
     * The single-threaded nature of this executor service ensures that events are delivered to
     * listeners in a thread-safe manner and in their proper sequence.
     */
    private final ExecutorService listenersService = Executors.newSingleThreadExecutor();

    /**
     * Native media player instance.
     */
    private libvlc_media_player_t mediaPlayerInstance;

    /**
     * Native media player event manager.
     */
    private libvlc_event_manager_t mediaPlayerEventManager;

    /**
     * Call-back to handle native media player events.
     */
    private libvlc_callback_t callback;

    /**
     * Native media instance for current media (if there is one).
     */
    private libvlc_media_t mediaInstance;

    /**
     * Mask of the native events that will cause notifications to be sent to listeners.
     */
    private long eventMask = MediaPlayerEventType.ALL.value();

    /**
     * Standard options to be applied to all played media.
     */
    private String[] standardMediaOptions;

    /**
     *
     */
    // FIXME use a Java structure (encapsulate this in an event listener?)
    private libvlc_media_stats_t libvlcMediaStats;

    /**
     * Flag whether or not to automatically replay media after the media has finished playing.
     */
    private boolean repeat;

    /**
     * Flag whether or not to automatically play media sub-items if there are any.
     */
    private boolean playSubItems;

    /**
     * Index of the current sub-item, or -1.
     */
    private int subItemIndex;

    /**
     * Optional name of the directory to save video snapshots to.
     * <p>
     * If this is not set then snapshots will be saved to the user home directory.
     */
    private String snapshotDirectoryName;

    /**
     * Audio equalizer.
     *
     * May be <code>null</code>.
     */
    private Equalizer equalizer;

    /**
     * Native audio equalizer instance.
     */
    private libvlc_equalizer_t equalizerInstance;

    /**
     * Opaque reference to user/application-specific data associated with this media player.
     */
    private Object userData;

    /**
     * Set to true when the player has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Media that was last played (including media options).
     * <p>
     * This reference also serves to keep the media instance pinned and prevented from garbage
     * collection - this is critical if the media is using native callbacks.
     */
    private Media lastPlayedMedia;

    /**
     * Create a new media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public DefaultMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        super(libvlc, instance);
        logger.debug("DefaultMediaPlayer(libvlc={}, instance={})", libvlc, instance);
        createInstance();
    }

    @Override
    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        logger.debug("addMediaPlayerEventListener(listener={})", listener);
        if (listener != null) {
            eventListenerList.add(listener);
        }
        else {
            throw new IllegalArgumentException("listener can not be null");
        }
    }

    @Override
    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        logger.debug("removeMediaPlayerEventListener(listener={})", listener);
        if (listener != null) {
            eventListenerList.remove(listener);
        }
        else {
            throw new IllegalArgumentException("listener can not be null");
        }
    }

    @Override
    public void enableEvents(long eventMask) {
        logger.debug("enableEvents(eventMask={})", eventMask);
        this.eventMask = eventMask;
    }

    // === Media Controls =======================================================

    @Override
    public void setStandardMediaOptions(String... options) {
        logger.debug("setStandardMediaOptions(options={})", Arrays.toString(options));
        this.standardMediaOptions = options;
    }

    @Override
    public boolean playMedia(String mrl, String... mediaOptions) {
        logger.debug("playMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        return playMedia(new SimpleMedia(mrl, mediaOptions));
    }

    @Override
    public boolean playMedia(Media media) {
        logger.debug("playMedia(media={})", media);
        // First 'prepare' the media...
        if(prepareMedia(media)) {
            // ...then play it
            play();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean prepareMedia(String mrl, String... mediaOptions) {
        logger.debug("prepareMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        return prepareMedia(new SimpleMedia(mrl, mediaOptions));
    }

    @Override
    public boolean prepareMedia(Media media) {
        logger.debug("prepareMedia(media={})", media);
        return setMedia(media);
    }

    @Override
    public boolean startMedia(String mrl, String... mediaOptions) {
        logger.debug("startMedia(mrl={}, mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        return startMedia(new SimpleMedia(mrl, mediaOptions));
    }

    @Override
    public boolean startMedia(Media media) {
        logger.debug("startMedia(media={})", media);
        // First 'prepare' the media...
        if(prepareMedia(media)) {
            // ...then play it and wait for it to start (or error)
            return new MediaPlayerLatch(this).play();
        }
        else {
            return false;
        }
    }

    @Override
    public void parseMedia() {
        logger.debug("parseMedia()");
        if(mediaInstance != null) {
            libvlc.libvlc_media_parse(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public void requestParseMedia() {
        logger.debug("requestParseMedia()");
        if(mediaInstance != null) {
            libvlc.libvlc_media_parse_async(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public boolean requestParseMediaWithOptions(libvlc_media_parse_flag_t... options) {
        logger.debug("requestParseMediaWithOptions(options={})", options != null ? Arrays.toString(options) : "");
        return requestParseMediaWithOptions(0, options);
    }

    @Override
    public boolean requestParseMediaWithOptions(int timeout, libvlc_media_parse_flag_t... options) {
        logger.debug("requestParseMediaWithOptions(timeout={},options={})", timeout, options != null ? Arrays.toString(options) : "");
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
            int flags = 0;
            for (libvlc_media_parse_flag_t option : options) {
                flags |= option.intValue();
            }
            return libvlc.libvlc_media_parse_with_options(mediaInstance, flags, timeout) == 0;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isMediaParsed() {
        logger.debug("isMediaParsed()");
        if(mediaInstance != null) {
            return 0 != libvlc.libvlc_media_is_parsed(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public MediaMeta getMediaMeta() {
        logger.debug("getMediaMeta()");
        return getMediaMeta(mediaInstance);
    }

    @Override
    public MediaMeta getMediaMeta(libvlc_media_t media) {
        logger.debug("getMediaMeta(media={})", media);
        if(media != null) {
            return new DefaultMediaMeta(libvlc, media);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public List<MediaMeta> getSubItemMediaMeta() {
        logger.debug("getSubItemMediaMeta()");
        return handleSubItems(new SubItemsHandler<List<MediaMeta>>() {
            @Override
            public List<MediaMeta> subItems(int count, libvlc_media_list_t subItems) {
                List<MediaMeta> result = new ArrayList<MediaMeta>(count);
                for(libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                    result.add(getMediaMeta(subItem));
                }
                return result;
            }
        });
    }

    @Override
    public MediaMetaData getMediaMetaData() {
        logger.debug("getMediaMetaData()");
        return getMediaMeta().asMediaMetaData();
    }

    @Override
    public List<MediaMetaData> getSubItemMediaMetaData() {
        logger.debug("getSubItemMediaMetaData()");
        List<MediaMetaData> result;
        List<MediaMeta> metas = getSubItemMediaMeta();
        if(metas != null) {
            result = new ArrayList<MediaMetaData>(metas.size());
            for(MediaMeta meta : getSubItemMediaMeta()) {
                result.add(meta.asMediaMetaData());
            }
        }
        else {
            result = null;
        }
        return result;
    }

    @Override
    public void addMediaOptions(String... mediaOptions) {
        logger.debug("addMediaOptions(mediaOptions={})", Arrays.toString(mediaOptions));
        if(mediaInstance != null) {
            for(String mediaOption : mediaOptions) {
                logger.debug("mediaOption={}", mediaOption);
                libvlc.libvlc_media_add_option(mediaInstance, mediaOption);
            }
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public void setRepeat(boolean repeat) {
        logger.debug("setRepeat(repeat={})", repeat);
        this.repeat = repeat;
    }

    @Override
    public boolean getRepeat() {
        logger.debug("getRepeat()");
        return repeat;
    }

    // === Sub-Item Controls ====================================================

    @Override
    public void setPlaySubItems(boolean playSubItems) {
        logger.debug("setPlaySubItems(playSubItems={})", playSubItems);
        this.playSubItems = playSubItems;
    }

    @Override
    public int subItemCount() {
        logger.debug("subItemCount()");
        return handleSubItems(new SubItemsHandler<Integer>() {
            @Override
            public Integer subItems(int count, libvlc_media_list_t subItems) {
                return count;
            }
        });
    }

    @Override
    public int subItemIndex() {
        return subItemIndex;
    }

    @Override
    public List<String> subItems() {
        logger.debug("subItems()");
        return handleSubItems(new SubItemsHandler<List<String>>() {
            @Override
            public List<String> subItems(int count, libvlc_media_list_t subItems) {
                List<String> result = new ArrayList<String>(count);
                for(libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                    result.add(NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(subItem)));
                }
                return result;
            }
        });
    }

    @Override
    public List<libvlc_media_t> subItemsMedia() {
        logger.debug("subItemsMedia()");
        return handleSubItems(new SubItemsHandler<List<libvlc_media_t>>() {
            @Override
            public List<libvlc_media_t> subItems(int count, libvlc_media_list_t subItems) {
                List<libvlc_media_t> result = new ArrayList<libvlc_media_t>(count);
                for(libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                    result.add(subItem);
                }
                return result;
            }
        });
    }

    @Override
    public MediaList subItemsMediaList() {
        logger.debug("subItemsMediaList()");
        MediaList result;
        if(mediaInstance != null) {
            libvlc_media_list_t mediaListInstance = libvlc.libvlc_media_subitems(mediaInstance);
            result = new MediaList(libvlc, instance, mediaListInstance);
            libvlc.libvlc_media_list_release(mediaListInstance);
        }
        else {
            result = null;
        }
        return result;
    }

    @Override
    public boolean playNextSubItem(String... mediaOptions) {
        logger.debug("playNextSubItem(mediaOptions={})", Arrays.toString(mediaOptions));
        return playSubItem(subItemIndex + 1, mediaOptions);
    }

    @Override
    public boolean playSubItem(final int index, final String... mediaOptions) {
        logger.debug("playSubItem(index={},mediaOptions={})", index, Arrays.toString(mediaOptions));
        return handleSubItems(new SubItemsHandler<Boolean>() {
            @Override
            public Boolean subItems(int count, libvlc_media_list_t subItems) {
                if(subItems != null) {
                    logger.debug("Handling media sub-item...");
                    // Advance the current sub-item (initially it will be -1)...
                    logger.debug("count={}", count);
                    subItemIndex = index;
                    logger.debug("subItemIndex={}", subItemIndex);
                    // If the last sub-item already been played...
                    if(subItemIndex >= count) {
                        logger.debug("End of sub-items reached");
                        if(!repeat) {
                            logger.debug("Do not repeat sub-items");
                            subItemIndex = -1;
                            logger.debug("Raising events for end of sub-items");
                            raiseEvent(eventFactory.createMediaEndOfSubItemsEvent(eventMask));
                        }
                        else {
                            logger.debug("Repeating sub-items");
                            subItemIndex = 0;
                        }
                    }
                    if(subItemIndex != -1) {
                        // Get the required sub item from the list
                        libvlc_media_t subItem = libvlc.libvlc_media_list_item_at_index(subItems, subItemIndex);
                        logger.debug("subItem={}", subItem);
                        // If there is an item to play...
                        if(subItem != null) {
                            logger.debug("subItemMrl={}", mrl(subItem));
                            // Set the sub-item as the new media for the media player
                            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, subItem);
                            // Set any standard media options
                            if(standardMediaOptions != null) {
                                for(String standardMediaOption : standardMediaOptions) {
                                    logger.debug("standardMediaOption={}", standardMediaOption);
                                    libvlc.libvlc_media_add_option(subItem, standardMediaOption);
                                }
                            }
                            // Set any media options
                            if(mediaOptions != null) {
                                for(String mediaOption : mediaOptions) {
                                    logger.debug("mediaOption={}", mediaOption);
                                    libvlc.libvlc_media_add_option(subItem, mediaOption);
                                }
                            }
                            // Play the media
                            libvlc.libvlc_media_player_play(mediaPlayerInstance);
                            // Release the sub-item
                            libvlc.libvlc_media_release(subItem);
                            // Raise a semantic event to announce the sub-item was played
                            logger.debug("Raising played event for sub-item {}", subItemIndex);
                            raiseEvent(eventFactory.createMediaSubItemPlayedEvent(subItemIndex, eventMask));
                            // A sub-item was played
                            return true;
                        }
                    }
                }
                // A sub-item was not played
                return false;
            }
        });
    }

    // === Status Controls ======================================================

    @Override
    public boolean isPlayable() {
        logger.trace("isPlayable()");
        return libvlc.libvlc_media_player_will_play(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean isPlaying() {
        logger.trace("isPlaying()");
        return libvlc.libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean isSeekable() {
        logger.trace("isSeekable()");
        return libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean canPause() {
        logger.trace("canPause()");
        return libvlc.libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean programScrambled() {
        logger.trace("programScrambled()");
        return libvlc.libvlc_media_player_program_scrambled(mediaPlayerInstance) == 1;
    }

    @Override
    public long getLength() {
        logger.trace("getLength()");
        return libvlc.libvlc_media_player_get_length(mediaPlayerInstance);
    }

    @Override
    public long getTime() {
        logger.trace("getTime()");
        return libvlc.libvlc_media_player_get_time(mediaPlayerInstance);
    }

    @Override
    public float getPosition() {
        logger.trace("getPosition()");
        return libvlc.libvlc_media_player_get_position(mediaPlayerInstance);
    }

    @Override
    public float getFps() {
        logger.trace("getFps()");
        return libvlc.libvlc_media_player_get_fps(mediaPlayerInstance);
    }

    @Override
    public float getRate() {
        logger.trace("getRate()");
        return libvlc.libvlc_media_player_get_rate(mediaPlayerInstance);
    }

    @Override
    public int getVideoOutputs() {
        logger.trace("getVideoOutputs()");
        return libvlc.libvlc_media_player_has_vout(mediaPlayerInstance);
    }

    @Override
    public Dimension getVideoDimension() {
        logger.debug("getVideoDimension()");
        if(getVideoOutputs() > 0) {
            IntByReference px = new IntByReference();
            IntByReference py = new IntByReference();
            int result = libvlc.libvlc_video_get_size(mediaPlayerInstance, 0, px, py);
            if(result == 0) {
                return new Dimension(px.getValue(), py.getValue());
            }
            else {
                logger.warn("Video size is not available");
                return null;
            }
        }
        else {
            logger.warn("Can't get video dimension if no video output has been started");
            return null;
        }
    }

    @Override
    public MediaDetails getMediaDetails() {
        logger.debug("getMediaDetails()");
        // The media must be playing to get this meta data...
        if(isPlaying()) {
            MediaDetails mediaDetails = new MediaDetails();
            mediaDetails.setTitleCount(getTitleCount());
            mediaDetails.setVideoTrackCount(getVideoTrackCount());
            mediaDetails.setAudioTrackCount(getAudioTrackCount());
            mediaDetails.setSpuCount(getSpuCount());
            mediaDetails.setTitleDescriptions(getTitleDescriptions());
            mediaDetails.setVideoDescriptions(getVideoDescriptions());
            mediaDetails.setAudioDescriptions(getAudioDescriptions());
            mediaDetails.setSpuDescriptions(getSpuDescriptions());
            mediaDetails.setChapterDescriptions(getAllChapterDescriptions());
            return mediaDetails;
        }
        else {
            logger.warn("Can't get media meta data if media is not playing");
            return null;
        }
    }

    @Override
    public String getAspectRatio() {
        logger.debug("getAspectRatio()");
        return NativeString.getNativeString(libvlc, libvlc.libvlc_video_get_aspect_ratio(mediaPlayerInstance));
    }

    @Override
    public float getScale() {
        logger.debug("getScale()");
        return libvlc.libvlc_video_get_scale(mediaPlayerInstance);
    }

    @Override
    public String getCropGeometry() {
        logger.debug("getCropGeometry()");
        return NativeString.getNativeString(libvlc, libvlc.libvlc_video_get_crop_geometry(mediaPlayerInstance));
    }

    @Override
    public libvlc_media_stats_t getMediaStatistics() {
        logger.trace("getMediaStatistics()");
        return getMediaStatistics(mediaInstance);
    }

    @Override
    public libvlc_media_stats_t getMediaStatistics(libvlc_media_t media) {
        logger.trace("getMediaStatistics(media={})", media);
        // Must first check that the media is playing otherwise a fatal JVM crash
        // will occur - potentially this could still cause a fatal crash if the
        // media item supplied is not the one actually playing right now
        if(isPlaying() && media != null) {
            libvlc.libvlc_media_get_stats(media, libvlcMediaStats);
        }
        return libvlcMediaStats;
    }

    // FIXME do not return the native structure, should be a Java enum
    @Override
    public libvlc_state_t getMediaState() {
        logger.debug("getMediaState()");
        libvlc_state_t state = null;
        if(mediaInstance != null) {
            state = libvlc_state_t.state(libvlc.libvlc_media_get_state(mediaInstance));
        }
        return state;
    }

    // FIXME do not return the native structure, should be a Java enum
    @Override
    public libvlc_state_t getMediaPlayerState() {
        logger.debug("getMediaPlayerState()");
        return libvlc_state_t.state(libvlc.libvlc_media_player_get_state(mediaPlayerInstance));
    }

    // === Title/Track Controls =================================================

    @Override
    public int getTitleCount() {
        logger.debug("getTitleCount()");
        return libvlc.libvlc_media_player_get_title_count(mediaPlayerInstance);
    }

    @Override
    public int getTitle() {
        logger.debug("getTitle()");
        return libvlc.libvlc_media_player_get_title(mediaPlayerInstance);
    }

    @Override
    public void setTitle(int title) {
        logger.debug("setTitle(title={})", title);
        libvlc.libvlc_media_player_set_title(mediaPlayerInstance, title);
    }

    @Override
    public int getVideoTrackCount() {
        logger.debug("getVideoTrackCount()");
        return libvlc.libvlc_video_get_track_count(mediaPlayerInstance);
    }

    @Override
    public int getVideoTrack() {
        logger.debug("getVideoTrack()");
        return libvlc.libvlc_video_get_track(mediaPlayerInstance);
    }

    @Override
    public int setVideoTrack(int track) {
        logger.debug("setVideoTrack(track={})", track);
        libvlc.libvlc_video_set_track(mediaPlayerInstance, track);
        return getVideoTrack();
    }

    @Override
    public int getAudioTrackCount() {
        logger.debug("getVideoTrackCount()");
        return libvlc.libvlc_audio_get_track_count(mediaPlayerInstance);
    }

    @Override
    public int getAudioTrack() {
        logger.debug("getAudioTrack()");
        return libvlc.libvlc_audio_get_track(mediaPlayerInstance);
    }

    @Override
    public int setAudioTrack(int track) {
        logger.debug("setAudioTrack(track={})", track);
        libvlc.libvlc_audio_set_track(mediaPlayerInstance, track);
        return getAudioTrack();
    }

    // === Basic Playback Controls ==============================================

    @Override
    public void play() {
        logger.debug("play()");
        onBeforePlay();
        libvlc.libvlc_media_player_play(mediaPlayerInstance);
        logger.debug("after play");
    }

    @Override
    public boolean start() {
        return new MediaPlayerLatch(this).play();
    }

    @Override
    public void stop() {
        logger.debug("stop()");
        libvlc.libvlc_media_player_stop(mediaPlayerInstance);
    }

    @Override
    public void setPause(boolean pause) {
        logger.debug("setPause(pause={})", pause);
        libvlc.libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
    }

    @Override
    public void pause() {
        logger.debug("pause()");
        libvlc.libvlc_media_player_pause(mediaPlayerInstance);
    }

    @Override
    public void nextFrame() {
        logger.debug("nextFrame()");
        libvlc.libvlc_media_player_next_frame(mediaPlayerInstance);
    }

    @Override
    public void skip(long delta) {
        logger.debug("skip(delta={})", delta);
        long current = getTime();
        logger.debug("current={}", current);
        if(current != -1) {
            setTime(current + delta);
        }
    }

    @Override
    public void skipPosition(float delta) {
        logger.debug("skipPosition(delta={})", delta);
        float current = getPosition();
        logger.debug("current={}", current);
        if(current != -1) {
            setPosition(current + delta);
        }
    }

    @Override
    public void setTime(long time) {
        logger.debug("setTime(time={})", time);
        libvlc.libvlc_media_player_set_time(mediaPlayerInstance, Math.max(time, 0));
    }

    @Override
    public void setPosition(float position) {
        logger.debug("setPosition(position={})", position);
        libvlc.libvlc_media_player_set_position(mediaPlayerInstance, Math.max(position, 0));
    }

    @Override
    public int setRate(float rate) {
        logger.debug("setRate(rate={})", rate);
        return libvlc.libvlc_media_player_set_rate(mediaPlayerInstance, rate);
    }

    @Override
    public void setAspectRatio(String aspectRatio) {
        logger.debug("setAspectRatio(aspectRatio={})", aspectRatio);
        libvlc.libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
    }

    @Override
    public void setScale(float factor) {
        logger.debug("setScale(factor={})", factor);
        libvlc.libvlc_video_set_scale(mediaPlayerInstance, factor);
    }

    @Override
    public void setCropGeometry(String cropGeometry) {
        logger.debug("setCropGeometry(cropGeometry={})", cropGeometry);
        libvlc.libvlc_video_set_crop_geometry(mediaPlayerInstance, cropGeometry);
    }

    // === Audio Controls =======================================================

    @Override
    public boolean setAudioOutput(String output) {
        logger.debug("setAudioOutput(output={})", output);
        return 0 == libvlc.libvlc_audio_output_set(mediaPlayerInstance, output);
    }

    @Override
    public void setAudioOutputDevice(String output, String outputDeviceId) {
        logger.debug("setAudioOutputDevice(output={},outputDeviceId={})", output, outputDeviceId);
        libvlc.libvlc_audio_output_device_set(mediaPlayerInstance, output, outputDeviceId);
    }

    @Override
    public String getAudioOutputDevice() {
        logger.debug("getAudioOutputDevice()");
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
            return NativeString.getNativeString(libvlc, libvlc.libvlc_audio_output_device_get(mediaPlayerInstance));
        }
        else {
            return null;
        }
    }

    @Override
    public List<AudioDevice> getAudioOutputDevices() {
        logger.debug("getAudioOutputDevices()");
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_220)) {
            List<AudioDevice> result = new ArrayList<AudioDevice>();
            libvlc_audio_output_device_t audioDevices = libvlc.libvlc_audio_output_device_enum(mediaPlayerInstance);
            if (audioDevices != null) {
                // Must prevent automatic synchronisation on the native structure, otherwise a
                // fatal JVM crash will occur when the native release call is made - not quite
                // sure why this is needed here
                audioDevices.setAutoSynch(false);
                libvlc_audio_output_device_t audioDevice = audioDevices;
                while(audioDevice != null) {
                    // The native strings must be copied here, but not freed (they are freed natively
                    // in the subsequent release call)
                    String device = NativeString.copyNativeString(libvlc, audioDevice.psz_device);
                    String description = NativeString.copyNativeString(libvlc, audioDevice.psz_description);
                    result.add(new AudioDevice(device, description));
                    audioDevice = audioDevice.p_next;
                }
                libvlc.libvlc_audio_output_device_list_release(audioDevices);
            }
            return result;
        }
        else {
            logger.warn("Audio output device enumeration requires libvlc 2.2.0 or later");
            return null;
        }
    }

    @Override
    public boolean mute() {
        logger.debug("mute()");
        libvlc.libvlc_audio_toggle_mute(mediaPlayerInstance);
        return isMute();
    }

    @Override
    public void mute(boolean mute) {
        logger.debug("mute(mute={})", mute);
        libvlc.libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
    }

    @Override
    public boolean isMute() {
        logger.debug("isMute()");
        return libvlc.libvlc_audio_get_mute(mediaPlayerInstance) != 0;
    }

    @Override
    public int getVolume() {
        logger.debug("getVolume()");
        return libvlc.libvlc_audio_get_volume(mediaPlayerInstance);
    }

    @Override
    public void setVolume(int volume) {
        logger.debug("setVolume(volume={})", volume);
        libvlc.libvlc_audio_set_volume(mediaPlayerInstance, volume);
    }

    @Override
    public int getAudioChannel() {
        logger.debug("getAudioChannel()");
        return libvlc.libvlc_audio_get_channel(mediaPlayerInstance);
    }

    @Override
    public void setAudioChannel(int channel) {
        logger.debug("setAudioChannel(channel={})", channel);
        libvlc.libvlc_audio_set_channel(mediaPlayerInstance, channel);
    }

    @Override
    public long getAudioDelay() {
        logger.debug("getAudioDelay()");
        return libvlc.libvlc_audio_get_delay(mediaPlayerInstance);
    }

    @Override
    public void setAudioDelay(long delay) {
        logger.debug("setAudioDelay(delay={})", delay);
        libvlc.libvlc_audio_set_delay(mediaPlayerInstance, delay);
    }

    // === Chapter Controls =====================================================

    @Override
    public int getChapterCount() {
        logger.trace("getChapterCount()");
        return libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance);
    }

    @Override
    public int getChapter() {
        logger.trace("getChapter()");
        return libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance);
    }

    @Override
    public void setChapter(int chapterNumber) {
        logger.debug("setChapter(chapterNumber={})", chapterNumber);
        libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber);
    }

    @Override
    public void nextChapter() {
        logger.debug("nextChapter()");
        libvlc.libvlc_media_player_next_chapter(mediaPlayerInstance);
    }

    @Override
    public void previousChapter() {
        logger.debug("previousChapter()");
        libvlc.libvlc_media_player_previous_chapter(mediaPlayerInstance);
    }

    // === DVD Menu Navigation Controls =========================================

    @Override
    public void menuActivate() {
        logger.debug("menuActivate()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_activate.intValue());
    }

    @Override
    public void menuUp() {
        logger.debug("menuUp()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_up.intValue());
    }

    @Override
    public void menuDown() {
        logger.debug("menuDown()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_down.intValue());
    }

    @Override
    public void menuLeft() {
        logger.debug("menuLeft()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_left.intValue());
    }

    @Override
    public void menuRight() {
        logger.debug("menuRight()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_right.intValue());
    }

    // === Sub-Picture/Sub-Title Controls =======================================

    @Override
    public int getSpuCount() {
        logger.debug("getSpuCount()");
        return libvlc.libvlc_video_get_spu_count(mediaPlayerInstance);
    }

    @Override
    public int getSpu() {
        logger.debug("getSpu()");
        return libvlc.libvlc_video_get_spu(mediaPlayerInstance);
    }

    @Override
    public int setSpu(int spu) {
        logger.debug("setSpu(spu={})", spu);
        libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu);
        return getSpu();
    }

    @Override
    public long getSpuDelay() {
        logger.debug("getSpuDelay()");
        return libvlc.libvlc_video_get_spu_delay(mediaPlayerInstance);
    }

    @Override
    public void setSpuDelay(long delay) {
        logger.debug("setSpuDelay(delay={})", delay);
        libvlc.libvlc_video_set_spu_delay(mediaPlayerInstance, delay);
    }

    @Override
    public void setSubTitleFile(String subTitleFileName) {
        logger.debug("setSubTitleFile(subTitleFileName={})", subTitleFileName);
        libvlc.libvlc_video_set_subtitle_file(mediaPlayerInstance, subTitleFileName);
    }

    @Override
    public void setSubTitleFile(File subTitleFile) {
        logger.debug("setSubTitleFile(subTitleFile={})", subTitleFile);
        setSubTitleFile(subTitleFile.getAbsolutePath());
    }

    // === Teletext Controls ====================================================

    @Override
    public int getTeletextPage() {
        logger.debug("getTeletextPage()");
        return libvlc.libvlc_video_get_teletext(mediaPlayerInstance);
    }

    @Override
    public void setTeletextPage(int pageNumber) {
        logger.debug("setTeletextPage(pageNumber={})", pageNumber);
        libvlc.libvlc_video_set_teletext(mediaPlayerInstance, pageNumber);
    }

    @Override
    public void setTeletextKey(libvlc_teletext_key_e key) {
        logger.debug("setTeletextKey(key={})", key);
        libvlc.libvlc_video_set_teletext(mediaPlayerInstance, key.intValue());
    }

    @Override
    public void toggleTeletext() {
        logger.debug("toggleTeletext()");
        libvlc.libvlc_toggle_teletext(mediaPlayerInstance);
    }

    // === Description Controls =================================================

    @Override
    public List<TrackDescription> getTitleDescriptions() {
        logger.debug("getTitleDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_title_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getVideoDescriptions() {
        logger.debug("getVideoDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getAudioDescriptions() {
        logger.debug("getAudioDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_audio_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getSpuDescriptions() {
        logger.debug("getSpuDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_spu_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<String> getChapterDescriptions(int title) {
        logger.debug("getChapterDescriptions(title={})", title);
        List<String> trackDescriptionList;
        if(title >= 0 && title < getTitleCount()) {
            trackDescriptionList = new ArrayList<String>();
            libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_chapter_description(mediaPlayerInstance, title);
            libvlc_track_description_t trackDescription = trackDescriptions;
            while(trackDescription != null) {
                trackDescriptionList.add(trackDescription.psz_name);
                trackDescription = trackDescription.p_next;
            }
            if(trackDescriptions != null) {
                libvlc.libvlc_track_description_list_release(trackDescriptions.getPointer());
            }
        }
        else {
            trackDescriptionList = null;
        }
        return trackDescriptionList;
    }

    @Override
    public List<String> getChapterDescriptions() {
        logger.debug("getChapterDescriptions()");
        return getChapterDescriptions(getTitle());
    }

    @Override
    public List<List<String>> getAllChapterDescriptions() {
        logger.debug("getAllChapterDescriptions()");
        int titleCount = getTitleCount();
        List<List<String>> result = new ArrayList<List<String>>(Math.max(titleCount, 0));
        for(int i = 0; i < titleCount; i ++ ) {
            result.add(getChapterDescriptions(i));
        }
        return result;
    }

    @Override
    public List<TitleDescription> getExtendedTitleDescriptions() {
        logger.debug("getExtendedTitleDescriptions()");
        List<TitleDescription> result;
        if (LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
            PointerByReference titles = new PointerByReference();
            int titleCount = libvlc.libvlc_media_player_get_full_title_descriptions(mediaPlayerInstance, titles);
            if (titleCount != -1) {
                result = new ArrayList<TitleDescription>(titleCount);
                Pointer[] pointers = titles.getValue().getPointerArray(0, titleCount);
                for (Pointer pointer : pointers) {
                    libvlc_title_description_t titleDescription = (libvlc_title_description_t) Structure.newInstance(libvlc_title_description_t.class, pointer);
                    titleDescription.read();
                    result.add(new TitleDescription(titleDescription.i_duration, NativeString.copyNativeString(libvlc, titleDescription.psz_name), titleDescription.b_menu != 0));
                }
                libvlc.libvlc_title_descriptions_release(titles.getValue(), titleCount);
            }
            else {
                result = new ArrayList<TitleDescription>(0);
            }
        }
        else {
            result = new ArrayList<TitleDescription>(0);
        }
        return result;
    }

    @Override
    public List<ChapterDescription> getExtendedChapterDescriptions() {
        logger.debug("getExtendedChapterDescriptions()");
        return getExtendedChapterDescriptions(getTitle());
    }

    @Override
    public List<ChapterDescription> getExtendedChapterDescriptions(int title) {
        logger.debug("getExtendedChapterDescriptions(title={})", title);
        List<ChapterDescription> result;
        if (LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
            PointerByReference chapters = new PointerByReference();
            int chapterCount = libvlc.libvlc_media_player_get_full_chapter_descriptions(mediaPlayerInstance, title, chapters);
            if (chapterCount != -1) {
                result = new ArrayList<ChapterDescription>(chapterCount);
                Pointer[] pointers = chapters.getValue().getPointerArray(0, chapterCount);
                for (Pointer pointer : pointers) {
                    libvlc_chapter_description_t chapterDescription = (libvlc_chapter_description_t) Structure.newInstance(libvlc_chapter_description_t.class, pointer);
                    chapterDescription.read();
                    result.add(new ChapterDescription(chapterDescription.i_time_offset, chapterDescription.i_duration, NativeString.getNativeString(libvlc, chapterDescription.psz_name)));
                }
                libvlc.libvlc_chapter_descriptions_release(chapters.getValue(), chapterCount);
            }
            else {
                result = new ArrayList<ChapterDescription>(0);
            }
        }
        else {
            result = new ArrayList<ChapterDescription>(0);
        }
        return result;
    }

    @Override
    public List<TrackInfo> getTrackInfo(TrackType... types) {
        logger.debug("getTrackInfo(types={})", Arrays.toString(types));
        return getTrackInfo(mediaInstance, types);
    }

    @Override
    public List<TrackInfo> getTrackInfo(libvlc_media_t media, TrackType... types) {
        logger.debug("getTrackInfo(media={},types={})", media, Arrays.toString(types));
        List<TrackInfo> result = null;
        if(media != null) {
            // Convert the types parameter for ease of use
            Set<TrackType> requestedTypes;
            if(types == null || types.length == 0) {
                requestedTypes = null;
            }
            else {
                requestedTypes = new HashSet<TrackType>(types.length);
                for(TrackType type : types) {
                    requestedTypes.add(type);
                }
            }
            result = getTrackInfo(media, requestedTypes);
        }
        return result;
    }

    /**
     * Get track info using the new libvlc 2.1.0+ implementation.
     *
     * @param types set of desired track types
     * @param media media descriptor
     * @return track info
     */
    private List<TrackInfo> getTrackInfo(libvlc_media_t media, Set<TrackType> types) {
        logger.debug("newGetTrackInfo(media={},types={})", media, types);
        PointerByReference tracksPointer = new PointerByReference();
        int numberOfTracks = libvlc.libvlc_media_tracks_get(media, tracksPointer);
        logger.debug("numberOfTracks={}", numberOfTracks);
        List<TrackInfo> result = new ArrayList<TrackInfo>(numberOfTracks);
        if(numberOfTracks > 0) {
            Pointer[] tracks = tracksPointer.getValue().getPointerArray(0, numberOfTracks);
            for(Pointer track : tracks) {
                libvlc_media_track_t trackInfo = new libvlc_media_track_t(track);
                switch(libvlc_track_type_t.valueOf(trackInfo.i_type)) {
                    case libvlc_track_unknown:
                        if(types == null || types.contains(TrackType.UNKNOWN)) {
                            result.add(new UnknownTrackInfo(
                                trackInfo.i_codec,
                                trackInfo.i_original_fourcc,
                                trackInfo.i_id,
                                trackInfo.i_profile,
                                trackInfo.i_level,
                                trackInfo.i_bitrate,
                                NativeString.copyNativeString(libvlc, trackInfo.psz_language),
                                NativeString.copyNativeString(libvlc, trackInfo.psz_description),
                                getCodecDescription(libvlc_track_type_t.libvlc_track_unknown, trackInfo.i_codec)
                            ));
                        }
                        break;

                    case libvlc_track_video:
                        if(types == null || types.contains(TrackType.VIDEO)) {
                            trackInfo.u.setType(libvlc_video_track_t.class);
                            trackInfo.u.read();
                            if (LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
                                result.add(new VideoTrackInfo(
                                    trackInfo.i_codec,
                                    trackInfo.i_original_fourcc,
                                    trackInfo.i_id,
                                    trackInfo.i_profile,
                                    trackInfo.i_level,
                                    trackInfo.i_bitrate,
                                    NativeString.copyNativeString(libvlc, trackInfo.psz_language),
                                    NativeString.copyNativeString(libvlc, trackInfo.psz_description),
                                    trackInfo.u.video.i_width,
                                    trackInfo.u.video.i_height,
                                    trackInfo.u.video.i_sar_num,
                                    trackInfo.u.video.i_sar_den,
                                    trackInfo.u.video.i_frame_rate_num,
                                    trackInfo.u.video.i_frame_rate_den,
                                    libvlc_video_orient_e.orientation(trackInfo.u.video.i_orientation),
                                    libvlc_video_projection_e.projection(trackInfo.u.video.i_projection),
                                    trackInfo.u.video.f_yaw_degrees,
                                    trackInfo.u.video.f_pitch_degrees,
                                    trackInfo.u.video.f_roll_degrees,
                                    trackInfo.u.video.f_fov_degrees,
                                    getCodecDescription(libvlc_track_type_t.libvlc_track_video, trackInfo.i_codec)
                                ));
                            }
                            else {
                                result.add(new VideoTrackInfo(
                                    trackInfo.i_codec,
                                    trackInfo.i_original_fourcc,
                                    trackInfo.i_id,
                                    trackInfo.i_profile,
                                    trackInfo.i_level,
                                    trackInfo.i_bitrate,
                                    NativeString.copyNativeString(libvlc, trackInfo.psz_language),
                                    NativeString.copyNativeString(libvlc, trackInfo.psz_description),
                                    trackInfo.u.video.i_width,
                                    trackInfo.u.video.i_height,
                                    trackInfo.u.video.i_sar_num,
                                    trackInfo.u.video.i_sar_den,
                                    trackInfo.u.video.i_frame_rate_num,
                                    trackInfo.u.video.i_frame_rate_den,
                                    null,
                                    null,
                                    0.f,
                                    0.f,
                                    0.f,
                                    0.f,
                                    getCodecDescription(libvlc_track_type_t.libvlc_track_video, trackInfo.i_codec)
                                ));
                            }
                        }
                        break;

                    case libvlc_track_audio:
                        if(types == null || types.contains(TrackType.AUDIO)) {
                            trackInfo.u.setType(libvlc_audio_track_t.class);
                            trackInfo.u.read();
                            result.add(new AudioTrackInfo(
                                trackInfo.i_codec,
                                trackInfo.i_original_fourcc,
                                trackInfo.i_id,
                                trackInfo.i_profile,
                                trackInfo.i_level,
                                trackInfo.i_bitrate,
                                NativeString.copyNativeString(libvlc, trackInfo.psz_language),
                                NativeString.copyNativeString(libvlc, trackInfo.psz_description),
                                trackInfo.u.audio.i_channels,
                                trackInfo.u.audio.i_rate,
                                getCodecDescription(libvlc_track_type_t.libvlc_track_audio, trackInfo.i_codec)
                            ));
                        }
                        break;

                    case libvlc_track_text:
                        if(types == null || types.contains(TrackType.TEXT)) {
                            trackInfo.u.setType(libvlc_subtitle_track_t.class);
                            trackInfo.u.read();
                            result.add(new TextTrackInfo(
                                trackInfo.i_codec,
                                trackInfo.i_original_fourcc,
                                trackInfo.i_id,
                                trackInfo.i_profile,
                                trackInfo.i_level,
                                trackInfo.i_bitrate,
                                NativeString.copyNativeString(libvlc, trackInfo.psz_language),
                                NativeString.copyNativeString(libvlc, trackInfo.psz_description),
                                NativeString.copyNativeString(libvlc, trackInfo.u.subtitle.psz_encoding),
                                getCodecDescription(libvlc_track_type_t.libvlc_track_text, trackInfo.i_codec)
                            ));
                        }
                        break;
                }
            }
            libvlc.libvlc_media_tracks_release(tracksPointer.getValue(), numberOfTracks); // FIXME maybe should copy the nativestring here?
        }
        return result;
    }

    @Override
    public libvlc_media_type_e getMediaType() {
        logger.debug("getMediaType()");
        return getMediaType(mediaInstance);
    }

    @Override
    public libvlc_media_type_e getMediaType(libvlc_media_t media) {
        logger.debug("getMediaType(media={})", media);
        if (media != null) {
            return libvlc_media_type_e.mediaType(libvlc.libvlc_media_get_type(media));
        }
        else {
            return null;
        }
    }

    @Override
    public String getCodecDescription(libvlc_track_type_t type, int codec) {
        logger.debug("getCodecDescription(type={},codec={})", type, codec);
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_300)) {
            return libvlc.libvlc_media_get_codec_description(type.intValue(), codec);
        }
        else {
            return "";
        }
    }

    @Override
    public List<List<TrackInfo>> getSubItemTrackInfo(TrackType... types) {
        logger.debug("getSubItemTrackInfo(types={})", Arrays.toString(types));
        return handleSubItems(new SubItemsHandler<List<List<TrackInfo>>>() {
            @Override
            public List<List<TrackInfo>> subItems(int count, libvlc_media_list_t subItems) {
                List<List<TrackInfo>> result = new ArrayList<List<TrackInfo>>(count);
                for(libvlc_media_t subItem : new LibVlcMediaListIterator(libvlc, subItems)) {
                    result.add(getTrackInfo(subItem));
                }
                return result;
            }
        });
    }

    /**
     * Get track descriptions.
     *
     * @param trackDescriptions native track descriptions, this pointer will be freed by this method
     * @return collection of track descriptions
     */
    private List<TrackDescription> getTrackDescriptions(libvlc_track_description_t trackDescriptions) {
        logger.debug("getTrackDescriptions()");
        List<TrackDescription> trackDescriptionList = new ArrayList<TrackDescription>();
        libvlc_track_description_t trackDescription = trackDescriptions;
        while(trackDescription != null) {
            trackDescriptionList.add(new TrackDescription(trackDescription.i_id, trackDescription.psz_name));
            trackDescription = trackDescription.p_next;
        }
        if(trackDescriptions != null) {
            libvlc.libvlc_track_description_list_release(trackDescriptions.getPointer());
        }
        return trackDescriptionList;
    }

    // === Snapshot Controls ====================================================

    @Override
    public void setSnapshotDirectory(String snapshotDirectoryName) {
        logger.debug("setSnapshotDirectory(snapshotDirectoryName={})", snapshotDirectoryName);
        this.snapshotDirectoryName = snapshotDirectoryName;
    }

    @Override
    public boolean saveSnapshot() {
        logger.debug("saveSnapshot()");
        return saveSnapshot(0, 0);
    }

    @Override
    public boolean saveSnapshot(int width, int height) {
        logger.debug("saveSnapshot(width={},height={})", width, height);
        File snapshotDirectory = new File(snapshotDirectoryName == null ? System.getProperty("user.home") : snapshotDirectoryName);
        File snapshotFile = new File(snapshotDirectory, "vlcj-snapshot-" + System.currentTimeMillis() + ".png");
        return saveSnapshot(snapshotFile, width, height);
    }

    @Override
    public boolean saveSnapshot(File file) {
        logger.debug("saveSnapshot(file={})", file);
        return saveSnapshot(file, 0, 0);
    }

    @Override
    public boolean saveSnapshot(File file, int width, int height) {
        logger.debug("saveSnapshot(file={},width={},height={})", file, width, height);
        File snapshotDirectory = file.getParentFile();
        if(snapshotDirectory == null) {
            snapshotDirectory = new File(".");
            logger.debug("No directory specified for snapshot, snapshot will be saved to {}", snapshotDirectory.getAbsolutePath());
        }
        if(!snapshotDirectory.exists()) {
            snapshotDirectory.mkdirs();
        }
        if(snapshotDirectory.exists()) {
            boolean snapshotTaken = libvlc.libvlc_video_take_snapshot(mediaPlayerInstance, 0, file.getAbsolutePath(), width, height) == 0;
            logger.debug("snapshotTaken={}", snapshotTaken);
            return snapshotTaken;
        }
        else {
            throw new RuntimeException("Directory does not exist and could not be created for '" + file.getAbsolutePath() + "'");
        }
    }

    @Override
    public BufferedImage getSnapshot() {
        logger.debug("getSnapshot()");
        return getSnapshot(0, 0);
    }

    @Override
    public BufferedImage getSnapshot(int width, int height) {
        logger.debug("getSnapshot(width={},height={})", width, height);
        File file = null;
        try {
            file = File.createTempFile("vlcj-snapshot-", ".png");
            logger.debug("file={}", file.getAbsolutePath());
            return ImageIO.read(new File(new WaitForSnapshot(this, file, width, height).await()));
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to get snapshot image", e);
        }
        catch(InterruptedException e) {
            throw new RuntimeException("Failed to get snapshot image", e);
        }
        catch(BeforeConditionAbortedException e) {
            logger.debug("Failed to take snapshot");
            return null;
        }
        finally {
            if(file != null) {
                boolean deleted = file.delete();
                logger.debug("deleted={}", deleted);
            }
        }
    }

    // === Logo Controls ========================================================

    @Override
    public void enableLogo(boolean enable) {
        logger.debug("enableLogo(enable={})", enable);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
    }

    @Override
    public void setLogoOpacity(int opacity) {
        logger.debug("setLogoOpacity(opacity={})", opacity);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
    }

    @Override
    public void setLogoOpacity(float opacity) {
        logger.debug("setLogoOpacity(opacity={})", opacity);
        int opacityValue = Math.round(opacity * 255.0f);
        logger.debug("opacityValue={}", opacityValue);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
    }

    @Override
    public void setLogoLocation(int x, int y) {
        logger.debug("setLogoLocation(x={},y={})", x, y);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
    }

    @Override
    public void setLogoPosition(libvlc_logo_position_e position) {
        logger.debug("setLogoPosition(position={})", position);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    }

    @Override
    public void setLogoFile(String logoFile) {
        logger.debug("setLogoFile(logoFile={})", logoFile);
        libvlc.libvlc_video_set_logo_string(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_file.intValue(), logoFile);
    }

    @Override
    public void setLogoImage(RenderedImage logoImage) {
        logger.debug("setLogoImage(logoImage={})", logoImage);
        File file = null;
        try {
            // Create a temporary file for the logo...
            file = File.createTempFile("vlcj-logo-", ".png");
            ImageIO.write(logoImage, "png", file);
            if (file.exists()) {
                // ...then set the logo as normal
                setLogoFile(file.getAbsolutePath());
                // Flag the temporary file to be deleted when the JVM exits - the file can not be
                // deleted immediately because setLogoFile ultimately invokes an asynchronous
                // native method to set the logo from the file
                file.deleteOnExit();
            }
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to set logo image", e);
        }
    }

    @Override
    public void setLogo(Logo logo) {
        logger.debug("setLogo(logo={})", logo);
        logo.apply(this);
    }

    // === Marquee Controls =====================================================

    @Override
    public void enableMarquee(boolean enable) {
        logger.debug("enableMarquee(enable={})", enable);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
    }

    @Override
    public void setMarqueeText(String text) {
        logger.debug("setMarqueeText(text={})", text);
        libvlc.libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
    }

    @Override
    public void setMarqueeColour(Color colour) {
        logger.debug("setMarqueeColour(colour={})", colour);
        setMarqueeColour(colour.getRGB() & 0x00ffffff);
    }

    @Override
    public void setMarqueeColour(int colour) {
        logger.debug("setMarqueeColour(colour={})", colour);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
    }

    @Override
    public void setMarqueeOpacity(int opacity) {
        logger.debug("setMarqueeOpacity(opacity={})", opacity);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
    }

    @Override
    public void setMarqueeOpacity(float opacity) {
        logger.debug("setMarqueeOpacity(opacity={})", opacity);
        int opacityValue = Math.round(opacity * 255.0f);
        logger.debug("opacityValue={}", opacityValue);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
    }

    @Override
    public void setMarqueeSize(int size) {
        logger.debug("setMarqueeSize(size={})", size);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
    }

    @Override
    public void setMarqueeTimeout(int timeout) {
        logger.debug("setMarqueeTimeout(timeout={})", timeout);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
    }

    @Override
    public void setMarqueeLocation(int x, int y) {
        logger.debug("setMarqueeLocation(x={},y={})", x, y);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_X.intValue(), x);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Y.intValue(), y);
    }

    @Override
    public void setMarqueePosition(libvlc_marquee_position_e position) {
        logger.debug("setMarqueePosition(position={})", position);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Position.intValue(), position.intValue());
    }

    @Override
    public void setMarquee(Marquee marquee) {
        logger.debug("setMarquee(marquee={})", marquee);
        marquee.apply(this);
    }

    // === Filter Controls ======================================================

    @Override
    public void setDeinterlace(DeinterlaceMode deinterlaceMode) {
        logger.debug("setDeinterlace(deinterlaceMode={})", deinterlaceMode);
        libvlc.libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlaceMode != null ? deinterlaceMode.mode() : null);
    }

    // === Video Adjustment Controls ============================================

    @Override
    public void setAdjustVideo(boolean adjustVideo) {
        logger.debug("setAdjustVideo(adjustVideo={})", adjustVideo);
        libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue(), adjustVideo ? 1 : 0);
    }

    @Override
    public boolean isAdjustVideo() {
        logger.debug("isAdjustVideo()");
        return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue()) == 1;
    }

    @Override
    public float getContrast() {
        logger.debug("getContrast()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
    }

    @Override
    public void setContrast(float contrast) {
        logger.debug("setContrast(contrast={})", contrast);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue(), contrast);
    }

    @Override
    public float getBrightness() {
        logger.debug("getBrightness()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
    }

    @Override
    public void setBrightness(float brightness) {
        logger.debug("setBrightness(brightness={})", brightness);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue(), brightness);
    }

    @Override
    public int getHue() {
        logger.debug("getHue()");
        return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
    }

    @Override
    public void setHue(int hue) {
        logger.debug("setHue(hue={})", hue);
        libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue(), hue);
    }

    @Override
    public float getSaturation() {
        logger.debug("getSaturation()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
    }

    @Override
    public void setSaturation(float saturation) {
        logger.debug("setSaturation(saturation={})", saturation);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
    }

    @Override
    public float getGamma() {
        logger.debug("getGamma()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
    }

    @Override
    public void setGamma(float gamma) {
        logger.debug("setGamma(gamma={})", gamma);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
    }

    // === Video Title Controls =================================================

    @Override
    public void setVideoTitleDisplay(libvlc_position_e position, int timeout) {
        logger.debug("setVideoTitleDisplay(position={},timeout={})", position, timeout);
        libvlc.libvlc_media_player_set_video_title_display(mediaPlayerInstance, position.intValue(), timeout);
    }

    // === Audio Equalizer Controls =============================================

    @Override
    public Equalizer getEqualizer() {
        logger.debug("getEqualizer()");
        return equalizer;
    }

    @Override
    public void setEqualizer(Equalizer equalizer) {
        logger.debug("setEqualizer(equalizer={})", equalizer);
        if(this.equalizer != null) {
            this.equalizer.removeEqualizerListener(this);
            libvlc.libvlc_audio_equalizer_release(equalizerInstance);
            equalizerInstance = null;
        }
        this.equalizer = equalizer;
        if(this.equalizer != null) {
            equalizerInstance = libvlc.libvlc_audio_equalizer_new();
            this.equalizer.addEqualizerListener(this);
        }
        applyEqualizer();
    }

    /**
     * Apply the audio equalizer settings to the native media player.
     */
    private void applyEqualizer() {
        logger.trace("applyEqualizer()");
        logger.trace("equalizerInstance={}", equalizerInstance);
        if(equalizerInstance != null) {
            logger.trace("Set equalizer");
            libvlc.libvlc_audio_equalizer_set_preamp(equalizerInstance, equalizer.getPreamp());
            for(int i = 0; i < libvlc.libvlc_audio_equalizer_get_band_count(); i ++ ) {
                libvlc.libvlc_audio_equalizer_set_amp_at_index(equalizerInstance, equalizer.getAmp(i), i);
            }
            libvlc.libvlc_media_player_set_equalizer(mediaPlayerInstance, equalizerInstance);
        }
        else {
            logger.trace("Disable equalizer");
            libvlc.libvlc_media_player_set_equalizer(mediaPlayerInstance, null);
        }
    }

    // === Media Player Role ====================================================

    @Override
    public libvlc_media_player_role_e getRole() {
        logger.debug("getRole()");
        return libvlc_media_player_role_e.role(libvlc.libvlc_media_player_get_role(mediaPlayerInstance));
    }

    @Override
    public void setRole(libvlc_media_player_role_e role) {
        logger.debug("setRole(role={})", role);
        libvlc.libvlc_media_player_set_role(mediaPlayerInstance, role.intValue());
    }

    // === Implementation =======================================================

    @Override
    public String mrl() {
        logger.debug("mrl()");
        if(mediaInstance != null) {
            return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
        }
        else {
            return null;
        }
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
    public final void release() {
        logger.debug("release()");
        if(released.compareAndSet(false, true)) {
            destroyInstance();
            onAfterRelease();
        }
    }

    @Override
    public final libvlc_media_player_t mediaPlayerInstance() {
        return mediaPlayerInstance;
    }

    /**
     * Allow sub-classes to do something just before the video is started.
     */
    protected void onBeforePlay() {
        // Base implementation does nothing
    }

    /**
     * Allow sub-classes to clean-up.
     */
    protected void onAfterRelease() {
        // Base implementation does nothing
    }

    /**
     * Create and prepare the native media player resources.
     */
    private void createInstance() {
        logger.debug("createInstance()");

        mediaPlayerInstance = libvlc.libvlc_media_player_new(instance);
        logger.debug("mediaPlayerInstance={}", mediaPlayerInstance);

        mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance);
        logger.debug("mediaPlayerEventManager={}", mediaPlayerEventManager);

        registerEventListener();

        // The order these handlers execute in is important for proper operation
        eventListenerList.add(new NewMediaEventHandler());
        eventListenerList.add(new RepeatPlayEventHandler());
        eventListenerList.add(new SubItemEventHandler());
        eventListenerList.add(new ResetMediaHandler());
    }

    /**
     * Clean up the native media player resources.
     */
    private void destroyInstance() {
        logger.debug("destroyInstance()");

        logger.debug("Detach media events...");
        deregisterMediaEventListener();
        logger.debug("Media events detached.");

        if(mediaInstance != null) {
            logger.debug("Release media...");
            libvlc.libvlc_media_release(mediaInstance);
            logger.debug("Media released.");
        }

        logger.debug("Detach media player events...");
        deregisterEventListener();
        logger.debug("Media player events detached.");

        eventListenerList.clear();

        if(mediaPlayerInstance != null) {
            logger.debug("Release media player...");
            libvlc.libvlc_media_player_release(mediaPlayerInstance);
            logger.debug("Media player released.");
        }

        if(equalizer != null) {
            equalizer.removeEqualizerListener(this);
            equalizer = null;
        }

        if(equalizerInstance != null) {
            libvlc.libvlc_audio_equalizer_release(equalizerInstance);
            equalizerInstance = null;
        }

        logger.debug("Shut down listeners...");
        listenersService.shutdown();
        logger.debug("Listeners shut down.");
    }

    /**
     * Register a call-back to receive native media player events.
     */
    private void registerEventListener() {
        logger.debug("registerEventListener()");
        callback = new EventCallback();
        libvlc_event_e lastKnownEvent = lastKnownEvent();
        for(libvlc_event_e event : libvlc_event_e.values()) {
            if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= lastKnownEvent.intValue()) {
                logger.debug("event={}", event);
                int result = libvlc.libvlc_event_attach(mediaPlayerEventManager, event.intValue(), callback, null);
                logger.debug("result={}", result);
            }
        }
    }

    /**
     * De-register the call-back used to receive native media player events.
     */
    private void deregisterEventListener() {
        logger.debug("deregisterEventListener()");
        if(callback != null) {
            libvlc_event_e lastKnownEvent = lastKnownEvent();
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= lastKnownEvent.intValue()) {
                    logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaPlayerEventManager, event.intValue(), callback, null);
                }
            }
            callback = null;
        }
    }

    /**
     * Get the last known event type supported by the run-time native event manager.
     * <p>
     * This is required to support earlier than LibVLC 2.2.0, and can be removed when such support
     * is no longer required.
     *
     * @return event type
     */
    private libvlc_event_e lastKnownEvent() {
        libvlc_event_e result;
        Version version = new Version(libvlc.libvlc_get_version());
        if(version.atLeast(new Version("3.0.0"))) {
            result = libvlc_event_e.libvlc_MediaPlayerChapterChanged;
        }
        else if(version.atLeast(new Version("2.2.0"))) {
            result = libvlc_event_e.libvlc_MediaPlayerScrambledChanged;
        }
        else {
            result = libvlc_event_e.libvlc_MediaPlayerVout;
        }
        return result;
    }

    /**
     * Register a call-back to receive media native events.
     */
    private void registerMediaEventListener() {
        logger.debug("registerMediaEventListener()");
        // If there is a media, register a new listener...
        if(mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            libvlc_event_e lastKnownMediaEvent = lastKnownMediaEvent();
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= lastKnownMediaEvent.intValue()) {
                    logger.debug("event={}", event);
                    int result = libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                    logger.debug("result={}", result);
                }
            }
        }
    }

    /**
     * De-register the call-back used to receive native media events.
     */
    private void deregisterMediaEventListener() {
        logger.debug("deregisterMediaEventListener()");
        // If there is a media, deregister the listener...
        if(mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            libvlc_event_e lastKnownMediaEvent = lastKnownMediaEvent();
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= lastKnownMediaEvent.intValue()) {
                    logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    /**
     * Get the last known media event type supported by the run-time native event manager.
     * <p>
     * This is required to support earlier than LibVLC 2.2.0, and can be removed when such support
     * is no longer required.
     *
     * @return event type
     */
    private libvlc_event_e lastKnownMediaEvent() {
        libvlc_event_e result;
        Version version = new Version(libvlc.libvlc_get_version());
        if(version.atLeast(new Version("3.0.0"))) {
            result = libvlc_event_e.libvlc_MediaParsedStatus;
        }
        else if(version.atLeast(new Version("2.1.5"))) {
            result = libvlc_event_e.libvlc_MediaSubItemTreeAdded;
        }
        else {
            result = libvlc_event_e.libvlc_MediaStateChanged;
        }
        return result;
    }

    /**
     * Raise an event.
     *
     * @param mediaPlayerEvent event to raise, may be <code>null</code>
     */
    private void raiseEvent(MediaPlayerEvent mediaPlayerEvent) {
        logger.trace("raiseEvent(mediaPlayerEvent={}", mediaPlayerEvent);
        if(mediaPlayerEvent != null) {
            listenersService.submit(new NotifyEventListenersRunnable(mediaPlayerEvent));
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
        logger.debug("setMedia(media={})", media);
        // Remember the the media and options for possible replay later (also to keep the media
        // instance pinned to prevent it from being garbage collected - critical when using the
        // native media callbacks)
        this.lastPlayedMedia = media;
        // If there is a current media, clean it up
        if(mediaInstance != null) {
            // Release the media event listener
            deregisterMediaEventListener();
            // Release the native resource
            libvlc.libvlc_media_release(mediaInstance);
            mediaInstance = null;
        }
        // Reset sub-items
        subItemIndex = -1;
        // Create the native media handle for the given media
        mediaInstance = createMediaInstance(media);
        logger.debug("mediaInstance={}", mediaInstance);
        if(mediaInstance != null) {
            // Set the standard media options (if any)...
            if(standardMediaOptions != null) {
                for(String standardMediaOption : standardMediaOptions) {
                    logger.debug("standardMediaOption={}", standardMediaOption);
                    libvlc.libvlc_media_add_option(mediaInstance, standardMediaOption);
                }
            }
            // Set the particular media options (if any)...
            if(media.mediaOptions() != null) {
                for(String mediaOption : media.mediaOptions()) {
                    logger.debug("mediaOption={}", mediaOption);
                    libvlc.libvlc_media_add_option(mediaInstance, mediaOption);
                }
            }
            // Attach a listener to the new media
            registerMediaEventListener();
            // Set the new media on the media player
            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaInstance);
        }
        else {
            logger.error("Failed to create native media resource for '{}'", media);
        }
        // Prepare a new statistics object to re-use for the new media item
        libvlcMediaStats = new libvlc_media_stats_t();
        boolean result = mediaInstance != null;
        logger.debug("result={}", result);
        return result;
    }

    /**
     * Create a native media handle for the given media.
     *
     * @param media media
     * @return native media handle
     */
    private libvlc_media_t createMediaInstance(Media media) {
        logger.debug("createMediaInstance(media={})", media);
        // Create new media...
        libvlc_media_t result;
        if (media instanceof SimpleMedia) {
            String mrl = ((SimpleMedia) media).mrl();
            if(MediaResourceLocator.isLocation(mrl)) {
                logger.debug("Treating mrl as a location");
                result = libvlc.libvlc_media_new_location(instance, mrl);
            }
            else {
                logger.debug("Treating mrl as a path");
                result = libvlc.libvlc_media_new_path(instance, mrl);
            }
        }
        else if (media instanceof CallbackMedia) {
            CallbackMedia callbackMedia = (CallbackMedia)media;
            result = libvlc.libvlc_media_new_callbacks(instance,
                callbackMedia.getOpen(),
                callbackMedia.getRead(),
                callbackMedia.getSeek(),
                callbackMedia.getClose(),
                callbackMedia.getOpaque()
            );
        }
        else {
            throw new IllegalStateException("Don't know about media type " + media);
        }
        return result;
    }

    /**
     * Handle sub-items.
     * <p>
     * This method contains the common code that is required when iterating over the media sub-items
     * - the sub-items are obtained from the media player, the list is locked, the sub-items are
     * processed by a {@link SubItemsHandler} implementation, then the list is unlocked and
     * released.
     *
     * @param <T> type of result
     * @param subItemsHandler handler implementation
     * @return result, will never be <code>null</code>
     * @throws IllegalStateException if no current media
     */
    private <T> T handleSubItems(SubItemsHandler<T> subItemsHandler) {
        logger.debug("handleSubItems()");
        libvlc_media_list_t subItemList = null;
        try {
            if(mediaInstance != null) {
                // Get the list of sub-items
                subItemList = libvlc.libvlc_media_subitems(mediaInstance);
                logger.debug("subItemList={}", subItemList);
                if(subItemList != null) {
                    // Lock the sub-item list
                    libvlc.libvlc_media_list_lock(subItemList);
                }
                // Invoke the handler
                return subItemsHandler.subItems(subItemList != null ? libvlc.libvlc_media_list_count(subItemList) : 0, subItemList);
            }
            else {
                throw new IllegalStateException("No media");
            }
        }
        finally {
            if(subItemList != null) {
                libvlc.libvlc_media_list_unlock(subItemList);
                libvlc.libvlc_media_list_release(subItemList);
            }
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
    private final class EventCallback implements libvlc_callback_t {
        @Override
        public void callback(libvlc_event_t event, Pointer userData) {
            logger.trace("callback(event={},userData={})", event, userData);
            // Create a new media player event for the native event - due to internal implementation
            // details the event listener list is never empty so it is redundant to check that here
            MediaPlayerEvent mediaPlayerEvent = eventFactory.createEvent(event, eventMask);
            if(event != null) {
                raiseEvent(mediaPlayerEvent);
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
        private final MediaPlayerEvent mediaPlayerEvent;

        /**
         * Create a runnable.
         *
         * @param mediaPlayerEvent event to notify
         */
        private NotifyEventListenersRunnable(MediaPlayerEvent mediaPlayerEvent) {
            this.mediaPlayerEvent = mediaPlayerEvent;
        }

        @Override
        public void run() {
            logger.trace("run()");
            for(int i = eventListenerList.size() - 1; i >= 0; i -- ) {
                MediaPlayerEventListener listener = eventListenerList.get(i);
                try {
                    mediaPlayerEvent.notify(listener);
                }
                catch(Exception e) {
                    logger.warn("Event listener {} threw an exception {}", listener, e.getMessage());
                    // Continue with the next listener...
                }
            }
            logger.trace("runnable exits");
        }
    }

    /**
     * Event listener implementation that handles a new item being played.
     * <p>
     * This is not for sub-items.
     */
    private final class NewMediaEventHandler extends MediaPlayerEventAdapter {

        @Override
        public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
            logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})", mediaPlayer, media, mrl);
            // If this is not a sub-item...
            if(subItemIndex() == -1) {
                // Raise a semantic event to announce the media was changed
                logger.debug("Raising event for new media");
                raiseEvent(eventFactory.createMediaNewEvent(eventMask));
            }
        }
    }

    /**
     * Event listener implementation that handles auto-repeat.
     */
    private final class RepeatPlayEventHandler extends MediaPlayerEventAdapter {

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            logger.debug("finished(mediaPlayer={})", mediaPlayer);
            if(repeat && mediaInstance != null) {
                int subItemCount = subItemCount();
                logger.debug("subitemCount={}", subItemCount);
                if(subItemCount == 0) {
                    String mrl = NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
                    logger.debug("auto repeat mrl={}", mrl);
                    // It is not sufficient to simply call play(), the MRL must explicitly
                    // be played again - this is the reason why the repeat play might not
                    // be seamless
                    mediaPlayer.playMedia(mrl, lastPlayedMedia.mediaOptions());
                }
                else {
                    logger.debug("Sub-items handling repeat");
                }
            }
            else {
                logger.debug("No repeat");
            }
        }
    }

    /**
     * Event listener implementation that handles media sub-items.
     * <p>
     * Some media types when you 'play' them do not actually play any media and instead sub-items
     * are created and attached to the current media descriptor.
     * <p>
     * This event listener responds to the media player "finished" event by getting the current
     * media from the player and automatically playing the first sub-item (if there is one).
     * <p>
     * If there is more than one sub-item, then they will simply be played in order, and repeated
     * depending on the value of the "repeat" property.
     */
    private final class SubItemEventHandler extends MediaPlayerEventAdapter {
        @Override
        public void finished(MediaPlayer mediaPlayer) {
            logger.debug("finished(mediaPlayer={})", mediaPlayer);
            // If a sub-item being played...
            if(subItemIndex != -1) {
                // Raise a semantic event to announce the sub-item was finished
                logger.debug("Raising finished event for sub-item {}", subItemIndex);
                raiseEvent(eventFactory.createMediaSubItemFinishedEvent(subItemIndex, eventMask));
            }
            // If set to automatically play sub-items...
            if(playSubItems) {
                // ...play the next sub-item
                playNextSubItem();
            }
        }
    }

    /**
     * Event listener implementation that resets the media ready for replay after is has finished.
     * <p>
     * This enables an application to invoke play() after the media is finished.
     * <p>
     * Without this, an application must invoke stop(), then play().
     */
    private final class ResetMediaHandler extends MediaPlayerEventAdapter {
        @Override
        public void finished(MediaPlayer mediaPlayer) {
            if (subItemCount() == 0) {
                resetMedia();
            }
        }
    }

    /**
     * Reset the media so it can be replayed.
     */
    private void resetMedia() {
        logger.debug("resetMedia()");
        setMedia(lastPlayedMedia);
    }

    /**
     * Specification for a component that handles media list sub-items.
     *
     * @param <T> desired result type
     */
    private interface SubItemsHandler<T> {

        /**
         * Handle sub-items.
         *
         * @param count number of sub-items in the list, will always be zero or greater
         * @param subItems sub-item list, may be <code>null</code>
         * @return result of processing the sub-items
         */
        T subItems(int count, libvlc_media_list_t subItems);
    }

    // === EqualizerListener ====================================================

    @Override
    public final void equalizerChanged(Equalizer equalizer) {
        logger.trace("equalizerChanged(equalizer={})", equalizer);
        applyEqualizer();
    }
}
