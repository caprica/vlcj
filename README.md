![vlcj](https://github.com/caprica/vlcj/raw/master/etc/vlcj-logo.png "vlcj")

vlcj
====

The vlcj project provides a Java framework to allow an instance of a native
[vlc](http://www.videolan.org/vlc "vlc") media player to be embedded in a Java 
AWT Window or Swing JFrame. 

You get more than just simple bindings, you also get a higher level framework
that hides a lot of the complexities of working with libvlc.

vlcj is primarily developed and therefore extensively tested on Linux - it does
also work just fine on Windows and should work on Mac - but for Mac you likely
need to use release 2.0.3, 2.0.7 or later, *but not any version inbetween*.

At least JDK 1.6 is required, and it works without changes on JDK 1.7.

This is the open source vlcj project page, see also the 'official' 
[home page](http://www.capricasoftware.co.uk/vlcj "Official vlcj home page at Caprica Software")
where you can find more information as well as some new simple tutorials.

Note the git master branch is for vlcj version 3.0.0 (currently pre-release).

Please switch to the vlcj-2.0 branch for the latest official release.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj</artifactId>
    <version>3.0.0</version>
</dependency>
```

Note that some features of vlcj 3.0.0 and later (audio equalizer) require vlc 2.2.0 which has *not*
yet been released.

If you still need to use vlc 2.0.0 then you must stay with vlcj 2.x.x instead of upgrading to the
new vlcj 3.0.0 series.

News
----

??/??/201? Made new release 3.0.0 at [Maven Central](http://search.maven.org/#search|ga|1|vlcj), new audio equalizer API, new native full-screen implementation for Windows.

 * Note: vlcj 3.0.0 *requires* vlc 2.1.0 or later, earlier versions of vlc are not supported.
 * Note: vlcj now *requires* JNA 3.5.2 or later.
 * Note: the vlcj project page at googlecode should be considered for historical reference only.

You can also follow @capricasoftware on Twitter for more vlcj news.

Documentation
-------------

The vlcj project page is at [github](http://caprica.github.com/vlcj "vlcj at github").

Online Javadoc is available:

* [3.0.0 (next)](http://caprica.github.com/vlcj/javadoc/3.0.0/index.html "3.0.0 Javadoc")
* [2.4.0 (current)](http://caprica.github.com/vlcj/javadoc/2.4.0/index.html "2.4.0 Javadoc")
* [2.3.0](http://caprica.github.com/vlcj/javadoc/2.3.0/index.html "2.3.0 Javadoc")
* [2.2.0](http://caprica.github.com/vlcj/javadoc/2.2.0/index.html "2.2.0 Javadoc")
* [2.1.0](http://caprica.github.com/vlcj/javadoc/2.1.0/index.html "2.1.0 Javadoc")
* [2.0.0](http://caprica.github.com/vlcj/javadoc/2.0.0/index.html "2.0.0 Javadoc")

Documentation is being made available at [Caprica Software](http://www.capricasoftware.co.uk/projects/vlcj/index.html "Caprica Software"). 

Support
-------

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
