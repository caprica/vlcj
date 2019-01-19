package uk.co.caprica.vlcj.runtime.osx;

import com.sun.jna.Native;

import java.awt.*;
import java.lang.reflect.Method;

public final class OsxComponentId {

    public static long getOsxComponentId(Window window) {
        try {
            return Native.getComponentID(window);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            Method getPeer = Window.class.getMethod("getPeer");
            Object peer = getPeer.invoke(window);
            Method getPlatformWindow = peer.getClass().getMethod("getPlatformWindow");
            Object platformWindow = getPlatformWindow.invoke(peer);
            Method getContentView = platformWindow.getClass().getMethod("getContentView");
            Object contentView = getContentView.invoke(platformWindow);
            Method getAwtView = contentView.getClass().getMethod("getAWTView");
            return (Long) getAwtView.invoke(contentView);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
