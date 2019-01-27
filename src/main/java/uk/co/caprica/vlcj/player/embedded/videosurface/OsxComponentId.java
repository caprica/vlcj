/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.videosurface;

import com.sun.jna.Native;

import java.awt.*;
import java.lang.reflect.Method;

final class OsxComponentId {

    static long getOsxComponentId(Component component) {
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

    private OsxComponentId() {
    }

}
