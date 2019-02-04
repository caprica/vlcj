package uk.co.caprica.vlcj.discovery;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.discovery.strategy.*;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

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
                        onFound(path, discoveryStrategy);
                        alreadyFound = true;
                        return true;
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
            discoveryStrategy.onSetPluginPath(path);
        }
    }

    protected void onFound(String path, NativeDiscoveryStrategy strategy) {
    }

    protected void onNotFound() {
    }

}
