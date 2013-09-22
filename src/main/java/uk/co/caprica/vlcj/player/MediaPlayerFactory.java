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

import java.awt.Canvas;
import java.awt.Toolkit;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_device_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_equalizer_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_module_description_t;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.directaudio.AudioCallback;
import uk.co.caprica.vlcj.player.directaudio.DefaultDirectAudioPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;
import uk.co.caprica.vlcj.player.discoverer.MediaDiscoverer;
import uk.co.caprica.vlcj.player.embedded.DefaultEmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.mac.MacVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.windows.WindowsVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.headless.DefaultHeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.DefaultMediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.manager.DefaultMediaManager;
import uk.co.caprica.vlcj.player.manager.MediaManager;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;
import uk.co.caprica.vlcj.version.Version;

/**
 * Factory for media player instances.
 * <p>
 * The factory initialises a single libvlc instance and uses that to create media player instances.
 * <p>
 * If required, you can create multiple factory instances each with their own libvlc options.
 * <p>
 * You should release the factory when your application terminates to properly clean up native
 * resources.
 * <p>
 * The factory also provides access to the native libvlc Logger and other resources such as the list
 * of audio outputs, and the list of available audio and video filters.
 * <p>
 * Usage:
 *
 * <pre>
 *   // Set some options for libvlc
 *   String[] libvlcArgs = {...add options here...};
 *
 *   // Create a factory instance (once), you can keep a reference to this
 *   MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
 *
 *   // Create a full-screen strategy
 *   FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(mainFrame);
 *
 *   // Create a media player instance
 *   EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
 *
 *   // Do some interesting things with the media player, like setting a video surface...
 *
 *   ...
 *
 *   // Release the media player
 *   mediaPlayer.release();
 *
 *   // Release the factory
 *   factory.release();
 * </pre>
 *
 * You <em>must</em> make sure you keep a hard reference to the media player (and possibly other)
 * objects created by this factory. If you allow a media player object to go out of scope, then
 * unpredictable behaviour will occur (such as events no longer seeming to fire) even though the
 * video playback continues (since that happens via native code). You may also likely suffer fatal
 * JVM crashes.
 * <p>
 * It is always a better strategy to reuse media player instances, rather than repeatedly creating
 * and destroying instances.
 */
public class MediaPlayerFactory {

