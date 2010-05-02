package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media URL for SSM streams.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new RtpMediaUrl().serverAddress("myhost.com")
 *                               .multicastAddress("234.0.0.1")
 *                               .port(5401)
 *                               .value();
 * </pre>
 * This will generate <code>"rtp://myhost.com@234.0.0.1:5401"</code>.
 */
public class SsmMediaUrl implements MediaUrl {

  private static final String RTP_TYPE = "rtp";
  
  private String serverAddress;
  private String multicastAddress;
  private int port;

  private String value;
  
  public SsmMediaUrl serverAddress(String serverAddress) {
    this.serverAddress = serverAddress;
    return this;
  }
  
  public SsmMediaUrl multicastAddress(String multicastAddress) {
    this.multicastAddress = multicastAddress;
    return this;
  }

  public SsmMediaUrl port(int port) {
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
    sb.append("://");
    sb.append(serverAddress);
    sb.append('@');
    sb.append(multicastAddress);
    sb.append(':');
    sb.append(port);
    return sb.toString();
  }
}
