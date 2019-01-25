package uk.co.caprica.vlcj.discovery;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.discovery.strategy.*;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

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
        for (NativeDiscoveryStrategy discoveryStrategy : discoveryStrategies) {
            if (discoveryStrategy.supported()) {
                String path = discoveryStrategy.discover();
                if (path != null) {
                    if (discoveryStrategy.onFound(path)) {
                        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
                    }
                    tryPluginPath(path, discoveryStrategy);
                    onFound(path, discoveryStrategy);
                    return true;
                }
            }
        }
        onNotFound();
        return false;
    }

    private void tryPluginPath(String path, NativeDiscoveryStrategy discoveryStrategy) {
        String env = System.getenv(PLUGIN_ENV_NAME);
        if (env == null || env.length() > 0) {
            discoveryStrategy.onSetPluginPath(path);
        }
    }

    protected void onFound(String path, NativeDiscoveryStrategy strategy) {
    }

    protected void onNotFound() {
    }

}
