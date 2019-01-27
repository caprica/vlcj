![vlcj](https://github.com/caprica/vlcj/raw/master/etc/vlcj-logo.png "vlcj")

*You are currently looking at the development branch for vlcj-4.0.0, if you want a stable version of vlcj you should
switch to the [vlcj-3.x branch](https://github.com/caprica/vlcj/tree/vlcj-3.x).* 

vlcj
====

The vlcj project provides a Java framework to allow an instance of a native [VLC](http://www.videolan.org/vlc "VLC")
media player to be embedded in a Java application.

You get more than just simple bindings, you also get a higher level framework that hides a lot of the complexities of
working with LibVLC.

vlcj is primarily developed and therefore extensively tested on Linux - it does also work just fine on Windows and
OSX, although there may be some limitations on OSX.

At least JDK 1.6 is required.

*This version of vlcj requires VLC 3.0.0 as a minimum, no earlier version is supported.*

This is the open source vlcj project page, see also the 'official'
[home page](http://capricasoftware.co.uk/projects/vlcj "Official vlcj home page at Caprica Software") where you can find
more information as well as some new simple tutorials.

vlcj-4
======

This is the new development branch for the next major release of vlcj. Most things will stay the same but vlcj is
undergoing a significant refactoring exercise for the long-term health of the project and ease of further development.

As such, at the present time the API is fluid and completely subject to change. If you are migraging from vlcj-3 then
you will have to make changes to your own code. The implementation behind the API is pretty much the same as it ever
was, although there have been and will continue to be significant improvements in some areas, nevertheless if you are
using this branch you do so at your own risk.

Any issues raised pertaining to these API changes will be summarily deleted, perhaps with a snarky comment, although
sensible suggestions for improvements and genuine problems (i.e. problems other than "why did this change?" and "my code
doesn't compile any more") are still welcome. 

At the present time there is no cut-off for API changes or in fact any other breaking changes, so use this branch at
your own risk!

This branch will also introduce significant new features with new API, some headline new features are:

 - 360 degree video, changing pitch, yaw, roll etc
 - new API for alternate renderers (e.g. you can now send your media to Chromecast)
 - integrated native dialog callbacks (e.g. you can now be prompted for credentials when accessing a protected stream)
 - use any AWT Component as a video surface, not just a Canvas (Window should work on OSX)

For a full list of changes in this release, check the [release milestone](https://github.com/caprica/vlcj/milestone/14).

Building vlcj - sun.misc.Unsafe
-------------------------------

Currently the target supported JDK is still 1.6, since there are no new language or platform features used in vlcj
that need anything past 1.6. There is particularly strong reason to keep supporting 1.6, but there is no particular
reason to abandon it either.

On the other hand, the project is at the moment built with OpenJDK 11 on Linux and cross-compiled to 1.6. This will
work just fine when using Maven to build the project from the command-line, or when working with Eclipse.

However, if you use IntelliJ IDEA you may encounter some compilation problems...

When compiling, IDEA will complain that package sun.misc does not exist - the Unsafe class from this package is
required for the "direct" media players.

This can be worked around in a number of ways:

 - use source and target JDK 1.10 in the pom.xml, which IDEA will then incorporate into the project
 - use JDK 1.9 and convert the project to use the Java Module System and add jdk.unsupported as a required module
 - change the IDEA compiler settings to *uncheck* the "Use '--release' option for cross-compilation (Java 9 and later)"

The latter option is probably the simplest to deal with.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj</artifactId>
    <version>4.0.0</version>
</dependency>
```

*This artefact may not be available for quite some time, we do not push snapshot releases to Maven Central.*

News
----

You can follow @capricasoftware on Twitter for more vlcj news.

Documentation
-------------

Some new tutorials are available at the [official project page](http://capricasoftware.co.uk/projects/vlcj/tutorials).

The vlcj project page is at [github](http://caprica.github.com/vlcj "vlcj at github").

Online Javadoc is *not yet available* but will appear here in due course:

* [4.0.0 (current)](http://caprica.github.com/vlcj/javadoc/4.0.0/index.html "4.0.0 Javadoc")

Examples
--------

There are many examples in the vlcj test sources showing how to use vlcj.

For a more complete example of a feature-rich media player built with vlcj,
see [vlcj-player](https://github.com/caprica/vlcj-player).

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
