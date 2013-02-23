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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.logger;

import java.io.PrintWriter;
import java.util.Formatter;

/**
 * A very simple lightweight log system.
 * <p>
 * The log level can be changed by invoking {@link #setLevel(Level)}.
 * <p>
 * The log level can be configured at run-time by specifying a system property on the command-line,
 * for example:
 *
 * <pre>
 *   -Dvlcj.log=INFO
 * </pre>
 *
 * The log levels are defined in {@link Level}.
 */
public final class Logger {

    /**
     * Place-holder identifier.
     */
    private static final String PLACE_HOLDER = "{}";

    private static final Logger INSTANCE = new Logger();

    private final PrintWriter out = new PrintWriter(System.out);

    private final PrintWriter err = new PrintWriter(System.err);

    private Level threshold = Level.NONE;

    public enum Level {
        NONE, FATAL, ERROR, WARN, INFO, DEBUG, TRACE
    }

    private Logger() {
        String logProperty = System.getProperty("vlcj.log");
        if(logProperty != null) {
            threshold = Level.valueOf(logProperty);
        }
    }

    public static void setLevel(Level threshold) {
        INSTANCE.threshold = threshold;
    }

    public static Level level() {
        return INSTANCE.threshold;
    }

    public static void trace(String msg, Object... args) {
        if(Level.TRACE.compareTo(INSTANCE.threshold) <= 0) {
            out("TRACE", msg, null, args);
        }
    }

    public static void trace(String msg, Throwable t, Object... args) {
        if(Level.TRACE.compareTo(INSTANCE.threshold) <= 0) {
            out("TRACE", msg, t, args);
        }
    }

    public static void debug(String msg, Object... args) {
        if(Level.DEBUG.compareTo(INSTANCE.threshold) <= 0) {
            out("DEBUG", msg, null, args);
        }
    }

    public static void debug(String msg, Throwable t, Object... args) {
        if(Level.DEBUG.compareTo(INSTANCE.threshold) <= 0) {
            out("DEBUG", msg, t, args);
        }
    }

    public static void info(String msg, Object... args) {
        if(Level.INFO.compareTo(INSTANCE.threshold) <= 0) {
            out("INFO", msg, null, args);
        }
    }

    public static void info(String msg, Throwable t, Object... args) {
        if(Level.INFO.compareTo(INSTANCE.threshold) <= 0) {
            out("INFO", msg, t, args);
        }
    }

    public static void warn(String msg, Object... args) {
        if(Level.WARN.compareTo(INSTANCE.threshold) <= 0) {
            out("WARN", msg, null, args);
        }
    }

    public static void warn(String msg, Throwable t, Object... args) {
        if(Level.WARN.compareTo(INSTANCE.threshold) <= 0) {
            out("WARN", msg, t, args);
        }
    }

    public static void error(String msg, Object... args) {
        if(Level.ERROR.compareTo(INSTANCE.threshold) <= 0) {
            out("ERROR", msg, null, args);
        }
    }

    public static void error(String msg, Throwable t, Object... args) {
        if(Level.ERROR.compareTo(INSTANCE.threshold) <= 0) {
            out("ERROR", msg, t, args);
        }
    }

    public static void fatal(String msg, Object... args) {
        if(Level.FATAL.compareTo(INSTANCE.threshold) <= 0) {
            out("FATAL", msg, null, args);
        }
    }

    public static void fatal(String msg, Throwable t, Object... args) {
        if(Level.FATAL.compareTo(INSTANCE.threshold) <= 0) {
            out("FATAL", msg, t, args);
        }
    }

    private static void out(String level, String msg, Throwable t, Object... args) {
        PrintWriter out = INSTANCE.out;
        synchronized(out) {
            StackTraceElement el = getLine();
            Formatter formatter = new Formatter();
            try {
                String location = formatter.format("(%s:%d)", el.getFileName(), el.getLineNumber()).toString();
                out.printf("vlcj: %-46s | %-5s | %s%n", location, level, format(msg, args));
                out.flush();
                if(t != null) {
                    PrintWriter err = INSTANCE.err;
                    err.printf("vlcj: %-46s | %-5s | %s%n", location, level, t.getMessage());
                    err.flush();
                    t.printStackTrace();
                }
            }
            finally {
                formatter.close();
            }
        }
    }

    private static StackTraceElement getLine() {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] els = t.getStackTrace();
        return els[3]; // Take care!
    }

    /**
     * Format a string, such as "something{0}, another{1}", replacing the tokens with argument
     * values.
     *
     * @param msg message, including token place-holders
     * @param args values to substitute
     * @return formatted string
     */
    public static String format(String msg, Object... args) {
        if(args == null || args.length == 0 || msg == null) {
            return msg;
        }
        else {
            StringBuilder sb = new StringBuilder(msg.length() + args.length * 10);
            for(int current = 0, argIndex = 0; current < msg.length();) {
                int token = msg.indexOf(PLACE_HOLDER, current);
                if(token > -1) {
                    sb.append(msg.substring(current, token));
                    sb.append(args[argIndex ++ ]);
                    current = token + PLACE_HOLDER.length();
                }
                else {
                    sb.append(msg.substring(current));
                    break;
                }
            }
            return sb.toString();
        }
    }
}
