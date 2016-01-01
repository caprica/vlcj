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
 * Copyright 2009-2016 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.player.embedded;

import java.util.HashMap;
import java.util.Map;

import uk.co.caprica.vlcj.runtime.RuntimeType;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * An implementation of a runtime strategy that picks a strategy implementation from those registered
 * based on the current runtime operating system.
 * <p>
 * Applications are expected to sub-class this class and invoke {@link #setStrategy(RuntimeType, FullScreenStrategy)}
 * as needed in the constructor.
 */
public abstract class AdaptiveRuntimeFullScreenStrategy implements FullScreenStrategy {

    /**
     * Map of registered strategies, by runtime.
     */
    private final Map<RuntimeType,FullScreenStrategy> strategies = new HashMap<RuntimeType,FullScreenStrategy>();

    @Override
    public final void enterFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if(strategy != null) {
            strategy.enterFullScreenMode();
        }
    }

    @Override
    public final void exitFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if(strategy != null) {
            strategy.exitFullScreenMode();
        }
    }

    @Override
    public final boolean isFullScreenMode() {
        FullScreenStrategy strategy = strategy();
        if(strategy != null) {
            return strategy.isFullScreenMode();
        }
        else {
            return false;
        }
    }

    /**
     * Set a strategy for a particular runtime operating system type.
     *
     * @param runtimeType runtime type
     * @param strategy strategy
     */
    protected final void setStrategy(RuntimeType runtimeType, FullScreenStrategy strategy) {
        strategies.put(runtimeType, strategy);
    }

    /**
     * Get the strategy for the current runtime.
     *
     * @return strategy, or <code>NULL</code> if no strategy registered for the current runtime
     */
    private FullScreenStrategy strategy() {
        return strategies.get(RuntimeUtil.runtimeType());
    }
}
