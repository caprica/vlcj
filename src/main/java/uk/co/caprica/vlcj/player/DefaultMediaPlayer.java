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

package uk.co.caprica.vlcj.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_callback_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_manager_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_event_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_list_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_info_audio_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_info_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_info_video_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_navigate_mode_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_subtitle_track_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_description_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_track_type_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_adjust_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_marquee_option_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_track_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.events.MediaPlayerEvent;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventFactory;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Media player implementation.
 */
public abstract class DefaultMediaPlayer extends AbstractMediaPlayer implements MediaPlayer {

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
    private int eventMask = MediaPlayerEventType.ALL.value();

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
     * Opaque reference to user/application-specific data associated with this media player.
     */
    private Object userData;

    /**
     * Set to true when the player has been released.
     */
    private final AtomicBoolean released = new AtomicBoolean();

    /**
     * Create a new media player.
     *
     * @param libvlc native library interface
     * @param instance libvlc instance
     */
    public DefaultMediaPlayer(LibVlc libvlc, libvlc_instance_t instance) {
        super(libvlc, instance);
        Logger.debug("DefaultMediaPlayer(libvlc={}, instance={})", libvlc, instance);
        createInstance();
    }

    @Override
    public void addMediaPlayerEventListener(MediaPlayerEventListener listener) {
        Logger.debug("addMediaPlayerEventListener(listener={})", listener);
        eventListenerList.add(listener);
    }

    @Override
    public void removeMediaPlayerEventListener(MediaPlayerEventListener listener) {
        Logger.debug("removeMediaPlayerEventListener(listener={})", listener);
        eventListenerList.remove(listener);
    }

    @Override
    public void enableEvents(int eventMask) {
        Logger.debug("enableEvents(eventMask={})", eventMask);
        this.eventMask = eventMask;
    }

    // === Media Controls =======================================================

    @Override
    public void setStandardMediaOptions(String... options) {
        Logger.debug("setStandardMediaOptions(options={})", Arrays.toString(options));
        this.standardMediaOptions = options;
    }

