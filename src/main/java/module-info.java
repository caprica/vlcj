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
 * Copyright 2022 Caprica Software Limited.
 */

module uk.co.caprica.vlcj {
    requires uk.co.caprica.vlcj.natives;

    requires com.sun.jna;
    requires java.desktop;
    requires jdk.unsupported;

    exports uk.co.caprica.vlcj.factory;
    exports uk.co.caprica.vlcj.factory.discovery;
    exports uk.co.caprica.vlcj.factory.discovery.provider;
    exports uk.co.caprica.vlcj.factory.discovery.strategy;

    exports uk.co.caprica.vlcj.log;

    exports uk.co.caprica.vlcj.media;
    exports uk.co.caprica.vlcj.media.callback;
    exports uk.co.caprica.vlcj.media.callback.nonseekable;
    exports uk.co.caprica.vlcj.media.callback.seekable;
    exports uk.co.caprica.vlcj.media.discoverer;
    exports uk.co.caprica.vlcj.media.events;

    exports uk.co.caprica.vlcj.medialist;
    exports uk.co.caprica.vlcj.medialist.events;

    exports uk.co.caprica.vlcj.player.base;
    exports uk.co.caprica.vlcj.player.base.callback;
    exports uk.co.caprica.vlcj.player.base.events;
    exports uk.co.caprica.vlcj.player.component;
    exports uk.co.caprica.vlcj.player.component.callback;
    exports uk.co.caprica.vlcj.player.component.overlay;
    exports uk.co.caprica.vlcj.player.embedded;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.exclusivemode;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.libvlc;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.unsupported;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.windows;
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.x;
    exports uk.co.caprica.vlcj.player.embedded.videosurface;
    exports uk.co.caprica.vlcj.player.embedded.videosurface.callback;
    exports uk.co.caprica.vlcj.player.embedded.videosurface.callback.format;

    exports uk.co.caprica.vlcj.player.list;
    exports uk.co.caprica.vlcj.player.list.events;

    exports uk.co.caprica.vlcj.player.renderer;
    exports uk.co.caprica.vlcj.player.renderer.events;

    exports uk.co.caprica.vlcj.support;
    exports uk.co.caprica.vlcj.support.version;

    // Native library discovery services
    uses uk.co.caprica.vlcj.factory.discovery.provider.DiscoveryDirectoryProvider;
    uses uk.co.caprica.vlcj.factory.discovery.strategy.NativeDiscoveryStrategy;

    // Standard implementations of native library discovery directory providers
    provides uk.co.caprica.vlcj.factory.discovery.provider.DiscoveryDirectoryProvider with
        uk.co.caprica.vlcj.factory.discovery.provider.ConfigDirConfigFileDiscoveryDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.ConfigurationFileDiscoveryDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.JnaLibraryPathDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.LinuxWellKnownDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.MacOsWellKnownDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.SystemPathDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.UserDirConfigFileDiscoveryDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.UserDirDirectoryProvider,
        uk.co.caprica.vlcj.factory.discovery.provider.WindowsInstallDirectoryProvider;

    // Standard implementations of native library discovery strategy providers
    provides uk.co.caprica.vlcj.factory.discovery.strategy.NativeDiscoveryStrategy with
        uk.co.caprica.vlcj.factory.discovery.strategy.LinuxNativeDiscoveryStrategy,
        uk.co.caprica.vlcj.factory.discovery.strategy.MacOsNativeDiscoveryStrategy,
        uk.co.caprica.vlcj.factory.discovery.strategy.WindowsNativeDiscoveryStrategy;
}
