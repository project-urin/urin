/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

public final class Scheme {

    private final String value;

    public Scheme(final String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Scheme must contain at least one character");
        }
        if (!isAlpha(name.charAt(0))) {
            throw new IllegalArgumentException("First character must be a-z or A-Z in scheme [" + name + "]");
        }
        for (int i = 1; i < name.length(); i++) {
            char testChar = name.charAt(i);
            if (!isAlpha(testChar) && !(isDigit(testChar)) && !(isPlus(testChar)) && !(isHyphen(testChar)) && !(isDot(testChar))) {
                throw new IllegalArgumentException("Character " + (i + 1) + " must be a-z, A-Z, 0-9, +, -, or . in scheme [" + name + "]");
            }
        }
        value = name.toLowerCase();
    }

    private boolean isPlus(final char testChar) {
        return testChar == '+';
    }

    private boolean isHyphen(final char testChar) {
        return testChar == '-';
    }

    private boolean isDot(final char testChar) {
        return testChar == '.';
    }

    private boolean isDigit(final char testChar) {
        return testChar >= '0' && testChar <= '9';
    }

    private static boolean isAlpha(final char testChar) {
        return (testChar >= 'a' && testChar <= 'z')
                || (testChar >= 'A' && testChar <= 'Z');
    }

    public String asString() {
        return value;
    }
}
