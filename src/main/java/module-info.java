module uk.co.caprica.vlcj {
    requires uk.co.caprica.vlcj.natives;
    requires java.desktop;
    requires jdk.unsupported;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    requires static uk.co.caprica.vlcj.osx.stubs;

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
    exports uk.co.caprica.vlcj.player.embedded.fullscreen.osx;
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
    exports uk.co.caprica.vlcj.waiter;
    exports uk.co.caprica.vlcj.waiter.media;
    exports uk.co.caprica.vlcj.waiter.mediaplayer;

    uses uk.co.caprica.vlcj.factory.discovery.provider.DiscoveryDirectoryProvider;
}
