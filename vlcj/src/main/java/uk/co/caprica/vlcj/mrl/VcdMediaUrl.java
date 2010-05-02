package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media URL for Video CDs.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new VcdMediaUrl().device("/media/dvd")
 *                               .startingPosition("S")
 *                               .number(1)
 *                               .value();
 * </pre>
 * This will generate <code>"vcd:///media/vcd/@S1"</code>.
 */
public class VcdMediaUrl implements MediaUrl {

  private static final String VCD_TYPE = "vcd";
  
  private String device;
  private String startingPosition;
  private int number = -1;

  private String value;
  
  
  public VcdMediaUrl device(String device) {
    this.device = device;
    return this;
  }

  public VcdMediaUrl startingPosition(String startingPosition) {
    this.startingPosition = startingPosition;
    return this;
  }

  public VcdMediaUrl number(int number) {
    this.number = number;
    return this;
  }

  public String value() {
    if(value == null) {
      value = constructValue();
    }
    return value;
  }
  
  private String constructValue() {
    StringBuilder sb = new StringBuilder(40);
    sb.append(VCD_TYPE);
    sb.append("://");
    sb.append(device);
    sb.append('@');
    sb.append(startingPosition);
    if(number != -1) {
      sb.append(number);
    }
    return sb.toString();
  }
}
