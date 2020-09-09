![vlcj](https://github.com/caprica/vlcj/raw/master/etc/vlcj-logo.png "vlcj")

vlcj-5 requires VLC 4.0 as a minimum baseline - VLC 4.0 is currently in development and it may be some time before
it is released.

If you are looking for a stable combination of vlcj and VLC then switch to the
[vlcj-4.x branch](https://github.com/caprica/vlcj/tree/vlcj-4.x) instead.

vlcj
====

The vlcj project provides a Java framework to allow an instance of a native [VLC](http://www.videolan.org/vlc "VLC")
media player to be embedded in a Java application.

You get more than just simple bindings, you also get a higher level framework that hides a lot of the complexities of
working with LibVLC.

vlcj is primarily developed and therefore extensively tested on Linux - it does also work just fine on Windows and
OSX, although there may be some limitations on OSX.

Additionally, whilst not supported as one of the main platforms, this version of vlcj has been tested and shown to be
working on contemporary Raspberry Pi builds.

At least JDK 8 is required.

vlcj-4.6.x, which is still current, is the last version of vlcj that was built against JDK 1.6.

*This version of vlcj requires VLC 4.0.0 as a minimum, no earlier version is supported.*

This is the open source vlcj project page, see also the 'official'
[home page](http://capricasoftware.co.uk/projects/vlcj "Official vlcj home page at Caprica Software") where you can find
more information as well as some new simple tutorials.

Build Status
------------

![Continuous Integration](https://github.com/caprica/vlcj/workflows/Java%20CI%20with%20Maven/badge.svg)

The Continuous Integration build will remain in a failing state until the first release version of vlcj-natives-5.0.0
has been made.

[![Maven Central](https://img.shields.io/maven-central/v/uk.co.caprica/vlcj.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22uk.co.caprica%22%20AND%20a:%22vlcj%22)

News
====

- Future, 2020 - vlcj 5.0.0 release, significant changes for LibVLC 4.0.0, minimum Java version now Java 8 LTS.

All releases are at available at [Maven Central](https://search.maven.org/search?q=a:vlcj).

You can follow @capricasoftware on Twitter for more vlcj news.

Swing/AWT, JavaFX, OpenGL
=========================

Core vlcj embeds native media players in Swing/AWT applications. This has been the foundation of vlcj since the earliest
versions. This is still fully supported and still works well, in fact it is still likely the most performant solution.

However, recent versions of JavaFX introduced the `PixelBuffer` component, providing direct access to a shared native
memory buffer that can be by vlcj/LibVLC as a "callback" video buffer. This performs really well. even when using
multiple concurrent video players.

Another factor to consider here is that contemporary macOS does not support AWT at all, so JavaFX is a very good option
on that platform. 

For more information see the [vlcj-javafx](https://github.com/caprica/vlcj-javafx) and 
[vlcj-javafx-demo](https://github.com/caprica/vlcj-javafx-demo) projects.

A further possibility with this "callback" approach to video rendering is that with vlcj-5.x and VLC 4.x it becomes very
easy to embed a media player in an OpenGL application. This should give very good playback performance, possibly right
on a par with the embedded approach, but it would mean foremost developing an OpenGL application.

For more information see the [vlcj-lwjgl-demo](https://github.com/caprica/vlcj-lwjgl-demo) project.

The beauty of vlcj's design and architecture means adopting any of these approaches is near identical in usage, only a
single video surface abstraction is the difference.

vlcj-5
======

vlcj-5 is primarily a incremental feature-release, preserving the vlcj-4 API as much as possible.

Major New Features
------------------

Headline changes:

 - requires LibVLC 4.0.0+
 - requires Java 8+
 - full support for LibVLC "video engine" rendering, specifically via OpenGL
   * example using LWJGL at [vlcj-lwjgl-demo](https://github.com/caprica/vlcj-lwjgl-demo)
 - fast vs precise seeking when setting media position/time
 - generation of in-memory thumbnail pictures

For a full list of changes in this release, check the release milestones:

- [vlcj 5.0.0 release milestone](https://github.com/caprica/vlcj/milestone/31?closed=1)

Examples
--------

All of the examples have been moved to the [vlcj-examples](https://github.com/caprica/vlcj-examples) project.

Known Issues
------------

 - `CallbackMediaPlayerComponent` does not properly render media that does not have a sample-aspect-ratio (SAR) 1:1,
   this mostly affects DVD ISO, you can still provide your own implementation that handles other SAR's if you need to.
   In any case, using the callback media player with DVD ISO is somewhat of a niche combination and for the vast
   majority of media types this will not be an issue. This may be improved in a later release. The fundamental problem
   right now is that there is simply no *reliable* way to know the SAR - SAR does appear eventually in track information
   but there is no concrete link between that SAR track information and the currently playing video track. This is an
   issue in the underlying native library.

 - When using the new alternate renderer API, if you attempt to play another media while a media is already being sent
   to something like Chromecast you may experience problems - even if you stop the current media first. The cause of
   this is currently unknown, but it may be a native issue.

Tutorials
---------

New tutorials for vlcj-4 are available [here](http://capricasoftware.co.uk/projects/vlcj-4/tutorials).

There are simple tests or demo applications available for pretty much every aspect of vlcj functionality, these are
provided in the
[project test sources](https://github.com/caprica/vlcj/tree/master/src/test/java/uk/co/caprica/vlcj/test).

There is also a major demo application available at the [vlcj-player](https://github.com/caprica/vlcj-player) project
page.

These tutorials are still valid for vlcj-5.

Building vlcj - sun.misc.Unsafe
-------------------------------

When compiling with Maven it is simply not possible to suppress the warnings about using sun.misc.Unsafe.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj</artifactId>
    <version>5.0.0</version>
</dependency>
```

The core vlcj project now no longer contains the required JNA bindings to LibVLC, these are provided instead by the
separate [vlcj-natives](https://github.com/caprica/vlcj-natives) project. The vlcj core project therefore has a new
required dependency on the vlcj-natives project.

If you are using Maven (or similar) to manage your dependencies, the vlcj-natives dependency will be handled
automatically for you (you only need to explicitly add vlcj to your project, not vlcj-natives).

If you are installing vlcj manually, then you will need to include the new vlcj-natives jar file along with the existing
vlcj jar file.

Threading Model
---------------

This section is very important.

With vlcj-4, every native event coming from LibVLC is processed on the native callback thread. This should give some
small performance gains when compared with vlcj-3.

The critical issue is that it is generally not permitted to call back into LibVLC from the event callback thread. Doing
so may cause subtle failures or outright hard JVM crashes.

A prime example of the sort of trap waiting for you is the very common case of handling a media player "finished" event
so that you can then play the next item in a play-list:

```
mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
    @Override
    public void finished(MediaPlayer mediaPlayer) {
        mediaPlayer.media().play(nextMrl); // <-- This is VERY BAD INDEED
    }
});
```

In this example, the `finished` method is being invoked on a native callback thread owned by LibVLC. The implementation
of this method is calling back into LibVLC when it invokes `play`. This is very likely to cause a JVM crash and
kill your application.

In cases such as this, you should make use of an asynchronous task-executor queue conveniently provided by the 
`MediaPlayer` object passed to the listener method:

```
mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
    @Override
    public void finished(final MediaPlayer mediaPlayer) {
        mediaPlayer.submit(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.media().play(nextMrl);
            }
        });
    }
});
```

You should *not* use this mechanism for *all* of your event handlers, *only those that will call back into LibVLC*.

Other high-level vlcj components may also provide their own asynchronous task executor, it is not limited to the media
player.

An added caveat for vlcj-4 is that when you implement event handling you must be sure to execute quickly, and to not
block the native thread with any long-running operation.

Your event handler implementations must *not* throw an `Exception`, failure of your event handlers to catch and handle
any thrown exception may prevent other listeners from being notified of the event.

If you are attempting to use multiple media players in your application, or using media players from multiple threads,
you may need to take some extra care so that you do not have multiple threads calling into LibVLC concurrently. You may
encounter subtle bugs and races that are very difficult to diagnose.

In addition, you must take care not to update Swing UI components from the native thread - all Swing UI updates are
supposed to go via the Swing Event Dispatch Thread (EDT).
 
You can achieve this in the usual way by using `SwingUtilities#invokeLater` in your event handler:

```
mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
    @Override
    public void finished(MediaPlayer mediaPlayer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // ...change UI state here...
            }
        });
    }
});
```

Garbage Collection
------------------

This section is also very important.

Ordinarily when developing with Java you will be used to not thinking about the scope and life-cycle of the objects that
you create, instead you will rely on the garbage collector in the Java Virtual Machine to just take of things for you.

With vlcj's `MediaPlayerFactory`, `MediaPlayer`, and associated classes, you must take care to prevent those objects
from being garbage collected - if you do not, at best your media player will simply unexpectedly stop working and at
worst you may see a fatal JVM crash.

Those vlcj objects wrap a native resource (e.g. a native media player). Those media player resources know nothing about
any JVM. So just because a native media player is still "alive" it will not prevent your object instance from being
garbage collected. If your object instance does get garbage collected, the native resource still has no idea, and will
will keeping sending native events back to the JVM via a callback. If your object is gone, that native callback has
nowhere to go and will most likely crash your JVM.

A very common mistake is to declare vlcj objects on the local heap in some sort of initialisation method:

```
    private void setup() {
        MediaPlayerFactory factory = new MediaPlayerFactory();
        EmbeddedMediaPlayer mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        // ... other initialisation ...
    }
```

When this method returns, the `factory` and `mediaPlayer` objects go out of scope and become eligible for garbage
collection. The garbage collection may happen immediately, or some time later.

The most common solution is to change those local heap declarations to class fields:

```
    private MediaPlayerFactory factory;

    private EmbeddedMediaPlayer mediaPlayer;

    private void setup() {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        // ... other initialisation ...
    }

```

This is fine and will work in most cases, but you must still make sure that the enclosing class does not itself get
garbage collected!

See this [vlcj garbage collection tutorial](http://capricasoftware.co.uk/projects/vlcj-4/tutorials/garbage-collection)
for more information.

Privacy Considerations
----------------------

When parsing media, depending on configuration, it may be possible that a remote network access is made for meta data
and album/cover art. This may unintentionally expose sensitive data regarding the media being parsed.

To affirmatively prevent all network access for meta data, consider using the `--no-metadata-network-access` argument
when creating a `MediaPlayerFactory`.

It should also be possible to prevent such network accesses by using appropriate `ParseFlag` values when requesting to
parse media.

Even with network access disabled, some media cover art may still appear locally (e.g. ~/.cache/vlc) - this does not
necessarily mean that a remote network request was made for the cover art, rather the art that was already embedded in
the media file was extracted to this temporary cache directory.

In any case, you need to be aware of this issue and inform users of your application about it.

Documentation
-------------

The vlcj project page is at [github](http://caprica.github.com/vlcj "vlcj at github").

Online Javadoc is available here:

* [5.0.0 (snapshot)](http://caprica.github.com/vlcj/javadoc/5.0.0/index.html "5.0.0 Javadoc")

Examples
--------

There are many examples in the [vlcj-examples](https://github.com/caprica/vlcj-examples) project showing how to use
vlcj.

For a more complete example of a feature-rich media player built with vlcj,
see [vlcj-player](https://github.com/caprica/vlcj-player).

Related Projects
----------------

 * [vlcj-natives](https://github.com/caprica/vlcj-natives)
 * [vlcj-examples](https://github.com/caprica/vlcj-examples)
 * [vlcj-subs](https://github.com/caprica/vlcj-subs)
 * [vlcj-player](https://github.com/caprica/vlcj-player)
 * [vlcj-javafx](https://github.com/caprica/vlcj-javafx)
 * [vlcj-javafx-demo](https://github.com/caprica/vlcj-javafx-demo)
 * [vlcj-lwjgl-demo](https://github.com/caprica/vlcj-lwjgl-demo)
 * [vlcj-mrls](https://github.com/caprica/vlcj-mrls)
 * [vlcj-file-filters](https://github.com/caprica/vlcj-file-filters)
 * [vlcj-swt](https://github.com/caprica/vlcj-swt)
 * [vlcj-swt-demo](https://github.com/caprica/vlcj-swt-demo)
 * [vlcj-swt-swing](https://github.com/caprica/vlcj-swt-swing)
 * [vlcj-info](https://github.com/caprica/vlcj-info)
 * [vlcj-radio-demo](https://github.com/caprica/vlcj-radio-demo)
 * [generator-vlcj](https://github.com/caprica/generator-vlcj)

Support
-------

Free support for Open Source and non-commercial projects is generally provided - you can
use [github issues](https://github.com/caprica/vlcj/issues "vlcj github issues") for this purpose.

Commercial services for vlcj are provided by [Caprica Software](http://www.capricasoftware.co.uk).

Support for commercial projects is provided exclusively on commercial terms - send an email to the following address for
more information:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk

LibVLC Discord
-------
[![Join the chat at https://discord.gg/3h3K3JF](https://img.shields.io/discord/716939396464508958?label=discord)](https://discord.gg/3h3K3JF)

vlcj is part of the LibVLC Discord Community server. Feel free to come say hi!

License
-------

The vlcj framework is provided under the GPL, version 3 or later.

If you want to consider a commercial license for vlcj that allows you to use and redistribute vlcj without complying
with the GPL then send an email to the address below:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk

Contributors
------------

Contributions are welcome and will always be licensed according to the Open Source license terms of the project (currently GPL).

However, for a contribution to be accepted you must agree to transfer any copyright so that your contribution does not
impede our ability to provide commercial licenses for vlcj.
