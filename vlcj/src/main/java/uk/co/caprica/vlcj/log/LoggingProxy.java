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
 * Copyright 2009, 2010 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 *
 */
public class LoggingProxy implements InvocationHandler {

  /**
   * 
   */
  private static final Logger LOG = Logger.getLogger(LoggingProxy.class);
  
  /**
   * 
   */
  private final Object target;
  
  /**
   * 
   * 
   * @param target
   */
  public LoggingProxy(Object target) {
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
    if(LOG.isTraceEnabled()) {LOG.trace("call " + method.getName() + ": " + Arrays.toString(args));}
  }

  /**
   * 
   * 
   * @param method
   * @param result
   */
  private void after(Method method, Object result) {
    if(LOG.isTraceEnabled()) {
      if(!method.getReturnType().equals(Void.TYPE)) {
        LOG.trace("return " + method.getName() + ": " + result);
      }
      else {
        LOG.trace("return " + method.getName() + ": void");
      }
    }
  }

  /**
   * 
   * 
   * @param method
   * @param t
   */
  private void exception(Method method, Throwable t) {
    if(LOG.isTraceEnabled()) {LOG.trace("exception " + method.getName() + ": " + t);}
  }
}
