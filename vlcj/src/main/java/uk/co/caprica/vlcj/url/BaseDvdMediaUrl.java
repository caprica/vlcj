package uk.co.caprica.vlcj.url;

/**
 * Base implementation of a media URL for DVD URLs.
 * <p>
 * This class provides a fluent API for initialising the URL, e.g.
 * <pre>
 * String url = new BaseDvdMediaUrl().type("dvdsimple").
 *                                   .device("/media/dvd")
 *                                   .rawDevice("/dev/cdrom0")
 *                                   .title(0)
 *                                   .chapter(3)
 *                                   .angle(1)
 *                                   .value();
 * </pre>
 * This will generate <code>"dvdsimple:///media/dvd/@dev/cdrom@0:3:1"</code>.
 */
public class BaseDvdMediaUrl implements MediaUrl {

  private String type;
  private String device;
  private String rawDevice;
  private int title = -1;
  private int chapter = -1;
  private int angle = -1;

  private String value;
  
  public BaseDvdMediaUrl type(String type) {
    this.type = type;
    return this;
  }
  
  public BaseDvdMediaUrl device(String device) {
    this.device = device;
    return this;
  }

  public BaseDvdMediaUrl rawDevice(String rawDevice) {
    this.rawDevice = rawDevice;
    return this;
  }
  
  public BaseDvdMediaUrl title(int title) {
    this.title = title;
    return this;
  }

  public BaseDvdMediaUrl chapter(int chapter) {
    this.chapter = chapter;
    return this;
  }

  public BaseDvdMediaUrl angle(int angle) {
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
