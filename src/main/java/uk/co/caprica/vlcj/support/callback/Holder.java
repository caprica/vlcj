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
 * Copyright 2009-2025 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.support.callback;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A simple threadsafe value holder.
 *
 * @param <T> type of object being held
 */
public class Holder<T> implements Supplier<T> {

    private final AtomicReference<@Nullable T> reference = new AtomicReference<>();

    public final void set(T value) {
        reference.set(value);
    }

    @Override
    public final T get() {
        T value = reference.get();
        if (value != null) {
            return value;
        }
        throw new IllegalStateException("Value has not been initialised yet");
    }
}