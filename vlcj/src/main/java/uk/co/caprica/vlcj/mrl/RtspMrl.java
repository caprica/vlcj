package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of an HTTP media resource locator.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new RtspMrl().host("www.myhost.com")
 *                           .port("8888")
 *                           .path("/example")
 *                           .value();
 * </pre>
 * This will generate <code>"rtsp://www.myhost.com:8888/example"</code>.
 */
public class RtspMrl extends WebMrl {

  private static final String RTSP_TYPE = "rtsp";
  
  public RtspMrl() {
    type(RTSP_TYPE);
  }
}
