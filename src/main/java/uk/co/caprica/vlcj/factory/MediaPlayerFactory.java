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

package uk.co.caprica.vlcj.factory;

import com.sun.jna.StringArray;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LinuxNativeInit;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.factory.discovery.strategy.NativeDiscoveryStrategy;
import uk.co.caprica.vlcj.support.eventmanager.TaskExecutor;
import uk.co.caprica.vlcj.support.version.LibVlcVersion;

import java.util.Collection;

import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_new;
import static uk.co.caprica.vlcj.binding.LibVlc.libvlc_release;

/**
 * Factory for creating media player instances and associated components.
 * <p>
 * When using VLC options/arguments to initialise the factory, generally any options that enable/disable modules (e.g.
 * video/audio filters) must be set via the factory instance and not when invoking
 * {@link uk.co.caprica.vlcj.player.base.MediaApi#play(String, String...)}. However, the module-specific
 * options <em>may</em> be able to be passed as media options and be effective via that play call.
 * <p>
 * The factory will attempt to automatically discover the location of the required LibVLC native library, so it should
 * just work by default (at least for the most common/likely environment configurations). If you have other requirements
 * for the native library discovery mechanism, you can pass in your own implementation of {@link NativeDiscovery} when
 * you create the factory.
 * <p>
 * You should explicitly {@link #release()} the factory when your application terminates to properly clean up native
 * resources.
 * <p>
 * The factory also provides access to the native libvlc Logger and other resources such as the list of audio outputs,
 * and the list of available audio and video filters.
 * <p>
 * You <em>must</em> make sure you keep a hard reference to the components created by this factory to prevent them from
 * going out of scope and being garbage-collected. If you allow one of these components to go out of scope, then
 * unpredictable behaviour will occur (such as events no longer seeming to fire) even though the video playback may
 * continue (since that happens via native code). You will also likely suffer fatal JVM crashes.
 * <p>
 * It is <em>always</em> a better strategy to <em>reuse</em> media player instances, rather than repeatedly creating
 * and destroying them.
 */
public class MediaPlayerFactory {

    /**
     * Custom one-time initialisation required for Linux.
     */
    static {
        if (RuntimeUtil.isNix()) {
            LinuxNativeInit.init();
        }
    }

    /**
     * Native library instance.
     */
    protected final libvlc_instance_t libvlcInstance;

    /**
     * Single-threaded service to execute tasks that need to be off-loaded from a native callback thread.
     * <p>
     * See {@link #submit(Runnable)}.
     */
    private final TaskExecutor executor = new TaskExecutor();

    private final ApplicationApi     applicationApi;
    private final AudioApi           audioApi;
    private final DialogsApi         dialogsApi;
    private final MediaDiscovererApi mediaDiscovererApi;
    private final EqualizerApi       equalizerApi;
    private final MediaPlayerApi     mediaPlayerApi;
    private final MediaApi           mediaApi;
    private final RendererApi        rendererApi;
    private final VideoSurfaceApi    videoSurfaceApi;

    /**
     * The discovery strategy instance that discovered the native library.
     * <p>
     * May be <code>null</code>.
     */
    private NativeDiscoveryStrategy nativeDiscoveryStrategy;

    /**
     * The path to the native library that was discovered.
     * <p>
     * May be <code>null</code>.
     */
    private String nativeLibraryPath;

    /**
     * Create a new media player factory.
     *
     * @param discovery native discovery used to locate the native LibVLC library, may be <code>null</code>
     * @param libvlcArgs array of options/arguments to pass to LibVLC for initialisation of the native library
     * @throws NativeLibraryMappingException if one or more of the declared method bindings in {@link LibVlc} could not be found in the native library that was loaded
     */
    public MediaPlayerFactory(NativeDiscovery discovery, String... libvlcArgs) {
        discoverNativeLibrary(discovery);

        this.libvlcInstance = newLibVlcInstance(libvlcArgs != null ? libvlcArgs : new String[0]);

        this.applicationApi     = new ApplicationApi    (this);
        this.audioApi           = new AudioApi          (this);
        this.dialogsApi         = new DialogsApi        (this);
        this.mediaDiscovererApi = new MediaDiscovererApi(this);
        this.equalizerApi       = new EqualizerApi      (this);
        this.mediaPlayerApi     = new MediaPlayerApi    (this);
        this.mediaApi           = new MediaApi          (this);
        this.rendererApi        = new RendererApi       (this);
        this.videoSurfaceApi    = new VideoSurfaceApi   (this);
    }

    /**
     * Create a new media player factory with default native discovery.
     * <p>
     * If you do not want to use native discovery, use the {@link #MediaPlayerFactory(NativeDiscovery, String...)}
     * constructor instead, passing <code>null</code>.
     *
     * @param libvlcArgs array of options/arguments to pass to LibVLC for initialisation of the native library
     */
    public MediaPlayerFactory(String... libvlcArgs) {
        this(new NativeDiscovery(), libvlcArgs);
    }

    /**
     * Create a new media player factory.
     * <p>
     * If you do not want to use native discovery, pass <code>null</code> for the <code>discovery</code> parameter.
     *
     * @param discovery native discovery used to locate the native LibVLC library, may be <code>null</code>
     * @param libvlcArgs collection of options/arguments to pass to LibVLC for initialisation of the native library
     */
    public MediaPlayerFactory(NativeDiscovery discovery, Collection<String> libvlcArgs) {
        this(discovery, libvlcArgs.toArray(new String[0]));
    }

