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
 * Copyright 2009-2017 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of dialog question types.
 */
public enum DialogQuestionType {

    NORMAL  (0),
    WARNING (1),
    CRITICAL(2);

    private static final Map<Integer, DialogQuestionType> INT_MAP = new HashMap<Integer, DialogQuestionType>();

    static {
        for (DialogQuestionType questionType : DialogQuestionType.values()) {
            INT_MAP.put(questionType.intValue, questionType);
        }
    }

    public static DialogQuestionType questionType(int intValue) {
        return INT_MAP.get(intValue);
    }

    private final int intValue;

    DialogQuestionType(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
