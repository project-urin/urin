/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public final class MoreRandomStringUtils {

    private static final Random RANDOM = new Random();

    private MoreRandomStringUtils() {
    }

    public static String aStringDifferentTo(final String aString) {
        final String random = aString();
        return random.equals(aString) ? random + RandomStringUtils.insecure().next(1) : random;
    }

    public static String aStringIncluding(final char included) {
        final StringBuilder result = new StringBuilder();
        final int includeAt = RANDOM.nextInt(5);
        for (int i = 0; i < 5; i++) {
            if (i == includeAt) {
                result.append(included);
            } else {
                result.append(RandomStringUtils.insecure().next(1));
            }
        }
        return result.toString();
    }

    public static char aChar() {
        return RandomStringUtils.insecure().next(1).charAt(0);
    }

    public static String aString() {
        return RandomStringUtils.insecure().next(5);
    }
}
