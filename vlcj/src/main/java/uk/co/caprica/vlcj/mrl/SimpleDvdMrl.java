package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media resource locator for DVDs without menus.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new SimpleDvdMrl().device("/media/dvd")
 *                                .rawDevice("/dev/cdrom0")
 *                                .title(0)
 *                                .chapter(3)
 *                                .angle(1)
 *                                .value();
 * </pre>
 * This will generate <code>"dvdsimple:///media/dvd/@dev/cdrom@0:3:1"</code>.
 */
public class SimpleDvdMrl extends BaseDvdMrl {

  private static final String DVD_TYPE = "dvdsimple";

  public SimpleDvdMrl() {
    type(DVD_TYPE);
  }
}
