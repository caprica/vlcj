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

package uk.co.caprica.vlcj.binding;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
class LoggingProxy implements InvocationHandler {

    /**
     * Log.
     */
    private final Logger logger = LoggerFactory.getLogger(LoggingProxy.class);

    /**
     *
     */
    private final LibVlc target;

    /**
     *
     *
     * @param target
     */
    LoggingProxy(LibVlc target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            before(method, args);
            result = method.invoke(target, args);
            after(method, result);
        }
        catch(InvocationTargetException e) {
            exception(method, e.getCause());
            throw e;
        }
        return result;
    }

    /**
     *
     *
     * @param method
     * @param args
     */
    private void before(Method method, Object[] args) {
        logger.trace("call {}: {}", method.getName(), Arrays.toString(args));
    }

    /**
     *
     *
     * @param method
     * @param result
     */
    private void after(Method method, Object result) {
        if(!method.getReturnType().equals(Void.TYPE)) {
            logger.trace("return {}: {}", method.getName(), result);
        }
        else {
            logger.trace("return {}: void", method.getName());
        }
    }

    /**
     *
     *
     * @param method
     * @param t
     */
    private void exception(Method method, Throwable t) {
        logger.trace("exception {}: {}", method.getName(), t);
    }
}
