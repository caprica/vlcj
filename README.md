![vlcj](https://github.com/caprica/vlcj/raw/master/etc/vlcj-logo.png "vlcj")

*You are currently looking at the development branch for vlcj-4.0.0, if you are looking for the previous version of vlcj
you should switch to the [vlcj-3.x branch](https://github.com/caprica/vlcj/tree/vlcj-3.x).* 

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

At least JDK 1.6 is required.

*This version of vlcj requires VLC 3.0.0 as a minimum, no earlier version is supported.*

This is the open source vlcj project page, see also the 'official'
[home page](http://capricasoftware.co.uk/projects/vlcj "Official vlcj home page at Caprica Software") where you can find
more information as well as some new simple tutorials.

News
====

- 5th April, 2019 - vlcj 4.1.0 release, this release brings JNA "direct mapping" which should give a modest performance boost
- 1st April, 2019 - vlcj 4.0.8 released, fixes an issue with the BufferedImage in the CallbackMediaPlayerComponent
- 24th March, 2019 - vlcj 4.0.7 released, minor change to allow to change the callback video surface image painter after creation
- 18th March, 2019 - published a Yeoman generator for vlcj starter projects, see [generator-vlcj](https://github.com/caprica/generator-vlcj)
- 5th March, 2019 - vlcj 4.0.6 released, fixes issues with image painters when using CallbackMediaPlayerComponent
- 4th March, 2019 - vlcj 4.0.5 released, fixes a problem using ByteBuffer on Java8
- 4th March, 2019 - vlcj 4.0.4 released, audio callbacks can now optionally manage audio volume, some minor API changes
- 1st March, 2019 - vlcj 4.0.3 released, fixes a problem with native discovery directory providers and minor API changes
- 28th February, 2019 - vlcj 4.0.2 released, resolves a potential deadlock (upgrade to this version strongly recommended)
- 27th February, 2019 - vlcj 4.0.1 released, primarily fixes a small number of minor public API issues
- 20th February, 2019 - vlcj 4.0.0 released

All releases are at available at [Maven Central](https://search.maven.org/search?q=a:vlcj).

You can follow @capricasoftware on Twitter for more vlcj news.

vlcj-4
======

vlcj-4 has a new API, but there is still a lot of similarity with vlcj-3 and under the covers there is still mostly the
same mature and stable implementation.

The vlcj-4 API is now pretty much stable, although some changes may be made depending on feedback and usage after the
first release of 4.0.0 is let loose into the wild - but no further *major* API changes are planned or expected.

If you are interested in using vlcj-4, now is a good time to start.

vlcj-4.1
========

vlcj-4.1 brings JNA "direct mapping" which should give a modest performance boost at the expense of less helpful error
messages if things go wrong when loading the native LibVLC library.

If you wish to stay with "traditional" JNA bindings, then stick with the latest vlcj-4.0.x.

This direct mapping approach will be used in all future versions of vlcj, meaning vlcj-4.1.x, vlcj-5.x and later.

Major New Features
------------------

Headline changes:

 - full support for 360 degree video, changing pitch, yaw, roll, field-of-view
 - full support for discovery and usage of alternate media renderers, e.g. Chromecast
 - full support for media-slave API to set subtitle tracks and additional/alternate audio tracks
 - full support for integrated native dialogs, e.g. you can now be prompted for credentials when accessing a protected
   stream
 - use any AWT Component as a video surface, not just a Canvas (Window will work on OSX, with limitations)
 - easy to add support for alternate video surfaces, e.g. an SWT Composite
 - major changes and improvements to the so-called "direct-rendering" media players, the direct audio and video media
   players are no longer separate components and are now instead intrinsic to the standard media player. For video, a
   new "Callback" video surface brings a vastly improved implementation, an optional related component provides a good
   default implementation for direct-rendering and an easy way to deal with re-sizing of the video, with easy extension
   points for custom video "painters"
 - improvements to full-screen support with sensible default implementations provided for Linux, Windows and OSX, all
   using a native solution to provide the best result
 - automatic handling of subitems (e.g. when playing a YouTube video or a streaming playlist) is now intrinsic to the
   media player and requires no involvement of the client application
 - simplified native library discovery, now intrinsic to the media player factory and it should just work out-of-the-box
   in the vast majority of cases
 - API support for multiple logos (in series, not concurrent)
 - logo and marquee now work without having to explicitly enable the respective native modules
 - there is now better support for media generally (e.g. using media without a media player, for parsing meta data etc),
   and also better support for media-lists (e.g. it should now be easier to manage your own play-lists)

There have also been a lot of more general improvements to freshen up the codebase, make it more maintainable for the
future, and to clear some legacy issues that have dogged the project for quite some time.

For a full list of changes in this release, check the release milestones:

- [vlcj 4.0.0 release milestone](https://github.com/caprica/vlcj/milestone/14?closed=1)
- [vlcj 4.0.1 release milestone](https://github.com/caprica/vlcj/milestone/32?closed=1)
- [vlcj 4.0.2 release milestone](https://github.com/caprica/vlcj/milestone/33?closed=1)
- [vlcj 4.0.3 release milestone](https://github.com/caprica/vlcj/milestone/34?closed=1)
- [vlcj 4.0.4 release milestone](https://github.com/caprica/vlcj/milestone/35?closed=1)
- [vlcj 4.0.5 release milestone](https://github.com/caprica/vlcj/milestone/36?closed=1)
- [vlcj 4.0.6 release milestone](https://github.com/caprica/vlcj/milestone/37?closed=1)
- [vlcj 4.0.7 release milestone](https://github.com/caprica/vlcj/milestone/38?closed=1)
- [vlcj 4.0.8 release milestone](https://github.com/caprica/vlcj/milestone/40?closed=1)

vlcj 4.1.0+ uses JNA direct-mapping:

- [vlcj 4.1.0 release milestone](https://github.com/caprica/vlcj/milestone/39?closed=1)

Despite all of these changes, running on JDK 1.6 is still supported!

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

API Breakage (vlcj-3)
---------------------

This is the *tenth year* of the project, the API has been pretty much static for that entire time. The codebase has
evolved gradually and incrementally in that time and resulted sometimes in sub-optimal implementations and choices, as
well as some generally unwieldy individual classes containing literally thousands of lines of code. Keeping the API
fixed for those almost ten years also locked in some long-standing architectural issues that simply could not be
resolved.

The decision to break backwards compatibility with the vlcj-3 API was not taken lightly, but the results have been worth
it. All legacy architectural issues have been resolved, the giant god-classes have been factored to more manageable
chunks, and ongoing maintenance will be much easier.

The price for these improvements is some API breakage, sorry.

The short version of the situation is that vlcj-4 can *not* be considered a drop-in upgrade for any vlcj-3 applications.
If you want to move to vlcj-4 with your existing applications, you *will* be impacted, at best you will need to use new
names for existing methods, at worst although very unlikely you may have to make some *deep* changes in your own
codebase. 

There is no automatic migration tool.

The longer version of the situation is documented more fully in
[this ticket](https://github.com/caprica/vlcj/issues/681). 

Tutorials
---------

New tutorials for vlcj-4 are available [here](http://capricasoftware.co.uk/projects/vlcj-4/tutorials).

There are simple tests or demo applications available for pretty much every aspect of vlcj functionality, these are
provided in the
[project test sources](https://github.com/caprica/vlcj/tree/master/src/test/java/uk/co/caprica/vlcj/test).

There is also a major demo application available at the [vlcj-player](https://github.com/caprica/vlcj-player) project
page.

Building vlcj - sun.misc.Unsafe
-------------------------------

Currently the target supported JDK is still 1.6, since there are no new language or platform features used in vlcj
that need anything past 1.6. There is no particularly strong reason to keep supporting 1.6, but there is no particular
reason to abandon it either.

On the other hand, the project is at the moment built with OpenJDK 11 on Linux and cross-compiled to 1.6. This will
work just fine when using Maven to build the project from the command-line, or when working with Eclipse.

However, if you use IntelliJ IDEA you may encounter some compilation problems...

When compiling, IDEA will complain that package sun.misc does not exist - the `Unsafe` class from this package is
required for the "direct" media players.

This can be worked around in a number of ways:

 - use source and target JDK 1.10 in the pom.xml, which IDEA will then incorporate into the project
 - use JDK 1.9 and convert the project to use the Java Module System and add jdk.unsupported as a required module
 - change the IDEA compiler settings to *uncheck* the "Use '--release' option for cross-compilation (Java 9 and later)"

The latter option is probably the simplest to deal with.

When compiling with Maven it is simply not possible to suppress the warnings about using sun.misc.Unsafe.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj</artifactId>
    <version>4.1.0</version>
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

Tutorials will be made available, not yet, at the
[official project page](http://capricasoftware.co.uk/projects/vlcj/tutorials).

The vlcj project page is at [github](http://caprica.github.com/vlcj "vlcj at github").

Online Javadoc is available here:

JNA direct-mapping:

* [4.1.0 (current)](http://caprica.github.com/vlcj/javadoc/4.1.0/index.html "4.1.0 Javadoc")

JNA traditional mapping:

* [4.0.8 (current)](http://caprica.github.com/vlcj/javadoc/4.0.8/index.html "4.0.8 Javadoc")
* [4.0.7](http://caprica.github.com/vlcj/javadoc/4.0.7/index.html "4.0.7 Javadoc")
* [4.0.6](http://caprica.github.com/vlcj/javadoc/4.0.6/index.html "4.0.6 Javadoc")
* [4.0.5](http://caprica.github.com/vlcj/javadoc/4.0.5/index.html "4.0.5 Javadoc")
* [4.0.4](http://caprica.github.com/vlcj/javadoc/4.0.4/index.html "4.0.4 Javadoc")
* [4.0.3](http://caprica.github.com/vlcj/javadoc/4.0.3/index.html "4.0.3 Javadoc")
* [4.0.2](http://caprica.github.com/vlcj/javadoc/4.0.2/index.html "4.0.2 Javadoc")
* [4.0.1](http://caprica.github.com/vlcj/javadoc/4.0.1/index.html "4.0.1 Javadoc")
* [4.0.0](http://caprica.github.com/vlcj/javadoc/4.0.0/index.html "4.0.0 Javadoc")

Examples
--------

There are many examples in the vlcj test sources showing how to use vlcj.

For a more complete example of a feature-rich media player built with vlcj,
see [vlcj-player](https://github.com/caprica/vlcj-player).

Related Projects
----------------

 * [vlcj-natives](https://github.com/caprica/vlcj-natives)
 * [vlcj-player](https://github.com/caprica/vlcj-player)
 * [vlcj-javafx](https://github.com/caprica/vlcj-javafx)
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

Development of vlcj is carried out by [Caprica Software](http://www.capricasoftware.co.uk).

Free support for Open Source and non-commercial projects is generally provided - you can
use [github issues](https://github.com/caprica/vlcj/issues "vlcj github issues") for this purpose.

Support for commercial projects is provided exclusively on commercial terms - send an email to the following address for
more information:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk

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
