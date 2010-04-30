package uk.co.caprica.vlcj.url;

/**
 * Implementation of a media URL for DVDs with menus.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new DvdMediaUrl().device("/media/dvd")
 *                               .rawDevice("/dev/cdrom0")
 *                               .title(0)
 *                               .chapter(3)
 *                               .angle(1)
 *                               .value();
 * </pre>
 * 
 * This will generate <code>"dvd:///media/dvd/@dev/cdrom@0:3:1"</code>.
 */
public class DvdMediaUrl extends BaseDvdMediaUrl {

  private static final String DVD_TYPE = "dvd";

  public DvdMediaUrl() {
    type(DVD_TYPE);
  }
}
