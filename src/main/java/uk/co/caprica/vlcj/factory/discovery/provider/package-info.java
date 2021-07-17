/**
 * Native discovery strategy implementation that searches well-known directories to find native libraries.
 * <p>
 * Components that provide the directories to search are loaded at run-time by the {@link java.util.ServiceLoader}
 * mechanism.
 * <p>
 * Applications can easily provide their own {@link uk.co.caprica.vlcj.factory.discovery.provider.DiscoveryDirectoryProvider}
 * implementations and register the fully-qualified classnames of those implementations in the services descriptor file
 * located at <code>META-INF/services/uk.co.caprica.vlcj.factory.discovery.provider.DiscoveryDirectoryProvider</code> (in their
 * own application classpath).
 */
package uk.co.caprica.vlcj.factory.discovery.provider;
