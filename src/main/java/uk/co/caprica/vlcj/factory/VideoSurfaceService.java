package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentIdVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.ComponentVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.mac.MacVideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.windows.WindowsVideoSurfaceAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.awt.*;

public final class VideoSurfaceService extends BaseService {

    VideoSurfaceService(MediaPlayerFactory factory) {
        super(factory);
    }

    /**
     * Create a new video surface for a Component.
     * <p>
     * The optimal component in a {@link Canvas}, {@link Window} can be used, as can any other AWT {@link Component}, at
     * least in principle.
     * </p>
     *
     * @param component component
     * @return video surface
     */
    public ComponentVideoSurface newVideoSurface(Component component) {
        VideoSurfaceAdapter videoSurfaceAdapter;
        if (RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        } else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        } else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        } else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        return new ComponentVideoSurface(component, videoSurfaceAdapter);
    }

    /**
     * Create a new video surface for a native component id.
     *
     * @param componentId native component id
     * @return video surface
     */
    public ComponentIdVideoSurface newVideoSurface(long componentId) {
        VideoSurfaceAdapter videoSurfaceAdapter;
        if(RuntimeUtil.isNix()) {
            videoSurfaceAdapter = new LinuxVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isWindows()) {
            videoSurfaceAdapter = new WindowsVideoSurfaceAdapter();
        }
        else if(RuntimeUtil.isMac()) {
            videoSurfaceAdapter = new MacVideoSurfaceAdapter();
        }
        else {
            throw new RuntimeException("Unable to create a media player - failed to detect a supported operating system");
        }
        ComponentIdVideoSurface videoSurface = new ComponentIdVideoSurface(componentId, videoSurfaceAdapter);
        return videoSurface;
    }

}
