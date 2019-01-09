package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.version.LibVlcVersion;

/**
 * Private helper class to check the runtime version of VLC is supported.
 */
final class LibVlcVersionCheck {

    /**
     * Check that the runtime version of VLC is supported, and if not throw a {@link RuntimeException}.
     */
    static void check() {
        if (!LibVlcVersion.isSupported()) {
            throw new RuntimeException(String.format("This version of vlcj requires VLC 3.x or later, but VLC %s was found.", LibVlcVersion.getVersion()));
        }
    }

    private LibVlcVersionCheck() {
    }

}
