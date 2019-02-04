package uk.co.caprica.vlcj.discovery;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_instance_t;
import uk.co.caprica.vlcj.discovery.strategy.*;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

/**
 * Native library discovery component.
 * <p>
 *     ...
 * <p>
 * It is possible that even if native discovery fails, i.e. {@link #discover()} returns <code>false</code>, that the
 * native library can be loaded successfully. This could happen in an environment that is already well-configured, with
 * libraries installed in places that the Operating System and JVM already know about.
 * <p>
 * In {@link uk.co.caprica.vlcj.factory.MediaPlayerFactory}, where this native discovery component is primarily used, an
 * attempt to load the native library will <em>always</em> be made, whether explicit discovery works or not.
 * <p>
 * This behaviour is by design, as is always trying the discovery first whether or not this "default" library loading
 * would work - since it is possible that a client application does not actually want to prioritise the default library
 * load (e.g. different version of VLC if multiple are installed).
 */
public class NativeDiscovery {

    /**
     * Name of the system environment variable containing the VLC plugin path location.
     * <p>
     * This is optional, and might not be set.
     */
    protected static final String PLUGIN_ENV_NAME = "VLC_PLUGIN_PATH";

    /**
     * Flag if the discovery already completed and found the native libraries.
     * <p>
     * There is no point running the discovery again if the libraries were already found, since the native library
     * search path will already have been set and a successful discovery would do no more than set it again.
     * <p>
     * If the discovery failed before, then running it again may work e.g. if the client application took some remedial
     * steps to make the native libraries available.
     */
    private boolean alreadyFound;

    private static final NativeDiscoveryStrategy[] DEFAULT_STRATEGIES = new NativeDiscoveryStrategy[] {
        new JnaLibraryPathNativeDiscoveryStrategy(),
        new LinuxNativeDiscoveryStrategy(),
        new OsxNativeDiscoveryStrategy(),
        new WindowsNativeDiscoveryStrategy()
    };

    /**
     * Strategy implementations.
     */
    private final NativeDiscoveryStrategy[] discoveryStrategies;

    /**
     *
     *
     * @param discoveryStrategies
     */
    public NativeDiscovery(NativeDiscoveryStrategy... discoveryStrategies) {
        this.discoveryStrategies = discoveryStrategies.length > 0 ? discoveryStrategies : DEFAULT_STRATEGIES;
    }

    /**
     *
     * <p>
     * Discovery will stop when a strategy returns a discovered location - it is still possible that the native library
     * will fail to load, but even if does not load there is no chance to resume discovery with that strategy or any of
     * the subsequent ones (due to how {@link NativeLibrary#addSearchPath(String, String)} works).
     *
     * @return
     */
    public final boolean discover() {
        if (alreadyFound) {
            return true;
        } else {
            for (NativeDiscoveryStrategy discoveryStrategy : discoveryStrategies) {
                if (discoveryStrategy.supported()) {
                    String path = discoveryStrategy.discover();
                    if (path != null) {
                        if (discoveryStrategy.onFound(path)) {
                            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
                        }
                        tryPluginPath(path, discoveryStrategy);
                        if (tryLoadingLibrary()) {
                            onFound(path, discoveryStrategy);
                            alreadyFound = true;
                            return true;
                        } else {
                            // We have to stop here, because we already added a search path for the native library and
                            // any further search paths we add will be tried AFTER the one that already failed - the
                            // subsequent directories we may like to try will never actually be tried
                            return false;
                        }
                    }
                }
            }
            onNotFound();
            return false;
        }
    }

    /**
     *
     *
     * Rather than setting the plugin path here, we must ask the strategy to set the path. This is because there are
     * different ways (different native API) to set process environment variables on e.g. Linux vs Windows.
     *
     * @param path
     * @param discoveryStrategy
     */
    private void tryPluginPath(String path, NativeDiscoveryStrategy discoveryStrategy) {
        String env = System.getenv(PLUGIN_ENV_NAME);
        if (env == null || env.length() == 0) {
            // The return value from onSetPluginPath is currently not used (it would imply that the API call to set the
            // process environment variable failed, which is somewhat of a stretch that it would ever occur)
            discoveryStrategy.onSetPluginPath(path);
        }
    }

    /**
     *
     */
    private boolean tryLoadingLibrary() {
        try {
            LibVlc libvlc = Native.load(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
            libvlc_instance_t instance = libvlc.libvlc_new(0, new String[0]);
            if (instance != null) {
                libvlc.libvlc_release(instance);
                LibVlcVersion version = new LibVlcVersion(libvlc);
                if (version.isSupported()) {
                    return true;
                }
            }
        }
        catch (UnsatisfiedLinkError e) {
        }
        return false;
    }

    protected void onFound(String path, NativeDiscoveryStrategy strategy) {
    }

    protected void onNotFound() {
    }

}
