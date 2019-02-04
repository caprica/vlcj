package uk.co.caprica.vlcj.discovery;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.discovery.strategy.*;
import uk.co.caprica.vlcj.binding.RuntimeUtil;

/**
 * Native library discovery component.
 * <p>
 *     ...
 * <p>
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
