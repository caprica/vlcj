package uk.co.caprica.vlcj.player.base;

import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_video_logo_option_t;
import uk.co.caprica.vlcj.player.Logo;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public final class LogoService extends BaseService {

    LogoService(DefaultMediaPlayer mediaPlayer) {
        super(mediaPlayer);
    }

    /**
     * Enable/disable the logo.
     * <p>
     * The logo will not be enabled if there is currently no video being played.
     *
     * @param enable <code>true</code> to show the logo, <code>false</code> to hide it
     */
    public void enableLogo(boolean enable) {
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_enable.intValue(), enable ? 1 : 0);
    }

    /**
     * Set the logo opacity.
     *
     * @param opacity opacity in the range 0 to 255 where 255 is fully opaque
     */
    public void setLogoOpacity(int opacity) {
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacity);
    }

    /**
     * Set the logo opacity.
     *
     * @param opacity opacity percentage in the range 0.0 to 1.0 where 1.0 is fully opaque
     */
    public void setLogoOpacity(float opacity) {
        int opacityValue = Math.round(opacity * 255.0f);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_opacity.intValue(), opacityValue);
    }

    /**
     * Set the logo location.
     *
     * @param x x co-ordinate for the top left of the logo
     * @param y y co-ordinate for the top left of the logo
     */
    public void setLogoLocation(int x, int y) {
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_x.intValue(), x);
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_y.intValue(), y);
    }

    /**
     * Set the logo position.
     *
     * @param position position
     */
    public void setLogoPosition(libvlc_logo_position_e position) {
        libvlc.libvlc_video_set_logo_int(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_position.intValue(), position.intValue());
    }

    /**
     * Set the logo file.
     *
     * @param logoFile logo file name
     */
    public void setLogoFile(String logoFile) {
        libvlc.libvlc_video_set_logo_string(mediaPlayerInstance, libvlc_video_logo_option_t.libvlc_logo_file.intValue(), logoFile);
    }

    /**
     * Set the logo image.
     * <p>
     * The image will first be written to a temporary file, before invoking
     * {@link #setLogoFile(String)}. This is not optimal, but creating a temporary file for the logo
     * in this way is unavoidable.
     * <p>
     * The temporary file will persist until the JVM exits. The file can not be deleted immediately
     * due to the asynchronous nature of the native API call that sets the logo from the file.
     * <p>
     * There are circumstances under which this temporary file may <em>fail</em> to be deleted, as
     * per {@link File#deleteOnExit()} - i.e. "Deletion will be attempted only for normal termination
     * of the virtual machine".
     *
     * @param logoImage logo image
     */
    public void setLogoImage(RenderedImage logoImage) {
        File file = null;
        try {
            // Create a temporary file for the logo...
            file = File.createTempFile("vlcj-logo-", ".png");
            ImageIO.write(logoImage, "png", file);
            if (file.exists()) {
                // ...then set the logo as normal
                setLogoFile(file.getAbsolutePath());
                // Flag the temporary file to be deleted when the JVM exits - the file can not be
                // deleted immediately because setLogoFile ultimately invokes an asynchronous
                // native method to set the logo from the file
                file.deleteOnExit();
            }
        }
        catch(IOException e) {
            throw new RuntimeException("Failed to set logo image", e);
        }
    }

    /**
     * Set a logo.
     *
     * @param logo logo
     */
    public void setLogo(Logo logo) {
        logo.apply(mediaPlayer);
    }

}
