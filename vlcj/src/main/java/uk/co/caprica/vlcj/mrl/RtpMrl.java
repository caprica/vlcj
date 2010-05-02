package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media resource locator for RTP streams.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new RtpMrl().multicastAddress("234.0.0.1")
 *                          .port(5401)
 *                          .value();
 * </pre>
 * This will generate <code>"rtp://@234.0.0.1:5401"</code>.
 */
public class RtpMrl implements Mrl {

  private static final String RTP_TYPE = "rtp";
  
  private String multicastAddress;
  private int port;

  private String value;
  
  
  public RtpMrl multicastAddress(String multicastAddress) {
    this.multicastAddress = multicastAddress;
    return this;
  }

  public RtpMrl port(int port) {
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
