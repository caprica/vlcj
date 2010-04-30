package uk.co.caprica.vlcj.url;

/**
 * Implementation of an MMS media URL.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new MmsMediaUrl().host("www.myhost.com")
 *                                  .port("8000")
 *                                  .path("/mediainfo")
 *                                  .value();
 * </pre>
 * This will generate <code>"mms://www.myhost.com:8000/mediainfo"</code>.
 */
public class MmsMediaUrl extends WebMediaUrl {

  private static final String MMS_TYPE = "mms";
  
  public MmsMediaUrl() {
    type(MMS_TYPE);
  }
}
