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

package uk.co.caprica.vlcj.component;

import java.awt.*;

/**
 * A layout manager that keeps a single component at its preferred size centered within its container.
 */
final class CallbackVideoSurfaceLayoutManager implements LayoutManager {

    private Component component;

    CallbackVideoSurfaceLayoutManager() {
    }

    @Override
    public void addLayoutComponent(String s, Component component) {
        this.component = component;
    }

    @Override
    public void removeLayoutComponent(Component component) {
        this.component = null;
    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        return component != null ? component.getPreferredSize() : new Dimension(0, 0);
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        return component != null ? component.getPreferredSize() : new Dimension(0, 0);
    }

    @Override
    public void layoutContainer(Container container) {
        if (component != null) {
            Dimension size = component.getPreferredSize();
            int x = (container.getWidth() - size.width) / 2;
            int y = (container.getHeight() - size.height) / 2;
            component.setBounds(x, y, size.width, size.height);
        }
    }

}
