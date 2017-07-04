/*
 * Copyright 2017 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.sourceforge.urin;

import java.nio.charset.Charset;
import java.util.Locale;

import static net.sourceforge.urin.CharacterSetMembershipFunction.ALL_CHARACTERS;
import static net.sourceforge.urin.CharacterSetMembershipFunction.NO_CHARACTERS;

final class PercentEncoder {
    static final PercentEncoder ENCODE_NOTHING = new PercentEncoder(ALL_CHARACTERS);
    static final PercentEncoder ENCODE_EVERYTHING = new PercentEncoder(NO_CHARACTERS);

    private static final Locale NO_LOCALISATION = null;
    private final CharacterSetMembershipFunction nonPercentEncodedCharacterSet;

    PercentEncoder(final CharacterSetMembershipFunction nonPercentEncodedCharacterSet) {
        this.nonPercentEncodedCharacterSet = nonPercentEncodedCharacterSet;
    }

    PercentEncoder additionallyEncoding(final char additionallyEncodedCharacter) {
        return new PercentEncoder(nonPercentEncodedCharacterSet.remove(additionallyEncodedCharacter));
    }

    String encode(final String notEncoded) {
        StringBuilder result = new StringBuilder();
        for (byte character : notEncoded.getBytes(Charset.forName("UTF-8"))) {
            if (nonPercentEncodedCharacterSet.isMember((char) character)) {
                result.append((char) character);
            } else {
                result.append(percentEncode(character));
            }
        }
        return result.toString();
    }

    private static String percentEncode(final byte character) {
        return String.format(NO_LOCALISATION, "%%%02X", character);
    }

    private static final byte BINARY_1000_0000 = -128;
    private static final byte BINARY_1100_0000 = -64;
    private static final byte BINARY_1110_0000 = -32;
    private static final byte BINARY_1111_0000 = -16;

    private static byte getByte(char[] source, int startIndex) throws IllegalArgumentException, ParseException {
        if (source.length <= startIndex + 2 || !('%' == source[startIndex])) {
            throw new ParseException("Cannot extract a percent encoded byte from [" + new String(source) + "] starting at index [" + startIndex + "]");
        } else {
            return (byte) Integer.parseInt(new String(source, startIndex + 1, 2), 16);
        }
    }

    String decode(String encoded) throws IllegalArgumentException, ParseException {
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[4];
        char[] candidateChars = encoded.toCharArray();
        for (int i = 0; i < candidateChars.length; i++) {
            char candidateChar = candidateChars[i];
            if ('%' == candidateChar) {
                buffer[0] = getByte(candidateChars, i);
                final int byteCount;
                if ((buffer[0] & BINARY_1000_0000) == 0) {
                    byteCount = 1;
                } else if ((buffer[0] & BINARY_1111_0000) == BINARY_1111_0000) {
                    byteCount = 4;
                } else if ((buffer[0] & BINARY_1110_0000) == BINARY_1110_0000) {
                    byteCount = 3;
                } else if ((buffer[0] & BINARY_1100_0000) == BINARY_1100_0000) {
                    byteCount = 2;
                } else {
                    throw new ParseException("First byte of a percent encoded character must begin 0, 11, 111, or 1111, but was " + buffer[0]);
                }
                for (int j = 1; j < byteCount; j++) {
                    buffer[j] = getByte(candidateChars, i + (3 * j));
                }
                i = i + (3 * byteCount) - 1;
                result.append(new String(buffer, 0, byteCount, Charset.forName("UTF-8")));
            } else {
                if (nonPercentEncodedCharacterSet.isMember(candidateChar)) {
                    result.append(candidateChar);
                } else {
                    throw new ParseException("Invalid character [" + candidateChar + "] - must be " + nonPercentEncodedCharacterSet.describe());
                }
            }
        }
        return result.toString();
    }

    public boolean isMember(final String string) {
        try {
            decode(string);
            return true;
        } catch (final ParseException e) {
            return false;
        }
    }


}
