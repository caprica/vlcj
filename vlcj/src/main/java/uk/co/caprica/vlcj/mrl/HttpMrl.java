package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of an HTTP media resource locator.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new HttpMrl().host("www.myhost.com")
 *                           .port("8080")
 *                           .path("/media/example.mp4")
 *                           .value();
 * </pre>
 * This will generate <code>"http://www.myhost.com:8080/media/example.mp4"</code>.
 */
public class HttpMrl extends WebMrl {

  private static final String HTTP_TYPE = "http";
  
  public HttpMrl() {
    type(HTTP_TYPE);
  }
}
