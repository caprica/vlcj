package uk.co.caprica.vlcj.runtime.osx;

import com.sun.jna.Native;

import java.awt.*;
import java.lang.reflect.Method;

public final class OsxComponentId {

    public static long getOsxComponentId(Component component) {
        long componentId;
        // Try the usual method first, this should still work on JDK 1.6
        try {
            componentId = Native.getComponentID(component);
            if (componentId != 0) {
                return componentId;
            }
        }
        catch (Exception e) {
        }
        if (component instanceof Window) {
            Window window = (Window) component;
            try {
                Method getPeer = Window.class.getMethod("getPeer");
                Object peer = getPeer.invoke(window);
                Method getPlatformWindow = peer.getClass().getMethod("getPlatformWindow");
                Object platformWindow = getPlatformWindow.invoke(peer);
                Method getContentView = platformWindow.getClass().getMethod("getContentView");
                Object contentView = getContentView.invoke(platformWindow);
                Method getAwtView = contentView.getClass().getMethod("getAWTView");
                componentId = (Long) getAwtView.invoke(contentView);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            componentId = 0;
        }
        return componentId;
    }

}
