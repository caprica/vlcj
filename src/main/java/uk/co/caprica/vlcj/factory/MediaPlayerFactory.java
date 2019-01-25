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

import com.sun.jna.Native;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import java.util.Collection;

/**
 * Factory for creating media player instances and associated components.
 * <p>
 * When using VLC options/arguments, generally any options that enable/disable modules (e.g. video/audio filters) must
 * be set via the factory instance and not when invoking {@link MediaPlayer#playMedia(String, String...)}. However,
 * the filter-specific options <em>may</em> be able to be passed and be effective via a playMedia call.
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

    static {
        LinuxNativeInit.init();
    }

    private final Logger logger = LoggerFactory.getLogger(MediaPlayerFactory.class);

    private final LibVlc libvlc; // FIXME maybe just protected and get rid of the getter?

    private final libvlc_instance_t libvlcInstance; // FIXME maybe just protected and get rid of the getter?

    private final ApplicationService applicationService;

    private final AudioService audioService;

    private final DialogsService dialogsService;

    private final DiscovererService discovererService;

    private final EqualizerService equalizerService;

    private final MediaPlayerService mediaPlayerService;

    private final MediaService mediaService;

    private final ModuleService moduleService;

    private final RendererService rendererService;

    private final VideoSurfaceService videoSurfaceService;

    /**
     * Create a new media player factory.
     *
     * @param discovery optional native discovery implementation, used to locate the native LibVLC library, may be <code>null</code>
     * @param libvlcArgs array of options/arguments to pass to LibVLC for initialisation of the native library
     */
    public MediaPlayerFactory(NativeDiscovery discovery, String... libvlcArgs) {
        logger.debug("MediaPlayerFactory(discovery={},libvlcArgs={})", discovery, libvlcArgs);

        this.libvlc         = discoverNativeLibrary(discovery);
        this.libvlcInstance = newLibVlcInstance(libvlcArgs != null ? libvlcArgs : new String[0]);

        this.applicationService  = new ApplicationService (this);
        this.audioService        = new AudioService       (this);
        this.dialogsService      = new DialogsService     (this);
        this.discovererService   = new DiscovererService  (this);
        this.equalizerService    = new EqualizerService   (this);
        this.mediaPlayerService  = new MediaPlayerService (this);
        this.mediaService        = new MediaService       (this);
        this.moduleService       = new ModuleService      (this);
        this.rendererService     = new RendererService    (this);
        this.videoSurfaceService = new VideoSurfaceService(this);
    }

    /**
     * Create a new media player factory.
     * <p>
     * The default {@link NativeDiscovery} implementation will used to locate the native LibVLC library.
     *
     * @param libvlcArgs array of options/arguments to pass to LibVLC for initialisation of the native library
     */
    public MediaPlayerFactory(String... libvlcArgs) {
        this(new NativeDiscovery(), libvlcArgs);
    }

    /**
     *
     *
     * @param discovery
     * @param libvlcArgs
     */
    public MediaPlayerFactory(NativeDiscovery discovery, Collection<String> libvlcArgs) {
        this(discovery, libvlcArgs.toArray(new String[libvlcArgs.size()]));
    }

    /**
     *
     *
     * @param libvlcArgs
     */
    public MediaPlayerFactory(Collection<String> libvlcArgs) {
        this(new NativeDiscovery(), libvlcArgs);
    }

    private LibVlc discoverNativeLibrary(NativeDiscovery discovery) {
        logger.debug("discoverNativeLibrary()");
        if (discovery != null) {
            boolean found = discovery.discover();
            logger.debug("found={}", found);
        } else {
            logger.debug("skipping native discovery");
        }
        LibVlc nativeLibrary = Native.load(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        logger.info("nativeLibrary={}", NativeLibraryPath.getNativeLibraryPath(nativeLibrary));
        checkVersion(nativeLibrary);
        // FIXME likely we must use a synchronized instance
        return nativeLibrary;
    }

    private void checkVersion(LibVlc nativeLibrary) {
        LibVlcVersion version = new LibVlcVersion(nativeLibrary);
        if (!version.isSupported()) {
            throw new RuntimeException(String.format("Failed to find minimum required VLC version %s, found %s in %s",
                version.getRequiredVersion(),
                version.getVersion(),
                NativeLibraryPath.getNativeLibraryPath(nativeLibrary)));
        }
    }

    private libvlc_instance_t newLibVlcInstance(String... libvlcArgs) {
        libvlc_instance_t result = libvlc.libvlc_new(libvlcArgs.length, libvlcArgs);
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException("Failed to get a new native library instance");
        }
    }

    public final ApplicationService application() {
        return applicationService;
    }

    public final AudioService audio() {
        return audioService;
    }

    public final DialogsService dialogs() {
        return dialogsService;
    }

    public final DiscovererService discoverers() {
        return discovererService;
    }

    public final EqualizerService equalizers() {
        return equalizerService;
    }

    public final MediaPlayerService mediaPlayers() {
        return mediaPlayerService;
    }

    public final MediaService media() {
        return mediaService;
    }

    public final ModuleService modules() {
        return moduleService;
    }

    public final RendererService renderers() {
        return rendererService;
    }

    public final VideoSurfaceService videoSurfaces() {
        return videoSurfaceService;
    }

    /**
     *
     * @return
     */
    public final String nativeLibraryPath() {
        return NativeLibraryPath.getNativeLibraryPath(libvlc);
    }

    /**
     * Release all native resources associated with this factory.
     * <p>
     * The factory must not be used again after it has been released.
     */
    public final void release() {
        onBeforeRelease();

        applicationService .release();
        audioService       .release();
        dialogsService     .release();
        discovererService  .release();
        equalizerService   .release();
        mediaPlayerService .release();
        mediaService       .release();
        moduleService      .release();
        rendererService    .release();
        videoSurfaceService.release();

        libvlc.libvlc_release(this.libvlcInstance);

        onAfterRelease();
    }

    protected void onBeforeRelease() {
    }

    protected void onAfterRelease() {
    }

    protected final LibVlc libvlc() {
        return libvlc;
    }

    protected final libvlc_instance_t instance() {
        return libvlcInstance;
    }

}
