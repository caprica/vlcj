package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media resource locator for UDP streams.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new RtpMrl().groupAddress("234.0.0.1")
 *                          .port(1234)
 *                          .value();
 * </pre>
 * This will generate <code>"udp://@234.0.0.1:1234"</code>.
 * <p> 
 * <strong>UDP is deprecated in VLC.</strong>
 */
@Deprecated
public class UdpMrl implements Mrl {

  private static final String UDP_TYPE = "udp";
  
  private String groupAddress;
  private int port;

  private String value;
  
  public UdpMrl groupAddress(String groupAddress) {
    this.groupAddress = groupAddress;
    return this;
  }

  public UdpMrl port(int port) {
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
    sb.append(UDP_TYPE);
    sb.append("://@");
    sb.append(groupAddress);
    sb.append(':');
    sb.append(port);
    return sb.toString();
  }
}
