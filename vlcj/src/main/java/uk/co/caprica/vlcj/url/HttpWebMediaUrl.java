package uk.co.caprica.vlcj.url;

/**
 * Implementation of an HTTP media URL.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new HttpWebMediaUrl().host("www.myhost.com")
 *                                   .port("8080")
 *                                   .path("/media/example.mp4")
 *                                   .value();
 * </pre>
 * This will generate <code>"http://www.myhost.com:8080/media/example.mp4"</code>.
 */
public class HttpWebMediaUrl extends WebMediaUrl {

  private static final String HTTP_TYPE = "http";
  
  public HttpWebMediaUrl() {
    type(HTTP_TYPE);
  }
}