    @Override
    public boolean playMedia(String mrl, String... mediaOptions) {
        Logger.debug("playMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        // First 'prepare' the media...
        if(prepareMedia(mrl, mediaOptions)) {
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
        Logger.debug("prepareMedia(mrl={},mediaOptions={})", mrl, Arrays.toString(mediaOptions));
        return setMedia(mrl, mediaOptions);
    }

    @Override
    public boolean startMedia(String mrl, String... mediaOptions) {
        Logger.debug("startMedia(mrl={}, mediaOptions)", mrl, Arrays.toString(mediaOptions));
        // First 'prepare' the media...
        if(prepareMedia(mrl, mediaOptions)) {
            // ...then play it and wait for it to start (or error)
            return new MediaPlayerLatch(this).play();
        }
        else {
            return false;
        }
    }

    @Override
    public void parseMedia() {
        Logger.debug("parseMedia()");
        if(mediaInstance != null) {
            libvlc.libvlc_media_parse(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public void requestParseMedia() {
        Logger.debug("requestParseMedia()");
        if(mediaInstance != null) {
            libvlc.libvlc_media_parse_async(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public boolean isMediaParsed() {
        Logger.debug("isMediaParsed()");
        if(mediaInstance != null) {
            return 0 != libvlc.libvlc_media_is_parsed(mediaInstance);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public MediaMeta getMediaMeta() {
        Logger.debug("getMediaMeta()");
        return getMediaMeta(mediaInstance);
    }

    @Override
    public MediaMeta getMediaMeta(libvlc_media_t media) {
        Logger.debug("getMediaMeta(media={})", media);
        if(media != null) {
            return new DefaultMediaMeta(libvlc, media);
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public List<MediaMeta> getSubItemMediaMeta() {
        Logger.debug("getSubItemMediaMeta()");
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
    public void addMediaOptions(String... mediaOptions) {
        Logger.debug("addMediaOptions(mediaOptions={})", Arrays.toString(mediaOptions));
        if(mediaInstance != null) {
            for(String mediaOption : mediaOptions) {
                Logger.debug("mediaOption={}", mediaOption);
                libvlc.libvlc_media_add_option(mediaInstance, mediaOption);
            }
        }
        else {
            throw new IllegalStateException("No media");
        }
    }

    @Override
    public void setRepeat(boolean repeat) {
        Logger.debug("setRepeat(repeat={})", repeat);
        this.repeat = repeat;
    }

    @Override
    public boolean getRepeat() {
        Logger.debug("getRepeat()");
        return repeat;
    }

    // === Sub-Item Controls ====================================================

    @Override
    public void setPlaySubItems(boolean playSubItems) {
        Logger.debug("setPlaySubItems(playSubItems={})", playSubItems);
        this.playSubItems = playSubItems;
    }

    @Override
    public int subItemCount() {
        Logger.debug("subItemCount()");
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
        Logger.debug("subItems()");
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
        Logger.debug("subItemsMedia()");
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
        Logger.debug("subItemsMediaList()");
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
        Logger.debug("playNextSubItem(mediaOptions={})", Arrays.toString(mediaOptions));
        return playSubItem(subItemIndex + 1, mediaOptions);
    }

    @Override
    public boolean playSubItem(final int index, final String... mediaOptions) {
        Logger.debug("playSubItem(index={},mediaOptions={})", index, Arrays.toString(mediaOptions));
        return handleSubItems(new SubItemsHandler<Boolean>() {
            @Override
            public Boolean subItems(int count, libvlc_media_list_t subItems) {
                if(subItems != null) {
                    Logger.debug("Handling media sub-item...");
                    // Advance the current sub-item (initially it will be -1)...
                    Logger.debug("count={}", count);
                    subItemIndex = index;
                    Logger.debug("subItemIndex={}", subItemIndex);
                    // If the last sub-item already been played...
                    if(subItemIndex >= count) {
                        Logger.debug("End of sub-items reached");
                        if(!repeat) {
                            Logger.debug("Do not repeat sub-items");
                            subItemIndex = -1;
                            Logger.debug("Raising events for end of sub-items");
                            raiseEvent(eventFactory.createMediaEndOfSubItemsEvent(eventMask));
                        }
                        else {
                            Logger.debug("Repeating sub-items");
                            subItemIndex = 0;
                        }
                    }
                    if(subItemIndex != -1) {
                        // Get the required sub item from the list
                        libvlc_media_t subItem = libvlc.libvlc_media_list_item_at_index(subItems, subItemIndex);
                        // If there is an item to play...
                        if(subItem != null) {
                            // Set the sub-item as the new media for the media player
                            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, subItem);
                            // Set any standard media options
                            if(standardMediaOptions != null) {
                                for(String standardMediaOption : standardMediaOptions) {
                                    Logger.debug("standardMediaOption={}", standardMediaOption);
                                    libvlc.libvlc_media_add_option(subItem, standardMediaOption);
                                }
                            }
                            // Set any media options
                            if(mediaOptions != null) {
                                for(String mediaOption : mediaOptions) {
                                    Logger.debug("mediaOption={}", mediaOption);
                                    libvlc.libvlc_media_add_option(subItem, mediaOption);
                                }
                            }
                            // Play the media
                            libvlc.libvlc_media_player_play(mediaPlayerInstance);
                            // Release the sub-item
                            libvlc.libvlc_media_release(subItem);
                            // Raise a semantic event to announce the sub-item was played
                            Logger.debug("Raising played event for sub-item {}", subItemIndex);
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
        Logger.trace("isPlayable()");
        return libvlc.libvlc_media_player_will_play(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean isPlaying() {
        Logger.trace("isPlaying()");
        return libvlc.libvlc_media_player_is_playing(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean isSeekable() {
        Logger.trace("isSeekable()");
        return libvlc.libvlc_media_player_is_seekable(mediaPlayerInstance) == 1;
    }

    @Override
    public boolean canPause() {
        Logger.trace("canPause()");
        return libvlc.libvlc_media_player_can_pause(mediaPlayerInstance) == 1;
    }

    @Override
    public long getLength() {
        Logger.trace("getLength()");
        return libvlc.libvlc_media_player_get_length(mediaPlayerInstance);
    }

    @Override
    public long getTime() {
        Logger.trace("getTime()");
        return libvlc.libvlc_media_player_get_time(mediaPlayerInstance);
    }

    @Override
    public float getPosition() {
        Logger.trace("getPosition()");
        return libvlc.libvlc_media_player_get_position(mediaPlayerInstance);
    }

    @Override
    public float getFps() {
        Logger.trace("getFps()");
        return libvlc.libvlc_media_player_get_fps(mediaPlayerInstance);
    }

    @Override
    public float getRate() {
        Logger.trace("getRate()");
        return libvlc.libvlc_media_player_get_rate(mediaPlayerInstance);
    }

    @Override
    public int getVideoOutputs() {
        Logger.trace("getVideoOutputs()");
        return libvlc.libvlc_media_player_has_vout(mediaPlayerInstance);
    }

    @Override
    public Dimension getVideoDimension() {
        Logger.debug("getVideoDimension()");
        if(getVideoOutputs() > 0) {
            IntByReference px = new IntByReference();
            IntByReference py = new IntByReference();
            int result = libvlc.libvlc_video_get_size(mediaPlayerInstance, 0, px, py);
            if(result == 0) {
                return new Dimension(px.getValue(), py.getValue());
            }
            else {
                Logger.warn("Video size is not available");
                return null;
            }
        }
        else {
            Logger.warn("Can't get video dimension if no video output has been started");
            return null;
        }
    }

    @Override
    public MediaDetails getMediaDetails() {
        Logger.debug("getMediaDetails()");
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
            Logger.warn("Can't get media meta data if media is not playing");
            return null;
        }
    }

    @Override
    public String getAspectRatio() {
        Logger.debug("getAspectRatio()");
        return NativeString.getNativeString(libvlc, libvlc.libvlc_video_get_aspect_ratio(mediaPlayerInstance));
    }

    @Override
    public float getScale() {
        Logger.debug("getScale()");
        return libvlc.libvlc_video_get_scale(mediaPlayerInstance);
    }

    @Override
    public String getCropGeometry() {
        Logger.debug("getCropGeometry()");
        return NativeString.getNativeString(libvlc, libvlc.libvlc_video_get_crop_geometry(mediaPlayerInstance));
    }

    @Override
    public libvlc_media_stats_t getMediaStatistics() {
        Logger.trace("getMediaStatistics()");
        return getMediaStatistics(mediaInstance);
    }

    @Override
    public libvlc_media_stats_t getMediaStatistics(libvlc_media_t media) {
        Logger.trace("getMediaStatistics(media={})", media);
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
        Logger.debug("getMediaState()");
        libvlc_state_t state = null;
        if(mediaInstance != null) {
            state = libvlc_state_t.state(libvlc.libvlc_media_get_state(mediaInstance));
        }
        return state;
    }

    // FIXME do not return the native structure, should be a Java enum
    @Override
    public libvlc_state_t getMediaPlayerState() {
        Logger.debug("getMediaPlayerState()");
        return libvlc_state_t.state(libvlc.libvlc_media_player_get_state(mediaPlayerInstance));
    }

    // === Title/Track Controls =================================================

    @Override
    public int getTitleCount() {
        Logger.debug("getTitleCount()");
        return libvlc.libvlc_media_player_get_title_count(mediaPlayerInstance);
    }

    @Override
    public int getTitle() {
        Logger.debug("getTitle()");
        return libvlc.libvlc_media_player_get_title(mediaPlayerInstance);
    }

    @Override
    public void setTitle(int title) {
        Logger.debug("setTitle(title={})", title);
        libvlc.libvlc_media_player_set_title(mediaPlayerInstance, title);
    }

    @Override
    public int getVideoTrackCount() {
        Logger.debug("getVideoTrackCount()");
        return libvlc.libvlc_video_get_track_count(mediaPlayerInstance);
    }

    @Override
    public int getVideoTrack() {
        Logger.debug("getVideoTrack()");
        return libvlc.libvlc_video_get_track(mediaPlayerInstance);
    }

    @Override
    public int setVideoTrack(int track) {
        Logger.debug("setVideoTrack(track={})", track);
        libvlc.libvlc_video_set_track(mediaPlayerInstance, track);
        return getVideoTrack();
    }

    @Override
    public int getAudioTrackCount() {
        Logger.debug("getVideoTrackCount()");
        return libvlc.libvlc_audio_get_track_count(mediaPlayerInstance);
    }

    @Override
    public int getAudioTrack() {
        Logger.debug("getAudioTrack()");
        return libvlc.libvlc_audio_get_track(mediaPlayerInstance);
    }

    @Override
    public int setAudioTrack(int track) {
        Logger.debug("setAudioTrack(track={})", track);
        libvlc.libvlc_audio_set_track(mediaPlayerInstance, track);
        return getAudioTrack();
    }

    // === Basic Playback Controls ==============================================

    @Override
    public void play() {
        Logger.debug("play()");
        onBeforePlay();
        libvlc.libvlc_media_player_play(mediaPlayerInstance);
        Logger.debug("after play");
    }

    @Override
    public boolean start() {
        return new MediaPlayerLatch(this).play();
    }

    @Override
    public void stop() {
        Logger.debug("stop()");
        libvlc.libvlc_media_player_stop(mediaPlayerInstance);
    }

    @Override
    public void setPause(boolean pause) {
        Logger.debug("setPause(pause={})", pause);
        libvlc.libvlc_media_player_set_pause(mediaPlayerInstance, pause ? 1 : 0);
    }

    @Override
    public void pause() {
        Logger.debug("pause()");
        libvlc.libvlc_media_player_pause(mediaPlayerInstance);
    }

    @Override
    public void nextFrame() {
        Logger.debug("nextFrame()");
        libvlc.libvlc_media_player_next_frame(mediaPlayerInstance);
    }

    @Override
    public void skip(long delta) {
        Logger.debug("skip(delta={})", delta);
        long current = getTime();
        Logger.debug("current={}", current);
        if(current != -1) {
            setTime(current + delta);
        }
    }

    @Override
    public void skipPosition(float delta) {
        Logger.debug("skipPosition(delta={})", delta);
        float current = getPosition();
        Logger.debug("current={}", current);
        if(current != -1) {
            setPosition(current + delta);
        }
    }

    @Override
    public void setTime(long time) {
        Logger.debug("setTime(time={})", time);
        libvlc.libvlc_media_player_set_time(mediaPlayerInstance, time);
    }

    @Override
    public void setPosition(float position) {
        Logger.debug("setPosition(position={})", position);
        libvlc.libvlc_media_player_set_position(mediaPlayerInstance, position);
    }

    @Override
    public int setRate(float rate) {
        Logger.debug("setRate(rate={})", rate);
        return libvlc.libvlc_media_player_set_rate(mediaPlayerInstance, rate);
    }

    @Override
    public void setAspectRatio(String aspectRatio) {
        Logger.debug("setAspectRatio(aspectRatio={})", aspectRatio);
        libvlc.libvlc_video_set_aspect_ratio(mediaPlayerInstance, aspectRatio);
    }

    @Override
    public void setScale(float factor) {
        Logger.debug("setScale(factor={})", factor);
        libvlc.libvlc_video_set_scale(mediaPlayerInstance, factor);
    }

    @Override
    public void setCropGeometry(String cropGeometry) {
        Logger.debug("setCropGeometry(cropGeometry={})", cropGeometry);
        libvlc.libvlc_video_set_crop_geometry(mediaPlayerInstance, cropGeometry);
    }

    // === Audio Controls =======================================================

    @Override
    public boolean setAudioOutput(String output) {
        Logger.debug("setAudioOutput(output={})", output);
        return 0 == libvlc.libvlc_audio_output_set(mediaPlayerInstance, output);
    }

    @Override
    public void setAudioOutputDevice(String output, String outputDeviceId) {
        Logger.debug("setAudioOutputDevice(output={},outputDeviceId={})", output, outputDeviceId);
        libvlc.libvlc_audio_output_device_set(mediaPlayerInstance, output, outputDeviceId);
    }

    @Override
    public void setAudioOutputDeviceType(AudioOutputDeviceType deviceType) {
        Logger.debug("setAudioOutputDeviceType(deviceType={})");
        libvlc.libvlc_audio_output_set_device_type(mediaPlayerInstance, deviceType.intValue());
    }

    @Override
    public AudioOutputDeviceType getAudioOutputDeviceType() {
        Logger.debug("getAudioOutputDeviceType()");
        return AudioOutputDeviceType.valueOf(libvlc.libvlc_audio_output_get_device_type(mediaPlayerInstance));
    }

    @Override
    public void mute() {
        Logger.debug("mute()");
        libvlc.libvlc_audio_toggle_mute(mediaPlayerInstance);
    }

    @Override
    public void mute(boolean mute) {
        Logger.debug("mute(mute={})", mute);
        libvlc.libvlc_audio_set_mute(mediaPlayerInstance, mute ? 1 : 0);
    }

    @Override
    public boolean isMute() {
        Logger.debug("isMute()");
        return libvlc.libvlc_audio_get_mute(mediaPlayerInstance) != 0;
    }

    @Override
    public int getVolume() {
        Logger.debug("getVolume()");
        return libvlc.libvlc_audio_get_volume(mediaPlayerInstance);
    }

    @Override
    public void setVolume(int volume) {
        Logger.debug("setVolume(volume={})", volume);
        libvlc.libvlc_audio_set_volume(mediaPlayerInstance, volume);
    }

    @Override
    public int getAudioChannel() {
        Logger.debug("getAudioChannel()");
        return libvlc.libvlc_audio_get_channel(mediaPlayerInstance);
    }

    @Override
    public void setAudioChannel(int channel) {
        Logger.debug("setAudioChannel(channel={})", channel);
        libvlc.libvlc_audio_set_channel(mediaPlayerInstance, channel);
    }

    @Override
    public long getAudioDelay() {
        Logger.debug("getAudioDelay()");
        return libvlc.libvlc_audio_get_delay(mediaPlayerInstance);
    }

    @Override
    public void setAudioDelay(long delay) {
        Logger.debug("setAudioDelay(delay={})", delay);
        libvlc.libvlc_audio_set_delay(mediaPlayerInstance, delay);
    }

    // === Chapter Controls =====================================================

    @Override
    public int getChapterCount() {
        Logger.trace("getChapterCount()");
        return libvlc.libvlc_media_player_get_chapter_count(mediaPlayerInstance);
    }

    @Override
    public int getChapter() {
        Logger.trace("getChapter()");
        return libvlc.libvlc_media_player_get_chapter(mediaPlayerInstance);
    }

    @Override
    public void setChapter(int chapterNumber) {
        Logger.debug("setChapter(chapterNumber={})", chapterNumber);
        libvlc.libvlc_media_player_set_chapter(mediaPlayerInstance, chapterNumber);
    }

    @Override
    public void nextChapter() {
        Logger.debug("nextChapter()");
        libvlc.libvlc_media_player_next_chapter(mediaPlayerInstance);
    }

    @Override
    public void previousChapter() {
        Logger.debug("previousChapter()");
        libvlc.libvlc_media_player_previous_chapter(mediaPlayerInstance);
    }

    // === DVD Menu Navigation Controls =========================================

    @Override
    public void menuActivate() {
        Logger.debug("menuActivate()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_activate.intValue());
    }

    @Override
    public void menuUp() {
        Logger.debug("menuUp()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_up.intValue());
    }

    @Override
    public void menuDown() {
        Logger.debug("menuDown()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_down.intValue());
    }

    @Override
    public void menuLeft() {
        Logger.debug("menuLeft()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_left.intValue());
    }

    @Override
    public void menuRight() {
        Logger.debug("menuRight()");
        libvlc.libvlc_media_player_navigate(mediaPlayerInstance, libvlc_navigate_mode_e.libvlc_navigate_right.intValue());
    }

    // === Sub-Picture/Sub-Title Controls =======================================

    @Override
    public int getSpuCount() {
        Logger.debug("getSpuCount()");
        return libvlc.libvlc_video_get_spu_count(mediaPlayerInstance);
    }

    @Override
    public int getSpu() {
        Logger.debug("getSpu()");
        return libvlc.libvlc_video_get_spu(mediaPlayerInstance);
    }

    @Override
    public int setSpu(int spu) {
        Logger.debug("setSpu(spu={})", spu);
        libvlc.libvlc_video_set_spu(mediaPlayerInstance, spu);
        return getSpu();
    }

    @Override
    public int cycleSpu() {
        Logger.debug("cycleSpu()");
        int spu = getSpu();
        int spuCount = getSpuCount();
        if(spu >= spuCount) {
            spu = 0;
        }
        else {
            spu ++ ;
        }
        return setSpu(spu);
    }

    @Override
    public long getSpuDelay() {
        Logger.debug("getSpuDelay()");
        return libvlc.libvlc_video_get_spu_delay(mediaPlayerInstance);
    }

    @Override
    public void setSpuDelay(long delay) {
        Logger.debug("setSpuDelay(delay={})", delay);
        libvlc.libvlc_video_set_spu_delay(mediaPlayerInstance, delay);
    }

    @Override
    public void setSubTitleFile(String subTitleFileName) {
        Logger.debug("setSubTitleFile(subTitleFileName={})", subTitleFileName);
        libvlc.libvlc_video_set_subtitle_file(mediaPlayerInstance, subTitleFileName);
    }

    @Override
    public void setSubTitleFile(File subTitleFile) {
        Logger.debug("setSubTitleFile(subTitleFile={})", subTitleFile);
        setSubTitleFile(subTitleFile.getAbsolutePath());
    }

    // === Teletext Controls ====================================================

    @Override
    public int getTeletextPage() {
        Logger.debug("getTeletextPage()");
        return libvlc.libvlc_video_get_teletext(mediaPlayerInstance);
    }

    @Override
    public void setTeletextPage(int pageNumber) {
        Logger.debug("setTeletextPage(pageNumber={})", pageNumber);
        libvlc.libvlc_video_set_teletext(mediaPlayerInstance, pageNumber);
    }

    @Override
    public void toggleTeletext() {
        Logger.debug("toggleTeletext()");
        libvlc.libvlc_toggle_teletext(mediaPlayerInstance);
    }

    // === Description Controls =================================================

    @Override
    public List<TrackDescription> getTitleDescriptions() {
        Logger.debug("getTitleDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_title_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getVideoDescriptions() {
        Logger.debug("getVideoDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getAudioDescriptions() {
        Logger.debug("getAudioDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_audio_get_track_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<TrackDescription> getSpuDescriptions() {
        Logger.debug("getSpuDescriptions()");
        libvlc_track_description_t trackDescriptions = libvlc.libvlc_video_get_spu_description(mediaPlayerInstance);
        return getTrackDescriptions(trackDescriptions);
    }

    @Override
    public List<String> getChapterDescriptions(int title) {
        Logger.debug("getChapterDescriptions(title={})", title);
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
        Logger.debug("getChapterDescriptions()");
        return getChapterDescriptions(getTitle());
    }

    @Override
    public List<List<String>> getAllChapterDescriptions() {
        Logger.debug("getAllChapterDescriptions()");
        int titleCount = getTitleCount();
        List<List<String>> result = new ArrayList<List<String>>(titleCount);
        for(int i = 0; i < titleCount; i ++ ) {
            result.add(getChapterDescriptions(i));
        }
        return result;
    }

    @Override
    public List<TrackInfo> getTrackInfo() {
        Logger.debug("getTrackInfo()");
        return getTrackInfo(mediaInstance);
    }

    @Override
    public List<TrackInfo> getTrackInfo(libvlc_media_t media) {
        Logger.debug("getTrackInfo(media={})", media);
        List<TrackInfo> result = null;
        if(media != null) {
            // Preferred implementation using new functions in libvlc 2.1.0 and later...
            if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_210)) {
                result = newGetTrackInfo(media);
            }
            // Legacy implementation for libvlc 2.0.0 and earlier...
            else {
                result = oldGetTrackInfo(media);
            }
        }
        return result;
    }

    /**
     * Get track info using the new libvlc 2.1.0+ implementation.
     *
     * @param media media descriptor
     * @return track info
     */
    private List<TrackInfo> newGetTrackInfo(libvlc_media_t media) {
        Logger.debug("newGetTrackInfo(media={})", media);
        PointerByReference tracksPointer = new PointerByReference();
        int numberOfTracks = libvlc.libvlc_media_tracks_get(media, tracksPointer);
        Logger.debug("numberOfTracks={}", numberOfTracks);
        List<TrackInfo> result = new ArrayList<TrackInfo>(numberOfTracks);
        if(numberOfTracks > 0) {
            Pointer[] tracks = tracksPointer.getValue().getPointerArray(0, numberOfTracks);
            for(Pointer track : tracks) {
                libvlc_media_track_t trackInfo = new libvlc_media_track_t(track);
                switch(libvlc_track_type_t.valueOf(trackInfo.i_type)) {
                    case libvlc_track_unknown:
                        result.add(new UnknownTrackInfo(
                            trackInfo.i_codec,
                            trackInfo.i_original_fourcc,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            trackInfo.i_bitrate,
                            NativeString.getNativeString(libvlc, trackInfo.psz_language),
                            NativeString.getNativeString(libvlc, trackInfo.psz_description)
                        ));
                        break;

                    case libvlc_track_video:
                        trackInfo.u.setType(libvlc_video_track_t.class);
                        trackInfo.u.read();
                        result.add(new VideoTrackInfo(
                            trackInfo.i_codec,
                            trackInfo.i_original_fourcc,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            trackInfo.i_bitrate,
                            NativeString.getNativeString(libvlc, trackInfo.psz_language),
                            NativeString.getNativeString(libvlc, trackInfo.psz_description),
                            trackInfo.u.video.i_width,
                            trackInfo.u.video.i_height,
                            trackInfo.u.video.i_sar_num,
                            trackInfo.u.video.i_sar_den,
                            trackInfo.u.video.i_frame_rate_num,
                            trackInfo.u.video.i_frame_rate_den
                        ));
                        break;

                    case libvlc_track_audio:
                        trackInfo.u.setType(libvlc_audio_track_t.class);
                        trackInfo.u.read();
                        result.add(new AudioTrackInfo(
                            trackInfo.i_codec,
                            trackInfo.i_original_fourcc,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            trackInfo.i_bitrate,
                            NativeString.getNativeString(libvlc, trackInfo.psz_language),
                            NativeString.getNativeString(libvlc, trackInfo.psz_description),
                            trackInfo.u.audio.i_channels,
                            trackInfo.u.audio.i_rate
                        ));
                        break;

                    case libvlc_track_text:
                        trackInfo.u.setType(libvlc_subtitle_track_t.class);
                        trackInfo.u.read();
                        result.add(new SpuTrackInfo(
                            trackInfo.i_codec,
                            trackInfo.i_original_fourcc,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            trackInfo.i_bitrate,
                            NativeString.getNativeString(libvlc, trackInfo.psz_language),
                            NativeString.getNativeString(libvlc, trackInfo.psz_description),
                            NativeString.getNativeString(libvlc, trackInfo.u.subtitle.psz_encoding)
                        ));
                        break;
                }
            }
        }
        return result;
    }

    /**
     * Get track info using the new pre-libvlc 2.1.0 implementation.
     *
     * @param media media descriptor
     * @return track info
     */
    private List<TrackInfo> oldGetTrackInfo(libvlc_media_t media) {
        Logger.debug("oldGetTrackInfo(media={})", media);
        PointerByReference tracks = new PointerByReference();
        int numberOfTracks = libvlc.libvlc_media_get_tracks_info(media, tracks);
        Logger.debug("numberOfTracks={}", numberOfTracks);
        List<TrackInfo> result = new ArrayList<TrackInfo>(numberOfTracks);
        if(numberOfTracks > 0) {
            libvlc_media_track_info_t trackInfos = new libvlc_media_track_info_t(tracks.getValue());
            libvlc_media_track_info_t[] trackInfoArray = (libvlc_media_track_info_t[])trackInfos.toArray(numberOfTracks);
            for(libvlc_media_track_info_t trackInfo : trackInfoArray) {
                switch(libvlc_track_type_t.valueOf(trackInfo.i_type)) {
                    case libvlc_track_unknown:
                        result.add(new UnknownTrackInfo(
                            trackInfo.i_codec,
                            0,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            0,
                            null,
                            null
                        ));
                        break;

                    case libvlc_track_video:
                        trackInfo.u.setType(libvlc_media_track_info_video_t.ByValue.class);
                        trackInfo.u.read();
                        result.add(new VideoTrackInfo(
                            trackInfo.i_codec,
                            0,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            0,
                            null,
                            null,
                            trackInfo.u.video.i_width,
                            trackInfo.u.video.i_height,
                            0,
                            0,
                            0,
                            0
                        ));
                        break;

                    case libvlc_track_audio:
                        trackInfo.u.setType(libvlc_media_track_info_audio_t.ByValue.class);
                        trackInfo.u.read();
                        result.add(new AudioTrackInfo(
                            trackInfo.i_codec,
                            0,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            0,
                            null,
                            null,
                            trackInfo.u.audio.i_channels,
                            trackInfo.u.audio.i_rate
                        ));
                        break;

                    case libvlc_track_text:
                        result.add(new SpuTrackInfo(
                            trackInfo.i_codec,
                            0,
                            trackInfo.i_id,
                            trackInfo.i_profile,
                            trackInfo.i_level,
                            0,
                            null,
                            null,
                            null
                        ));
                        break;
                }
            }
        }
        libvlc.libvlc_free(tracks.getValue());
        return result;
    }

    @Override
    public List<List<TrackInfo>> getSubItemTrackInfo() {
        Logger.debug("getSubItemTrackInfo()");
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
        Logger.debug("getTrackDescriptions()");
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
        Logger.debug("setSnapshotDirectory(snapshotDirectoryName={})", snapshotDirectoryName);
        this.snapshotDirectoryName = snapshotDirectoryName;
    }

    @Override
    public boolean saveSnapshot() {
        Logger.debug("saveSnapshot()");
        return saveSnapshot(0, 0);
    }

    @Override
    public boolean saveSnapshot(int width, int height) {
        Logger.debug("saveSnapshot(width={},height={})", width, height);
        File snapshotDirectory = new File(snapshotDirectoryName == null ? System.getProperty("user.home") : snapshotDirectoryName);
        File snapshotFile = new File(snapshotDirectory, "vlcj-snapshot-" + System.currentTimeMillis() + ".png");
        return saveSnapshot(snapshotFile, width, height);
    }

    @Override
    public boolean saveSnapshot(File file) {
        Logger.debug("saveSnapshot(file={})", file);
        return saveSnapshot(file, 0, 0);
    }

    @Override
    public boolean saveSnapshot(File file, int width, int height) {
        Logger.debug("saveSnapshot(file={},width={},height={})", file, width, height);
        File snapshotDirectory = file.getParentFile();
        if(snapshotDirectory == null) {
            snapshotDirectory = new File(".");
            Logger.debug("No directory specified for snapshot, snapshot will be saved to {}", snapshotDirectory.getAbsolutePath());
        }
        if(!snapshotDirectory.exists()) {
            snapshotDirectory.mkdirs();
        }
        if(snapshotDirectory.exists()) {
            boolean snapshotTaken = libvlc.libvlc_video_take_snapshot(mediaPlayerInstance, 0, file.getAbsolutePath(), width, height) == 0;
            Logger.debug("snapshotTaken={}", snapshotTaken);
            return snapshotTaken;
        }
        else {
            throw new RuntimeException("Directory does not exist and could not be created for '" + file.getAbsolutePath() + "'");
        }
    }

    @Override
    public BufferedImage getSnapshot() {
        Logger.debug("getSnapshot()");
        return getSnapshot(0, 0);
    }

    @Override
    public BufferedImage getSnapshot(int width, int height) {
        Logger.debug("getSnapshot(width={},height={})", width, height);
        File file = null;
        try {
            file = File.createTempFile("vlcj-snapshot-", ".png");
            Logger.debug("file={}", file.getAbsolutePath());
            if(saveSnapshot(file, width, height)) {
                return ImageIO.read(file);
            }
            else {
                return null;
            }
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to get snapshot image", e);
        }
        finally {
            if(file != null) {
                boolean deleted = file.delete();
                Logger.debug("deleted={}", deleted);
            }
        }
    }

    // === Logo Controls ========================================================

    @Override
    public void enableLogo(boolean enable) {
        Logger.debug("enableLogo(enable={})", enable);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
    }

    @Override
    public void setLogoOpacity(int opacity) {
        Logger.debug("setLogoOpacity(opacity={})", opacity);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
    }

    @Override
    public void setLogoOpacity(float opacity) {
        Logger.debug("setLogoOpacity(opacity={})", opacity);
        int opacityValue = Math.round(opacity * 255.0f);
        Logger.debug("opacityValue={}", opacityValue);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
    }

    @Override
    public void setLogoLocation(int x, int y) {
        Logger.debug("setLogoLocation(x={},y={})", x, y);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
    }

    @Override
    public void setLogoPosition(libvlc_logo_position_e position) {
        Logger.debug("setLogoPosition(position={})", position);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    }

    @Override
    public void setLogoFile(String logoFile) {
        Logger.debug("setLogoFile(logoFile={})", logoFile);
        libvlc.libvlc_video_set_logo_string(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_file.intValue(), logoFile);
    }

    @Override
    public void setLogoImage(RenderedImage logoImage) {
        Logger.debug("setLogoImage(logoImage={})", logoImage);
        File file = null;
        try {
            // Create a temporary file for the logo...
            file = File.createTempFile("vlcj-logo-", ".png");
            ImageIO.write(logoImage, "png", file);
            // ...then set the logo as normal
            setLogoFile(file.getAbsolutePath());
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to set logo image", e);
        }
        finally {
            if(file != null) {
                boolean deleted = file.delete();
                Logger.debug("deleted={}", deleted);
            }
        }
    }

    // === Marquee Controls =====================================================

    @Override
    public void enableMarquee(boolean enable) {
        Logger.debug("enableMarquee(enable={})", enable);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Enable.intValue(), enable ? 1 : 0);
    }

    @Override
    public void setMarqueeText(String text) {
        Logger.debug("setMarqueeText(text={})", text);
        libvlc.libvlc_video_set_marquee_string(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Text.intValue(), text);
    }

    @Override
    public void setMarqueeColour(Color colour) {
        Logger.debug("setMarqueeColour(colour={})", colour);
        setMarqueeColour(colour.getRGB() & 0x00ffffff);
    }

    @Override
    public void setMarqueeColour(int colour) {
        Logger.debug("setMarqueeColour(colour={})", colour);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Color.intValue(), colour);
    }

    @Override
    public void setMarqueeOpacity(int opacity) {
        Logger.debug("setMarqueeOpacity(opacity={})", opacity);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacity);
    }

    @Override
    public void setMarqueeOpacity(float opacity) {
        Logger.debug("setMarqueeOpacity(opacity={})", opacity);
        int opacityValue = Math.round(opacity * 255.0f);
        Logger.debug("opacityValue={}", opacityValue);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Opacity.intValue(), opacityValue);
    }

    @Override
    public void setMarqueeSize(int size) {
        Logger.debug("setMarqueeSize(size={})", size);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Size.intValue(), size);
    }

    @Override
    public void setMarqueeTimeout(int timeout) {
        Logger.debug("setMarqueeTimeout(timeout={})", timeout);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Timeout.intValue(), timeout);
    }

    @Override
    public void setMarqueeLocation(int x, int y) {
        Logger.debug("setMarqueeLocation(x={},y={})", x, y);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_X.intValue(), x);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Y.intValue(), y);
    }

    @Override
    public void setMarqueePosition(libvlc_marquee_position_e position) {
        Logger.debug("setMarqueePosition(position={})", position);
        libvlc.libvlc_video_set_marquee_int(mediaPlayerInstance, libvlc_video_marquee_option_t.libvlc_marquee_Position.intValue(), position.intValue());
    }

    // === Filter Controls ======================================================

    @Override
    public void setDeinterlace(DeinterlaceMode deinterlaceMode) {
        Logger.debug("setDeinterlace(deinterlaceMode={})", deinterlaceMode);
        libvlc.libvlc_video_set_deinterlace(mediaPlayerInstance, deinterlaceMode != null ? deinterlaceMode.mode() : null);
    }

    // === Video Adjustment Controls ============================================

    @Override
    public void setAdjustVideo(boolean adjustVideo) {
        Logger.debug("setAdjustVideo(adjustVideo={})", adjustVideo);
        libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue(), adjustVideo ? 1 : 0);
    }

    @Override
    public boolean isAdjustVideo() {
        Logger.debug("isAdjustVideo()");
        return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Enable.intValue()) == 1;
    }

    @Override
    public float getContrast() {
        Logger.debug("getContrast()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue());
    }

    @Override
    public void setContrast(float contrast) {
        Logger.debug("setContrast(contrast={})", contrast);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Contrast.intValue(), contrast);
    }

    @Override
    public float getBrightness() {
        Logger.debug("getBrightness()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue());
    }

    @Override
    public void setBrightness(float brightness) {
        Logger.debug("setBrightness(brightness={})", brightness);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Brightness.intValue(), brightness);
    }

    @Override
    public int getHue() {
        Logger.debug("getHue()");
        return libvlc.libvlc_video_get_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue());
    }

    @Override
    public void setHue(int hue) {
        Logger.debug("setHue(hue={})", hue);
        libvlc.libvlc_video_set_adjust_int(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Hue.intValue(), hue);
    }

    @Override
    public float getSaturation() {
        Logger.debug("getSaturation()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue());
    }

    @Override
    public void setSaturation(float saturation) {
        Logger.debug("setSaturation(saturation={})", saturation);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Saturation.intValue(), saturation);
    }

    @Override
    public float getGamma() {
        Logger.debug("getGamma()");
        return libvlc.libvlc_video_get_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue());
    }

    @Override
    public void setGamma(float gamma) {
        Logger.debug("setGamma(gamma={})", gamma);
        libvlc.libvlc_video_set_adjust_float(mediaPlayerInstance, libvlc_video_adjust_option_t.libvlc_adjust_Gamma.intValue(), gamma);
    }

    // === Implementation =======================================================

    @Override
    public String mrl() {
        Logger.debug("mrl()");
        if(mediaInstance != null) {
            return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
        }
        else {
            throw null;
        }
    }

    @Override
    public String mrl(libvlc_media_t mediaInstance) {
        Logger.debug("mrl(mediaInstance={})", mediaInstance);
        return NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
    }

    @Override
    public Object userData() {
        Logger.debug("userData()");
        return userData;
    }

    @Override
    public void userData(Object userData) {
        Logger.debug("userData(userData={})", userData);
        this.userData = userData;
    }

    @Override
    public final void release() {
        Logger.debug("release()");
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
        Logger.debug("createInstance()");

        mediaPlayerInstance = libvlc.libvlc_media_player_new(instance);
        Logger.debug("mediaPlayerInstance={}", mediaPlayerInstance);

        mediaPlayerEventManager = libvlc.libvlc_media_player_event_manager(mediaPlayerInstance);
        Logger.debug("mediaPlayerEventManager={}", mediaPlayerEventManager);

        registerEventListener();

        // The order these handlers execute in is important for proper operation
        eventListenerList.add(new NewMediaEventHandler());
        eventListenerList.add(new RepeatPlayEventHandler());
        eventListenerList.add(new SubItemEventHandler());
    }

    /**
     * Clean up the native media player resources.
     */
    private void destroyInstance() {
        Logger.debug("destroyInstance()");

        Logger.debug("Detach media events...");
        deregisterMediaEventListener();
        Logger.debug("Media events detached.");

        if(mediaInstance != null) {
            Logger.debug("Release media...");
            libvlc.libvlc_media_release(mediaInstance);
            Logger.debug("Media released.");
        }

        Logger.debug("Detach media player events...");
        deregisterEventListener();
        Logger.debug("Media player events detached.");

        eventListenerList.clear();

        if(mediaPlayerInstance != null) {
            Logger.debug("Release media player...");
            libvlc.libvlc_media_player_release(mediaPlayerInstance);
            Logger.debug("Media player released.");
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
        callback = new EventCallback();
        for(libvlc_event_e event : libvlc_event_e.values()) {
            if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaPlayerVout.intValue()) {
                Logger.debug("event={}", event);
                int result = libvlc.libvlc_event_attach(mediaPlayerEventManager, event.intValue(), callback, null);
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
                if(event.intValue() >= libvlc_event_e.libvlc_MediaPlayerMediaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaPlayerVout.intValue()) {
                    Logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaPlayerEventManager, event.intValue(), callback, null);
                }
            }
            callback = null;
        }
    }

    /**
     * Register a call-back to receive media native events.
     */
    private void registerMediaEventListener() {
        Logger.debug("registerMediaEventListener()");
        // If there is a media, register a new listener...
        if(mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
                    Logger.debug("event={}", event);
                    int result = libvlc.libvlc_event_attach(mediaEventManager, event.intValue(), callback, null);
                    Logger.debug("result={}", result);
                }
            }
        }
    }

    /**
     * De-register the call-back used to receive native media events.
     */
    private void deregisterMediaEventListener() {
        Logger.debug("deregisterMediaEventListener()");
        // If there is a media, deregister the listener...
        if(mediaInstance != null) {
            libvlc_event_manager_t mediaEventManager = libvlc.libvlc_media_event_manager(mediaInstance);
            for(libvlc_event_e event : libvlc_event_e.values()) {
                if(event.intValue() >= libvlc_event_e.libvlc_MediaMetaChanged.intValue() && event.intValue() <= libvlc_event_e.libvlc_MediaStateChanged.intValue()) {
                    Logger.debug("event={}", event);
                    libvlc.libvlc_event_detach(mediaEventManager, event.intValue(), callback, null);
                }
            }
        }
    }

    /**
     * Raise an event.
     *
     * @param mediaPlayerEvent event to raise, may be <code>null</code>
     */
    private void raiseEvent(MediaPlayerEvent mediaPlayerEvent) {
        Logger.trace("raiseEvent(mediaPlayerEvent={}", mediaPlayerEvent);
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
     * @param media media resource locator (MRL)
     * @param mediaOptions zero or more media options
     * @throws IllegalArgumentException if the supplied MRL could not be parsed
     */
    private boolean setMedia(String media, String... mediaOptions) {
        Logger.debug("setMedia(media={},mediaOptions={})", media, Arrays.toString(mediaOptions));
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
        // Create new media...
        if(MediaResourceLocator.isLocation(media)) {
            Logger.debug("Treating mrl as a location");
            mediaInstance = libvlc.libvlc_media_new_location(instance, media);
        }
        else {
            Logger.debug("Treating mrl as a path");
            mediaInstance = libvlc.libvlc_media_new_path(instance, media);
        }
        Logger.debug("mediaInstance={}", mediaInstance);
        if(mediaInstance != null) {
            // Set the standard media options (if any)...
            if(standardMediaOptions != null) {
                for(String standardMediaOption : standardMediaOptions) {
                    Logger.debug("standardMediaOption={}", standardMediaOption);
                    libvlc.libvlc_media_add_option(mediaInstance, standardMediaOption);
                }
            }
            // Set the particular media options (if any)...
            if(mediaOptions != null) {
                for(String mediaOption : mediaOptions) {
                    Logger.debug("mediaOption={}", mediaOption);
                    libvlc.libvlc_media_add_option(mediaInstance, mediaOption);
                }
            }
            // Attach a listener to the new media
            registerMediaEventListener();
            // Set the new media on the media player
            libvlc.libvlc_media_player_set_media(mediaPlayerInstance, mediaInstance);
        }
        else {
            Logger.error("Failed to create native media resource for '{}'", media);
        }
        // Prepare a new statistics object to re-use for the new media item
        libvlcMediaStats = new libvlc_media_stats_t();
        return mediaInstance != null;
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
     * @return result
     */
    private <T> T handleSubItems(SubItemsHandler<T> subItemsHandler) {
        Logger.debug("handleSubItems()");
        libvlc_media_list_t subItemList = null;
        try {
            if(mediaInstance != null) {
                // Get the list of sub-items
                subItemList = libvlc.libvlc_media_subitems(mediaInstance);
                Logger.debug("subItemList={}", subItemList);
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
            Logger.trace("callback(event={},userData={})", event, userData);
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
            Logger.trace("run()");
            for(int i = eventListenerList.size() - 1; i >= 0; i -- ) {
                MediaPlayerEventListener listener = eventListenerList.get(i);
                try {
                    mediaPlayerEvent.notify(listener);
                }
                catch(Exception e) {
                    Logger.warn("Event listener {} threw an exception", e, listener);
                    // Continue with the next listener...
                }
            }
            Logger.trace("runnable exits");
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
            Logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})", mediaPlayer, media, mrl);
            // If this is not a sub-item...
            if(subItemIndex() == -1) {
                // Raise a semantic event to announce the media was changed
                Logger.debug("Raising event for new media");
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
            Logger.debug("finished(mediaPlayer={})", mediaPlayer);
            if(repeat && mediaInstance != null) {
                int subItemCount = subItemCount();
                Logger.debug("subitemCount={}", subItemCount);
                if(subItemCount == 0) {
                    String mrl = NativeString.getNativeString(libvlc, libvlc.libvlc_media_get_mrl(mediaInstance));
                    Logger.debug("auto repeat mrl={}", mrl);
                    // It is not sufficient to simply call play(), the MRL must explicitly
                    // be played again - this is the reason why the repeat play might not
                    // be seamless
                    mediaPlayer.playMedia(mrl);
                }
                else {
                    Logger.debug("Sub-items handling repeat");
                }
            }
            else {
                Logger.debug("No repeat");
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
            Logger.debug("finished(mediaPlayer={})", mediaPlayer);
            // If a sub-item being played...
            if(subItemIndex != -1) {
                // Raise a semantic event to announce the sub-item was finished
                Logger.debug("Raising finished event for sub-item {}", subItemIndex);
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
}
