package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of an MMS media resource locator.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new MmsMrl().host("www.myhost.com")
 *                          .port("8000")
 *                          .path("/mediainfo")
 *                          .value();
 * </pre>
 * This will generate <code>"mms://www.myhost.com:8000/mediainfo"</code>.
 */
public class MmsMrl extends WebMrl {

  private static final String MMS_TYPE = "mms";
  
  public MmsMrl() {
    type(MMS_TYPE);
  }
}
