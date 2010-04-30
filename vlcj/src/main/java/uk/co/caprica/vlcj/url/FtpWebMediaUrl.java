package uk.co.caprica.vlcj.url;

/**
 * Implementation of an HTTP media URL.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new FtpWebMediaUrl().host("www.myhost.com")
 *                                  .port("21")
 *                                  .path("/media/example.mp4")
 *                                  .value();
 * </pre>
 * This will generate <code>"ftp://www.myhost.com:21/media/example.mp4"</code>.
 */
public class FtpWebMediaUrl extends WebMediaUrl {

  private static final String FTP_TYPE = "ftp";
  
  public FtpWebMediaUrl() {
    type(FTP_TYPE);
  }
}
