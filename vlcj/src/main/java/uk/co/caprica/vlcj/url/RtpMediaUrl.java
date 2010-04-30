package uk.co.caprica.vlcj.url;

/**
 * Implementation of a media URL for RTP streams.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new RtpMediaUrl().multicastAddress("234.0.0.1")
 *                               .port(5401)
 *                               .value();
 * </pre>
 * This will generate <code>"rtp://@234.0.0.1:5401"</code>.
 */
public class RtpMediaUrl implements MediaUrl {

  private static final String RTP_TYPE = "rtp";
  
  private String multicastAddress;
  private int port;

  private String value;
  
  
  public RtpMediaUrl multicastAddress(String multicastAddress) {
    this.multicastAddress = multicastAddress;
    return this;
  }

  public RtpMediaUrl port(int port) {
    this.port = port;
    return this;
  }
  
  @Override
  public String value() {
    if(value == null) {
      value = constructValue();
    }
    return value;
  }

  private String constructValue() {
    StringBuilder sb = new StringBuilder(40);
    sb.append(RTP_TYPE);
    sb.append("://@");
    sb.append(multicastAddress);
    sb.append(':');
    sb.append(port);
    return sb.toString();
  }
}
