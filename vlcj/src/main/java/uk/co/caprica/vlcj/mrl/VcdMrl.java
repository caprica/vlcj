package uk.co.caprica.vlcj.mrl;

/**
 * Implementation of a media resource locator for Video CDs.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new VcdMrl().device("/media/dvd")
 *                          .startingPosition("S")
 *                          .number(1)
 *                          .value();
 * </pre>
 * This will generate <code>"vcd:///media/vcd/@S1"</code>.
 */
public class VcdMrl implements Mrl {

  private static final String VCD_TYPE = "vcd";
  
  private String device;
  private String startingPosition;
  private int number = -1;

  private String value;
  
  
  public VcdMrl device(String device) {
    this.device = device;
    return this;
  }

  public VcdMrl startingPosition(String startingPosition) {
    this.startingPosition = startingPosition;
    return this;
  }

  public VcdMrl number(int number) {
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
