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

package uk.co.caprica.vlcj.test.bug.lua;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

/**
 * This simple test case is used to trigger a native crash under the following conditions:
 * <ul>
 *   <li>32-bit Ubuntu, and;</li>
 *   <li>32-bit Java7 (official JDK or OpenJDK), and;</li>
 *   <li>LUA as packaged by Ubuntu, and;</li>
 *   <li>Execute compiled LUA script.</li>
 * </ul>
 * More information is available at <a href="https://github.com/caprica/vlcj/issues/62">Github</a>.
 * <p>
 * Compile the lua source script to bytecode with:
 * <pre>
 * luac -o test.luac test.lua
 * </pre>
 * <p>
 * Workarounds are:
 * <ul>
 *   <li>64-bit Ubuntu, or;</li>
 *   <li>Java6 (official JDK or OpenJDK), or;</li>
 *   <li>LUA as built from vanilla source from lua.org, or;</li>
 *   <li>Execute un-compiled source script.</li>
 * </ul>
 */
public class LuaUbuntuJava7Test {

    /**
     * Optional explicit path to the lua shared object.
     */
    private static final String LUA_PATH = null;

    /**
     * Path to the 64-bit lua shared object, if needed.
     */
//    private static final String LUA_PATH = "/usr/lib/x86_64-linux-gnu";

    /**
     * Path to the 64-bit lua shared object, if needed.
     */
//    private static final String LUA_PATH = "/usr/lib/i386-linux-gnu";

    /**
     *
     */
    private static final String SOURCE_SCRIPT = "src/test/resources/lua/test.lua";

    /**
     *
     */
//    private static final String BINARY_SCRIPT = "src/test/resources/lua/test-64.luac";

    /**
     *
     */
    private static final String BINARY_SCRIPT = "src/test/resources/lua/test-32.luac";

    /**
     * Minimal bindings to the native lua library.
     */
    public interface LibLua extends Library {

        LibLua INSTANCE = Native.load("lua5.1", LibLua.class);

        int LUA_MULTRET = -1;

        Pointer luaL_newstate();

        void luaL_openlibs(Pointer instance);

        int luaL_loadfile(Pointer pointer, String filename);

        int lua_pcall(Pointer pointer, int nargs, int nresults, int errfunc);

        void lua_close(Pointer instance);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("java.version=" + System.getProperty("java.version"));

        if (LUA_PATH != null) {
            NativeLibrary.addSearchPath("lua5.1", LUA_PATH);
        }

        LibLua lua = LibLua.INSTANCE;

        Pointer instance = lua.luaL_newstate();
        System.out.println("instance: " + instance);

        lua.luaL_openlibs(instance);

        int loadFile;
        int pcall;

        System.out.println();

        // Test the uncompiled script, this works...
        System.out.println("Testing: " + SOURCE_SCRIPT);

        loadFile = lua.luaL_loadfile(instance, SOURCE_SCRIPT);
        System.out.println("loadFile: " + loadFile);

        pcall = lua.lua_pcall(instance, 0, LibLua.LUA_MULTRET, 0);
        System.out.println("pcall: " + pcall);

        System.out.println();

        // Test the compiled script, this triggers the fatal crash...
        System.out.println("Testing: " + BINARY_SCRIPT);

        loadFile = lua.luaL_loadfile(instance, BINARY_SCRIPT);
        System.out.println("loadFile: " + loadFile);

        pcall = lua.lua_pcall(instance, 0, LibLua.LUA_MULTRET, 0);
        System.out.println("pcall: " + pcall);

        lua.lua_close(instance);

        System.out.println();

        System.out.println("Finished successfully.");
    }
}
