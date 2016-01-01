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

package uk.co.caprica.vlcj.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Private utility class to merge argument arrays.
 */
final class Arguments {

    private Arguments() {
    }

    /**
     * Merge two argument arrays.
     *
     * @param args first argument array, will never be <code>null</code>
     * @param extraArgs second argument array, may be <code>null</code>
     * @return
     */
    static final String[] mergeArguments(String[] args, String[] extraArgs) {
        if (extraArgs != null && extraArgs.length > 0) {
            List<String> combinedArgs = new ArrayList<String>(args.length + extraArgs.length);
            combinedArgs.addAll(Arrays.asList(args));
            combinedArgs.addAll(Arrays.asList(extraArgs));
            args = combinedArgs.toArray(args);
        }
        return args;
    }
}
