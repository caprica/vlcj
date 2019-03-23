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

package uk.co.caprica.vlcj.factory;

/**
 * Exception thrown if not all methods in the native library could be mapped at run-time.
 * <p>
 * When using JNA direct mapping, if any method in the implementation class declared native does not exist in the native
 * library loaded at run-time, an exception will be thrown and further because this mapping is done during class static
 * initialisation what will actually be thrown is {@link NoClassDefFoundError} exception.
 * <p>
 * This is extremely unfortunate as that exception can not, for historical reasons, have a root cause exception.
 * <p>
 * So we make the reasonable assumption when registering the direct-mapped native library that if NoClassDefFoundError
 * occurs this is the reason.
 */
public class NativeLibraryMappingException extends RuntimeException {

    public NativeLibraryMappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
