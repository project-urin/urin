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

    static String randomDifferentTo(final String aString, final int size) {
        String random = random(size);
        return random.equals(aString) ? random + random(1) : random;
    }

    static String randomExcluding(final char excluded, final int size) {
        int maximumAttempts = 5;
        for (int i = 0; i < maximumAttempts; i++) {
            String random = random(size);
            if (random.indexOf(excluded) == -1) {
                return random;
            }
        }
        throw new RuntimeException("Couldn't make a random String excluding [" + excluded + "] in " + maximumAttempts + " attempts");
    }

    static String randomIncluding(final char included, final int size) {
        if (size == 0) {
            throw new IllegalArgumentException("Cannot create a String containing [" + included + "] of size " + size);
        }
        StringBuilder result = new StringBuilder();
        int includeAt = RANDOM.nextInt(size);
        for (int i = 0; i < size; i++) {
            if (i == includeAt) {
                result.append(included);
            } else {
                result.append(random(1));
            }
        }
        return result.toString();
    }

    static char aRandomChar() {
        return random(1).charAt(0);
    }
}
