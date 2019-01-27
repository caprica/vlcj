/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded.fullscreen.windows;

/**
 * Encapsulation of window state.
 */
final class WindowState {

    private boolean maximized;

    private int style;

    private int exStyle;

    private int left;

    private int top;

    private int right;

    private int bottom;

    WindowState() {
    }

    boolean getMaximized() {
        return maximized;
    }

    void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    int getStyle() {
        return style;
    }

    void setStyle(int style) {
        this.style = style;
    }

    int getExStyle() {
        return exStyle;
    }

    void setExStyle(int exStyle) {
        this.exStyle = exStyle;
    }

    int getLeft() {
        return left;
    }

    void setLeft(int left) {
        this.left = left;
    }

    int getTop() {
        return top;
    }

    void setTop(int top) {
        this.top = top;
    }

    int getRight() {
        return right;
    }

    void setRight(int right) {
        this.right = right;
    }

    int getBottom() {
        return bottom;
    }

    void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return new StringBuilder(100)
            .append(getClass().getName()).append('[')
            .append("maximized=").append(maximized).append(',')
            .append("style=").append(style).append(',')
            .append("exStyle=").append(exStyle).append(',')
            .append("left=").append(left).append(',')
            .append("top=").append(top).append(',')
            .append("right=").append(right).append(',')
            .append("bottom=").append(bottom).append(']')
            .toString();
    }
}
