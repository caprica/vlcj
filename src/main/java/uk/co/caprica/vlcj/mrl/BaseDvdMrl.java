package uk.co.caprica.vlcj.mrl;

/**
 * Base implementation of a media resource locator for DVD MRLs.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new BaseDvdMrl().type("dvdsimple").
 *                              .device("/media/dvd")
 *                              .rawDevice("/dev/cdrom0")
 *                              .title(0)
 *                              .chapter(3)
 *                              .angle(1)
 *                              .value();
 * </pre>
 * This will generate <code>"dvdsimple:///media/dvd/@dev/cdrom@0:3:1"</code>.
 */
public class BaseDvdMrl implements Mrl {

  private String type;
  private String device;
  private String rawDevice;
  private int title = -1;
  private int chapter = -1;
  private int angle = -1;

  private String value;
  
  public BaseDvdMrl type(String type) {
    this.type = type;
    return this;
  }
  
  public BaseDvdMrl device(String device) {
    this.device = device;
    return this;
  }

  public BaseDvdMrl rawDevice(String rawDevice) {
    this.rawDevice = rawDevice;
    return this;
  }
  
  public BaseDvdMrl title(int title) {
    this.title = title;
    return this;
  }

  public BaseDvdMrl chapter(int chapter) {
    this.chapter = chapter;
    return this;
  }

  public BaseDvdMrl angle(int angle) {
    this.angle = angle;
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
    sb.append(type);
    sb.append("://");
    sb.append(device);
    sb.append('@');
    sb.append(rawDevice);
    if(title != -1) {
      sb.append('@');
      sb.append(title);
      if(chapter != -1) {
        sb.append(':');
        sb.append(chapter);
        if(angle != -1) {
          sb.append(':');
          sb.append(angle);
        }
      }
    }
    return sb.toString();
  }
}