    /**
     * Workaround for running under Java7 on Linux.
     * <p>
     * Without this (unless other client configuration changes have already been made) an
     * unsatisfied link error will likely be thrown by the JVM when an attempt is made to play
     * video in an embedded media player.
     */
    static {
        // Only apply for Linux...
        if(RuntimeUtil.isNix()) {
            // Only apply if the run-time version is Java 1.7.0 or later...
            Version actualJavaVersion = new Version(System.getProperty("java.version"));
            if(actualJavaVersion.atLeast(new Version("1.7.0"))) {
                Logger.debug("Trying workaround for Java7 on Linux");
                Toolkit.getDefaultToolkit();
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        try {
                            Logger.debug("Attempting to load jawt...");
                            System.loadLibrary("jawt");
                            Logger.debug("...loaded jawt");
                        }
                        catch(UnsatisfiedLinkError e) {
                            Logger.debug("Failed to load jawt", e);
                        }
                        return null;
                    }
                });
                Logger.debug("Java7 on Linux workaround complete.");
            }
        }
    }

    /**
     * Help text if libvlc failed to load and initialise.
     */
    private static final String PLUGIN_PATH_HELP =
        "Failed to initialise libvlc.\n\n" +
        "This is most often caused either by an invalid vlc option begin passed when creating a MediaPlayerFactory or by libvlc being unable to locate the required plugins.\n\n" +
        "If libvlc is unable to locate the required plugins the instructions below may help:\n\n" +
        "In the text below <libvlc-path> represents the name of the directory containing \"{0}\" and \"{1}\" and <plugins-path> represents the name of the directory containing the vlc plugins...\n\n" +
        "For libvlc to function correctly the vlc plugins must be available, there are a number of different ways to achieve this:\n" +
        " 1. Make sure the plugins are installed in the \"<libvlc-path>/{2}\" directory, this should be the case with a normal vlc installation.\n" +
        " 2. Set the VLC_PLUGIN_PATH operating system environment variable to point to \"<plugins-path>\".\n\n" +
        "More information may be available in the log, specify -Dvlcj.log=DEBUG on the command-line when starting your application.\n\n";

    /**
     * Native library interface.
     */
    protected final LibVlc libvlc;

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t instance;

    /**
     * Flag if the native equalizer is available or not.
     * <p>
     * Requires libvlc 2.2.0 or later.
     * <p>
     * This flag will be removed eventually when 2.1.0 and earlier are no longer supported.
     */
    private final boolean equalizerAvailable;

    /**
     * Cached equalizer band frequencies.
     */
    private final List<Float> equalizerBandFrequencies;

    /**
     * Cached equalizer preset names.
     */
    private final List<String> equalizerPresetNames;

    /**
     * True when the factory has been released.
     */
    private boolean released;

    /**
     * Create a new media player factory.
     * <p>
     * If you want to enable logging or synchronisation of the native library interface you must use
     * {@link #MediaPlayerFactory(LibVlc)} and {@link LibVlcFactory}.
     * <p>
     * This factory constructor will enforce a minimum required native library version check - if a
     * suitable native library version is not found a RuntimeException will be thrown.
     * <p>
     * If you do not want to enforce this version check, use one of the other constructors that
     * accepts a LibVlc instance that you obtain from the {@link LibVlcFactory}.
     */
    public MediaPlayerFactory() {
        this(new String[] {});
    }

    /**
     * Create a new media player factory.
     * <p>
     * If you want to enable logging or synchronisation of the native library interface you must use
     * {@link #MediaPlayerFactory(LibVlc)} and {@link LibVlcFactory}.
     * <p>
     * This factory constructor will enforce a minimum required native library version check - if a
     * suitable native library version is not found, a RuntimeException will be thrown.
     * <p>
     * If you do not want to enforce this version check, use one of the other constructors that
     * accepts a LibVlc instance that you obtain from the {@link LibVlcFactory}.
     * <p>
     * Most initialisation arguments may be gleaned by invoking <code>"vlc -H"</code>.
     *
     * @param libvlcArgs initialisation arguments to pass to libvlc
     */
    public MediaPlayerFactory(String... libvlcArgs) {
        this(LibVlcFactory.factory().atLeast("2.1.0").create(), libvlcArgs);
    }

    /**
     * Create a new media player factory.
     * <p>
     * Use {@link LibVlcFactory} to get a reference to the native library.
     *
     * @param libvlc interface to the native library
     */
    public MediaPlayerFactory(LibVlc libvlc) {
        this(libvlc, new String[] {});
    }

    /**
     * Create a new media player factory.
     * <p>
     * Use {@link LibVlcFactory} to get a reference to the native library.
     *
     * @param libvlc interface to the native library
     * @param libvlcArgs initialisation arguments to pass to libvlc
     */
    public MediaPlayerFactory(LibVlc libvlc, String... libvlcArgs) {
        Logger.debug("MediaPlayerFactory(libvlc={},libvlcArgs={})", libvlc, Arrays.toString(libvlcArgs));
        // JNA will look for the libvlc shared library here (and also libvlccore)...
        Logger.debug("jna.library.path={}", System.getProperty("jna.library.path"));
        // Convenience
        if(libvlcArgs == null) {
            libvlcArgs = new String[0];
        }
        // Ordinarily libvlc will look for it's plugins in a directory named "vlc/plugins" relative
        // to the directory where libvlccore is loaded from, this can be overridden by explicitly
        // specifying the "VLC_PLUGIN_PATH" system property (although this should not be necessary)
        String vlcPluginPath = System.getProperty("VLC_PLUGIN_PATH");
        if(vlcPluginPath != null) {
            Logger.debug("VLC_PLUGIN_PATH={}", vlcPluginPath);
        }
        this.libvlc = libvlc;
        this.instance = libvlc.libvlc_new(libvlcArgs.length, libvlcArgs);
        Logger.debug("instance={}", instance);
        if(instance == null) {
            Logger.error("Failed to initialise libvlc");
            String msg = MessageFormat.format(PLUGIN_PATH_HELP, new Object[] {RuntimeUtil.getLibVlcName(), RuntimeUtil.getLibVlcCoreName(), RuntimeUtil.getPluginsDirectoryName()});
            throw new RuntimeException(msg);
        }
        // Cache the equalizer static data
        equalizerAvailable = LibVlcVersion.getVersion().atLeast(new Version("2.2.0"));
        Logger.debug("equalizerAvailable={}", equalizerAvailable);
        if(equalizerAvailable) {
            equalizerBandFrequencies = createEqualizerBandFrequencies();
            equalizerPresetNames = createEqualizerPresetNames();
        }
        else {
            equalizerBandFrequencies = null;
            equalizerPresetNames = null;
        }
    }

    /**
     * Create a new media player factory.
     * <p>
     * This is simply an alternate constructor for convenience, see
     * {@link #MediaPlayerFactory(String...)}.
     *
     * @param libvlcArgs initialisation arguments to pass to libvlc, may be empty but must not be <code>null</code>
     */
    public MediaPlayerFactory(List<String> libvlcArgs) {
        this(libvlcArgs.toArray(new String[libvlcArgs.size()]));
    }

    /**
     * Create a new media player factory.
     * <p>
     * Use {@link LibVlcFactory} to get a reference to the native library.
     * <p>
     * This is simply an alternate constructor for convenience, see
     * {@link #MediaPlayerFactory(LibVlc, String...)}.
     *
     * @param libvlc interface to the native library
     * @param libvlcArgs initialisation arguments to pass to libvlc, may be empty but must not be <code>null</code>
     */
    public MediaPlayerFactory(LibVlc libvlc, List<String> libvlcArgs) {
        this(libvlc, libvlcArgs.toArray(new String[libvlcArgs.size()]));
    }

    /**
     * Release the native resources associated with this factory.
     */
    public void release() {
        Logger.debug("release()");
        if(!released) {
            if(instance != null) {
                libvlc.libvlc_release(instance);
            }
            released = true;
        }
    }

    // === Factory Configuration ================================================

    /**
     * Set the application name.
     *
     * @param userAgent application name
     */
    public void setUserAgent(String userAgent) {
        Logger.debug("setUserAgent(userAgent={})", userAgent);
        setUserAgent(userAgent, null);
    }

    /**
     * Set the application name.
     *
     * @param userAgent application name
     * @param httpUserAgent application name for HTTP
     */
    public void setUserAgent(String userAgent, String httpUserAgent) {
        Logger.debug("setUserAgent(userAgent={},httpUserAgent={})", userAgent, httpUserAgent);
        libvlc.libvlc_set_user_agent(instance, userAgent, userAgent);
    }

    /**
     * Set the application identification information.
     *
     * @param id application id, e.g. com.somecompany.myapp
     * @param version application version
     * @param icon path to application icon
     *
     * @since libvlc 2.1.0
     */
    public void setApplicationId(String id, String version, String icon) {
        Logger.debug("setApplicationId(id=" + id + ",version=" + version + ",icon=" + icon + ")");
        libvlc.libvlc_set_app_id(instance, id, version, icon);
    }

    /**
     * Get the available audio outputs.
     * <p>
     * Each audio output has zero or more audio devices, each device having it's own unique
     * identifier that can be used on a media player to set the select the required output device.
     *
     * @return collection of audio outputs
     */
    public List<AudioOutput> getAudioOutputs() {
        Logger.debug("getAudioOutputs()");
        List<AudioOutput> result = new ArrayList<AudioOutput>();
        libvlc_audio_output_t audioOutputs = libvlc.libvlc_audio_output_list_get(instance);
        if(audioOutputs != null) {
            // Must prevent automatic synchronisation on the native structure, otherwise a
            // fatal JVM crash will occur when the native release call is made - not quite
            // sure why this is needed here
            audioOutputs.setAutoSynch(false);
            libvlc_audio_output_t audioOutput = audioOutputs;
            while(audioOutput != null) {
                // The native strings must be copied here, but not freed (they are freed natively
                // in the subsequent release call
                String name = NativeString.copyNativeString(libvlc, audioOutput.psz_name);
                String description = NativeString.copyNativeString(libvlc, audioOutput.psz_description);
                result.add(new AudioOutput(name, description, getAudioOutputDevices(name)));
                audioOutput = audioOutput.p_next;
            }
            libvlc.libvlc_audio_output_list_release(audioOutputs);
        }
        return result;
    }

    /**
     * Get the devices associated with an audio output.
     *
     * @param outputName output
     * @return collection of audio output devices
     */
    private List<AudioDevice> getAudioOutputDevices(String outputName) {
        Logger.debug("getAudioOutputDevices(outputName={})", outputName);
        List<AudioDevice> result = new ArrayList<AudioDevice>();
        libvlc_audio_output_device_t audioDevices = libvlc.libvlc_audio_output_device_list_get(instance, outputName);
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

    /**
     * Get the available audio filters.
     *
     * @return collection of audio filter descriptions
     *
     * @since libvlc 2.0.0
     */
    public List<ModuleDescription> getAudioFilters() {
        Logger.debug("getAudioFilters()");
        libvlc_module_description_t moduleDescriptions = libvlc.libvlc_audio_filter_list_get(instance);
        // Without disabling auto synch on this JNA structure a fatal crash will
        // intermittently occur when the release call is made - this is the only
        // time (filters) in all of the vlcj bindings that this is required and I
        // do not understand why it is needed only in this case
        moduleDescriptions.setAutoSynch(false);
        List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
        libvlc.libvlc_module_description_list_release(moduleDescriptions);
        return result;
    }

    /**
     * Get the available video filters.
     *
     * @return collection of video filter descriptions
     *
     * @since libvlc 2.0.0
     */
    public List<ModuleDescription> getVideoFilters() {
        Logger.debug("getVideoFilters()");
        libvlc_module_description_t moduleDescriptions = libvlc.libvlc_video_filter_list_get(instance);
        // Without disabling auto synch on this JNA structure a fatal crash will
        // intermittently occur when the release call is made - this is the only
        // time (filters) in all of the vlcj bindings that this is required and I
        // do not understand why it is needed only in this case
        moduleDescriptions.setAutoSynch(false);
        List<ModuleDescription> result = getModuleDescriptions(moduleDescriptions);
        libvlc.libvlc_module_description_list_release(moduleDescriptions);
        return result;
    }

    /**
     * Convert a collection of native module description structures.
     *
     * @param moduleDescriptions module descriptions
     * @return collection of module descriptions
     */
    private List<ModuleDescription> getModuleDescriptions(libvlc_module_description_t moduleDescriptions) {
        List<ModuleDescription> result = new ArrayList<ModuleDescription>();
        libvlc_module_description_t moduleDescription = moduleDescriptions;
        while(moduleDescription != null) {
            result.add(new ModuleDescription(moduleDescription.psz_name, moduleDescription.psz_shortname, moduleDescription.psz_longname, moduleDescription.psz_help));
            moduleDescription = moduleDescription.p_next;
        }
        return result;
    }

    // === Equalizer ============================================================

    /**
     * Is the audio equalizer available?
     *
     * This will be removed when libvlc 2.1.x is no longer supported.
     *
     * @return <code>true</code> if the equalizer is available; <code>false</code> otherwise
     */
    public final boolean isEqualizerAvailable() {
        return equalizerAvailable;
    }

    /**
     * Create the available equalizer band frequency values.
     *
     * @return list of equalizer band frequencies
     */
    private List<Float> createEqualizerBandFrequencies() {
        Logger.debug("createEqualizerBandFrequencies()");
        int numBands = libvlc.libvlc_audio_equalizer_get_band_count();
        Logger.debug("numBands={}", numBands);
        List<Float> result = new ArrayList<Float>(numBands);
        for(int i = 0; i < numBands; i++) {
            result.add(libvlc.libvlc_audio_equalizer_get_band_frequency(i));
        }
        Logger.debug("result={}", result);
        return Collections.unmodifiableList(result);
    }

    /**
     * Create the available equalizer preset names.
     *
     * @return list of equalizer preset names
     */
    private List<String> createEqualizerPresetNames() {
        Logger.debug("createEqualizerPresetNames()");
        int numPresets = libvlc.libvlc_audio_equalizer_get_preset_count();
        Logger.debug("numPresets={}", numPresets);
        List<String> result = new ArrayList<String>(numPresets);
        for(int i = 0; i < numPresets; i++) {
            result.add(libvlc.libvlc_audio_equalizer_get_preset_name(i));
        }
        Logger.debug("result={}", result);
        return Collections.unmodifiableList(result);
    }

    /**
     * Get the list of distinct equalizer band frequencies.
     *
     * @return list of frequencies (Hz)
     * @since libvlc 2.2.0
     */
    public final List<Float> getEqualizerBandFrequencies() {
        Logger.debug("getEqualizerBandFrequencies()");
        checkEqualizer();
        return equalizerBandFrequencies;
    }

    /**
     * Get the list of names of available equalizer presets.
     *
     * @return list of preset names
     * @since libvlc 2.2.0
     */
    public final List<String> getEqualizerPresetNames() {
        Logger.debug("getEqualizerPresetNames()");
        checkEqualizer();
        return equalizerPresetNames;
    }

    /**
     * Create a new audio equalizer.
     *
     * @return equalizer
     * @since libvlc 2.2.0
     */
    public final Equalizer newEqualizer() {
        Logger.debug("newEqualizer()");
        checkEqualizer();
        return new Equalizer(libvlc);
    }

    /**
     * Create a new audio equalizer from a named preset.
     *
     * @param presetName name of the preset
     * @return equalizer
     * @since libvlc 2.2.0
     */
    public final Equalizer newEqualizer(String presetName) {
        Logger.debug("newEqualizer(presetName={})", presetName);
        checkEqualizer();
        int index = equalizerPresetNames.indexOf(presetName);
        if(index != -1) {
            libvlc_equalizer_t presetEqualizer = libvlc.libvlc_audio_equalizer_new_from_preset(index);
            if(presetEqualizer != null) {
                Equalizer equalizer = new Equalizer(libvlc);
                equalizer.setPreamp(libvlc.libvlc_audio_equalizer_get_preamp(presetEqualizer));
                for(int i = 0; i < libvlc.libvlc_audio_equalizer_get_band_count(); i++) {
                    equalizer.setAmp(i, libvlc.libvlc_audio_equalizer_get_amp_at_index(presetEqualizer, i));
                }
                libvlc.libvlc_audio_equalizer_release(presetEqualizer);
                return equalizer;
            }
            else {
                return null;
            }
        }
        else {
            throw new IllegalArgumentException("No such preset named '" + presetName + "'");
        }
    }

    /**
     * Get all of the available preset equalizer instances.
     * <p>
     * This will return new equalizer instances (i.e. they are not cached or shared), so
     * applications are free to change the values in the returned equalizer instances if
     * so desired.
     *
     * @return map of preset name to equalizer instance, sorted by name
     * @since libvlc 2.2.0
     */
    public final Map<String, Equalizer> getAllPresetEqualizers() {
        Logger.debug("getAllPresetEqualizers()");
        checkEqualizer();
        Map<String, Equalizer> result = new TreeMap<String, Equalizer>();
        for(String presetName : equalizerPresetNames) {
            result.put(presetName, newEqualizer(presetName));
        }
        Logger.trace("result={}", result);
        return result;
    }

    /**
     * Check whether or not the audio equalizer is available.
     *
     * @throws UnsupportedOperationException if the equalizer is not available
     */
    private void checkEqualizer() {
        if(!equalizerAvailable) {
            throw new UnsupportedOperationException("Equalizer is not available, you need libvlc 2.1.0 or later");
        }
    }

    // === Media Player =========================================================

    /**
     * Create a new embedded media player.
     * <p>
     * Full-screen will not be available, to enable full-screen support see
     * {@link #newEmbeddedMediaPlayer(FullScreenStrategy)}, or use an alternate mechanism to
     * manually set full-screen if needed.
     *
     * @return media player instance
     */
    public EmbeddedMediaPlayer newEmbeddedMediaPlayer() {
        Logger.debug("newEmbeddedMediaPlayer()");
        return newEmbeddedMediaPlayer(null);
    }

    /**
     * Create a new embedded media player.
     *
     * @param fullScreenStrategy full screen implementation, may be <code>null</code>
     * @return media player instance
     */
    public EmbeddedMediaPlayer newEmbeddedMediaPlayer(FullScreenStrategy fullScreenStrategy) {
        Logger.debug("newEmbeddedMediaPlayer(fullScreenStrategy={})", fullScreenStrategy);
        return new DefaultEmbeddedMediaPlayer(libvlc, instance, fullScreenStrategy);
    }

    /**
     * Create a new direct video rendering media player.
     *
     * @param bufferFormatCallback callback to set the desired buffer format
     * @param renderCallback callback to receive the video frame data
     * @return media player instance
     */
    public DirectMediaPlayer newDirectMediaPlayer(BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback) {
        Logger.debug("newDirectMediaPlayer(formatCallback={},renderCallback={})", bufferFormatCallback, renderCallback);
        return new DefaultDirectMediaPlayer(libvlc, instance, bufferFormatCallback, renderCallback);
    }

    /**
     * Create a new direct audio media player.
     *
     * @param format decoded audio format
     * @param rate decoded audio sample rate
     * @param channels decoded audio channels
     * @param audioCallback callback
     * @return media player instance
     */
    public DirectAudioPlayer newDirectAudioPlayer(String format, int rate, int channels, AudioCallback audioCallback) {
        Logger.debug("newDirectAudioPlayer(format={},rate={},channels={},audioCallback={}", format, rate, channels, audioCallback);
        return new DefaultDirectAudioPlayer(libvlc, instance, format, rate, channels, audioCallback);
    }

    /**
     * Create a new headless media player.
     * <p>
     * The head-less player is intended for audio media players or streaming server media players
     * and may spawn a native video player window unless you set proper media options when playing
     * media.
     *
     * @return media player instance
     */
    public HeadlessMediaPlayer newHeadlessMediaPlayer() {
        Logger.debug("newHeadlessMediaPlayer()");
        return new DefaultHeadlessMediaPlayer(libvlc, instance);
    }

    /**
     * Create a new play-list media player.
     *
     * @return media player instance
     */
    public MediaListPlayer newMediaListPlayer() {
        Logger.debug("newMediaListPlayer()");
        return new DefaultMediaListPlayer(libvlc, instance);
    }

    // === Video Surface ========================================================

    /**
     * Create a new video surface for a Canvas.
     *
     * @param canvas canvas
     * @return video surface
     */
    public CanvasVideoSurface newVideoSurface(Canvas canvas) {
        Logger.debug("newVideoSurface(canvas={})", canvas);
        VideoSurfaceAdapter videoSurfaceAdapter;
        if(RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        }
        else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        CanvasVideoSurface videoSurface = new CanvasVideoSurface(canvas, videoSurfaceAdapter);
        Logger.debug("videoSurface={}", videoSurface);
        return videoSurface;
    }

    /**
     * Create a new video surface for a native component id.
     *
     * @param componentId native component id
     * @return video surface
     */
    public ComponentIdVideoSurface newVideoSurface(long componentId) {
        Logger.debug("newVideoSurface(componentId={})", componentId);
        VideoSurfaceAdapter videoSurfaceAdapter;
        if(RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        }
        else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        ComponentIdVideoSurface videoSurface = new ComponentIdVideoSurface(componentId, videoSurfaceAdapter);
        Logger.debug("videoSurface={}", videoSurface);
        return videoSurface;
    }

    // === Media List ===========================================================

    /**
     * Create a new media list for a play-list media player.
     *
     * @return media list instance
     */
    public MediaList newMediaList() {
        Logger.debug("newMediaList()");
        return new MediaList(libvlc, instance);
    }

    // === Meta Data ============================================================

    /**
     * Get local media meta data.
     * <p>
     * Note that requesting meta data may cause one or more HTTP connections to
     * be made to external web-sites to attempt download of album art.
     * <p>
     * This method should <strong>not</strong> be invoked for non-local MRL's
     * like streaming network addresses.
     *
     * @param mediaPath path to the local media
     * @param parse <code>true</code> if the media should be parsed immediately</code>; otherwise <code>false</code>
     * @return media meta data, or <code>null</code> if the media could not be located
     */
    public MediaMeta getMediaMeta(String mediaPath, boolean parse) {
        Logger.debug("getMediaMeta(mediaPath={},parse={})", mediaPath, parse);
        libvlc_media_t media = libvlc.libvlc_media_new_path(instance, mediaPath);
        Logger.debug("media={}", media);
        if(media != null) {
            if(parse) {
                Logger.debug("Parsing media...");
                libvlc.libvlc_media_parse(media);
                Logger.debug("Media parsed.");
            }
            MediaMeta mediaMeta = new DefaultMediaMeta(libvlc, media);
            // Release this native reference, the media meta instance retains its own native reference
            libvlc.libvlc_media_release(media);
            return mediaMeta;
        }
        else {
            return null;
        }
    }

    // === Log ==================================================================

    /**
     * Create a new native log component.
     * <p>
     * <strong>The native log requires vlc 2.1.0 or later.</strong>
     *
     * @return native log component, or <code>null</code> if the native log is not available
     */
    public NativeLog newLog() {
        Logger.debug("newLog()");
        if(LibVlcVersion.getVersion().atLeast(LibVlcVersion.LIBVLC_210)) {
            return new NativeLog(libvlc, instance);
        }
        else {
            Logger.warn("Native log not available on this platform, needs libvlc 2.1.0 or later");
            return null;
        }
    }

    // === Media Discoverer =====================================================

    /**
     * Create a new native media service discoverer.
     * <p>
     * Not all media discoveres are supported on all platforms.
     *
     * @param name name of the required service discoverer, e.g. "audio", "video".
     * @return native media discoverer component
     */
    public MediaDiscoverer newMediaDiscoverer(String name) {
        Logger.debug("newMediaDiscoverer(name={})", name);
        return new MediaDiscoverer(libvlc, instance, name);
    }

    /**
     * Create a new native audio media service discoverer.
     * <p>
     * This method is simply a convenient wrapper around {@link #newMediaDiscoverer(String)}.
     *
     * @return native media discoverer component
     */
    public MediaDiscoverer newAudioMediaDiscoverer() {
        Logger.debug("newAudioMediaDiscoverer()");
        return newMediaDiscoverer("audio");
    }

    /**
     * Create a new native video media service discoverer.
     * <p>
     * This should return for example video capture devices currently attached to
     * the system.
     * <p>
     * This method is simply a convenient wrapper around {@link #newMediaDiscoverer(String)}.
     * <p>
     * The video discoverer may not be available on all platforms.
     *
     * @return native media discoverer component
     */
    public MediaDiscoverer newVideoMediaDiscoverer() {
        Logger.debug("newVideoMediaDiscoverer()");
        return newMediaDiscoverer("video");
    }

    // === Clock ================================================================

    /**
     * Get the time as defined by LibVLC.
     * <p>
     * The time is not meaningful in the sense of what time is it, rather it is a monotonic clock
     * with an arbitrary starting value.
     *
     * @return current clock time value, in microseconds
     */
    public long clock() {
        Logger.trace("clock()");
        return libvlc.libvlc_clock();
    }

    // === Media Manager ========================================================

    /**
     * Create a new media manager.
     *
     * @return media manager instance
     */
    public MediaManager newMediaManager() {
        Logger.trace("newMediaManager()");
        return new DefaultMediaManager(libvlc, instance);
    }

    // === Build Information ====================================================

    /**
     * Get the libvlc version.
     *
     * @return native library version
     */
    public String version() {
        Logger.debug("version()");
        return libvlc.libvlc_get_version();
    }

    /**
     * Get the compiler used to build libvlc.
     *
     * @return compiler
     */
    public String compiler() {
        Logger.debug("compiler()");
        return libvlc.libvlc_get_compiler();
    }

    /**
     * Get the source code change-set id used to build libvlc.
     *
     * @return change-set
     */
    public String changeset() {
        Logger.debug("changeset()");
        return libvlc.libvlc_get_changeset();
    }
}
