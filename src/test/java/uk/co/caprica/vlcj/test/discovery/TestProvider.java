package uk.co.caprica.vlcj.test.discovery;

import uk.co.caprica.vlcj.discovery.provider.DiscoveryDirectoryProvider;

/**
 * Remember that you need to add a META-INF/services entry for any new provider implementation.
 */
public class TestProvider implements DiscoveryDirectoryProvider {

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String[] directories() {
        // Return one or more custom directories here
        return new String[0];
    }

    @Override
    public boolean supported() {
        return true;
    }

}
