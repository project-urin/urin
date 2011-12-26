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

import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class MoreRandomStringUtils {

    private static final Random RANDOM = new Random();

    static String aStringDifferentTo(final String aString) {
        String random = aString();
        return random.equals(aString) ? random + random(1) : random;
    }

    static String aStringExcluding(final char excluded) {
        int maximumAttempts = 5;
        for (int i = 0; i < maximumAttempts; i++) {
            String random = aString();
            if (random.indexOf(excluded) == -1) {
                return random;
            }
        }
        throw new RuntimeException("Couldn't make a random String excluding [" + excluded + "] in " + maximumAttempts + " attempts");
    }

    static String aStringIncluding(final char included) {
        StringBuilder result = new StringBuilder();
        int includeAt = RANDOM.nextInt(5);
        for (int i = 0; i < 5; i++) {
            if (i == includeAt) {
                result.append(included);
            } else {
                result.append(random(1));
            }
        }
        return result.toString();
    }

    static char aChar() {
        return random(1).charAt(0);
    }

    static String aString() {
        return random(5);
    }
}
