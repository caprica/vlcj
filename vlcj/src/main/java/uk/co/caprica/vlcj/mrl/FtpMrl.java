package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of an HTTP media resource locator.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new FtpMrl().host("www.myhost.com")
 *                          .port("21")
 *                          .path("/media/example.mp4")
 *                          .value();
 * </pre>
 * This will generate <code>"ftp://www.myhost.com:21/media/example.mp4"</code>.
 */
public class FtpMrl extends WebMrl {

  private static final String FTP_TYPE = "ftp";
  
  public FtpMrl() {
    type(FTP_TYPE);
  }
}
