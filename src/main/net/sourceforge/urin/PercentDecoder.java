/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.nio.charset.Charset;

import static net.sourceforge.urin.CharacterSetMembershipFunction.HEX_DIGIT;

final class PercentDecoder {
    private final CharacterSetMembershipFunction characterSetMembershipFunction;

    PercentDecoder(final CharacterSetMembershipFunction characterSetMembershipFunction) {
        this.characterSetMembershipFunction = characterSetMembershipFunction;
    }

    boolean isMember(String candidateString) {
        boolean result = true;
        char[] candidateChars = candidateString.toCharArray();
        for (int i = 0; result && i < candidateChars.length; i++) {
            char candidateChar = candidateChars[i];
            if (isByte(candidateChars, i)) {
                byte firstByte = getByte(candidateChars, i);
                i = i + 2;
                if (firstByte < 0) {
                    i = i + 1;
                    if (isByte(candidateChars, i)) {
                        byte secondByte = getByte(candidateChars, i);
                        i = i + 2;
                        i = i + 1;
                        if (secondByte < 0) {
                            result = isByte(candidateChars, i);
                            i = i + 2;
                        } else {
                            result = true;
                        }
                    } else {
                        result = false;
                    }
                } else {
                    result = true;
                }
            } else {
                result = characterSetMembershipFunction.isMember(candidateChar);
            }
        }
        return result;
    }

    private static boolean isByte(char[] source, int startIndex) throws IllegalArgumentException {
        return !((source.length <= startIndex + 2) || !('%' == source[startIndex]))
                && HEX_DIGIT.isMember(source[startIndex + 1]) && HEX_DIGIT.isMember(source[startIndex + 2]);
    }

    private static byte getByte(char[] source, int startIndex) throws IllegalArgumentException {
        if (!('%' == source[startIndex]) || (source.length <= startIndex + 2)) {
            throw new IllegalArgumentException("Cannot extract a percent encoded byte from [" + new String(source) + "] starting at index [" + startIndex + "]");
        } else {
            return (byte) Integer.parseInt(new String(source, startIndex + 1, 2), 16);
        }
    }

    String decode(String encoded) throws IllegalArgumentException {
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[3];
        char[] candidateChars = encoded.toCharArray();
        for (int i = 0; i < candidateChars.length; i++) {
            char candidateChar = candidateChars[i];
            if ('%' == candidateChar) {
                buffer[0] = getByte(candidateChars, i);
                i = i + 2;
                if (buffer[0] < 0) {
                    i = i + 1;
                    buffer[1] = getByte(candidateChars, i);
                    i = i + 2;
                    if (buffer[1] < 0) {
                        i = i + 1;
                        buffer[2] = getByte(candidateChars, i);
                        i = i + 2;
                        result.append(new String(buffer, 0, 3, Charset.forName("UTF-8")));
                    } else {
                        result.append(new String(buffer, 0, 2, Charset.forName("UTF-8")));
                    }
                } else {
                    result.append(new String(buffer, 0, 1, Charset.forName("UTF-8")));
                }
            } else {
                result.append(candidateChar);
            }
        }
        return result.toString();
    }
}
