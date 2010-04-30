package uk.co.caprica.vlcj.url;

/**
 * Implementation of an HTTP media URL.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new RtspWebMediaUrl().host("www.myhost.com")
 *                                   .port("8888")
 *                                   .path("/example")
 *                                   .value();
 * </pre>
 * This will generate <code>"rtsp://www.myhost.com:8888/example"</code>.
 */
public class RtspWebMediaUrl extends WebMediaUrl {

  private static final String RTSP_TYPE = "rtsp";
  
  public RtspWebMediaUrl() {
    type(RTSP_TYPE);
  }
}