    /**
     * Create a new media player factory with default native discovery.
     * <p>
     * If you do not want to use native discovery, use the {@link #MediaPlayerFactory(NativeDiscovery, Collection)}
     * constructor instead, passing <code>null</code>.
     *
     * @param libvlcArgs collection of options/arguments to pass to LibVLC for initialisation of the native library
     */
    public MediaPlayerFactory(Collection<String> libvlcArgs) {
        this(new NativeDiscovery(), libvlcArgs);
    }

    /**
     * Create a new media player factory with default native discovery and no initialisation options.
     */
    public MediaPlayerFactory() {
        this(new NativeDiscovery());
    }

    /**
     * Run native discovery, if set, and attempt to load the native library.
     *
     * @param discovery native discovery used to find the native library, may be <code>null</code>
     * @throws NativeLibraryMappingException if one or more of the declared method bindings in {@link LibVlc} could not be found in the native library that was loaded
     */
    private void discoverNativeLibrary(NativeDiscovery discovery) {
        if (discovery != null) {
            // The discover method return value is not currently used, since we try and load the native library whether
            // discovery worked or not
            discovery.discover();
            this.nativeDiscoveryStrategy = discovery.successfulStrategy();
            this.nativeLibraryPath = discovery.discoveredPath();
        }
        try {
            checkVersion();
        }
        catch (NoClassDefFoundError e) {
            throw new NativeLibraryMappingException("Failed to properly initialise the native library", e);
        }
    }

    /**
     * Runtime LibVLC version check.
     * <p>
     * This check must be done here even though the default {@link NativeDiscovery} implementation already does it,
     * simply because using the default {@link NativeDiscovery} is optional.
     */
    private void checkVersion() {
        LibVlcVersion version = new LibVlcVersion();
        if (!version.isSupported()) {
            throw new RuntimeException(String.format("Failed to find minimum required VLC version %s, found %s",
                version.getRequiredVersion(),
                version.getVersion()));
        }
    }

    /**
     * Get a new LibVLC instance.
     *
     * @param libvlcArgs native library initialisation arguments/options
     * @return native library instance
     */
    private libvlc_instance_t newLibVlcInstance(String... libvlcArgs) {
        libvlc_instance_t result = libvlc_new(libvlcArgs.length, new StringArray(libvlcArgs));
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native library instance");
        }
    }

    public final ApplicationApi application() {
        return applicationApi;
    }

    public final AudioApi audio() {
        return audioApi;
    }

    public final DialogsApi dialogs() {
        return dialogsApi;
    }

    public final MediaDiscovererApi mediaDiscoverers() {
        return mediaDiscovererApi;
    }

    public final EqualizerApi equalizer() {
        return equalizerApi;
    }

    public final MediaPlayerApi mediaPlayers() {
        return mediaPlayerApi;
    }

    public final MediaApi media() {
        return mediaApi;
    }

    public final RendererApi renderers() {
        return rendererApi;
    }

    public final VideoSurfaceApi videoSurfaces() {
        return videoSurfaceApi;
    }

    /**
     * Submit a task for asynchronous execution.
     * <p>
     * This is useful in particular for event handling code as native events are generated on a native event callback
     * thread and it is not allowed to call back into LibVLC from this callback thread. If you do, either the call will
     * be ineffective, strange behaviour will happen, or a fatal JVM crash may occur.
     * <p>
     * To mitigate this, those tasks can be offloaded from the native thread, serialised and executed using this method.
     *
     * @param r task to execute
     */
    public final void submit(Runnable r) {
        executor.submit(r);
    }

    /**
     * Release all native resources associated with this factory.
     * <p>
     * The factory must <em>not</em> be used again after it has been released.
     */
    public final void release() {
        executor.release();

        onBeforeRelease();

        applicationApi    .release();
        audioApi          .release();
        dialogsApi        .release();
        mediaDiscovererApi.release();
        equalizerApi      .release();
        mediaPlayerApi    .release();
        mediaApi          .release();
        rendererApi       .release();
        videoSurfaceApi   .release();

        libvlc_release(this.libvlcInstance);

        onAfterRelease();
    }

    /**
     * Get the native discovery strategy instance that discovered the native library.
     * <p>
     * Used only for diagnostic purposes.
     *
     * @return strategy instance
     */
    public final NativeDiscoveryStrategy nativeDiscoveryStrategy() {
        return nativeDiscoveryStrategy;
    }

    /**
     * Get the discovered native library path.
     * <p>
     * Used only for diagnostic purposes.
     *
     * @return native library path
     */
    public final String nativeLibraryPath() {
        return nativeLibraryPath;
    }

    /**
     * Template method invoked immediately prior to the factory being released.
     * <p>
     * A factory sub-class can override this to perform its own clean-up before the factory goes away.
     */
    protected void onBeforeRelease() {
    }

    /**
     * Template method invoked immediately after the factory has been released.
     * <p>
     * A factory subclass can override this to perform its own clean-up after the factory goes away.
     */
    protected void onAfterRelease() {
    }

}
