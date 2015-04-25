![vlcj](https://github.com/caprica/vlcj/raw/master/etc/vlcj-logo.png "vlcj")

vlcj
====

The vlcj project provides a Java framework to allow an instance of a native
[VLC](http://www.videolan.org/vlc "VLC") media player to be embedded in a Java
AWT Window or Swing JFrame.

You get more than just simple bindings, you also get a higher level framework
that hides a lot of the complexities of working with LibVLC.

vlcj is primarily developed and therefore extensively tested on Linux - it does
also work just fine on Windows and MacOSX.

At least JDK 1.6 is required, and it works without changes on JDK 1.7+.

This is the open source vlcj project page, see also the 'official'
[home page](http://capricasoftware.co.uk/#/projects/vlcj "Official vlcj home page at Caprica Software")
where you can find more information as well as some new simple tutorials.

Version 3.0.0+ of vlcj requires version 2.1.0+ of VLC, earlier versions of VLC
are *not* supported and will *not* work.

Version 3.0.0+ of vlcj requires version 3.5.2 of JNA.

Some features of version 3.0.0+ of vlcj (such as the new audio equalizer API)
require version 2.2.0+ of VLC (which has *not* yet been released).

If you still need to use VLC 2.0.x then you must stay with vlcj 2.x.x instead
of upgrading to the new vlcj 3.0.0 series.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj</artifactId>
    <version>3.7.0</version>
</dependency>
```

News
----

25/04/2015 Made new release 3.7.0 at [Maven Central](http://search.maven.org/#search|ga|1|vlcj),
check the github issues milestone for details. Play media using Java IO (LibVLC 3.0.0+)! Minor
fixes and improvements.

10/03/2015 Made new release 3.6.0 at [Maven Central](http://search.maven.org/#search|ga|1|vlcj),
check the github issues milestone for details. Minor fixes and improvements.

02/03/2015 vlcj-pro released, free time-limited trial packages are available from the official
[vlcj-pro](http://t.co/Gv04rAta9c) product page.

Version 3.0.0+ of vlcj requires version 2.1.0+ of vlc, earlier versions of vlc
are *not* supported and will *not* work.

Some features provided by vlcj depend on the runtime version of vlc. The most current release of
vlcj often tracks the bleeding edge of VLC development, consequently some features provided by
vlcj will require a very recent version of VLC and in some cases a pre-release version of VLC
may be needed. Where the runtime version of VLC is not new enough, in most cases vlcj attempts to
fail-safe. 

See also [vlcj-info](https://github.com/caprica/vlcj-info) for a small library that can extract
information from local media files using the MediaInfo library.

You can also follow @capricasoftware on Twitter for more vlcj news.

Documentation
-------------

Some new tutorials are available at the [official project page](http://capricasoftware.co.uk/#/projects/vlcj/tutorial).

The vlcj project page is at [github](http://caprica.github.com/vlcj "vlcj at github").

Online Javadoc is available:

* [3.6.0 (current)](http://caprica.github.com/vlcj/javadoc/3.6.0/index.html "3.6.0 Javadoc")
* [3.5.0](http://caprica.github.com/vlcj/javadoc/3.5.0/index.html "3.5.0 Javadoc")
* [3.4.0](http://caprica.github.com/vlcj/javadoc/3.4.0/index.html "3.4.0 Javadoc")
* [3.3.0](http://caprica.github.com/vlcj/javadoc/3.3.0/index.html "3.3.0 Javadoc")
* [3.2.0](http://caprica.github.com/vlcj/javadoc/3.2.0/index.html "3.2.0 Javadoc")
* [3.1.0](http://caprica.github.com/vlcj/javadoc/3.1.0/index.html "3.1.0 Javadoc")
* [3.0.0](http://caprica.github.com/vlcj/javadoc/3.0.0/index.html "3.0.0 Javadoc")
* [2.4.0](http://caprica.github.com/vlcj/javadoc/2.4.0/index.html "2.4.0 Javadoc")
* [2.3.0](http://caprica.github.com/vlcj/javadoc/2.3.0/index.html "2.3.0 Javadoc")
* [2.2.0](http://caprica.github.com/vlcj/javadoc/2.2.0/index.html "2.2.0 Javadoc")
* [2.1.0](http://caprica.github.com/vlcj/javadoc/2.1.0/index.html "2.1.0 Javadoc")
* [2.0.0](http://caprica.github.com/vlcj/javadoc/2.0.0/index.html "2.0.0 Javadoc")

Examples
--------

There are many examples in the vlcj test sources showing how to use vlcj.

For a more complete example of a feature-rich media player built with vlcj, 
see [vlcj-player](https://github.com/caprica/vlcj-player).

Support
-------

Development of vlcj is carried out by [Caprica Software](http://www.capricasoftware.co.uk).

Free support for Open Source and non-commercial projects is generally provided - you
can use [github issues](https://github.com/caprica/vlcj/issues "vlcj github issues")
for this purpose.

Support for commercial projects is provided exclusively on commercial terms -
send an email to the following address for more information:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk

License
-------

The vlcj framework is provided under the GPL, version 3 or later.

If you want to consider a commercial license for vlcj that allows you to use and
redistribute vlcj without complying with the GPL then send your proposal to:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk
